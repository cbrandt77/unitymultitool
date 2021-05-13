package com.nfhsnetwork.unitytool.common;

import java.awt.Window;

public final class UnityToolCommon 
{
	private static Window activeWindow;
	
	public static void setActiveWindow(Window w)
	{
		activeWindow = w;
	}
	
	public static Window getActiveWindow()
	{
		return activeWindow;
	}
	
	


	public static final String DICTKEY_GAMEKEY = "key";
	
	public static final int SUCCESSFUL = 12;
	public static final int FAILED = 32;
	public static final int INTERRUPTED = 41;
	


	public static final boolean ISWINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");
	
	
}
