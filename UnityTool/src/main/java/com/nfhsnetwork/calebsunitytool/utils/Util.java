package com.nfhsnetwork.calebsunitytool.utils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.json.JSONException;
import org.json.JSONObject;

public final class Util 
{
	
	
	
	
	public static String stripQuotes(String input)
	{
		return input.replaceAll("^\"", "").replaceAll("\"$", "");
	}
	
	
	private static final ZoneId TIMEZONE_EST = ZoneId.of("America/New_York");
	
	public static LocalDateTime convertDateTimeToEST(String unityDT)
	{
		return ZonedDateTime.parse(unityDT).withZoneSameInstant(TIMEZONE_EST).toLocalDateTime();
	}
	
	public static LocalDateTime convertEpochSecondToEST(long epoch)
	{
		return ZonedDateTime.ofInstant(
									Instant.ofEpochSecond(epoch),
									ZoneId.of("Z"))
				  			.withZoneSameInstant(TIMEZONE_EST)
				  			.toLocalDateTime();
	}
	
	
	
	private static final String BCAST_ENDPOINT = "http://broadcasts.bcast.nfhsnetwork.com/broadcast/";
	public static JSONObject getBdcStateJSON(String bdc) throws JSONException, IOException
	{
		return JSONUtils.JSONReader.readJSONFromURL(BCAST_ENDPOINT + bdc);
	}
	
	
	
}
