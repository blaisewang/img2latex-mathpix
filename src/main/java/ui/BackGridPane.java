package ui;

import io.IOUtils;
import io.Response;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.Instant;


/**
 * UI.BackGridPane.java
 * Used for display current clipboard image and confidence progressbar.
 * The back grid panel has 3 Labels, 2 ImageViews, 1 Button, and 1 ProgressBar.
 */
public class BackGridPane extends GridPane {

    private static final int PREFERRED_WIDTH = 300;
    private static final int PREFERRED_HEIGHT = 100;
    private static final int PREFERRED_MARGIN = 10;

    private static ImageView clipboardImageView = new ImageView();
    private static ImageView renderedImageView = new ImageView();
    private static Label waitingTextLabel = new Label("Waiting...");
    private static ProgressBar confidenceProgressBar = new ProgressBar(0);

    private final Clipboard clipboard = Clipboard.getSystemClipboard();

    private long lastUpdateCompletionTimestamp = Instant.now().getEpochSecond();
    private long lastRequestCompletionTimestamp = Instant.now().getEpochSecond();

    private static final Color PANE_BORDER_COLOR = new Color(0.898, 0.902, 0.9216, 1);
    private static final BorderWidths PANE_BORDER_WIDTHS = new BorderWidths(1, 0, 1, 0);
    private static final BorderStroke PANE_BORDER_STROKE = new BorderStroke(PANE_BORDER_COLOR, BorderStrokeStyle.SOLID, null, PANE_BORDER_WIDTHS);

    private static final BackgroundFill BACKGROUND_FILL = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
    private static final Background BACKGROUND = new Background(BACKGROUND_FILL);

    private static FrontGridPane frontGridPane = new FrontGridPane(PREFERRED_MARGIN, PANE_BORDER_STROKE);

    private PreferencesDialog preferencesDialog = new PreferencesDialog();

    // get components from UI.FrontGridPane instance
    private static CopiedButton copiedButton = frontGridPane.getCopiedButton();
    private static PressCopyTextField firstResult = frontGridPane.getFirstPressCopyTextField();
    private static PressCopyTextField secondResult = frontGridPane.getSecondPressCopyTextField();
    private static PressCopyTextField thirdResult = frontGridPane.getThirdPressCopyTextField();
    private static PressCopyTextField fourthResult = frontGridPane.getFourthPressCopyTextField();

