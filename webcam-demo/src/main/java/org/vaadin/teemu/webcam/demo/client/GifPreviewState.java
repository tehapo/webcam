package org.vaadin.teemu.webcam.demo.client;

import com.vaadin.shared.AbstractComponentState;

@SuppressWarnings("serial")
public class GifPreviewState extends AbstractComponentState {

    /** Number of frames available as resources. */
    public int frameCount;

    public int frameDelay = 200;

}
