package com.nfhsnetwork.unitytool.common;

public interface UnityToolCommon 
{
//	private static Window activeWindow; //TODO move all of this to a proper View Controller, which will recall the current window state, and also all of the window fields.
//	
//	public static void setActiveWindow(Window w)
//	{
//		activeWindow = w;
//	}
//	
//	public static Window getActiveWindow()
//	{
//		return activeWindow;
//	}
	
	public static final String CONSOLE_ENDPOINT = "http://console.nfhsnetwork.com/";
	public static final String CONSOLE_EVENT_ENDPOINT = CONSOLE_ENDPOINT + "nfhs-events/#/events/";
	
	
	
	public static final String DICTKEY_GAMEKEY = "key";
	
	public static final int SUCCESSFUL = 12;
	public static final int FAILED = 32;
	public static final int INTERRUPTED = 41;
	
	
	public static final boolean ISWINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");
	
	
}
