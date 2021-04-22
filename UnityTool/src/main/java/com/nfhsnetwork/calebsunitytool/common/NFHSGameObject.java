package com.nfhsnetwork.calebsunitytool.common;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nfhsnetwork.calebsunitytool.exceptions.GameNotFoundException;
import com.nfhsnetwork.calebsunitytool.exceptions.InvalidContentTypeException;
import com.nfhsnetwork.calebsunitytool.exceptions.NullFieldException;
import com.nfhsnetwork.calebsunitytool.io.UnityInterface;
import com.nfhsnetwork.calebsunitytool.scripts.focuscompare.FocusCompareScript;
import com.nfhsnetwork.calebsunitytool.utils.Util.TimeUtils;


public class NFHSGameObject
{ 
	
	public static final Pattern GAMEIDPATTERN = Pattern.compile("((?:gam){1}[a-f\\d]{10})");
	public static final Pattern BDCIDPATTERN = Pattern.compile("((?:bdc){1}[a-f\\d]{10})");
	public static final Pattern EVENTIDPATTERN = Pattern.compile("((?:evt){1}[a-f\\d]{10})");
	
	private static final int numDigitsInID = 13;
	
	//TODO implement new regex and Builder method of initialization.
	public static NFHSGameObject buildFromIdOrUrl(String in) throws InvalidContentTypeException, GameNotFoundException
	{
		if (in.length() < 13)
			throw new InvalidContentTypeException("String too short");
		
		String event_id = null;
		String bdc_id = null;
		NFHSContentType n = null;
		
		int startIndex;
		if ((startIndex = in.indexOf("gam")) != -1) 
		{
			String temp = in.substring(startIndex, startIndex + numDigitsInID);
			
			if (GAMEIDPATTERN.matcher(temp).matches())
			{
				event_id = temp;
				n = NFHSContentType.GAME;
				
				//System.out.println("[DEBUG] game id detected: " + event_id);
				
				if (NFHSContentType.identify(event_id) != NFHSContentType.GAME)
					throw new RuntimeException("[DEBUG] game id mismatch, bad substring | event id: " + event_id + " | input: " + in);
				//TODO debug info ^
			}
		}
		
		if ((startIndex = in.indexOf("evt")) != -1) 
		{
			String temp = in.substring(startIndex, startIndex + numDigitsInID);
			
			if (EVENTIDPATTERN.matcher(temp).matches())
			{
				event_id = temp;
				n = NFHSContentType.EVENT;
				//System.out.println("[DEBUG] event id detected: " + event_id);
			}
		}
		
		if ((startIndex = in.indexOf("bdc")) != -1) 
		{
			String temp = in.substring(startIndex, startIndex + numDigitsInID);
			
			if (BDCIDPATTERN.matcher(temp).matches()) 
			{
				bdc_id = temp;
				//System.out.println("[DEBUG] bdc id detected: " + bdc_id);
				if (event_id == null) 
				{
					try 
					{
						event_id = UnityToolCommon.IDConversion.fetchEventIDFromChild(bdc_id, NFHSContentType.BROADCAST);
					} 
					catch (IOException e) {
						e.printStackTrace();
						throw new GameNotFoundException(bdc_id, e);
					}
				}
			}
		}
			
		
		if (event_id == null && bdc_id == null) {
			System.out.println("[DEBUG] {buildFromIdOrUrl} invalidcontenttypeexception thrown for " + in);
			throw new InvalidContentTypeException(in, "gam/evt/bdc/vod");
		}
		
		//TODO figure out when to fetch json and when not to
		JSONObject jGet = null;
		try {
			jGet = UnityInterface.fetchEventFromUnity(event_id);
		} catch (IOException e) {
			throw new GameNotFoundException(event_id, e);
		}
		
		return new NFHSGameObject(
				event_id,
				new String[] {bdc_id},
				jGet,
				n);
	}
	
	public static final Pattern FOCUS_DATE_PATTERN = Pattern.compile("((?:\\d{1,2})/(?:\\d{1,2})/(?:\\d{2,4}))");
	public static final Pattern FOCUS_TIME_PATTERN = Pattern.compile("((?:\\d){1,2}:(?:\\d){1,2}:(?:\\d){1,2} (?:PM|AM|pm|am))");
	
