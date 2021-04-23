package com.nfhsnetwork.calebsunitytool.updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.protobuf.ByteString;
import com.nfhsnetwork.calebsunitytool.Wrapper;
import com.nfhsnetwork.calebsunitytool.utils.Util;
import com.nfhsnetwork.calebsunitytool.utils.Util.IOUtils;

public class UpdateManager {
	private static final String CURRENTVERSION = "v1.0.0";
	private static final String VERSIONMODIFIERS = "snapshot";
	public static final String FULLVERSIONNAME = CURRENTVERSION + " - " + VERSIONMODIFIERS;
	
	//TODO hide token
	private static final String token = "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";
	private static final String VERSION_URL = "https://raw.githubusercontent.com/ByThePowerOfScience/unitymultitool/master/version.json";
	private static final String DOWNLOAD_URL = "https://api.github.com/repos/unitymultitool/releases/latest"; //TODO
	
	
	private static final String DOWNLOADPATH;
	
	static {
		DOWNLOADPATH = Util.getCurrentDirectory();
	}
	
	private static ByteString source_checksum;
	
	
	
	
	//STATUS
	public static final int CURRENT = 2;
	public static final int INTERRUPTED = 3;
	public static final int OUTDATED = 4;
	
	/**
	 * 
	 * @return True if application update was downloaded.
	 */
	public static boolean checkAndGetUpdates() 
	{
		switch (checkVersion()) {
			case CURRENT:
				return false;
			case INTERRUPTED:
				versionCheckInterrupted();
				return false;
			case OUTDATED:
				return doUpdate();
			default:
				return false;
		}
	}
	
	
	/**
	 * Side effects: sets source_checksum.
	 * @return UpdateManager.CURRENT/OUTDATED/INTERRUPTED
	 */
	private static int checkVersion()
	{
		try {
			JSONObject version = new JSONObject(IOUtils.httpGET(VERSION_URL, makeAuthMap()));
			String versionString = version.getString("current");
			String modifierString = version.getString("modifiers");
			if (CURRENTVERSION.equals(versionString)
					&& VERSIONMODIFIERS.equals(modifierString))
			{
				if (Wrapper.isDebugMode) {
					System.out.println("[DEBUG] {checkVersion} current version");
					System.out.println("[DEBUG] {checkVersion} version json: " + version.toString());
				}
				
				return CURRENT;
			}
			else
			{
				String[] versionarr = versionString.split("[.|-]");
				String[] current = CURRENTVERSION.split("[.|-]");
				
				boolean isGreater = false;
				for (int i = 0, l = versionarr.length - 1; i < l; i++) 
				{
					try {
						if (Integer.valueOf(versionarr[i]) > Integer.valueOf(current[i]) && modifierString.equals(""))
						{
							isGreater = true; // still need to check for modifiers
							continue;
						}
					} catch (NumberFormatException e) {
						// assume this is the modifiers?
						return CURRENT;
					}
				}
				if (isGreater)
				{
					source_checksum = Util.hexStringToByteString(version.getString("checksum"));
					
					if (Wrapper.isDebugMode) {
						System.out.println("[DEBUG] {checkVersion} outdated");
					}
					
					return OUTDATED;
				}
				
				return CURRENT;
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to get version from version JSON??", e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Wrapper.isDebugMode) {
			System.out.println("[DEBUG] {checkVersion} interrupted");
		}
		
		return INTERRUPTED;
	}
	
	
	
	
	private static boolean doUpdate()
	{
		ByteString filesum;
		do {
			try {
				filesum = ByteString.copyFrom(downloadFile());
				if (filesum != null && filesum.equals(source_checksum))
				{
					return true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while (requestRetry());
		
		return false;
	}
	
	/**
	 * 
	 * @return should retry update.
	 */
	private static boolean requestRetry() 
	{
		int res = JOptionPane.showConfirmDialog(null, "Update failed. Retry?", "Update Failed", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
		
		if (res == JOptionPane.YES_OPTION)
			return true;
		
		return false;
	}
	
	private static void versionCheckInterrupted()
	{
		JOptionPane.showOptionDialog(null, "Failed to fetch version.", "Update Failed", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, null, null);

		return;
	}
	
	
	/**
	 * Reads file from URL, and puts it in the directory.
	 * @param url
	 * @return MD5 hash
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	private static byte[] downloadFile() throws IOException
	{
		URL url;
		try {
			url = new URL(DOWNLOAD_URL);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return null;
		}
		
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		http.addRequestProperty("Authorization", "token " + token);
		
		if (Wrapper.isDebugMode) {
			System.out.println("[DEBUG] {downloadFile} downloading file.");
		}
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
		try (InputStream is = http.getInputStream();
		     DigestInputStream dis = new DigestInputStream(is, md);
				FileOutputStream fos = new FileOutputStream(new File(DOWNLOADPATH))) 
		{
			dis.transferTo(fos);
		}
		
		
		
		
		
		return md.digest();
	}
	
	
	
	private static Map<String, String> makeAuthMap()
	{
		return Map.of("Authorization", "token " + token);
	}
	
	
	
	
	
}
