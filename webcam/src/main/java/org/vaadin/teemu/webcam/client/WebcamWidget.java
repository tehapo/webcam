package org.vaadin.teemu.webcam.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.media.client.Video;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class WebcamWidget extends Widget {

    private final Video video;

    public WebcamWidget() {
        setElement(DOM.createDiv());
        setStyleName("webcam");

        video = Video.createIfSupported();
        if (video != null) {
            getElement().appendChild(video.getElement());
            video.setWidth("100%");
            video.setHeight("100%");
            requestWebCam();
        } else {
            com.google.gwt.dom.client.Element errorDiv = DOM.createDiv();
            errorDiv.setInnerText("Video not supported.");
            getElement().appendChild(errorDiv);
        }
    }

    private void setVideoSrc(String src) {
        video.setSrc(src);
        video.play();
    }

    private void webcamNotSupported() {
        // TODO - better error handling
        Window.alert("Webcam not available.");
    }

    private Element getVideoElement() {
        return video.getElement();
    }

    public void addClickListener(EventListener listener) {
        if (video != null) {
            DOM.sinkEvents(video.getElement(), Event.ONCLICK);
            DOM.setEventListener(video.getElement(), listener);
        }
    }

    // @formatter:off
    private native String requestWebCam() /*-{
        var callbackInstance = this;
        $wnd.navigator.webkitGetUserMedia({ video: true },
            function(stream) {
                var url = $wnd.webkitURL.createObjectURL(stream);
                callbackInstance.@org.vaadin.teemu.webcam.client.WebcamWidget::setVideoSrc(Ljava/lang/String;)(url);
            },
            function(error) {
                callbackInstance.@org.vaadin.teemu.webcam.client.WebcamWidget::webcamNotSupported()();
            }
        );
    }-*/;

    public native String captureAsDataURL() /*-{
        var canvas = $doc.createElement("canvas");
        var videoElement = this.@org.vaadin.teemu.webcam.client.WebcamWidget::getVideoElement()();
        canvas.width = videoElement.videoWidth;
        canvas.height = videoElement.videoHeight;
        
        var context = canvas.getContext("2d");
        context.drawImage(videoElement, 0, 0, canvas.width, canvas.height);
        return canvas.toDataURL("image/jpeg");
    }-*/;
    // @formatter:on
}