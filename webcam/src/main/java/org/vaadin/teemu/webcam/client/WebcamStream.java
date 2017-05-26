package org.vaadin.teemu.webcam.client;

import com.google.gwt.core.client.JavaScriptObject;

public class WebcamStream extends JavaScriptObject {

    protected WebcamStream() {
        // Required constructor – intentionally empty.
    }

    // @formatter:off
    
    public final native void stop() /*-{
        for (var i = 0; i < this.getTracks().length; i++) {
            var track = this.getTracks()[i];
            if(track !== null) {
                track.stop();
            }
        }
    }-*/;
    
    // @formatter:on

}