	public static NFHSGameObject buildFromFocusSheetLine(String focusLine) throws InvalidContentTypeException
	{
		String[] temp = focusLine.split("\\t", -1);
		if (temp.length < FocusCompareScript.NUM_COLUMNS_FROM_FEL)
		{
			throw new InvalidContentTypeException(focusLine);
		}
		
		Matcher m;
		
		String gameID;
		m = GAMEIDPATTERN.matcher(focusLine);
		
		if (m.find()) 
			gameID = m.group();
		else {
			m = EVENTIDPATTERN.matcher(focusLine);
			if (m.find())
				gameID = m.group();
			else {
				gameID = null;
			}
		}	
		
		
		
		String[] broadcastKey;
		m = BDCIDPATTERN.matcher(focusLine);
		if (m.find())
			broadcastKey = new String[] { m.group() };
		else {
			System.out.println("[DEBUG] {buildFromFocusSheetLine} bdc key not found");
			broadcastKey = null;
		}

		
		String time;
		m = FOCUS_TIME_PATTERN.matcher(focusLine);
		if (m.find())
			time = m.group();
		else {	
			System.out.println("[DEBUG] {buildFromFocusSheetLine} time not found");
			time = null;
		}
		
		String date;
		m = FOCUS_DATE_PATTERN.matcher(focusLine);
		if (m.find()) {
			date = m.group();
		} else {
			date = null;
			System.out.println("[DEBUG] {buildFromFocusSheetLine} date not found");
		}
		
		if (time == null || date == null)
			System.out.println("[DEBUG] {buildFromFocusSheetLine} null date or time for line:\n" + focusLine);
		
		LocalDateTime focusDateTime = LocalDateTime.parse(date + " " + time, dtf_focusDT);
		
		
		String title;
		try {
			title = temp[2];
		} catch (Exception e) {
			title = null;
			e.printStackTrace();
		}
		
		String type;
		try {
			type = temp[3];
		} catch (Exception e) {
			type = null;
			e.printStackTrace();
		}
		
		String status;
		try {
			status = temp[6];
		} catch (Exception e) {
			status = null;
			e.printStackTrace();
		}
		
		System.out.println("[DEBUG] game id detected: " + gameID + "\n[DEBUG] bdc id detected: " + ((broadcastKey == null) ? "null" : broadcastKey[0]));
		
		
		NFHSGameObject n;
		try {
			n = Builder.newBuilder()
									  .setEventId(gameID)
									  .addBroadcastKeys(broadcastKey)
									  .setFocusDateTime(focusDateTime)
									  .setFocusStatus(status)
									  .setFocusTitle(title)
									  .setFocusMonitoringType(type)
									  .build();
		} catch (GameNotFoundException | InvalidContentTypeException | IOException e) {
			e.printStackTrace();
			return new NullNFHSObject(gameID, e);
		}
		
		
		return n;
	}
	
	
	
	
	
	
	
	
	private final JSONObject game_json;
	private final String game_id;
	private final String[] bdc_ids;
	private final NFHSContentType content_type;
	private final TerritoryManagers terr_mgr;
	
	
	//Only initialized if created by focus data import
	private boolean isFocusEvent = false;
	private LocalDateTime focus_dateTime;
	private String focus_title;
	private String focus_status;
	private String focus_type;
	
	private JSONObject bdcstate_json;
	
	
	
	//TODO figure out what should be cached and what can be fetched ad-hoc from the JSON
	
	public NFHSGameObject(String game_id, String[] bdc_ids, JSONObject j, NFHSContentType n)
	{
		this.game_id = game_id;
		this.bdc_ids = bdc_ids;
		this.game_json = j;
		this.content_type = n;
		
		this.terr_mgr = TerritoryManagers.getTerritoryManager(getStateCode());
		
		fetchAndSetBdcStateJSON();
	}
	
	private String getStateCode() {
		return this.game_json.getString("state_code");
	}


	private void fetchAndSetBdcStateJSON()
	{
		JSONObject stateJSON;
		try {
			stateJSON = UnityInterface.fetchBdcStateJSON(this.bdc_ids[0]);
		} catch (Exception e) {
			System.out.println("[DEBUG] {fetchAndSetBdcStateJSON} exception thrown for game " + this.game_id);
			e.printStackTrace();
			return;
		}
		
		this.bdcstate_json = stateJSON;
	}
	
	
	
	
	
