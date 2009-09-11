
package com.punegtug.agile.iteration.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.wave.api.Blip;
import com.google.wave.api.ElementType;
import com.google.wave.api.FormElement;
import com.google.wave.api.FormView;
import com.google.wave.api.Range;
import com.google.wave.api.StyleType;
import com.google.wave.api.TextView;

/**
 * The Admin pane displays the form that allows the user to define the poll,
 * its title, question, choices and recipients.
 * 
 * @author scovitz@google.com (Seth Covitz)
 */
public class AdminPane {
  
  // Names used to get and set form element values.
  private static final String RECIPIENTS_LABEL = "recipients_label";
  private static final String RECIPIENTS_INPUT = "recipients_input";
  
  private static final String STARTDATE_INPUT = "startdate_input";
  private static final String STARTDATE_LABEL = "startdate_label";
  
  private static final String DURATION_INPUT = "duration_input";
  private static final String DURATION_LABEL = "duration_label";
  
  private static final String ITERATION_INPUT = "iteration_input";
  private static final String ITERATION_LABEL = "iteration_label";
  
  private static final String START_ITERATION_BUTTON = "start_iteration_button";

  /**
   * The metadata that represents the current state of the form if there has
   * been no change or the previous state of the form if it has been modified. 
   */
  private IterationMetadata metadata;

  /**
   * The blip that represents the current state of the admin form.
   */
  private Blip blip;
  
  /**
   * The current question text for the poll.
   */
  private String iterationName=null;
  
  private String startDate=null;
  private String duration=null;
  
  /**
   * The current list of recipients for the poll.
   */
  private String recipients;
  
  /**
   * Constructs an AdminPane from the current blip and poll metadata.
   * 
   * @param blip the blip within which the admin pane is present.
   * @param metadata the most recent metadata for the poll.
   */
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

    // Set the 'effective' author to be Polly.
    textView.setAuthor(AdminWavelet.WAVEITERATION);
    
    textView.append("\n\n");

    textView.appendElement(new FormElement(ElementType.LABEL, ITERATION_LABEL,
        "Iteration No:"));

    textView.appendElement(new FormElement(ElementType.INPUT, ITERATION_INPUT,
        metadata.getIterationName()));
    
    textView.append("\n");

    textView.appendElement(new FormElement(ElementType.LABEL, STARTDATE_LABEL,
        "Iteration Start Date:"));

    textView.appendElement(new FormElement(ElementType.INPUT, STARTDATE_INPUT,
        metadata.getStartDate()));
    
    textView.append("\n");
    
    textView.appendElement(new FormElement(ElementType.LABEL, DURATION_LABEL,
        "Duration:"));
    
    textView.appendElement(new FormElement(ElementType.INPUT, DURATION_INPUT,
            metadata.getDuration()));
    

    textView.appendElement(new FormElement(ElementType.LABEL, RECIPIENTS_LABEL,
        "Iteration members (comma separated list of participants):"));

    textView.appendElement(new FormElement(ElementType.INPUT, RECIPIENTS_INPUT,
        metadata.getRecipients()));
    
    textView.append("\n");

    textView.appendElement(new FormElement(ElementType.BUTTON, START_ITERATION_BUTTON,
        "Start Iteration Wave"));

    // Create an annotation over the document so that we can recognize it. No
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
      startDate = formView.getFormElement(STARTDATE_INPUT).getValue();
      duration = formView.getFormElement(DURATION_INPUT).getValue();
      recipients = formView.getFormElement(RECIPIENTS_INPUT).getValue();
    } catch(NullPointerException npx) {
      // Form will not be valid until it has been fully created.
    }
  }

  /**
   * Returns the current title of the poll.
   * 
   * @return the current title of the poll.
   */
  public String getIterationName() {
    return iterationName;
  }

  
  /**
   * Checks the current value of the question and the previous value to see if
   * it has changed.
   * 
   * @return true if the question has changed, false otherwise.
   */
  public boolean hasIterationNameChanged() {
    return !iterationName.equals(metadata.getIterationName());
  }
  
  /**
   * Returns the current question for the poll.
   * 
   * @return the current question for the poll.
   */
  public String getStartDate() {
    return startDate;
  }
  
  /**
   * Checks the current value of the question and the previous value to see if
   * it has changed.
   * 
   * @return true if the question has changed, false otherwise.
   */
  public boolean hasStartDateChanged() {
    return !startDate.equals(metadata.getStartDate());
  }
  
  /**
   * Returns the current list of choices for the poll.
   * 
   * @return the current list of choices for the poll.
   */
  public String getDuration() {
    return duration;
  }
  
  /**
   * Checks the current list of choices and the previous list to see if it has
   * changed.
   * 
   * @return true if the choices have changed, false otherwise.
   */
  public boolean haveDurationChanged() {
	  return !duration.equals(metadata.getDuration());
  }
  
  /**
   * Returns the current list of recipients for the poll.
   * 
   * @return the current list of recipients for the poll.
   */
  public String getRecipients() {
    return recipients;
  }
  
  /**
   * Checks the current list of recipients and the previous list to see if it
   * has changed.
   * 
   * @return true if the recipients have changed, false otherwise.
   */
  public boolean haveRecipientsChanged() {
    return !recipients.equals(metadata.getRecipients());
  }
  
  /**
   * Checks the admin pane to see if any of the poll data has changed since it
   * was last saved.
   * 
   * @return true if the data has changed, false otherwise.
   */
  public boolean hasChanged() {
    return hasIterationNameChanged() || hasStartDateChanged() || haveDurationChanged() ||
        haveRecipientsChanged();
  }

  /**
   * Checks the to see if the 'distribute poll' button is currently clicked. If
   * so, the button is reset into the unclicked/default state.
   * 
   * @return true if the button was clicked, false otherwise.
   */
  public boolean isStartIterationButtonPressed() {
    FormView formView = blip.getDocument().getFormView();
    FormElement startIteration = formView.getFormElement(START_ITERATION_BUTTON);
    
    boolean isPressed = "clicked".equals(startIteration.getValue());
    if (isPressed) {
    	startIteration.setValue(startIteration.getDefaultValue());
      formView.replace(startIteration);
    }
    
    return isPressed;
  }
}
