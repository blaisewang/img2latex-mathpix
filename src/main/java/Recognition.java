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


public class Recognition implements Callable<OCRRequest.Response> {

    private static JsonObject parameters = new JsonObject();

    Recognition() {

        parameters.addProperty("src", "");

        JsonArray ocrParameters = new JsonArray();
        ocrParameters.add("math");
        ocrParameters.add("text");
        parameters.add("ocr", ocrParameters);

        parameters.addProperty("skip_recrop", true);

        JsonArray formatParameters = new JsonArray();
        formatParameters.add("text");
        formatParameters.add("text_display");
        formatParameters.add("latex_styled");
        parameters.add("formats", formatParameters);

        JsonArray transformParameters = new JsonArray();
        transformParameters.add("rm_spaces");
        transformParameters.add("long_frac");

        JsonArray displayMathDelimiterParameters = new JsonArray();
        displayMathDelimiterParameters.add("\n$$\n ");
        displayMathDelimiterParameters.add(" \n$$\n");

        JsonArray mathDelimiterParameters = new JsonArray();
        mathDelimiterParameters.add("\\(");
        mathDelimiterParameters.add("\\)");

        JsonObject textFormatOptions = new JsonObject();
        textFormatOptions.add("math_delims", mathDelimiterParameters);
        textFormatOptions.add("transforms", transformParameters);

        JsonObject textDisplayFormatOptions = new JsonObject();
        textDisplayFormatOptions.add("displaymath_delims", displayMathDelimiterParameters);
        textDisplayFormatOptions.add("transforms", transformParameters);

        JsonObject transformOptions = new JsonObject();
        transformOptions.add("transforms", transformParameters);

        JsonObject formatOptions = new JsonObject();
        formatOptions.add("text", textFormatOptions);
        formatOptions.add("text_display", textDisplayFormatOptions);
        formatOptions.add("latex_styled", transformOptions);

        parameters.add("format_options", formatOptions);

    }

    Boolean setSrcParameters(Image image) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        try {

            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

            byte[] imageInByte = byteArrayOutputStream.toByteArray();

            String src = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(imageInByte);

            parameters.addProperty("src", src);

            return true;

        } catch (IOException e) {

            return false;

        }

    }

    @Override
    public OCRRequest.Response call() throws IOException {

        return OCRRequest.getResult(parameters);

    }

}
