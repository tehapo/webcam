package org.vaadin.teemu.webcam.demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.madgag.gif.fmsware.AnimatedGifEncoder;

public class GifUtil {

    public static File writeAnimatedGif(File... images) throws IOException {
        File targetFile = File.createTempFile(UUID.randomUUID().toString(),
                ".gif");
        FileOutputStream output = new FileOutputStream(targetFile);

        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.setRepeat(0);
        encoder.setDelay(200);
        encoder.start(output);

        for (File imageFile : images) {
            BufferedImage bufImg = ImageIO.read(imageFile);
            encoder.addFrame(bufImg);
        }
        encoder.finish();
        return targetFile;
    }

}
