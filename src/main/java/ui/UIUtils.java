package ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * UI.UIUtils.java
 * Contains common UI methods.
 */
public class UIUtils {

    /**
     * Put text into clipboard.
     *
     * @param text the recognised result to be put into clipboard.
     */
    public static void putStringIntoClipboard(String text) {

        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard.getSystemClipboard().setContent(content);

    }

    /**
     * Method to set left and right margin to a node with default bottom margin
     *
     * @param node  node to be set margin
     * @param left  left margin
     * @param right right margin
     */
    public static void setDefaultNodeMargin(Node node, int left, int right) {
        GridPane.setMargin(node, new Insets(0, right, 5, left));
    }

    /**
     * Method to get a customised Label
     *
     * @param text text to be displayed
     * @return a customised Label
     */
    public static Label getTextLabel(String text) {

        Label label = new Label(text);
        // set font size
        label.setFont(Font.font(12));
        // set text color
        label.setTextFill(new Color(0.149, 0.149, 0.149, 1));

        return label;

    }

    /**
     * Display an error alert dialog.
     *
     * @param error error message to be displayed.
     */
    public static void displayError(String error) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");

        // Clear default OK button
        alert.getButtonTypes().clear();
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().addAll(okButtonType);

        // set no header area in the dialog
        alert.setHeaderText(null);
        alert.setContentText(error);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        stage.showAndWait();

    }

}
