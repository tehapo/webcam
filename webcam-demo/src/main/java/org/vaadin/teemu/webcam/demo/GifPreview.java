package org.vaadin.teemu.webcam.demo;

import org.vaadin.teemu.webcam.demo.client.GifPreviewState;

import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;

@SuppressWarnings("serial")
public class GifPreview extends AbstractComponent {

    public void setFrameDelay(int frameDelayMs) {
        getState().frameDelay = frameDelayMs;
    }

    public void addImage(Resource image) {
        setResource("" + getState().frameCount, image);
        getState().frameCount++;
    }

    public void removeImage(int frameIndex) {
        for (int i = frameIndex; i < getState().frameCount - 1; i++) {
            setResource("" + i, getResource("" + (i + 1)));
        }
        getState().frameCount--;
    }

    @Override
    protected GifPreviewState getState() {
        return (GifPreviewState) super.getState();
    }

}
