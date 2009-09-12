package com.punegtug.agile.iteration.server;

import java.util.logging.Logger;

import com.google.wave.api.Blip;
import com.google.wave.api.Gadget;
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

	private static final Logger log = Logger.getLogger(IterationWavelet.class
			.getName());

	private static final String IterationGadgetURI = "http://i-mank.appspot.com/iteration/com.punegtug.agile.iteration.client.Iteration.gadget.xml";

	/**
	 * A reference to the wavelet that received the event or represents the
	 * poll.
	 */
	private Wavelet wavelet;

	private RobotMessageBundle context = null;

	/**
	 * The persistent state of the poll.
	 */
	private IterationMetadata metadata;

	/**
	 * Constructs a PollWavelet in the current robot context. This constructor
	 * is called when the PollWavelet receives an event.
	 * 
	 * @param context
	 *            the context within which the robot is currently running.
	 */
	public IterationWavelet(RobotMessageBundle context) {
		this.wavelet = context.getWavelet();
		this.context = context;
		this.metadata = new IterationMetadata(context);

		Blip rootBlip = wavelet.getRootBlip();

	}


	/**
	 * Constructs a PollWavelet given the current robot context and a newly
	 * created Wavelet. New PollMetadata is created for each poll.
	 * 
	 * @param context
	 *            the context within which the robot is currently running.
	 * @param wavelet
	 *            a new wavelet to be initialized as a poll.
	 */
	public IterationWavelet(RobotMessageBundle context, Wavelet wavelet) {
		this.wavelet = wavelet;
		this.metadata = new IterationMetadata(context);

	}

	/**
	 * Determines whether the wavelet that received an event has been
	 * initialized by Polly as a Poll Wavelet.
	 * 
	 * @param context
	 *            the context within which the robot is currently running.
	 * @return true if the wavelet is a poll wavelet, false otherwise.
	 */
	public static boolean isIterationWavelet(RobotMessageBundle context) {
		return context.getWavelet().getRootBlip().getDocument().hasAnnotation(
				"iteration-progress");
	}

	
	public void create(Wavelet iterationWavelet) {
		
		wavelet.setTitle(new StyledText("Iteration No:"
				+ metadata.getIterationName(), StyleType.HEADING2));

		wavelet.getRootBlip().getDocument().setAnnotation("poll-wavelet", "");
		Gadget iterationGadget = new Gadget(IterationGadgetURI);
		iterationGadget.setField("iterationNo", metadata.getIterationName());
		iterationGadget.setField("startDate", metadata.getStartDate());
		iterationGadget.setField("duration", metadata.getDuration());
		iterationGadget.setField("members", metadata.getRecipients());

		iterationGadget.setField("owner", metadata.getOwner());

		wavelet.getRootBlip().getDocument().appendElement(iterationGadget);
		metadata.saveMetadata(wavelet);
		wavelet.getRootBlip().getDocument().setAnnotation("iteration-progress","iteration-progress");
	}

	/**
	 * A handler for PollWavelet events.
	 */
	public void handleEvents() {
	}

}
