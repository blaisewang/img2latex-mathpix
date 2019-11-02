import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


class FrontGridPane extends GridPane {

    private static Label resultText = new Label("LaTeX");
    private static CopiedButton copiedButton = new CopiedButton("COPIED");
    private static PressCopyTextField latexStyledResultTextField = new PressCopyTextField();
    private static PressCopyTextField textResultTextField = new PressCopyTextField();
    private static PressCopyTextField nNBlockResultTextField = new PressCopyTextField();
    private static PressCopyTextField nBlockResultTextField = new PressCopyTextField();

    private static final Color frontBackgroundColor = new Color(0.9725, 0.9765, 0.9804, 1);
    private static final BackgroundFill frontFill = new BackgroundFill(frontBackgroundColor, CornerRadii.EMPTY, Insets.EMPTY);
    private static final Background frontPaneBackground = new Background(frontFill);

    FrontGridPane(int itemMargin, int textMargin, BorderStroke borderStroke) {

        this.setBorder(new Border(borderStroke));
        this.setBackground(frontPaneBackground);
        this.setPadding(new Insets(itemMargin));
        this.setVgap(5);
        this.setHgap(2);

        resultText.setFont(Font.font(12));
        resultText.setTextFill(new Color(0.149, 0.149, 0.149, 1));
        GridPane.setMargin(resultText, new Insets(0, 0, textMargin, 0));
        this.add(resultText, 0, 0);

        GridPane.setMargin(copiedButton, new Insets(0, 0, 0, itemMargin));
        copiedButton.setVisible(false);
        this.add(copiedButton, 1, 1);

        latexStyledResultTextField.setOnMouseClicked(event -> {

            if (latexStyledResultTextField.getLength() > 0) {

                copiedButton.setVisible(true);
                GridPane.setRowIndex(copiedButton, 1);

            }

        });

        this.add(latexStyledResultTextField, 0, 1);

        textResultTextField.setOnMouseClicked(event -> {

            if (textResultTextField.getLength() > 0) {

                copiedButton.setVisible(true);
                GridPane.setRowIndex(copiedButton, 2);

            }

        });

        this.add(textResultTextField, 0, 2);

        nNBlockResultTextField.setOnMouseClicked(event -> {

            if (nNBlockResultTextField.getLength() > 0) {

                copiedButton.setVisible(true);
                GridPane.setRowIndex(copiedButton, 3);

            }

        });

        this.add(nNBlockResultTextField, 0, 3);

        nBlockResultTextField.setOnMouseClicked(event -> {

            if (nBlockResultTextField.getLength() > 0) {

                copiedButton.setVisible(true);
                GridPane.setRowIndex(copiedButton, 4);

            }

        });

        this.add(nBlockResultTextField, 0, 4);

    }

    CopiedButton getCopiedButton() {

        return copiedButton;

    }

    PressCopyTextField getLatexStyledResultTextField() {

        return latexStyledResultTextField;

    }

    PressCopyTextField getTextResultTextField() {

        return textResultTextField;

    }


    PressCopyTextField getNNBlockResultTextField() {

        return nNBlockResultTextField;

    }

    PressCopyTextField getNBlockResultTextField() {

        return nBlockResultTextField;

    }

    void setCopiedButtonRowIndex() {

        copiedButton.setVisible(true);
        GridPane.setRowIndex(copiedButton, 1);

    }

}
