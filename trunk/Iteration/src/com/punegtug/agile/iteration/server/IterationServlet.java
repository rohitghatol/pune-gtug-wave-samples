/**
 * 
 */
package com.punegtug.agile.iteration.server;

import java.util.logging.Logger;

import com.google.wave.api.AbstractRobotServlet;
import com.google.wave.api.RobotMessageBundle;

public class IterationServlet extends AbstractRobotServlet {

	private static final Logger log = Logger.getLogger(IterationServlet.class
			.getName());

	
	@SuppressWarnings("unchecked")
	@Override
	public void processEvents(RobotMessageBundle robotMessageBundle) {
		// Iteration Administration Flow
		log.warning("inside IterationServlet processEvents()");
	    if (isNewIterationWave(robotMessageBundle) || AdminWavelet.isAdminWavelet(robotMessageBundle)) {
	      log.warning("Creating an AdminWave");
	      AdminWavelet adminWavelet = new AdminWavelet(robotMessageBundle);
	      adminWavelet.handleEvents();
	      return;
	    }
	    

	}

	private boolean isNewIterationWave(RobotMessageBundle context) {
		return context.wasSelfAdded()
				&& context.getWavelet().getTitle().isEmpty();
	}

}
