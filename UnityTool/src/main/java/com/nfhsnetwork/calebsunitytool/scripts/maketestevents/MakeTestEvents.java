package com.nfhsnetwork.calebsunitytool.scripts.maketestevents;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nfhsnetwork.calebsunitytool.common.UnityContainer;

public class MakeTestEvents {
	
	private final List<String> gameList;
	//TODO goal: input ids and make test events for all pixellot games
	
	public MakeTestEvents(List<String> games)
	{
		this.gameList = games; 
	}
	
	
	private static final int NUMTHREADS = 4;
	private ExecutorService makeThreadPool()
	{
		ExecutorService e = Executors.newFixedThreadPool(4);
		return e;
	}
	
	
	private JSONObject buildOutputBase() 
	{
		JSONObject out = new JSONObject();
		
		out.put("city", "Atlanta")
		   .put("created_by_user", UnityContainer.getUserEmail())
		   .put("creator", "unityTool") //default: eventConsole
		   .put("custom_title", false)
		   .put("duration", 0.5)
		   .put("game_type", "Regular Season")
		   .put("hls_startup_buffer_seconds", 60)
		   //.put("home", "")
		   .put("level", "Varsity")
		   .put("location_to_be_determined", false)
		   .put("payment_required", false)
		   //.put("producer_key", "")
		   //.put("publisher_key", "")
		   //.put("school_keys", new JSONArray() {})
		   //.put("sport", "")
		   //.put("start_time", "2021-04-16T21:50:00-04:00")
		   .put("state_code", "GA")
		   .put("status", "scheduled")
		   .put("teams_to_be_determined", true)
		   .put("time_to_be_determined", false)
		   .put("time_zone", "East") //should this be dynamic?
		   .put("unlisted", true)
		   .put("updated_by_user", UnityContainer.getUserEmail())
		   .put("updater", "unityTool") //default: eventConsole
		   .put("vod_unlisted", true);
		
		return out;
		
	}
	
	
}
