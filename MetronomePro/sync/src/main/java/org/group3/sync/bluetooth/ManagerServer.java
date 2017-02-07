package org.group3.sync.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

import org.group3.sync.MetroConfig;
import org.group3.sync.Peer;
import org.group3.sync.Server;
import org.group3.sync.exception.ConnectivityException;
import org.group3.sync.ServerActionListener;
import org.group3.sync.exception.ErrorCode;


class ManagerServer extends Receiver implements Server{
	private static final String TAG = "ManagerServer";
	private ServerSocket mServer;
	private ServerActionListener mListener;

	ManagerServer(Context ctx, ServerActionListener listener) throws ConnectivityException {
		super(ctx);
		mListener = listener;

		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		mContext.startActivity(discoverableIntent);

		mServer = new ServerSocket(mBluetoothAdapter, mListener);
	}

	@Override
	public void broadcastStart(MetroConfig config, long startTime) {
		mServer.broadcastStart(config.getBpm(), config.getRhythm(), config.getPlaylist(), startTime);
	}

	@Override
	public void broadcastStart(long startTime) {
		mServer.broadcastStart(startTime);
	}

	@Override
	public void broadcastStop() {
		mServer.broadcastStop();
	}

	@Override
	public void broadcastSet(MetroConfig config) {
		mServer.broadcastSet(config.getBpm(),config.getRhythm(), config.getPlaylist());
	}

	@Override
	public void broadcastGeneralMessage(byte[] message) {
		mServer.broadcastGeneralMessage(message);
	}

	@Override
	public void sendSetTo(Peer peer, MetroConfig config) {
		mServer.sendSetTo(peer,config.getBpm(),config.getRhythm(), config.getPlaylist());
	}

	@Override
	public void sendStartTo(Peer peer, MetroConfig config, long startTime) {
		mServer.sendStartTo(peer,config.getBpm(),config.getRhythm(), config.getPlaylist(),startTime);
	}

	@Override
	public void sendStartTo(Peer peer, long startTime) {
		mServer.sendStartTo(peer,startTime);
	}


	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		switch (action){
			case BluetoothAdapter.ACTION_STATE_CHANGED:{
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,-1);
				switch (state){
					case BluetoothAdapter.STATE_ON:{
						break;
					}
					case BluetoothAdapter.STATE_OFF:{
						mListener.onError(ErrorCode.ADAPTER_NOT_ACTIVATED);
						mListener.onServerStopped();
						break;
					}
					default:{
						break;
					}
				}
				break;
			}
			default:{
				break;
			}
		}
	}

	@Override
	public void onActivityResume() {
		registerReceiver();
	}

	@Override
	public void onActivityPaused() {
		unregisterReceiver();
	}

	@Override
	public void terminate(){
		clean();
		if(mServer != null)
			mServer.stop();
	}
}
