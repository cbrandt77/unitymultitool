package com.nfhsnetwork.calebsunitytool.types;

public enum NFHSContentType {
		GAME("gam", "games/"),
		EVENT("evt", "events/"),
		BROADCAST("bdc", "broadcasts/"),
		VOD("vod", "vods/");
	
	public static final String UNITY_BASE_URL = "http://unity.nfhsnetwork.com/v2/";
	private final String content_prefix;
	private final String unity_endpoint;
	
	NFHSContentType(String prefix, String endpoint)
	{
		content_prefix = prefix;
		unity_endpoint = endpoint;
	}
	
	public String getContentPrefix()
	{
		return content_prefix;
	}
	
	public String getEndpointExt()
	{
		return unity_endpoint;
	}
	
	public String getEndpointURL()
	{
		return UNITY_BASE_URL + unity_endpoint;
	}
	
	public boolean is(String id)
	{
		return id.startsWith(content_prefix);
	}
	
	public static boolean isValidEventId(String id)
	{
		return GAME.is(id) || EVENT.is(id);
	}
	
	public static NFHSContentType identify(String id) 
	{
		switch (id.substring(0, 3)) {
			case ("gam"):
				return GAME;
			case ("evt"):
				return EVENT;
			case ("bdc"):
				return BROADCAST;
			case ("vod"):
				return VOD;
			default:
				return null;
		}
	}
}