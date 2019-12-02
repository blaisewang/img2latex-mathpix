import java.util.ArrayList;

/**
 * Response.java
 * used by Gson to deserialize the JSON string to the object.
 */
class Response {

    private String error;
    private String text;
    private String text_display;
    private String latex_styled;
    private double latex_confidence;
    private ArrayList<String> detection_list;

    Response(String error, String text, String text_display, String latex_styled, double latex_confidence, ArrayList<String> detection_list) {

        this.error = error;
        this.text = text;
        this.text_display = text_display;
        this.latex_styled = latex_styled;
        this.latex_confidence = latex_confidence;
        this.detection_list = detection_list;

    }

    /**
     * @return error message if any.
     */
    String getError() {
        return error;
    }

    /**
     * @return text format result.
     */
    String getText() {
        return text;
    }

    /**
     * @return text_display format result.
     */
    String getText_display() {
        return text_display;
    }

    /**
     * @return LaTeX format result.
     */
    String getLatex_styled() {
        return latex_styled;
    }

    /**
     * @return confidence of the correctness.
     */
    Double getLatex_confidence() {
        return latex_confidence;
    }

    /**
     * @return if no equation in the image.
     */
    Boolean is_not_math() {
        return detection_list.contains("is_not_math");
    }

}