	public static final DateTimeFormatter dtf_focusDT = new DateTimeFormatterBuilder()
			.parseCaseInsensitive()
			.appendPattern("M/d/yyyy h:mm:ss a")
			.toFormatter(Locale.US);
	
	
	public String getProducerName()
	{
		JSONArray broadcasts;
		try {
			broadcasts = getBroadcasts();
		} catch (NullFieldException e1) {
			return null;
		}
		
		if (broadcasts.length() == 1)
		{
			try {
				return broadcasts.getJSONObject(0).getString("producer_name");
			} catch (JSONException e) {
				return null;
			}
		}
		
		String output = "";
		for (int i = 0, length = broadcasts.length(); i < length; i++)
		{
			try {
				output += broadcasts.getJSONObject(i).getString("producer_name");
			} catch (JSONException e) {
				continue;
			}
			if (i != length - 2)
				output += " | ";
		}
		
		return output;
	}
	
	
	public JSONArray getBroadcasts() throws NullFieldException 
	{
		try {
			return this.game_json.getJSONArray("publishers").getJSONObject(0).getJSONArray("broadcasts");
		} catch (JSONException e) {
			throw new NullFieldException("broadcasts", e);
		}
	}
	
	public JSONObject getFirstBroadcast() throws NullFieldException
	{
		try {
			return this.game_json.getJSONArray("publishers").getJSONObject(0).getJSONArray("broadcasts").getJSONObject(0);
		} 
		catch (JSONException e) {
			throw new NullFieldException("broadcasts", e);
		}
	}
	
	public static JSONObject getFirstBroadcast(JSONObject game) throws JSONException
	{
		return game.getJSONArray("publishers").getJSONObject(0).getJSONArray("broadcasts").getJSONObject(0);
	}
	
	
	
	public LocalDateTime getDateTime()
	{
		return TimeUtils.convertDateTimeToEST(this.game_json.getString("start_time"));
	}
	
	public NFHSContentType getContentType()
	{
		return this.content_type;
	}
	
	public String getGameID()
	{
		return this.game_id;
	}
	
	public boolean getIsDeleted()
	{
		return this.game_json.getBoolean("is_deleted");
	}
	
	public String[] getBdcIDs()
	{
		return this.bdc_ids;
	}
	
	public JSONObject getJSONObject()
	{
		return this.game_json;
	}

	/**
	 * @return the focus_dateTime
	 */
	public LocalDateTime getFocusDateTime() {
		return focus_dateTime;
	}

	/**
	 * @param focus_dateTime the focus_dateTime to set
	 */
	public void setFocusDateTime(LocalDateTime focus_dateTime) {
		this.focus_dateTime = focus_dateTime;
	}

	/**
	 * @return the focus_title
	 */
	public String getFocusTitle() {
		return focus_title;
	}

	/**
	 * @param focus_title the focus_title to set
	 */
	public void setFocusTitle(String focus_title) {
		this.focus_title = focus_title;
	}

	/**
	 * @return the focus_status
	 */
	public String getFocusStatus() {
		return focus_status;
	}

	/**
	 * @param focus_status the focus_status to set
	 */
	public void setFocusStatus(String focus_status) {
		this.focus_status = focus_status;
	}

	/**
	 * @return the focus_type
	 */
	public String getFocusEventType() {
		return focus_type;
	}

	/**
	 * @param focus_type the focus_type to set
	 */
	public void setFocusEventType(String focus_type) {
		this.focus_type = focus_type;
	}
	
	public void setFocusEvent(boolean isFocusEvent)
	{
		this.isFocusEvent = isFocusEvent;
	}
	
	public boolean isFocusEvent()
	{
		return this.isFocusEvent;
	}
	
	public String getGender() {
		try {
			return this.game_json.getString("gender");
		} catch (JSONException e) {
			e.printStackTrace();
			return "Not specified.";
		}
	}
	
	public String getCompLevel() {
		try {
			return this.game_json.getString("level");
		} catch (JSONException e) {
			e.printStackTrace();
			return "Not specified.";
		}
	}
	
