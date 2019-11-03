import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.*;


/**
 * Utilities.java
 * Contains common methods used across the project.
 */
class Utilities {

    // Recognition object initialisation
    private static Recognition recognition = new Recognition();

    /**
     * Original source: https://stackoverflow.com/a/33477375/4658633
     *
     * @return if the macOS enabled dark mode.
     */
    static boolean isMacDarkMode() {

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
     * @return current image in the system clipboard.
     */
    static Image getClipboardImage() {

        final Clipboard clipboard = Clipboard.getSystemClipboard();

        // return null if there is no image in the clipboard
        if (clipboard.hasImage()) {
            return clipboard.getImage();
        }

        return null;

    }

    /**
     * @param text the recognised result to be put into clipboard.
     */
    static void putStringIntoClipboard(String text) {

        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard.getSystemClipboard().setContent(content);

    }

    /**
     * Execute the OCR request in Java concurrent way.
     *
     * @param image image to be recognised.
     * @return recognised result.
     */
    static OCRRequest.Response concurrentCall(Image image) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        if (recognition.setSrcParameters(image)) {
            Future<OCRRequest.Response> result = executor.submit(recognition);
            try {
                return result.get();
            } catch (InterruptedException | ExecutionException e) {
                // show internet connection error
                showErrorDialog("Broken internet connection");
                return null;
            }
        }

        return null;

    }

    /**
     * Show an alert dialog.
     *
     * @param context context text to be displayed.
     */
    static void showErrorDialog(String context) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");

        // set no header area in the dialog
        alert.setHeaderText(null);
        alert.setContentText(context);

        // add an issue button
        ButtonType issueButton = new ButtonType("Open an issue");
        alert.getButtonTypes().add(issueButton);

        // get the selected result
        Optional<ButtonType> result = alert.showAndWait();

        // if the value is present
        if (result.isPresent()) {
            // issue button clicked
            if (result.get() == issueButton) {
                try {
                    // open the project page to open an issue if necessary
                    Desktop.getDesktop().browse(new URI("https://github.com/blaisewang/img2latex-mathpix/issues"));
                } catch (IOException | URISyntaxException ignored) {
                }
            }
        }

    }

    /**
     * Wrap the original recognised result with $$ ... $$.
     *
     * @param originalResult recognised result.
     * @return recognised result with $$ wrapped.
     */
    static String addDoubleDollarWrapper(String originalResult) {
        // return null if the original result is null
        return originalResult == null ? null : "$$\n " + originalResult + " \n$$";
    }

    /**
     * Wrap the original recognised result with \begin{equation} ... \end{equation}.
     *
     * @param originalResult recognised result.
     * @return recognised result with {equation} wrapped.
     */
    static String addEquationWrapper(String originalResult) {
        // return null if the original result is null
        return originalResult == null ? null : "\\begin{equation}\n " + originalResult + " \n\\end{equation}";
    }

}
