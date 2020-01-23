package io;

import com.google.gson.JsonArray;
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
 * It initialises a common JsonObject used for request with unchanged parameters.
 * Set the "src" value with base64 encoded clipboard image before each request.
 */
public class Recognition implements Callable<Response> {

    private static JsonObject parameters = new JsonObject();

    /**
     * Original parameter explanation: https://docs.mathpix.com/
     * Initialisation of the IO.Recognition class and a JsonObject with unchanged parameters.
     */
    public Recognition() {

        // "src" initialises with empty string
        parameters.addProperty("src", "");

        // process both math and text OCR
        JsonArray ocrParameters = new JsonArray();
        ocrParameters.add("math");
        ocrParameters.add("text");
        parameters.add("ocr", ocrParameters);

        // force algorithm to consider whole image
        parameters.addProperty("skip_recrop", true);

        // string postprocessing formats
        JsonArray formatParameters = new JsonArray();
        // inline math by default
        formatParameters.add("text");
        // block mode math when in doubt
        formatParameters.add("text_display");
        // modified output to improve the visual appearance
        formatParameters.add("latex_styled");
        parameters.add("formats", formatParameters);

        // transforms
        JsonArray transformParameters = new JsonArray();
        // omit spaces around LaTeX groups and other places where spaces are superfluous
        transformParameters.add("rm_spaces");
        // convert longdiv to frac as longdiv is not a non-valid LaTeX markup
        transformParameters.add("long_frac");

        // delimiters for math mode
        JsonArray mathDelimiterParameters = new JsonArray();
        mathDelimiterParameters.add("\\(");
        mathDelimiterParameters.add("\\)");

        // parameters used for math mode (text)
        JsonObject textFormatOptions = new JsonObject();
        textFormatOptions.add("math_delims", mathDelimiterParameters);
        textFormatOptions.add("transforms", transformParameters);

        // delimiters for displaymath mode
        JsonArray displayMathDelimiterParameters = new JsonArray();
        displayMathDelimiterParameters.add("\n$$\n ");
        displayMathDelimiterParameters.add(" \n$$\n");

        // parameters used for displaymath mode (text_display)
        JsonObject textDisplayFormatOptions = new JsonObject();
        textDisplayFormatOptions.add("displaymath_delims", displayMathDelimiterParameters);
        textDisplayFormatOptions.add("transforms", transformParameters);

        // parameters used for styled LaTeX (latex_styled)
        JsonObject transformOptions = new JsonObject();
        transformOptions.add("transforms", transformParameters);

        // format options combined
        JsonObject formatOptions = new JsonObject();
        formatOptions.add("text", textFormatOptions);
        formatOptions.add("text_display", textDisplayFormatOptions);
        formatOptions.add("latex_styled", transformOptions);

        parameters.add("format_options", formatOptions);

        // privacy option for not helping to improve
        JsonObject privacyOption = new JsonObject();
        privacyOption.addProperty("improve_mathpix", false);

        parameters.add("metadata", privacyOption);

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
