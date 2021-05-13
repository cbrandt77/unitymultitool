package com.nfhsnetwork.unitytool.common;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import com.nfhsnetwork.unitytool.json.JSONObjectNoComments;
import com.nfhsnetwork.unitytool.logging.Debug;
import com.nfhsnetwork.unitytool.utils.IOUtils;
import com.nfhsnetwork.unitytool.utils.Util;

public final class Config {

	private static JSONObject CONFIG_JSON;
	
	private static boolean configNeedsRegen = false;
	
	
	
	static {
		
	}
	
	
	private static final String CONFIGPATH = "cfg/config.json";
	private static final String DEFAULTCONFIG_PATH = "cfg/default_config.json";
	
	
	
	public static void init()
	{
		CONFIG_JSON = readConfig();
		Debug.out(CONFIG_JSON.toString());
	}
	
	private static JSONObject readConfig()
	{
		try {
			Debug.out("[DEBUG] {readConfig} Reading config.");
			return new JSONObjectNoComments(IOUtils.readFromFile(Util.getAbsoluteDir(CONFIGPATH).toFile()));
		} catch (JSONException | IOException e) {
			return new JSONObject(HARDCODEDCONFIG);
		}
	}
	
	
	
	
	private static final String KEY_OUTPUTS = "outputs";
	private static final String KEY_UI = "view";
	private static final String KEY_IO = "io";
	
	
	public static final JSONObject getUIConfig()
	{
		return CONFIG_JSON.getJSONObject(KEY_UI);
	}
	
	public static final JSONObject getIOConfig()
	{
		return CONFIG_JSON.getJSONObject(KEY_IO);
	}
	
	public static final JSONObject getOutputConfig()
	{
		return CONFIG_JSON.getJSONObject(KEY_OUTPUTS);
	}
	
	
	
	
	public static final boolean shouldSaveToken()
	{
		try {
			return getIOConfig().getBoolean("save_sso_cookie");
		} catch (JSONException e) {
			e.printStackTrace();
			configNeedsRegen = true;
			return false;
		}
	}
	
	public static final DateTimeFormatter getOutputDTF()
	{
		return DateTimeFormatter.ofPattern(CONFIG_JSON.getJSONObject("outputs").getString("timestamp_format"));
	}
	
	public static final String getOutputPrefix()
	{
		return getOutputConfig().getString("prefix");
	}
	
	
	
	
	
	private static final File COOKIEFILE = Util.getAbsoluteDir("bin/login/token").toFile();
	
	public static final File getCookieFile()
	{
		return COOKIEFILE;
	}
	
	
	
	
	
	private static final String HARDCODEDCONFIG = "{"
			+ "	\"version\": \"v1.0.2\","
			+ "	"
			+ "	\"is_first_launch\": true,"
			+ "	"
			+ "	"
			+ "	\"outputs\": {"
			+ "		"
			+ "		\"save_focus_compare\": true,"
			+ "		"
			+ "		"
			+ "		\"dir\": \"/outputs\","
			+ "		"
			+ "		\"prefix\": \"Output \","
			+ "		"
			+ "		"
			+ "		\"timestamp_format\": \"yyyy-MM-dd HH-mm-ss\""
			+ "	},"
			+ "	"
			+ "	"
			+ "	\"view\": {"
			+ "		\"game_list_display_info\": 0"
			+ "	},"
			+ "	"
			+ "	\"io\": {"
			+ "		\"save_sso_cookie\": true"
			+ "	}"
			+ "	"
			+ "}";
	
	
}
