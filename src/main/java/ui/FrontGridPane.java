package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;


/**
 * UI.FrontGridPane.java
 * Used for display recognised results.
 * The front grid panel has 1 Label, 1 UI.CopiedButton and 4 PressCopyTextFields.
 */
public class FrontGridPane extends GridPane {

    private static CopiedButton copiedButton = new CopiedButton("COPIED");
    private static PressCopyTextField latexStyledResult = new PressCopyTextField();
    private static PressCopyTextField textResult = new PressCopyTextField();
    private static PressCopyTextField notNumberedBlockModeResult = new PressCopyTextField();
    private static PressCopyTextField numberedBlockModeResult = new PressCopyTextField();

    private static final Color frontBackgroundColor = new Color(0.9725, 0.9765, 0.9804, 1);
    private static final BackgroundFill frontFill = new BackgroundFill(frontBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
    private static final Background frontPaneBackground = new Background(frontFill);

    /**
     * @param itemMargin   margin between items.
     * @param borderStroke customised border stroke, same as in UI.BackGridPane.
     */
    public FrontGridPane(int itemMargin, BorderStroke borderStroke) {

        this.setBorder(new Border(borderStroke));
        this.setBackground(frontPaneBackground);
        this.setPadding(new Insets(itemMargin, 0, itemMargin, 0));

        // 5 * 2 grid layout
        this.setVgap(5);
        this.setHgap(2);

        // add "Result" textL label
        Label resultTextLabel = UIUtils.getTextLabel("Result");
        UIUtils.setDefaultNodeMargin(resultTextLabel, itemMargin, 0);
        this.add(resultTextLabel, 0, 0);

        // used to indicate which result on the left side has been copied, default invisible
        copiedButton.setVisible(false);
        UIUtils.setDefaultNodeMargin(copiedButton, itemMargin / 2, 0);
        this.add(copiedButton, 1, 1);

        // add latexStyledResult
        UIUtils.setDefaultNodeMargin(latexStyledResult, itemMargin, 0);
        setTextFieldEvent(latexStyledResult, 1);
        this.add(latexStyledResult, 0, 1);

        // add textResult
        UIUtils.setDefaultNodeMargin(textResult, itemMargin, 0);
        setTextFieldEvent(textResult, 2);
        this.add(textResult, 0, 2);

        // add notNumberedBlockModeResult
        UIUtils.setDefaultNodeMargin(notNumberedBlockModeResult, itemMargin, 0);
        setTextFieldEvent(notNumberedBlockModeResult, 3);
        this.add(notNumberedBlockModeResult, 0, 3);

        // add numberedBlockModeResult
        UIUtils.setDefaultNodeMargin(numberedBlockModeResult, itemMargin, 0);
        setTextFieldEvent(numberedBlockModeResult, 4);
        this.add(numberedBlockModeResult, 0, 4);

    }

    /**
     * Method to add a click event to a UI.PressCopyTextField.
     *
     * @param textField TextField to add the click event.
     * @param rowIndex  row index of UI.CopiedButton showed.
     */
    private void setTextFieldEvent(PressCopyTextField textField, int rowIndex) {

        // mouse clicked event
        textField.setOnMouseClicked(event -> {
            if (textField.getLength() > 0) {
                // UI.CopiedButton shows at (1, rowIndex)
                copiedButton.setVisible(true);
                GridPane.setRowIndex(copiedButton, rowIndex);
            }
        });

    }

    /**
     * @return UI.CopiedButton object used to be controlled by UI.BackGridPane event.
     */
    public CopiedButton getCopiedButton() {
        return copiedButton;
    }

    /**
     * @return LaTeX styled result TextField.
     */
    public PressCopyTextField getLatexStyledResult() {
        return latexStyledResult;
    }

    /**
     * @return text result TextField.
     */
    public PressCopyTextField getTextResult() {
        return textResult;
    }

    /**
     * @return not numbered block mode result TextField.
     */
    public PressCopyTextField getNotNumberedBlockModeResult() {
        return notNumberedBlockModeResult;
    }

    /**
     * @return numbered block mode result TextField.
     */
    public PressCopyTextField getNumberedBlockModeResult() {
        return numberedBlockModeResult;
    }

    /**
     * Method to set the row index of UI.CopiedButton in UI.BackGridPane.
     */
    public void setCopiedButtonRowIndex() {
        copiedButton.setVisible(true);
        GridPane.setRowIndex(copiedButton, 1);
    }

}
