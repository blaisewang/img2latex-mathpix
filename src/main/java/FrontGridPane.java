import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


/**
 * FrontGridPane.java
 * Used for display recognised results.
 * The front grid panel has 1 Label, 1 CopiedButton and 4 PressCopyTextFields.
 */
class FrontGridPane extends GridPane {

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
     * @param borderStroke customised border stroke, same as in BackGridPane.
     */
    FrontGridPane(int itemMargin, BorderStroke borderStroke) {

        this.setBorder(new Border(borderStroke));
        this.setBackground(frontPaneBackground);
        this.setPadding(new Insets(itemMargin, 0, itemMargin, 0));

        // 5 * 2 grid layout
        this.setVgap(5);
        this.setHgap(2);

        // add "Result" textL label
        Label resultTextLabel = Utilities.getTextLabel("Result");
        Utilities.setNodeLeftMargin(resultTextLabel, itemMargin);
        this.add(resultTextLabel, 0, 0);

        // used to indicate which result on the left side has been copied, default invisible
        copiedButton.setVisible(false);
        Utilities.setNodeLeftMargin(copiedButton, itemMargin / 2);
        this.add(copiedButton, 1, 1);

        // add latexStyledResult
        Utilities.setNodeLeftMargin(latexStyledResult, itemMargin);
        setTextFieldEvent(latexStyledResult, 1);
        this.add(latexStyledResult, 0, 1);

        // add textResult
        Utilities.setNodeLeftMargin(textResult, itemMargin);
        setTextFieldEvent(textResult, 2);
        this.add(textResult, 0, 2);

        // add notNumberedBlockModeResult
        Utilities.setNodeLeftMargin(notNumberedBlockModeResult, itemMargin);
        setTextFieldEvent(notNumberedBlockModeResult, 3);
        this.add(notNumberedBlockModeResult, 0, 3);

        // add numberedBlockModeResult
        Utilities.setNodeLeftMargin(numberedBlockModeResult, itemMargin);
        setTextFieldEvent(numberedBlockModeResult, 4);
        this.add(numberedBlockModeResult, 0, 4);

    }

    /**
     * Method to add a click event to a PressCopyTextField
     *
     * @param textField TextField to add the click event
     * @param rowIndex  row index of CopiedButton showed
     */
    private void setTextFieldEvent(PressCopyTextField textField, int rowIndex) {

        // mouse clicked event
        textField.setOnMouseClicked(event -> {
            if (textField.getLength() > 0) {
                // CopiedButton shows at (1, rowIndex)
                copiedButton.setVisible(true);
                GridPane.setRowIndex(copiedButton, rowIndex);
            }
        });

    }

    /**
     * @return CopiedButton object used to be controlled by BackGridPane event.
     */
    CopiedButton getCopiedButton() {
        return copiedButton;
    }

    /**
     * @return LaTeX styled result TextField.
     */
    PressCopyTextField getLatexStyledResult() {
        return latexStyledResult;
    }

    /**
     * @return text result TextField.
     */
    PressCopyTextField getTextResult() {
        return textResult;
    }

    /**
     * @return not numbered block mode result TextField.
     */
    PressCopyTextField getNotNumberedBlockModeResult() {
        return notNumberedBlockModeResult;
    }

    /**
     * @return numbered block mode result TextField.
     */
    PressCopyTextField getNumberedBlockModeResult() {
        return numberedBlockModeResult;
    }

    /**
     * Method to set the row index of CopiedButton in BackGridPane
     */
    void setCopiedButtonRowIndex() {
        copiedButton.setVisible(true);
        GridPane.setRowIndex(copiedButton, 1);
    }

}
