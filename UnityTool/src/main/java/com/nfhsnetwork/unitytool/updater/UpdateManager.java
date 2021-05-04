package com.nfhsnetwork.unitytool.updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.protobuf.ByteString;
import com.nfhsnetwork.unitytool.common.UnityToolCommon;
import com.nfhsnetwork.unitytool.utils.Debug;
import com.nfhsnetwork.unitytool.utils.Util;
import com.nfhsnetwork.unitytool.utils.Util.IOUtils;

public class UpdateManager {
	private enum PackageType {
		ZIP(".zip", "application/zip", "zip-checksum"),
		JAR(".jar", "application/java-archive", "jar-checksum"),
		DMG(".dmg", "application/octet-stream", "dmg-checksum"),
		EXE(".exe", "application/octet-stream", "exe-checksum"),
		CHECKSUM(".chksum", "application/json", null);
		
		private final String extension;
		private final String encoding;
		private final String checksumName;
		
		PackageType(final String extension, final String encoding, final String checksum)
		{
			this.extension = extension;
			this.encoding = encoding;
			this.checksumName = checksum;
		}
		
		public String getExtension()
		{
			return this.extension;
		}
		
		public String getEncoding()
		{
			return this.encoding;
		}
		
		public String getChecksumName()
		{
			return this.checksumName;
		}
		
		public static PackageType idByExtension(String filename)
		{
			if (filename.endsWith(ZIP.extension))
				return ZIP;
			else if (filename.endsWith(JAR.extension))
				return JAR;
			else if (filename.endsWith(DMG.extension))
				return DMG;
			else if (filename.endsWith(EXE.extension))
				return EXE;
			else
				return null;
		}
		
	}


	private static final String DOWNLOAD_URL = "https://api.github.com/repos/ByThePowerOfScience/unitymultitool/releases/latest";
	
	private static final int THISRELEASE = 41851038;
	
	private static JSONObject checksumObject = null;
	
	
	
	
	public static void update()
	{
		int endcode;
		boolean shouldRetry = false;
		
		do {
			endcode = doUpdateProcess();
			
			if (endcode == UnityToolCommon.INTERRUPTED)
			{
				shouldRetry = showInterruptedDialog();
			}
		} while (shouldRetry);
		
		//TODO should this part continue or return here? I can't think rn...
	}
	
	
	
	
	private static boolean showInterruptedDialog() {
		int res = JOptionPane.showOptionDialog(null, "Update interrupted. Retry?", "Update Failed", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
				null, null, null);

		return res == JOptionPane.YES_OPTION;
	}




	private static Integer doUpdateProcess()
	{
		try {
			JSONObject releaseObject = getReleaseObject();
			
			if (needsUpdate(releaseObject))
			{
				Debug.out("[DEBUG] {doUpdateProcess} Needs update = true");
				
				JSONObject correctAsset = getCorrectAssetForSystem(releaseObject);
				
				boolean confirmDoUpdate = showUpdateAvailableDialog(correctAsset);
				
				if (confirmDoUpdate)
				{
					Debug.out("[DEBUG] {doUpdateProcess} Starting update procedure...");
					Package p = downloadPackage(correctAsset);
					
					if (doChecksum(p))
					{
						executeUpdateScript(p);
					}
					else
					{
						return UnityToolCommon.FAILED;
					}
				}
				
				return UnityToolCommon.SUCCESSFUL;
			}
			
			return UnityToolCommon.FAILED;
		} 
		catch (JSONException | IOException e)
		{
			e.printStackTrace();
			return UnityToolCommon.INTERRUPTED;
		}
		
	}
	


