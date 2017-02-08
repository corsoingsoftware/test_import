package org.group3.sync.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.group3.sync.ClientActionListener;
import org.group3.sync.MetroConfig;
import org.group3.sync.Peer;
import org.group3.sync.exception.ErrorCode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

/**
 * ClientSocket Thread which reads from and writes to the socket
 * @author DAOHONG LI
 */
class ClientSocket implements Runnable{
	private static final String TAG = "ClientSocket";

	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice mBluetoothDevice;
	private BluetoothSocket mBluetoothSocket;
	private Connected mConnected;
	private ClientActionListener mListener;
	private long mTimeDifference = 0;
	private boolean isConnected = false;

	/**
	 *
	 * @param device selected device passed in {@link ManagerClient#connectToPeer(Peer)}
	 * @param adapter bluetooth adapter service
	 * @param listener listener must be implemented by the user
	 */
	ClientSocket(BluetoothDevice device, BluetoothAdapter adapter, ClientActionListener listener){
		mBluetoothDevice = device;
		mBluetoothAdapter = adapter;
		mListener = listener;
		Log.d(TAG,"start ClientSocket ");
		try {
			mBluetoothSocket = device.createRfcommSocketToServiceRecord(Constants.APP_ID);
		} catch (IOException e) {
			notifyError(ErrorCode.SOCKET_OPEN_UNABLE);
		}
		new Thread(this).start();
	}

	@Override
	public void run(){
		mBluetoothAdapter.cancelDiscovery();
		try {
			Log.d(TAG,"connecting "+mBluetoothDevice.getName());
			notifyConnecting();
			mBluetoothSocket.connect();
		} catch (IOException connectException) {
			notifyError(ErrorCode.CONNECT_UNABLE);
			Log.e(TAG, "Unable to connect socket",connectException);
			try {
				mBluetoothSocket.close();
			} catch (IOException closeException) {
				notifyError(ErrorCode.SOCKET_CLOSE_UNABLE);
				Log.e(TAG, "Unable to close socket",closeException);
			}
			return;
		}
		notifyConnected();
		mConnected = new Connected(mBluetoothSocket);
		Log.d(TAG,"socket number :"+mBluetoothSocket);
	}

	/**
	 * notify on connecting action
	 */
	private void notifyConnecting(){
		mListener.onConnecting();
	}

	/**
	 * notify on connected action
	 */
	private void notifyConnected(){
		isConnected = true;
		mListener.onConnected();
	}

	/**
	 * notify on synchronized action
	 * @param timeDifference time difference between two devices
	 */
	private void notifySynchronized(long timeDifference){
		mListener.onSynchronized(timeDifference);
	}

	/**
	 * notify on start action
	 * @param config the metronome config {@link MetroConfig}
	 * @param time the time which was sent by the server in milliseconds
	 * @param delay delay of this message, calculated by using time difference
	 */
	private void notifyStart(MetroConfig config, long time, long delay){
		mListener.onReceiveStart(config, time, delay);
	}

	/**
	 * notify on stop action
	 */
	private void notifyStop(){
		mListener.onReceiveStop();
	}

	/**
	 * notify on set metronome config
	 * @param config the config info which was sent by the server
	 */
	private void notifySet(MetroConfig config){
		mListener.onReceiveSet(config);
	}

	/**
	 * notify on general message
	 * @param msg a general message in byte array
	 */
	private void notifyGeneralMessage(byte[] msg){
		mListener.onReceiveGeneralMessage(msg);
	}

	/**
	 * notify on error
	 * @param error an error {@link ErrorCode}
	 */
	private void notifyError(ErrorCode error){
		mListener.onError(error);
	}

	/**
	 * notify on disconnected action
	 */
	private void notifyDisconnected(){
		isConnected = false;
		mListener.onDisconnected();
	}

	boolean isConnected(){
		return isConnected;
	}

	/**
	 * stop the connection
	 */
	void terminate(){
		if(mConnected != null)
			mConnected.stop();
	}

	/**
	 * private class to handler a connection
	 */
	private class Connected extends ConnectedDevice{
		private static final long DEFAULT_ERROR = -1000;
		private static final String mTAG = "connected@ClientSocket";

		private long currentTimeDifference;
		private long sendTime;
		private long mError = DEFAULT_ERROR;
		private Thread handshake;
		private KeepAlive keepAlive;

		/**
		 * private class which keeps alive the connection
		 */
		private class KeepAlive extends Thread{
			private boolean alive = true;
			@Override
			public void run() {
				while(alive) {
					try {
						requestServerTime();
						Thread.sleep(5000);
					}catch (Exception e){
						return;
					}
				}
			}

			void terminate(){
				alive = false;
			}
		}

		/**
		 *
		 * @param socket a opened socket
		 */
		Connected(BluetoothSocket socket){
			super(socket,mTAG);
			handshake = new Thread(new Runnable() {
				@Override
				public void run() {
					for(int i=0;i<Constants.HANDSHAKE_TIMES;i++) {
						try {
							requestServerTime();
							Thread.sleep(100);
						}catch (Exception e){
							return;
						}
					}
					notifySynchronized(mTimeDifference);
					sendConnectionOk();
					keepAlive.start();
				}
			});
			keepAlive = new KeepAlive();
			new Thread(this).start();
			handshake.start();
		}

