package com.nfhsnetwork.calebsunitytool.scripts.focuscompare;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.FileWriter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.protobuf.ByteString;
import com.nfhsnetwork.calebsunitytool.common.NFHSGameObject;
import com.nfhsnetwork.calebsunitytool.common.UnityContainer;
import com.nfhsnetwork.calebsunitytool.exceptions.NullFieldException;
import com.nfhsnetwork.calebsunitytool.utils.Util;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

//import java.util.regex.Matcher;
//import java.util.regex.Pattern;


public class FocusCompareScript
{
	private List<FocusGameObject> deletedGames;
	private List<FocusGameObject> gamesWithAlteredDatesOrTimes;
	private List<FocusGameObject> gamesWithErrors;
	private List<FocusGameObject> gamesWithBadPixellots;
	private final Queue<FocusGameObject> gameQueue;
	
	public static final int NUM_COLUMNS_FROM_FEL = 7;
	public static final int NUM_RELEVANT_COLUMNS = 5; // handling more data being used potentially; want to minimize used space.
	
	public static final int SUCCESSFUL = 0;
	public static final int FAILED = 1;
	
	
	public FocusCompareScript()
	{
		gameQueue = new LinkedBlockingQueue<>();
	}
	
	
	/*
	 * @param focusData_raw = string containing all data from JTextBox
	 * @return true if enough data is provided, false if not enough data is provided
	 */
	public int setFocusData(String focusData_raw)
	{
		String[] focusData_splitOnce = focusData_raw.split("\\r\\n|\\n|\\r");
		
		if (focusData_splitOnce[0].split("\\t", -1).length < NUM_COLUMNS_FROM_FEL ||
				focusData_raw.charAt(0) == ' ') {
			return FAILED;
		}
		
		for (int i = 0, length = focusData_splitOnce.length; i < length; i++)
		{
			String focusLine = focusData_splitOnce[i];
			
			String[] temp = focusLine.split("\\t", -1);
			
			if (temp.length < NUM_COLUMNS_FROM_FEL)
			{
				System.out.println("[DEBUG] {setFocusData} bad line: " + focusLine);
				continue;
			}
			
			Matcher m;
			
			String gameID;
			m = NFHSGameObject.GAMEIDPATTERN.matcher(focusLine);
			
			if (m.find()) 
				gameID = m.group();
			else {
				m = NFHSGameObject.EVENTIDPATTERN.matcher(focusLine);
				if (m.find())
					gameID = m.group();
				else {
					gameID = null;
				}
			}	
			
			
			
			String broadcastKey;
			m = NFHSGameObject.BDCIDPATTERN.matcher(focusLine);
			if (m.find())
				broadcastKey = m.group();
			else {
				System.out.println("[DEBUG] {buildFromFocusSheetLine} bdc key not found");
				broadcastKey = null;
			}

			
			String time;
			m = NFHSGameObject.FOCUS_TIME_PATTERN.matcher(focusLine);
			if (m.find())
				time = m.group();
			else {	
				System.out.println("[DEBUG] {buildFromFocusSheetLine} time not found");
				time = null;
			}
			
			String date;
			m = NFHSGameObject.FOCUS_DATE_PATTERN.matcher(focusLine);
			if (m.find()) {
				date = m.group();
			} else {
				date = null;
				System.out.println("[DEBUG] {buildFromFocusSheetLine} date not found");
			}
			
			if (time == null || date == null)
				System.out.println("[DEBUG] {buildFromFocusSheetLine} null date or time for line:\n" + focusLine);
			
			LocalDateTime focusDateTime = LocalDateTime.parse(date + " " + time, NFHSGameObject.dtf_focusDT);
			
			
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
			
			FocusGameObject game = new FocusGameObject();
			game.setBdcID(broadcastKey);
			game.setDt(focusDateTime);
			game.setGameID(gameID);
			game.setStatus(status);
			game.setEventType(type);
			game.setTitle(title);
			
			gameQueue.add(game);
			
			this.firePropertyChangeEvent(PC_PROGRESS, length, i);
			//System.out.println("[DEBUG] {setFocusData} Game added, pc progress = " + i);
		}
		
		System.out.println("[DEBUG] gamequeue size: " + gameQueue.size());
		
		return SUCCESSFUL;
	}
	
	
	
