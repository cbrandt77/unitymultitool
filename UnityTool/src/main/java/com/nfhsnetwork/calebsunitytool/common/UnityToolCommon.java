package com.nfhsnetwork.calebsunitytool.common;

public final class UnityToolCommon 
{
	public static final String DICTKEY_GAMEKEY = "key";
	
	public static final int SUCCESSFUL = 12;
	public static final int FAILED = 32;
	
	
	
	
	
	// TODO set to false
	// also maybe use a logger instead of sysout?
	public static final boolean isDebugMode;
	
	
	static {
		isDebugMode = System.getenv("isdebugmode") != null && System.getenv("isdebugmode").equals("true");
	}


	public static final boolean ISWINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");
	
}
