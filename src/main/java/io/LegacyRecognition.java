package io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * IO.LegacyRecognition.java
 * It initialises a common JsonObject used for request with unchanged parameters.
 */
public class LegacyRecognition extends Recognition {

    /**
     * Original parameter explanation: https://docs.mathpix.com/
     * Initialisation of the IO.LegacyRecognition class and a JsonObject with unchanged parameters.
     */
    public LegacyRecognition() {

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

        // parameters used for styled LaTeX (latex_styled)
        JsonObject transformOptions = new JsonObject();
        transformOptions.add("transforms", transformParameters);

        // format options combined
        JsonObject formatOptions = new JsonObject();
        formatOptions.add("text", textFormatOptions);
        formatOptions.add("latex_styled", transformOptions);

        parameters.add("format_options", formatOptions);

        // privacy option for not helping to improve
        JsonObject privacyOption = new JsonObject();
        privacyOption.addProperty("improve_mathpix", false);

        parameters.add("metadata", privacyOption);

    }

}
