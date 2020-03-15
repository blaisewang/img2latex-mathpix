package io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * IO.TextRecognition.java
 * It initialises a common JsonObject used for request with unchanged parameters.
 */
public class TextRecognition extends Recognition {

    /**
     * Original parameter explanation: https://docs.mathpix.com/
     * Initialisation of the IO.TextRecognition class and a JsonObject with unchanged parameters.
     */
    public TextRecognition() {

        // "src" initialises with empty string
        parameters.addProperty("src", "");

        // formats
        JsonArray formatsParameters = new JsonArray();
        formatsParameters.add("text");

        parameters.add("formats", formatsParameters);

        // privacy option for not helping to improve
        JsonObject privacyOption = new JsonObject();
        privacyOption.addProperty("improve_mathpix", false);

        parameters.add("metadata", privacyOption);

    }

}
