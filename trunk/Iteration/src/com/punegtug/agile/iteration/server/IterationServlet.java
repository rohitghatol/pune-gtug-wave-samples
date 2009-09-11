/**
 * 
 */
package com.punegtug.agile.iteration.server;

import java.util.logging.Logger;

import com.google.wave.api.AbstractRobotServlet;
import com.google.wave.api.Gadget;
import com.google.wave.api.RobotMessageBundle;

public class IterationServlet extends AbstractRobotServlet {

	private static final Logger log = Logger.getLogger(IterationServlet.class
			.getName());

	private static final String IterationGadgetURI="http://waveiteration.appspot.com/iteration/com.punegtug.agile.iteration.client.Iteration.gadget.xml";
	@SuppressWarnings("unchecked")
	@Override
	public void processEvents(RobotMessageBundle robotMessageBundle) {
		log.warning("cron job start!");
		Gadget iterationGadget = new Gadget(IterationGadgetURI);
		robotMessageBundle.getWavelet().getRootBlip().getDocument().append("Iteration Robot is now activie");
		robotMessageBundle.getWavelet().getRootBlip().getDocument().appendElement(iterationGadget);
		log.warning("cron job end!");
	}

}
