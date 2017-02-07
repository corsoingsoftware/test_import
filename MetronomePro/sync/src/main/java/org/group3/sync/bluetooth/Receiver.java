package org.group3.sync.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.group3.sync.exception.AdapterNotActivatedException;
import org.group3.sync.exception.AdapterNotSupportedException;
import org.group3.sync.exception.ConnectivityException;
import org.group3.sync.exception.ErrorCode;


abstract class Receiver extends BroadcastReceiver{
	private static final String TAG = "Receiver";

	protected BluetoothAdapter mBluetoothAdapter;
	protected LocalBroadcastManager mBroadcaster;
	protected Context mContext;

	private boolean isBroadcasterRegistered;
	private IntentFilter mIntentFilter;

	Receiver(Context ctx) throws ConnectivityException {
		mContext = ctx;
		mBroadcaster = LocalBroadcastManager.getInstance(mContext);

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			throw new AdapterNotSupportedException(ErrorCode.ADAPTER_NOT_SUPPORTED.getInfo());
		}

		if(!mBluetoothAdapter.isEnabled()){
			throw new AdapterNotActivatedException(ErrorCode.ADAPTER_NOT_ACTIVATED.getInfo());
		}

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		mIntentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		mIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		mIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		mIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		mContext.registerReceiver(this,mIntentFilter);
		isBroadcasterRegistered = true;
	}

	protected void registerReceiver(){
		if(!isBroadcasterRegistered) {
			mContext.registerReceiver(this, mIntentFilter);
			isBroadcasterRegistered = true;
		}
	}

	protected void unregisterReceiver(){
		try {
			mContext.unregisterReceiver(this);
		}catch (IllegalArgumentException e){
			Log.d(TAG, "the broadcaster receiver is not registered");
		}finally {
			isBroadcasterRegistered = false;
		}
	}

	protected void clean(){
		unregisterReceiver();
		if(mBluetoothAdapter.isDiscovering())
			mBluetoothAdapter.cancelDiscovery();
	}
}
