package org.vaadin.teemu.webcam.client;

import com.google.gwt.core.client.JavaScriptObject;

public class WebcamStream extends JavaScriptObject {

    protected WebcamStream() {
        // Required constructor – intentionally empty.
    }

    // @formatter:off
    
    public final native void stop() /*-{
        this.getVideoTracks()[0].stop();
		this.getAudioTracks()[0].stop();
    }-*/;
    
    // @formatter:on

}
