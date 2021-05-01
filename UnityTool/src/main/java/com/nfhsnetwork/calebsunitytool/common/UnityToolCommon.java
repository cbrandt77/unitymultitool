package com.nfhsnetwork.calebsunitytool.common;

public final class UnityToolCommon 
{
	public enum PropertyChangeType {
		PARSE_COMPLETE,
		DONE,
		PROGRESS
	}


	public static final String DICTKEY_GAMEKEY = "key";
	
	public static final int SUCCESSFUL = 12;
	public static final int FAILED = 32;
	


	public static final boolean ISWINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");
	
}
