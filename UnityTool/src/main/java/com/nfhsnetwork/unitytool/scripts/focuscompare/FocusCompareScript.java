package com.nfhsnetwork.unitytool.scripts.focuscompare;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.json.JSONException;
import org.json.JSONObject;

import com.nfhsnetwork.unitytool.common.Config;
import com.nfhsnetwork.unitytool.common.StdPropertyChangeEvent;
import com.nfhsnetwork.unitytool.common.StdPropertyChangeEvent.PropertyType;
import com.nfhsnetwork.unitytool.common.StdPropertyChangeListener;
import com.nfhsnetwork.unitytool.common.UnityToolCommon;
import com.nfhsnetwork.unitytool.common.UnityContainer.ClubInventory;
import com.nfhsnetwork.unitytool.exceptions.NullFieldException;
import com.nfhsnetwork.unitytool.logging.Debug;
import com.nfhsnetwork.unitytool.types.NFHSGameObject;
import com.nfhsnetwork.unitytool.ui.ImportDataFrame;
import com.nfhsnetwork.unitytool.utils.IOUtils;
import com.nfhsnetwork.unitytool.utils.Util;
import com.nfhsnetwork.unitytool.utils.Util.TimeUtils;

import java.time.format.DateTimeFormatter;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;

//import java.util.regex.Matcher;
//import java.util.regex.Pattern;


