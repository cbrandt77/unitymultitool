package com.nfhsnetwork.calebsunitytool.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.protobuf.ByteString;
import com.nfhsnetwork.calebsunitytool.exceptions.GameNotFoundException;
import com.nfhsnetwork.calebsunitytool.exceptions.InvalidContentTypeException;
import com.nfhsnetwork.calebsunitytool.types.NFHSGameObject;
import com.nfhsnetwork.calebsunitytool.types.NullNFHSObject;
import com.nfhsnetwork.calebsunitytool.utils.Util;
import com.nfhsnetwork.calebsunitytool.utils.Util.StringUtils;

public final class UnityContainer {
	
	//TODO Optimization: make all GET requests asynchronous
	// -populate the data ad-hoc then cache, rather than GET on init?
	
	private static UnityContainer container;
	
	private static boolean containerMade = false;
	
	public synchronized static UnityContainer makeOrGetInstance() 
	{
		if (containerMade)
			return container;
		
		containerMade = true;
		container = new UnityContainer();
		return container;
	}
	
	public static UnityContainer getInstance() 
	{
        if (containerMade)
            return container;
        return makeOrGetInstance();
	}
	
	public synchronized static UnityContainer forceMakeInstance()
	{
		containerMade = true;
		container = new UnityContainer();
		return container;
	}
	
	// Yoooooo cheating to do an inline try/catch block lessgo
//	public static final JSONObject validEventTags = (new Function<String, JSONObject>() {
//		public JSONObject apply(String t) {
//			try {
//				return JSONUtils.JSONReader.readJSONFromURL(t);
//			} catch (JSONException | IOException e) {
//				return null;
//			}
//		}
//	}.apply("https://cfunity.nfhsnetwork.com/v2/event_tags"));
	
	
	
	
	
	
	private ConcurrentHashMap<String, NFHSGameObject> eventMap;
	
	
	public UnityContainer() 
    {
		eventMap = new ConcurrentHashMap<String, NFHSGameObject>();
	}
	
	public Map<String, NFHSGameObject> getEventMap()
	{
		return eventMap;
	}
	
	
	
	
	public enum ImportTypes
	{
		FOCUS,
		URL,
		OTHER
	}
	
	
	private BlockingQueue<String> getQueue;
	
