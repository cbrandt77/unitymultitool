package com.nfhsnetwork.calebsunitytool.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.protobuf.ByteString;

/**
 * 
 * @author calebbrandt
 * Contains helper functions
 */
public final class Util 
{
	private static final String CURRENTDIRECTORY;
	
	static {
		CURRENTDIRECTORY = new File("").getAbsolutePath();
	}
	
	
	
	
	public static class StringUtils
	{
		public static String stripQuotes(String input)
		{
			return input.replaceAll("^\"", "").replaceAll("\"$", "");
		}
		
	}
	
	public static class TimeUtils
	{
		public static final ZoneId TIMEZONE_UTC = ZoneId.of("Z");
		public static final ZoneId TIMEZONE_EST = ZoneId.of("America/New_York");
		
		public static LocalDateTime convertDateTimeToEST(String unityDT)
		{
			return ZonedDateTime.parse(unityDT).withZoneSameInstant(TimeUtils.TIMEZONE_EST).toLocalDateTime();
		}

		public static LocalDateTime convertEpochSecondToEST(long epoch)
		{
			return ZonedDateTime.ofInstant(
										Instant.ofEpochSecond(epoch),
										ZoneId.of("Z"))
					  			.withZoneSameInstant(TimeUtils.TIMEZONE_EST)
					  			.toLocalDateTime();
		}
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
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static class IOUtils
	{

		public static String readFromFile(File file) throws FileNotFoundException, IOException
		{
			StringBuilder sb = new StringBuilder();
			
			try (BufferedReader rd = new BufferedReader(new FileReader(file)))
			{
				IOUtils.readAllFromReader(rd);
			}
			
			return sb.toString();
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
		
	}
	
	
	
	public static String getCurrentDirectory()
	{
		return CURRENTDIRECTORY;
	}
	
}