	public JSONArray getParticipants() {
		try {
			return this.game_json.getJSONArray("participants");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}



	public String getSportType() {
		try {
			return this.game_json.getString("sport");
		} catch (JSONException e) {
			e.printStackTrace();
			return "Not specified.";
		}
	}



	public String getLocation() {
		try {
			return this.game_json.getString("city") + ", " + this.game_json.getString("state_code");
		} catch (JSONException e) {
			e.printStackTrace();
			return "Not specified.";
		}
	}



	public String getRedirectID() {
		try {
			return this.game_json.getString("event_redirect");
		} catch (JSONException e) {
			return "Not specified.";
		}
	}
	
	public String getCompType() {
		try {
			return this.game_json.getString("game_type");
		} catch (JSONException e) {
			e.printStackTrace();
			return "Not specified.";
		}
	}
	
	
	
	private enum TerritoryManagers 
	{
		STEFAN("Stefan Loller", "(404) 368-9878"),
		COREY("Corey Stienecker", "(770) 335-9929"),
		MARK("Mark King", "(901) 218-0583"),
		DON("Don Boyle", "(203) 927-4273"),
		JEFF("Jeff Kurtz", "(619) 339-8184"),
		BRIAN("Brian Vilven", "(858) 518-4072"),
		NONE("NO MANAGER", "N/A");
		
		
		private final String name;
		private final String number;
		
		private TerritoryManagers(String name, String number)
		{
			this.name = name;
			this.number = number;
		}
		
		private static String[] stefanStates = new String[] {"AL", "FL", "GA", "IL", "KY", "MI", "ND", "PA", "SC", "WI"};
		private static String[] brianStates = new String[] {"AK", "CA", "NV", "OR", "WA"};
		private static String[] jeffStates = new String[] {"AZ", "CO", "ID", "MT", "NM", "UT", "WY"};
		private static String[] markStates = new String[] {"AR", "LA", "MS", "MO", "OK", "TX"};
		private static String[] donStates = new String[] {"CT", "ME", "MA", "NH", "NJ", "NY", "RI", "VT"};
		private static String[] coreyStates = new String[] {"DE", "IA", "MD", "NE", "NC", "SD", "TN", "VA", "WV"};
		private static String[] noneStates = new String[] {"HI", "IN", "KS", "MN", "OH"};
		
		
		
		public static TerritoryManagers getTerritoryManager(String stateCode)
		{
			for (String state : stefanStates)
				if (state.equals(stateCode))
					return STEFAN;
			
			for (String state : brianStates)
				if (state.equals(stateCode))
					return BRIAN;
			
			for (String state : jeffStates)
				if (state.equals(stateCode))
					return JEFF;
			
			for (String state : markStates)
				if (state.equals(stateCode))
					return MARK;
			
			for (String state : donStates)
				if (state.equals(stateCode))
					return DON;
			
			for (String state : coreyStates)
				if (state.equals(stateCode))
					return COREY;
			
			for (String state : noneStates)
				if (state.equals(stateCode))
					return NONE;
			
			return null;
		}
		
		public String getName()
		{
			return this.name;
		}
		
		public String getNumber()
		{
			return this.number;
		}
	}
	
	
	
	public String getTerritoryMgrName() {
		return this.terr_mgr.getName();
	}
	
	public String getTerritoryMgrNumber() {
		return this.terr_mgr.getNumber();
	}
	
	
	
	
	public String getProducerType() {
		try {
			return this.getFirstBroadcast().getString("producer_type");
		} catch (JSONException | NullFieldException e) {
			e.printStackTrace();
			return "undefined";
		}
	}



	public String getOnAirStatus() {
		try {
			return this.getFirstBroadcast().getString("status");
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullFieldException e) {
			e.printStackTrace();
		}
		return "Undefined";
	}
	
	
	public JSONObject getBdcState() {
		return this.bdcstate_json;
	}
	
	public String getHLSStatus() {
		return this.bdcstate_json.getString("BroadcastState");
	}
	
	public JSONArray getEventTags() {
		return this.game_json.getJSONArray("event_tags");
	}
	
	public String getTitle() {
		try {
			return this.getFirstBroadcast().getString("headline") + " | " + this.getFirstBroadcast().getString("subheadline");
		} 
		catch (NullFieldException e)
		{
			return null;
		}
	}
	
	public static class Builder
	{
		private String eventId = null;
		private List<String> childIds = null;
		private JSONObject event_json = null;
		
		private boolean isFocusEvent = false;
		private String focusTitle;
		private LocalDateTime focusDateTime;
		private String focusStatus;
		private String focusType;
		
		
		private Builder()
		{
			childIds = new ArrayList<>();
		}
		
		public static Builder newBuilder()
		{
			return new Builder();
		}
		
		public Builder addBroadcastKey(String key)
		{
			this.childIds.add(key);
			return this;
		}
		
		public Builder addBroadcastKeys(String[] keys)
		{
			for (String s : keys)
			{
				this.childIds.add(s);
			}
			return this;
		}
		
		public Builder setEventId(String id)
		{
			eventId = id;
			return this;
		}
		
		public Builder setEventJson(JSONObject json)
		{
			this.event_json = json;
			return this;
		}
		
		public Builder setFocusMonitoringType(String type)
		{
			this.focusType = type;
			return this;
		}
		
		public Builder setFocusTitle(String title)
		{
			this.isFocusEvent = true;
			
			this.focusTitle = title;
			
			return this;
		}
		
		public Builder setFocusDateTime(LocalDateTime focusDateTime)
		{
			this.isFocusEvent = true;
			
			this.focusDateTime = focusDateTime;
			
			return this;
		}
		
		public Builder setFocusStatus(String status)
		{
			this.isFocusEvent = true;
			
			this.focusStatus = status;
			
			return this;
		}
		
		public NFHSGameObject build() throws GameNotFoundException, InvalidContentTypeException, IOException
		{
			if (eventId == null) 
			{
				if (childIds == null)
				{
					if (event_json != null)
					{
						String key;
						try {
							 key = event_json.getString("key");
						} catch (JSONException e) {
							throw new GameNotFoundException("No information given in building.");
						}
						
						switch (NFHSContentType.identify(key)) {
							case EVENT:
							case GAME:
								eventId = key;
								break;
							case BROADCAST:
							case VOD:
								childIds.add(key);
								return build();
							default:
								throw new InvalidContentTypeException(key, "gam/evt/bdc/vod");
						}
					}
					else
						throw new InvalidContentTypeException("No information given in building.");
				}
				else // if childIds != null
				{
					for (String s : childIds)
					{
						String get;
						try {
							get = UnityToolCommon.IDConversion.fetchEventIDIfNeeded(s);
						} catch (GameNotFoundException e) {
							continue;
						} catch (InvalidContentTypeException e) {
							continue;
						} catch (IOException e) {
							throw new IOException("Issue contacting Unity broadcast server.", e);
						}
						
						eventId = get;
						break;
					}
					throw new GameNotFoundException("Unable to find broadcast.");
				}
			}
			
			assert(eventId != null);
			
			
			if (event_json == null)
			{
				event_json = UnityInterface.fetchEventFromUnity(eventId);
			}
			else 
			{
				String key;
				try {
					 key = event_json.getString("key");
				} catch (JSONException e) {
					throw new GameNotFoundException("Not enough information given in building.");
				}
				
				switch (NFHSContentType.identify(key)) {
					case EVENT:
					case GAME:
						if (!eventId.equals(key)) {
							System.out.println("[DEBUG] {Builder} {build} eventId " + eventId + " does not equal JSON key " + key + ", conforming to json");
							eventId = key;
							childIds.clear();
							childIds.add(NFHSGameObject.getFirstBroadcast(event_json).getString("key"));
						}
						break;
					case BROADCAST:
					case VOD:
					default:
						event_json = UnityInterface.fetchEventFromUnity(eventId);
				}
			}
			
			NFHSGameObject n = new NFHSGameObject(eventId, childIds.toArray(new String[childIds.size()]), event_json, NFHSContentType.identify(eventId));
			
			if (isFocusEvent)
			{
				n.setFocusEvent(true);
				
				if (this.focusDateTime != null)
					n.setFocusDateTime(focusDateTime);
				if (this.focusStatus != null)
					n.setFocusStatus(focusStatus);
				if (this.focusTitle != null)
					n.setFocusTitle(focusTitle);
				if (this.focusType != null)
					n.setFocusEventType(focusType);
			}
			
			return n;
		}
		
		
	}
	
}