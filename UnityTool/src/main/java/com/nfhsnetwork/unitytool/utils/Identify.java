package com.nfhsnetwork.unitytool.utils;

import org.json.JSONObject;

import com.nfhsnetwork.unitytool.types.NFHSGameObject;

public final class Identify {
	public static boolean isPixellot(NFHSGameObject n)
	{
		return n.isPixellot();
	}
	
	public static boolean isPixellot(JSONObject j)
	{
		return j.getBoolean("is_pixellot");
	}
}
