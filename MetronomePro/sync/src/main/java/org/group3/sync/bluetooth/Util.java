package org.group3.sync.bluetooth;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by summer on 11/12/16.
 */

public class Util {
	public static class ParsedInfo{
		private final String TAG = "Util.ParsedInfo";
		private JSONObject info;
		public ParsedInfo(String buffer){
			try {
				info = new JSONObject(buffer);
			}catch (JSONException e){
				Log.e(TAG, "NOT A JSON STRING");
			}
		}

		public String getName(){
			try {
				return info.getString(Constants.KEY_NAME);
			}catch (JSONException e){
				Log.e(TAG, Constants.KEY_NAME+"NOT EXIST");
				return "";
			}
		}

		public int getBPM(){
			try {
				return info.getInt(Constants.KEY_BPM);
			}catch (JSONException e){
				Log.e(TAG, Constants.KEY_BPM+"NOT EXIST");
				return 0;
			}
		}

		public String getRhythm(){
			try {
				return info.getString(Constants.KEY_RHYTHM);
			}catch (JSONException e){
				Log.e(TAG, Constants.KEY_RHYTHM+"NOT EXIST");
				return "-1";
			}
		}

		public Object getPlaylist(){
			try {
				return info.getString("playlist");
			}catch (JSONException e){
				Log.e(TAG, "Playlist NOT EXIST");
				return null;
			}
		}

		public byte getCommand(){
			try {
				return ((Integer) info.get(Constants.KEY_COMMAND)).byteValue();
			}catch (JSONException e){
				Log.e(TAG, Constants.KEY_COMMAND+"NOT EXIST");
				return 0;
			}
		}

		public long getTime(){
			try {
				return info.getLong(Constants.KEY_TIME);
			}catch (JSONException e){
				Log.e(TAG, Constants.KEY_TIME+"NOT EXIST");
				return 0;
			}
		}

		public long getDelay(){
			try {
				return info.getLong(Constants.KEY_DELAY);
			}catch (JSONException e){
				Log.e(TAG, Constants.KEY_DELAY+"NOT EXIST");
				return 0;
			}
		}
	}

	public static byte[] integerToBytes(int number){
		byte[] result = new byte[4];
		result[0] = (byte) (number >> 24);
		result[1] = (byte) (number >> 16);
		result[2] = (byte) (number >> 8);
		result[3] = (byte) (number);
		return result;
	}
/*
	public static int bytesToInteger(byte[] bytes){
		return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}*/
}
