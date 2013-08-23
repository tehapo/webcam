package org.vaadin.teemu.webcam.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vaadin.teemu.webcam.Webcam;
import org.vaadin.teemu.webcam.Webcam.CaptureSucceededEvent;
import org.vaadin.teemu.webcam.Webcam.CaptureSucceededListener;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("GifBooth")
@SuppressWarnings("serial")
public class GifBoothUI extends UI {

    private VerticalLayout layout;
    private Image gifImage;
    private CssLayout framesLayout;
    private List<File> imageFiles = new ArrayList<File>();
    private HorizontalLayout webcamAndGif;
    private ImageReceiver imageReceiver = new ImageReceiver();
    private GifPreview preview = new GifPreview();
    private Button downloadButton;
    private int frameDelayMs = 200;
    private final FileDownloader downloader = new FileDownloader(
            new StreamResource(new StreamSource() {

                @Override
                public InputStream getStream() {
                    final File gifFile = createGif();
                    if (gifFile != null) {
                        try {
                            return new FileInputStream(gifFile);
                        } catch (FileNotFoundException e) {
                            handleException(e);
                        }
                    }
                    return null;
                }
            }, "gifbooth.gif"));

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
        layout.setSpacing(true);

        webcamAndGif = new HorizontalLayout();
        webcamAndGif.setSpacing(true);
        webcamAndGif.setMargin(true);
        webcamAndGif.setWidth("900px");
        webcamAndGif.addComponent(webcam);
        webcamAndGif.addComponent(gifImage = new Image(null, new ThemeResource(
                "images/sample.gif")));
        preview.setWidth("100%");
        gifImage.addStyleName("gif-image");
        gifImage.setWidth("100%");

        // layout.addComponent(upload);
        layout.addComponent(new Label("GifBooth"));
        layout.addComponent(webcamAndGif);
        layout.setComponentAlignment(webcamAndGif, Alignment.TOP_CENTER);
        downloadButton = new Button("Download as animated Gif");
        downloadButton.setDisableOnClick(true);
        downloadButton.addStyleName("download");
        downloadButton.setEnabled(false);
        downloader.extend(downloadButton);
        Component frameRateSelector = createFrameRateSelector();
        layout.addComponent(frameRateSelector);
        layout.setComponentAlignment(frameRateSelector, Alignment.TOP_CENTER);
        layout.addComponent(downloadButton);
        layout.setComponentAlignment(downloadButton, Alignment.TOP_CENTER);
        layout.addComponent(framesLayout = new CssLayout());
        layout.setComponentAlignment(framesLayout, Alignment.TOP_CENTER);
        framesLayout.addStyleName("frames");
        framesLayout.setWidth("864px");
        setContent(layout);
    }

    private Component createFrameRateSelector() {
        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setCaption("Framerate");
        buttons.addStyleName("framerate");
        Button.ClickListener listener = new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                for (Component button : buttons) {
                    button.removeStyleName("selected");
                }
                frameDelayMs = (Integer) event.getButton().getData();
                preview.setFrameDelay(frameDelayMs);
                event.getButton().addStyleName("selected");
            }
        };
        int[] options = { 300, 200, 100 };
        for (int option : options) {
            Button b = new Button("", listener);
            b.setIcon(new ThemeResource(String.format("images/frames-%d.png",
                    option)));
            b.setData(option);
            buttons.addComponent(b);
            if (option == frameDelayMs) {
                b.addStyleName("selected");
            }
        }
        return buttons;
    }

    private void addImage(final File imageFile) {
        FileResource imageResource = new FileResource(imageFile);

        // Frame list
        Image image = new Image(null, imageResource);
        image.setWidth("104px");
        final CssLayout wrapper = new CssLayout();
        wrapper.addComponent(image);
        image.addClickListener(new ClickListener() {

            @Override
            public void click(ClickEvent event) {
                framesLayout.removeComponent(wrapper);
                preview.removeImage(imageFiles.indexOf(imageFile));
                imageFiles.remove(imageFile);
                downloadButton.setEnabled(!imageFiles.isEmpty());
            }
        });
        framesLayout.addComponent(wrapper);

        // Add to preview animation
        preview.addImage(imageResource);

        // Add to our internal list
        imageFiles.add(imageFile);
        if (imageFiles.size() > 1) {
            webcamAndGif.replaceComponent(gifImage, preview);
            downloadButton.setEnabled(true);
        }
    }

    private File createGif() {
        if (imageFiles.size() > 1) {
            try {
                File gifImageFile = GifUtil.writeAnimatedGif(frameDelayMs,
                        imageFiles.toArray(new File[imageFiles.size()]));
                return gifImageFile;
            } catch (IOException e) {
                handleException(e);
            }
        }
        return null;
    }

    private void handleException(Exception e) {
        Notification.show(
                "Whoops, something went wrong. Please try again later.",
                Type.ERROR_MESSAGE);
        Logger.getLogger(getClass().getSimpleName()).log(Level.WARNING,
                e.getMessage(), e);
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