		/**
		 * stop current thread
		 */
		@Override
		public void stop(){
			keepAlive.terminate();
			super.stop();
		}

		/**
		 * request to have server time
		 */
		private void requestServerTime(){
			sendTime = System.currentTimeMillis();
			JSONObject info = new JSONObject();
			try {
				info.put(Constants.KEY_COMMAND, Constants.MESSAGE_REQUEST_SERVER_TIME);
				byte[] command = info.toString().getBytes();
				write(command);
			}catch (JSONException e){
				Log.e(TAG,e.toString());
			}
		}

		/**
		 * send acknowledge of the time
		 */
		private void sendActServerTime(){
			JSONObject info = new JSONObject();
			try {
				info.put(Constants.KEY_COMMAND, Constants.MESSAGE_ACT_SERVER_TIME);
				byte[] command = info.toString().getBytes();
				write(command);
			}catch (JSONException e){
				Log.e(TAG,e.toString());
			}
		}

		/**
		 * send connection ok
		 */
		private void sendConnectionOk(){
			JSONObject info = new JSONObject();
			try {
				info.put(Constants.KEY_COMMAND, Constants.MESSAGE_CONNECTION_OK);
				byte[] command = info.toString().getBytes();
				write(command);
			}catch (JSONException e){
				Log.e(TAG,e.toString());
			}
		}

		/**
		 * protected method which handlers the errors and terminate the manager on error
		 * @param error error message
		 */
		@Override
		protected void errorHandler(ErrorCode error){
			if(error.equals(ErrorCode.DISCONNECTED)) {
				notifyDisconnected();
				if(keepAlive.isAlive())
					keepAlive.terminate();
			}else {
				notifyError(error);
			}
		}

		/**
		 * protected method which handlers the message
		 * @param msg received messages in byte
		 * @param length length of the message
		 */
		@Override
		protected void messageHandler(byte[] msg, int length){
			long currentTime = System.currentTimeMillis();
			byte tag;
			Util.ParsedInfo info = new Util.ParsedInfo(new String(Arrays.copyOfRange(msg, 0, length)));
			try {
				tag = info.getCommand();
			}catch (NullPointerException e){
				tag = Constants.MESSAGE_GENERAL;
			}
			switch (tag){
				case Constants.MESSAGE_START_CONFIG: {

					int bpm = info.getBPM();
					String rhythm = info.getRhythm()+"";
					Object playlist = info.getPlaylist();
					long serverTime = info.getTime();
					long delay = currentTime - mTimeDifference - serverTime;

					MetroConfig config = new MetroConfig(bpm, rhythm, playlist);
					notifyStart(config, serverTime, delay);
					break;
				}
				case Constants.MESSAGE_START:{
					long serverTime = info.getTime();
					long delay = currentTime - mTimeDifference - serverTime;
					notifyStart(null, serverTime, delay);
					break;
				}
				case Constants.MESSAGE_SET:{
					int bpm = info.getBPM();
					String rhythm = info.getRhythm()+"";
					Object playlist = info.getPlaylist();
					MetroConfig config = new MetroConfig(bpm, rhythm, playlist);
					notifySet(config);
					break;
				}
				case Constants.MESSAGE_STOP: {
					notifyStop();
					break;
				}
				case Constants.MESSAGE_GENERAL:{
					notifyGeneralMessage(Arrays.copyOfRange(msg, 0, length));
					break;
				}
				case Constants.MESSAGE_SERVER_TIME: {
					long serverTime = info.getTime();
					currentTimeDifference = currentTime - serverTime;
					/*Log.d(TAG, "Sync Begin++++++++++++++++++++++++++++++++++++++++++++++++++++");
					Log.d(TAG, "ON SERVER TIME current: " + currentTime + " diff: " + currentTimeDifference);*/
					sendTime = System.currentTimeMillis();
					sendActServerTime();
					break;
				}
				case Constants.MESSAGE_DELAY: {
					long localDelay = currentTime - sendTime;
					long serverDelay = info.getDelay();
					long serverTime = info.getTime();
					long error = currentTime - currentTimeDifference - serverTime;
					//Log.d(TAG, "ON DELAY currentTime: " + (currentTime - currentTimeDifference) + " serverTime: " + serverTime);
					if (mError == -1000) {
						mError = error;
					}
					if (Math.abs(error) <= Math.abs(mError)) {
						mError = error;
						mTimeDifference = currentTimeDifference + mError - (localDelay + serverDelay) / 4;
						Log.d(TAG, "new Diff: " + mTimeDifference);
						//notifySynchronized(mTimeDifference);
					}
					//Log.i(TAG, "time error: " + mError + " diff: " + mTimeDifference + " delay: " + serverDelay + " local delay: " + localDelay);
					//currentTimeDifference = currentTimeDifference - delay - delay/2 ;
					//Log.d(TAG, "Sync End-----------------------------------------------------\n");
					//Log.d(TAG, "synced time "+(System.currentTimeMillis()-currentTimeDifference));
					break;
				}
				default:
					break;
			}
		}
	}

}
