package com.nfhsnetwork.calebsunitytool.scripts.focuscompare;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.io.FileWriter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONObject;

import com.google.protobuf.ByteString;
import com.nfhsnetwork.calebsunitytool.common.UnityContainer.ClubInventory;
import com.nfhsnetwork.calebsunitytool.common.UnityToolCommon;
import com.nfhsnetwork.calebsunitytool.exceptions.NullFieldException;
import com.nfhsnetwork.calebsunitytool.types.NFHSGameObject;
import com.nfhsnetwork.calebsunitytool.utils.Util;
import com.nfhsnetwork.calebsunitytool.utils.Util.TimeUtils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Comparator;
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
			return UnityToolCommon.FAILED;
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
		
		return UnityToolCommon.SUCCESSFUL;
	}
	
	
	
	
	@Nullable
	private String[] getSysIDDetails(String search) {
		if (search == null)
			return null;
		
	    try {
			
	    	
	    	//debug info:
	    	if (UnityToolCommon.isDebugMode) {
				String[] deets = ClubInventory.get(Util.hexStringToByteString(search));
				if (deets == null) {
					System.out.println("[DEBUG] {getSysIDDetails} details null for " + search + ". key exists: "
							+ ClubInventory.containsKey(Util.hexStringToByteString(search)));
					return null;
				}
				return deets;
			} 
	    	else 
	    	{
				return ClubInventory.get(Util.hexStringToByteString(search));
			}
	    	
	    	
	    } catch (NullPointerException e) {
	    	if (UnityToolCommon.isDebugMode)
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
		if (!ClubInventory.exists())
			return false;
		
		
		try {
			String[] details = getSysIDDetails(pixellot_key);
			
			if (details == null)
				return true;
			
			
			String pixellotStatus = details[ClubInventory.STATUS];
			
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
		LocalDateTime unityDT = TimeUtils.convertDateTimeToEST(f.getGameJson().getString("start_time"));
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
	
	public String compareFocus() throws IOException
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
		
		if (ClubInventory.exists()) 
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
		
		printOutputToFile(output); //TODO show "failed to print" window
		
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
		
		if (ClubInventory.exists()) {
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
							+ "\n\t-Club Name: " + details[ClubInventory.SYSNAME]
							+ "\n\t-Producer name: " + NFHSGameObject.getFirstBroadcast(game.getGameJson()).getString("producer_name")
							+ "\n\t-Unit Status: " + details[ClubInventory.STATUS]
							+ "\n\t-Version: " + details[ClubInventory.VERSION]);
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
	
	private boolean printOutputToFile(String s) throws IOException
	{
		System.out.println("[DEBUG] {printOutputToFile} Print output");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
		LocalDateTime now = LocalDateTime.now();
		
		String fileName = Util.getCurrentDirectory() + File.separator + "outputs";
		File file = new File(fileName);
		
		fileName = "Output " + dtf.format(now).toString() + ".txt";
		
		return Util.IOUtils.createFolderPrintFile(s, file, fileName);
	}
	
	List<PropertyChangeListener> pclisteners = new LinkedList<>();
	
	public void addPropertyChangeListener(PropertyChangeListener e)
	{
		pclisteners.add(e);
	}
	
	public static final String PC_PROGRESS = "progress_changed";
	public static final String PC_DONE = "task_complete";
	
	
	/**
	 * //TODO progress listener
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
	
	
	

