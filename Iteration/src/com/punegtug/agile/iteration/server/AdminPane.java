package com.punegtug.agile.iteration.server;

import java.util.logging.Logger;

import com.google.wave.api.Blip;
import com.google.wave.api.ElementType;
import com.google.wave.api.FormElement;
import com.google.wave.api.FormView;
import com.google.wave.api.TextView;


public class AdminPane {

	private static final Logger log = Logger.getLogger(AdminPane.class
			.getName());

	private static final String RECIPIENTS_LABEL = "recipients_label";
	private static final String RECIPIENTS_INPUT = "recipients_input";

	private static final String STARTDATE_INPUT = "startdate_input";
	private static final String STARTDATE_LABEL = "startdate_label";

	private static final String DURATION_INPUT = "duration_input";
	private static final String DURATION_LABEL = "duration_label";

	
	private static final String OWNER_INPUT = "owner_input";
	private static final String OWNER_LABEL = "owner_label";

	private static final String SPREADSHEETURL_INPUT = "spreadsheeturl_input";
	private static final String SPREADSHEETURL_LABEL = "spreadsheeturl_label";

	private static final String USERNAME_INPUT = "username_input";
	private static final String USERNAME_LABEL = "username_label";

	private static final String PASSWORD_INPUT = "password_input";
	private static final String PASSWORD_LABEL = "password_label";

	private static final String ITERATION_INPUT = "iteration_input";
	private static final String ITERATION_LABEL = "iteration_label";

	private static final String START_ITERATION_BUTTON = "start_iteration_button";

	private IterationMetadata metadata;

	/**
	 * The blip that represents the current state of the admin form.
	 */
	private Blip blip;

	private String iterationName = null;
	private String startDate = null;
	private String duration = null;
	private String recipients =null;
	private String owner = null;
	private String spreadsheetURL=null;
	private String userName=null;
	private String password=null;

	public AdminPane(Blip blip, IterationMetadata metadata) {
		this.blip = blip;
		this.metadata = metadata;

		// Parse the blip to derive the current state of the form metadata.
		parseBlip();
	}

	/**
	 * Creates the AdminPane from an empty blip.
	 */
	public void create() {
		TextView textView = blip.getDocument();

		// Set the 'effective' author to be waveiteration bot.
		textView.setAuthor(AdminWavelet.WAVEITERATION);

		textView.append("\n\n");

		textView.appendElement(new FormElement(ElementType.LABEL,
				ITERATION_LABEL, "Iteration No:"));

		textView.appendElement(new FormElement(ElementType.INPUT,
				ITERATION_INPUT, metadata.getIterationName()));

		textView.append("\n");

		textView.appendElement(new FormElement(ElementType.LABEL,
				STARTDATE_LABEL, "Iteration Start Date:"));

		textView.appendElement(new FormElement(ElementType.INPUT,
				STARTDATE_INPUT, metadata.getStartDate()));

		textView.append("\n");

		textView.appendElement(new FormElement(ElementType.LABEL,
				DURATION_LABEL, "Duration:"));

		textView.appendElement(new FormElement(ElementType.INPUT,
				DURATION_INPUT, metadata.getDuration()));

		textView.appendElement(new FormElement(ElementType.LABEL,
				RECIPIENTS_LABEL,
				"Iteration members (comma separated list of participants):"));

		textView.appendElement(new FormElement(ElementType.INPUT,
				RECIPIENTS_INPUT, metadata.getRecipients()));

		
		textView.appendElement(new FormElement(ElementType.LABEL,
				OWNER_LABEL,
				"Iteration Owner (one of the recepients):"));

		textView.appendElement(new FormElement(ElementType.INPUT,
				OWNER_INPUT, metadata.getOwner()));

		textView.appendElement(new FormElement(ElementType.LABEL,
				SPREADSHEETURL_LABEL,
				"SpreadSheet URL where the iteration updates will be stored:"));

		textView.appendElement(new FormElement(ElementType.INPUT,
				SPREADSHEETURL_INPUT, metadata.getSpreadSheetURL()));

		textView.appendElement(new FormElement(ElementType.LABEL,
				USERNAME_LABEL,
				"UserName required to access the spreadsheet:"));

		textView.appendElement(new FormElement(ElementType.INPUT,
				USERNAME_INPUT, metadata.getUserName()));
		textView.appendElement(new FormElement(ElementType.LABEL,
				PASSWORD_LABEL,
				"Password required to access the spreadsheet:"));

		textView.appendElement(new FormElement(ElementType.INPUT,
				PASSWORD_INPUT, metadata.getPassword()));

		
		textView.append("\n");

		textView.appendElement(new FormElement(ElementType.BUTTON,
				START_ITERATION_BUTTON, "Start Iteration Wave"));

		// Create an annotation over the document so that we can recognize it.
		// No
		// value is set since we are only concerned with the existence of the
		// annotation.
		textView.setAnnotation("iteration-admin", "");

		// Parse the form to retrieve the inital values.
		parseBlip();
	}

	/**
	 * Parse the form elements in the blip to derive their current values.
	 */
	private void parseBlip() {
		try {
			
			FormView formView = blip.getDocument().getFormView();
			iterationName = formView.getFormElement(ITERATION_INPUT).getValue();
			metadata.setIterationName(iterationName);
			
			startDate = formView.getFormElement(STARTDATE_INPUT).getValue();
			metadata.setStartDate(startDate);
			
			duration = formView.getFormElement(DURATION_INPUT).getValue();
			metadata.setDuration(duration);
			
			recipients = formView.getFormElement(RECIPIENTS_INPUT).getValue();
			metadata.setRecipients(recipients);
			
			owner = formView.getFormElement(OWNER_INPUT).getValue();
			metadata.setOwner(owner);
			
			spreadsheetURL = formView.getFormElement(SPREADSHEETURL_INPUT).getValue();
			metadata.setSpreadSheetURL(spreadsheetURL);

			userName = formView.getFormElement(USERNAME_INPUT).getValue();
			metadata.setUserName(spreadsheetURL);

			password = formView.getFormElement(PASSWORD_INPUT).getValue();
			metadata.setPassword(password);
			
		} catch (NullPointerException npx) {
			// Form will not be valid until it has been fully created
			log.warning("inside parseBlip() "+npx);
		}
	}

	public String getIterationName() {
		return iterationName;
	}

	public String getStartDate() {
		return startDate;
	}


	public String getDuration() {
		return duration;
	}


	public String getRecipients() {
		return recipients;
	}


	

	/**
	 * Checks the to see if the 'distribute poll' button is currently clicked.
	 * If so, the button is reset into the unclicked/default state.
	 * 
	 * @return true if the button was clicked, false otherwise.
	 */
	public boolean isStartIterationButtonPressed() {
		log.warning("inside isStartIterationButtonPressed");
		FormView formView = blip.getDocument().getFormView();
		FormElement startIteration = formView
				.getFormElement(START_ITERATION_BUTTON);

		log.warning("inside isStartIterationButtonPressed startIteration.getValue()="+startIteration.getValue());
		
		boolean isPressed = "clicked".equals(startIteration.getValue());
		if (isPressed) {
			startIteration.setValue(startIteration.getDefaultValue());
			formView.replace(startIteration);
		}
		log.warning("inside isStartIterationButtonPressed returns "+isPressed);
		return isPressed;
	}
}
