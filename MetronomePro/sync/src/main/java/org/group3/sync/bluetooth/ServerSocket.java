package org.group3.sync.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.group3.sync.Peer;
import org.group3.sync.ServerActionListener;
import org.group3.sync.ServerInfo;
import org.group3.sync.exception.ErrorCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by summer on 10/12/16.
 */

class ServerSocket implements Runnable{
	private static final String TAG = "ServerSocket";

	private final ArrayList<ConnectedClient> mDevices;
	private BluetoothServerSocket mServerSocket;
	private ServerActionListener mListener;

	ServerSocket(final BluetoothAdapter bluetoothAdapter, ServerActionListener listener){
		mListener = listener;
		mDevices = new ArrayList<>();
		BluetoothServerSocket tmp = null;
		try {
			tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("bluetooth test", Constants.APP_ID);
		} catch (IOException e) {
			notifyError(ErrorCode.SOCKET_OPEN_UNABLE);
		}
		mServerSocket = tmp;
		notifyServerStarted(new ServerInfo() {
			@Override
			public String getServerName() {
				return bluetoothAdapter.getName();
			}
		});
		new Thread(this).start();
	}

	@Override
	public void run() {
		BluetoothSocket socket;
		while (true) {
			try {
				Log.d(TAG,"listening");
				socket = mServerSocket.accept();
			} catch (IOException e) {
				notifyError(ErrorCode.SERVER_CANNOT_LISTEN);
				break;
			}
			if (socket != null) {
				synchronized (mDevices) {
					mDevices.add(new ConnectedClient(socket));
				}
			}
		}
	}

	private void notifyError(ErrorCode error){
		mListener.onError(error);
	}

	private void notifyServerStarted(ServerInfo serverInfo){
		mListener.onServerStarted(serverInfo);
	}

	private void notifyServerStopped(){
		mListener.onServerStopped();
	}

	private void notifyClientConnected(Peer peer){
		mListener.onClientConnected(peer);
	}

	private void notifyClientSynchronized(Peer peer){
		mListener.onClientSynchronized(peer);
	}

	private void notifyClientDisconnected(Peer peer){
		mListener.onClientDisconnected(peer);
	}

	void stop(){
		notifyServerStopped();
		try {
			mServerSocket.close();
		}catch (IOException e){
			notifyError(ErrorCode.SOCKET_CLOSE_UNABLE);
		}
		synchronized (mDevices) {
			for (ConnectedClient device : mDevices) {
				Log.d("test", "" + device.peer);
				device.stop();
			}
		}
	}

	void broadcastStart(int bpm,String rhythm, Object playlist,long startTime){
		synchronized (mDevices) {
			for (ConnectedClient client : mDevices) {
				sendStart(client, bpm, rhythm, playlist, startTime);
			}
		}
	}

	void broadcastStart(long time){
		synchronized (mDevices) {
			for (ConnectedClient client : mDevices) {
				sendStart(client, time);
			}
		}
	}

	void broadcastStop(){
		synchronized (mDevices) {
			for (ConnectedClient client : mDevices) {
				JSONObject info = new JSONObject();
				try {
					info.put(Constants.KEY_COMMAND, Constants.MESSAGE_STOP);
					byte[] command = info.toString().getBytes();
					client.write(command);
				} catch (JSONException e) {
					Log.e(TAG, e.toString());
				}
			}
		}
	}

	void broadcastSet(int bpm,String rhythm, Object playlist){
		synchronized (mDevices) {
			for (ConnectedClient client : mDevices) {
				sendSet(client, bpm, rhythm, playlist);
			}
		}
	}

	void broadcastGeneralMessage(byte[] msg){
		synchronized (mDevices) {
			for (ConnectedClient client : mDevices) {
				client.write(msg);
			}
		}
	}

	void sendSetTo(Peer peer, int bpm, String rhythm, Object playlist){
		synchronized (mDevices) {
			for (ConnectedClient client : mDevices) {
				Log.d(TAG, "client: " + client.peer.toString() + " peer: " + peer.toString());
				if (client.peer.equals(peer)) {
					sendSet(client, bpm, rhythm, playlist);
				}
			}
		}
	}

	void sendStartTo(Peer peer, int bpm, String rhythm, Object playlist, long time){
		synchronized (mDevices) {
			for (ConnectedClient client : mDevices) {
				if (client.peer.equals(peer)) {
					sendStart(client, bpm, rhythm, playlist, time);
				}
			}
		}
	}

