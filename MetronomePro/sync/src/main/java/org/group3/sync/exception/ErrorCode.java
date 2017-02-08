package org.group3.sync.exception;

/**
 * Created by summer on 21/12/16.
 */

public enum ErrorCode{
	ADAPTER_NOT_SUPPORTED(100, "This device does not support selected adapter"),
	ADAPTER_NOT_ACTIVATED(101, "Selected adapter is not activated"),

	SERVER_CANNOT_LISTEN(102, "Server cannot listen incoming connections"),
	SOCKET_OPEN_UNABLE(103, "Unable to open a socket"),
	SOCKET_CLOSE_UNABLE(104, "Unable to close a socket"),
	DISCONNECTED(105, "disconnected"),
	STREAM_OPEN_UNABLE(106, "Unable to open I/O stream"),
	STREAM_CLOSE_UNABLE(107, "Unable to open I/O stream"),
	CONNECT_UNABLE(108, "Unable to connect device"),
	ALREADY_CONNECTED(109, "You are already connected to a device");

	private int code;
	private String info;
	ErrorCode(int code, String info){
		this.code = code;
		this.info = info;
	}

	public int getCode(){
		return code;
	}

	public String getInfo(){
		return info;
	}

	@Override
	public String toString(){
		return "("+getCode()+")"+" "+getInfo();
	}
}
