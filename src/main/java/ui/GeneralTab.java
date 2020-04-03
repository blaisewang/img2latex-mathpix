package ui;

import io.IOUtils;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * UI.GeneralTab.java
 * Used to display and edit third and fourth formatting options in the preferences panel.
 */
public class GeneralTab extends Tab {

    private static final int PANEL_MARGIN = 20;
    private static final int MINIMUM_MARGIN = 5;

    private static final String ORIGINAL_RESULT = "e^{\\pi}";

    public GeneralTab() {

        // tab header
        setText(" General ");
        // non-closable
        setClosable(false);

        // 5 * 4 layout
        var gridPane = new GridPane();
        gridPane.setHgap(6);
        gridPane.setVgap(4);
        gridPane.setPadding(new Insets(PANEL_MARGIN, PANEL_MARGIN + MINIMUM_MARGIN, PANEL_MARGIN, PANEL_MARGIN));

        // load initial improved OCR enable option
        var improvedOCREnableOption = IOUtils.getImprovedOCREnableOption();

        var improvedOCREnableOptionCheckBox = new CheckBox("Improved OCR");
        improvedOCREnableOptionCheckBox.setSelected(improvedOCREnableOption);
        improvedOCREnableOptionCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> IOUtils.setImprovedOCREnableOption(newValue));
        GridPane.setMargin(improvedOCREnableOptionCheckBox, new Insets(0, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));
        gridPane.add(improvedOCREnableOptionCheckBox, 0, 0, 4, 1);

        // add header label
        var headerLabel = new Label("Formatting");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setMargin(headerLabel, new Insets(MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));
        gridPane.add(headerLabel, 0, 1, 4, 1);

        // initial third example result
        var initialThirdResult = IOUtils.thirdResultFormatter("\\( " + ORIGINAL_RESULT + " \\)").replace("\n", "");
        var thirdResult = new Label(initialThirdResult);
        thirdResult.setFont(Font.font(14));
        GridPane.setMargin(thirdResult, new Insets(MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));
        gridPane.add(thirdResult, 0, 2, 4, 1);

        var thirdFormattingOptionList = Arrays.asList(
                new RadioButton("\\begin{equation*}.."),
                new RadioButton("\\begin{align*}.."),
                new RadioButton("$$ .. $$"),
                new RadioButton("\\[ .. \\]")
        );

        // load initial third formatting option
        var thirdOption = new AtomicInteger(IOUtils.getThirdResultFormattingOption());
        thirdFormattingOptionList.get(thirdOption.get()).setSelected(true);

        // toggle changed listener
        final var thirdFormattingOptions = new ToggleGroup();
        thirdFormattingOptions.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            thirdOption.set(thirdFormattingOptions.getToggles().indexOf(thirdFormattingOptions.getSelectedToggle()));
            IOUtils.setThirdResultFormattingOption(thirdOption.get());
            thirdResult.setText(IOUtils.thirdResultFormatter("\\( " + ORIGINAL_RESULT + " \\)").replace("\n", ""));
        });

        // add all radio buttons to the same toggle group
        for (var i = 0; i < thirdFormattingOptionList.size(); i++) {
            var radioButton = thirdFormattingOptionList.get(i);
            radioButton.setToggleGroup(thirdFormattingOptions);
            GridPane.setMargin(radioButton, new Insets(0, MINIMUM_MARGIN, 3 * MINIMUM_MARGIN, MINIMUM_MARGIN));
            gridPane.add(radioButton, i, 3);
        }

        // initial fourth example result
        var initialFourthResult = IOUtils.fourthResultFormatter("\\( " + ORIGINAL_RESULT + " \\)").replace("\n", "");
        var fourthResult = new Label(initialFourthResult);
        fourthResult.setFont(Font.font(14));
        GridPane.setMargin(fourthResult, new Insets(MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));
        gridPane.add(fourthResult, 0, 4, 4, 1);

        var fourthFormattingOptionList = Arrays.asList(
                new RadioButton("\\begin{equation}.."),
                new RadioButton("\\begin{align}.."),
                new RadioButton("$ .. $")
        );

        // load initial fourth formatting option
        var fourthOption = new AtomicInteger(IOUtils.getFourthResultFormattingOption());
        fourthFormattingOptionList.get(fourthOption.get()).setSelected(true);

        // toggle changed listener
        final var fourthFormattingOptions = new ToggleGroup();
        fourthFormattingOptions.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            fourthOption.set(fourthFormattingOptions.getToggles().indexOf(fourthFormattingOptions.getSelectedToggle()));
            IOUtils.setFourthResultFormattingOption(fourthOption.get());
            fourthResult.setText(IOUtils.fourthResultFormatter("\\( " + ORIGINAL_RESULT + " \\)").replace("\n", ""));
        });

        // add all radio buttons to the same toggle group
        for (var i = 0; i < fourthFormattingOptionList.size(); i++) {
            var radioButton = fourthFormattingOptionList.get(i);
            radioButton.setToggleGroup(fourthFormattingOptions);
            GridPane.setMargin(radioButton, new Insets(0, MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN));
            gridPane.add(radioButton, i, 5);
        }

        setContent(gridPane);

    }

}
