package io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.Callable;


/**
 * IO.Recognition.java
 * implements Callable for concurrent call.
 * Set the "src" value with base64 encoded clipboard image before each request.
 */
public class Recognition implements Callable<Response> {

    private final JsonObject parameters = new JsonObject();

    /**
     * Original parameter explanation: https://docs.mathpix.com/
     * Initialisation of the IO.TextRecognition class and a JsonObject with unchanged parameters.
     */
    public Recognition() {

        // "src" initialises with empty string
        parameters.addProperty("src", "");

        // formats
        var formatsParameters = new JsonArray();
        formatsParameters.add("text");
        formatsParameters.add("data");

        parameters.add("formats", formatsParameters);

        // data options for including mathml result
        var dataOptions = new JsonObject();
        dataOptions.addProperty("include_tsv", true);
        dataOptions.addProperty("include_mathml", true);

        parameters.add("data_options", dataOptions);

        // metadata option for not helping to improve
        var metadataOption = new JsonObject();
        metadataOption.addProperty("improve_mathpix", true);

        parameters.add("metadata", metadataOption);

    }

    /**
     * Convert the image to byte[] and encode the image with base64.
     * Replace the "src" value in the pre-initialised parameters JsonObject.
     *
     * @param image Image to be recognised.
     * @return whether the parameter setting is successful.
     */
    public Boolean setSrcParameters(Image image) {

        // output stream in byte array
        var byteArrayOutputStream = new ByteArrayOutputStream();

        // convert javafx.scene.image.Image to java.awt.image.BufferedImage
        var bufferedImage = SwingFXUtils.fromFXImage(image, null);

        try {
            // BufferedImage to byte array output stream
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            // byte array output stream to byte[]
            var imageInByte = byteArrayOutputStream.toByteArray();
            // add header of "src"
            var src = "data:image/png;base64," + Base64.getEncoder().encodeToString(imageInByte);
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
    public Response call() throws NoSuchAlgorithmException {
        return OCRRequestHelper.getResult(parameters);
    }

}
