package org.group3.sync;

/**
 * Created by summer on 3/1/17.
 */

public class MetroConfig {

	private int bpm;
	private String rhythm;
	private Object playlist;
	public MetroConfig(int bpm, String rhythm, Object playlist){
		this.bpm = bpm;
		this.rhythm = rhythm;
		this.playlist = playlist;
	}

	public int getBpm(){
		return bpm;
	}

	public String getRhythm(){
		return rhythm;
	}

	public Object getPlaylist(){ return playlist;}
}
