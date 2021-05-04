package com.nfhsnetwork.unitytool.scripts.multiviewertag;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nfhsnetwork.unitytool.common.UnityContainer;
import com.nfhsnetwork.unitytool.common.UnityToolCommon;
import com.nfhsnetwork.unitytool.exceptions.GameNotFoundException;
import com.nfhsnetwork.unitytool.exceptions.InvalidContentTypeException;
import com.nfhsnetwork.unitytool.types.NFHSGameObject;
import com.nfhsnetwork.unitytool.ui.components.ProgressBarDialogBox;
import com.nfhsnetwork.unitytool.utils.Debug;

public class MultiviewerTagScript {
	
	static final int SUCCESS = 27;
	static final int FAILURE = 35;
	
	private final String unityGETAddress = "https://unity.nfhsnetwork.com/v2/game_or_event/%s";
	private final String unityPUTAddress = "https://unity.nfhsnetwork.com/v2/game_or_event/%s/update_all";
	
	List<String> idOrUrlList; //TODO turn this into a Set
	
	final JFrame parent;
	
	public MultiviewerTagScript(JFrame parent) 
	{
		this.parent = parent;
		Debug.out("[DEBUG] init object");
	}
	
	

	public Integer inputIDs(String sInput)
	{
		idOrUrlList = Arrays.stream(sInput.split("\n"))
						.map(s -> new String(s))
						.collect(Collectors.toList());
		
		
		return SUCCESS;
	}
	
	
	
	public void setProgressBar(ProgressBarDialogBox linkedProgressBar)
	{
		if (linkedProgressBar != null)
			this.hasProgressBar = true;
		
		this.linkedProgressBar = linkedProgressBar;
	}
	
	
	private ProgressBarDialogBox linkedProgressBar;
	private boolean hasProgressBar = false;
	
	public void startTagOperation()
	{
		Debug.out("[DEBUG] Tag operation started");
		totalTasks = idOrUrlList.size();
		Debug.out("[DEBUG] total tasks: " + totalTasks);
		initAndQueueJSONBuilders();
		initPUTThread();
	}
	
	
	private final int NUMTHREADS = 4;
	void initAndQueueJSONBuilders() //multi-threaded
	{
		Debug.out("[DEBUG] jsonbuilder thread started");
		ExecutorService e = Executors.newFixedThreadPool(NUMTHREADS);
		
		for (String s : idOrUrlList)
		{
			workQueue.add(s);
			e.execute(new BackgroundTask());
		}
		
		Thread thread = new Thread(new BackgroundTask()); //NOTE: need to change how the tasks work for single-threaded and multi-threaded GETs. One uses a while loop while the other uses a single operation.
		thread.start();
	}
	final void initPUTThread() //Single-threaded
	{
		Debug.out("[DEBUG] put thread started");
		Thread putThread = new Thread(new PutThread());
		putThread.start();
	}
	
	private final BlockingQueue<String> workQueue = new LinkedBlockingQueue<>();
	private final BlockingQueue<NFHSGameObject> putQueue = new LinkedBlockingQueue<>();
	
	
//	private static final int NUM_THREADS = 4;
	
	
//	final void initAndQueueJSONBuilders()
//	{
//		ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
//		for (int i = 0, length = idList.size(); i < length; i++)
//		{
//			workQueue.add(idList.get(i));
//			BackgroundTask task = new BackgroundTask();
//			executor.execute(task);
//			System.out.println("Added " + idList.get(i) + " to queue!");
//		}
//	}
	
