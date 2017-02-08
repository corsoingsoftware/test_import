package org.group3.sync;

import java.util.List;

/**
 * The client interface which is implemented in sub modules
 * @see org.group3.sync.bluetooth.ManagerClient
 * @author DAOHONG LI
 */
public interface Client extends ManagerState {
	/**
	 * connect to a selected device and some events will occur
	 * @see ClientActionListener#onConnecting()
	 * @see ClientActionListener#onConnected()
	 * @see ClientActionListener#onSynchronized(long)
	 * @param peer selected device
	 */
	void connectToPeer(Peer peer);

	/**
	 * disconnect from current connection, a disconnect event will occur
	 * @see ClientActionListener#onDisconnected()
	 */
	void disconnect();

	/**
	 * discover remote devices <br>
	 * when the devices are found an peer list ready event is occurred.
	 * @see ClientActionListener#onPeerListReady(List)
	 */
	void discoveryDevices();
}