	private static void executeUpdateScript(Package p) {
		JOptionPane.showOptionDialog(null, "Update downloaded. Application will now restart.", "Update downloaded", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, null, null);
		
		try {
			Runtime.getRuntime().exec(p.getFile().getAbsolutePath());
			
			//System.exit(0); //TODO
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




	private static boolean needsUpdate(JSONObject releaseObject)
	{
		return releaseObject.getInt("id") != THISRELEASE;
	}
	
	
	
	private static JSONObject getReleaseObject() throws JSONException, IOException
	{
		return new JSONObject(IOUtils.httpGETString(DOWNLOAD_URL,
				Map.of("Accept", "application/vnd.github.v3+json",
						"User-Agent", "UnityMultiTool")));
	}
	
	
	
	private static JSONObject getCorrectAssetForSystem(final JSONObject releaseObject) throws FileNotFoundException
	{
		final JSONArray assets = releaseObject.getJSONArray("assets");
		
		JSONObject backup = null;
		
		JSONObject currentAsset;
		for (int i = 0, l = assets.length(); i < l; i++)
		{
			currentAsset = assets.getJSONObject(i);
			String currentName = currentAsset.getString("name");
			PackageType type = PackageType.idByExtension(currentName);
			
			if (UnityToolCommon.ISWINDOWS && type == PackageType.EXE)
				return currentAsset;
			else if (!UnityToolCommon.ISWINDOWS && type == PackageType.DMG)
				return currentAsset;
			else if (type == PackageType.ZIP)
				backup = currentAsset;
			else if (type == PackageType.CHECKSUM)
				checksumObject = currentAsset;
		}
		
		if (backup == null)
			throw new FileNotFoundException("Failed to find executable from GitHub release.");
		else
			return backup;
	}
	
	
	//TODO show update notes using JSONObject and use custom dialog box (kinda like OBS)
	private static boolean showUpdateAvailableDialog(JSONObject j)
	{
		int res = JOptionPane.showOptionDialog(null, "Update Available. Download?", "Update Available", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, null, null);

		return res == JOptionPane.YES_OPTION;
	}
	
	
	private static Package downloadPackage(JSONObject asset) throws IOException {
		
		String downloadUrl = asset.getString("url");
		String newFileName = asset.getString("name");
		
		PackageType type = PackageType.idByExtension(newFileName);
		
		Debug.out("[DEBUG] {downloadAndInstallPackage} download url: " + downloadUrl);
		
		
		File temp = Files.createTempFile("tmpinstaller-", type.getExtension()).toFile();
		
		
		HttpURLConnection http = getGitHubURLConnection(new URL(downloadUrl));
		http.addRequestProperty("Accept", type.getEncoding());
		
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		try (InputStream is = http.getInputStream();
				DigestInputStream dis = new DigestInputStream(is, md);
				FileOutputStream fos = new FileOutputStream(temp))
		{
			dis.transferTo(fos);
		}
		
		
		return new Package(temp, type, ByteString.copyFrom(md.digest()));
	}

	
	
	private static HttpURLConnection getGitHubURLConnection(URL url) throws MalformedURLException, IOException {
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		
		http.addRequestProperty("User-Agent", "UnityMultiTool");
		return http;
	}
	
	
	private static JSONObject getChecksumObject(JSONObject j) throws IOException
	{
		String url = j.getString("url");
		
		HttpURLConnection http = getGitHubURLConnection(new URL(url));
		http.addRequestProperty("Accept", "application/json");
		
		
		String response;
		try (InputStream is = http.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is)))
		{
			response = IOUtils.readAllFromReader(rd);
		}
		
		Debug.out("[DEBUG] {getChecksumObject} checksum json object: " + response);
		
		return new JSONObject(response);
	}
	
	
	
	private static boolean doChecksum(Package p) {
		if (checksumObject == null)
			return false;
		
		JSONObject checksumDownload;
		try {
			checksumDownload = getChecksumObject(checksumObject);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		ByteString checkAgainst = Util.hexStringToByteString(checksumDownload.getString(p.getType().getChecksumName()));
		
		return p.getChecksum().equals(checkAgainst);
	}
	
	
	
	
	private static final class Package {
		
		private final File f;
		private final PackageType t;
		private final ByteString s;
		
		Package(File file, PackageType type, ByteString checksum)
		{
			f = file;
			t = type;
			s = checksum;
		}
		
		
		File getFile()
		{
			return f;
		}
		
		ByteString getChecksum()
		{
			return s;
		}
		
		PackageType getType()
		{
			return t;
		}
		
	}
	
	
}
