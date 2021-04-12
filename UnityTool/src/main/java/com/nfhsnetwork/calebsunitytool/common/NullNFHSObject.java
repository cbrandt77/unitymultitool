package com.nfhsnetwork.calebsunitytool.common;

public final class NullNFHSObject extends NFHSGameObject {
	
	private final String game_id;
	private final Throwable cause;
	
	public NullNFHSObject()
	{
		this("");
	}
	
	public NullNFHSObject(String gameID)
	{
		this(gameID, new Exception("Unknown error."));
	}
	
	public NullNFHSObject(String gameID, Throwable e)
	{
		this.game_id = gameID;
		this.cause = e;
	}
	
	public String getID()
	{
		return game_id;
	}
	
	public Throwable getCause()
	{
		return cause;
	}
}
