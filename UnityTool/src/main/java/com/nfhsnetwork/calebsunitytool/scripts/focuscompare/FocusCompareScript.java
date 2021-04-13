package com.nfhsnetwork.calebsunitytool.scripts.focuscompare;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
	
	private boolean hasClubCSV = false;
	
	private static final int NUM_COLUMNS_FROM_FEL = 7;
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
		
		
		for (String focusLine : focusData_splitOnce)
		{
			
			String[] temp;
			
			//TODO add more strict data validation here.  Need to use some regex.
			
			temp = focusLine.split("\\t", -1);
			
			if (temp.length < NUM_COLUMNS_FROM_FEL)
				continue;
			
			
			LocalDateTime fDT;
			String gID;
			String title;
			String type;
			String bID;
			String status;
			
			fDT = LocalDateTime.parse(temp[0] + " " + temp[1], NFHSGameObject.dtf_focusDT);
			title = temp[2];
			type = temp[3];
			gID = temp[4];
			
			try {
				bID = temp[5];
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
			
			FocusGameObject game = new FocusGameObject();
			game.setBdcID(bID);
			game.setDt(fDT);
			game.setGameID(gID);
			game.setStatus(status);
			game.setEventType(type);
			game.setTitle(title);
			
			gameQueue.add(game);
		}
		
		
		
		return SUCCESSFUL;
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
	
	private String[][] clubCSV_split = null;
	private HashMap<String, Integer> dict_SysID_Line = null;
	
	
	
	
	
	public Integer parseClubCSV(List<String> list)
	{
		hasClubCSV = true;
		
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
	
	
	
	private boolean hasBadPxlStatus(FocusGameObject f)
	{
		String pixellot_key;
		try {
			pixellot_key = f.getGameJson().getString("pixellot_id");
		} catch (JSONException e1) {
			return true;
		}
		
		if (clubCSV_split == null)
			return false;
		
		// no need for an if statement here cause it won't get this far if pixellot_key is null
		try
		{
			int index = getIndexOfSystemID(pixellot_key);
			f.setPxlIndex(index);
			if (index == -1)
			{
				return true;
			}
			
			String pixellotStatus = clubCSV_split[index][CLUBARRAY_INDEX_STATUS];
			
			if (pixellotStatus.toUpperCase().equals("OFFLINE") || pixellotStatus.toUpperCase().equals("RESET")) 
			{
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		
		return false;
	}
	
	private boolean compareDateTime(FocusGameObject f)
	{
		LocalDateTime unityDT = Util.convertDateTimeToEST(f.getGameJson().getString("start_time"));
		
		f.setUnityDT(unityDT);
		
		return unityDT.equals(f.getDt());
		
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
	
	
	
	
	public void setCancelled(boolean b)
	{
		isCancelled = b;
	}
	
	boolean isCancelled = false;
	
	
	private boolean isDeleted(FocusGameObject f)
	{
		Boolean b = f.getGameJson().getBoolean("is_deleted");
		f.setDeleted(b);
		return b;
	}
	
	public String compareFocus()
	{
		List<FocusGameObject> filteredGameList = gameQueue.parallelStream()
													  .filter(el -> el.getStatus().equals("Deleted") || el.getStatus().equals("Cancelled"))
													  .collect(Collectors.toUnmodifiableList());
		
		deletedGames = filteredGameList.parallelStream()
											 .filter(this::isDeleted)
											 .collect(Collectors.toUnmodifiableList());
		

		
		gamesWithErrors = filteredGameList.parallelStream()
											 .filter(e -> !e.isDeleted())
											 .filter(this::hasErrors)
											 .collect(Collectors.toUnmodifiableList());
				

		
		gamesWithAlteredDatesOrTimes = filteredGameList.parallelStream()
						 					 .filter(e -> !e.isDeleted())
											 .filter(this::compareDateTime)
											 .collect(Collectors.toUnmodifiableList());

		if (hasClubCSV) 
		{
			gamesWithBadPixellots = filteredGameList.parallelStream()
											 .filter(e -> !e.isDeleted())
											 .filter(this::hasBadPxlStatus)
											 .collect(Collectors.toUnmodifiableList());
		}

		
		String output = buildOutputText();
		
		printOutputToFile(output);
		
		firePropertyChange(PC_DONE, null, null);
		
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
		
		if (hasClubCSV) {
			if (gamesWithBadPixellots.size() == 0)
			{
				sb.append("All Focus Pixellots are online.");
				//sb.append("Focus Pixellot functionality temporarily disabled.");
			} 
			else 
			{
				List<String> l = new LinkedList<>();
				
				gamesWithBadPixellots.forEach(game -> {
					l.add("\n" + game.getGameID() + " | " + game.getBdcID()
							+ "\n"
							+ "\t-Unit Status: " 
							+ game.getPxlStatus()
							+ "\n"
							+ "\t-Club Name: " 
							+ clubCSV_split[game.getPxlIndex()][CLUBARRAY_INDEX_SYSTEMNAME]
							+ "\n" 
							+ "\t-Producer name: " 
							+ NFHSGameObject.getFirstBroadcast(game.getGameJson()).getString("producer_name"));
				});
				
				Collections.sort(l, new Comparator<String>() {
					@Override public int compare(String str1, String str2)
					{
						String substr1 = str1.substring(str1.indexOf("status:"));
						String substr2 = str2.substring(str2.indexOf("status:"));
						
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
		System.out.println("[DEBUG] Print output");
		
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
	
	public void firePropertyChange(String name, Object oldValue, Object newValue)
	{
		pclisteners.forEach(e -> e.propertyChange(new PropertyChangeEvent(this, name, oldValue, newValue)));
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
//		//TODO change references to array to references to NFHSJSONObject
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
//			// TODO adjust the output mechanism to not just be a text file
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
	
	
	

