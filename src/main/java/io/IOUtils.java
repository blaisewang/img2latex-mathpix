package io;

import javafx.scene.image.Image;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;


/**
 * IO.IOUtils.java
 * Contains common IO methods.
 */
public class IOUtils {

    public static final String EXCEPTION_MARK = "Exception:";

    public static final String UNEXPECTED_ERROR = "Unexpected error";
    public static final String INVALID_CREDENTIALS_ERROR = "Invalid credentials";
    public static final String INVALID_PROXY_CONFIG_ERROR = "Invalid proxy config";
    public static final String NO_IMAGE_FOUND_IN_THE_CLIPBOARD_ERROR = "No image found in the clipboard";

    public static final String TEXT_API_URL = "https://api.mathpix.com/v3/text";
    public static final String LEGACY_API_URL = "https://api.mathpix.com/v3/latex";
    public static final String MATHPIX_DASHBOARD_URL = "https://dashboard.mathpix.com/";
    public static final String GITHUB_RELEASES_URL = "https://github.com/blaisewang/img2latex-mathpix/releases";

    private static final String I2L_APP_ID = "I2L_APP_ID";
    private static final String I2L_APP_KEY = "I2L_APP_KEY";
    private static final String I2L_THIRD_RESULT_FORMATTING_OPTION = "I2L_THIRD_RESULT_FORMATTING_OPTION";
    private static final String I2L_FOURTH_RESULT_FORMATTING_OPTION = "I2L_FOURTH_RESULT_FORMATTING_OPTION";
    private static final String I2L_PROXY_ENABLE_OPTION = "I2L_PROXY_ENABLE_OPTION";
    private static final String I2L_PROXY_HOSTNAME = "I2L_PROXY_HOSTNAME";
    private static final String I2L_PROXY_PORT = "I2L_PROXY_PORT";
    private static final String I2L_IMPROVED_OCR_ENABLE_OPTION = "I2L_IMPROVED_OCR_ENABLE_OPTION";

    private static final String CONFIG_NODE_PATH = "I2L_API_CREDENTIAL_CONFIG";
    private static final Preferences PREFERENCES = Preferences.userRoot().node(CONFIG_NODE_PATH);

    /**
     * @return if os is macOS.
     */
    public static boolean isOSMacOSX() {

        var osName = System.getProperty("os.name");
        if (osName == null) {
            return false;
        }

        return osName.startsWith("Mac OS X");

    }

