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
		log.warning(" inside AdminWavelet");
		this.context = context;
		this.wavelet = context.getWavelet();
		this.metadata = new IterationMetadata(context);

		Blip rootBlip = wavelet.getRootBlip();
		this.adminPane = new AdminPane(rootBlip, metadata);
		log.warning(" ended AdminWavelet");
	}

	public void handleEvents() {
		log.warning(" inside AdminWavelet handleEvents");
		// If the admin form builder has not been initialized, then initialize
		// it.
		if (!isInitialized(wavelet.getRootBlip())) {
			log.warning(" inside AdminWavelet handleEvents create()");
			create();
		}


		// Distribute the poll when needed.
		if (adminPane.isStartIterationButtonPressed()) {
			log.warning(" inside AdminWavelet handleEvents startIteration()");
			startIteration();
		}

		// Save metadata.
		if (metadata.hasChanged()) {
			log.warning(" inside AdminWavelet handleEvents metadata.saveMetaData()");
			metadata.saveMetadata();
		}
		log.warning(" ended AdminWavelet handleEvents");
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
		log.warning("inside startIteration");
		for (String recipient : metadata.getRecipientsAsList()) {
			// Append domain of the poll creator if not specified.
			if (!recipient.contains("@")) {
				String domain = wavelet.getCreator().split("@")[1];
				recipient = recipient + "@" + domain;
			}

			List<String> participants = new ArrayList<String>();
			participants.add(recipient);
			participants.add(WAVEITERATION);

			log.warning("creating iteration wavelet : participants="+participants);
			// Create the Iteration
			IterationWavelet iteration = new IterationWavelet(context, context
					.createWavelet(participants));
			log.warning("created iteration wavelet");
			iteration.create(wavelet, recipient);

		}

	}
}
