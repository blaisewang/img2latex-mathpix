package ui;

import io.IOUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * UI.FormattingTab.java
 * Used to display and edit second and third formatting options in the preferences panel.
 */
public class FormattingTab extends Tab {

    private static final int PANEL_MARGIN = 20;
    private static final int MINIMUM_MARGIN = 5;

    private static final String ORIGINAL_RESULT = "\\( e^{\\pi} \\)";

    public FormattingTab() {

        // tab header
        setText(" Formatting ");
        // non-closable
        setClosable(false);

        // 4 * 4 layout
        var gridPane = new GridPane();
        gridPane.setHgap(4);
        gridPane.setVgap(4);
        gridPane.setPadding(new Insets(PANEL_MARGIN, PANEL_MARGIN + MINIMUM_MARGIN, PANEL_MARGIN, PANEL_MARGIN));

        var secondResult = new Label("");
        secondResult.setFont(Font.font(14));
        GridPane.setMargin(secondResult, new Insets(MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));
        gridPane.add(secondResult, 0, 0, 4, 1);

        var secondFormattingOptionList = Arrays.asList(
                new RadioButton("\\begin{equation*}.."),
                new RadioButton("\\begin{align*}.."),
                new RadioButton("$$ .. $$"),
                new RadioButton("\\[ .. \\]")
        );

        // load initial second formatting option
        var secondOption = new AtomicInteger(IOUtils.getSecondResultFormattingOption());
        secondFormattingOptionList.get(secondOption.get()).setSelected(true);

        // toggle changed listener
        final var secondFormattingOptions = new ToggleGroup();
        secondFormattingOptions.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            secondOption.set(secondFormattingOptions.getToggles().indexOf(secondFormattingOptions.getSelectedToggle()));
            IOUtils.setSecondResultFormattingOption(secondOption.get());
            secondResult.setText(IOUtils.secondResultFormatter(ORIGINAL_RESULT).replace("\n", ""));
        });

        // add all radio buttons to the same toggle group
        for (var i = 0; i < secondFormattingOptionList.size(); i++) {
            var radioButton = secondFormattingOptionList.get(i);
            radioButton.setToggleGroup(secondFormattingOptions);
            GridPane.setMargin(radioButton, new Insets(0, MINIMUM_MARGIN, 3 * MINIMUM_MARGIN, MINIMUM_MARGIN));
            gridPane.add(radioButton, i, 1);
        }

        // initial third example result
        var thirdResult = new Label("");
        thirdResult.setFont(Font.font(14));
        GridPane.setMargin(thirdResult, new Insets(MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));
        gridPane.add(thirdResult, 0, 2, 4, 1);

        var thirdFormattingOptionList = Arrays.asList(
                new RadioButton("\\begin{equation}.."),
                new RadioButton("\\begin{align}.."),
                new RadioButton("$ .. $")
        );

        // load initial third formatting option
        var thirdOption = new AtomicInteger(IOUtils.getThirdResultFormattingOption());
        thirdFormattingOptionList.get(thirdOption.get()).setSelected(true);

        // toggle changed listener
        final var thirdFormattingOptions = new ToggleGroup();
        thirdFormattingOptions.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            thirdOption.set(thirdFormattingOptions.getToggles().indexOf(thirdFormattingOptions.getSelectedToggle()));
            IOUtils.setThirdFormattingOption(thirdOption.get());
            thirdResult.setText(IOUtils.thirdResultFormatter(ORIGINAL_RESULT).replace("\n", ""));
        });

        // add all radio buttons to the same toggle group
        for (var i = 0; i < thirdFormattingOptionList.size(); i++) {
            var radioButton = thirdFormattingOptionList.get(i);
            radioButton.setToggleGroup(thirdFormattingOptions);
            GridPane.setMargin(radioButton, new Insets(0, MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN));
            gridPane.add(radioButton, i, 3);
        }

        setContent(gridPane);

    }

}
