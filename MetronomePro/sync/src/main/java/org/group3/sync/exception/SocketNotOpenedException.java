package org.group3.sync.exception;

/**
 * Created by summer on 21/12/16.
 */

public class SocketNotOpenedException extends ConnectivityException {
	public SocketNotOpenedException(String error){
		super(error);
	}
}
