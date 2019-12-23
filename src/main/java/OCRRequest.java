import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
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

        // workaround to resolve #26
        SSLContext context = SSLContexts.createSystemDefault();
        SSLConnectionSocketFactory fac = new SSLConnectionSocketFactory(context, new String[]{"TLSv1"}, null, NoopHostnameVerifier.INSTANCE);

        // maximum connection waiting time 20 seconds
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(20000).build();

        // build the HTTP client with above config
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setSSLSocketFactory(fac).build();

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
