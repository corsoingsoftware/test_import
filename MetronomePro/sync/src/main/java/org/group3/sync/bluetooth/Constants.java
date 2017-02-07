package org.group3.sync.bluetooth;

import java.util.UUID;


interface Constants {
	UUID APP_ID = UUID.fromString("12a567cb-3ef4-4d38-bd43-e9075680fef0");
	int HANDSHAKE_TIMES = 20;

	byte MESSAGE_CONNECTION_OK = 0x50;
	byte MESSAGE_START_CONFIG = 0x51;
	byte MESSAGE_STOP = 0x52;
	byte MESSAGE_NAME = 0x53;

	byte MESSAGE_REQUEST_SERVER_TIME = 0x54;
	byte MESSAGE_SERVER_TIME = 0x55;
	byte MESSAGE_ACT_SERVER_TIME = 0x56;
	byte MESSAGE_DELAY = 0x57;

	byte MESSAGE_GENERAL = 0x58;
	byte MESSAGE_SET = 0x59;
	byte MESSAGE_START = 0x5A;


	String KEY_COMMAND = "cmd";
	String KEY_TIME = "time";
	String KEY_DELAY = "delay";
	String KEY_BPM = "bpm";
	String KEY_RHYTHM = "rhythm";
	String KEY_NAME = "name";
}