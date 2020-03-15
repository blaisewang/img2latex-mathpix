package ui;


import io.IOUtils;
import io.ProxyConfig;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 * UI.HTTPTab.java
 * Used to display and edit HTTP proxy options in the preferences panel.
 */
public class HTTPTab extends Tab {

    private static final int PANEL_MARGIN = 20;
    private static final int MINIMUM_MARGIN = 12;

    public HTTPTab() {

        // tab header
        setText(" HTTP ");
        // non-closable
        setClosable(false);

        // load initial proxy enable option
        boolean proxyEnableOption = IOUtils.getProxyEnableOption();
        // load initial proxy config
        ProxyConfig proxyConfig = IOUtils.getProxyConfig();

        // 4 * 2 layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(4);
        gridPane.setVgap(2);
        gridPane.setPadding(new Insets(PANEL_MARGIN, PANEL_MARGIN + MINIMUM_MARGIN, PANEL_MARGIN, PANEL_MARGIN));

        // add header label
        Label headerLabel = new Label("HTTP Proxy");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setMargin(headerLabel, new Insets(0, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));
        gridPane.add(headerLabel, 0, 0, 2, 1);

        CheckBox proxyEnableOptionCheckBox = new CheckBox("HTTP Proxy");
        TextField hostnameTextField = new TextField();
        TextField portTextField = new TextField();

        proxyEnableOptionCheckBox.setSelected(proxyEnableOption);
        hostnameTextField.setDisable(!proxyEnableOption);
        portTextField.setDisable(!proxyEnableOption);

        proxyEnableOptionCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            IOUtils.setProxyEnableOption(newValue);
            hostnameTextField.setDisable(!newValue);
            portTextField.setDisable(!newValue);
        });

        GridPane.setMargin(proxyEnableOptionCheckBox, new Insets(MINIMUM_MARGIN));
        gridPane.add(proxyEnableOptionCheckBox, 0, 1, 2, 1);

        // add "Host:" label
        Label hostLabel = new Label("Host:");
        GridPane.setMargin(hostLabel, new Insets(MINIMUM_MARGIN));
        gridPane.add(hostLabel, 0, 2);

        // customise hostname TextFiled
        hostnameTextField.setPromptText("Host");
        hostnameTextField.setText(proxyConfig.getHostname());
        hostnameTextField.setPrefWidth(200);
        GridPane.setMargin(hostnameTextField, new Insets(MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));

        // save to Java Preferences API when hostname is changed
        hostnameTextField.textProperty().addListener((observable, oldValue, newValue) -> IOUtils.setProxyHostname(newValue));

        // moves the caret after the last char of the text
        hostnameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(hostnameTextField::end);
            }
        });

        gridPane.add(hostnameTextField, 1, 2);

        // add "Port" label
        Label portLabel = new Label("Port:");
        GridPane.setMargin(portLabel, new Insets(MINIMUM_MARGIN));
        gridPane.add(portLabel, 0, 3);

        // customise port TextFiled
        portTextField.setPromptText("Port");
        portTextField.setText(proxyConfig.getPortAsString());
        portTextField.setMaxWidth(80);
        GridPane.setMargin(portTextField, new Insets(MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));

        // save to Java Preferences API when port number is changed
        portTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                portTextField.setText(newValue.replaceAll("\\D+", ""));
            } else {
                IOUtils.setProxyPort(newValue);
            }
        });

        // moves the caret after the last char of the text
        portTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(portTextField::end);
            }
        });

        gridPane.add(portTextField, 1, 3);

        setContent(gridPane);

    }

}
