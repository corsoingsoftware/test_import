package org.group3.sync;

import android.content.Context;

import org.group3.sync.exception.ConnectivityException;

public interface Manager {
	Server newServer(Context context, ServerActionListener listener) throws ConnectivityException;
	Client newClient(Context context, ClientActionListener listener) throws ConnectivityException;
}
