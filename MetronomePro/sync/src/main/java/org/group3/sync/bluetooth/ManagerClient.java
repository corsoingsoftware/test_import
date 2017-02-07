package org.group3.sync.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.group3.sync.Client;
import org.group3.sync.ClientActionListener;
import org.group3.sync.Peer;
import org.group3.sync.exception.ConnectivityException;
import org.group3.sync.exception.ErrorCode;


class ManagerClient extends Receiver implements Client {
	private static final String TAG = "ManagerClient";
	private final List<Peer> mDevices = new ArrayList<>();
	private ClientSocket mClient;
	private ClientActionListener mListener;

	ManagerClient(Context ctx, ClientActionListener listener) throws ConnectivityException {
		super(ctx);
		startDiscoverPeers();
		mListener = listener;
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
						break;
					}
					default:{
						break;
					}
				}
				break;
			}
			case BluetoothDevice.ACTION_FOUND:{
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				Log.d(TAG, device.getAddress()+" "+device.getName());
				synchronized (mDevices) {
					if (device.getBondState() == BluetoothDevice.BOND_NONE) {
						mDevices.add(new Peer(device.getName(), device.getAddress(), device));
					} else if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
						mDevices.add(new Peer(device.getName(), device.getAddress(), device));
					}
				}
				notifyPeerListReady(mDevices);
				break;
			}
			case BluetoothAdapter.ACTION_DISCOVERY_STARTED:{
				break;
			}
			case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:{
				break;
			}
			default:{
				break;
			}
		}
	}

	@Override
	public void connectToPeer(Peer peer){
		if(mClient != null && mClient.isConnected()) {
			notifyError(ErrorCode.ALREADY_CONNECTED);
			return;
		}
		mClient = new ClientSocket((BluetoothDevice) peer.getDevice(), mBluetoothAdapter, mListener);
	}

	@Override
	public void discoveryDevices() {
		registerReceiver();
		startDiscoverPeers();
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
		disconnect();
	}

	@Override
	public void disconnect(){
		if(mClient != null)
			mClient.terminate();
	}

	private void startDiscoverPeers(){
		synchronized (mDevices) {
			mDevices.clear();
		}
		mBluetoothAdapter.startDiscovery();
	}

	private void notifyPeerListReady(List<Peer> peerList){
		mListener.onPeerListReady(peerList);
	}

	private void notifyError(ErrorCode error){
		mListener.onError(error);
	}
}
