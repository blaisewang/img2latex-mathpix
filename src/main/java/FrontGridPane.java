import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


/**
 * FrontGridPane.java
 * Used for display recognised results.
 * The front grid panel has 1 Label, 1 CopiedButton and 4 PressCopyTextFields.
 */
class FrontGridPane extends GridPane {

    private static Label resultText = new Label("LaTeX");
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
     * @param textMargin   margin between text and item.
     * @param borderStroke customised border stroke, same as in BackGridPane.
     */
    FrontGridPane(int itemMargin, int textMargin, BorderStroke borderStroke) {

        this.setBorder(new Border(borderStroke));
        this.setBackground(frontPaneBackground);
        this.setPadding(new Insets(itemMargin));

        // 5 * 2 grid layout
        this.setVgap(5);
        this.setHgap(2);

        // "LaTeX" label text
        resultText.setFont(Font.font(12));
        resultText.setTextFill(new Color(0.149, 0.149, 0.149, 1));
        GridPane.setMargin(resultText, new Insets(0, 0, textMargin, 0));
        this.add(resultText, 0, 0);

        // CopiedButton to indicate which result on the left side has been copied
        GridPane.setMargin(copiedButton, new Insets(0, 0, 0, itemMargin));
        // default invisible
        copiedButton.setVisible(false);
        this.add(copiedButton, 1, 1);

        // mouse clicked event
        latexStyledResult.setOnMouseClicked(event -> {
            if (latexStyledResult.getLength() > 0) {
                // CopiedButton shows at (1, 1)
                copiedButton.setVisible(true);
                GridPane.setRowIndex(copiedButton, 1);
            }
        });
        this.add(latexStyledResult, 0, 1);

        // mouse clicked event
        textResult.setOnMouseClicked(event -> {
            if (textResult.getLength() > 0) {
                // CopiedButton shows at (1, 2)
                copiedButton.setVisible(true);
                GridPane.setRowIndex(copiedButton, 2);
            }
        });
        this.add(textResult, 0, 2);

        // mouse clicked event
        notNumberedBlockModeResult.setOnMouseClicked(event -> {
            if (notNumberedBlockModeResult.getLength() > 0) {
                // CopiedButton shows at (1, 3)
                copiedButton.setVisible(true);
                GridPane.setRowIndex(copiedButton, 3);
            }
        });
        this.add(notNumberedBlockModeResult, 0, 3);

        // mouse clicked event
        numberedBlockModeResult.setOnMouseClicked(event -> {
            if (numberedBlockModeResult.getLength() > 0) {
                // CopiedButton shows at (1, 4)
                copiedButton.setVisible(true);
                GridPane.setRowIndex(copiedButton, 4);
            }
        });
        this.add(numberedBlockModeResult, 0, 4);

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
