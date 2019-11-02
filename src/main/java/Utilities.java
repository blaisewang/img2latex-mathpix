import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.IOException;
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

                return null;

            }

        }

        return null;

    }


    static String addDoubleDollarWrapper(String originalResult) {

        return "$$\n " + originalResult + " \n$$";

    }

    static String addEquationWrapper(String originalResult) {

        return "\\begin{equation}\n " + originalResult + " \n\\end{equation}";

    }

}
