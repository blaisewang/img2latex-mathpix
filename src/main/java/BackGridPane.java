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


/**
 * BackGridPane.java
 * Used for display current clipboard image and confidence progressbar.
 * The back grid panel has 2 Labels, 1 ImageView with 1 BorderPane, 1 Button, and 1 ProgressBar.
 */
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

    // get components from FrontGridPane instance
    private static CopiedButton copiedButton = frontGridPane.getCopiedButton();
    private static PressCopyTextField latexStyledResult = frontGridPane.getLatexStyledResult();
    private static PressCopyTextField textResult = frontGridPane.getTextResult();
    private static PressCopyTextField notNumberedBlockModeResult = frontGridPane.getNotNumberedBlockModeResult();
    private static PressCopyTextField numberedBlockModeResult = frontGridPane.getNumberedBlockModeResult();

    /**
     * BackGridPane Initialisation.
     */
    BackGridPane() {

        this.setPadding(new Insets(PREFERRED_MARGIN, 0, PREFERRED_MARGIN, 0));
        this.setBackground(BACKGROUND);

        // 6 * 1 layout
        this.setVgap(6);
        this.setHgap(1);

        imageViewText.setFont(Font.font(12));
        GridPane.setMargin(imageViewText, new Insets(0, 0, TEXT_MARGIN, PREFERRED_MARGIN));
        imageViewText.setTextFill(new Color(0.149, 0.149, 0.149, 1));
        this.add(imageViewText, 0, 0);

        // preserve image ratio
        clipboardImageView.setPreserveRatio(true);
        // maximum width is 390 maximum height is 150
        // image larger than the above size will be scaled down
        clipboardImageView.setFitWidth(PREFERRED_WIDTH);
        clipboardImageView.setFitHeight(150);

        // use BorderPane to add a border stroke to the ImageView
        borderPane.setBorder(new Border(PANE_BORDER_STROKE));
        borderPane.setPrefSize(PREFERRED_WIDTH, 150);
        this.add(borderPane, 0, 1);

        // is not a part of focus traversal cycle
        submitButton.setFocusTraversable(false);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(TEXT_MARGIN, 0, TEXT_MARGIN, 0));
        submitButton.setOnMouseClicked(event -> {

            Image clipboardImage = clipboardImageView.getImage();

            if (clipboardImage != null) {

                // clear last location
                copiedButton.setVisible(false);

                OCRRequest.Response response = Utilities.concurrentCall(clipboardImage);

                // if response received
                if (response != null) {
                    // error occurred
                    if (response.getError() != null) {
                        // clear error image and last results
                        clearErrorImage();
                        // show error content with a alert dialog
                        Utilities.showErrorDialog(response.getError());

                        return;
                    }

                    // put default result into the system clipboard
                    Utilities.putStringIntoClipboard(response.getLatex_styled());
                    // set CopiedButton to the corresponded location
                    frontGridPane.setCopiedButtonRowIndex();

                    // set results to corresponded TextFields.
                    latexStyledResult.setFormattedText(response.getLatex_styled());
                    textResult.setFormattedText(response.getText());
                    // no equation found in image
                    if (response.is_not_math()) {
                        // add $$ ... $$ wrapper, similar handling as Mathpix Snip
                        notNumberedBlockModeResult.setFormattedText(Utilities.addDoubleDollarWrapper(response.getLatex_styled()));
                    } else {
                        notNumberedBlockModeResult.setFormattedText(response.getText_display());
                    }
                    numberedBlockModeResult.setFormattedText(Utilities.addEquationWrapper(response.getLatex_styled()));

                    double confidence = response.getLatex_confidence();

                    // minimal confidence is set to 1%
                    if (confidence > 0 && confidence < 0.01) {
                        confidence = 0.01;
                    }

                    confidenceProgressBar.setProgress(confidence);

                } else {

                    // no response received
                    clearErrorImage();

                }

            } else {

                // no image in the system clipboard
                Utilities.showErrorDialog("No image found in the clipboard");

            }

        });

        this.add(submitButton, 0, 2);

        this.add(frontGridPane, 0, 3);

        // "Confidence" label text
        confidenceText.setFont(Font.font(12));
        GridPane.setMargin(confidenceText, new Insets(TEXT_MARGIN, 0, TEXT_MARGIN, PREFERRED_MARGIN));
        confidenceText.setTextFill(new Color(0.149, 0.149, 0.149, 1));
        this.add(confidenceText, 0, 4);

        // confidence progress bar
        confidenceProgressBar.setPrefSize(PREFERRED_WIDTH - 2 * PREFERRED_MARGIN - 1, 20);
        GridPane.setMargin(confidenceProgressBar, new Insets(0, 0, 0, PREFERRED_MARGIN));
        // red for less than 20% certainty, yellow for 20% ~ 60%, and green for above 60%
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

    /**
     * Method to clear error image and last recognition results
     */
    private void clearErrorImage() {
        // put empty string into the clipboard to avoid displaying the same error image again
        Utilities.putStringIntoClipboard("");

        // set empty image
        clipboardImageView.setImage(null);

        // clear result TextFields
        latexStyledResult.setText("");
        textResult.setText("");
        notNumberedBlockModeResult.setText("");
        numberedBlockModeResult.setText("");

        // set 0 confidence
        confidenceProgressBar.setProgress(0);
    }

    /**
     * If new image found in the system clipboard, this method is used to display new image.
     *
     * @param image image to be displayed at the ImageView.
     */
    void setClipboardImageView(Image image) {
        clipboardImageView.setImage(image);
    }

}
