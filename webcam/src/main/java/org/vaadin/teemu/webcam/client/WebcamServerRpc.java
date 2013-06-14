package org.vaadin.teemu.webcam.client;

import com.vaadin.shared.communication.ServerRpc;

public interface WebcamServerRpc extends ServerRpc {

    /**
     * Sends the captured webcam image as a data URL to the server.
     */
    public void captured(String imgAsDataUrl);

}
