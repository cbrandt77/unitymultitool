package com.nfhsnetwork.calebsunitytool.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.protobuf.ByteString;
import com.nfhsnetwork.calebsunitytool.common.UnityToolCommon;
import com.nfhsnetwork.calebsunitytool.exceptions.GameNotFoundException;
import com.nfhsnetwork.calebsunitytool.exceptions.InvalidContentTypeException;
import com.nfhsnetwork.calebsunitytool.exceptions.NullFieldException;
import com.nfhsnetwork.calebsunitytool.types.NFHSContentType;

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
			CURRENTDIRECTORY = Path.of(truncateFilePath(Util.class.getProtectionDomain().getCodeSource().getLocation().getPath()));
		}
		else {
			boolean worked = false;
			Path p = null;
			try {
				p = Path.of(truncateFilePath(Files.readSymbolicLink(Path.of("/proc/self/exe")).toString())); //TODO verify
				worked = true;
			} catch (IOException e) {
				e.printStackTrace();
				worked = false;
			}
			
			if (worked) //workaround to get java to stop yelling at me about reassigned final vars
				CURRENTDIRECTORY = p;
			else
				CURRENTDIRECTORY = Path.of(truncateFilePath(Util.class.getProtectionDomain().getCodeSource().getLocation().getPath()));
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
			if (UnityToolCommon.isDebugMode) {
				System.out.println("[DEBUG] {parseClubCSV} not div by 2 for id " + s);
			}
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

		public static class FetchID 
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
					JSONObject bdc_or_vod = readJSONFromURL(contentType.getEndpointURL() + id);
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
		
		public static String httpGET(String url, Map<String, String> headers) throws IOException
		{
			HttpURLConnection http = (HttpURLConnection)new URL(url).openConnection();
			
			if (headers != null)
				headers.forEach(http::addRequestProperty);
			
			http.connect();
			String out;
			try (InputStream is = http.getInputStream();
				 BufferedReader rd = new BufferedReader(new InputStreamReader(is)))
			{
				out = readAllFromReader(rd);
			}
			
			return out;
		}

		public static boolean printToFile(String toPrint, File file) throws IOException
		{
			if (file.isDirectory())
				throw new IOException("Cannot print to directory.");
			
			
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
			{
				for (int i = 0, l = toPrint.length(); i < l; i++)
				{
					char c;
					if ((c = toPrint.charAt(i)) == '\n')
						writer.newLine();
					else
						writer.write(c);
				}
				
				return true;
			} 
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		
		/**
		 * 
		 * @param toPrint
		 * @param directory
		 * @param fileName
		 * @return
		 * @throws IOException
		 */
		public static boolean createFolderPrintFile(String toPrint, File directory, String fileName) throws IOException
		{
			if (!directory.isDirectory())
				throw new IOException("Invalid directory provided.");
			
			if (!directory.exists())
			{
				directory.mkdir();
			}
			String s = directory.toString();
			if (!s.endsWith(File.separator)
					&& !fileName.startsWith(File.separator)) {
				printToFile(toPrint, new File(s + File.separator + fileName));
			}
			else
				printToFile(toPrint, new File(s + fileName));
			
			return true;
			
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




	public static Path getCurrentDirectory()
	{
		return CURRENTDIRECTORY;
	}
	
}
