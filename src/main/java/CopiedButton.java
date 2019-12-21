import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 * CopiedButton.java
 * javafx.scene.control.Button with customised border stroke and layout style.
 * Used to indicate which result has been copied.
 */
class CopiedButton extends Button {

    private static final CornerRadii RADII = new CornerRadii(14);
    private static final Color COLOR = new Color(0.2392, 0.5765, 0.9686, 1);

    CopiedButton(String text) {

        this.setText(text);
        this.setPrefHeight(20);
        // white text colour
        this.setTextFill(Color.WHITE);
        this.setFont(Font.font("Arial Black", FontWeight.BOLD, 10));
        // blue background
        this.setBackground(new Background(new BackgroundFill(COLOR, RADII, Insets.EMPTY)));

    }

}
