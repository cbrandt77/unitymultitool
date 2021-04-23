package com.nfhsnetwork.calebsunitytool.ui;

import java.util.List;

import com.nfhsnetwork.calebsunitytool.common.NFHSGameObject;
import com.nfhsnetwork.calebsunitytool.common.NullNFHSObject;

public class UIController 
{
	private MainWindow mw;
	
	UIController(MainWindow mw)
	{
		this.mw = mw;
		onNoneSelected();
	}
	
	protected void onNoneSelected() 
	{
		// TODO change all fields to blank and/or disable all fields
		updateAllLabels(null);
		updateAllEditableLabels(new NullNFHSObject());
	}

	

	protected void onMultipleSelected(List<NFHSGameObject> l) 
	{
		// TODO display the first game selected, enable editing of only batch-viable fields
		updateAllLabels(l.get(0));
		updateAllEditableLabels(l);
	}

	protected void onOneSelected(NFHSGameObject n) 
	{
		// TODO display all info, change all labels to the info
		
		updateAllLabels(n);
		updateAllEditableLabels(n);
	}
	
	private void updateAllLabels(NFHSGameObject n) 
	{
		if (n instanceof NullNFHSObject || n == null)
		{
			mw.mwf.setGender("");
			mw.mwf.setGameid("");
			mw.mwf.setBdcid("");
			mw.mwf.setProdType("");
			mw.mwf.setEd_level("");
			mw.mwf.setParticipants(null);
			mw.mwf.setEd_sport("");
			mw.mwf.setEd_location("");
			mw.mwf.setEd_redirect("");
			mw.mwf.setEd_type("");
			mw.mwf.setProd_producer("");
			mw.mwf.setProd_mgr_name("");
			mw.mwf.setProd_mgr_phone("");
			mw.mwf.setOnairstatus("");
			mw.mwf.setProd_hlsstatus("");
			mw.mwf.setStarttime(null);
			mw.mwf.setTitle("");
			mw.mwf.setEventTags(null);
		}
		else
		{
			mw.mwf.setGender(n.getGender());
			mw.mwf.setGameid(n.getGameID());
			mw.mwf.setBdcid(n.getBdcIDs()[0]);
			mw.mwf.setProdType(n.getProducerType());
			mw.mwf.setEd_level(n.getCompLevel());
			mw.mwf.setParticipants(n.getParticipants());
			mw.mwf.setEd_sport(n.getSportType());
			mw.mwf.setEd_location(n.getLocation());
			mw.mwf.setEd_redirect(n.getRedirectID());
			mw.mwf.setEd_type(n.getCompType());
			mw.mwf.setProd_producer(n.getProducerName());
			mw.mwf.setProd_mgr_name(n.getTerritoryMgrName());
			mw.mwf.setProd_mgr_phone(n.getTerritoryMgrNumber());
			mw.mwf.setOnairstatus(n.getOnAirStatus());
			mw.mwf.setProd_hlsstatus(n.getHLSStatus());
			mw.mwf.setStarttime(n.getDateTime());
			mw.mwf.setTitle(n.getTitle());
			mw.mwf.setEventTags(n.getEventTags());
		}
		
		
		
		
		//TODO 
		
	}
	
	
	private void updateAllEditableLabels(List<NFHSGameObject> n)
	{
		//TODO keep a record of all fields, and check to see if they're different among the set?
			//then make any fields that are different blank, any fields that are the same are filled in with that info that's the same
		
		// for specifically the event tags, make sure that if that field is edited, it only *adds* to the tags, not overwrites the tags.
			// might be better to handle as a List on the backend for that very reason.
	}
	
	
	private void updateAllEditableLabels(NFHSGameObject n)
	{
		//TODO
	}
	
	
	private void updateProductionHistory(NFHSGameObject n)
	{
		
		
		
		
		
	}
	
	
	
}