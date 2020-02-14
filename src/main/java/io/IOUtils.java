package io;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.scene.image.Image;
import org.apache.http.HttpHost;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;


/**
 * IO.IOUtils.java
 * Contains common IO methods.
 */
public class IOUtils {


    public final static String UNKNOWN_HOST_ERROR = "Unknown host";
    public final static String CONNECTION_REFUSED_ERROR = "Connection refused";
    public final static String INVALID_CREDENTIALS_ERROR = "Invalid credentials";
    public final static String INVALID_PROXY_CONFIG_ERROR = "Invalid proxy config";
    public final static String SSL_PEER_SHUT_DOWN_INCORRECTLY_ERROR = "SSL peer shut down incorrectly";

    public final static String OCR_API_URL = "https://api.mathpix.com/v3/latex";
    public final static String I2L_GITHUB_RELEASES_URL = "https://github.com/blaisewang/img2latex-mathpix/releases";

    private final static String I2L_LATEST_RELEASE_API_URL = "https://api.github.com/repos/blaisewang/img2latex-mathpix/releases/latest";

    private final static String I2L_APP_ID = "I2L_APP_ID";
    private final static String I2L_APP_KEY = "I2L_APP_KEY";
    private final static String I2L_THIRD_RESULT_WRAPPER = "I2L_THIRD_RESULT_WRAPPER";
    private final static String I2L_FOURTH_RESULT_WRAPPER = "I2L_FOURTH_RESULT_WRAPPER";
    private final static String I2L_PROXY_ENABLED = "I2L_PROXY_ENABLED";
    private final static String I2L_PROXY_HOSTNAME = "I2L_PROXY_HOSTNAME";
    private final static String I2L_PROXY_PORT = "I2L_PROXY_PORT";

    private final static String CONFIG_NODE_PATH = "I2L_API_CREDENTIAL_CONFIG";
    private static Preferences preferences = Preferences.userRoot().node(CONFIG_NODE_PATH);

    public final static String[] SUPPORTED_PROTOCOLS = new String[]{"TLSv1.2"};

    // IO.Recognition object initialisation
    private static Recognition recognition = new Recognition();

    /**
     * Original source: https://stackoverflow.com/a/33477375/4658633
     *
     * @return whether macOS enabled dark mode.
     */
    public static boolean isMacDarkMode() {

        try {
            // process will exit with 0 if dark mode enabled
            final Process process = Runtime.getRuntime().exec(new String[]{"defaults", "read", "-g", "AppleInterfaceStyle"});
            process.waitFor(100, TimeUnit.MILLISECONDS);
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException | IllegalThreadStateException e) {
            return false;
        }

    }

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

        // maximum connection waiting time 1 second
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).build();

        if (IOUtils.getProxyEnabled()) {
            // proxy enabled
            ProxyConfig proxyConfig = IOUtils.getProxyConfig();
            if (proxyConfig.isValid()) {
                HttpHost proxy = new HttpHost(proxyConfig.getHostname(), proxyConfig.getPort());
                // maximum connection waiting time 1 second
                requestConfig = RequestConfig.custom().setConnectTimeout(1000).setProxy(proxy).build();
            }
        }

        // build the HTTP client with above config
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setSSLSocketFactory(sslConnectionSocketFactory).build();

        // API url
        HttpGet request = new HttpGet(I2L_LATEST_RELEASE_API_URL);

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
     * @return whether the API credential config is valid.
     */
    public static boolean isAPICredentialConfigValid() {
        return getAPICredentialConfig().isValid();
    }

    /**
     * Set app ID.
     *
     * @param appId App ID to be written.
     */
    public static void setAppId(String appId) {
        preferences.put(I2L_APP_ID, appId);
    }

    /**
     * Set app key.
     *
     * @param appKey App key to be written.
     */
    public static void setAppKey(String appKey) {
        preferences.put(I2L_APP_KEY, appKey);
    }

    /**
     * Get App ID and App Key from Java Preferences API.
     *
     * @return IO.APICredentialConfig object.
     */
    public static APICredentialConfig getAPICredentialConfig() {
        return new APICredentialConfig(preferences.get(I2L_APP_ID, ""), preferences.get(I2L_APP_KEY, ""));
    }

    /**
     * Set third result wrapper option.
     *
     * @param option option to be written.
     */
    public static void setThirdResultWrapper(int option) {
        preferences.putInt(I2L_THIRD_RESULT_WRAPPER, option);
    }

    /**
     * Get third result wrapper option.
     *
     * @return third result wrapper option.
     */
    public static int getThirdResultWrapper() {
        return preferences.getInt(I2L_THIRD_RESULT_WRAPPER, 2);
    }

    /**
     * Set fourth result wrapper option.
     *
     * @param option option to be written.
     */
    public static void setFourthResultWrapper(int option) {
        preferences.putInt(I2L_FOURTH_RESULT_WRAPPER, option);
    }

    /**
     * Get fourth result wrapper option.
     *
     * @return fourth result wrapper option.
     */
    public static int getFourthResultWrapper() {
        return preferences.getInt(I2L_FOURTH_RESULT_WRAPPER, 0);
    }

    /**
     * Wrap the original recognised result with the selected formatting options.
     *
     * @param result recognised result.
     * @return the wrapped result.
     */
    public static String thirdResultWrapper(String result) {

        // return null if the original result is null
        if (result == null) {
            return null;
        }

        int option = getThirdResultWrapper();

        switch (option) {
            case 0:
                return "\\begin{equation*}\n " + result + " \n\\end{equation*}";
            case 1:
                return "\\begin{align*}\n " + result + " \n\\end{align*}";
            case 3:
                return "\\[\n " + result + " \n\\]";
            default:
                // default for option 2 and others
                return "$$\n " + result + " \n$$";
        }

    }

    /**
     * Wrap the original recognised result with the selected formatting options.
     *
     * @param result recognised result.
     * @return the wrapped result.
     */
    public static String fourthResultWrapper(String result) {

        // return null if the original result is null
        if (result == null) {
            return null;
        }

        int option = getFourthResultWrapper();

        if (option == 1) {
            return "\\begin{align}\n " + result + " \n\\end{align}";
        }

        return "\\begin{equation}\n " + result + " \n\\end{equation}";

    }

    /**
     * Set using proxy or not.
     *
     * @param option option to be written.
     */
    public static void setProxyEnabled(boolean option) {
        preferences.putBoolean(I2L_PROXY_ENABLED, option);
    }

    /**
     * Get using proxy or not.
     *
     * @return proxy enabled option.
     */
    public static boolean getProxyEnabled() {
        return preferences.getBoolean(I2L_PROXY_ENABLED, false);
    }

    /**
     * Set proxy host.
     *
     * @param host host to be written.
     */
    public static void setProxyHostname(String host) {
        preferences.put(I2L_PROXY_HOSTNAME, host);
    }

    /**
     * Set proxy port.
     *
     * @param port port to be written.
     */
    public static void setProxyPort(String port) {
        preferences.put(I2L_PROXY_PORT, port);
    }

    /**
     * Get proxy config.
     *
     * @return proxy config.
     */
    public static ProxyConfig getProxyConfig() {

        int port;
        try {
            port = Integer.parseInt(preferences.get(I2L_PROXY_PORT, ""));
        } catch (NumberFormatException e) {
            port = -1;
        }

        return new ProxyConfig(preferences.get(I2L_PROXY_HOSTNAME, ""), port);
    }

}
