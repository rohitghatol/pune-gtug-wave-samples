package com.punegtug.agile.iteration.client;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.cobogw.gwt.waveapi.gadget.client.NeedsWave;
import org.cobogw.gwt.waveapi.gadget.client.ParticipantUpdateEvent;
import org.cobogw.gwt.waveapi.gadget.client.ParticipantUpdateEventHandler;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEvent;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEventHandler;
import org.cobogw.gwt.waveapi.gadget.client.WaveFeature;

import com.google.gwt.gadgets.client.DynamicHeightFeature;
import com.google.gwt.gadgets.client.Gadget;
import com.google.gwt.gadgets.client.NeedsDynamicHeight;
import com.google.gwt.gadgets.client.UserPreferences;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

@com.google.gwt.gadgets.client.Gadget.ModulePrefs(title = "Iteration")
public class Iteration extends Gadget<UserPreferences> implements NeedsWave,NeedsDynamicHeight,
		ParticipantUpdateEventHandler, StateUpdateEventHandler {

	// use this variable to access the Wave Gadget API.
	private WaveFeature wave;
	private DynamicHeightFeature dynHeight;

	/**
	 * The Gadget API will inject the wave object, which can be used for access.
	 */
	public void initializeFeature(WaveFeature feature) {
		wave = feature;
		
	}
	
	@Override
	public void initializeFeature(DynamicHeightFeature feature) {
		dynHeight=feature;
		
	}

	@Override
	protected void init(UserPreferences preferences) {
		if(wave!=null){
			wave.addParticipantUpdateEventHandler(this);
			wave.addStateUpdateEventHandler(this);
		}
		
	}

	@Override
	public void onUpdate(ParticipantUpdateEvent event) {
		updateGadget();

	}

	@Override
	public void onUpdate(StateUpdateEvent event) {
		
		updateGadget();
	}
	
	public void updateGadget(){
		String IterationNo=wave.getState().get("iterationNo");
		String startDate = wave.getState().get("startDate");
		String duration = wave.getState().get("duration");
		String members = wave.getState().get("members");
		String owner = wave.getState().get("owner");		
	
		List<String> membersList = new ArrayList<String>();
		
		//Hard coding to run to the integration part
		membersList.add("rohitsghatol@wavesandbox.com");
		membersList.add("rohitsghatol-test@wavesandbox.com");
		
		int iterationDuration = Integer.parseInt(duration);
		RootPanel.get().clear();
		TabPanel tabPanel=new TabPanel();
		tabPanel.setHeight("600px");
		tabPanel.setWidth("100%");
		RootPanel.get().add(tabPanel);
		
		VerticalPanel vPanel=new VerticalPanel();
		vPanel.setHeight("100%");
		Label IterationNoLabel = new Label("Iteration No:"+IterationNo);
		Label startDateLabel = new Label("Start Date:"+startDate);
		Label durationLabel = new Label("Duration:"+duration+" days");
		Grid grid = new Grid(membersList.size()+1,iterationDuration+1);
		grid.setHeight("100%");
		grid.setWidget(0, 0, new Label("Features/Days"));
		for(int index=1;index<iterationDuration+1;index++){
			grid.setWidget(0, index, new Label(String.valueOf(index)));
		}
		for(int index=1;index<membersList.size()+1;index++){
			grid.setWidget(index, 0, new Label("Feature "+String.valueOf(index)));
		}
		
		vPanel.add(IterationNoLabel);
		vPanel.add(startDateLabel);
		vPanel.add(durationLabel);
		vPanel.add(grid);
		tabPanel.add(vPanel, "Iteration Progress");
		
		if(wave.getHost().getId().equals(owner)){
			
		}
		
		
		dynHeight.adjustHeight();
	}



}

