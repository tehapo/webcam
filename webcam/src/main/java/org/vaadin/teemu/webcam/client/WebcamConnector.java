package org.vaadin.teemu.webcam.client;

import org.vaadin.teemu.webcam.Webcam;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * Binds the server-side {@link Webcam} with the client-side
 * {@link WebcamWidget}.
 */
@Connect(Webcam.class)
@SuppressWarnings("serial")
public class WebcamConnector extends AbstractComponentConnector {

    private WebcamServerRpc rpc = RpcProxy.create(WebcamServerRpc.class, this);

    public WebcamConnector() {
        getWidget().addClickListener(new EventListener() {

            @Override
            public void onBrowserEvent(Event event) {
                // Commanded by the user clicking the client-side widget.
                captureNow();
            }
        });

        registerRpc(WebcamClientRpc.class, new WebcamClientRpc() {

            @Override
            public void capture() {
                // Commanded by the server-side component.
                captureNow();
            }
        });
    }

    private void captureNow() {
        String dataUrl = getWidget().captureAsDataURL();
        rpc.captured(dataUrl);
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(WebcamWidget.class);
    }

    @Override
    public WebcamWidget getWidget() {
        return (WebcamWidget) super.getWidget();
    }

}
