package io;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.net.ssl.SSLContext;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;


/**
 * IO.OCRRequestHelper.java
 * handles the OCR request with HTTP post.
 * Parsing the result as a IO.Response object.
 */
public class OCRRequestHelper {

    /**
     * Send the request with Json parameters to Mathpix API.
     * Parsing the result as a IO.Response object.
     *
     * @param parameters JsonObject to send as the request parameters.
     * @return a IO.Response object.
     */
    public static Response getResult(JsonObject parameters) throws NoSuchAlgorithmException {

        String appId;
        String appKey;

        var APICredentialConfig = PreferenceHelper.getAPICredentialConfig();

        if (APICredentialConfig.isValid()) {
            appId = APICredentialConfig.getAppId();
            appKey = APICredentialConfig.getAppKey();
        } else {
            // early return
            return new Response(IOUtils.INVALID_CREDENTIALS_ERROR);
        }

        // TLSv1.2 only
        var sslParameters = SSLContext.getDefault().getDefaultSSLParameters();
        sslParameters.setProtocols(new String[]{"TLSv1.2"});
        var httpClientBuilder = HttpClient.newBuilder().sslParameters(sslParameters);

        if (PreferenceHelper.getProxyEnableOption()) {
            // proxy enabled
            var proxyConfig = PreferenceHelper.getProxyConfig();
            if (proxyConfig.isValid()) {
                var inetSocketAddress = new InetSocketAddress(proxyConfig.getHostname(), proxyConfig.getPort());
                httpClientBuilder.proxy(ProxySelector.of(inetSocketAddress));
            } else {
                return new Response(IOUtils.INVALID_PROXY_CONFIG_ERROR);
            }
        }

        var httpClient = httpClientBuilder.build();

        // request body
        var requestBody = HttpRequest.BodyPublishers.ofString(parameters.toString());
        // wait up to 30 seconds
        var httpRequest = HttpRequest.newBuilder().uri(URI.create(IOUtils.API_URL)).
                headers("app_id", appId, "app_key", appKey, "Content-type", "application/json").
                POST(requestBody).timeout(Duration.ofSeconds(30)).build();

        var completableFuture = httpClient.
                sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body);

        try {
            return new Gson().fromJson(completableFuture.get(), Response.class);
        } catch (RuntimeException | InterruptedException | ExecutionException e) {
            return new Response(e.getMessage());
        }

    }

}
