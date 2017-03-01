package org.vaadin.teemu.webcam.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.media.client.Video;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
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
            video.setAutoplay(true);
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
    var gotDevices = function(deviceInfos) {
        for (var i = 0; i !== deviceInfos.length; ++i) {
           var deviceInfo = deviceInfos[i];
           if (deviceInfo.kind == 'videoinput' && (!deviceInfo.label ||  deviceInfo.label.indexOf('fron') < 0)) {
              if ($wnd.navigator.mediaDevices && $wnd.navigator.mediaDevices.getUserMedia) {
                   $wnd.navigator.mediaDevices.getUserMedia({video: {deviceId : deviceInfo.deviceId}})
                       .then(play);  //.catch(errorCallback);
              } else  if ($wnd.navigator.getUserMedia) {
                $wnd.navigator.getUserMedia({video: {deviceId : deviceInfo.deviceId}}, playLegacy, errorCallback);
              } else if($wnd.navigator.webkitGetUserMedia) { // WebKit-prefixed
                 $wnd.navigator.webkitGetUserMedia( {video: {deviceId : deviceInfo.deviceId}}, play, errorCallback);
              } else if($wnd.navigator.mozGetUserMedia) { // Mozilla-prefixed 
                $wnd.navigator.getUserMedia({video: {deviceId : deviceInfo.deviceId}}, play, errorCallback);
              }
              return;
            }
           }}
           
    var play = function(stream) {
        var v = callbackInstance.@org.vaadin.teemu.webcam.client.WebcamWidget::getVideoElement()();        
        callbackInstance.@org.vaadin.teemu.webcam.client.WebcamWidget::stream = stream;
        v.src = $wnd.URL.createObjectURL(stream);
        callbackInstance.@org.vaadin.teemu.webcam.client.WebcamWidget::webcamAvailable()();
    }
    
    var playLegacy = function(stream) {
        var v = callbackInstance.@org.vaadin.teemu.webcam.client.WebcamWidget::getVideoElement()();
        callbackInstance.@org.vaadin.teemu.webcam.client.WebcamWidget::stream = stream;
        v.src = stream;
        callbackInstance.@org.vaadin.teemu.webcam.client.WebcamWidget::webcamAvailable()();
    }
    
    var errorCallback = function(error) {
        $wnd.alert("Webcam not available. " + error);
    }
           
        navigator.mediaDevices.enumerateDevices().then(gotDevices); //.catch(errorCallback);
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