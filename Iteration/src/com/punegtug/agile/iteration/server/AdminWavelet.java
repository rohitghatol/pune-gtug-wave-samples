package com.punegtug.agile.iteration.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.wave.api.Blip;
import com.google.wave.api.RobotMessageBundle;
import com.google.wave.api.StyleType;
import com.google.wave.api.StyledText;
import com.google.wave.api.Wavelet;

/**
 * 
 * @author rohitsghatol@gmail.com
 */
public class AdminWavelet {
	private static final Logger log = Logger.getLogger(AdminWavelet.class
			.getName());
	static final String WAVEITERATION = "i-mank@appspot.com";

	private RobotMessageBundle context;

	private Wavelet wavelet;

	private IterationMetadata metadata;

	private AdminPane adminPane;

	public static boolean isAdminWavelet(RobotMessageBundle context) {

		return isInitialized(context.getWavelet().getRootBlip());
	}

	private static boolean isInitialized(Blip blip) {
		return blip.getDocument().hasAnnotation("iteration-admin");
	}

	public AdminWavelet(RobotMessageBundle context) {

		this.context = context;
		this.wavelet = context.getWavelet();
		this.metadata = new IterationMetadata(context);

		Blip rootBlip = wavelet.getRootBlip();
		this.adminPane = new AdminPane(rootBlip, metadata);

	}

	public void handleEvents() {

		// If the admin form builder has not been initialized, then initialize
		// it.
		if (!isInitialized(wavelet.getRootBlip())) {
			create();
		}

		// Distribute the poll when needed.
		if (adminPane.isStartIterationButtonPressed()) {
			startIteration();
		}

		// Save metadata.
		if (metadata.hasChanged()) {
			metadata.saveMetadata();
		}

	}

	public void create() {
		setTitle(metadata.getIterationName());
		adminPane.create();

	}

	private void setTitle(String title) {
		wavelet.setTitle(new StyledText(title + " -- Administration",
				StyleType.HEADING2));
	}

	private void startIteration() {


		List<String> participants = new ArrayList<String>();
		participants.addAll(metadata.getRecipientsAsList());
		participants.add(WAVEITERATION);

		// Create the Iteration
		IterationWavelet iteration = new IterationWavelet(context, context
				.createWavelet(participants));
		

		iteration.create(wavelet);

	}
}
