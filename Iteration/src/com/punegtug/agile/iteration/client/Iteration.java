package com.punegtug.agile.iteration.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cobogw.gwt.waveapi.gadget.client.NeedsWave;
import org.cobogw.gwt.waveapi.gadget.client.ParticipantUpdateEvent;
import org.cobogw.gwt.waveapi.gadget.client.ParticipantUpdateEventHandler;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEvent;
import org.cobogw.gwt.waveapi.gadget.client.StateUpdateEventHandler;
import org.cobogw.gwt.waveapi.gadget.client.WaveFeature;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.gadgets.client.DynamicHeightFeature;
import com.google.gwt.gadgets.client.Gadget;
import com.google.gwt.gadgets.client.NeedsDynamicHeight;
import com.google.gwt.gadgets.client.UserPreferences;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

@com.google.gwt.gadgets.client.Gadget.ModulePrefs(title = "Iteration")
public class Iteration extends Gadget<UserPreferences> implements NeedsWave,
		NeedsDynamicHeight, ParticipantUpdateEventHandler,
		StateUpdateEventHandler, ClickHandler {

	// use this variable to access the Wave Gadget API.
	private WaveFeature wave;
	private DynamicHeightFeature dynHeight;
	private Map<Integer, Map<Integer, TextBox>> iterationProgress = new HashMap<Integer, Map<Integer, TextBox>>();

	/**
	 * The Gadget API will inject the wave object, which can be used for access.
	 */
	public void initializeFeature(WaveFeature feature) {
		wave = feature;

	}

	@Override
	public void initializeFeature(DynamicHeightFeature feature) {
		dynHeight = feature;

	}

	@Override
	protected void init(UserPreferences preferences) {
		if (wave != null) {
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

	public void updateGadget() {
		String IterationNo = wave.getState().get("iterationNo");
		String startDate = wave.getState().get("startDate");
		String duration = wave.getState().get("duration");
		String members = wave.getState().get("members");
		String owner = wave.getState().get("owner");
		
		List<String> membersList = Util.getMemberList(members);

		int iterationDuration = Integer.parseInt(duration);
		RootPanel.get().clear();
		TabPanel tabPanel = new TabPanel();

		RootPanel.get().add(tabPanel);

		VerticalPanel vPanel = new VerticalPanel();

		Label IterationNoLabel = new Label("Iteration No:" + IterationNo);
		Label startDateLabel = new Label("Start Date:" + startDate + "   "
				+ "Duration:" + duration + " days");

		Grid grid = new Grid(membersList.size() + 1, iterationDuration + 3);
		grid.setBorderWidth(2);
		grid.setCellPadding(5);
		grid.setCellSpacing(1);
		grid.setWidget(0, 0, new Label("Features/Days"));
		grid.setWidget(0, 1, new Label("Owner"));
		grid.setWidget(0, 2, new Label("Initial Estimate"));

		int dayCount = 0;
		for (int index = 3; index < grid.getColumnCount(); index++) {
			grid.setWidget(0, index, new Label(String.valueOf(dayCount++)));
		}
		int count = 0;
		for (int index = 1; index < grid.getRowCount(); index++) {
			grid.setWidget(index, 0, new Label("Feature "
					+ String.valueOf(index)));
			grid.setWidget(index, 1, new Label(membersList.get(count++)));
			grid.setWidget(index, 2, new Label(duration));
		}

		int featureCount = 0;
		
		for (int row = 1; row < grid.getRowCount(); row++) {
			dayCount = 0;
			Map<Integer, TextBox> featureProgress = new HashMap<Integer, TextBox>();
			String featureProgressState = wave.getState().get(String.valueOf(featureCount));
			
			JSONArray featureProgressJSONArray = null;
			if(featureProgressState!=null && featureProgressState.trim().length()>0){
				featureProgressJSONArray=(JSONArray)JSONParser.parse(featureProgressState);
			}
			
			for (int col = 3; col < grid.getColumnCount(); col++) {
					
				
				TextBox textBox = new TextBox();
				if(featureProgressJSONArray!=null){
					JSONString value=(JSONString)featureProgressJSONArray.get(dayCount);
					if(value!=null){
						textBox.setText(value.stringValue());
					}
				}

				
				featureProgress.put(dayCount++, textBox);
				grid.setWidget(row, col, textBox);
			}
			iterationProgress.put(featureCount++, featureProgress);
		}

		Button updateButton = new Button("Update");
		updateButton.addClickHandler(this);
		vPanel.add(IterationNoLabel);
		vPanel.add(startDateLabel);

		vPanel.add(grid);
		vPanel.add(updateButton);
		tabPanel.add(vPanel, "Iteration Progress");
		tabPanel.selectTab(0);
		if (wave.getHost().getId().equals(owner)) {
			tabPanel.add(new Label("Settings"), "Settings");
		}

		tabPanel.setHeight("100%");
		tabPanel.setWidth("100%");
		vPanel.setHeight("100%");
		grid.setWidth("100%");
		grid.setHeight("100%");

		dynHeight.adjustHeight();
	}

	@Override
	public void onClick(ClickEvent event) {
		int featureCount = iterationProgress.size();
		HashMap<String,String> waveState = new HashMap<String,String>();
		for (int index = 0; index < featureCount; index++) {
			Map<Integer, TextBox> featureDayMap = iterationProgress.get(index);
			int duration = featureDayMap.size();
			JSONArray featureDayProgress = new JSONArray();
			for (int day = 0; day < duration; day++) {
				TextBox textBox = featureDayMap.get(day);
				String progress = textBox.getText();

				featureDayProgress.set(day, new JSONString(progress));
			}
			waveState.put(String.valueOf(index), featureDayProgress.toString());

		}
		wave.getState().submitDelta(waveState);

	}

}
