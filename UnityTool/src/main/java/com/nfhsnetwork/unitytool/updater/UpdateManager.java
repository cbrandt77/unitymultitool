package com.nfhsnetwork.unitytool.updater;

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
import com.nfhsnetwork.unitytool.common.UnityToolCommon;
import com.nfhsnetwork.unitytool.utils.Debug;
import com.nfhsnetwork.unitytool.utils.Util;
import com.nfhsnetwork.unitytool.utils.Util.IOUtils;

public class UpdateManager {
	private static final String CURRENTVERSION = "v1.0.1";
	private static final String VERSIONMODIFIERS = "";
	public static final String FULLVERSIONNAME = CURRENTVERSION + " - " + VERSIONMODIFIERS;
	
	private static final String VERSION_URL = "https://api.github.com/repos/ByThePowerOfScience/unitymultitool/contents/version.json";
	private static final String DOWNLOAD_URL = "https://api.github.com/repos/ByThePowerOfScience/unitymultitool/releases/latest";
	
	
	public static String NEWFILENAME;
	public static final String OLDFILENAME = UpdateManager.class.getProtectionDomain().getCodeSource().getLocation().getFile();
	
	private static final String DOWNLOADPATH;
	
	public static final String SCRIPTNAME = ((UnityToolCommon.ISWINDOWS) ? ".update.bat" : ".update.sh");
	
