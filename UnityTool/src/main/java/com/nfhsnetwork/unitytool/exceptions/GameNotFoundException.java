package com.nfhsnetwork.unitytool.exceptions;

public class GameNotFoundException extends UnityException 
{
	private final String id;
	
	
	public GameNotFoundException() {
		super();
		id = null;
	}

	public GameNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		id = null;
	}

	public GameNotFoundException(String id, Throwable cause) {
		super(id + " not found.", cause);
		this.id = id;
	}

	public GameNotFoundException(Throwable cause) {
		super(cause);
		id = null;
	}
	
	public GameNotFoundException(String id) 
	{
		super(id + " not found.");
		this.id = id;
	}
	
	public String getID()
	{
		return (this.id == null) ? "" : this.id;
	}
	
	
}