	private class BackgroundTask implements Runnable
	{
		@Override
		public void run() {
			try {
				Debug.out("[DEBUG] Started background task.");
				String s = workQueue.take();
				
				if (s.length() < 13) {
					Debug.out("[DEBUG] {MTS} length less than 13: " + s);
					return;
				}
					
				
				NFHSGameObject n = NFHSGameObject.buildFromIdOrUrl(s);
				
				putQueue.add(n);
				Debug.out("[DEBUG] Added " + s + " to Put Queue!");
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (GameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidContentTypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	private volatile int totalTasks;
	
	private final class PutThread implements Runnable
	{
		private AtomicInteger taskCompleteCounter = new AtomicInteger(0);
		
		@Override
		public void run() 
		{
			int counter = 0;
			Debug.out("[DEBUG] Put thread initialized");
			
			main_loop:
			while (true)
			{
				if (taskCompleteCounter.get() >= totalTasks)
				{
					Debug.out("[DEBUG] All puts done!");
					firePropertyChangeEvent(UnityToolCommon.PropertyChangeType.DONE);
					return;
				}
				if (!putQueue.isEmpty())
				{
					Debug.out("[DEBUG] Put task started for ");
					try {
						updateProgressBar(counter, totalTasks);
						
						Thread.sleep(1000);
						
						NFHSGameObject j = putQueue.take();
						
						JSONArray ja = j.getEventTags();
						
						for (int i = 0, length = ja.length(); i < length; i++)
						{
							Object o = ja.get(i);
							if (o instanceof String && o.equals("multiviewer")) 
							{
								counter = taskCompleteCounter.incrementAndGet(); //TODO caching an int as a shortcut is not thread-safe
								continue main_loop;
							}
						}
						
						
						ja.put("multiviewer");
						JSONObject out = new JSONObject();
						out.put("event_tags", ja);
						
						sendJSONToUnity(out, j.getGameID());
					} 
					catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
					counter = taskCompleteCounter.incrementAndGet(); // TODO this is stupid why am i doing this
					
					System.out.println(counter);
				}
				
			}
			
				
		}
		
		private void updateProgressBar(int numComplete, int totalTasks)
		{
			if (!hasProgressBar)
				return;
			
			int newValue = numComplete * 100 / totalTasks;
			linkedProgressBar.getProgressBar().setValue(newValue);
		}
	}
	
	
	
	
	

		
		/*
		JSONObject j2 = null;
		try {
			JSONObject broadcast = j1.getJSONArray("publishers").getJSONObject(0).getJSONArray("broadcasts").getJSONObject(0);
			
			j2 = new JSONObject()
					.put("start_time", j1.get("start_time"))
					.put("track_wrestling", j1.get("track_wrestling"))
					.put("headline", broadcast.get("headline"))
					.put("subheadline", broadcast.get("subheadline"))
					.put("publisher_key", broadcast.get("publisher_key"))
					.put("sport", j1.get("sport"))
					.put("gender", j1.get("gender"))
					.put("game_type", j1.get("game_type"))
					.put("level", j1.get("level"))
					.put("sublocation", j1.get("sublocation"))
					.put("sublocation_detail", j1.get("sublocation_detail"))
					.put("home", j1.get("home_team"))
					.put("teams_to_be_determined", j1.get("teams_to_be_determined"))
					.put("multi_court", broadcast.get("multi_court"))
					.put("time_zone", j1.get("time_zone"))
					.put("state_code", j1.get("state_code"))
					.put("city", j1.get("city"))
					.put("school_keys", j1.get("school_keys"))
					.put("school_names", j1.get("school_names"))
					.put("time_to_be_determined", j1.get("time_to_be_determined"))
					.put("location_to_be_determined", j1.get("location_to_be_determined"))
					.put("event_redirect", j1.get("event_redirect")) // could be null... 
					.put("creator", j1.get("creator"))
					//.put("updater", j1.get("updater"))
					.put("updater", "unityTool")
					.put("unlisted", j1.get("unlisted"))
					.put("vod_unlisted", j1.get("vod_unlisted"))
					.put("mute", j1.get("mute"))
					.put("payment_required", j1.get("payment_required"))
					.put("created_by_user", j1.get("created_by_user"))
					//.put("updated_by_user", j1.get("updated_by_user"))
					.put("updated_by_user", "NFHS Unity Tool")
					.put("duration", j1.get("duration"))
					.put("producer_key", j1.get("producer_key"))
					.put("custom_title", j1.get("custom_title"))
					.put("event_tags", j1.getJSONArray("event_tags").put("multiviewer")) // the only altered one
					.put("revenue_share", j1.get("revenue_share"))
					.put("description", j1.get("description"))
					.put("html_details_content", j1.get("html_details_content"))
					.put("hls_startup_buffer_seconds", j1.get("hls_startup_buffer_seconds"));
			
			if (!j1.isNull("live_like_chat_room"))
			{
				JSONObject live_like_original = j1.getJSONObject("live_like_chat_room");
				JSONArray live_like_chatrooms = live_like_original.getJSONArray("chat_rooms");
				
				// TODO convert the live_like_room_ids array-making to a method with a while loop so it'll handle more than 2 ids.
				
				JSONObject live_like = new JSONObject()
						.put("live_like_program_id", live_like_original.get("live_like_program_id"))
						.put("live_like_room_ids", new JSONArray()
															.put(new JSONObject()
																	.put("room_id", live_like_chatrooms.getJSONObject(0).get("live_like_room_id"))
																	.put("school_key", live_like_chatrooms.getJSONObject(0).get("school_key")))
															.put(new JSONObject()
																	.put("room_id", live_like_chatrooms.getJSONObject(1).get("live_like_room_id"))
																	.put("school_key", live_like_chatrooms.getJSONObject(1).get("school_key"))));
				j2.put("live_like", live_like);
			}
			
			return j2;
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		*/
		
		
		
	
	
	
	
	List<String> failedToUpdate = new ArrayList<String>();
	
	private void sendJSONToUnity(JSONObject jsonIn, String id) throws IOException
	{
		String jsonText = jsonIn.toString();
		byte[] out = jsonText.getBytes(StandardCharsets.UTF_8);
		
		URL url = new URL(String.format(unityPUTAddress, id));
		URLConnection con = url.openConnection();
		
		HttpURLConnection http = (HttpURLConnection)con;
		http.setRequestMethod("PUT");
		http.setDoOutput(true);
		http.setDoInput(true);
		
		addRequestHeaders(http);
		http.addRequestProperty("Content-Length", jsonText.length() + "");
		
		http.addRequestProperty("Authorization", UnityContainer.getAuthToken());
		
		http.setFixedLengthStreamingMode(out.length);
		
		//http.connect(); // actually redundant in java apparently
		
		try (OutputStream os = http.getOutputStream())
		{
			os.write(out);
		}
		
		
		//InputStream response = http.getInputStream();
		
		if (http.getResponseCode() != 200)
		{
			failedToUpdate.add(id); //TODO
		}
		
		
		http.disconnect();
		
	}
	
	public List<String> getFailedToUpdate()
	{
		return failedToUpdate; //TODO print failed to update list, etc
	}
	
	
	
	
	private void addRequestHeaders(HttpURLConnection http)
	{
		http.addRequestProperty("Accept", "application/json, text/plain, */*");
		http.addRequestProperty("Accept-Encoding", "gzip, deflate, br");
		http.addRequestProperty("Accept-Language", "en-US,en;q=0.9");
//		http.addRequestProperty("Connection", "keep-alive");
		http.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
//		http.addRequestProperty("Host", "unity.nfhsnetwork.com");
//		http.addRequestProperty("Origin", "http://console.nfhsnetwork.com");
//		http.addRequestProperty("Referer", "http://console.nfhsnetwork.com/");
//		http.addRequestProperty("sec-ch-ua", "\"Chromium\";v=\"88\", \"Google Chrome\";v=\"88\", \";Not A Brand\";v=\"99\"");
//		http.addRequestProperty("sec-ch-ua-mobile", "?0");
//		http.addRequestProperty("Sec-Fetch-Dest", "empty");
//		http.addRequestProperty("Sec-Fetch-Mode", "cors");
//		http.addRequestProperty("Sec-Fetch-Site", "cross-site");
//		http.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36");
	}
	
	
	
	
	
	Set<PropertyChangeListener> listeners = new CopyOnWriteArraySet<>();
	
	public MultiviewerTagScript addPropertyChangeListener(PropertyChangeListener e)
	{
		listeners.add(e);
		
		return this;
	}
	
	public void firePropertyChangeEvent(UnityToolCommon.PropertyChangeType type)
	{
		Debug.out("[DEBUG] {firePropertyChangeEvent} change event fired of type " + type.name() + ": " + type.toString());
		listeners.forEach(e -> e.propertyChange(new PropertyChangeEvent(this, type.toString(), null, null))); //TODO progress bar and CHANGED
	}
	
	public void firePropertyChangeEvent(Object source, UnityToolCommon.PropertyChangeType type, Object oldValue, Object newValue)
	{
		PropertyChangeEvent evt = new PropertyChangeEvent(source, type.toString(), oldValue, newValue);
		listeners.forEach(e -> e.propertyChange(evt));
	}
	
	
	
	
	
}
