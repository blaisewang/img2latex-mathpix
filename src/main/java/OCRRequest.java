import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;


/**
 * OCRRequest.java
 * handles the OCR request with HTTP post.
 * Parsing the result as a OCRRequest.Response object.
 */
class OCRRequest {

    // get app_id and app_key from environment variables
    private static final String APP_ID = System.getenv("MATHPIX_APP_ID");
    private static final String APP_KEY = System.getenv("MATHPIX_APP_KEY");

    /**
     * Send the request with Json parameters to Mathpix API.
     * Parsing the result as a OCRRequest.Response object.
     *
     * @param parameters JsonObject to send as the request parameters.
     * @return a OCRRequest.Response object.
     * @throws IOException if errors happened during the request execution.
     */
    static Response getResult(JsonObject parameters) throws IOException {

        // maximum connection waiting time 10 seconds
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).build();

        // build the HTTP client with above config
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

        // set the request parameters
        StringEntity requestParameters = new StringEntity(parameters.toString());

        // API url
        HttpPost request = new HttpPost("https://api.mathpix.com/v3/latex");

        // with app_id, app_key, and json type content as the post header
        request.addHeader("app_id", APP_ID);
        request.addHeader("app_key", APP_KEY);
        request.addHeader("Content-type", "application/json");
        request.setEntity(requestParameters);

        // get the raw result from the execution
        HttpResponse result = httpClient.execute(request);
        // obtain the message entity of this response
        String json = EntityUtils.toString(result.getEntity(), "UTF-8");

        // return the parsed result
        return new Gson().fromJson(json, Response.class);

    }

    /**
     * Response class
     * used by Gson to deserialize the JSON string to the object.
     */
    static class Response {

        private String error;
        private String text;
        private String text_display;
        private String latex_styled;
        private double latex_confidence;
        private ArrayList<String> detection_list;

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

        public void setError(String error) {
            this.error = error;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setText_display(String text_display) {
            this.text_display = text_display;
        }

        public void setLatex_styled(String latex_styled) {
            this.latex_styled = latex_styled;
        }

        public void setLatex_confidence(double latex_confidence) {
            this.latex_confidence = latex_confidence;
        }

        public void setDetection_list(ArrayList<String> detection_list) {
            this.detection_list = detection_list;
        }

    }

}
