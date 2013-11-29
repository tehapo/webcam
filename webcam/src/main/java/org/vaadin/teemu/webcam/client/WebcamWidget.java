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
    private WebcamStream stream;

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

    private void webcamAvailable() {
        // Sink the events only after we get the webcam.
        DOM.sinkEvents(video.getElement(), Event.ONCLICK);
    }

    private void webcamNotAvailable() {
        // TODO - better error handling
        Window.alert("Webcam not available.");
    }

    private Element getVideoElement() {
        return video.getElement();
    }

    public void setClickListener(EventListener listener) {
        if (video != null) {
            DOM.setEventListener(video.getElement(), listener);
        }
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        if (stream != null) {
            stream.stop();
        }
    }

    // @formatter:off
    private native String requestWebCam() /*-{
        var callbackInstance = this;
        $wnd.navigator.getMedia = ($wnd.navigator.getUserMedia || $wnd.navigator.webkitGetUserMedia || $wnd.navigator.mozGetUserMedia || $wnd.navigator.msGetUserMedia);
        $wnd.navigator.getMedia({ video: true, audio: false },
            function(stream) {
                var videoElement = callbackInstance.@org.vaadin.teemu.webcam.client.WebcamWidget::getVideoElement()();
                callbackInstance.@org.vaadin.teemu.webcam.client.WebcamWidget::stream = stream;
                if ($wnd.navigator.mozGetUserMedia) {
                    // Firefox
                    videoElement.mozSrcObject = stream;
                } else {
                    // Other browsers
                    if ($wnd.URL) {
                        videoElement.src = $wnd.URL.createObjectURL(stream);
                    } else if ($wnd.webkitURL) {
                        videoElement.src = $wnd.webkitURL.createObjectURL(stream);
                    }
                }
                videoElement.play();
                callbackInstance.@org.vaadin.teemu.webcam.client.WebcamWidget::webcamAvailable()();
            },
            function(error) {
                callbackInstance.@org.vaadin.teemu.webcam.client.WebcamWidget::webcamNotAvailable()();
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