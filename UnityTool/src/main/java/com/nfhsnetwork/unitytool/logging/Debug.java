package com.nfhsnetwork.unitytool.logging;

import java.io.OutputStream;
import java.io.PrintStream;

public final class Debug {
	
	public static boolean IS_DEV_MODE = true; //TODO
	
	private static PrintStream DEBUGSTREAM = new DebugPrintStream(System.out);
	
	//TODO use Logger or output log to file
	
	private static final String[] potentialDebugVars = {
			"debug",
			"isDebug",
			"isDebugMode"
	};
	
	private static boolean isDebugMode;
	
	public static void checkIfDebug()
	{
		isDebugMode = readIsDebug();
	}
	
	private static boolean readIsDebug()
	{
		for (String var : potentialDebugVars)
		{
			if ("true".equals(System.getenv(var))
			 || "1".equals(System.getenv(var))
			 || "true".equals(System.getenv(var.toUpperCase()))
			 || "1".equals(System.getenv(var.toUpperCase()))
			 || "true".equals(System.getenv(var.toLowerCase()))
			 || "1".equals(System.getenv(var.toLowerCase())))
			{
				System.out.println("DEBUG MODE ENABLED");
				return true;
			}
		}
		
		return false;
	}
	
	
	
	
	public static void out(String text)
	{
		if (isDebugMode)
		{
			DEBUGSTREAM.println(text);
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

class DebugPrintStream extends java.io.PrintStream
{
	public DebugPrintStream(OutputStream out) {
		super(out);
		// TODO Auto-generated constructor stub
	}
	
	
    private StackTraceElement getCallSite() {
        for (StackTraceElement e : Thread.currentThread()
                .getStackTrace())
            if (!e.getMethodName().equals("getStackTrace")
                    && !e.getClassName().equals(getClass().getName())
                    && !e.getClassName().equals(Debug.class.getName()))
                return e;
        return null;
    }
    
    
    
    @Override
    public void println(final String s) {
        println((Object) s);
    }
    
    

    @Override
    public void println(final Object o) {
    	final StackTraceElement e = getCallSite();
    	final String callSite =
	    		(e == null) 
	    			? "??" 
	    			: String.format("%s.%s(%s:%d)",
	                      e.getClassName(),
	                      e.getMethodName(),
	                      e.getFileName(),
	                      e.getLineNumber());
        
        super.println(o + "\n\t" + "at " + callSite);
    }
}
