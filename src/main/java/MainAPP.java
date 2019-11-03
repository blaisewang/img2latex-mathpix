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


public class MainAPP extends Application {

    private Stage stage;

    private static Image lastImage = null;

    private BackGridPane backGridPane = new BackGridPane();

    private static final String APPLICATION_TITLE = "Image2LaTeX";

    private ClipboardListener clipboardListener = new ClipboardListener();

    private class ClipboardListener extends Task<Void> {

        private volatile boolean exit = false;

        @Override
        protected Void call() {

            while (!exit) {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }

                Platform.runLater(() -> {

                    Image image = Utilities.getClipboardImage();

                    if (image != null && !image.equals(lastImage)) {

                        lastImage = image;

                        backGridPane.setImageView(image);

                    }

                });

            }

            return null;

        }

        void stop() {
            exit = true;
        }

    }

    @Override
    public void start(Stage primaryStage) {

        boolean hasAddIconToTray = false;

        this.stage = primaryStage;

        Platform.setImplicitExit(false);

        new Thread(clipboardListener).start();

        try {

            hasAddIconToTray = addIconToTray();

        } catch (IOException | AWTException e) {

            clipboardListener.stop();

            Platform.exit();
            System.exit(0);

        }

        Scene scene = new Scene(backGridPane);

        this.stage.setScene(scene);

        this.stage.setTitle(APPLICATION_TITLE);

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("icon.png");
        assert inputStream != null;

        this.stage.getIcons().add(new Image(inputStream));

        if (hasAddIconToTray) {

            this.stage.initStyle(StageStyle.UTILITY);

        } else {

            this.stage.setOnCloseRequest(e -> {

                clipboardListener.stop();

                Platform.exit();
                System.exit(0);

            });

        }

        this.stage.show();

        this.stage.setResizable(false);

    }

    private Boolean addIconToTray() throws IOException, AWTException {

        Toolkit.getDefaultToolkit();

        if (!SystemTray.isSupported()) {

            return false;

        }

        InputStream inputStream;

        if (SystemUtils.IS_OS_MAC_OSX) {

            if (Utilities.isMacDarkMode()) {

                inputStream = getClass().getClassLoader().getResourceAsStream("icon-dark.png");

            } else {

                inputStream = getClass().getClassLoader().getResourceAsStream("icon-mac.png");

            }

        } else {

            inputStream = getClass().getClassLoader().getResourceAsStream("icon.png");

        }
        assert inputStream != null;

        SystemTray tray = SystemTray.getSystemTray();

        BufferedImage image = ImageIO.read(inputStream);
        TrayIcon trayIcon = new TrayIcon(image);

        trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

        MenuItem openItem = new MenuItem("Img2LaTeX");
        openItem.addActionListener(event -> Platform.runLater(this::showStage));

        MenuItem exitItem = new MenuItem("Quit");

        exitItem.addActionListener(event -> {

            clipboardListener.stop();

            tray.remove(trayIcon);
            Platform.exit();
            System.exit(0);

        });

        final PopupMenu popup = new PopupMenu();
        popup.add(openItem);
        popup.addSeparator();
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        tray.add(trayIcon);

        return true;

    }

    private void showStage() {

        if (stage != null) {

            stage.show();
            stage.toFront();

        }

    }

    public static void main(String[] args) {

        launch(args);

    }

}