	public UnityContainer importEventData(String input, ImportTypes importType)
	{
//		if (importType != ImportTypes.FOCUS || importType != ImportTypes.OTHER)
//			throw new RuntimeException("Invalid Import Type");
		
		
		
		String[] inputSplit = input.split("\\r\\n|\\n");
		
		getQueue = new LinkedBlockingQueue<String>();
		
		ExecutorService executor = Executors.newFixedThreadPool(4);
		
		
		
		int totalTasks = inputSplit.length;
		AtomicInteger completedTaskCounter = new AtomicInteger(0);
		
		System.out.println("[DEBUG] Total Tasks: " + totalTasks);
		
		
		for (String s : inputSplit) 
		{
			getQueue.add(s);
			
			Task_BuildNFHSObjects task = new Task_BuildNFHSObjects(completedTaskCounter) {
					@Override
					protected NFHSGameObject factoryMethod(String input)
							throws InvalidContentTypeException, GameNotFoundException 
					{
						if (importType == ImportTypes.FOCUS) {
							NFHSGameObject n = NFHSGameObject.buildFromFocusSheetLine(input);
							System.out.println("[DEBUG] [UC] focus build | game id: " + n.getGameID());
							return n;
						}
						else {
							NFHSGameObject n = NFHSGameObject.buildFromIdOrUrl(input);
							System.out.println("[DEBUG] [UC] url build | game id: " + n.getGameID());
							return n;
						}
					}
			};
			
			executor.execute(task);
			System.out.println("[DEBUG] [UC] Added " + s + " to queue!"); //TODO debug
		}
		
		
		//TODO don't busy-wait
		new Thread(
				() -> {
					while (true)
					{
						if (completedTaskCounter.intValue() >= totalTasks)
						{
							System.out.println("[DEBUG] [UC] All games added to list.");
							System.out.println("[DEBUG] [UC] Size of map: " + eventMap.size());
							actionListeners.stream()
										   .forEach(l -> l.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "parse_complete"))); //TODO
							return;
						}
					}
				}
			).start();
		
		return this;
	}
	
	
	private abstract class Task_BuildNFHSObjects implements Runnable
	{
		private final AtomicInteger counter;
		
		Task_BuildNFHSObjects(AtomicInteger counter)
		{
			this.counter = counter;
		}
		
		@Override
		public void run() 
		{
			try 
			{
				//System.out.println("[DEBUG] [UC] [BNO] Building game");
				
				String input = getQueue.take();
				
				NFHSGameObject n;
				try {
					n = factoryMethod(input);
				} 
				catch (InvalidContentTypeException e) {
					e.printStackTrace();
					n = new NullNFHSObject(input, e);
				} 
				catch (GameNotFoundException e) {
					e.printStackTrace();
					n = new NullNFHSObject(input, e);
				}
				if (n.getGameID() == null)
				{
					System.out.println("[DEBUG] {UC run} game ID is null for line: " + "\n" + input);
					counter.incrementAndGet();
					return;
				}
				
				eventMap.put(n.getGameID(), n);
				counter.incrementAndGet();
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("Queue Take operation interrupted.", e);
			}
			
		}

		protected abstract NFHSGameObject factoryMethod(String input) throws InvalidContentTypeException, GameNotFoundException;
	}
	
        
    public static String authToken;
	
	public static void setAuthToken(String s)
	{
		authToken = s;
    }
	public static String getAuthToken()
	{
		return authToken;
	}
        
	
	private List<ActionListener> actionListeners = new ArrayList<>();

	
	
	
	
	public static class ClubInventory
	{
		
		public static Integer parse(String rawCSV)
		{
			return parseClubCSV(Arrays.asList(rawCSV.split("\\n")));
		}
		
		public static Integer parse(List<String> csv)
		{
			return parseClubCSV(csv);
		}
		
		public static void clear()
		{
			clubInventoryMap = new HashMap<>();
		}
		
		private static Map<ByteString, String[]> clubInventoryMap = null;
		
		
		
		
		public static Set<ByteString> keySet()
		{
			return clubInventoryMap.keySet();
		}
		
		public static Set<Map.Entry<ByteString, String[]>> getAllEntries()
		{
			return clubInventoryMap.entrySet();
		}
		
		public static String[] get(ByteString b)
		{
			return clubInventoryMap.get(b);
		}
		
		public static boolean exists()
		{
			return clubInventoryMap != null;
		}
		
		public static boolean containsKey(Object key)
		{
			return clubInventoryMap.containsKey(key);
		}
		
		
		
		
		private static Integer parseClubCSV(List<String> csv)
		{
			clubInventoryMap = new ConcurrentHashMap<>();
			
			if (!ClubInventory.csv_fetchHeaderIndices(csv.get(0)))
			{
				return UnityToolCommon.FAILED;
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
							systemID = Util.hexStringToByteString(StringUtils.stripQuotes(items[csv_sysid_index]));
						} catch (NumberFormatException e){
							return;
						}
						
						String[] details = new String[] {
								StringUtils.stripQuotes(items[csv_sysname_index]),
								StringUtils.stripQuotes(items[csv_status_index]),
								StringUtils.stripQuotes(items[csv_version_index])
						};
						
						if (UnityToolCommon.isDebugMode) {
							System.out.println("[DEBUG] {parseClubCSV} put " + StringUtils.stripQuotes(items[csv_sysid_index])
									+ " into map.");
							for (String s : details) {
								System.out.println("[DEBUG] {parseClubCSV} \t-" + s);
							}
						}
						
						clubInventoryMap.put(systemID, details);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				
				if (UnityToolCommon.isDebugMode) {
					System.out.println("[DEBUG] {csv_fetchHeaderIndices} sysname: " + csv_sysname_index + " | sysid: " + csv_sysid_index + " | status: " + csv_status_index + " | version: " + csv_version_index);
				}
			}
			
			return UnityToolCommon.SUCCESSFUL;
		}
		
		
		private static boolean csv_fetchHeaderIndices(String headerLine)
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
		
		
		
		
		private static int csv_sysname_index = -1;
		private static int csv_sysid_index = -1;
		private static int csv_status_index = -1;
		private static int csv_version_index = -1;
		
		private static final String CSV_SYSNAME_HEADER = "System Name";
		private static final String CSV_SYSID_HEADER = "System ID";
		private static final String CSV_STATUS_HEADER = "VPU Status";
		private static final String CSV_VERSION_HEADER = "Version";
		
		
		
		
		public static final int SYSNAME = 0;
		public static final int STATUS = 1;
		public static final int VERSION = 2;
	}
	
	
	
	
	
	public UnityContainer addActionListener(ActionListener listener) 
	{
		actionListeners.add(listener);
		return this;
	}
	
	private static String user_email;
	
	public static String getUserEmail() {
		return user_email;
	}

	public static void setUserEmail(String email) {
		user_email = email;
	}
	

}