    /**
     * UI.BackGridPane Initialisation.
     */
    public BackGridPane() {

        setPadding(new Insets(PREFERRED_MARGIN, 0, PREFERRED_MARGIN, 0));
        setBackground(BACKGROUND);

        // 8 * 2 layout
        setVgap(8);
        setHgap(2);

        // add "Clipboard Image" text label
        Label clipboardTextLabel = UIUtils.getTextLabel("Clipboard Image");
        UIUtils.setDefaultNodeMargin(clipboardTextLabel, PREFERRED_MARGIN, 0);
        add(clipboardTextLabel, 0, 0);

        waitingTextLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 12));
        waitingTextLabel.setTextFill(new Color(0.3882, 0.7882, 0.3373, 1));
        waitingTextLabel.setVisible(false);
        GridPane.setHalignment(waitingTextLabel, HPos.RIGHT);
        UIUtils.setDefaultNodeMargin(waitingTextLabel, 0, PREFERRED_MARGIN);
        add(waitingTextLabel, 1, 0);

        // get bordered ImageView
        BorderPane clipboardBorderPane = setImageViewBorder(clipboardImageView);
        add(clipboardBorderPane, 0, 1, 2, 1);

        // add "Rendered Equation" text label
        Label renderedTextLabel = UIUtils.getTextLabel("Rendered Equation");
        UIUtils.setDefaultNodeMargin(renderedTextLabel, PREFERRED_MARGIN, 0);
        add(renderedTextLabel, 0, 2, 2, 1);

        // get bordered ImageView
        BorderPane renderedBorderPane = setImageViewBorder(renderedImageView);
        add(renderedBorderPane, 0, 3, 2, 1);

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
                requestHandler();
            }

        });
        // add front grid panel
        add(frontGridPane, 0, 4, 2, 1);

        // enter key pressed event binding to the UI.FrontGridPane
        onKeyReleasedProperty().bind(frontGridPane.onKeyReleasedProperty());

        // add "Confidence" label text
        Label confidenceText = UIUtils.getTextLabel("Confidence");
        UIUtils.setDefaultNodeMargin(confidenceText, PREFERRED_MARGIN, 0);
        add(confidenceText, 0, 5, 2, 1);

        // confidence progress bar
        UIUtils.setDefaultNodeMargin(confidenceProgressBar, PREFERRED_MARGIN, 0);
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
        add(confidenceProgressBar, 0, 6, 2, 1);

    }

    /**
     * Method to set ImageView style and add a BorderPane for border plotting.
     *
     * @param imageView ImageView to be customised.
     * @return customised ImageView with BorderPane.
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
     * Method to clear error image and last recognition results.
     */
    private void clearErrorImage() {
        // put empty string into the clipboard to avoid displaying the same error image again
        UIUtils.putStringIntoClipboard("");

        // set empty image
        clipboardImageView.setImage(null);
        renderedImageView.setImage(null);

        // clear result TextFields
        firstResult.setText("");
        secondResult.setText("");
        thirdResult.setText("");
        fourthResult.setText("");

        // set 0 confidence
        confidenceProgressBar.setProgress(0);
    }

    /**
     * Show preferences dialog with the given tab index.
     */
    public void showPreferencesDialog(int index) {
        preferencesDialog.show(index);
    }

    /**
     * Response handler.
     */
    private void responseHandler(Response response) {

        // if response received
        if (response != null) {

            // error occurred
            if (response.getError() != null) {

                // show error content with a alert dialog
                UIUtils.displayError(response.getError());

                switch (response.getError()) {
                    case IOUtils.INVALID_CREDENTIALS_ERROR:
                        // show API credential setting dialog for invalid credential error
                        showPreferencesDialog(1);
                        break;
                    case IOUtils.INVALID_PROXY_CONFIG_ERROR:
                        // show proxy setting dialog for invalid proxy config
                        showPreferencesDialog(2);
                        break;
                    case IOUtils.CONNECTION_REFUSED_ERROR:
                        if (IOUtils.getProxyEnabled()) {
                            // show proxy setting dialog for possible invalid proxy config
                            showPreferencesDialog(2);
                        }
                        break;
                    default:
                        // clear error image and last results
                        clearErrorImage();
                        break;
                }

                return;
            }

            // put default result into the system clipboard
            UIUtils.putStringIntoClipboard(response.getLatexStyled());
            // set UI.CopiedButton to the corresponded location
            frontGridPane.setCopiedButtonRowIndex();

            // set rendered image to renderedImageView
            renderedImageView.setImage(JLaTeXMathRenderingHelper.render(response.getLatexStyled()));

            // set results to corresponded TextFields.
            firstResult.setFormattedText(response.getLatexStyled());
            secondResult.setFormattedText(response.getText());
            // wrap the result
            thirdResult.setFormattedText(IOUtils.thirdResultWrapper(response.getLatexStyled()));
            // wrap the result
            fourthResult.setFormattedText(IOUtils.fourthResultWrapper(response.getLatexStyled()));

            double confidence = response.getLatexConfidence();

            // minimal confidence is set to 1%
            if (confidence > 0 && confidence < 0.01) {
                confidence = 0.01;
            }

            confidenceProgressBar.setProgress(confidence);

        } else {

            // no response received
            UIUtils.displayError("Unexpected error occurred");
            clearErrorImage();

        }

    }

    /**
     * OCR request handler.
     */
    private void requestHandler() {

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
                    return IOUtils.concurrentCall(clipboardImageView.getImage());
                }
            };
            task.setOnSucceeded(event -> {

                // hide waiting label
                waitingTextLabel.setVisible(false);

                Response response = task.getValue();

                responseHandler(response);

            });
            new Thread(task).start();

        } else {

            // no image in the system clipboard
            UIUtils.displayError("No image found in the clipboard");

        }

        lastRequestCompletionTimestamp = Instant.now().getEpochSecond();

    }

}
