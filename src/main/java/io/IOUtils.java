package io;

import javafx.scene.image.Image;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    public static final String API_URL = "https://api.mathpix.com/v3/text";
    public static final String MATHPIX_DASHBOARD_URL = "https://dashboard.mathpix.com/";
    public static final String GITHUB_RELEASES_URL = "https://github.com/blaisewang/img2latex-mathpix/releases";

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
        return PreferenceHelper.getAPICredentialConfig().isValid();
    }

    /**
     * @return current date formatted like ${year}-${month}.
     */
    public static String getCurrentDate() {
        var localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear() + "-" + localDate.getMonthValue();
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
     * Method to determine if there is unwrapped text in a string.
     *
     * @param string string to be search.
     * @return is text all wrapped.
     */
    public static boolean isTextAllWrapped(String string) {

        if (!string.startsWith("\\(")) {
            return false;
        }

        var rIndex = string.indexOf("\\)");

        while (rIndex < string.length() - 2) {
            var lIndex = string.indexOf("\\(", rIndex);
            if (rIndex == -1 || lIndex == -1) {
                return false;
            }
            if (string.substring(rIndex + 2, lIndex).trim().length() > 0) {
                return false;
            }
            rIndex = string.indexOf("\\)", rIndex + 1);
        }

        return true;

    }

    /**
     * Shadow method of formatHelper()
     *
     * @param string         string to be formatted.
     * @param leftDelimiter  prefix.
     * @param rightDelimiter postfix.
     * @return formatted string.
     */
    private static String formatHandler(String string, String leftDelimiter, String rightDelimiter) {

        String lDelimiter;
        String rDelimiter;

        if ("$".equals(leftDelimiter)) {
            lDelimiter = leftDelimiter;
            rDelimiter = rightDelimiter;
        } else {
            lDelimiter = leftDelimiter + "\n";
            rDelimiter = "\n" + rightDelimiter;
        }

        if (isTextAllWrapped(string)) {
            return string.replace("\\(", lDelimiter).replace("\\)", rDelimiter);
        }

        if ("$".equals(leftDelimiter)) {
            lDelimiter = "\\[\n";
            rDelimiter = "\n\\]";
        }

        return string.replace("\\(", "$").replace("\\)", "$").
                replace("\\[\n", "\\[").replace("\n\\]", "\\]").
                replace("\\[", lDelimiter).replace("\\]", rDelimiter);

    }

    /**
     * @param string         string to be formatted.
     * @param leftDelimiter  prefix.
     * @param rightDelimiter postfix.
     * @return formatted string with multiple spaces replaced with single spaces.
     */
    public static String formatHelper(String string, String leftDelimiter, String rightDelimiter) {
        return formatHandler(string, leftDelimiter, rightDelimiter).replaceAll("( )+", " ");
    }

    /**
     * Format the original recognised result with the selected formatting options.
     *
     * @param result recognised result.
     * @return the formatted result.
     */
    public static String secondResultFormatter(String result) {

        // return null if the original result is null
        if (result == null) {
            return null;
        }

        var option = PreferenceHelper.getSecondResultFormattingOption();

        // default for option 2 and others
        return switch (option) {
            case 0 -> formatHelper(result, "\\begin{equation*}", "\\end{equation*}");
            case 1 -> formatHelper(result, "\\begin{align*}", "\\end{align*}");
            case 3 -> formatHelper(result, "\\[", "\\]");
            default -> formatHelper(result, "$$", "$$");
        };
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

        var option = PreferenceHelper.getThirdResultFormattingOption();

        // default for option 0 and others
        return switch (option) {
            case 1 -> formatHelper(result, "\\begin{align}", "\\end{align}");
            case 2 -> formatHelper(result, "$", "$");
            default -> formatHelper(result, "\\begin{equation}", "\\end{equation}");
        };

    }


}
