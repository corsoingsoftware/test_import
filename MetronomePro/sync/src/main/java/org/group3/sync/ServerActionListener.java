package org.group3.sync;

import org.group3.sync.exception.ErrorCode;

/**
 * An action listener which is called by the implemented class of the {@link Server} interface
 * @see Server
 * @author DAOHONG LI
 */
public interface ServerActionListener {

	/**
	 * Event occurs when connection between a client and the server is established
	 * @param peer the client
	 * @see Peer
	 */
	void onClientConnected(Peer peer);

	/**
	 * Event occurs when a client and the server is synchronized
	 * @param peer the client
	 * @see Peer
	 */
	void onClientSynchronized(Peer peer);

	/**
	 * Event occurs when the connection between a client and the server is lost
	 * @param peer the client
	 * @see Peer
	 */
	void onClientDisconnected(Peer peer);

	/**
	 * Event occurs when the server is started
	 * @see ServerInfo
	 * @param serverInfo server info
	 */
	void onServerStarted(ServerInfo serverInfo);

	/**
	 * Event occurs when the server is stopped
	 */
	void onServerStopped();

	/**
	 * Event occurs when an error is occurred. Such error should not be fatal.
	 * @see ErrorCode
	 * @param error the error
	 */
	void onError(ErrorCode error);
}