	/**
	 * 
	 */
	private Map<ByteString, String[]> clubInventoryMap = null;
	
	private int csv_sysname_index = -1;
	private int csv_sysid_index = -1;
	private int csv_status_index = -1;
	private int csv_version_index = -1;
	
	private static final String CSV_SYSNAME_HEADER = "System Name";
	private static final String CSV_SYSID_HEADER = "System ID";
	private static final String CSV_STATUS_HEADER = "VPU Status";
	private static final String CSV_VERSION_HEADER = "Version";
	private static final int CSVINDEX_SYSNAME = 0;
	private static final int CSVINDEX_STATUS = 1;
	private static final int CSVINDEX_VERSION = 2;
	
	
	public Integer parseClubCSV(List<String> csv)
	{
		this.clubInventoryMap = new ConcurrentHashMap<>();
		
		if (!csv_fetchHeaderIndices(csv.get(0)))
		{
			return FAILED;
		}
		else
		{
			int largestIndex = Integer.max(Integer.max(csv_sysname_index, csv_sysid_index),
					Integer.max(csv_status_index, csv_version_index));
			
			csv.parallelStream().forEach(line -> {
				try {
					String[] items = line.split("\",\"", largestIndex + 3); // don't need to go through the entire thing
					
					ByteString systemID; 
					try {
						systemID = Util.hexStringToByteString(Util.stripQuotes(items[csv_sysid_index]));
					} catch (NumberFormatException e){
						return;
					}
					
					String[] details = new String[] {
							Util.stripQuotes(items[csv_sysname_index]),
							Util.stripQuotes(items[csv_status_index]),
							Util.stripQuotes(items[csv_version_index])
					};
					
					if (Util.isDebugMode) {
						System.out.println("[DEBUG] {parseClubCSV} put " + Util.stripQuotes(items[csv_sysid_index])
								+ " into map.");
						for (String s : details) {
							System.out.println("[DEBUG] {parseClubCSV} \t-" + s);
						}
					}
					
					this.clubInventoryMap.put(systemID, details);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			
			if (Util.isDebugMode) {
				System.out.println("[DEBUG] {csv_fetchHeaderIndices} sysname: " + csv_sysname_index + " | sysid: " + csv_sysid_index + " | status: " + csv_status_index + " | version: " + csv_version_index);
			}
		}
		
		return SUCCESSFUL;
	}
	
	private boolean csv_fetchHeaderIndices(String headerLine)
	{
		// headers
		String[] headers = headerLine.split(",");
		
		headers:
		for (int i = 0, l = headers.length; i < l; i++) {
			switch (headers[i])
			{
				case CSV_SYSNAME_HEADER:
					csv_sysname_index = i;
					continue headers;
				case CSV_SYSID_HEADER:
					csv_sysid_index = i;
					continue headers;
				case CSV_STATUS_HEADER:
					csv_status_index = i;
					continue headers;
				case CSV_VERSION_HEADER:
					csv_version_index = i;
					continue headers;
			}
		}
		
		
		if (csv_sysname_index == -1
			 || csv_sysid_index == -1
			 || csv_status_index == -1
			 || csv_version_index == -1) 
		{
			return false;
		}
		
		
		
		return true;
	}
	
	@Nullable
	private String[] getSysIDDetails(String search) {
		if (search == null)
			return null;
		
	    try {
			
	    	
	    	//debug info:
	    	if (Util.isDebugMode) {
				String[] deets = clubInventoryMap.get(Util.hexStringToByteString(search));
				if (deets == null) {
					System.out.println("[DEBUG] {getSysIDDetails} details null for " + search + ". key exists: "
							+ clubInventoryMap.containsKey(Util.hexStringToByteString(search)));
					return null;
				}
				return deets;
			} 
	    	else 
	    	{
				return clubInventoryMap.get(Util.hexStringToByteString(search));
			}
	    	
	    	
	    } catch (NullPointerException e) {
	    	if (Util.isDebugMode)
	    		System.out.println("[DEBUG] {getSysIDDetails} NullPointerException for " + search);
	    	
	    	
	    	return null;
	    }
	}
	
	
	
	
	
	
	private boolean hasBadPxlStatus(FocusGameObject f)
	{
		String pixellot_key = f.getPxlId();
		
		if (pixellot_key == null)
			return true;
		
		// Unneeded?
		if (clubInventoryMap == null)
			return false;
		
		
		try {
			String[] details = getSysIDDetails(pixellot_key);
			
			if (details == null)
				return true;
			
			
			String pixellotStatus = details[CSVINDEX_STATUS];
			
			if (pixellotStatus.toUpperCase().equals("OFFLINE") || pixellotStatus.toUpperCase().equals("RESET")) 
			{
				return true;
			}
		} catch (Exception e) {
			return true;
		}
		
		return false;
	}
	
	private static final String DTF_OUTPUT_PATTERN = "MM-dd h:mm a";
	private static final DateTimeFormatter dtf_output = DateTimeFormatter.ofPattern(DTF_OUTPUT_PATTERN);
	
	private boolean compareDateTime(FocusGameObject f)
	{
		LocalDateTime unityDT = Util.convertDateTimeToEST(f.getGameJson().getString("start_time"));
		//System.out.println("[DEBUG] unityDT for " + f.getGameID() + ": " + unityDT.toString() + ", focus: " + f.getDt().toString());
		f.setUnityDT(unityDT);
		//System.out.println("[DEBUG] unityDT = focusDT for " + f.getGameID() + ": " + unityDT.equals(f.getDt()));
		return !unityDT.format(dtf_output).equals(f.getDt().format(dtf_output));
		
	}
	
	private boolean hasErrors(FocusGameObject f)
	{
		JSONObject currentBroadcast = NFHSGameObject.getFirstBroadcast(f.getGameJson());
		
		if (!currentBroadcast.isNull("errors"))
		{
			f.setError(currentBroadcast.getString("errors"));
			return true;
		}
		else 
		{
			f.setError(null); //added to hopefully ensure parallelism
			return false;
		}
		
	}
	
	
	private boolean isDeleted(FocusGameObject f)
	{
		return f.isDeleted();
	}
	
	public String compareFocus()
	{
//		gameQueue.forEach(e -> System.out.println(e.getGameID()));
		
		List<FocusGameObject> filteredGameList = gameQueue.parallelStream()
													  .filter(el -> !el.getStatus().toLowerCase().equals("deleted") && !el.getStatus().toLowerCase().equals("cancelled"))
													  .collect(Collectors.toUnmodifiableList());
		
//		System.out.println("[DEBUG] filteredGameList");
//		filteredGameList.forEach(e -> System.out.println(e.getGameID()));
		
		deletedGames = filteredGameList.parallelStream()
											 .filter(this::isDeleted)
											 .collect(Collectors.toUnmodifiableList());
		
//		System.out.println("[DEBUG] deletedgames:");
//		deletedGames.forEach(e -> {
//			System.out.println(e.getGameID());
//		});

		
		gamesWithErrors = filteredGameList.parallelStream()
											 .filter(e -> !e.isDeleted())
											 .filter(this::hasErrors)
											 .collect(Collectors.toUnmodifiableList());
		
//		System.out.println("[DEBUG] gameswitherrors:");
//		gamesWithErrors.forEach(e -> System.out.println(e.getGameID()));

		
		gamesWithAlteredDatesOrTimes = filteredGameList.parallelStream()
						 					 .filter(e -> !e.isDeleted())
											 .filter(this::compareDateTime)
											 .collect(Collectors.toUnmodifiableList());

//		System.out.println("[DEBUG] gamesWithAlteredDatesOrTimes:");
//		gamesWithAlteredDatesOrTimes.forEach(e -> System.out.println(e.getGameID()));
		
		if (clubInventoryMap != null) 
		{
			gamesWithBadPixellots = filteredGameList.parallelStream()
											 .filter(e -> !e.isDeleted())
											 .filter(e -> {
												try {
													return e.isPixellot();
												} catch (NullFieldException e1) {
													return false;
												}
											 })
											 .filter(this::hasBadPxlStatus)
											 .collect(Collectors.toUnmodifiableList());
		}

		
		String output = buildOutputText();
		
		printOutputToFile(output);
		
		firePropertyChangeEvent(PC_DONE, null, null);
		
		return output;
	}
	
	private String buildOutputText()
	{
		StringBuilder sb = new StringBuilder();
		
		if (deletedGames.size() == 0) {
			sb.append("No deleted events.");
		}
		else 
		{
			sb.append("Deleted events:");
			
			for (FocusGameObject f : deletedGames)
			{
				sb.append("\n-" + f.getGameID());
			}
			
		}
		
		sb.append("\n");
		sb.append("\n");
		sb.append("------------------------");
		sb.append("\n");
		sb.append("\n");
		
		
		
		if (gamesWithAlteredDatesOrTimes.size() == 0) 
		{
			sb.append("No games with altered dates or times.");
		} 
		else {
			sb.append("Games with altered dates and/or times:");
			
			for (FocusGameObject f : gamesWithAlteredDatesOrTimes) 
			{
				sb.append("\n- " + f.getGameID());
				sb.append("\n\t-Focus Start Time: " + f.getDt().format(dtf_output));
				sb.append("\n\t-Console Start Time: " + f.getUnityDT().format(dtf_output));
			}
		}
		
		sb.append("\n");
		sb.append("\n");
		sb.append("------------------------");
		sb.append("\n");
		sb.append("\n");
		
		if (gamesWithErrors.size() == 0)
		{
			sb.append("No games with Console scheduling errors.");
		} else {
			List<String> errorsList = new LinkedList<>();
			gamesWithErrors.forEach(f -> {
				errorsList.add("\n-" + f.getGameID() + " | " + f.getBdcID()
									 + "\n\t-Producer Name: " + NFHSGameObject.getFirstBroadcast(f.getGameJson()).getString("producer_name")
									 + "\n\t-Error: " + f.getError());
			});
			
			Collections.sort(errorsList, new Comparator<String>() {
				@Override public int compare(String str1, String str2)
				{
					String substr1 = str1.substring(str1.indexOf("Error:"));
					String substr2 = str2.substring(str2.indexOf("Error:"));
					
					return substr1.compareTo(substr2);
				}
			});
			
			sb.append("Games with Console scheduling errors:");
			
			for (String s : errorsList)
			{
				sb.append(s);
			}
		}
		
		

		sb.append("\n");
		sb.append("\n");
		sb.append("------------------------");
		sb.append("\n");
		sb.append("\n");
		
		if (clubInventoryMap != null) {
			if (gamesWithBadPixellots.size() == 0)
			{
				sb.append("All Focus Pixellots are online.");
				//sb.append("Focus Pixellot functionality temporarily disabled.");
			} 
			else 
			{
				List<String> l = new LinkedList<>();
				
				gamesWithBadPixellots.forEach(game -> {
					String[] details = getSysIDDetails(game.getPxlId());
					if (details == null)
					{
						System.out.println("[DEBUG] {buildOutputText} details is null for " + game.getPxlId() + " | game id: " + game.getGameID());
						return;
					}
					
					
					l.add("\n" + game.getGameID() + " | " + game.getBdcID()
							+ "\n\t-Club Name: " + details[CSVINDEX_SYSNAME]
							+ "\n\t-Producer name: " + NFHSGameObject.getFirstBroadcast(game.getGameJson()).getString("producer_name")
							+ "\n\t-Unit Status: " + details[CSVINDEX_STATUS]
							+ "\n\t-Version: " + details[CSVINDEX_VERSION]);
				});
				
				Collections.sort(l, new Comparator<String>() {
					@Override public int compare(String str1, String str2)
					{
						
						String substr1 = str1.substring(str1.indexOf("Status: "), str1.indexOf("Version: "));
						String substr2 = str2.substring(str2.indexOf("Status: "), str2.indexOf("Version: "));
						
						return substr1.compareTo(substr2);
					}
				});
				
				sb.append("Focus Pixellots that are OFFLINE/RESET:");
				
				for (String s : l)
				{
					sb.append(s);
				}
			}
		}
		
		
		return sb.toString();
	}
	
//	private boolean isNFHS(String s)
//	{
//		return (s.length() >= 4) && (s.substring(0, 3).equals("gam"));
//	}
	
	private boolean printOutputToFile(String s)
	{
		System.out.println("[DEBUG] {printOutputToFile} Print output");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
		LocalDateTime now = LocalDateTime.now();
		
		String fileName = getCurrentDirectory() + "/outputs/";
		File file = new File(fileName);
		
		if (!file.exists())
		{
			file.mkdir();
		}
		
		fileName += "Output " + dtf.format(now).toString() + ".txt";
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)))
		{
			for (int i = 0, l = s.length(); i < l; i++)
			{
				char c;
				if ((c = s.charAt(i)) == '\n')
					writer.newLine();
				else
					writer.write(c);
			}
			
			return true;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private String getCurrentDirectory()
	{
		return new File("").getAbsolutePath();
	}
	
	List<PropertyChangeListener> pclisteners = new LinkedList<>();
	
	public void addPropertyChangeListener(PropertyChangeListener e)
	{
		pclisteners.add(e);
	}
	
	public static final String PC_PROGRESS = "progress_changed";
	public static final String PC_DONE = "task_complete";
	
	
	/**
	 * //TODO
	 * @param name
	 * @param total
	 * @param newValue
	 */
	public void firePropertyChangeEvent(String name, Object total, Object newValue)
	{
		pclisteners.forEach(e -> e.propertyChange(new PropertyChangeEvent(this, name, total, newValue)));
	}
	
	
	
	
	
	
//	@Deprecated
//	private boolean checkErrors(NFHSGameObject n) throws NullFieldException 
//	{
//		JSONObject currentBroadcast = n.getFirstBroadcast();
//		if (!currentBroadcast.isNull("errors"))
//		{
//			String error;
//			try {
//				error = currentBroadcast.get("errors").toString();
//			} catch (JSONException e) {
//				throw new NullFieldException("errors", e);
//			}
//			addError(n, error);
//			
//			if (error.equals("Unlisted (Pending Pixellot Venue ID)"))
//				return true;
//		}
//		
//		return false;
//	}
//	
//	@Deprecated
//	private void addError(NFHSGameObject n, String error)
//	{
//		String producerName = n.getProducerName();
//		
//		gamesWithErrors.add(n.getGameID() + " | " + n.getBdcIDs()[0]
//				   + "\r\n"
//				   + "\t-Producer Name: "
//				   + ((producerName == null) ? "null" : producerName)
//				   + "\r\n"
//				   + "\t-Error: "
//				   + error);
//	}
//	
//	/*
//	 * @param currentIndex = current index in the focusData_split array from iterator
//	 * @param currentBroadcast = JSONObject from the current broadcast
//	 */
//	@Deprecated
//	private void retrieveAndCompareDateTime(NFHSGameObject n) throws NullFieldException
//	{
//		LocalDateTime focusDT = n.getFocusDateTime();
//		
//		
//		String unityTime;
//		try {
//			unityTime = n.getJSONObject().getString("start_time");
//		} 
//		catch (JSONException e)
//		{
//			throw new NullFieldException("start_time", e);
//		}
//		
//		
//		LocalDateTime unityDT = Util.convertDateTimeToEST(unityTime);
//		
//		System.out.println("[DEBUG] DateTime of " + n.getGameID() + ": " + unityDT.toString());
//		
//		
//		//Output
//		String x, y;
//		
//		
//		if (!(x = dtf_output.format(unityDT)).equals(y = dtf_output.format(focusDT)))
//		{
//			gamesWithAlteredDatesOrTimes.add(n.getGameID() + " is " + x + " on Focus Events List, is " + y + " on Console.");
//		}
//	}
//	
//	
//	// edited for use with Club CSV
//	@Deprecated
//	private void readPixellotStatus(NFHSGameObject game)
//	{
//		String pixellot_key;
//		try {
//			pixellot_key = game.getJSONObject().getString("pixellot_id");
//		} catch (JSONException e1) {
//			return;
//		}
//		
//		// no need for an if statement here cause it won't get this far if pixellot_key is null
//		try
//		{
//			int index = getIndexOfSystemID(pixellot_key);
//			if (index == -1)
//			{
//				addError(game, "System ID mismatch between Console and Club.");
//				return;
//			}
//			
//			String pixellotStatus = clubCSV_split[index][CLUBARRAY_INDEX_STATUS];
//			
//			if (pixellotStatus.toUpperCase().equals("OFFLINE") || pixellotStatus.toUpperCase().equals("RESET")) 
//			{
//				gamesWithBadPixellots.add(game.getGameID() + " | " + game.getBdcIDs()[0]
//											+ "\r\n"
//											+ "\t-Unit status: " 
//											+ pixellotStatus 
//											+ "\r\n"
//											+ "\t-LMI name: " 
//											+ clubCSV_split[index][CLUBARRAY_INDEX_SYSTEMNAME]
//											+ "\r\n" 
//											+ "\t-Producer name: " 
//											+ game.getProducerName());
//			}
//		} catch (Exception e) {
//			gamesWithBadPixellots.add("Failed to fetch Pixellot Status from Unity/Pixellots for game " + game.getBdcIDs()[0]);
//		}
//	}
//	
//	@Deprecated
//	private void oldCompareFocus()
//	{
//		// For each broadcast key:
//		UnityContainer.getContainer().getEventMap().entrySet().stream()
//				.forEach((k) -> {
//					NFHSGameObject g = k.getValue();
//					
//					
//					if (g.getFocusStatus().equals("Deleted")
//							|| g.getFocusStatus().equals("Cancelled"))
//					{
//						//count.incrementAndGet();
//						return;
//					}
//					
//					if (!isNFHS(k.getKey()))
//					{
//						//count.incrementAndGet();
//						System.out.println("[DEBUG] " + k + " is not NFHS.");
//						return;
//					}
//					
//					if (g.getIsDeleted())
//					{
//						this.deletedGames.add(k.getKey());
//						System.out.println("[DEBUG] {compareFocus} Deleted game detected - " + k);
//						//count.incrementAndGet();
//						return;
//					}
//						
//					
//					try 
//					{
//						try {
//							retrieveAndCompareDateTime(g);
//						} catch (NullFieldException e) {
//							e.printStackTrace();
//							addError(g, "no start time listed.");
//						}
//						
//						boolean isPending = false;
//						try {
//							isPending = checkErrors(g);
//						} catch (NullFieldException e) {
//							e.printStackTrace();
//						}
//						
//						if (!isPending && g.getFirstBroadcast().getString("producer_type").equals("pixellot"))
//						{
//							readPixellotStatus(g);
//						}
//					} 
//					catch (JSONException e) {
//						e.printStackTrace();
//					} catch (NullFieldException e) {
//						e.printStackTrace();
//					}
//					
//					//count.incrementAndGet();
//				});
//	}
//	
//	@Deprecated
//	private int setFocusData_old(String focusData_raw)
//	{
//		String[] focusData_splitOnce = focusData_raw.split("\\r\\n|\\n|\\r");
//		
//		if (focusData_splitOnce[0].split("\\t", -1).length < NUM_COLUMNS_FROM_FEL ||
//				focusData_raw.charAt(0) == ' ') {
//			return FAILED;
//		}
//		
//		UnityContainer.makeNewContainer()
//					  .importData(focusData_raw, UnityContainer.ImportTypes.FOCUS);
//		
//		UnityContainer.getContainer().getEventMap().keySet().forEach(e -> {
//			System.out.println("[DEBUG] " + e + ": " + UnityContainer.getContainer().getEventMap().get(e));
//		});
//		
//		System.out.println("[DEBUG] EventMap Size: " + UnityContainer.getContainer().getEventMap().size());
//		
//		return SUCCESSFUL;
//	}
}
	
	
	

