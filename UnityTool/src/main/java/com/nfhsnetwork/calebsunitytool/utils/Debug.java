package com.nfhsnetwork.calebsunitytool.utils;

public final class Debug {
	
	private static final String[] potentialDebugVars = {
			"debug",
			"isDebug",
			"isDebugMode"
	};
	
	private static final boolean isDebugMode;
	
	static {
		boolean isdebug = false;
		for (String var : potentialDebugVars)
		{
			if ("true".equals(System.getenv(var))
			 || "1".equals(System.getenv(var))
			 || "true".equals(System.getenv(var.toUpperCase()))
			 || "1".equals(System.getenv(var.toUpperCase()))
			 || "true".equals(System.getenv(var.toLowerCase()))
			 || "1".equals(System.getenv(var.toLowerCase())))
			{
				isdebug = true;
				
				System.out.println("DEBUG MODE ENABLED");
				
				break;
			}
		}
		
		isDebugMode = isdebug;
	}
	
	
	
	
	public static void out(String text)
	{
		if (isDebugMode)
		{
			System.out.println(text);
		}
	}
	
	public static void checkNull(Object toCheck, String ifNull)
	{
		if (isDebugMode)
		{
			if (toCheck == null)
				out(ifNull);
		}
	}
	
	
}
