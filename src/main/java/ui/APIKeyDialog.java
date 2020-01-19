package ui;

import io.IOUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;

/***
 * UI.APIKeyDialog.java
 * shows a dialog to enter app ID and app key information.
 */
public class APIKeyDialog {

    private Dialog<Pair<String, String>> dialog = new Dialog<>();
    private Stage stage;

    private TextField idTextField = new TextField();
    private TextField keyTextField = new TextField();

    /**
     * Original source: https://code.makery.ch/blog/javafx-dialogs-official/
     **/
    public APIKeyDialog() {

        dialog.setTitle("API Key");
        dialog.setHeaderText("Enter your MathpixOCR App ID and key below");

        // set the button types
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // create the ID and key labels and fields
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 10));

        idTextField.setPromptText("APP ID");
        idTextField.setPrefWidth(200);
        keyTextField.setPromptText("APP Key");
        keyTextField.setPrefWidth(200);

        gridPane.add(new Label("APP ID:"), 0, 0);
        gridPane.add(idTextField, 1, 0);
        gridPane.add(new Label("APP Key:"), 0, 1);
        gridPane.add(keyTextField, 1, 1);

        // enable/disable confirm button depending on whether an ID was entered
        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        // moves the caret to after the last char of the text
        idTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(idTextField::end);
            }
        });

        keyTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(keyTextField::end);
            }
        });

        // validation
        idTextField.textProperty().addListener((observable, oldValue, newValue) -> confirmButton.setDisable(newValue.trim().isEmpty()));

        dialog.getDialogPane().setContent(gridPane);

        // request focus on the app ID field by default.
        Platform.runLater(idTextField::requestFocus);

        // convert the result to a id-key-pair when the confirm button is clicked.
        dialog.setResultConverter(dialogButton -> {

            if (dialogButton == confirmButtonType) {

                Pair<String, String> pair = new Pair<>(idTextField.getText(), keyTextField.getText());

                idTextField.setText("");
                keyTextField.setText("");

                return pair;

            }

            return null;

        });

        stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);

    }

    /**
     * Set id text.
     *
     * @param id to display.
     */
    public void setId(String id) {
        this.idTextField.setText(id);
    }

    /**
     * Set key text.
     *
     * @param key to display.
     */
    public void setKey(String key) {
        this.keyTextField.setText(key);
    }

    /**
     * Show dialog stage.
     */
    public void show() {

        if (!stage.isShowing()) {
            Optional<Pair<String, String>> result = dialog.showAndWait();
            result.ifPresent(idKey -> IOUtils.createConfigFile(idKey.getKey(), idKey.getValue()));
        }

        stage.toFront();

    }

}
