package com.nfhsnetwork.calebsunitytool.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import com.nfhsnetwork.calebsunitytool.exceptions.GameNotFoundException;
import com.nfhsnetwork.calebsunitytool.exceptions.InvalidContentTypeException;
import java.io.File;

public final class UnityContainer {
	
	//TODO Optimization: make all GET requests asynchronous
	// -populate the data ad-hoc then cache, rather than GET on init?
	
	//TODO figure out why freaking netbeans hates this so much
	private static UnityContainer container;
        
    //private static File pixellotCSV; //TODO
	
	private static boolean containerMade = false;
	
	public synchronized static UnityContainer makeNewContainer() 
	{
		if (containerMade)
			return container;
		
		containerMade = true;
		container = new UnityContainer();
		return container;
	}
	
	public static UnityContainer getContainer() 
	{
        if (containerMade)
            return container;
        return makeNewContainer();
	}
	
	public synchronized static UnityContainer forceMakeContainer()
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
	
	public UnityContainer importData(String input, ImportTypes importType)
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
					//TODO figure out why I'm getting some fields with null game IDs...
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
