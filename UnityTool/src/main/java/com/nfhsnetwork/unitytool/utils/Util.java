package com.nfhsnetwork.unitytool.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.protobuf.ByteString;
import com.nfhsnetwork.unitytool.common.UnityToolCommon;
import com.nfhsnetwork.unitytool.exceptions.NullFieldException;
import com.nfhsnetwork.unitytool.logging.Debug;

/**
 * 
 * @author calebbrandt
 * Contains helper functions
 */
public final class Util 
{
	private static final Path CURRENTDIRECTORY;
	
	static {
		if (UnityToolCommon.ISWINDOWS) {
			CURRENTDIRECTORY = Path.of(new File("").getAbsolutePath());
		}
		else {
//			boolean worked = false;
//			Path p = null;
//			try {
//				p = Path.of(truncateFilePath(Files.readSymbolicLink(Path.of("/proc/self/exe")).toString())); //TODO verify
//				worked = true;
//			} catch (IOException e) {
//				//e.printStackTrace();
//				Debug.out("[DEBUG] {Util static init} /proc/self/exe didn't work.");
//				
//				
//				worked = false;
//				
//			}
//			
//			if (worked) //workaround to get java to stop yelling at me about reassigned final vars
//				CURRENTDIRECTORY = p;
//			else
				CURRENTDIRECTORY = Path.of(new File("").getAbsolutePath());//TODO debug info //Path.of(truncateFilePath(Util.class.getProtectionDomain().getCodeSource().getLocation().getPath()));
		}
	}
	
	private static String truncateFilePath(String s)
	{
		return s.substring(0, s.lastIndexOf(File.separator) + 1);
	}
	
	
	
	
	public static class StringUtils
	{
		public static String stripQuotes(String input)
		{
			return strip(input, "\"");
		}
		
		public static String strip(String input, String toStrip)
		{
			return input.replaceAll("^" + toStrip, "").replaceAll(toStrip + "$", "");
		}
		
		public static String stripFromEnd(String input, String toStrip)
		{
			return input.replaceAll(toStrip + "$", "");
		}
		
		public static String stripFromBeginning(String input, String toStrip)
		{
			return input.replaceAll("^" + toStrip, "");
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
		//Debug.out("[DEBUG] {hexStringToByteString} " + s);
		if (s.length() % 2 != 0) {
			Debug.out("[DEBUG] {hexStringToByteString} not div by 2 for id " + s);
			
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
	
	
	public static class ManipNFHSJSON
	{
		public static String[] getBroadcastKeys(final JSONObject j) throws NullFieldException
		{
			final JSONArray broadcasts;
			try {
				broadcasts = j.getJSONArray("publishers").getJSONObject(0).getJSONArray("broadcasts");
			} 
			catch (JSONException e) {
				throw new NullFieldException("\"broadcasts\" field does not exist for game " + j.getString("key"), e);
			}
			
			final String[] output = new String[broadcasts.length()];
			
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
	
	
	public static Path getCurrentDirectory()
	{
		return CURRENTDIRECTORY;
	}
	
	
	private static final String DEV_DIR = File.separator + "DEV";
	
	/**
	 * Sanitizes input relative path and converts it to an absolute path.
	 * @param relativeDirectory the relative path of the file or directory.  May or may not begin with a file separator char.
	 * @return Absolute {@link Path} of the file or directory.
	 */
	public static Path getAbsoluteDir(final String relativeDirectory)
	{
		final StringBuilder sb = new StringBuilder();
		
		final String currentDir = StringUtils.stripFromEnd(getCurrentDirectory().toString(), File.separator);
		sb.append(currentDir);
		
		if (Debug.IS_DEV_MODE)
		{
			sb.append(DEV_DIR);
		}
		
		sb.append(File.separator);
		
		// Remove potential file separators from the end of the current directory and the beginning of the file name
		final String relDir = StringUtils.stripFromBeginning(relativeDirectory.replace('/', File.separatorChar), File.separator);
		sb.append(relDir);
		
		return Path.of(sb.toString());
	}
	
}
