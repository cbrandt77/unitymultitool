package com.nfhsnetwork.calebsunitytool;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.FileWriter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.json.JSONException;
import org.json.JSONObject;

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
	private Queue<String> deletedGames;
	private Queue<String> gamesWithAlteredDatesOrTimes;
	private Queue<String> gamesWithErrors;
	private Queue<String> gamesWithBadPixellots;
	
	private static final int NUM_COLUMNS_FROM_FEL = 7;
	public static final int NUM_RELEVANT_COLUMNS = 5; // handling more data being used potentially, want to minimize used space.
	
	public static final int SUCCESSFUL = 0;
	public static final int FAILED = 1;
	
	
	public FocusCompareScript()
	{
		deletedGames = new ConcurrentLinkedQueue<>();
		gamesWithAlteredDatesOrTimes = new ConcurrentLinkedQueue<>();
		gamesWithErrors = new ConcurrentLinkedQueue<>();
		gamesWithBadPixellots = new ConcurrentLinkedQueue<>();
	}
	
	
	/*
	 * @param focusData_raw = string containing all data from JTextBox
	 * @return true if enough data is provided, false if not enough data is provided
	 */
	public int setFocusData(String focusData_raw)
	{
		String[] focusData_splitOnce = focusData_raw.split("\\r\\n|\\n|\\r", 1);
		
		if (focusData_splitOnce[0].split("\\t", -1).length < NUM_COLUMNS_FROM_FEL ||
				focusData_raw.charAt(0) == ' ') {
			return FAILED;
		}
		
		UnityContainer.makeNewContainer()
					  .importData(focusData_raw, UnityContainer.ImportTypes.FOCUS);
		
		UnityContainer.getContainer().getEventMap().keySet().forEach(e -> {
			System.out.println("[DEBUG] " + e + ": " + UnityContainer.getContainer().getEventMap().get(e));
		});
		
		System.out.println("[DEBUG] EventMap Size: " + UnityContainer.getContainer().getEventMap().size());
		
		
		
		return SUCCESSFUL;
	}
	
	
	/*
	 * @param currentKey the current broadcast key
	 * @param currentBroadcast the JSONObject for the current broadcast key
	 * @return failure type:
	 * 		0 = success
	 * 		1 = failed to get pixellot key in the first place
	 * 		2 = failed to fetch Pixellot status from Unity/Pixellots for currentKey
	 */
