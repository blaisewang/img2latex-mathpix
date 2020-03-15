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
import java.util.concurrent.CompletableFuture;
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

        String app_id;
        String app_key;

        APICredentialConfig APICredentialConfig = IOUtils.getAPICredentialConfig();

        if (APICredentialConfig.isValid()) {
            app_id = APICredentialConfig.getAppId();
            app_key = APICredentialConfig.getAppKey();
        } else {
            // early return
            return new Response(IOUtils.INVALID_CREDENTIALS_ERROR);
        }

        // default protocols: [TLSv1.3, TLSv1.2]
        HttpClient httpClient;

        // HTTP version 2 first, then HTTP version 1.1
        if (IOUtils.getProxyEnableOption()) {
            // proxy enabled
            ProxyConfig config = IOUtils.getProxyConfig();
            if (config.isValid()) {
                httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).
                        proxy(ProxySelector.of(new InetSocketAddress(config.getHostname(), config.getPort()))).build();
            } else {
                return new Response(IOUtils.INVALID_PROXY_CONFIG_ERROR);
            }
        } else {
            httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        }

        // URI
        String uri = parameters.has("skip_recrop") ? IOUtils.LEGACY_API_URL : IOUtils.TEXT_API_URL;
        // request body
        HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(parameters.toString());
        // wait up to 15 seconds
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(uri)).
                headers("app_id", app_id, "app_key", app_key, "Content-type", "application/json").
                POST(requestBody).timeout(Duration.ofSeconds(15)).build();

        CompletableFuture<String> future = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString()).
                thenApply(HttpResponse::body);

        try {
            return new Gson().fromJson(future.get(), Response.class);
        } catch (InterruptedException | ExecutionException e) {
            return new Response(e.getMessage());
        }

    }

}
