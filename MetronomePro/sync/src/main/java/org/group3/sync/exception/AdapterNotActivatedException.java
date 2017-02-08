package org.group3.sync.exception;

/**
 * Created by summer on 21/12/16.
 */

public class AdapterNotActivatedException extends ConnectivityException {
	public AdapterNotActivatedException(String error){
		super(error);
	}
}
