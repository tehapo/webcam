package org.vaadin.teemu.webcam.demo.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.WidgetCollection;

public class GifPreviewWidget extends FlowPanel {

    private Timer frameTimer;

    public GifPreviewWidget() {
        addStyleName("gif-preview");
    }

    /**
     * Removes all current frames and cancels the preview animation.
     */
    public void reset() {
        clear();
        if (frameTimer != null) {
            frameTimer.cancel();
        }
    }

    /**
     * Starts the preview animation with the given {@code delay} between frames.
     * Add the frames using {@link #addImage(String)} method before starting the
     * preview.
     */
    public void startPreview(int delay) {
        frameTimer = new Timer() {

            private int currentFrame;

            @Override
            public void run() {
                WidgetCollection frames = getChildren();
                for (int i = 0; i < frames.size(); i++) {
                    frames.get(i).setVisible(i == currentFrame);
                }
                currentFrame++;
                if (currentFrame >= frames.size()) {
                    currentFrame = 0;
                }
            }
        };
        frameTimer.scheduleRepeating(delay);
    }

    /**
     * Adds an image as a frame to the preview.
     * 
     * @param resourceUrl
     */
    public void addImage(String resourceUrl) {
        Image img = new Image(resourceUrl);
        img.setWidth("100%");
        add(img);
        if (getChildren().size() > 1) {
            img.setVisible(false);
        }
    }

}
