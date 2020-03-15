package ui;

import io.APICredentialConfig;
import io.IOUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * UI.APICredentialsTab.java
 * Used to display and edit API Credentials in the preferences panel.
 */
public class APICredentialsTab extends Tab {

    private static final int MINIMUM_MARGIN = 14;

    public APICredentialsTab() {

        // tab header
        setText(" API Credentials ");
        // non-closable
        setClosable(false);

        // load initial API credential config
        APICredentialConfig apiCredentialConfig = IOUtils.getAPICredentialConfig();

        // 3 * 2 layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(3);
        gridPane.setVgap(2);
        // 30 px padding
        gridPane.setPadding(new Insets(MINIMUM_MARGIN * 2));

        // add header hyperlink
        Hyperlink hyperlink = new Hyperlink("MathpixOCR API Credentials");
        hyperlink.setVisited(true);
        hyperlink.setUnderline(false);
        hyperlink.setBorder(Border.EMPTY);
        hyperlink.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        hyperlink.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(IOUtils.MATHPIX_DASHBOARD_URL));
                } catch (IOException | URISyntaxException ignored) {
                }
            }
        });

        GridPane.setMargin(hyperlink, new Insets(0, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));
        gridPane.add(hyperlink, 0, 0, 2, 1);

        // add "App ID:" label
        Label appIdLabel = new Label("App ID:");
        GridPane.setMargin(appIdLabel, new Insets(MINIMUM_MARGIN));
        gridPane.add(appIdLabel, 0, 1);

        // add app id TextFiled
        TextField idTextField = new TextField();
        idTextField.setPromptText("App ID");
        idTextField.setText(apiCredentialConfig.getAppId());
        idTextField.setPrefWidth(280);
        GridPane.setMargin(idTextField, new Insets(MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));

        // save to Java Preferences API when text is changed
        idTextField.textProperty().addListener((observable, oldValue, newValue) -> IOUtils.setAppId(newValue));

        // moves the caret after the last char of the text
        idTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(idTextField::end);
            }
        });

        gridPane.add(idTextField, 1, 1);

        // add "App Key:" label
        Label appKeyLabel = new Label("App Key:");
        GridPane.setMargin(appKeyLabel, new Insets(MINIMUM_MARGIN));
        gridPane.add(appKeyLabel, 0, 2);

        // add app key TextFiled
        TextField keyTextField = new TextField();
        keyTextField.setPromptText("App Key");
        keyTextField.setText(apiCredentialConfig.getAppKey());
        keyTextField.setPrefWidth(280);
        GridPane.setMargin(keyTextField, new Insets(MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));

        // save to Java Preferences API when text is changed
        keyTextField.textProperty().addListener((observable, oldValue, newValue) -> IOUtils.setAppKey(newValue));

        // moves the caret after the last char of the text
        keyTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(keyTextField::end);
            }
        });

        gridPane.add(keyTextField, 1, 2);

        setContent(gridPane);

    }

}
