package org.group3.sync;

/**
 * Created by summer on 10/12/16.
 */

public class ManagerFactory {

	public static Manager wifiP2PInstance(){
		return null;
	}

	public static Manager bluetoothInstance(){
		return new org.group3.sync.bluetooth.ManagerBluetooth();
	}

}
