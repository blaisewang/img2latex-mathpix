package io;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
    public static Response getResult(JsonObject parameters) {

        String appId;
        String appKey;

        var APICredentialConfig = IOUtils.getAPICredentialConfig();

        if (APICredentialConfig.isValid()) {
            appId = APICredentialConfig.getAppId();
            appKey = APICredentialConfig.getAppKey();
        } else {
            // early return
            return new Response(IOUtils.INVALID_CREDENTIALS_ERROR);
        }

        // default protocols: [TLSv1.3, TLSv1.2]
        HttpClient httpClient;

        // HTTP version 2 first, then HTTP version 1.1
        if (IOUtils.getProxyEnableOption()) {
            // proxy enabled
            var config = IOUtils.getProxyConfig();
            if (config.isValid()) {
                httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).
                        proxy(ProxySelector.of(new InetSocketAddress(config.getHostname(), config.getPort()))).build();
            } else {
                return new Response(IOUtils.INVALID_PROXY_CONFIG_ERROR);
            }
        } else {
            httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        }

        // request body
        var requestBody = HttpRequest.BodyPublishers.ofString(parameters.toString());
        // wait up to 15 seconds
        var httpRequest = HttpRequest.newBuilder().uri(URI.create(IOUtils.API_URL)).
                headers("app_id", appId, "app_key", appKey, "Content-type", "application/json").
                POST(requestBody).timeout(Duration.ofSeconds(15)).build();

        var completableFuture = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body);

        try {
            return new Gson().fromJson(completableFuture.get(), Response.class);
        } catch (InterruptedException | ExecutionException e) {
            return new Response(e.getMessage());
        }

    }

}