	static { //TODO un-hardcode this
		DOWNLOADPATH = Util.getCurrentDirectory() + File.separator + "bin" + File.separator + NEWFILENAME;
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
		Debug.out("[DEBUG] {checkAndGetUpdates} init check");
		
		
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
	
	//TODO restructure so that it just gets the latest release json, then reads the tag for the version instead of having a version.json
	//how to do checksum in that case?
	
	/**
	 * Side effects: sets source_checksum.
	 * @return UpdateManager.CURRENT/OUTDATED/INTERRUPTED
	 */
	private static int checkVersion()
	{
		try {
			Debug.out("[DEBUG] {checkVersion} checking version");
			
			
			JSONObject version = new JSONObject(getVersionJSON());
			
			
			Debug.out("[DEBUG] {checkVersion} version json: " + version.toString());
			
			
			String versionString = version.getString("current");
			String modifierString = version.getString("modifiers");
			
			if (CURRENTVERSION.equals(versionString)
					&& VERSIONMODIFIERS.equals(modifierString))
			{
				Debug.out("[DEBUG] {checkVersion} is current version");
				
				
				return CURRENT;
			}
			else
			{
				String[] versionarr = versionString.substring(1).split("[.|-]");
				String[] current = CURRENTVERSION.substring(1).split("[.|-]");
				
				boolean isGreater = false;
				for (int i = 0, l = versionarr.length; i < l; i++) 
				{
					try {
//						Debug.out("[DEBUG] {checkVersion} versionarr[i] = " + Integer.valueOf(versionarr[i]));
//						Debug.out("current[i] = " + Integer.valueOf(current[i]));
						
						if (Integer.valueOf(versionarr[i]) > Integer.valueOf(current[i])) //TODO change to only accept versions without modifiers. Also fix version control system completely.
						{
							isGreater = true;
							continue;
						}
					} catch (NumberFormatException e) {
						// assume this is the modifiers? I don't think I'm scanning the mods tbh
						Debug.out("[DEBUG] {checkVersion} NumFormatException");
						
						return CURRENT;
					}
				}
				
				Debug.out("[DEBUG] {checkVersion} isGreater = " + isGreater);
				
				
				if (isGreater)
				{
					source_checksum = Util.hexStringToByteString(version.getString("checksum"));
					
					Debug.out("[DEBUG] {checkVersion} version is outdated");
					
					
					return OUTDATED;
				}
				
				Debug.out("[DEBUG] {checkVersion} reach end return current");
				
				return CURRENT;
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to get version from version JSON?", e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Debug.out("[DEBUG] {checkVersion} interrupted");
		
		
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
					Debug.out("[DEBUG] {doUpdate} return true");
					
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
		//TODO show dialog box and progress bar for download
		
		JSONObject githubAPIObject = new JSONObject(
				Util.IOUtils.httpGET(
						DOWNLOAD_URL,
						Map.of("Accept", "application/vnd.github.v3+json",
								"User-Agent", "UnityMultiTool")))
				.getJSONArray("assets").getJSONObject(0);
		
		String downloadUrl = githubAPIObject.getString("url");
		
		UpdateManager.NEWFILENAME = githubAPIObject.getString("name");
		
		Debug.out("[DEBUG] {downloadFile} download url: " + downloadUrl);
		
		
		URL url;
		try {
			url = new URL(downloadUrl);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return null;
		}
		
		
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		http.addRequestProperty("Accept", "application/octet-stream"); //TODO application/exe
		http.addRequestProperty("User-Agent", "UnityMultiTool");
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
		try (InputStream is = http.getInputStream();
		     DigestInputStream dis = new DigestInputStream(is, md);
				FileOutputStream fos = new FileOutputStream(DOWNLOADPATH)) 
		{
			dis.transferTo(fos);
		}
		
		return md.digest();
	}
	
	private static Map<String, String> makeAuthMap()
	{
		return Map.of("Accept", "application/vnd.github.v3+json",
					  "User-Agent","ByThePowerOfScience");
		
//		Map<String, String> headers = new HashMap<String, String>();
//		
//		headers.put("Authorization", "token " + token);
//					 headers.put( "Accept", "application/vnd.github.v3.raw");
//					  headers.put("User-Agent", "bythepowerofscience");
//					  return headers;
	}
	
	
	private static String getVersionJSON() throws IOException
	{
		
		return IOUtils.httpGET(
				VERSION_URL, 
				Map.of("Accept", "application/vnd.github.v3.raw+json",
				  "User-Agent","ByThePowerOfScience"));
		
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
			+ "\n" + "kill -s 2 '$currentprocess'"
			+ "\n" + "mv '" + Util.getCurrentDirectory() + File.separator + NEWFILENAME + "' '" + Util.getCurrentDirectory() + File.separator + OLDFILENAME + "'"
			+ "\n" + "'" + Util.getCurrentDirectory() + File.separator + OLDFILENAME + "'";
	
	
	public static void printAndRunUpdateScript()
	{
		int res = JOptionPane.showOptionDialog(null, "running update script", "", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, null, null);

		if (res != 4) {
			
		}
		
		
		// Write a script file, call it, and make it delete itself?
		try 
		{
			File f = new File(Util.getCurrentDirectory().toString() + File.separator + UpdateManager.SCRIPTNAME);
			
			if (!f.exists()) {
				Util.IOUtils.printToFile(UpdateManager.UPDATESCRIPT, f);
			}
			
			executeUpdateCleanScript();
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
			Debug.out("[DEBUG] {executePostUpdateScript} is windows");
		}
		else
		{
			pb.command("sh", Util.getCurrentDirectory() + File.separator + UpdateManager.SCRIPTNAME);
			Debug.out("[DEBUG] {executePostUpdateScript} is not windows");
			
		}
		
		try {
			Debug.out("[DEBUG] {executePostUpdateScript} executing");
			
			pb.start();
			
			System.exit(0); //TODO should I do this???
		} catch (IOException e) {
			showFailedToCleanUpdateWindow();
			e.printStackTrace();
		}
		
	}
	
	public static void deleteUpdateScriptIfPresent() {
		File updatescript = new File(Util.getCurrentDirectory() + File.separator + UpdateManager.SCRIPTNAME);
		
		if (updatescript.exists())
		{
			Debug.out("[DEBUG] {deleteUpdateScriptIfPresent} update script exists, deleting");
			
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
