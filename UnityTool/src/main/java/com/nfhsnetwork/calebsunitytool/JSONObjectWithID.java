package com.nfhsnetwork.calebsunitytool;

import org.json.JSONObject;

public class JSONObjectWithID {
	private final String gameid;
	private final JSONObject json;
	
	JSONObjectWithID(String gameid, JSONObject json)
	{
		this.gameid = gameid;
		this.json = json;
	}

	protected String getGameid() {
		return gameid;
	}

	protected JSONObject getJson() {
		return json;
	}
}
