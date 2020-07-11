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
public final class CopiedButton extends Button {

    private static final CornerRadii RADII = new CornerRadii(14);

    public CopiedButton() {

        setText("COPIED");
        setPrefHeight(20);
        // white text colour
        setTextFill(Color.WHITE);
        setFont(Font.font("Arial Black", FontWeight.BOLD, 10));
        // blue background
        setBackground(new Background(new BackgroundFill(UIUtils.LIGHT_BLUE, RADII, Insets.EMPTY)));

    }

}
