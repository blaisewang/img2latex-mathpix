import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.Instant;


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
    private static Label waitingTextLabel = new Label("Waiting...");
    private static ProgressBar confidenceProgressBar = new ProgressBar(0);

    private final Clipboard clipboard = Clipboard.getSystemClipboard();

    private static long lastUpdateCompletionTimestamp = Instant.now().getEpochSecond();
    private static long lastRequestCompletionTimestamp = Instant.now().getEpochSecond();

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
        this.setHgap(2);

        // add "Clipboard Image" text label
        Label clipboardTextLabel = Utilities.getTextLabel("Clipboard Image");
        Utilities.setDefaultNodeMargin(clipboardTextLabel, PREFERRED_MARGIN, 0);
        this.add(clipboardTextLabel, 0, 0);

        waitingTextLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 12));
        waitingTextLabel.setTextFill(new Color(0.3882, 0.7882, 0.3373, 1));
        waitingTextLabel.setVisible(false);
        GridPane.setHalignment(waitingTextLabel, HPos.RIGHT);
        Utilities.setDefaultNodeMargin(waitingTextLabel, 0, PREFERRED_MARGIN);
        this.add(waitingTextLabel, 1, 0);

        // get bordered ImageView
        BorderPane clipboardBorderPane = setImageViewBorder(clipboardImageView);
        this.add(clipboardBorderPane, 0, 1, 2, 1);

        // add "Rendered Equation" text label
        Label renderedTextLabel = Utilities.getTextLabel("Rendered Equation");
        Utilities.setDefaultNodeMargin(renderedTextLabel, PREFERRED_MARGIN, 0);
        this.add(renderedTextLabel, 0, 2, 2, 1);

        // get bordered ImageView
        BorderPane renderedBorderPane = setImageViewBorder(renderedImageView);
        this.add(renderedBorderPane, 0, 3, 2, 1);

        frontGridPane.setOnKeyReleased(event -> {

            // space key to update image
            if (event.getCode() == KeyCode.SPACE) {

                // prevent multiple image updates in a short time
                if (Instant.now().getEpochSecond() - lastUpdateCompletionTimestamp < 1) {
                    lastUpdateCompletionTimestamp = Instant.now().getEpochSecond();
                    return;
                }

                // an Image has been registered on the clipboard
                if (clipboard.hasImage()) {
                    // update the ImageView
                    clipboardImageView.setImage(clipboard.getImage());
                }

                lastUpdateCompletionTimestamp = Instant.now().getEpochSecond();

            }

            // enter key to send the OCR request
            if (event.getCode() == KeyCode.ENTER) {
                this.requestHandler();
            }

        });
        // add front grid panel
        this.add(frontGridPane, 0, 4, 2, 1);

        // enter key pressed event binding to the FrontGridPane
        this.onKeyReleasedProperty().bind(frontGridPane.onKeyReleasedProperty());

        // add "Confidence" label text
        Label confidenceText = Utilities.getTextLabel("Confidence");
        Utilities.setDefaultNodeMargin(confidenceText, PREFERRED_MARGIN, 0);
        this.add(confidenceText, 0, 5, 2, 1);

        // confidence progress bar
        Utilities.setDefaultNodeMargin(confidenceProgressBar, PREFERRED_MARGIN, 0);
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
        this.add(confidenceProgressBar, 0, 6, 2, 1);

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
     * OCR request handler.
     */
    void requestHandler() {

        // prevent multiple OCR requests from being sent in a short time
        if (Instant.now().getEpochSecond() - lastRequestCompletionTimestamp < 1) {
            lastRequestCompletionTimestamp = Instant.now().getEpochSecond();
            return;
        }

        // no image displayed
        if (clipboard.hasImage()) {
            // update the ImageView
            clipboardImageView.setImage(clipboard.getImage());
        }

        if (clipboardImageView.getImage() != null) {

            // clear last location
            copiedButton.setVisible(false);

            // show waiting label
            waitingTextLabel.setVisible(true);

            Task<Response> task = new Task<>() {
                @Override
                protected Response call() {
                    return Utilities.concurrentCall(clipboardImageView.getImage());
                }
            };
            task.setOnSucceeded(event -> {

                // hide waiting label
                waitingTextLabel.setVisible(false);

                Response response = task.getValue();

                // if response received
                if (response != null) {
                    // error occurred
                    if (response.getError() != null) {
                        // clear error image and last results
                        clearErrorImage();
                        // show error content with a alert dialog
                        Utilities.displayError(response.getError());

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
                    Utilities.displayError("Unexpected error occurred");
                    clearErrorImage();
                }

            });
            new Thread(task).start();

        } else {

            // no image in the system clipboard
            Utilities.displayError("No image found in the clipboard");

        }

        lastRequestCompletionTimestamp = Instant.now().getEpochSecond();

    }

}
