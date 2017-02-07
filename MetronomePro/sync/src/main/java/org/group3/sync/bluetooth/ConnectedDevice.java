package org.group3.sync.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.group3.sync.exception.ErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by summer on 4/1/17.
 */

abstract class ConnectedDevice implements Runnable{
	final String TAG;
	private BluetoothSocket mSocket;
	private final InputStream mInStream;
	private final OutputStream mOutStream;

	ConnectedDevice(BluetoothSocket socket, String tag) {
		mSocket = socket;
		TAG = tag;
		InputStream tmpIn = null;
		OutputStream tmpOut = null;
		// Get the BluetoothSocket input and output streams
		try {
			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
		} catch (IOException e) {
			errorHandler(ErrorCode.STREAM_OPEN_UNABLE);
			Log.e(TAG, "temp sockets not created", e);
		}
		mInStream = tmpIn;
		mOutStream = tmpOut;
	}

	@Override
	public void run() {
		Log.d(TAG, "BEGIN mConnectedThread");
		byte[] buffer = new byte[1024];
		int bytes;
		// Keep listening to the InputStream while connected
		while (true) {
			try {
				bytes = mInStream.read(buffer);
				messageHandler(buffer,bytes);
			} catch (IOException e) {
				errorHandler(ErrorCode.DISCONNECTED);
				Log.e(TAG, "disconnected", e);
				break;
			}
		}
	}

	abstract protected void messageHandler(byte[] msg, int length);
	abstract protected void errorHandler(ErrorCode error);

	void write(byte[] buffer) {
		try {
			//Log.d(TAG, new String(buffer));
			synchronized (mOutStream) {
				mOutStream.write(buffer);
				mOutStream.flush();
			}
		} catch (IOException e) {
			errorHandler(ErrorCode.DISCONNECTED);
			Log.e(TAG, "Exception during write", e);
		}
	}

	void stop(){
		try {
			mInStream.close();
			mOutStream.close();
		}catch(IOException e){
			errorHandler(ErrorCode.STREAM_CLOSE_UNABLE);
		}
		try{
			mSocket.close();
		}catch (IOException e){
			errorHandler(ErrorCode.SOCKET_CLOSE_UNABLE);
		}
	}
}