
package com.punegtug.agile.iteration.server;

import com.google.wave.api.Blip;
import com.google.wave.api.RobotMessageBundle;
import com.google.wave.api.StyleType;
import com.google.wave.api.StyledText;
import com.google.wave.api.Wavelet;

/**
 * The Poll wavelet is distributed to each poll recipient and allows them to
 * vote/respond to the poll question.
 * 
 * @author scovitz@google.com (Seth Covitz)
 */
public class IterationWavelet {

  /**
   * A reference to the wavelet that received the event or represents the
   * poll.
   */
  private Wavelet wavelet;
  
  /**
   * The persistent state of the poll. 
   */
  private IterationMetadata metadata;
 


  /**
   * Constructs a PollWavelet in the current robot context. This constructor
   * is called when the PollWavelet receives an event.
   * 
   * @param context the context within which the robot is currently running.
   */
  public IterationWavelet(RobotMessageBundle context) {
    this.wavelet = context.getWavelet();
    this.metadata = new IterationMetadata(context);
    
    Blip rootBlip = wavelet.getRootBlip();

  }

  /**
   * Constructs a PollWavelet given the current robot context and a newly
   * created Wavelet. New PollMetadata is created for each poll.
   * 
   * @param context the context within which the robot is currently running.
   * @param wavelet a new wavelet to be initialized as a poll.
   */
  public IterationWavelet(RobotMessageBundle context, Wavelet wavelet) {
    this.wavelet = wavelet;
    this.metadata = new IterationMetadata(context);

  }

  /**
   * Determines whether the wavelet that received an event has been initialized
   * by Polly as a Poll Wavelet.
   * 
   * @param context the context within which the robot is currently running.
   * @return true if the wavelet is a poll wavelet, false otherwise.
   */
  public static boolean isPollWavelet(RobotMessageBundle context) {
    return context.getWavelet().getRootBlip().getDocument().hasAnnotation("poll-wavelet");
  }

  /**
   * Creates the poll form. The poll form consists of a single poll pane that
   * allows the recipient to vote on the poll and submit their vote when done.
   */
  public void create(Wavelet resultsWavelet, String recipient) {
    metadata.setRecipients(recipient);
    wavelet.setTitle(new StyledText("Iteration No:"+metadata.getIterationName() , StyleType.HEADING2));

    wavelet.getRootBlip().getDocument().setAnnotation("poll-wavelet", "");
    
    metadata.saveMetadata(wavelet);
  }

  /**
   * A handler for PollWavelet events.
   */
  public void handleEvents() {
  }

  /**
   * Submits the user's vote by writing the vote to the results wavelet's (aka
   * Admin Wavelet) data document.
   */
  private void submitPoll() {

  }
}
