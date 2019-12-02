import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

/***
 * APIKeyDialog.java
 * shows a dialog to enter app ID and app key information.
 */
class APIKeyDialog {

    private Dialog<Pair<String, String>> dialog = new Dialog<>();

    /**
     * Original source: https://code.makery.ch/blog/javafx-dialogs-official/
     **/
    APIKeyDialog() {

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

        TextField idTextField = new TextField();
        idTextField.setPromptText("APP ID");
        idTextField.setPrefWidth(200);
        TextField keyTextField = new TextField();
        keyTextField.setPromptText("APP Key");
        keyTextField.setPrefWidth(200);

        gridPane.add(new Label("APP ID:"), 0, 0);
        gridPane.add(idTextField, 1, 0);
        gridPane.add(new Label("APP Key:"), 0, 1);
        gridPane.add(keyTextField, 1, 1);

        // enable/disable confirm button depending on whether an ID was entered
        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

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

    }

    /**
     * Show dialog
     */
    void show() {

        if (!dialog.isShowing()) {
            Optional<Pair<String, String>> result = dialog.showAndWait();
            result.ifPresent(idKey -> Utilities.createConfigFile(idKey.getKey(), idKey.getValue()));
        }

    }

}
