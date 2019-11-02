import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


class CopiedButton extends Button {

    private static final Color COLOR = new Color(0.2392, 0.5765, 0.9686, 1);
    private static final CornerRadii RADII = new CornerRadii(14);

    CopiedButton(String text) {

        this.setText(text);

        this.setPrefHeight(24);

        this.setTextFill(Color.WHITE);

        this.setFont(Font.font("Arial Black", FontWeight.BOLD, 10));

        this.setBackground(new Background(new BackgroundFill(COLOR, RADII, Insets.EMPTY)));

    }

}
