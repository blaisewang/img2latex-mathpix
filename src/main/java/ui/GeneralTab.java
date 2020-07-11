package ui;

import io.PreferenceHelper;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * UI.FormattingTab.java
 * Used to display usage statistics and enable independent submit button.
 */
public final class GeneralTab extends Tab {

    private static final int PANEL_MARGIN = 20;
    private static final int MINIMUM_MARGIN = 20;

    private static final Label usageLabel = new Label();

    public GeneralTab() {

        // tab header
        setText(" General ");
        // non-closable
        setClosable(false);

        // load initial submit button enable option
        var submitButtonEnableOption = PreferenceHelper.getSubmitButtonEnableOption();

        // 2 * 2 layout
        var gridPane = new GridPane();
        gridPane.setHgap(2);
        gridPane.setVgap(2);
        gridPane.setPadding(new Insets(PANEL_MARGIN));

        var submitButtonEnableOptionCheckBox = new CheckBox("Manual Submit Button");
        submitButtonEnableOptionCheckBox.setSelected(submitButtonEnableOption);

        updateUsageLabelText();
        GridPane.setMargin(usageLabel, new Insets(MINIMUM_MARGIN));
        gridPane.add(usageLabel, 0, 0, 2, 1);

        // add "restart to take effect" warning label
        var warningLabel = new Label("restart to take effect");
        warningLabel.setVisible(false);
        warningLabel.setTextFill(Color.RED);
        GridPane.setMargin(warningLabel, new Insets(MINIMUM_MARGIN));
        gridPane.add(warningLabel, 1, 1);

        submitButtonEnableOptionCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            PreferenceHelper.setSubmitButtonEnableOption(newValue);
            // show "restart to take effect" if current option is different from the initial one
            warningLabel.setVisible(newValue != submitButtonEnableOption);
        });

        GridPane.setMargin(submitButtonEnableOptionCheckBox, new Insets(MINIMUM_MARGIN));
        gridPane.add(submitButtonEnableOptionCheckBox, 0, 1);

        setContent(gridPane);

    }

    /**
     * Update usage label text with usage count.
     */
    public final void updateUsageLabelText() {
        usageLabel.setText("Monthly Usage:  " + PreferenceHelper.getUsageCount());
    }

}
