import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Utilities {


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

        Recognition recognition = new Recognition(image);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<OCRRequest.Response> result = executor.submit(recognition);

        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }

    }

    static String replaceDoubleDollarWithWrapper(String doubleDollarResult) {

        String result = doubleDollarResult.replaceFirst("\\$\\$\n", "\\\\begin{equation}\n");
        return result.replaceFirst("\n\\$\\$", "\n\\\\end{equation}");

    }

}
