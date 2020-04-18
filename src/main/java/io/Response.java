package io;

import com.google.gson.JsonObject;

import java.util.ArrayList;


/**
 * IO.Response.java
 * used by Gson to deserialize the JSON string to the object.
 */
public class Response {

    private final String error;

    private String text;
    private double confidence;
    private ArrayList<JsonObject> data;

    public Response(String error) {
        this.error = error;
    }

    public Response(String error, String text, double confidence, ArrayList<JsonObject> data) {
        this.error = error;
        this.text = text;
        this.data = data;
        this.confidence = confidence;
    }

    /**
     * @return error message if any.
     */
    public String getError() {
        return error;
    }

    /**
     * @return text format result.
     */
    public String getText() {
        return text;
    }

    /**
     * @return confidence of the correctness.
     */
    public double getConfidence() {
        return confidence;
    }

    /**
     * @return MathML result from data;
     */
    private String getTypeFromData(String type) {

        if (data == null) {
            return "";
        }

        var result = new StringBuilder();

        for (JsonObject jsonObject : data) {
            if (type.equals(jsonObject.get("type").getAsString())) {
                result.append(jsonObject.get("value").getAsString()).append("\n\n");
            }
        }

        if (result.length() > 1) {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();

    }

    /**
     * @return TSV result.
     */
    public String getTSV() {
        return getTypeFromData("tsv");
    }

    /**
     * @return MathML result;
     */
    public String getMathML() {
        return getTypeFromData("mathml");
    }

}
