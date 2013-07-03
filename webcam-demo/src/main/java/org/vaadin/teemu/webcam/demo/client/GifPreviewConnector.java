package org.vaadin.teemu.webcam.demo.client;

import org.vaadin.teemu.webcam.demo.GifPreview;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(GifPreview.class)
public class GifPreviewConnector extends AbstractComponentConnector {

    @Override
    protected Widget createWidget() {
        return GWT.create(GifPreviewWidget.class);
    }

    @Override
    public GifPreviewWidget getWidget() {
        return (GifPreviewWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        GifPreviewWidget widget = getWidget();
        GifPreviewState state = getState();

        widget.reset();
        for (int i = 0; i < state.frameCount; i++) {
            widget.addImage(getResourceUrl("" + i));
        }
        widget.startPreview(state.frameDelay);
    }

    @Override
    public GifPreviewState getState() {
        return (GifPreviewState) super.getState();
    }

}
