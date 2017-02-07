package org.group3.sync.bluetooth;

import android.content.Context;
import org.group3.sync.ClientActionListener;
import org.group3.sync.Manager;
import org.group3.sync.ServerActionListener;
import org.group3.sync.Server;
import org.group3.sync.Client;
import org.group3.sync.exception.ConnectivityException;

public class ManagerBluetooth implements Manager {
	@Override
	public Server newServer(Context context, ServerActionListener listener) throws ConnectivityException {
		return new ManagerServer(context, listener);
	}

	@Override
	public Client newClient(Context context, ClientActionListener listener) throws ConnectivityException {
		return new ManagerClient(context, listener);
	}
}
