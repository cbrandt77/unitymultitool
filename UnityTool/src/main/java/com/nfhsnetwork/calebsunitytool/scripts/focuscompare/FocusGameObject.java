package com.nfhsnetwork.calebsunitytool.scripts.focuscompare;

import java.io.IOException;
import java.time.LocalDateTime;

import org.json.JSONObject;

import com.nfhsnetwork.calebsunitytool.common.UnityToolCommon;
import com.nfhsnetwork.calebsunitytool.exceptions.GameNotFoundException;
import com.nfhsnetwork.calebsunitytool.exceptions.InvalidContentTypeException;

public class FocusGameObject {
	
	private String gameID = null;
	private String bdcID = null;
	private LocalDateTime focus_dt = null;
	private LocalDateTime unity_dt = null;
	private String eventType = null;
	private String status = null;
	private JSONObject gamejson = null;
	private String title = null;
	private String error = null;
	private int pxlindex;
	private String pxlstatus = null;
	private boolean isDeleted = false;
	
	FocusGameObject()
	{
		
	}

	protected String getGameID() {
		return gameID;
	}

	protected void setGameID(String gameID) {
		this.gameID = gameID;
		try {
			gamejson = UnityToolCommon.GetFromUnity.getGameFromUnity(gameID);
		} catch (GameNotFoundException | InvalidContentTypeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String getBdcID() {
		return bdcID;
	}

	protected void setBdcID(String bdcID) {
		this.bdcID = bdcID;
	}

	protected LocalDateTime getDt() {
		return focus_dt;
	}

	protected void setDt(LocalDateTime dt) {
		this.focus_dt = dt;
	}

	protected String getEventType() {
		return eventType;
	}

	protected void setEventType(String eventType) {
		this.eventType = eventType;
	}

	protected String getStatus() {
		return status;
	}

	protected void setStatus(String status) {
		this.status = status;
	}
	
	protected JSONObject getGameJson()
	{
		return this.gamejson;
	}

	protected void setTitle(String title) {
		this.title = title;
	}
	
	protected String getTitle()
	{
		return title;
	}
	
	protected String getError()
	{
		return this.error;
	}
	
	protected void setError(String error)
	{
		this.error = error;
	}
	
	protected void setUnityDT(LocalDateTime ldt)
	{
		this.unity_dt = ldt;
	}
	
	protected LocalDateTime getUnityDT()
	{
		return this.unity_dt;
	}

	protected void setPxlIndex(int index) {
		this.pxlindex = index;
	}
	
	protected int getPxlIndex()
	{
		return this.pxlindex;
	}
	
	protected void setPxlStatus(String status)
	{
		this.pxlstatus = status.toUpperCase();
	}
	
	protected String getPxlStatus()
	{
		return this.pxlstatus;
	}

	protected boolean isDeleted() {
		return isDeleted;
	}

	protected void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
