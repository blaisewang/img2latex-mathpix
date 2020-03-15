package ui;

import io.IOUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;


/**
 * UI.App.java
 * Initialises main interface of the JavaFX application.
 * The primary stage will be initialised with 1 ImageView, 1 Button, 4 TextFields and 1 ProgressBar.
 * The app will add a tray icon to menu bar and set the window style as StageStyle.UTILITY.
 * The color of the icon dependents on the OS. White for macOS dark, black for macOS light, blue for the rest.
 */
public class App extends Application {

    private Stage stage;

    private BackGridPane backGridPane = new BackGridPane();

    private static Properties properties = new Properties();

    private static String APPLICATION_TITLE;

    /**
     * Start of JavaFX application.
     *
     * @param primaryStage stage of the main window.
     */
    @Override
    public void start(Stage primaryStage) {

        // show API key dialog if the config is invalid
        if (!IOUtils.isAPICredentialConfigValid()) {
            backGridPane.showPreferencesDialog(1);
        }

        // indicate whether the tray icon was successfully added to the menu bar
        boolean hasAddIconToTray = false;

        // store the reference of the primaryStage
        stage = primaryStage;

        try {
            // call add icon to menu bar method, get a boolean result
            hasAddIconToTray = addTrayIcon();
        } catch (IOException | AWTException e) {
            Platform.exit();
            System.exit(0);
        }

        // initialise scene with the UI.BackGridPane
        Scene scene = new Scene(backGridPane);

        // enter key pressed event binding to the Scene
        scene.onKeyReleasedProperty().bind(backGridPane.onKeyReleasedProperty());

        // add scene to the primary stage
        stage.setScene(scene);

        // set app title
        stage.setTitle(APPLICATION_TITLE);

        // load icon resources
        InputStream iconInputStream = getClass().getClassLoader().getResourceAsStream("icon-other.png");
        assert iconInputStream != null;

        // set the title bar app icon
        stage.getIcons().add(new Image(iconInputStream));

        if (hasAddIconToTray) {
            // set the JavaFX app not to shutdown when the last window is closed
            Platform.setImplicitExit(false);
            // set the app window with minimal platform decorations
            stage.initStyle(StageStyle.UTILITY);
        } else {
            // right click to show API credential setting dialog
            scene.setOnMouseReleased(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    backGridPane.showPreferencesDialog(0);
                }
            });
            // set the app shutdown when the window is closed
            stage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });

        }

        // set the app window always on top
        stage.setAlwaysOnTop(true);

        // show the primary stage
        stage.show();

        // set the app window in the upper right corner of the screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - stage.getWidth() - 80);
        stage.setY(primaryScreenBounds.getMinY() + 80);

        // set the app window is not resizable
        stage.setResizable(false);

    }

    /**
     * Tray icon handler.
     */
    private void trayIconHandler(InputStream iconInputStream) throws IOException, AWTException {

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

        // add Preferences menu item
        MenuItem settingItem = new MenuItem("Preferences");
        settingItem.addActionListener(event -> Platform.runLater(() -> backGridPane.showPreferencesDialog(0)));

        // add check for updates menu item
        MenuItem updateCheckItem = new MenuItem("Check for Updates");

        // add current version info menu item
        MenuItem versionItem = new MenuItem("Version: " + properties.getProperty("version"));

        // add click action listener
        updateCheckItem.addActionListener(event -> {
            try {
                Desktop.getDesktop().browse(new URI(IOUtils.GITHUB_RELEASES_URL));
            } catch (IOException | URISyntaxException ignored) {
            }
        });

        // add quit option as the app cannot be closed by clicking the window close button
        MenuItem exitItem = new MenuItem("Quit");

        // add action listener for cleanup
        exitItem.addActionListener(event -> {
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
        popup.addSeparator();
        popup.add(versionItem);
        popup.add(updateCheckItem);
        popup.addSeparator();
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        // add icon to the system
        tray.add(trayIcon);

    }

    /**
     * Set up a tray icon and add it to system menu bar.
     * Original source: https://stackoverflow.com/a/40571223/4658633
     *
     * @return boolean variable to indicate whether the tray icon was successfully added to the menu bar.
     * @throws IOException  if the icon resources cannot be loaded.
     * @throws AWTException if the icon cannot be correctly added to the system menu bar.
     */
    private Boolean addTrayIcon() throws IOException, AWTException {

        // initialise the AWT toolkit
        Toolkit.getDefaultToolkit();

        // current OS didn't support system tray
        if (!SystemTray.isSupported()) {
            return false;
        }

        InputStream iconInputStream;

        // macOS
        if (IOUtils.isOSMacOSX()) {
            // dark mode
            if (IOUtils.isMacDarkMode()) {
                // load the white colour icon
                iconInputStream = getClass().getClassLoader().getResourceAsStream("icon-mac-dark.png");
            } else {
                // load the black colour icon
                iconInputStream = getClass().getClassLoader().getResourceAsStream("icon-mac.png");
            }
        } else if (IOUtils.isOSWindows()) {
            // while colour icon for windows
            iconInputStream = getClass().getClassLoader().getResourceAsStream("icon-windows.png");
        } else {
            // blue colour icon for the rest OS
            iconInputStream = getClass().getClassLoader().getResourceAsStream("icon-other.png");
        }
        assert iconInputStream != null;

        trayIconHandler(iconInputStream);

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
     * Launch JavaFx application.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) throws IOException {

        // load application name
        properties.load(Objects.requireNonNull(App.class.getClassLoader().getResourceAsStream("project.properties")));
        APPLICATION_TITLE = properties.getProperty("applicationName");

        // font smoothing
        System.setProperty("prism.lcdtext", "false");

        launch(args);

    }

}
