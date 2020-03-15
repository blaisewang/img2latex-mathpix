package ui;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


/**
 * UI.PressCopyTextField.java
 * Extension of javafx.scene.control.TextField.
 * With customised border stroke and click-to-copy function.
 */
public class PressCopyTextField extends TextField {

    // variable to keep the original text format such as "\n"
    private String formattedText = "";

    private static final CornerRadii CORNER_RADII = new CornerRadii(2);
    private static final Color COLOR = new Color(0.8471, 0.851, 0.8784, 1);
    private static final BorderWidths BORDER_WIDTHS = new BorderWidths(1, 1, 1, 1);
    private static final BorderStroke BORDER_STROKE = new BorderStroke(COLOR, BorderStrokeStyle.SOLID, CORNER_RADII, BORDER_WIDTHS);

    public PressCopyTextField() {

        setPrefWidth(220);
        setPrefHeight(20);

        // UI.PressCopyTextField is not editable
        setEditable(false);

        setBorder(new Border(BORDER_STROKE));

        setFont(Font.font(14));

        // initialise with black text colour
        setStyle("-fx-text-inner-color: black;");

        // text colour turns to light blue when mouse enters the UI.PressCopyTextField
        addEventHandler(MouseEvent.MOUSE_ENTERED, event -> setStyle("-fx-text-inner-color: #3d93f7;"));

        // text colour turns back to black when mouse exits the UI.PressCopyTextField
        addEventHandler(MouseEvent.MOUSE_EXITED, event -> setStyle("-fx-text-inner-color: black;"));

        // put the formatted text into clipboard when the UI.PressCopyTextField is clicked
        addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (getLength() > 0) {
                // copy text if there is text
                UIUtils.putStringIntoClipboard(formattedText);
            }
        });

        // moves the caret before the first char of the text
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(this::home);
            }
        });

    }

    /**
     * @param text text to be displayed and copied.
     */
    public final void setFormattedText(String text) {
        // display text without formatted by the supertype method
        setText(text.replace("\n", " "));
        // store the formatted text in this object
        formattedText = text;
    }

    /**
     * Extension to the supertype copy.
     * If the whole text is selected, the the formatted text will be copied.
     * Otherwise, text without formatted will be copied.
     */
    @Override
    public void copy() {
        // if text is partially selected
        if (getLength() > getSelectedText().length()) {
            super.copy();
        } else {
            // copy formatted text
            UIUtils.putStringIntoClipboard(formattedText);
        }
    }

}
