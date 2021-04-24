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
import com.nfhsnetwork.calebsunitytool.common.UnityToolCommon;
import com.nfhsnetwork.calebsunitytool.utils.Util;
import com.nfhsnetwork.calebsunitytool.utils.Util.IOUtils;

public class UpdateManager {
	private static final String CURRENTVERSION = "v1.0.0";
	private static final String VERSIONMODIFIERS = "snapshot";
	public static final String FULLVERSIONNAME = CURRENTVERSION + " - " + VERSIONMODIFIERS;
	
	private static final String token = System.getenv("token");
	private static final String VERSION_URL = "https://raw.githubusercontent.com/ByThePowerOfScience/unitymultitool/master/version.json";
	private static final String DOWNLOAD_URL = "https://api.github.com/repos/unitymultitool/releases/latest"; //TODO
	
	public static final String NEWFILENAME = ".UnityTool.exe";
	public static final String OLDFILENAME = "UnityTool.exe";
	
	private static final String DOWNLOADPATH;
	
	public static final String SCRIPTNAME = ((UnityToolCommon.ISWINDOWS) ? ".update.bat" : ".update.sh");
	
	static {
		DOWNLOADPATH = Util.getCurrentDirectory() + File.separator + NEWFILENAME;
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
				if (UnityToolCommon.isDebugMode) {
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
						// assume this is the modifiers? I don't think I'm scanning the mods tbh
						return CURRENT;
					}
				}
				if (isGreater)
				{
					source_checksum = Util.hexStringToByteString(version.getString("checksum"));
					
					if (UnityToolCommon.isDebugMode) {
						System.out.println("[DEBUG] {checkVersion} outdated");
					}
					
					return OUTDATED;
				}
				
				return CURRENT;
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to get version from version JSON?", e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (UnityToolCommon.isDebugMode) {
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
					if (UnityToolCommon.isDebugMode) {
						System.out.println("[DEBUG] {doUpdate} return true");
					}
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
		
		if (UnityToolCommon.isDebugMode) {
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


	
	
	//Print update script and run it, then delete update script
	//TODO merge download process into update script?
		//No, cause then I can't do the checksum

	public static final String UPDATESCRIPT = (UnityToolCommon.ISWINDOWS) 
	? "taskkill /F /IM " + OLDFILENAME 
			+ "\n" + "rm " + Util.getCurrentDirectory() + File.separator + OLDFILENAME
			+ "\n" + "rename " + Util.getCurrentDirectory() + File.separator + NEWFILENAME + " " + Util.getCurrentDirectory() + File.separator + OLDFILENAME
			+ "\n" + Util.getCurrentDirectory() + File.separator + OLDFILENAME
	: "echo 'script executed'" 
			+ "\n" + "currentprocess=$(pgrep '" + OLDFILENAME + "')" //TODO see if this works
			+ "\n" + "kill -s SIGINT $currentprocess"
			+ "\n" + "rm " + Util.getCurrentDirectory() + File.separator + OLDFILENAME
			+ "\n" + "mv " + Util.getCurrentDirectory() + File.separator + NEWFILENAME + " " + Util.getCurrentDirectory() + File.separator + OLDFILENAME
			+ "\n" + Util.getCurrentDirectory() + File.separator + OLDFILENAME;
	
	
	public static void printAndRunUpdateScript()
	{
		// Write a script file, call it, and make it delete itself?
		try 
		{
			File f = new File(Util.getCurrentDirectory().toString() + File.separator + UpdateManager.SCRIPTNAME);
			
			if (f.exists())
				executeUpdateCleanScript();
			else
				Util.IOUtils.printToFile(UpdateManager.UPDATESCRIPT, f);
		}
		catch (IOException e) {
			showFailedToCleanUpdateWindow();
			e.printStackTrace();
		}
	}
	
	private static void executeUpdateCleanScript()
	{
		ProcessBuilder pb = new ProcessBuilder()
				.directory(Util.getCurrentDirectory().toFile());
		
		
		if (UnityToolCommon.ISWINDOWS)
		{
			pb.command("cmd", Util.getCurrentDirectory() + File.separator + UpdateManager.SCRIPTNAME);
			if (UnityToolCommon.isDebugMode) {
				System.out.println("[DEBUG] {executePostUpdateScript} is windows");
			}
		}
		else
		{
			pb.command("sh", Util.getCurrentDirectory() + File.separator + UpdateManager.SCRIPTNAME);
			if (UnityToolCommon.isDebugMode) {
				System.out.println("[DEBUG] {executePostUpdateScript} is not windows");
			}
		}
		
		try {
			if (UnityToolCommon.isDebugMode) {
				System.out.println("[DEBUG] {executePostUpdateScript} executing");
			}
			pb.start();
		} catch (IOException e) {
			showFailedToCleanUpdateWindow();
			e.printStackTrace();
		}
		
	}
	
	public static void deleteUpdateScriptIfPresent() {
		File updatescript = new File(Util.getCurrentDirectory() + File.separator + UpdateManager.SCRIPTNAME);
		
		if (updatescript.exists())
		{
			if (UnityToolCommon.isDebugMode) {
				System.out.println("[DEBUG] {deleteUpdateScriptIfPresent} update script exists, deleting");
			}
			if (!updatescript.delete())
				showFailedToCleanUpdateWindow();
		}
	}
	
	private static void showFailedToCleanUpdateWindow()
	{
		JOptionPane.showOptionDialog(null, "Failed to clean update files.", "Update failed", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
				null, null, null);
	}
	
}
