package ui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.List;


/**
 * UI.FrontGridPane.java
 * Used for display recognised results.
 * The front grid panel has 1 Label, 1 UI.CopiedButton and 4 PressCopyTextFields.
 */
public class FrontGridPane extends GridPane {

    private static final CopiedButton COPIED_BUTTON = new CopiedButton();
    private static final CopyResultButton COPY_TSV_BUTTON = new CopyResultButton("TSV");
    private static final CopyResultButton COPY_MATH_ML_BUTTON = new CopyResultButton("MathML");
    private static final PressCopyTextField FIRST_PRESS_COPY_TEXT_FIELD = new PressCopyTextField();
    private static final PressCopyTextField SECOND_PRESS_COPY_TEXT_FIELD = new PressCopyTextField();
    private static final PressCopyTextField THIRD_PRESS_COPY_TEXT_FIELD = new PressCopyTextField();

    private static final Color BACKGROUND_COLOR = new Color(0.9725, 0.9765, 0.9804, 1);
    private static final BackgroundFill BACKGROUND_FILL = new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY);
    private static final Background BACKGROUND = new Background(BACKGROUND_FILL);

    /**
     * @param itemMargin   margin between items.
     * @param borderStroke customised border stroke, same as in UI.BackGridPane.
     */
    public FrontGridPane(int itemMargin, BorderStroke borderStroke) {

        setBorder(new Border(borderStroke));
        setBackground(BACKGROUND);
        setPadding(new Insets(itemMargin, 0, itemMargin, 0));

        // 4 * 3 grid layout
        setVgap(4);
        setHgap(3);

        // add "Result" textL label
        var resultTextLabel = UIUtils.getTextLabel("Result");
        UIUtils.setDefaultNodeMargin(resultTextLabel, itemMargin, 0);
        add(resultTextLabel, 0, 0);

        // copy TSV result to clipboard event
        COPY_TSV_BUTTON.setVisible(false);
        GridPane.setHalignment(COPY_TSV_BUTTON, HPos.RIGHT);
        UIUtils.setDefaultNodeMargin(COPY_TSV_BUTTON, 0, itemMargin);
        add(COPY_TSV_BUTTON, 1, 0);

        // copy MathML result to clipboard event
        COPY_MATH_ML_BUTTON.setVisible(false);
        GridPane.setHalignment(COPY_MATH_ML_BUTTON, HPos.RIGHT);
        UIUtils.setDefaultNodeMargin(COPY_MATH_ML_BUTTON, 0, 0);
        add(COPY_MATH_ML_BUTTON, 2, 0);

        // used to indicate which result on the left side has been copied, default invisible
        COPIED_BUTTON.setVisible(false);
        UIUtils.setDefaultNodeMargin(COPIED_BUTTON, itemMargin / 2, 0);
        add(COPIED_BUTTON, 2, 1);

        // add the first PressCopyTextField
        UIUtils.setDefaultNodeMargin(FIRST_PRESS_COPY_TEXT_FIELD, itemMargin, 0);
        setTextFieldEvent(FIRST_PRESS_COPY_TEXT_FIELD, 1);
        add(FIRST_PRESS_COPY_TEXT_FIELD, 0, 1, 2, 1);

        // add the second PressCopyTextField
        UIUtils.setDefaultNodeMargin(SECOND_PRESS_COPY_TEXT_FIELD, itemMargin, 0);
        setTextFieldEvent(SECOND_PRESS_COPY_TEXT_FIELD, 2);
        add(SECOND_PRESS_COPY_TEXT_FIELD, 0, 2, 2, 1);

        // add the third PressCopyTextField
        UIUtils.setDefaultNodeMargin(THIRD_PRESS_COPY_TEXT_FIELD, itemMargin, 0);
        setTextFieldEvent(THIRD_PRESS_COPY_TEXT_FIELD, 3);
        add(THIRD_PRESS_COPY_TEXT_FIELD, 0, 3, 2, 1);

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
                COPIED_BUTTON.setVisible(true);
                GridPane.setRowIndex(COPIED_BUTTON, rowIndex);
            }
        });

    }

    /**
     * @return UI.CopiedButton object used to be controlled by UI.BackGridPane event.
     */
    public CopiedButton getCopiedButton() {
        return COPIED_BUTTON;
    }

    /**
     * @return UI.CopyTSVButton object used to be controlled by UI.BackGridPane event.
     */
    public CopyResultButton getCopyTSVButton() {
        return COPY_TSV_BUTTON;
    }

    /**
     * @return UI.CopyMathMLButton object used to be controlled by UI.BackGridPane event.
     */
    public CopyResultButton getCopyMathMLButton() {
        return COPY_MATH_ML_BUTTON;
    }

    /**
     * @return first PressCopyTextField.
     */
    public PressCopyTextField getFirstPressCopyTextField() {
        return FIRST_PRESS_COPY_TEXT_FIELD;
    }

    /**
     * @return second PressCopyTextField.
     */
    public PressCopyTextField getSecondPressCopyTextField() {
        return SECOND_PRESS_COPY_TEXT_FIELD;
    }

    /**
     * @return third PressCopyTextField.
     */
    public PressCopyTextField getThirdPressCopyTextField() {
        return THIRD_PRESS_COPY_TEXT_FIELD;
    }

    /**
     * Method to set the row index of UI.CopiedButton in UI.BackGridPane.
     */
    public void setCopiedButtonRowIndex() {
        COPIED_BUTTON.setVisible(true);
        GridPane.setRowIndex(COPIED_BUTTON, 1);
    }

    /**
     * Method to set the column index of UI.CopyResultButton in UI.BackGridPane.
     */
    public void setCopyResultButtonColumnIndex(List<CopyResultButton> buttonList) {

        var columnIndex = 2;

        for (CopyResultButton copyResultButton : buttonList) {
            copyResultButton.setVisible(true);
            GridPane.setColumnIndex(copyResultButton, columnIndex);
            columnIndex -= 1;
        }

    }

}
