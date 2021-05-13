package com.nfhsnetwork.unitytool.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public final class ManipJSON {
	public static JSONArray getEventTags(JSONObject j)
	{
		return j.getJSONArray("event_tags");
	}
}
