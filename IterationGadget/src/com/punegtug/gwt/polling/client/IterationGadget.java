package com.punegtug.gwt.polling.client;

import org.cobogw.gwt.waveapi.gadget.client.NeedsWave;
import org.cobogw.gwt.waveapi.gadget.client.ParticipantUpdateEvent;
import org.cobogw.gwt.waveapi.gadget.client.ParticipantUpdateEventHandler;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEvent;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEventHandler;
import org.cobogw.gwt.waveapi.gadget.client.WaveFeature;

import com.google.gwt.gadgets.client.Gadget;
import com.google.gwt.gadgets.client.UserPreferences;
import com.google.gwt.user.client.ui.RootPanel;

@Gadget.ModulePrefs(title = "Polling Gadget")
public class IterationGadget extends Gadget<UserPreferences> implements NeedsWave , ParticipantUpdateEventHandler, StateUpdateEventHandler{

	// use this variable to access the Wave Gadget API.
	private WaveFeature wave;

	/**
	 * The Gadget API will inject the wave object, which can be used for access.
	 */
	public void initializeFeature(WaveFeature feature) {
		wave = feature;
		
	}

	@Override
	protected void init(UserPreferences preferences) {
	}

	@Override
	public void onUpdate(ParticipantUpdateEvent event) {
		renderIterationPlan();
	}

	@Override
	public void onUpdate(StateUpdateEvent event) {
		renderIterationPlan();
	}
	
	public void renderIterationPlan(){
		
		
	}

}