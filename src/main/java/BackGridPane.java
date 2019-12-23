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
 * The back grid panel has 3 Labels, 2 ImageViews, 1 Button, and 1 ProgressBar.
 */
class BackGridPane extends GridPane {

    private static final int PREFERRED_WIDTH = 300;
    private static final int PREFERRED_HEIGHT = 100;
    private static final int PREFERRED_MARGIN = 10;

    private static ImageView clipboardImageView = new ImageView();
    private static ImageView renderedImageView = new ImageView();
    private static Button submitButton = new Button("Submit");
    private static ProgressBar confidenceProgressBar = new ProgressBar(0);

    private static final Color PANE_BORDER_COLOR = new Color(0.898, 0.902, 0.9216, 1);
    private static final BorderWidths PANE_BORDER_WIDTHS = new BorderWidths(1, 0, 1, 0);
    private static final BorderStroke PANE_BORDER_STROKE = new BorderStroke(PANE_BORDER_COLOR, BorderStrokeStyle.SOLID, null, PANE_BORDER_WIDTHS);

    private static final BackgroundFill BACKGROUND_FILL = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
    private static final Background BACKGROUND = new Background(BACKGROUND_FILL);

    private static FrontGridPane frontGridPane = new FrontGridPane(PREFERRED_MARGIN, PANE_BORDER_STROKE);

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

        // 8 * 1 layout
        this.setVgap(8);
        this.setHgap(1);

        // add "Clipboard Image" text label
        Label clipboardTextLabel = Utilities.getTextLabel("Clipboard Image");
        Utilities.setNodeLeftMargin(clipboardTextLabel, PREFERRED_MARGIN);
        this.add(clipboardTextLabel, 0, 0);

        // get bordered ImageView
        BorderPane clipboardBorderPane = setImageViewBorder(clipboardImageView);
        this.add(clipboardBorderPane, 0, 1);

        // add "Rendered Equation" text label
        Label renderedTextLabel = Utilities.getTextLabel("Rendered Equation");
        Utilities.setNodeLeftMargin(renderedTextLabel, PREFERRED_MARGIN);
        this.add(renderedTextLabel, 0, 2);

        // get bordered ImageView
        BorderPane renderedBorderPane = setImageViewBorder(renderedImageView);
        this.add(renderedBorderPane, 0, 3);

        // is not a part of focus traversal cycle
        submitButton.setFocusTraversable(false);
        submitButton.setFont(Font.font(12));
        GridPane.setHalignment(submitButton, HPos.CENTER);
        submitButton.setOnMouseClicked(event -> {

            Image clipboardImage = clipboardImageView.getImage();

            if (clipboardImage != null) {

                // clear last location
                copiedButton.setVisible(false);

                Response response = Utilities.concurrentCall(clipboardImage);

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

                    // set rendered image to renderedImageView
                    renderedImageView.setImage(JLaTeXMathRendering.render(response.getLatex_styled()));

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

        // add submit button
        this.add(submitButton, 0, 4);

        // add front grid panel
        this.add(frontGridPane, 0, 5);

        // add "Confidence" label text
        Label confidenceText = Utilities.getTextLabel("Confidence");
        Utilities.setNodeLeftMargin(confidenceText, PREFERRED_MARGIN);
        this.add(confidenceText, 0, 6);

        // confidence progress bar
        Utilities.setNodeLeftMargin(confidenceProgressBar, PREFERRED_MARGIN);
        confidenceProgressBar.setPrefSize(PREFERRED_WIDTH - 2 * PREFERRED_MARGIN - 1, 20);
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
        this.add(confidenceProgressBar, 0, 7);

    }

    /**
     * Method to set ImageView style and add a BorderPane for border plotting
     *
     * @param imageView ImageView to be customised
     * @return customised ImageView with BorderPane
     */
    private BorderPane setImageViewBorder(ImageView imageView) {
        // preserve image ratio
        imageView.setPreserveRatio(true);
        // maximum width is 390 maximum height is 150
        // image larger than the above size will be scaled down
        imageView.setFitWidth(PREFERRED_WIDTH);
        imageView.setFitHeight(PREFERRED_HEIGHT);

        BorderPane borderPane = new BorderPane(imageView);

        // use BorderPane to add a border stroke to the ImageView
        borderPane.setBorder(new Border(PANE_BORDER_STROKE));
        borderPane.setPrefSize(PREFERRED_WIDTH, 110);

        return borderPane;
    }

    /**
     * Method to clear error image and last recognition results
     */
    private void clearErrorImage() {
        // put empty string into the clipboard to avoid displaying the same error image again
        Utilities.putStringIntoClipboard("");

        // set empty image
        clipboardImageView.setImage(null);
        renderedImageView.setImage(null);

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
