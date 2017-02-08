package org.group3.sync.exception;

/**
 * Created by summer on 21/12/16.
 */

public class AdapterNotSupportedException extends ConnectivityException{
	public AdapterNotSupportedException(String error){
		super(error);
	}
}
