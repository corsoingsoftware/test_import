package org.group3.sync;

import java.util.List;

public interface Server extends ManagerState {
	void broadcastStart(MetroConfig config, long startTime);
	void broadcastStart(long startTime);
	void broadcastStop();
	void broadcastSet(MetroConfig config);
	void broadcastGeneralMessage(byte[] message);

	void sendSetTo(Peer peer, MetroConfig config);
	void sendStartTo(Peer peer, MetroConfig config, long startTime);
	void sendStartTo(Peer peer, long startTime);

	//List<Device> getListDevice();
}
