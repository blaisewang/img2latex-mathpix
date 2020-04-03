package ui;

import javafx.scene.control.Button;
import javafx.scene.text.Font;


/**
 * UI.CopyResultButton.java
 * javafx.scene.control.Button with customised border stroke and layout style.
 * Used to trigger copy text to clipboard event.
 */
public class CopyResultButton extends Button {

    private String result;

    public CopyResultButton(String text) {

        result = "";

        setText(text);
        setFont(Font.font(12));
        setBackground(null);
        setTextFill(UIUtils.DARK_GREY);

        // text colour turns to light blue when mouse entered
        setOnMouseEntered(event -> setTextFill(UIUtils.LIGHT_BLUE));
        // text colour turns to black when mouse exited
        setOnMouseExited(event -> setTextFill(UIUtils.DARK_GREY));

        setOnMouseReleased(event -> UIUtils.putStringIntoClipboard(result));

    }

    /**
     * @param result result to be set.
     */
    public void setResult(String result) {
        this.result = result;
    }

}
