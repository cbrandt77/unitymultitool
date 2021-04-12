package com.nfhsnetwork.calebsunitytool.common;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nfhsnetwork.calebsunitytool.exceptions.GameNotFoundException;
import com.nfhsnetwork.calebsunitytool.exceptions.InvalidContentTypeException;
import com.nfhsnetwork.calebsunitytool.exceptions.NullFieldException;
import com.nfhsnetwork.calebsunitytool.utils.JSONUtils;
import com.nfhsnetwork.calebsunitytool.utils.Util;


public class NFHSGameObject
{ 
	
	static final Pattern GAMEIDPATTERN = Pattern.compile("(?:gam){1}[a-f\\d]{10}");
	static final Pattern BDCIDPATTERN = Pattern.compile("(?:bdc){1}[a-f\\d]{10}");
	static final Pattern EVENTIDPATTERN = Pattern.compile("(?:evt){1}[a-f\\d]{10}");
	
	private static final int numDigitsInID = 13;
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
				
				System.out.println("[DEBUG] game id detected: " + event_id);
				
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
				System.out.println("[DEBUG] event id detected: " + event_id);
			}
		}
		
		if ((startIndex = in.indexOf("bdc")) != -1) 
		{
			String temp = in.substring(startIndex, startIndex + numDigitsInID);
			
			if (BDCIDPATTERN.matcher(temp).matches()) 
			{
				bdc_id = temp;
				System.out.println("[DEBUG] bdc id detected: " + bdc_id);
				if (event_id == null) 
				{
					try 
					{
						event_id = UnityToolCommon.Convert.getGameIDFromBroadcast(bdc_id, NFHSContentType.BROADCAST);
					} 
					catch (IOException e) {
						e.printStackTrace();
						throw new GameNotFoundException(bdc_id, e);
					}
				}
			}
		}
			
		
		if (event_id == null && bdc_id == null)
			throw new InvalidContentTypeException(in, "gam/evt/bdc/vod");
		
		
			
		JSONObject jGet = null;
		try {
			jGet = UnityToolCommon.GetFromUnity.getGameFromUnity(event_id);
		} catch (IOException e) {
			throw new GameNotFoundException(event_id, e);
		}
		
		return new NFHSGameObject(
				event_id,
				new String[] {bdc_id},
				jGet,
				n);
	}
	
	
	
	public static NFHSGameObject buildFromFocusSheetLine(String focusLine) throws InvalidContentTypeException, GameNotFoundException
	{
		String[] temp;
		LocalDateTime fDT;
		String gID;
		String title;
		String type;
		String[] bID;
		String status;
		
		
		//TODO add more strict data validation here.  Need to use some regex.
		
		temp = focusLine.split("\\t", -1);
		
		fDT = LocalDateTime.parse(temp[0] + " " + temp[1], dtf_focusDT);
		title = temp[2];
		type = temp[3];
		gID = temp[4];
		
		try {
			bID = new String[] { temp[5] };
		} catch (Exception e) {
			bID = null;
			e.printStackTrace();
		}
		
		try {
			status = temp[6];
		} catch (Exception e) {
			status = null;
			e.printStackTrace();
		}
		
		System.out.println("[DEBUG] game id detected: " + gID + "\n[DEBUG] bdc id detected: " + (bID == null ? "null" : bID[0]));
		
		
		NFHSGameObject n;
		try {
			n = new NFHSGameObject(gID);
		} catch (GameNotFoundException | NullFieldException | InvalidContentTypeException | IOException e) {
			e.printStackTrace();
			return null;
		} 
		
		n.setFocusDateTime(fDT);
		n.setFocusTitle(title);
		n.setFocusEventType(type);
		if (status == null)
			n.setFocusStatus("");
		
		return n;
	}
	
	
	
	
	
	
	
	
	private JSONObject game_json;
	private String game_id;
	private String[] bdc_ids;
	private NFHSContentType content_type;
	private TerritoryManagers terr_mgr;
	
	
	//Only initialized if created by focus data import
	private LocalDateTime focus_dateTime;
	private String focus_title;
	private String focus_status;
	private String focus_type;
	
	private JSONObject bdcstate_json;
	
	
	
	//TODO figure out what should be cached and what can be fetched ad-hoc from the JSON
	
	public NFHSGameObject()
	{
		
	}
	
	/*
	 * Should I assume that only game IDs will be passed in?
	 */
	public NFHSGameObject(String gameID) throws GameNotFoundException, InvalidContentTypeException, IOException, NullFieldException
	{
		this(gameID, UnityToolCommon.GetFromUnity.getGameFromUnity(gameID));
	}
	
	public NFHSGameObject(String game_id, String bdc_id) throws GameNotFoundException, InvalidContentTypeException, IOException
	{
		this(game_id,
			 new String[] { bdc_id },
			 UnityToolCommon.GetFromUnity.getGameFromUnity(game_id));
	}
	
	public NFHSGameObject(String game_id, String[] bdc_ids) throws GameNotFoundException, InvalidContentTypeException, IOException
	{
		this(game_id,
			 bdc_ids,
			 UnityToolCommon.GetFromUnity.getGameFromUnity(game_id));
	}
	
	public NFHSGameObject(String game_id, String bdc_id, JSONObject j)
	{
		this(game_id,
			 new String[] { bdc_id },
			 j);
	}
	
	public NFHSGameObject(JSONObject j) throws GameNotFoundException, InvalidContentTypeException, NullFieldException, IOException, JSONException
	{
		this(j.getString(UnityToolCommon.DICTKEY_GAMEKEY), JSONUtils.ManipNFHSJSON.getBroadcastKeys(j), j);
	}
	
	public NFHSGameObject(String game_id, JSONObject j) throws NullFieldException
	{
		this(game_id, JSONUtils.ManipNFHSJSON.getBroadcastKeys(j), j);
	}
	
	public NFHSGameObject(String game_id, String[] bdc_ids, JSONObject j)
	{
		this(game_id, bdc_ids, j, NFHSContentType.identify(game_id));
	}
	
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
			stateJSON = Util.getBdcStateJSON(this.bdc_ids[0]);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			return;
		}
		
		this.bdcstate_json = stateJSON;
	}
	
	
	
	
	
	private static final DateTimeFormatter dtf_focusDT = new DateTimeFormatterBuilder()
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
	
	public void setContentType(NFHSContentType n)
	{
		this.content_type = n;
	}
	
	public void setGameID(String newID)
	{
		this.game_id = newID;
	}
	
	public void setBdcIDs(String[] bdc_ids)
	{
		this.bdc_ids = bdc_ids;
	}
	
	public void setJSONObject(JSONObject j)
	{
		this.game_json = j;
	}
	
	
	
	public LocalDateTime getDateTime()
	{
		return Util.convertDateTimeToEST(this.game_json.getString("start_time"));
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
	
	
}