    /**
     * @return if os is Windows.
     */
    public static boolean isOSWindows() {

        var osName = System.getProperty("os.name");
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
            final var process = Runtime.getRuntime().exec(new String[]{"defaults", "read", "-g", "AppleInterfaceStyle"});
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

        var executor = Executors.newSingleThreadExecutor();

        if (recognition.setSrcParameters(image)) {
            var result = executor.submit(recognition);
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
        PREFERENCES.put(I2L_APP_ID, appId);
    }

    /**
     * Set app key.
     *
     * @param appKey App key to be written.
     */
    public static void setAppKey(String appKey) {
        PREFERENCES.put(I2L_APP_KEY, appKey);
    }

    /**
     * Get App ID and App Key from Java Preferences API.
     *
     * @return IO.APICredentialConfig object.
     */
    public static APICredentialConfig getAPICredentialConfig() {
        return new APICredentialConfig(PREFERENCES.get(I2L_APP_ID, ""), PREFERENCES.get(I2L_APP_KEY, ""));
    }

    /**
     * Set third result formatting option.
     *
     * @param option option to be written.
     */
    public static void setThirdResultFormattingOption(int option) {
        PREFERENCES.putInt(I2L_THIRD_RESULT_FORMATTING_OPTION, option);
    }

    /**
     * Get third result formatting option.
     *
     * @return third result formatting option.
     */
    public static int getThirdResultFormattingOption() {
        return PREFERENCES.getInt(I2L_THIRD_RESULT_FORMATTING_OPTION, 2);
    }

    /**
     * Set fourth result Formatting option.
     *
     * @param option option to be written.
     */
    public static void setFourthResultFormattingOption(int option) {
        PREFERENCES.putInt(I2L_FOURTH_RESULT_FORMATTING_OPTION, option);
    }

    /**
     * Get fourth result formatting option.
     *
     * @return fourth result formatting option.
     */
    public static int getFourthResultFormattingOption() {
        return PREFERENCES.getInt(I2L_FOURTH_RESULT_FORMATTING_OPTION, 0);
    }

    /**
     * @param exception exception message.
     * @return formatted exception message.
     */
    public static String exceptionFormatter(String exception) {

        var splitArray = exception.split(EXCEPTION_MARK, 2);
        var result = splitArray[splitArray.length - 1].trim();

        return result.substring(0, 1).toUpperCase() + result.substring(1);

    }

    /**
     * @param string          string to be formatted.
     * @param left_delimiter  prefix.
     * @param right_delimiter postfix.
     * @return formatted string.
     */
    public static String formatHelper(String string, String left_delimiter, String right_delimiter) {

        var formatted_left_delimiter = left_delimiter + "\n ";
        var formatted_right_delimiter = "\n" + right_delimiter;

        if (string.startsWith("\\(") && string.split("\\u005C\\u0028").length == 2) {

            if ("$".equals(left_delimiter)) {
                return string.replace("\\(", left_delimiter).replace("\\)", right_delimiter).
                        replace("  ", " ");
            }

            return string.replace("\\(", formatted_left_delimiter).replace("\\)", formatted_right_delimiter).
                    replace("  ", " ");

        }

        if ("$".equals(left_delimiter)) {
            formatted_left_delimiter = "\\[\n ";
            formatted_right_delimiter = "\n\\]";
        }

        return string.replace("\\(", "$").replace("\\)", "$").
                replace("\\[\n", "\\[").replace("\n\\]", "\\]").
                replace("\\[", formatted_left_delimiter).replace("\\]", formatted_right_delimiter).
                replace("  ", " ");

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

        var option = getThirdResultFormattingOption();

        // default for option 2 and others
        if (option == 0) {
            return formatHelper(result, "\\begin{equation*}", "\\end{equation*}");
        } else if (option == 1) {
            return formatHelper(result, "\\begin{align*}", "\\end{align*}");
        } else if (option == 3) {
            return formatHelper(result, "\\[", "\\]");
        }
        return formatHelper(result, "$$", "$$");

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

        var option = getFourthResultFormattingOption();

        if (option == 1) {
            return formatHelper(result, "\\begin{align}", "\\end{align}");
        } else if (option == 2) {
            return formatHelper(result, "$", "$");
        }

        return formatHelper(result, "\\begin{equation}", "\\end{equation}");

    }

    /**
     * Set proxy enable option.
     *
     * @param option option to be written.
     */
    public static void setProxyEnableOption(boolean option) {
        PREFERENCES.putBoolean(I2L_PROXY_ENABLE_OPTION, option);
    }

    /**
     * Get proxy option enabled or not.
     *
     * @return proxy enable option.
     */
    public static boolean getProxyEnableOption() {
        return PREFERENCES.getBoolean(I2L_PROXY_ENABLE_OPTION, false);
    }

    /**
     * Set proxy host.
     *
     * @param host host to be written.
     */
    public static void setProxyHostname(String host) {
        PREFERENCES.put(I2L_PROXY_HOSTNAME, host);
    }

    /**
     * Set proxy port.
     *
     * @param port port to be written.
     */
    public static void setProxyPort(String port) {
        PREFERENCES.put(I2L_PROXY_PORT, port);
    }

    /**
     * Get proxy config.
     *
     * @return proxy config.
     */
    public static ProxyConfig getProxyConfig() {

        int port;
        try {
            port = Integer.parseInt(PREFERENCES.get(I2L_PROXY_PORT, ""));
        } catch (NumberFormatException e) {
            port = -1;
        }

        return new ProxyConfig(PREFERENCES.get(I2L_PROXY_HOSTNAME, ""), port);

    }

    /**
     * Set improved OCR enable option.
     *
     * @param option option to be written.
     */
    public static void setImprovedOCREnableOption(Boolean option) {
        PREFERENCES.putBoolean(I2L_IMPROVED_OCR_ENABLE_OPTION, option);
    }

    /**
     * Get improved OCR option enabled or not.
     *
     * @return improved OCR enable option.
     */
    public static boolean getImprovedOCREnableOption() {
        return PREFERENCES.getBoolean(I2L_IMPROVED_OCR_ENABLE_OPTION, true);
    }

}
