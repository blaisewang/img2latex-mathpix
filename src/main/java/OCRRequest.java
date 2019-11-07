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


/**
 * OCRRequest.java
 * handles the OCR request with HTTP post.
 * Parsing the result as a Response object.
 */
class OCRRequest {

    /**
     * Send the request with Json parameters to Mathpix API.
     * Parsing the result as a Response object.
     *
     * @param parameters JsonObject to send as the request parameters.
     * @return a Response object.
     * @throws IOException if errors happened during the request execution.
     */
    static Response getResult(JsonObject parameters) throws IOException {

        String app_id;
        String app_key;

        AppConfig appConfig = Utilities.readConfigFile();

        if (appConfig != null) {
            app_id = appConfig.getApp_id();
            app_key = appConfig.getApp_key();
        } else {
            Utilities.showErrorDialog("Illegal App ID or App Key");
            // early return
            return null;
        }

        // maximum connection waiting time 10 seconds
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10000).build();

        // build the HTTP client with above config
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

        // set the request parameters
        StringEntity requestParameters = new StringEntity(parameters.toString());

        // API url
        HttpPost request = new HttpPost("https://api.mathpix.com/v3/latex");

        // with app_id, app_key, and json type content as the post header
        request.addHeader("app_id", app_id);
        request.addHeader("app_key", app_key);
        request.addHeader("Content-type", "application/json");
        request.setEntity(requestParameters);

        // get the raw result from the execution
        HttpResponse result = httpClient.execute(request);
        // obtain the message entity of this response
        String json = EntityUtils.toString(result.getEntity(), "UTF-8");

        // return the parsed result
        return new Gson().fromJson(json, Response.class);

    }

}
