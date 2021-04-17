package com.nfhsnetwork.calebsunitytool.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.protobuf.ByteString;

public final class Util 
{
	// TODO set to false
	// also maybe use a logger instead of sysout?
	public static boolean isDebugMode = true;

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
	
	
	
	public static String readFromFile(File file) throws FileNotFoundException, IOException
	{
		StringBuilder sb = new StringBuilder();
		
		try (BufferedReader rd = new BufferedReader(new FileReader(file)))
		{
			
			int cp;
			while ((cp = rd.read()) != -1)
			{
				sb.append((char)cp);
			}
		}
		
		return sb.toString();
	}
	
	/*
	 * Copied from https://stackoverflow.com/a/140861/
	 */
	public static ByteString hexStringToByteString(String s) throws NumberFormatException
	{
		//System.out.println("[DEBUG] {hexStringToByteString} " + s);
		if (s.length() % 2 != 0) {
			System.out.println("[DEBUG] {parseClubCSV} not div by 2 for id " + s);
			throw new NumberFormatException("String not divisible by 2.");
		}
		
		int len = s.length();
		
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
		
		return ByteString.copyFrom(data);
	}
	
	
	
}
