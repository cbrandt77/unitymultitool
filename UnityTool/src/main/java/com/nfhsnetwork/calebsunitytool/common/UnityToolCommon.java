package com.nfhsnetwork.calebsunitytool.common;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.nfhsnetwork.calebsunitytool.exceptions.InvalidContentTypeException;
import com.nfhsnetwork.calebsunitytool.utils.JSONUtils;
import com.nfhsnetwork.calebsunitytool.exceptions.GameNotFoundException;

public final class UnityToolCommon 
{
	public static final String DICTKEY_GAMEKEY = "key";
	
	public static class GetFromUnity 
	{
		
		public static JSONObject getGameFromUnity(String contentID) throws GameNotFoundException, IOException, InvalidContentTypeException
		{
			contentID = Convert.convertToGameID(contentID);
			
			try 
			{
				if (NFHSContentType.GAME.is(contentID))
					return JSONUtils.JSONReader.readJSONFromURL(NFHSContentType.GAME.getEndpointURL() + contentID);
				
				if (NFHSContentType.EVENT.is(contentID))
					return JSONUtils.JSONReader.readJSONFromURL(NFHSContentType.EVENT.getEndpointURL() + contentID);
				
			} 
			catch (JSONException e) {
				throw new GameNotFoundException(contentID, e);
			}
			
			throw new InvalidContentTypeException();
		}
		
		
	}
	
	public static class Convert 
	{
		public static String convertToGameID(String id) throws GameNotFoundException, IOException, InvalidContentTypeException
		{
			if (NFHSContentType.GAME.is(id) || NFHSContentType.EVENT.is(id))
			{
				return id;
			}
			else if (NFHSContentType.BROADCAST.is(id))
			{
				return getGameIDFromBroadcast(id, NFHSContentType.VOD);
			}
			else if (NFHSContentType.VOD.is(id))
			{
				return getGameIDFromBroadcast(id, NFHSContentType.VOD);
			}
			else
			{
				throw new InvalidContentTypeException(id, "gam/evt/bdc/vod");
			}
		}
		
		public static String getGameIDFromBroadcast(String id, NFHSContentType c) throws GameNotFoundException, IOException
		{
			try
			{
				JSONObject bdc_or_vod = JSONUtils.JSONReader.readJSONFromURL(c.getEndpointURL() + id);
				String gameID = bdc_or_vod.getString("game_key");
				
				if (gameID == null)
					throw new GameNotFoundException(id);
				
				return gameID;
			} 
			catch (JSONException e) {
				throw new GameNotFoundException(id, e);
			}
		}
	}
	
}
