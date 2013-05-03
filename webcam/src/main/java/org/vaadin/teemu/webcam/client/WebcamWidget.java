package org.vaadin.teemu.webcam.client;

import com.google.gwt.user.client.ui.Label;

// Extend any GWT Widget
public class WebcamWidget extends Label {

	public WebcamWidget() {

		// CSS class-name should not be v- prefixed
		setStyleName("webcam");

		// State is set to widget in WebcamConnector		
	}

}