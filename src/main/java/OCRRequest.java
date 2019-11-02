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


class OCRRequest {

    private static final String APP_ID = System.getenv("APP_ID");
    private static final String APP_KEY = System.getenv("APP_KEY");

    static Response getResult(JsonObject parameters) throws IOException {

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).build();

        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

        StringEntity requestParameters = new StringEntity(parameters.toString());

        HttpPost request = new HttpPost("https://api.mathpix.com/v3/latex");
        request.addHeader("app_id", APP_ID);
        request.addHeader("app_key", APP_KEY);
        request.addHeader("Content-type", "application/json");
        request.setEntity(requestParameters);

        HttpResponse result = httpClient.execute(request);
        String json = EntityUtils.toString(result.getEntity(), "UTF-8");

        return new Gson().fromJson(json, Response.class);

    }

    static class Response {

        private String text;
        private String text_display;
        private String latex_styled;
        private double latex_confidence;
        private ArrayList<String> detection_list;

        String getText() {
            return text;
        }

        String getText_display() {
            return text_display;
        }


        String getLatex_styled() {
            return latex_styled;
        }

        Double getLatex_confidence() {
            return latex_confidence;
        }

        Boolean is_not_math() {
            return detection_list.contains("is_not_math");
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
