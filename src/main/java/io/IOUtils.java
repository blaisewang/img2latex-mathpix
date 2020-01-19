package io;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.scene.image.Image;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * IO.IOUtils.java
 * Contains common IO methods.
 */
public class IOUtils {

    private static Path configFilePath = Paths.get("./config");

    public final static String[] SUPPORTED_PROTOCOLS = new String[]{"TLSv1.2"};

    // IO.Recognition object initialisation
    private static Recognition recognition = new Recognition();

    /**
     * Execute the OCR request in Java concurrent way.
     *
     * @param image image to be recognised.
     * @return recognised result.
     */
    public static Response concurrentCall(Image image) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        if (recognition.setSrcParameters(image)) {
            Future<Response> result = executor.submit(recognition);
            try {
                return result.get();
            } catch (InterruptedException | ExecutionException e) {
                return null;
            }
        }

        return null;

    }

    /**
     * Get latest release version via GitHub API.
     *
     * @return latest released version.
     */
    public static String getLatestVersion() {

        // workaround to resolve #26
        SSLContext context = SSLContexts.createSystemDefault();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(context, SUPPORTED_PROTOCOLS, null, NoopHostnameVerifier.INSTANCE);

        // maximum connection waiting time 1 seconds
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).build();

        // build the HTTP client with above config
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setSSLSocketFactory(sslConnectionSocketFactory).build();

        // API url
        HttpGet request = new HttpGet("https://api.github.com/repos/blaisewang/img2latex-mathpix/releases/latest");

        try {

            // get the raw result from the execution
            HttpResponse result = httpClient.execute(request);
            // obtain the message entity of this response
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            // parse json string to Json object
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            // close and release resources
            httpClient.close();

            String[] tag = jsonObject.get("tag_name").getAsString().split("v");
            return tag[tag.length - 1];

        } catch (IOException ignored) {

            return null;

        }

    }

    /**
     * Set config file path.
     */
    public static void setConfigFilePath() {
        configFilePath = Paths.get(System.getProperty("user.home") + "/Library/Image2LaTeX/config");
    }

    /**
     * @return if the config file exists.
     */
    public static Boolean isConfigExists() {
        return Files.exists(configFilePath);
    }

    /**
     * Create a standard config file.
     *
     * @param appID  APP ID to be written.
     * @param appKey APP key to be written.
     */
    public static void createConfigFile(String appID, String appKey) {

        String text = appID + System.lineSeparator() + appKey;

        try {
            // create one if not exists
            if (!isConfigExists()) {
                Files.createDirectories(configFilePath.getParent());
                Files.createFile(configFilePath);
            }
            Files.write(configFilePath, text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    /**
     * Read app_id and app_key config from ./config file.
     *
     * @return IO.AppConfig object.
     */
    public static AppConfig readConfigFile() {

        try {
            // read config file
            List<String> configs = Files.readAllLines(configFilePath);
            return new AppConfig(configs.get(0), configs.get(1));
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            return null;
        }

    }

}