public class FocusCompareScript
{
	private Stream<FocusGameObject> deletedGames;
	private Stream<FocusGameObject> gamesWithAlteredDatesOrTimes;
	private Stream<FocusGameObject> gamesWithErrors;
	private Stream<FocusGameObject> gamesWithBadPixellots;
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
				Debug.out("[DEBUG] {setFocusData} bad line: " + focusLine);
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
				Debug.out("[DEBUG] {buildFromFocusSheetLine} bdc key not found");
				broadcastKey = null;
			}

			
			String time;
			m = NFHSGameObject.FOCUS_TIME_PATTERN.matcher(focusLine);
			if (m.find())
				time = m.group();
			else {	
				Debug.out("[DEBUG] {buildFromFocusSheetLine} time not found");
				time = null;
			}
			
			String date;
			m = NFHSGameObject.FOCUS_DATE_PATTERN.matcher(focusLine);
			if (m.find()) {
				date = m.group();
			} else {
				date = null;
				Debug.out("[DEBUG] {buildFromFocusSheetLine} date not found");
			}
			
			if (time == null || date == null)
				Debug.out("[DEBUG] {buildFromFocusSheetLine} null date or time for line:\n" + focusLine);
			
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
			
			this.firePropertyChangeEvent(PropertyType.PROGRESS, length, i);
			//Debug.out("[DEBUG] {setFocusData} Game added, pc progress = " + i);
		}
		
		Debug.out("[DEBUG] gamequeue size: " + gameQueue.size());
		
		return UnityToolCommon.SUCCESSFUL;
	}
	
	
	private String[] getSysIDDetails(String search) {
		if (search == null)
			return null;
		
	    try {
	    	String[] details = ClubInventory.get(Util.hexStringToByteString(search));
	    	
	    	Debug.checkNull(details, "[DEBUG] {getSysIDDetails} details null for " + search + ". key exists: " + ClubInventory.containsKey(Util.hexStringToByteString(search)));
	    	
			return details;
	    } 
	    catch (NullPointerException e) {
	    	Debug.out("[DEBUG] {getSysIDDetails} NullPointerException for " + search);
	    	
	    	return null;
	    }
	}
	
	
	
	
	
	
	private boolean hasBadPxlStatus(FocusGameObject f)
	{
		final String pixellot_key = f.getPxlId();
		
		if (pixellot_key == null)
			return true;
		
		// Unneeded?
		if (!ClubInventory.exists())
			return false;
		
		
		try {
			final String[] details = getSysIDDetails(pixellot_key);
			
			if (details == null)
				return true;
			
			
			final String pixellotStatus = details[ClubInventory.STATUS];
			
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
		final LocalDateTime unityDT = TimeUtils.convertDateTimeToEST(f.getGameJson().getString("start_time"));
		//Debug.out("[DEBUG] unityDT for " + f.getGameID() + ": " + unityDT.toString() + ", focus: " + f.getDt().toString());
		f.setUnityDT(unityDT);
		//Debug.out("[DEBUG] unityDT = focusDT for " + f.getGameID() + ": " + unityDT.equals(f.getDt()));
		return !unityDT.format(dtf_output).equals(f.getDt().format(dtf_output));
		
	}
	
	private boolean hasErrors(FocusGameObject f)
	{
		final JSONObject currentBroadcast = NFHSGameObject.getFirstBroadcast(f.getGameJson());
		
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
	
	
	
	public String compareFocus() throws IOException
	{
//		gameQueue.forEach(e -> System.out.println(e.getGameID()));
		
		final List<FocusGameObject> filteredGameList = gameQueue.parallelStream()
													  .filter(el -> !el.getStatus().toLowerCase().equals("deleted") && !el.getStatus().toLowerCase().equals("cancelled"))
													  .collect(Collectors.toUnmodifiableList());
		
//		Debug.out("[DEBUG] filteredGameList");
//		filteredGameList.forEach(e -> System.out.println(e.getGameID()));
		
		deletedGames = filteredGameList.parallelStream()
											 .filter(FocusGameObject::isDeleted);
//											 .collect(Collectors.toUnmodifiableList());
		
//		Debug.out("[DEBUG] deletedgames:");
//		deletedGames.forEach(e -> {
//			System.out.println(e.getGameID());
//		});

		
		gamesWithErrors = filteredGameList.parallelStream()
											 .filter(e -> !e.isDeleted())
											 .filter(this::hasErrors);
//											 .collect(Collectors.toUnmodifiableList());
		
//		Debug.out("[DEBUG] gameswitherrors:");
//		gamesWithErrors.forEach(e -> System.out.println(e.getGameID()));

		
		gamesWithAlteredDatesOrTimes = filteredGameList.parallelStream()
						 					 .filter(e -> !e.isDeleted())
											 .filter(this::compareDateTime);
//											 .collect(Collectors.toUnmodifiableList());

//		Debug.out("[DEBUG] gamesWithAlteredDatesOrTimes:");
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
											 .filter(this::hasBadPxlStatus);
											 //.collect(Collectors.toUnmodifiableList());
		}

		
		final String output = buildOutputText();
		
		try {
			printOutputToFile(output); //TODO show "failed to print" window
		} catch (Exception e) {
			Debug.out("[DEBUG] {compareFocus} failed to print to file");
		}
		
		firePropertyChangeEvent(PropertyType.DONE, null, null);
		
		return output;
	}
	
	private String buildOutputText()
	{
		final StringBuilder sb = new StringBuilder();
		
		
		sb.append("Deleted events:");
		
		deletedGames.map(f -> "\n-" + f.getGameID())
					.sequential()
					.forEach(sb::append);
		
//		for (FocusGameObject f : deletedGames)
//		{
//			sb.append("\n-" + f.getGameID());
//		}
			
		
		
		sb.append("\n");
		sb.append("\n");
		sb.append("------------------------");
		sb.append("\n");
		sb.append("\n");
		
		
		
		
		sb.append("Games with altered dates and/or times:");
		
		this.gamesWithAlteredDatesOrTimes.map(f -> {
			return "\n- " + f.getGameID()
				 + "\n\t-Focus Start Time: " + f.getDt().format(dtf_output)
				 + "\n\t-Console Start Time: " + f.getUnityDT().format(dtf_output);
		})
		.sequential()
		.forEach(sb::append);
		
		
		
		
		sb.append("\n");
		sb.append("\n");
		sb.append("------------------------");
		sb.append("\n");
		sb.append("\n");
		
		
		
		sb.append("Games with Console scheduling errors:");

			
		gamesWithErrors.sorted((f1, f2) -> f1.getError().compareTo(f2.getError()))
					   .map(f -> {
						   return "\n-" + f.getGameID() + " | " + f.getBdcID()
							 + "\n\t-Producer Name: " + NFHSGameObject.getFirstBroadcast(f.getGameJson()).getString("producer_name")
							 + "\n\t-Error: " + f.getError();
					   })
					   .sequential()
					   .forEach(sb::append);
		
//		final List<String> errorsList = new LinkedList<>();
//			gamesWithErrors.forEach(f -> {
//				errorsList.add("\n-" + f.getGameID() + " | " + f.getBdcID()
//									 + "\n\t-Producer Name: " + NFHSGameObject.getFirstBroadcast(f.getGameJson()).getString("producer_name")
//									 + "\n\t-Error: " + f.getError());
//			});
//			
//			Collections.sort(errorsList, new Comparator<String>() {
//				@Override public int compare(String str1, String str2)
//				{
//					String substr1 = str1.substring(str1.indexOf("Error:"));
//					String substr2 = str2.substring(str2.indexOf("Error:"));
//					
//					return substr1.compareTo(substr2);
//				}
//			});
			
//			sb.append("Games with Console scheduling errors:");
//			
//			for (final String s : errorsList)
//			{
//				sb.append(s);
//			}
		
		
		

		sb.append("\n");
		sb.append("\n");
		sb.append("------------------------");
		sb.append("\n");
		sb.append("\n");
		
		if (ClubInventory.exists()) {
			
			//final List<String> l = new LinkedList<>();
			
			sb.append("Focus Pixellots that are OFFLINE/RESET:");
			
			gamesWithBadPixellots.map(g -> {
				final String[] details = getSysIDDetails(g.getPxlId());
				
				if (details == null)
					return null;
				
				return new String[] {
						g.getGameID(),
						g.getBdcID(),
						details[ClubInventory.SYSNAME],
						NFHSGameObject.getFirstBroadcast(g.getGameJson()).getString("producer_name"),
						details[ClubInventory.STATUS],
						details[ClubInventory.VERSION]
				};
			})
			.filter(details -> details != null)
			.sorted((d1, d2) -> d1[4].compareTo(d2[4]))
			.map(d -> {
				return "\n" + d[0] + " | " + d[1]
				+ "\n\t-Club Name: " + d[2]
				+ "\n\t-Producer name: " + d[3]
				+ "\n\t-Unit Status: " + d[4]
				+ "\n\t-Version: " + d[5];
			})
			.sequential()
			.forEach(sb::append);;
			
//			gamesWithBadPixellots.forEach(game -> {
//				final String[] details = getSysIDDetails(game.getPxlId());
//				if (details == null)
//				{
//					Debug.out("[DEBUG] {buildOutputText} details is null for " + game.getPxlId() + " | game id: " + game.getGameID());
//					return;
//				}
//				
//				
//				l.add("\n" + game.getGameID() + " | " + game.getBdcID()
//						+ "\n\t-Club Name: " + details[ClubInventory.SYSNAME]
//						+ "\n\t-Producer name: " + NFHSGameObject.getFirstBroadcast(game.getGameJson()).getString("producer_name")
//						+ "\n\t-Unit Status: " + details[ClubInventory.STATUS]
//						+ "\n\t-Version: " + details[ClubInventory.VERSION]);
//			});
//			
//			Collections.sort(l, new Comparator<String>() {
//				@Override public int compare(String str1, String str2)
//				{
//					
//					final String substr1 = str1.substring(str1.indexOf("Status: "), str1.indexOf("Version: "));
//					final String substr2 = str2.substring(str2.indexOf("Status: "), str2.indexOf("Version: "));
//					
//					return substr1.compareTo(substr2);
//				}
//			});
			
			
			
//			for (final String s : l)
//			{
//				sb.append(s);
//			}
			
		}
		
		
		return sb.toString();
	}
	
//	private boolean isNFHS(String s)
//	{
//		return (s.length() >= 4) && (s.substring(0, 3).equals("gam"));
//	}
	
	private boolean printOutputToFile(String s) throws IOException
	{
		Debug.out("[DEBUG] {printOutputToFile} Print output");
		
		final LocalDateTime now = LocalDateTime.now();
		
		String fileName = Util.getCurrentDirectory() + File.separator + "outputs" + File.separator;
		final File file = new File(fileName);
		
		String formattedTime;
		try {
			final DateTimeFormatter dtf = Config.getOutputDTF();
			formattedTime = dtf.format(now).toString();
		} catch (Exception e) {
			e.printStackTrace();
			formattedTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss").format(now);
			
		}
		
		
		String prefix;
		try {
			prefix = Config.getOutputPrefix();
		} catch (JSONException e) {
			prefix = "Output-";
		}
		
		fileName = prefix + formattedTime + ".txt";
		
		try {
			IOUtils.createAndPrintToFile(s, file, fileName);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private final List<StdPropertyChangeListener> pclisteners = new LinkedList<>();
	
	public void addPropertyChangeListener(StdPropertyChangeListener e)
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
	public void firePropertyChangeEvent(PropertyType name, Object total, Object newValue)
	{
		pclisteners.forEach(e -> e.propertyChange(new StdPropertyChangeEvent(this, name, total, newValue)));
	}
}
	
	
	

