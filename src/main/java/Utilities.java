import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;

class Utilities {

    static Image getClipboardImage() {

        final Clipboard clipboard = Clipboard.getSystemClipboard();

        if (clipboard.hasImage()) {

            return clipboard.getImage();

        }

        return null;

    }

    static String replaceDoubleDollarWithWrapper(String doubleDollarResult) {

        String result = doubleDollarResult.replaceFirst("\\$\\$\n", "\\\\begin{equation}\n");
        return result.replaceFirst("\n\\$\\$", "\n\\\\end{equation}");

    }

}
