import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

class OCRRequest {

    static Response getResult(JsonObject parameters, String appID, String appKey) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringEntity requestParameters = new StringEntity(parameters.toString());

        HttpPost request = new HttpPost("https://api.mathpix.com/v3/latex");
        request.addHeader("app_id", appID);
        request.addHeader("app_key", appKey);
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

        String getText() {
            return text;
        }

        String getText_display() {
            return text_display;
        }


        String getLatex_styled() {
            return latex_styled;
        }

        double getLatex_confidence() {
            return latex_confidence;
        }

    }

}