	void sendStartTo(Peer peer, long time){
		synchronized (mDevices) {
			for (ConnectedClient client : mDevices) {
				if (client.peer.equals(peer)) {
					sendStart(client, time);
				}
			}
		}
	}

	private void sendSet(ConnectedClient client, int bpm, String rhythm, Object playlist){
		JSONObject info = new JSONObject();
		try {
			info.put(Constants.KEY_COMMAND, Constants.MESSAGE_SET);
			info.put(Constants.KEY_BPM, bpm);
			info.put(Constants.KEY_RHYTHM,rhythm);
			info.put("Playlist", playlist);
			byte[] command = info.toString().getBytes();
			client.write(command);
		}catch (JSONException e){
			Log.e(TAG, e.toString());
		}
	}

	private void sendStart(ConnectedClient client, int bpm, String rhythm, Object playlist, long time){
		JSONObject info = new JSONObject();
		try {
			info.put(Constants.KEY_COMMAND, Constants.MESSAGE_START_CONFIG);
			info.put(Constants.KEY_BPM, bpm);
			info.put(Constants.KEY_RHYTHM,rhythm);
			info.put("Playlist",playlist);
			info.put(Constants.KEY_TIME, time);
			byte[] command = info.toString().getBytes();
			client.write(command);
		}catch (JSONException e){
			Log.e(TAG, e.toString());
		}
	}

	private void sendStart(ConnectedClient client, long time){
		JSONObject info = new JSONObject();
		try {
			info.put(Constants.KEY_COMMAND, Constants.MESSAGE_START);
			info.put(Constants.KEY_TIME, time);
			byte[] command = info.toString().getBytes();
			client.write(command);
		}catch (JSONException e){
			Log.e(TAG, e.toString());
		}
	}



	//local methods-----------------------------------------------------------
	private class ConnectedClient extends ConnectedDevice{
		private long sendTime;
		private static final String mTAG = "connected@ServerSocket";
		public Peer peer;

		ConnectedClient(BluetoothSocket socket){
			super(socket,mTAG);
			new Thread(this).start();
			BluetoothDevice clientDevice = socket.getRemoteDevice();
			peer = new Peer(clientDevice.getName(), clientDevice.getAddress(), clientDevice);
			Log.d(TAG,clientDevice+" connected");
			notifyClientConnected(peer);
		}

		private void sendServerTime(long time){
			JSONObject info = new JSONObject();
			try {
				info.put(Constants.KEY_COMMAND, Constants.MESSAGE_SERVER_TIME);
				info.put(Constants.KEY_TIME, time);
				byte[] command = info.toString().getBytes();
				write(command);
			}catch (JSONException e){
				Log.e(TAG, e.toString());
			}
		}

		private void sendDelay(long delay,long now){
			JSONObject info = new JSONObject();
			try {
				info.put(Constants.KEY_COMMAND, Constants.MESSAGE_DELAY);
				info.put(Constants.KEY_DELAY, delay);
				info.put(Constants.KEY_TIME, now);
				byte[] command = info.toString().getBytes();
				write(command);
			}catch (JSONException e){
				Log.e(TAG, e.toString());
			}
		}

		@Override
		protected void messageHandler(byte[] msg, int length) {
			Util.ParsedInfo info = new Util.ParsedInfo(new String(Arrays.copyOfRange(msg, 0, length)));
			byte tag = info.getCommand();
			switch (tag){
				case Constants.MESSAGE_CONNECTION_OK:
					Log.d(TAG, "received ok");
					notifyClientSynchronized(peer);
					break;
				case Constants.MESSAGE_REQUEST_SERVER_TIME:
					//save send start
					//send current server time
					sendTime = System.currentTimeMillis();
					sendServerTime(sendTime);
					break;
				case Constants.MESSAGE_ACT_SERVER_TIME:
					//calculate delay
					//send delay
					long now = System.currentTimeMillis();
					long delay = now-sendTime;
					sendDelay(delay,now);
					//Log.d(TAG, ""+(delay + sendTime));
					break;
				default:
					break;
			}
		}

		@Override
		protected void errorHandler(ErrorCode error) {
			//notifyError(error);
			notifyClientDisconnected(peer);
			synchronized (mDevices) {
				mDevices.remove(this);
			}
		}

	}
}
