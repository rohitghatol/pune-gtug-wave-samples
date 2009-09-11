package com.punegtug.agile.iteration.client;

import org.cobogw.gwt.waveapi.gadget.client.NeedsWave;
import org.cobogw.gwt.waveapi.gadget.client.ParticipantUpdateEvent;
import org.cobogw.gwt.waveapi.gadget.client.ParticipantUpdateEventHandler;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEvent;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEventHandler;
import org.cobogw.gwt.waveapi.gadget.client.WaveFeature;

import com.google.gwt.gadgets.client.Gadget;
import com.google.gwt.gadgets.client.UserPreferences;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

@com.google.gwt.gadgets.client.Gadget.ModulePrefs(title = "Iteration")
public class Iteration extends Gadget<UserPreferences> implements NeedsWave,
		ParticipantUpdateEventHandler, StateUpdateEventHandler {

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
		
		RootPanel.get().add(new Label("Iteration :"+IterationNo));
	}

}
