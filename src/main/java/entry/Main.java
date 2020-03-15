package entry;

import io.IOUtils;
import ui.App;

import java.awt.Toolkit;
import java.io.IOException;


/**
 * entry.Main.java
 * Java AWT wrapper to start up the JavaFX application.
 * Wrapper is used for hide app icon in the Dock.
 */
public class Main {

    /**
     * @param args command line arguments.
     */
    public static void main(String[] args) throws IOException {

        if (IOUtils.isOSMacOSX()) {
            // hide icon in the Dock of macOS
            System.setProperty("apple.awt.UIElement", "true");
            Toolkit.getDefaultToolkit();
        }

        // run JavaFX application
        App.main(args);

    }

}