//	private void readPixellotStatus(int currentIndex, JSONObject currentBroadcast)
//	{
//		String pixellot_key;
//		try {
//			pixellot_key = currentBroadcast.getString("pixellot_key");
//		} catch (JSONException e1) {
//			return;
//		}
//		
//		// no need for an if statement here cause it won't get this far if pixellot_key is null
//		String currentKey = focusData_split[currentIndex][BDCID_INDEX];
//		try
//		{
//			JSONObject currentPixellot = JSONReader.readJsonFromUrl("http://unity.nfhsnetwork.com/v2/pixellots/" + pixellot_key);
//			String pixellotStatus = currentPixellot.get("status").toString().toUpperCase();
//			
//			if (pixellotStatus.equals("OFFLINE") || pixellotStatus.equals("RESET"))
//				gamesWithBadPixellots.add(currentKey + " | " + currentBroadcast.get("game_key").toString()
//											+ "\r\n"
//											+ "\t-Unit status: " 
//											+ pixellotStatus 
//											+ "\r\n"
//											+ "\t-LMI name: " 
//											+ currentPixellot.get("logmein_name").toString() 
//											+ "\r\n" 
//											+ "\t-Producer name: " 
//											+ currentPixellot.get("formatted_name"));
//		} catch (Exception e) {
//			gamesWithBadPixellots.add("Failed to fetch Pixellot Status from Unity/Pixellots for broadcast " + currentKey);
//		}
//	}
	
	// edited for use with Club CSV
	private void readPixellotStatus(NFHSGameObject game)
	{
		String pixellot_key;
		try {
			pixellot_key = game.getJSONObject().getString("pixellot_id");
		} catch (JSONException e1) {
			return;
		}
		
		// no need for an if statement here cause it won't get this far if pixellot_key is null
		try
		{
			int index = getIndexOfSystemID(pixellot_key);
			if (index == -1)
			{
				addError(game, "System ID mismatch between Console and Club.");
				return;
			}
			
			String pixellotStatus = clubCSV_split[index][CLUBARRAY_INDEX_STATUS];
			
			if (pixellotStatus.toUpperCase().equals("OFFLINE") || pixellotStatus.toUpperCase().equals("RESET")) 
			{
				gamesWithBadPixellots.add(game.getGameID() + " | " + game.getBdcIDs()[0]
											+ "\r\n"
											+ "\t-Unit status: " 
											+ pixellotStatus 
											+ "\r\n"
											+ "\t-LMI name: " 
											+ clubCSV_split[index][CLUBARRAY_INDEX_SYSTEMNAME]
											+ "\r\n" 
											+ "\t-Producer name: " 
											+ game.getProducerName());
			}
		} catch (Exception e) {
			gamesWithBadPixellots.add("Failed to fetch Pixellot Status from Unity/Pixellots for game " + game.getBdcIDs()[0]);
		}
	}
	
	final int CLUBINPUT_INDEX_SYSTEMNAME = 0;
	final int CLUBINPUT_INDEX_SYSTEMID = 1;
	final int CLUBINPUT_INDEX_STATUS = 2;
	final int CLUBINPUT_INDEX_VERSION = 4;
	
	final int CLUBARRAY_NUMDATAINDICES = 3;
	//final int CLUBARRAY_INDEX_SYSTEMID = 0;
	final int CLUBARRAY_INDEX_SYSTEMNAME = 0;
	final int CLUBARRAY_INDEX_STATUS = 1;
	final int CLUBARRAY_INDEX_VERSION = 2;
	
	private String[][] clubCSV_split;
	private HashMap<String, Integer> dict_SysID_Line;
	
	
	
	public Integer parseClubCSV(List<String> list)
	{
		clubCSV_split = new String[list.size() - 1][CLUBARRAY_NUMDATAINDICES];
		
		dict_SysID_Line = new HashMap<String, Integer>();
		
		// line 0 is just headers so start at line 1
		for (int i = 1, length = list.size(); i < length; i++)
		{
			int currentArrayIndex = i - 1;
			
			if (!parseClubCSVLine(list.get(i), currentArrayIndex))
				continue;
		}
		
		
		return SUCCESSFUL;
	}
	
	private boolean parseClubCSVLine(String input, int currentArrayIndex)
	{
		String[] inputSplit = input.split("\",\"");
		
		if (inputSplit.length < 4)
			return false;
		
		clubCSV_split[currentArrayIndex][CLUBARRAY_INDEX_STATUS] = Util.stripQuotes(inputSplit[CLUBINPUT_INDEX_STATUS]);
		clubCSV_split[currentArrayIndex][CLUBARRAY_INDEX_SYSTEMNAME] = Util.stripQuotes(inputSplit[CLUBINPUT_INDEX_SYSTEMNAME]);
		clubCSV_split[currentArrayIndex][CLUBARRAY_INDEX_VERSION] = Util.stripQuotes(inputSplit[CLUBINPUT_INDEX_VERSION]);
		dict_SysID_Line.put(Util.stripQuotes(inputSplit[CLUBINPUT_INDEX_SYSTEMID]), currentArrayIndex);
		
		return true;
	}
	
	
	
	
	private int getIndexOfSystemID(String search) {
	    try {
			return dict_SysID_Line.get(search);
	    } catch (NullPointerException e) {
	    	return -1;
	    }
	}
	
	
	private static final String DTF_OUTPUT_PATTERN = "MM-dd h:mm a";
	private static final DateTimeFormatter dtf_output = DateTimeFormatter.ofPattern(DTF_OUTPUT_PATTERN);
	
	
	/*
	 * @param currentIndex = current index in the focusData_split array from iterator
	 * @param currentBroadcast = JSONObject from the current broadcast
	 */
	private void retrieveAndCompareDateTime(NFHSGameObject n) throws NullFieldException
	{
		//TODO change references to array to references to NFHSJSONObject
		LocalDateTime focusDT = n.getFocusDateTime();
		
		
		String unityTime;
		try {
			unityTime = n.getJSONObject().getString("start_time");
		} 
		catch (JSONException e)
		{
			throw new NullFieldException("start_time", e);
		}
		
		
		LocalDateTime unityDT = Util.convertDateTimeToEST(unityTime);
		
		System.out.println("[DEBUG] DateTime of " + n.getGameID() + ": " + unityDT.toString());
		
		
		//Output
		String x, y;
		
		
		if (!(x = dtf_output.format(unityDT)).equals(y = dtf_output.format(focusDT)))
		{
			gamesWithAlteredDatesOrTimes.add(n.getGameID() + " is " + x + " on Focus Events List, is " + y + " on Console.");
			// TODO adjust the output mechanism to not just be a text file
		}
	}
	
	
	private boolean checkErrors(NFHSGameObject n) throws NullFieldException 
	{
		JSONObject currentBroadcast = n.getFirstBroadcast();
		if (!currentBroadcast.isNull("errors"))
		{
			String error;
			try {
				error = currentBroadcast.get("errors").toString();
			} catch (JSONException e) {
				throw new NullFieldException("errors", e);
			}
			addError(n, error);
			
			if (error.equals("Unlisted (Pending Pixellot Venue ID)"))
				return true;
		}
		
		return false;
	}
	
	
	private void addError(NFHSGameObject n, String error)
	{
		String producerName = n.getProducerName();
		
		gamesWithErrors.add(n.getGameID() + " | " + n.getBdcIDs()[0]
				   + "\r\n"
				   + "\t-Producer Name: "
				   + ((producerName == null) ? "null" : producerName)
				   + "\r\n"
				   + "\t-Error: "
				   + error);
	}
	
	public void setCancelled(boolean b)
	{
		isCancelled = b;
	}
	
	boolean isCancelled = false;
	
	public void compareFocus()
	{
		//AtomicInteger count = new AtomicInteger(0);
		
		
		// For each broadcast key:
		UnityContainer.getContainer().getEventMap().entrySet().stream()
				.forEach((k) -> {
					NFHSGameObject g = k.getValue();
					
					
					if (g.getFocusStatus().equals("Deleted")
							|| g.getFocusStatus().equals("Cancelled"))
					{
						//count.incrementAndGet();
						return;
					}
					
					if (!isNFHS(k.getKey()))
					{
						//count.incrementAndGet();
						System.out.println("[DEBUG] " + k + " is not NFHS.");
						return;
					}
					
					if (g.getIsDeleted())
					{
						this.deletedGames.add(k.getKey());
						System.out.println("[DEBUG] {compareFocus} Deleted game detected - " + k);
						//count.incrementAndGet();
						return;
					}
						
					
					try 
					{
						try {
							retrieveAndCompareDateTime(g);
						} catch (NullFieldException e) {
							e.printStackTrace();
							addError(g, "no start time listed.");
						}
						
						boolean isPending = false;
						try {
							isPending = checkErrors(g);
						} catch (NullFieldException e) {
							e.printStackTrace();
						}
						
						if (!isPending && g.getFirstBroadcast().getString("producer_type").equals("pixellot"))
						{
							readPixellotStatus(g);
						}
					} 
					catch (JSONException e) {
						e.printStackTrace();
					} catch (NullFieldException e) {
						e.printStackTrace();
					}
					
					//count.incrementAndGet();
				});
		
//		Thread countChecker = new Thread(() -> {
//			while(true)
//			{
//				if (count.intValue() == UnityContainer.getContainer().getEventMap().size())
//					break;
//			}
//			printOutput();
//		});
//		countChecker.start();
		
		printOutput();
	}
	
	
	
	private boolean isNFHS(String s)
	{
		return (s.length() >= 4) && (s.substring(0, 3).equals("gam"));
	}
	
	private boolean printOutput()
	{
		System.out.println("[DEBUG] Print output");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
		LocalDateTime now = LocalDateTime.now();
		
		String fileName = getCurrentDirectory() + "/outputs/";
		File file = new File(fileName);
		file.mkdir();
		
		fileName += "Output " + dtf.format(now).toString() + ".txt";
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
			if (deletedGames.size() == 0) {
				writer.write("No deleted broadcasts.");
			} else {
				
				writer.write("Deleted broadcasts:");
				
				while (!deletedGames.isEmpty())
				{
					writer.newLine();
					writer.write("- " + deletedGames.remove());
				}
				
			}
			
			writer.newLine();
			writer.newLine();
			writer.write("------------------------");
			writer.newLine();
			writer.newLine();
			
			
			
			if (gamesWithAlteredDatesOrTimes.size() == 0) 
			{
				writer.write("No games with altered times.");
			} 
			else {
				writer.write("Games with altered times:");
				while (!gamesWithAlteredDatesOrTimes.isEmpty())
				{
					writer.newLine();
					writer.write("- " + gamesWithAlteredDatesOrTimes.remove());
				}
			}
			
			writer.newLine();
			writer.newLine();
			writer.write("------------------------");
			writer.newLine();
			writer.newLine();
			
			if (gamesWithErrors.size() == 0)
			{
				writer.write("No games with Console scheduling errors.");
			} else {
				List<String> s = Arrays.asList(gamesWithErrors.toArray(new String[gamesWithErrors.size()]));
				
				Collections.sort(s, new Comparator<String>() {
					@Override public int compare(String str1, String str2)
					{
						String substr1 = str1.substring(str1.indexOf("Error:"));
						String substr2 = str2.substring(str2.indexOf("Error:"));
						
						return substr1.compareTo(substr2);
					}				
				});
				
				writer.write("Games with Console scheduling errors:");
				
				while (!gamesWithErrors.isEmpty()) {
					writer.newLine();
					writer.write("- " + gamesWithErrors.remove());
				}
			}
			
			

			writer.newLine();
			writer.newLine();
			writer.write("------------------------");
			writer.newLine();
			writer.newLine();
			
			
			if (gamesWithBadPixellots.size() == 0)
			{
				//writer.write("All Focus Pixellots are online.");
				writer.write("Focus Pixellot functionality temporarily disabled.");
			} 
			else 
			{
				List<String> l = Arrays.asList(gamesWithBadPixellots.toArray(new String[gamesWithBadPixellots.size()]));
				
				Collections.sort(l, new Comparator<String>() {
					@Override public int compare(String str1, String str2)
					{
						String substr1 = str1.substring(str1.indexOf("status:"));
						String substr2 = str2.substring(str2.indexOf("status:"));
						
						return substr1.compareTo(substr2);
					}
				});
				
				
				writer.write("Focus Pixellots that are OFFLINE/RESET:");
				
				for (String s : gamesWithBadPixellots)
				{
					writer.newLine();
					writer.write("- " + s);
				}
			}
			
			writer.newLine();
			writer.newLine();
			writer.newLine();
			writer.newLine();
			writer.close();
			
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
	
	private void updatePropertyChangeListeners(String name, int oldValue, int newValue)
	{
		pclisteners.forEach(e -> e.propertyChange(new PropertyChangeEvent(this, name, oldValue, newValue)));
	}
	
}
	
	
	

