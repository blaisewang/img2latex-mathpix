import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


class PressCopyTextField extends TextField {

    private String formattedText = "";

    private static final Color COLOR = new Color(0.8471, 0.851, 0.8784, 1);
    private static final BorderWidths BORDER_WIDTHS = new BorderWidths(1, 1, 1, 1);
    private static final CornerRadii CORNER_RADII = new CornerRadii(2);
    private static final BorderStroke BORDER_STROKE = new BorderStroke(COLOR, BorderStrokeStyle.SOLID, CORNER_RADII, BORDER_WIDTHS);

    PressCopyTextField() {

        this.setPrefWidth(300);

        this.setEditable(false);

        this.setFocusTraversable(false);

        this.setBorder(new Border(BORDER_STROKE));

        this.setFont(Font.font(16));

        this.setStyle("-fx-text-inner-color: black;");

        this.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> this.setStyle("-fx-text-inner-color: #3d93f7;"));

        this.addEventHandler(MouseEvent.MOUSE_EXITED, event -> this.setStyle("-fx-text-inner-color: black;"));

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> Utilities.putStringIntoClipboard(this.getFormattedText()));

    }

    private String getFormattedText() {

        return this.formattedText;

    }

    final void setFormattedText(String text) {

        this.setText(text);

        this.formattedText = text;

    }

    @Override
    public void copy() {

        if (getLength() > getSelectedText().length()) {

            super.copy();

        } else {

            Utilities.putStringIntoClipboard(getFormattedText());

        }

    }

}
