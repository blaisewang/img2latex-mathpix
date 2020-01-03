import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


/**
 * PressCopyTextField.java
 * Extension of javafx.scene.control.TextField.
 * With customised border stroke and click-to-copy function.
 */
class PressCopyTextField extends TextField {

    // variable to keep the original text format such as "\n"
    private String formattedText = "";

    private static final CornerRadii CORNER_RADII = new CornerRadii(2);
    private static final Color COLOR = new Color(0.8471, 0.851, 0.8784, 1);
    private static final BorderWidths BORDER_WIDTHS = new BorderWidths(1, 1, 1, 1);
    private static final BorderStroke BORDER_STROKE = new BorderStroke(COLOR, BorderStrokeStyle.SOLID, CORNER_RADII, BORDER_WIDTHS);

    PressCopyTextField() {

        this.setPrefWidth(220);
        this.setPrefHeight(20);

        // PressCopyTextField is not editable
        this.setEditable(false);

        this.setBorder(new Border(BORDER_STROKE));

        this.setFont(Font.font(14));

        // initialise with black text colour
        this.setStyle("-fx-text-inner-color: black;");

        // text colour turns to light blue when mouse enters the PressCopyTextField
        this.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> this.setStyle("-fx-text-inner-color: #3d93f7;"));

        // text colour turns back to black when mouse exits the PressCopyTextField
        this.addEventHandler(MouseEvent.MOUSE_EXITED, event -> this.setStyle("-fx-text-inner-color: black;"));

        // put the formatted text into clipboard when the PressCopyTextField is clicked
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (getLength() > 0) {
                // copy text if there is text
                Utilities.putStringIntoClipboard(this.formattedText);
            }
        });

    }

    /**
     * @param text text to be displayed and copied.
     */
    final void setFormattedText(String text) {
        // display text without formatted by the supertype method
        this.setText(text);
        // store the formatted text in this object
        this.formattedText = text;
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
            Utilities.putStringIntoClipboard(this.formattedText);
        }
    }

}
