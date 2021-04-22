package com.nfhsnetwork.calebsunitytool.common;

/**
 * 
 * @author calebbrandt
 *
 *
 * @apiNote class for when an event could not be initialized properly, but still needs to be there.
 */
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
		super(gameID, null, null, null);
		this.game_id = gameID;
		this.cause = e;
	}
	
	@Override
	public String getGameID()
	{
		return game_id;
	}
	
	public Throwable getCause()
	{
		return cause;
	}
}
