package com.nfhsnetwork.calebsunitytool.updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.protobuf.ByteString;
import com.nfhsnetwork.calebsunitytool.utils.Util;
import com.nfhsnetwork.calebsunitytool.utils.Util.IOUtils;

public class UpdateManager {
	private static final String CURRENTVERSION = "v1.0.0";
	private static final String VERSIONMODIFIERS = "snapshot";
	public static final String FULLVERSIONNAME = CURRENTVERSION + " - " + VERSIONMODIFIERS;
	
	//TODO get token to fetch this
	private static final String VERSION_URL = "https://raw.githubusercontent.com/ByThePowerOfScience/unitymultitool/master/version.json";
	
	private static final String DOWNLOADPATH; //TODO
	
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
			JSONObject version = IOUtils.readJSONFromURL(VERSION_URL);
			
			if (CURRENTVERSION.equals(version.getString("current"))
					&& VERSIONMODIFIERS.equals(version.getString("modifiers")))
				return CURRENT;
			
			source_checksum = Util.hexStringToByteString(version.getString("checksum"));
			
			return OUTDATED;
		} 
		catch (JSONException e) {
			throw new RuntimeException("Failed to get version from version JSON??", e);
		} catch (IOException e) {
			e.printStackTrace();
			return INTERRUPTED;
		}
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
			} catch (NoSuchAlgorithmException | IOException e) {
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
	private static byte[] downloadFile() throws IOException, NoSuchAlgorithmException
	{
		URL url;
		try {
			url = new URL(VERSION_URL);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return null;
		}
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		try (InputStream is = Files.newInputStream(Paths.get(url.getPath()));
		     DigestInputStream dis = new DigestInputStream(is, md);
				FileOutputStream fos = new FileOutputStream(new File(DOWNLOADPATH))) 
		{
			dis.transferTo(fos);
		} 
		
		return md.digest();
	}
	
	
	
	
	
	
	
	
}
