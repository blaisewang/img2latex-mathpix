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
        var formatsParameters = new JsonArray();
        formatsParameters.add("text");
        formatsParameters.add("data");

        parameters.add("formats", formatsParameters);

        // data options for including mathml result
        var dataOptions = new JsonObject();
        dataOptions.addProperty("include_mathml", true);

        parameters.add("data_options", dataOptions);

        // metadata option for not helping to improve
        var metadataOption = new JsonObject();
        metadataOption.addProperty("improve_mathpix", false);

        parameters.add("metadata", metadataOption);

    }

}
