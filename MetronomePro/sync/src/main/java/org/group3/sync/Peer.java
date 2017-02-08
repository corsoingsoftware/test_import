package org.group3.sync;

public class Peer{
	private String mName;
	private String mAddress;
	private Object mDevice;

	public Peer(String name, String address, Object device){
		mName = name;
		mAddress = address;
		mDevice = device;
	}

	public String getName(){
		return mName;
	}
	public String getAddress(){
		return mAddress;
	}
	public Object getDevice(){
		return mDevice;
	}

	@Override
	public String toString(){
		return getName()+" "+getAddress();
	}

}

