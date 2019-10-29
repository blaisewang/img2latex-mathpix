import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageProcessing {

    static OCRRequest.Response queryResult(BufferedImage image, String appID, String appKey) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ImageIO.write(image, "png", byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();

        JsonObject parameters = new JsonObject();

        String src = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(imageInByte);
        parameters.addProperty("src", src);

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
        displayMathDelimiterParameters.add("\n$$\n");
        displayMathDelimiterParameters.add("\n$$\n");

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

        return OCRRequest.getResult(parameters, appID, appKey);

    }

    public static void main(String[] args) throws IOException, UnsupportedFlavorException {

        String appID = System.getenv("APP_ID");
        String appKey = System.getenv("APP_KEY");

        BufferedImage lastImage = Util.getImageFromClipboard();

        if (lastImage != null) {

            OCRRequest.Response response = queryResult(lastImage, appID, appKey);

            System.out.println(response.getLatex_styled() + "\n");
            System.out.println(response.getText() + "\n");
            System.out.println(response.getText_display() + "\n");
            System.out.println(Util.replaceDoubleDollarWithWrapper(response.getText_display()) + "\n");

            System.out.println("Confidence: " + response.getLatex_confidence());

        }

    }

}
