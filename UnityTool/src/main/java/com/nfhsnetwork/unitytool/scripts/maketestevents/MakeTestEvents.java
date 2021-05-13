package com.nfhsnetwork.unitytool.scripts.maketestevents;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nfhsnetwork.unitytool.common.UnityContainer;
import com.nfhsnetwork.unitytool.exceptions.InvalidContentTypeException;
import com.nfhsnetwork.unitytool.io.UnityInterface;
import com.nfhsnetwork.unitytool.logging.Debug;
import com.nfhsnetwork.unitytool.types.NFHSGameObject;
import com.nfhsnetwork.unitytool.utils.Identify;
import com.nfhsnetwork.unitytool.utils.Util;

public class MakeTestEvents {
	
	
	private static Set<String> focusLines;
	private static String time;
	
	
	public MakeTestEvents(final Set<String> games)
	{
		
	}
	
	
	
	public void execute(final Set<String> games)
	{
		focusLines = new CopyOnWriteArraySet<>(games);
		
		time = Instant.now()
				.atZone(Util.TimeUtils.TIMEZONE_EST)
				.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		
		
		
		Set<JSONObject> s = focusLines.parallelStream()
				.map(t -> {
					Matcher m = NFHSGameObject.GAMEIDPATTERN.matcher(t);
					if (m.matches())
						return m.group();
					else 
						return null;
				}) // Now contains only game IDs instead of whole focus lines
				.filter(el -> el != null)
				.map(t -> {
					try {
						return UnityInterface.fetchEventFromUnity(t);
					} catch (IOException e) {
						e.printStackTrace();
						return null;
					}
				}) // Now contains only JSONObjects
				.filter(el -> el != null)
				.filter(Identify::isPixellot)
				.map(MakeTestEvents::buildOutput)
				.collect(Collectors.toUnmodifiableSet());
		
		
		Thread putThread = new Thread(() -> {
			s.forEach(el -> {
				
				try {
					UnityInterface.createEvent(el);
				} catch (IOException e1) {
					Debug.out("[DEBUG] {MakeTestEvents} failed to create event: " + el.toString());
					e1.printStackTrace();
				}
				
				try {
					Thread.sleep(1200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			});
		});
		putThread.start();
	}
	
	
	
	
	private static final JSONObject OUTPUTBASE = new JSONObject()
				   .put("city", "Atlanta")
				   .put("home", "1e639e62e4")
				   .put("creator", "unityTool") //default: eventConsole
				   .put("custom_title", false)
				   .put("duration", 0.5)
				   .put("game_type", "Regular Season")
				   .put("hls_startup_buffer_seconds", 60)
				   .put("level", "Varsity")
				   .put("location_to_be_determined", false)
				   .put("payment_required", false)
				   .put("school_keys", new JSONArray().put("276ac6a6d1"))
				   .put("publisher_key", "276ac6a6d1")
				   .put("state_code", "GA")
				   .put("status", "scheduled")
				   .put("teams_to_be_determined", true)
				   .put("time_to_be_determined", false)
				   .put("time_zone", "East") //should this be dynamic?
				   .put("unlisted", true)
				   .put("updated_by_user", UnityContainer.getUserEmail())
				   .put("updater", "unityTool") //default: eventConsole
				   .put("vod_unlisted", true);
	
	
	
	private static JSONObject buildOutput(JSONObject j) 
	{
		final JSONObject out = new JSONObject(OUTPUTBASE);
		
		out.put("created_by_user", UnityContainer.getUserEmail())
		   .put("producer_key", j.getJSONArray("publishers").getJSONObject(0).getJSONArray("broadcasts").getJSONObject(0).getString("producer_key"))
		   .put("sport", j.getString("sport")) 
		   .put("start_time", time); // 20 minutes out from current instant
		   
		
		
		return out;
		
	}
	
	
	
}
