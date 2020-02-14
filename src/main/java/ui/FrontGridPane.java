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
    private static PressCopyTextField firstPressCopyTextField = new PressCopyTextField();
    private static PressCopyTextField secondPressCopyTextField = new PressCopyTextField();
    private static PressCopyTextField thirdPressCopyTextField = new PressCopyTextField();
    private static PressCopyTextField fourthPressCopyTextField = new PressCopyTextField();

    private static final Color frontBackgroundColor = new Color(0.9725, 0.9765, 0.9804, 1);
    private static final BackgroundFill frontFill = new BackgroundFill(frontBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
    private static final Background frontPaneBackground = new Background(frontFill);

    /**
     * @param itemMargin   margin between items.
     * @param borderStroke customised border stroke, same as in UI.BackGridPane.
     */
    public FrontGridPane(int itemMargin, BorderStroke borderStroke) {

        setBorder(new Border(borderStroke));
        setBackground(frontPaneBackground);
        setPadding(new Insets(itemMargin, 0, itemMargin, 0));

        // 5 * 2 grid layout
        setVgap(5);
        setHgap(2);

        // add "Result" textL label
        Label resultTextLabel = UIUtils.getTextLabel("Result");
        UIUtils.setDefaultNodeMargin(resultTextLabel, itemMargin, 0);
        add(resultTextLabel, 0, 0);

        // used to indicate which result on the left side has been copied, default invisible
        copiedButton.setVisible(false);
        UIUtils.setDefaultNodeMargin(copiedButton, itemMargin / 2, 0);
        add(copiedButton, 1, 1);

        // add the first PressCopyTextField
        UIUtils.setDefaultNodeMargin(firstPressCopyTextField, itemMargin, 0);
        setTextFieldEvent(firstPressCopyTextField, 1);
        add(firstPressCopyTextField, 0, 1);

        // add the second PressCopyTextField
        UIUtils.setDefaultNodeMargin(secondPressCopyTextField, itemMargin, 0);
        setTextFieldEvent(secondPressCopyTextField, 2);
        add(secondPressCopyTextField, 0, 2);

        // add the third PressCopyTextField
        UIUtils.setDefaultNodeMargin(thirdPressCopyTextField, itemMargin, 0);
        setTextFieldEvent(thirdPressCopyTextField, 3);
        add(thirdPressCopyTextField, 0, 3);

        // add the fourth PressCopyTextField
        UIUtils.setDefaultNodeMargin(fourthPressCopyTextField, itemMargin, 0);
        setTextFieldEvent(fourthPressCopyTextField, 4);
        add(fourthPressCopyTextField, 0, 4);

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
     * @return first PressCopyTextField.
     */
    public PressCopyTextField getFirstPressCopyTextField() {
        return firstPressCopyTextField;
    }

    /**
     * @return second PressCopyTextField.
     */
    public PressCopyTextField getSecondPressCopyTextField() {
        return secondPressCopyTextField;
    }

    /**
     * @return third PressCopyTextField.
     */
    public PressCopyTextField getThirdPressCopyTextField() {
        return thirdPressCopyTextField;
    }

    /**
     * @return fourth PressCopyTextField.
     */
    public PressCopyTextField getFourthPressCopyTextField() {
        return fourthPressCopyTextField;
    }

    /**
     * Method to set the row index of UI.CopiedButton in UI.BackGridPane.
     */
    public void setCopiedButtonRowIndex() {
        copiedButton.setVisible(true);
        GridPane.setRowIndex(copiedButton, 1);
    }

}
