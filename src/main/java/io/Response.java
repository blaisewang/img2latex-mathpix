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
    private String mathML;
    private ArrayList<JsonObject> data;
    private String latex_styled;
    private double confidence;
    private double latex_confidence;

    public Response(String error) {
        this.error = error;
    }

    public Response(String error, String text, ArrayList<JsonObject> data, String latex_styled, double confidence, double latex_confidence) {
        this.error = error;
        this.text = text;
        this.data = data;
        this.latex_styled = latex_styled;
        this.confidence = confidence;
        this.latex_confidence = latex_confidence;
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
     * @param text text to be set.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return MathML result from data;
     */
    public String getDataAsMathML() {

        if (data == null) {
            return "";
        }

        var result = new StringBuilder();

        for (JsonObject jsonObject : data) {
            result.append(jsonObject.get("value").getAsString()).append("\n\n");
        }

        if (result.length() > 1) {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();

    }

    /**
     * @return MathML result;
     */
    public String getMathML() {
        return mathML;
    }

    /**
     * @param text text to be set.
     */
    public void setMathML(String text) {
        mathML = text;
    }

    /**
     * @return LaTeX format result.
     */
    public String getLatexStyled() {
        return latex_styled;
    }

    /**
     * @return confidence of the latex correctness.
     */
    public Double getLatexConfidence() {
        return latex_confidence;
    }

    /**
     * @param latex_confidence latex confidence to be set.
     */
    public void setLatexConfidence(double latex_confidence) {
        this.latex_confidence = latex_confidence;
    }

    /**
     * @return confidence of the correctness.
     */
    public double getConfidence() {
        return confidence;
    }

}
