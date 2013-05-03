package org.vaadin.teemu.webcam;

import org.vaadin.teemu.webcam.client.WebcamClientRpc;
import org.vaadin.teemu.webcam.client.WebcamServerRpc;
import org.vaadin.teemu.webcam.client.WebcamState;

import com.vaadin.shared.MouseEventDetails;

// This is the server-side UI component that provides public API 
// for Webcam
public class Webcam extends com.vaadin.ui.AbstractComponent {

	private int clickCount = 0;

	// To process events from the client, we implement ServerRpc
	private WebcamServerRpc rpc = new WebcamServerRpc() {

		// Event received from client - user clicked our widget
		public void clicked(MouseEventDetails mouseDetails) {
			
			// Send nag message every 5:th click with ClientRpc
			if (++clickCount % 5 == 0) {
				getRpcProxy(WebcamClientRpc.class)
						.alert("Ok, that's enough!");
			}
			
			// Update shared state. This state update is automatically 
			// sent to the client. 
			getState().text = "You have clicked " + clickCount + " times";
		}
	};

	public Webcam() {

		// To receive events from the client, we register ServerRpc
		registerRpc(rpc);
	}

	// We must override getState() to cast the state to WebcamState
	@Override
	public WebcamState getState() {
		return (WebcamState) super.getState();
	}
}
