package com.nfhsnetwork.calebsunitytool.common;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nfhsnetwork.calebsunitytool.exceptions.InvalidContentTypeException;
import com.nfhsnetwork.calebsunitytool.exceptions.NullFieldException;
import com.nfhsnetwork.calebsunitytool.utils.Util.IOUtils;

import com.nfhsnetwork.calebsunitytool.exceptions.GameNotFoundException;

public final class UnityToolCommon 
{
	public static final String DICTKEY_GAMEKEY = "key";
	
	public static class IDConversion 
	{
		/**
		 * Wrapper function for {@link fetchEventIDFromChild}.
		 * Checks if the input is a valid event ID, and if not, determines what type of child it is, fetches the child JSON, and retrieves the parent ID from that.
		 * @param id Input child ID
		 * @return parent event ID for the given child id, or returns the input if it is already a valid parent.
		 * @throws GameNotFoundException if parent could not be found.
		 * @throws IOException if external
		 * @throws InvalidContentTypeException
		 */
		public static String fetchEventIDIfNeeded(String id) throws GameNotFoundException, IOException, InvalidContentTypeException
		{
			if (NFHSContentType.isValidEventId(id))
			{
				return id;
			}
			else if (NFHSContentType.BROADCAST.is(id))
			{
				return fetchEventIDFromChild(id, NFHSContentType.BROADCAST);
			}
			else if (NFHSContentType.VOD.is(id))
			{
				return fetchEventIDFromChild(id, NFHSContentType.VOD);
			}
			else
			{
				throw new InvalidContentTypeException(id, "gam/evt/bdc/vod");
			}
		}
		
		/**
		 * 
		 * @param id child ID.
		 * @param contentType the type of the ID.
		 * @return String containing parent event ID.
		 * @throws GameNotFoundException
		 * @throws IOException
		 */
		public static String fetchEventIDFromChild(String id, NFHSContentType contentType) throws GameNotFoundException, IOException
		{
			try
			{
				JSONObject bdc_or_vod = IOUtils.readJSONFromURL(contentType.getEndpointURL() + id);
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

	public static class ManipNFHSJSON
	{
		public static String[] getBroadcastKeys(JSONObject j) throws NullFieldException
		{
			JSONArray broadcasts;
			try {
				broadcasts = j.getJSONArray("publishers").getJSONObject(0).getJSONArray("broadcasts");
			} 
			catch (JSONException e) {
				throw new NullFieldException("\"broadcasts\" field does not exist for game " + j.getString("key"), e);
			}
			
			String[] output = new String[broadcasts.length()];
			
			for (int i = 0; i < broadcasts.length(); i++)
			{
				try {
					output[i] = broadcasts.getJSONObject(i).getString("key");
				} catch (JSONException e) {
					output[i] = "null";
				}
			}
			
			return output;
		}
	}
	
}
