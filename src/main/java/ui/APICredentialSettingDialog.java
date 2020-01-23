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
 * UI.APICredentialSettingDialog.java
 * shows a dialog to enter app ID and app key information.
 */
public class APICredentialSettingDialog {

    private Dialog<Pair<String, String>> dialog = new Dialog<>();
    private Stage stage;

    private TextField idTextField = new TextField();
    private TextField keyTextField = new TextField();

    /**
     * Original source: https://code.makery.ch/blog/javafx-dialogs-official/
     **/
    public APICredentialSettingDialog() {

        dialog.setTitle("API Credentials");
        dialog.setHeaderText("Enter your MathpixOCR API credentials below");

        // set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

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

        // enable/disable save button depending on whether an ID was entered
        Node saveButtonNode = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButtonNode.setDisable(true);

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
        keyTextField.textProperty().addListener((observable, oldValue, newValue) -> saveButtonNode.setDisable(newValue.trim().isEmpty()));

        dialog.getDialogPane().setContent(gridPane);

        // request focus on the app ID field by default.
        Platform.runLater(idTextField::requestFocus);

        // convert the result to a id-key-pair when the save button is clicked.
        dialog.setResultConverter(dialogButton -> {

            if (dialogButton == saveButtonType) {

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
            result.ifPresent(idKey -> IOUtils.setAPICredentialConfig(idKey.getKey(), idKey.getValue()));
        }

        stage.toFront();

    }

}
