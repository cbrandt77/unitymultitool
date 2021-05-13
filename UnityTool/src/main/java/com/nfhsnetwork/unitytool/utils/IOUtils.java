package com.nfhsnetwork.unitytool.utils;

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
import java.nio.file.Path;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.nfhsnetwork.unitytool.exceptions.GameNotFoundException;
import com.nfhsnetwork.unitytool.exceptions.InvalidContentTypeException;
import com.nfhsnetwork.unitytool.logging.Debug;
import com.nfhsnetwork.unitytool.types.NFHSContentType;

public class IOUtils
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
			public static String fetchEventIDIfNeeded(final String id) throws GameNotFoundException, IOException, InvalidContentTypeException
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
			public static String fetchEventIDFromChild(final String id, final NFHSContentType contentType) throws GameNotFoundException, IOException
			{
				try
				{
					final JSONObject bdc_or_vod = readJSONFromURL(contentType.getEndpointURL() + id);
					final String gameID = bdc_or_vod.getString("game_key");
					
					if (gameID == null)
						throw new GameNotFoundException(id);
					
					return gameID;
				} 
				catch (JSONException e) {
					throw new GameNotFoundException(id, e);
				}
			}
		}

		public static class HttpUtils
		{
			public static String readHttpURLCon(HttpURLConnection http) throws IOException
			{
				final String out;
				try (final InputStream is = http.getInputStream();
					 final BufferedReader rd = new BufferedReader(new InputStreamReader(is)))
				{
					out = IOUtils.readAllFromReader(rd);
				}
				
				return out;
			}
			
			
		}

		
		public static String readFromFile(final String path) throws IOException
		{
			return readFromFile(Path.of(path).toFile());
		}
		
		public static String readFromFile(final Path path) throws IOException
		{
			return readFromFile(path.toFile());
		}
		
		public static String readFromFile(final File file) throws IOException
		{
			Debug.out("[DEBUG] {readFromFile} File path: " + file.getAbsolutePath());
			
			final String s;
			
			try (final BufferedReader rd = new BufferedReader(new FileReader(file)))
			{
				s = IOUtils.readAllFromReader(rd);
			}
			
//			if (s == null)
//				throw new FileNotFoundException("[UnityTool] {readFromFile} returned null for " + file.getAbsolutePath());
		
			return s;
		}

		public static String readAllFromReader(final Reader rd) throws IOException
		{
			final StringBuilder sb = new StringBuilder();
			
			int cp;
			while ((cp = rd.read()) != -1)
			{
				sb.append((char)cp);
			}
			
			return sb.toString();
		}

		public static JSONObject readJSONFromURL(final String url) throws JSONException, IOException
		{
//			final String jsonText;
//			try (final InputStream inputStream = new URL(url).openStream();
//					final BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream)))
//			{
//				jsonText = readAllFromReader(rd);
//			}
//			return new JSONObject(jsonText);
			
			final String out = httpGET(url);
			
			return new JSONObject(out);
		}
		
		public static String httpGET(final String url) throws IOException
		{
			return httpGET(url, null);
		}
		
		
		
		public static String httpGET(final String url, final Map<String, String> headers) throws IOException
		{
			HttpURLConnection http;
			
			while (true) {
				http = (HttpURLConnection)(new URL(url).openConnection());
				
				if (headers != null)
					headers.forEach(http::addRequestProperty);
				
				http.connect();
				
				if (http.getResponseCode() != 503)
					break;
				
				http.disconnect();
				
				Debug.out("[DEBUG] {httpGET} Status code 503 for " + url);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} 
			
			
			Debug.out("[DEBUG] {httpGET} http status code: " + http.getResponseCode());
			
			final String out = IOUtils.HttpUtils.readHttpURLCon(http);
			
			Debug.out("[DEBUG] {httpGET} response payload: " + out);
			
			http.disconnect();
			
			
			return out;
		}

		
		
		
		
		
		
		public static boolean printToFile(final String toPrint, final File file) throws IOException
		{
			if (file.isDirectory())
				throw new IOException("Cannot print to a directory.");
			
			
			try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
			{
				for (int i = 0, l = toPrint.length(); i < l; i++)
				{
					char c;
					if ((c = toPrint.charAt(i)) == '\n')
						writer.newLine();
					else
						writer.write(c);
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		/**
		 * 
		 * @param toPrint the String to print
		 * @param directory the directory containing the file
		 * @param fileName the filename to be written to
		 * @throws IOException if file could not be found or written to.
		 */
		public static void createAndPrintToFile(final String toPrint, final File directory, final String fileName) throws IOException
		{
			if (!directory.exists())
			{
				directory.mkdir();
			}
			
			final String s = directory.toString();
			if (!s.endsWith(File.separator)
					&& !fileName.startsWith(File.separator)) {
				printToFile(toPrint, new File(s + File.separator + fileName));
			}
			else
				printToFile(toPrint, new File(s + fileName));
			
		}
		
	}