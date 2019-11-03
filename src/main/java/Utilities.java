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


class Utilities {

    private static Recognition recognition = new Recognition();

    static boolean isMacDarkMode() {

        try {

            final Process process = Runtime.getRuntime().exec(new String[]{"defaults", "read", "-g", "AppleInterfaceStyle"});
            process.waitFor(100, TimeUnit.MILLISECONDS);

            return process.exitValue() == 0;

        } catch (IOException | InterruptedException | IllegalThreadStateException e) {

            return false;

        }

    }

    static Image getClipboardImage() {

        final Clipboard clipboard = Clipboard.getSystemClipboard();

        if (clipboard.hasImage()) {

            return clipboard.getImage();

        }

        return null;

    }

    static void putStringIntoClipboard(String text) {

        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard.getSystemClipboard().setContent(content);

    }

    static OCRRequest.Response concurrentCall(Image image) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        if (recognition.setSrcParameters(image)) {

            Future<OCRRequest.Response> result = executor.submit(recognition);

            try {

                return result.get();

            } catch (InterruptedException | ExecutionException e) {

                showErrorDialog("Broken internet connection");

                return null;

            }

        }

        return null;

    }

    static void showErrorDialog(String context) {


        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(context);

        ButtonType issueButton = new ButtonType("Open an issue");
        alert.getButtonTypes().add(issueButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {

            if (result.get() == issueButton) {

                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/blaisewang/img2latex-mathpix/issues"));
                } catch (IOException | URISyntaxException ignored) {

                }
            }

        }

    }


    static String addDoubleDollarWrapper(String originalResult) {

        return originalResult == null ? null : "$$\n " + originalResult + " \n$$";

    }

    static String addEquationWrapper(String originalResult) {

        return originalResult == null ? null : "\\begin{equation}\n " + originalResult + " \n\\end{equation}";

    }

}
