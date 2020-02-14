package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 * UI.CopiedButton.java
 * javafx.scene.control.Button with customised border stroke and layout style.
 * Used to indicate which result has been copied.
 */
public class CopiedButton extends Button {

    private static final CornerRadii RADII = new CornerRadii(14);
    private static final Color COLOR = new Color(0.2392, 0.5765, 0.9686, 1);

    public CopiedButton(String text) {

        setText(text);
        setPrefHeight(20);
        // white text colour
        setTextFill(Color.WHITE);
        setFont(Font.font("Arial Black", FontWeight.BOLD, 10));
        // blue background
        setBackground(new Background(new BackgroundFill(COLOR, RADII, Insets.EMPTY)));

    }

}
