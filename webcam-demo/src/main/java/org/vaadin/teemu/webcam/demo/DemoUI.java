package org.vaadin.teemu.webcam.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.vaadin.teemu.webcam.Webcam;

import com.vaadin.annotations.Title;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

//@Theme("demo")
@Title("Webcam Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    private VerticalLayout layout;
    private CssLayout images;

    @Override
    protected void init(VaadinRequest request) {

        // Initialize our new UI component
        final Webcam webcam = new Webcam();
        webcam.setWidth("600px");
        webcam.setReceiver(new Upload.Receiver() {

            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                try {
                    File targetFile = File.createTempFile(filename, ".jpeg");
                    targetFile.deleteOnExit();
                    addImage(targetFile);
                    return new FileOutputStream(targetFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        // Show it in the middle of the screen
        layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.addComponent(new Button("Capture!", new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                webcam.capture();
            }
        }));
        layout.addComponent(webcam);
        layout.setComponentAlignment(webcam, Alignment.MIDDLE_CENTER);
        layout.addComponent(images = new CssLayout());
        images.setWidth("600px");
        layout.setComponentAlignment(images, Alignment.BOTTOM_CENTER);
        setContent(layout);
    }

    private void addImage(File imageFile) {
        Image image = new Image(null, new FileResource(imageFile));
        image.setWidth("50px");
        images.addComponent(image);
    }
}
