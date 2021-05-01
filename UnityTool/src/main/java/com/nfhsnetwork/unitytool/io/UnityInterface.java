package com.nfhsnetwork.unitytool.io;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.nfhsnetwork.unitytool.exceptions.GameNotFoundException;
import com.nfhsnetwork.unitytool.exceptions.InvalidContentTypeException;
import com.nfhsnetwork.unitytool.types.NFHSContentType;
import com.nfhsnetwork.unitytool.utils.Util.IOUtils;

public class UnityInterface 
{
	public static JSONObject fetchEventFromUnity(String contentID) throws GameNotFoundException, IOException, InvalidContentTypeException
	{
		if (!NFHSContentType.isValidEventId(contentID))
			contentID = IOUtils.FetchID.fetchEventIDIfNeeded(contentID);
		
		try 
		{
			if (NFHSContentType.GAME.is(contentID))
				return IOUtils.readJSONFromURL(NFHSContentType.GAME.getEndpointURL() + contentID);
			
			if (NFHSContentType.EVENT.is(contentID))
				return IOUtils.readJSONFromURL(NFHSContentType.EVENT.getEndpointURL() + contentID);
			
		} 
		catch (JSONException e) {
			throw new GameNotFoundException(contentID, e);
		}
		
		throw new InvalidContentTypeException(contentID, "gam/evt");
	}
	
	public static final String BDCSTATE_ENDPOINT = "http://broadcasts.bcast.nfhsnetwork.com/broadcast/";

	public static JSONObject fetchBdcStateJSON(String bdc) throws JSONException, IOException
	{
		return IOUtils.readJSONFromURL(BDCSTATE_ENDPOINT + bdc);
	}
	
	
}