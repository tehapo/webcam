package org.vaadin.teemu.webcam;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.vaadin.teemu.webcam.client.WebcamClientRpc;
import org.vaadin.teemu.webcam.client.WebcamServerRpc;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;

/**
 * Displays a live webcam viewfinder and takes a photo when either user clicks
 * on the viewfinder or the {@link #capture()} method is called.
 * 
 * <br>
 * <br>
 * To receive the captured image, the {@link Upload.Receiver} interface is used
 * similarily to the {@link Upload} component of core Vaadin framework.
 */
@SuppressWarnings("serial")
public class Webcam extends AbstractComponent {

    private static final Method CAPTURE_SUCCEEDED_METHOD;
    protected Receiver receiver;

    static {
        try {
            CAPTURE_SUCCEEDED_METHOD = CaptureSucceededListener.class
                    .getDeclaredMethod("captureSucceeded",
                            new Class[] { CaptureSucceededEvent.class });
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error finding methods in Webcam");
        }
    }

    public Webcam() {
        registerRpc(new WebcamServerRpc() {
            public void captured(String dataUrl) {
                handleDataUrl(dataUrl);
            }
        });
    }

    public void setReceiver(Receiver reciver) {
        this.receiver = reciver;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void addCaptureSucceededListener(CaptureSucceededListener listener) {
        addListener(CaptureSucceededEvent.class, listener,
                CAPTURE_SUCCEEDED_METHOD);
    }

    public void removeCaptureSucceededListener(CaptureSucceededListener listener) {
        removeListener(CaptureSucceededEvent.class, listener,
                CAPTURE_SUCCEEDED_METHOD);
    }

    public void capture() {
        getRpcProxy(WebcamClientRpc.class).capture();
    }

    protected OutputStream getOutputStream(String filename, String mimeType) {
        if (receiver != null) {
            return receiver.receiveUpload(filename, mimeType);
        }
        return null;
    }

    protected String generateFilename(String mimeType) {
        String randomUUID = UUID.randomUUID().toString();
        if (mimeType.equals("image/jpeg")) {
            return randomUUID + ".jpeg";
        } else if (mimeType.equals("image/png")) {
            return randomUUID + ".png";
        }
        return randomUUID;
    }

    protected void handleDataUrl(String dataUrl) {
        Pattern dataUrlPattern = Pattern.compile("data:(.*);base64,(.*)");
        Matcher matcher = dataUrlPattern.matcher(dataUrl);
        matcher.matches();

        String mimeType = matcher.group(1);
        String dataBase64 = matcher.group(2);
        String filename = generateFilename(mimeType);

        OutputStream out = getOutputStream(filename, mimeType);
        if (out != null) {
            try {
                // Convert the base64 encoded image to bytes.
                byte[] imageData = DatatypeConverter
                        .parseBase64Binary(dataBase64);
                out.write(imageData);
                out.flush();
                fireEvent(new CaptureSucceededEvent(this));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface CaptureSucceededListener extends Serializable {

        /**
         * Called when an image was successfully captured from the webcam.
         * 
         * @param event
         *            capture event.
         */
        public void captureSucceeded(CaptureSucceededEvent event);
    }

    public class CaptureSucceededEvent extends Component.Event {

        public CaptureSucceededEvent(Component source) {
            super(source);
        }

    }
}
