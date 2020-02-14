package io;


/**
 * IO.Response.java
 * used by Gson to deserialize the JSON string to the object.
 */
public class Response {

    private String error;
    private String text;
    private String latex_styled;
    private double latex_confidence;

    public Response(String error) {

        this.error = error;

    }

    public Response(String error, String text, String latex_styled, double latex_confidence) {

        this.error = error;
        this.text = text;
        this.latex_styled = latex_styled;
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
     * @return LaTeX format result.
     */
    public String getLatexStyled() {
        return latex_styled;
    }

    /**
     * @return confidence of the correctness.
     */
    public Double getLatexConfidence() {
        return latex_confidence;
    }

}