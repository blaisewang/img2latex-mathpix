package ui;

import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;


/**
 * UI.PreferencesDialog.java
 * Used to display app preferences.
 */
public class PreferencesDialog {

    private final Stage stage;

    private final Dialog<Boolean> dialog = new Dialog<>();

    private final SingleSelectionModel<Tab> selectionModel;

    public PreferencesDialog() {

        // set title
        dialog.setTitle("Preferences");

        stage = (Stage) dialog.getDialogPane().getScene().getWindow();

        // add FormattingTab (index: 0) and APICredentialsTab (index: 1) to the TabPane
        var tabPane = new TabPane(new FormattingTab(), new APICredentialsTab(), new ProxyTab());

        selectionModel = tabPane.getSelectionModel();

        var scene = new Scene(tabPane);

        // set the app preferences dialog always on top
        stage.setAlwaysOnTop(true);

        stage.setScene(scene);

        // hide the stage
        stage.setOnCloseRequest(e -> stage.close());

    }

    /**
     * Show dialog stage with the given index.
     */
    public void show(int index) {
        if (!stage.isShowing()) {
            selectionModel.select(index);
            dialog.showAndWait();
        }
    }

}
