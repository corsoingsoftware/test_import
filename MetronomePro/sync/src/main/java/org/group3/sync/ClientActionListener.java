package org.group3.sync;

import org.group3.sync.exception.ErrorCode;

import java.util.List;

/**
 * An action listener which is called by the implemented class of the {@link Client} interface
 * @see Client
 * @author DAOHONG LI
 */
public interface ClientActionListener {
	/**
	 * Event occurs when some devices are discovered
	 * @param list a device list
	 */
	void onPeerListReady(List<Peer> list);

	/**
	 * Event occurs when a client is connecting to the server
	 */
	void onConnecting();

	/**
	 * Event occurs when the connection between client and server is established
	 */
	void onConnected();

	/**
	 * Event occurs when the time between client and server is synchronized, this is the most important event. <br>
	 * The server time is determined by local time minus timeDifference. <br>
	 * Example:  long serverTime = System.currentTimeMillis() - timeDifference
	 * @param timeDifference the time difference between server and client
	 */
	void onSynchronized(long timeDifference);

	/**
	 * Event occurs when the connection between client and server is lost
	 */
	void onDisconnected();

	/**
	 * Event occurs when an error is occurred. Such error should not be fatal.
	 * @see ErrorCode
	 * @param error the error
	 */
	void onError(ErrorCode error);

	/**
	 * Event occurs when the client have received the set config command from the server
	 * @see MetroConfig
	 * @param config config
	 */
	void onReceiveSet(MetroConfig config);

	/**
	 * Event occurs when the client have received the start command from the server
	 * @see MetroConfig
	 * @param config config
	 * @param time time send by server
	 * @param delay delay of this message
	 */
	void onReceiveStart(MetroConfig config, long time, long delay);

	/**
	 * Event occurs when the client have received the stop command from the server
	 */
	void onReceiveStop();

	/**
	 * Event occurs when the client have received a message from the server
	 * @param message a message in byte array
	 */
	void onReceiveGeneralMessage(byte[] message);

}
