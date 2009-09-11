
package com.punegtug.agile.iteration.server;

import java.util.ArrayList;
import java.util.List;

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

  /**
   * WaveIterations' identity required for setting authorship for newly created
   * polls.
   */
  static final String WAVEITERATION = "waveiteration@appspot.com";

  /**
   * The robot context. This is only valid for one iteration of the robot's
   * event processing loop and should not be cached or persisted.
   */
  private RobotMessageBundle context;
  
  /**
   * A reference to the wavelet that received the event.
   */
  private Wavelet wavelet;
  
  /**
   * The persistent state of the poll. 
   */
  private IterationMetadata metadata;
  
  /**
   * A convenient wrapper to query/manipulate a virtual admin pane in the poll.
   */
  private AdminPane adminPane;
  
  
  /**
   * Determines whether the wavelet that received an event has been initialized
   * by Polly as an Admin Wavelet.
   * 
   * @param context the context within which the robot is currently running.
   * @return true if the wavelet is an admin wavelet, false otherwise.
   */
  public static boolean isAdminWavelet(RobotMessageBundle context) {
    return isInitialized(context.getWavelet().getRootBlip());
  }

  /**
   * Determines if a blip is part of an AdminWavelet by checking for the 
   * presence of the 'poll-admin' annotation.
   * 
   * @param blip the blip to check.
   * @return true if the blip is part of an admin wavelet, false otherwise.
   */
  private static boolean isInitialized(Blip blip) {
    return blip.getDocument().hasAnnotation("iteration-admin");
  }
  
  /**
   * Constructs an AdminWavelet given the current robot context. 
   * 
   * @param context the context within which the robot is currently running.
   */
  public AdminWavelet(RobotMessageBundle context) {
    this.context = context;
    this.wavelet = context.getWavelet();
    this.metadata = new IterationMetadata(context);
    
    Blip rootBlip = wavelet.getRootBlip();
    this.adminPane = new AdminPane(rootBlip, metadata);
  }

  /**
   * A handler for AdminWavelet events.
   */
  public void handleEvents() {
    // If the admin form builder has not been initialized, then initialize it.
    if (!isInitialized(wavelet.getRootBlip())) {
      create();
    }
    
    // As the user builds the poll, update the preview.
    if (adminPane.hasChanged()) {
      syncPreview();
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

  /**
   * Create the admin form builder for this form. The Admin form consists of
   * three panes. The admin pane defines the question and possible answers.The
   * preview pane shows the user how the form will look when distributed. The
   * results pane shows the poll votes as they come in.
   */
  public void create() {
    setTitle(metadata.getIterationName());
    adminPane.create();
  
  }

  /**
   * Sets the title of the wave using StyledText.
   * 
   * @param title the title of the wave.
   */
  private void setTitle(String title) {
    wavelet.setTitle(new StyledText(title + " -- Administration", StyleType.HEADING2));
  }

  /**
   * Syncs the contents of the preview pane with anything that has changed in
   * the admin pane. The results pane is also updated if new votes (or changed
   * votes) have come in.
   */
  private void syncPreview() {
    if (adminPane.hasIterationNameChanged()) {
      setTitle(adminPane.getIterationName());
      metadata.setIterationName(adminPane.getIterationName());
    }
    
    if (adminPane.hasStartDateChanged()) {

      metadata.setStartDate(adminPane.getStartDate());
    }
    
    if (adminPane.haveDurationChanged()) {

      metadata.setDuration(adminPane.getDuration());
    }
    
    if (adminPane.haveRecipientsChanged()) {
      metadata.setRecipients(adminPane.getRecipients());
    }
    
  }
  
  /**
   * Distributes the poll to all recipients. As a convenience, recipients
   * specified without a domain are assumed to be in the same domain as the
   * poll creator.
   */
  private void startIteration() {
    for (String recipient : metadata.getRecipientsAsList()) {
      // Append domain of the poll creator if not specified.
      if (!recipient.contains("@")) {
        String domain = wavelet.getCreator().split("@")[1];
        recipient = recipient + "@" + domain;
      }
      
      // Initial participant list for poll must include POLLY so that the poll
      // events can be monitored.
      List<String> participants = new ArrayList<String>();
      participants.add(recipient);
      participants.add(WAVEITERATION);

      // Create the poll.
      IterationWavelet iteration = new IterationWavelet(context, context.createWavelet(
          participants, metadata.getIterationWriteBackId(recipient)));
      iteration.create(wavelet, recipient);
      
     
    }
    
  }
}
