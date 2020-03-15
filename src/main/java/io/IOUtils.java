package io;

import javafx.scene.image.Image;

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

    public final static String UNEXPECTED_ERROR = "Unexpected error";
    public final static String INVALID_CREDENTIALS_ERROR = "Invalid credentials";
    public final static String INVALID_PROXY_CONFIG_ERROR = "Invalid proxy config";
    public final static String NO_IMAGE_FOUND_IN_THE_CLIPBOARD_ERROR = "No image found in the clipboard";

    public final static String TEXT_API_URL = "https://api.mathpix.com/v3/text";
    public final static String LEGACY_API_URL = "https://api.mathpix.com/v3/latex";
    public final static String MATHPIX_DASHBOARD_URL = "https://dashboard.mathpix.com/";
    public final static String GITHUB_RELEASES_URL = "https://github.com/blaisewang/img2latex-mathpix/releases";

    private final static String I2L_APP_ID = "I2L_APP_ID";
    private final static String I2L_APP_KEY = "I2L_APP_KEY";
    private final static String I2L_THIRD_RESULT_FORMATTING_OPTION = "I2L_THIRD_RESULT_FORMATTING_OPTION";
    private final static String I2L_FOURTH_RESULT_FORMATTING_OPTION = "I2L_FOURTH_RESULT_FORMATTING_OPTION";
    private final static String I2L_PROXY_ENABLE_OPTION = "I2L_PROXY_ENABLE_OPTION";
    private final static String I2L_PROXY_HOSTNAME = "I2L_PROXY_HOSTNAME";
    private final static String I2L_PROXY_PORT = "I2L_PROXY_PORT";
    private final static String I2L_IMPROVED_OCR_ENABLE_OPTION = "I2L_IMPROVED_OCR_ENABLE_OPTION";

    private final static String CONFIG_NODE_PATH = "I2L_API_CREDENTIAL_CONFIG";
    private static Preferences preferences = Preferences.userRoot().node(CONFIG_NODE_PATH);

    /**
     * @return if os is macOS.
     */
    public static boolean isOSMacOSX() {

        String osName = System.getProperty("os.name");
        if (osName == null) {
            return false;
        }

        return osName.startsWith("Mac OS X");

    }

    /**
     * @return if os is Windows.
     */
    public static boolean isOSWindows() {

        String osName = System.getProperty("os.name");
        if (osName == null) {
            return false;
        }

        return osName.startsWith("Windows");

    }

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
    public static Response concurrentCall(Recognition recognition, Image image) {

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
     * Set third result formatting option.
     *
     * @param option option to be written.
     */
    public static void setThirdResultFormattingOption(int option) {
        preferences.putInt(I2L_THIRD_RESULT_FORMATTING_OPTION, option);
    }

    /**
     * Get third result formatting option.
     *
     * @return third result formatting option.
     */
    public static int getThirdResultFormattingOption() {
        return preferences.getInt(I2L_THIRD_RESULT_FORMATTING_OPTION, 2);
    }

    /**
     * Set fourth result Formatting option.
     *
     * @param option option to be written.
     */
    public static void setFourthResultFormattingOption(int option) {
        preferences.putInt(I2L_FOURTH_RESULT_FORMATTING_OPTION, option);
    }

    /**
     * Get fourth result formatting option.
     *
     * @return fourth result formatting option.
     */
    public static int getFourthResultFormattingOption() {
        return preferences.getInt(I2L_FOURTH_RESULT_FORMATTING_OPTION, 0);
    }

    /**
     * @param string          string to be formatted.
     * @param left_delimiter  prefix.
     * @param right_delimiter postfix.
     * @return formatted string.
     */
    public static String formatHelper(String string, String left_delimiter, String right_delimiter) {

        String formatted_left_delimiter = left_delimiter + "\n ";
        String formatted_right_delimiter = "\n" + right_delimiter;

        if (string.startsWith("\\(") && string.split("\\u005C\\u0028").length == 2) {

            return string.replace("\\(", formatted_left_delimiter).replace("\\)", formatted_right_delimiter);

        }

        return string.replace("\\(", "$").replace("\\)", "$").
                replace("\\[\n", "\\[").replace("\n\\]", "\\]").
                replace("\\[", formatted_left_delimiter).replace("\\]", formatted_right_delimiter);

    }

    /**
     * Format the original recognised result with the selected formatting options.
     *
     * @param result recognised result.
     * @return the formatted result.
     */
    public static String thirdResultFormatter(String result) {

        // return null if the original result is null
        if (result == null) {
            return null;
        }

        int option = getThirdResultFormattingOption();

        switch (option) {
            case 0:
                return formatHelper(result, "\\begin{equation*}", "\\end{equation*}");
            case 1:
                return formatHelper(result, "\\begin{align*}", "\\end{align*}");
            case 3:
                return formatHelper(result, "\\[", "\\]");
            default:
                // default for option 2 and others
                return formatHelper(result, "$$", "$$");
        }

    }

    /**
     * Format the original recognised result with the selected formatting options.
     *
     * @param result recognised result.
     * @return the formatted result.
     */
    public static String fourthResultFormatter(String result) {

        // return null if the original result is null
        if (result == null) {
            return null;
        }

        int option = getFourthResultFormattingOption();


        if (option == 1) {
            return formatHelper(result, "\\begin{align}", "\\end{align}");
        }

        return formatHelper(result, "\\begin{equation}", "\\end{equation}");

    }

    /**
     * Set proxy enable option.
     *
     * @param option option to be written.
     */
    public static void setProxyEnableOption(boolean option) {
        preferences.putBoolean(I2L_PROXY_ENABLE_OPTION, option);
    }

    /**
     * Get proxy option enabled or not.
     *
     * @return proxy enable option.
     */
    public static boolean getProxyEnableOption() {
        return preferences.getBoolean(I2L_PROXY_ENABLE_OPTION, false);
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

    /**
     * Set improved OCR enable option.
     *
     * @param option option to be written.
     */
    public static void setImprovedOCREnableOption(Boolean option) {
        preferences.putBoolean(I2L_IMPROVED_OCR_ENABLE_OPTION, option);
    }

    /**
     * Get improved OCR option enabled or not.
     *
     * @return improved OCR enable option.
     */
    public static boolean getImprovedOCREnableOption() {
        return preferences.getBoolean(I2L_IMPROVED_OCR_ENABLE_OPTION, true);
    }

}
