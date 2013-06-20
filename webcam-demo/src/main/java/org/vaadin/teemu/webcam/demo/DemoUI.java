package org.vaadin.teemu.webcam.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.teemu.webcam.Webcam;
import org.vaadin.teemu.webcam.Webcam.CaptureSucceededEvent;
import org.vaadin.teemu.webcam.Webcam.CaptureSucceededListener;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.FileResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("Webcam Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    private VerticalLayout layout;
    private Image gifImage;
    private CssLayout framesLayout;
    private List<File> imageFiles = new ArrayList<File>();
    private HorizontalLayout webcamAndGif;
    private ImageReceiver imageReceiver = new ImageReceiver();

    @Override
    protected void init(VaadinRequest request) {

        Upload upload = new Upload("Upload", imageReceiver);
        upload.addSucceededListener(imageReceiver);

        // Initialize our new UI component
        final Webcam webcam = new Webcam();
        webcam.setReceiver(imageReceiver);
        webcam.addCaptureSucceededListener(imageReceiver);

        layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.setSpacing(true);

        webcamAndGif = new HorizontalLayout();
        webcamAndGif.setSpacing(true);
        webcamAndGif.setMargin(true);
        webcamAndGif.setWidth("900px");
        webcamAndGif.addComponent(webcam);
        webcamAndGif.addComponent(gifImage = new Image(null, new ThemeResource(
                "images/sample.gif")));
        gifImage.setWidth("100%");

        // layout.addComponent(upload);
        layout.addComponent(webcamAndGif);
        layout.addComponent(framesLayout = new CssLayout());
        layout.setComponentAlignment(webcamAndGif, Alignment.TOP_CENTER);
        layout.setComponentAlignment(framesLayout, Alignment.TOP_CENTER);
        framesLayout.setWidth("864px");
        setContent(layout);
    }

    private void addImage(File imageFile) {
        Image image = new Image(null, new FileResource(imageFile));
        image.setWidth("10%");
        framesLayout.addComponent(image);

        imageFiles.add(imageFile);
        if (imageFiles.size() > 1) {
            try {
                File gifImageFile = GifUtil.writeAnimatedGif(imageFiles
                        .toArray(new File[imageFiles.size()]));
                Image newGif = new Image(null, new FileResource(gifImageFile));
                newGif.setWidth("100%");
                webcamAndGif.replaceComponent(gifImage, newGif);
                gifImage = newGif;
            } catch (IOException e) {
                Notification
                        .show("Whoops, something went wrong. Please try again later.",
                                Type.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private class ImageReceiver implements Receiver, SucceededListener,
            CaptureSucceededListener {

        private File targetFile;

        @Override
        public OutputStream receiveUpload(String filename, String mimeType) {
            try {
                targetFile = File.createTempFile(filename, ".jpeg");
                targetFile.deleteOnExit();
                return new FileOutputStream(targetFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void uploadSucceeded(SucceededEvent event) {
            // From upload.
            addImage(targetFile);
        }

        @Override
        public void captureSucceeded(CaptureSucceededEvent event) {
            // From webcam.
            addImage(targetFile);
        }
    };
}
