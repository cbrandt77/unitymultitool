package com.nfhsnetwork.unitytool.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;

import com.nfhsnetwork.unitytool.common.UnityContainer;
import com.nfhsnetwork.unitytool.exceptions.GameNotFoundException;
import com.nfhsnetwork.unitytool.exceptions.InvalidContentTypeException;
import com.nfhsnetwork.unitytool.types.NFHSContentType;
import com.nfhsnetwork.unitytool.utils.IOUtils;

public class UnityInterface 
{
	public static JSONObject fetchEventFromUnity(final String inputID) throws IOException
	{
		final String contentID;
		if (!NFHSContentType.isValidEventId(inputID))
			contentID = IOUtils.FetchID.fetchEventIDIfNeeded(inputID);
		else
			contentID = inputID;
		
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

	public static JSONObject fetchBdcStateJSON(final String bdc) throws JSONException, IOException
	{
		return IOUtils.readJSONFromURL(BDCSTATE_ENDPOINT + bdc);
	}
	
	
	
	private static final String GAMECREATE_ENDPOINT = "https://unity.nfhsnetwork.com/v2/games/create_all";
	
	public static final boolean createEvent(final JSONObject submit) throws IOException
	{
		return sendJSONToUnity(submit, GAMECREATE_ENDPOINT);
	}
	
	public static final boolean sendJSONToUnity(final JSONObject jsonIn, final String endpoint) throws IOException
	{
		final String jsonText = jsonIn.toString();
		final byte[] out = jsonText.getBytes(StandardCharsets.UTF_8);
		
		final URL url = new URL(endpoint);
		final URLConnection con = url.openConnection();
		
		HttpURLConnection http = (HttpURLConnection)con;
		http.setRequestMethod("PUT");
		http.setDoOutput(true);
		http.setDoInput(true);
		
		addRequestHeaders(http);
		http.addRequestProperty("Content-Length", jsonText.length() + "");
		
		http.addRequestProperty("Authorization", UnityContainer.getAuthToken());
		
		http.setFixedLengthStreamingMode(out.length);
		
		//http.connect(); // actually redundant in java apparently
		
		try (final OutputStream os = http.getOutputStream())
		{
			os.write(out);
		}
		
		
		//InputStream response = http.getInputStream();
		
		final boolean succeeded = http.getResponseCode() != 200;

		http.disconnect();
		
		return succeeded;
		
	}
	private static void addRequestHeaders(HttpURLConnection http)
	{
		http.addRequestProperty("Accept", "application/json, text/plain, */*");
		http.addRequestProperty("Accept-Encoding", "gzip, deflate, br");
		http.addRequestProperty("Accept-Language", "en-US,en;q=0.9");
//		http.addRequestProperty("Connection", "keep-alive");
		http.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
//		http.addRequestProperty("Host", "unity.nfhsnetwork.com");
//		http.addRequestProperty("Origin", "http://console.nfhsnetwork.com");
//		http.addRequestProperty("Referer", "http://console.nfhsnetwork.com/");
//		http.addRequestProperty("sec-ch-ua", "\"Chromium\";v=\"88\", \"Google Chrome\";v=\"88\", \";Not A Brand\";v=\"99\"");
//		http.addRequestProperty("sec-ch-ua-mobile", "?0");
//		http.addRequestProperty("Sec-Fetch-Dest", "empty");
//		http.addRequestProperty("Sec-Fetch-Mode", "cors");
//		http.addRequestProperty("Sec-Fetch-Site", "cross-site");
//		http.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36");
	}
	
}