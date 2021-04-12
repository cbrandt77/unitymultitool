package com.nfhsnetwork.calebsunitytool.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nfhsnetwork.calebsunitytool.exceptions.NullFieldException;

public final class JSONUtils 
{
	public static class JSONReader 
	{
		public static JSONObject readJSONFromURL(String url) throws JSONException, IOException
		{
			//TODO optimize the read function to stop after getting the multiviewer tags.  Might not be easy, and also might not save any time either.
			InputStream inputStream = new URL(url).openStream();
			try (BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream)))
			{
				String jsonText = readAllFromReader(rd);
				return new JSONObject(jsonText);
			}
		}
		
		public static String readAllFromReader(Reader rd) throws IOException
		{
			StringBuilder sb = new StringBuilder();
			int cp;
			while ((cp = rd.read()) != -1)
			{
				sb.append((char)cp);
			}
			return sb.toString();
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
				throw new NullFieldException("\"broadcasts\" field does not exist.", e);
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








