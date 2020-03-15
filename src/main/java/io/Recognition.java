package io;

import com.google.gson.JsonObject;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.Callable;

/**
 * IO.Recognition.java
 * implements Callable for concurrent call.
 * Set the "src" value with base64 encoded clipboard image before each request.
 */
public class Recognition implements Callable<Response> {

    protected JsonObject parameters = new JsonObject();

    /**
     * Convert the image to byte[] and encode the image with base64.
     * Replace the "src" value in the pre-initialised parameters JsonObject.
     *
     * @param image Image to be recognised.
     * @return whether the parameter setting is successful.
     */
    public Boolean setSrcParameters(Image image) {

        // output stream in byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // convert javafx.scene.image.Image to java.awt.image.BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        try {
            // BufferedImage to byte array output stream
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            // byte array output stream to byte[]
            byte[] imageInByte = byteArrayOutputStream.toByteArray();
            // add header of "src"
            String src = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(imageInByte);
            // replace original "src" with new encoded value
            parameters.addProperty("src", src);
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    /**
     * Override the supertype method.
     *
     * @return OCR request result.
     */
    @Override
    public Response call() {
        return OCRRequestHelper.getResult(parameters);
    }

}
