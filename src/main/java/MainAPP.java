import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.SystemUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


/**
 * MainAPP.java
 * Initialises main interface of the JavaFX application.
 * The primary stage will be initialised with 1 ImageView, 1 Button, 4 TextFields and 1 ProgressBar.
 * The app will also start a new thread to listen to the system clipboard changes.
 * The app will add a tray icon to menu bar and set the window style as StageStyle.UTILITY.
 * The color of the icon dependents on the OS. White for macOS dark, black for macOS light, blue for the rest.
 */
public class MainAPP extends Application {

    private Stage stage;

    private APIKeyDialog apiKeyDialog = new APIKeyDialog();

    private static Image lastImage = null;

    private BackGridPane backGridPane = new BackGridPane();

    private static final String APPLICATION_TITLE = "Image2LaTeX";

    private ClipboardListener clipboardListener = new ClipboardListener();

    /**
     * ClipboardListener class
     * javafx.concurrent.Task for the listener thread to monitor the system clipboard.
     */
    private class ClipboardListener extends Task<Void> {

        // boolean variable for stopping the while loop in Void call()
        private volatile boolean exit = false;

        /**
         * Set new image found in clipboard to be painted by the ImageView.
         *
         * @return nothing.
         */
        @Override
        protected Void call() {

            while (!exit) {

                // check the clipboard ~2 times per second
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }

                // Platform.runLater() for update UI elements in JavaFX app
                Platform.runLater(() -> {

                    Image image = Utilities.getClipboardImage();

                    // check if the current image in the clipboard is as the same as the previous one
                    if (image != null && !image.equals(lastImage)) {

                        // store the current image for next check
                        lastImage = image;

                        // update the ImageView
                        backGridPane.setClipboardImageView(image);

                    }

                });

            }

            return null;

        }

        /**
         * Stop the listener thread.
         */
        void stop() {
            exit = true;
        }

    }

    /**
     * Start of JavaFX application.
     *
     * @param primaryStage stage of the main window.
     */
    @Override
    public void start(Stage primaryStage) {

        // show API key dialog if config file does not exist
        if (!Utilities.configFileExists()) {
            showAPIKeyDialog();
        }

        // indicate whether the tray icon was successfully added to the menu bar
        boolean hasAddIconToTray = false;

        // store the reference of the primaryStage
        this.stage = primaryStage;

        // start the clipboard listener thread
        new Thread(clipboardListener).start();

        try {
            // call add icon to menu bar method, get a boolean result
            hasAddIconToTray = addIconToMenuBar();
        } catch (IOException | AWTException e) {
            // otherwise, stop the listener thread and exit the app
            clipboardListener.stop();

            Platform.exit();
            System.exit(0);
        }

        // initialise scene with the BackGridPane
        Scene scene = new Scene(backGridPane);

        // add scene to the primary stage
        this.stage.setScene(scene);

        // set app title
        this.stage.setTitle(APPLICATION_TITLE);

        // load icon resources
        InputStream iconInputStream = getClass().getClassLoader().getResourceAsStream("icon-other.png");
        assert iconInputStream != null;

        // set the title bar app icon
        this.stage.getIcons().add(new Image(iconInputStream));

        if (hasAddIconToTray) {
            // set the JavaFX app not to shutdown when the last window is closed
            Platform.setImplicitExit(false);
            // set the app window with minimal platform decorations
            this.stage.initStyle(StageStyle.UTILITY);
        } else {
            // set the app shutdown when the window is closed
            this.stage.setOnCloseRequest(e -> {
                // stop the listener thread
                clipboardListener.stop();

                Platform.exit();
                System.exit(0);
            });

        }

        // set the app window always on top
        this.stage.setAlwaysOnTop(true);

        // show the primary stage
        this.stage.show();

        // set the app window is not resizable
        this.stage.setResizable(false);

    }

    /**
     * Set up a tray icon and add it to system menu bar.
     * Original source: https://stackoverflow.com/a/40571223/4658633
     *
     * @return boolean variable to indicate whether the tray icon was successfully added to the menu bar.
     * @throws IOException  if the icon resources cannot be loaded.
     * @throws AWTException if the icon cannot be correctly added to the system menu bar.
     */
    private Boolean addIconToMenuBar() throws IOException, AWTException {

        // initialise the AWT toolkit
        Toolkit.getDefaultToolkit();

        // current OS didn't support system tray
        if (!SystemTray.isSupported()) {
            return false;
        }

        InputStream iconInputStream;

        // macOS
        if (SystemUtils.IS_OS_MAC_OSX) {
            // dark mode
            if (Utilities.isMacDarkMode()) {
                // load the white colour icon
                iconInputStream = getClass().getClassLoader().getResourceAsStream("icon-mac-dark.png");
            } else {
                // load the black colour icon
                iconInputStream = getClass().getClassLoader().getResourceAsStream("icon-mac.png");
            }
        } else if (SystemUtils.IS_OS_WINDOWS) {
            // while colour icon for windows
            iconInputStream = getClass().getClassLoader().getResourceAsStream("icon-windows.png");
        } else {
            // blue colour icon for the rest OS
            iconInputStream = getClass().getClassLoader().getResourceAsStream("icon-other.png");
        }
        assert iconInputStream != null;

        // set up the system tray
        SystemTray tray = SystemTray.getSystemTray();
        BufferedImage image = ImageIO.read(iconInputStream);
        // use the loaded icon as tray icon
        TrayIcon trayIcon = new TrayIcon(image);

        // show the primary stage if the icon is right clicked
        trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

        // add app name as a menu item
        MenuItem openItem = new MenuItem(APPLICATION_TITLE);
        // show the primary stage if the app name item is clicked
        openItem.addActionListener(event -> Platform.runLater(this::showStage));

        // add change API key setting menu item
        MenuItem settingItem = new MenuItem("API Keys");
        settingItem.addActionListener(event -> Platform.runLater(this::showAPIKeyDialog));

        // add quit option as the app cannot be closed by clicking the window close button
        MenuItem exitItem = new MenuItem("Quit");

        // add action listener for cleanup
        exitItem.addActionListener(event -> {
            // stop the listener thread
            clipboardListener.stop();

            // remove the icon
            tray.remove(trayIcon);

            Platform.exit();
            System.exit(0);
        });

        // set up the popup menu
        final PopupMenu popup = new PopupMenu();
        popup.add(openItem);
        popup.addSeparator();
        popup.add(settingItem);
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        // add icon to the system
        tray.add(trayIcon);

        // return as successful
        return true;

    }

    /**
     * Show the primary stage in front of all other apps.
     */
    private void showStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }

    /**
     * Call Utilities.showAPIKeyDialog() to change API key.
     */
    private void showAPIKeyDialog() {

        AppConfig appConfig = Utilities.readConfigFile();
        if (appConfig != null) {
            apiKeyDialog.idTextField.setText(appConfig.getApp_id());
            apiKeyDialog.keyTextField.setText(appConfig.getApp_key());
        }

        apiKeyDialog.show();

    }

    /**
     * Launch JavaFx application
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
