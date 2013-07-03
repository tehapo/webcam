package org.vaadin.teemu.webcam.demo;

import org.vaadin.teemu.webcam.demo.client.GifPreviewState;

import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;

@SuppressWarnings("serial")
public class GifPreview extends AbstractComponent {

    public void addImage(Resource image) {
        setResource("" + getState().frameCount, image);
        getState().frameCount++;
    }

    @Override
    protected GifPreviewState getState() {
        return (GifPreviewState) super.getState();
    }

}
