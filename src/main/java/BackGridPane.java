import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


class BackGridPane extends GridPane {

    private static final int PREFERRED_WIDTH = 390;
    private static final int PREFERRED_MARGIN = 10;
    private static final int TEXT_MARGIN = 5;

    private static Label imageViewText = new Label("Clipboard Image");
    private static ImageView clipboardImageView = new ImageView();
    private static BorderPane borderPane = new BorderPane(clipboardImageView);
    private static Button submitButton = new Button("Submit");
    private static Label confidenceText = new Label("Confidence");
    private static ProgressBar confidenceProgressBar = new ProgressBar(0);

    private static final Color PANE_BORDER_COLOR = new Color(0.898, 0.902, 0.9216, 1);
    private static final BorderWidths PANE_BORDER_WIDTHS = new BorderWidths(1, 0, 1, 0);
    private static final BorderStroke PANE_BORDER_STROKE = new BorderStroke(PANE_BORDER_COLOR, BorderStrokeStyle.SOLID, null, PANE_BORDER_WIDTHS);

    private static final BackgroundFill BACKGROUND_FILL = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
    private static final Background BACKGROUND = new Background(BACKGROUND_FILL);

    private static FrontGridPane frontGridPane = new FrontGridPane(PREFERRED_MARGIN, TEXT_MARGIN, PANE_BORDER_STROKE);

    private static CopiedButton copiedButton = frontGridPane.getCopiedButton();
    private static PressCopyTextField latexStyledResultTextField = frontGridPane.getLatexStyledResultTextField();
    private static PressCopyTextField textResultTextField = frontGridPane.getTextResultTextField();
    private static PressCopyTextField nNBlockResultTextField = frontGridPane.getNNBlockResultTextField();
    private static PressCopyTextField nBlockResultTextField = frontGridPane.getNBlockResultTextField();

    BackGridPane() {

        this.setPadding(new Insets(PREFERRED_MARGIN, 0, PREFERRED_MARGIN, 0));
        this.setBackground(BACKGROUND);
        this.setVgap(6);
        this.setHgap(1);

        imageViewText.setFont(Font.font(12));
        GridPane.setMargin(imageViewText, new Insets(0, 0, TEXT_MARGIN, PREFERRED_MARGIN));
        imageViewText.setTextFill(new Color(0.149, 0.149, 0.149, 1));
        this.add(imageViewText, 0, 0);

        clipboardImageView.setPreserveRatio(true);
        clipboardImageView.setFitWidth(PREFERRED_WIDTH);
        clipboardImageView.setFitHeight(150);

        borderPane.setBorder(new Border(PANE_BORDER_STROKE));
        borderPane.setPrefSize(PREFERRED_WIDTH, 150);
        this.add(borderPane, 0, 1);

        submitButton.setFocusTraversable(false);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(TEXT_MARGIN, 0, TEXT_MARGIN, 0));
        submitButton.setOnMouseClicked(event -> {

            Image clipboardImage = clipboardImageView.getImage();

            if (clipboardImage != null) {

                copiedButton.setVisible(false);

                OCRRequest.Response response = Utilities.concurrentCall(clipboardImage);

                if (response != null) {

                    if (response.getError() != null) {

                        clearErrorImage();

                        Utilities.showErrorDialog(response.getError());

                        return;

                    }

                    Utilities.putStringIntoClipboard(response.getLatex_styled());
                    frontGridPane.setCopiedButtonRowIndex();

                    latexStyledResultTextField.setFormattedText(response.getLatex_styled());
                    textResultTextField.setFormattedText(response.getText());

                    if (response.is_not_math()) {
                        nNBlockResultTextField.setFormattedText(Utilities.addDoubleDollarWrapper(response.getLatex_styled()));
                    } else {
                        nNBlockResultTextField.setFormattedText(response.getText_display());
                    }

                    nBlockResultTextField.setFormattedText(Utilities.addEquationWrapper(response.getLatex_styled()));


                    double confidence = response.getLatex_confidence();

                    if (confidence > 0 && confidence < 0.01) {
                        confidence = 0.01;
                    }

                    confidenceProgressBar.setProgress(confidence);

                } else {

                    clearErrorImage();

                }

            } else {

                Utilities.showErrorDialog("No image found in the clipboard");

            }

        });

        this.add(submitButton, 0, 2);

        this.add(frontGridPane, 0, 3);

        confidenceText.setFont(Font.font(12));
        GridPane.setMargin(confidenceText, new Insets(TEXT_MARGIN, 0, TEXT_MARGIN, PREFERRED_MARGIN));
        confidenceText.setTextFill(new Color(0.149, 0.149, 0.149, 1));
        this.add(confidenceText, 0, 4);

        confidenceProgressBar.setPrefSize(PREFERRED_WIDTH - 2 * PREFERRED_MARGIN - 1, 20);
        GridPane.setMargin(confidenceProgressBar, new Insets(0, 0, 0, PREFERRED_MARGIN));

        confidenceProgressBar.progressProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.doubleValue() < 0.2) {

                setStyle("-fx-accent: #ec4d3d;");

            } else if (newValue.doubleValue() < 0.6) {

                setStyle("-fx-accent: #f8cd46;");

            } else {

                setStyle("-fx-accent: #63c956;");

            }

        });

        this.add(confidenceProgressBar, 0, 5);

    }

    private void clearErrorImage() {

        Utilities.putStringIntoClipboard("");

        clipboardImageView.setImage(null);

        latexStyledResultTextField.setText("");
        textResultTextField.setText("");
        nNBlockResultTextField.setText("");
        nBlockResultTextField.setText("");

        confidenceProgressBar.setProgress(0);

    }

    void setImageView(Image image) {

        clipboardImageView.setImage(image);

    }

}
