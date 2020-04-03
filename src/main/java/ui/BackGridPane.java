package ui;

import io.IOUtils;
import io.LegacyRecognition;
import io.Response;
import io.TextRecognition;
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * UI.BackGridPane.java
 * Used for display current clipboard image and confidence progressbar.
 * The back grid panel has 3 Labels, 2 ImageViews, 1 Button, and 1 ProgressBar.
 */
public class BackGridPane extends GridPane {

    private static final int PREFERRED_WIDTH = 300;
    private static final int PREFERRED_HEIGHT = 100;
    private static final int PREFERRED_MARGIN = 10;

    private static final TextRecognition TEXT_RECOGNITION = new TextRecognition();
    private static final LegacyRecognition LEGACY_RECOGNITION = new LegacyRecognition();

    private static final ImageView CLIPBOARD_IMAGE_VIEW = new ImageView();
    private static final ImageView RENDERED_IMAGE_VIEW = new ImageView();
    private static final Label WAITING_TEXT_LABEL = new Label("Waiting...");
    private static final ProgressBar CONFIDENCE_PROGRESS_BAR = new ProgressBar(0);

    private final Clipboard clipboard = Clipboard.getSystemClipboard();

    private long lastUpdateCompletionTimestamp = Instant.now().getEpochSecond();
    private long lastRequestCompletionTimestamp = Instant.now().getEpochSecond();

    private static final Color PANE_BORDER_COLOR = new Color(0.898, 0.902, 0.9216, 1);
    private static final BorderWidths PANE_BORDER_WIDTHS = new BorderWidths(1, 0, 1, 0);
    private static final BorderStroke PANE_BORDER_STROKE = new BorderStroke(PANE_BORDER_COLOR, BorderStrokeStyle.SOLID, null, PANE_BORDER_WIDTHS);

    private static final BackgroundFill BACKGROUND_FILL = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
    private static final Background BACKGROUND = new Background(BACKGROUND_FILL);

    private static final FrontGridPane FRONT_GRID_PANE = new FrontGridPane(PREFERRED_MARGIN, PANE_BORDER_STROKE);

    private final PreferencesDialog preferencesDialog = new PreferencesDialog();

    // get components from UI.FrontGridPane instance
    private static final CopiedButton COPIED_BUTTON = FRONT_GRID_PANE.getCopiedButton();
    private static final CopyResultButton COPY_TSV_BUTTON = FRONT_GRID_PANE.getCopyTSVButton();
    private static final CopyResultButton COPY_MATH_ML_BUTTON = FRONT_GRID_PANE.getCopyMathMLButton();

    private final List<PressCopyTextField> resultTextFiledList = Arrays.asList(
            FRONT_GRID_PANE.getFirstPressCopyTextField(),
            FRONT_GRID_PANE.getSecondPressCopyTextField(),
            FRONT_GRID_PANE.getThirdPressCopyTextField(),
            FRONT_GRID_PANE.getFourthPressCopyTextField()
    );

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
        var clipboardTextLabel = UIUtils.getTextLabel("Clipboard Image");
        UIUtils.setDefaultNodeMargin(clipboardTextLabel, PREFERRED_MARGIN, 0);
        add(clipboardTextLabel, 0, 0);

        WAITING_TEXT_LABEL.setFont(Font.font("Arial Black", FontWeight.BOLD, 12));
        WAITING_TEXT_LABEL.setTextFill(new Color(0.3882, 0.7882, 0.3373, 1));
        WAITING_TEXT_LABEL.setVisible(false);
        GridPane.setHalignment(WAITING_TEXT_LABEL, HPos.RIGHT);
        UIUtils.setDefaultNodeMargin(WAITING_TEXT_LABEL, 0, PREFERRED_MARGIN);
        add(WAITING_TEXT_LABEL, 1, 0);

        // get bordered ImageView
        var clipboardBorderPane = setImageViewBorder(CLIPBOARD_IMAGE_VIEW);
        add(clipboardBorderPane, 0, 1, 2, 1);

        // add "Rendered Equation" text label
        var renderedTextLabel = UIUtils.getTextLabel("Rendered Equation");
        UIUtils.setDefaultNodeMargin(renderedTextLabel, PREFERRED_MARGIN, 0);
        add(renderedTextLabel, 0, 2, 2, 1);

        // get bordered ImageView
        var renderedBorderPane = setImageViewBorder(RENDERED_IMAGE_VIEW);
        add(renderedBorderPane, 0, 3, 2, 1);

        FRONT_GRID_PANE.setOnKeyReleased(event -> {

            // space key to update image
            if (event.getCode() == KeyCode.BACK_SPACE) {

                // prevent multiple image updates in a short time
                if (Instant.now().getEpochSecond() - lastUpdateCompletionTimestamp < 1) {
                    lastUpdateCompletionTimestamp = Instant.now().getEpochSecond();
                    return;
                }

                // an Image has been registered on the clipboard
                if (clipboard.hasImage()) {
                    // update the ImageView
                    CLIPBOARD_IMAGE_VIEW.setImage(clipboard.getImage());
                }

                lastUpdateCompletionTimestamp = Instant.now().getEpochSecond();

            }

            // enter key to send the OCR request
            if (event.getCode() == KeyCode.ENTER) {
                requestHandler();
            }

        });
        // add front grid panel
        add(FRONT_GRID_PANE, 0, 4, 2, 1);

        // enter key pressed event binding to the UI.FrontGridPane
        onKeyReleasedProperty().bind(FRONT_GRID_PANE.onKeyReleasedProperty());

        // hide copied button if copy MathML button or copy TSV button is clicked.
        COPY_TSV_BUTTON.setOnMouseClicked(event -> COPIED_BUTTON.setVisible(false));
        COPY_MATH_ML_BUTTON.setOnMouseClicked(event -> COPIED_BUTTON.setVisible(false));

        // add "Confidence" label text
        var confidenceText = UIUtils.getTextLabel("Confidence");
        UIUtils.setDefaultNodeMargin(confidenceText, PREFERRED_MARGIN, 0);
        add(confidenceText, 0, 5, 2, 1);

        // confidence progress bar
        UIUtils.setDefaultNodeMargin(CONFIDENCE_PROGRESS_BAR, PREFERRED_MARGIN, 0);
        CONFIDENCE_PROGRESS_BAR.setPrefSize(PREFERRED_WIDTH - 2 * PREFERRED_MARGIN - 1, 20);
        // red for less than 20% certainty, yellow for 20% ~ 60%, and green for above 60%
        CONFIDENCE_PROGRESS_BAR.progressProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() < 0.2) {
                setStyle("-fx-accent: #ec4d3d;");
            } else if (newValue.doubleValue() < 0.6) {
                setStyle("-fx-accent: #f8cd46;");
            } else {
                setStyle("-fx-accent: #63c956;");
            }
        });
        add(CONFIDENCE_PROGRESS_BAR, 0, 6, 2, 1);

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

        var borderPane = new BorderPane(imageView);

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
        CLIPBOARD_IMAGE_VIEW.setImage(null);
        RENDERED_IMAGE_VIEW.setImage(null);

        // clear result TextFields
        for (PressCopyTextField pressCopyTextField : resultTextFiledList) {
            pressCopyTextField.setFormattedText("");
        }

        // set 0 confidence
        CONFIDENCE_PROGRESS_BAR.setProgress(0);
    }

    /**
     * Show preferences dialog with the given tab index.
     */
    public void showPreferencesDialog(int index) {
        preferencesDialog.show(index);
    }

    /**
     * Error Handler.
     */
    private void errorHandler(Response response) {

        var error = response.getError();

        if (IOUtils.INVALID_CREDENTIALS_ERROR.equals(error)) {
            // show API credential setting dialog for invalid credential error
            UIUtils.displayError(error);
            showPreferencesDialog(1);
        } else if (IOUtils.INVALID_PROXY_CONFIG_ERROR.equals(error)) {
            // show proxy setting dialog for invalid proxy config
            UIUtils.displayError(error);
            showPreferencesDialog(2);
        } else if (error.contains(IOUtils.EXCEPTION_MARK)) {
            // display exception error
            UIUtils.displayError(IOUtils.exceptionFormatter(error));
        } else {
            // clear error image and last results
            UIUtils.displayError(error);
            clearErrorImage();
        }

    }

    /**
     * Response handler.
     */
    private void responseHandler(Response response) {

        // if response received
        if (response != null) {

            // error occurred
            if (response.getError() != null) {
                errorHandler(response);
                return;
            }

            var resultList = new String[]{
                    response.getLatexStyled(),
                    response.getText(),
                    IOUtils.thirdResultFormatter(response.getText()),
                    IOUtils.fourthResultFormatter(response.getText()),
            };

            // put default result into the system clipboard
            UIUtils.putStringIntoClipboard(resultList[0]);
            // set UI.CopiedButton to the corresponded location
            FRONT_GRID_PANE.setCopiedButtonRowIndex();

            List<CopyResultButton> buttonList = new LinkedList<>();

            var mathML = response.getMathML();
            if (mathML != null && !"".equals(mathML)) {
                COPY_MATH_ML_BUTTON.setResult(mathML);
                buttonList.add(COPY_MATH_ML_BUTTON);
            }

            var tsv = response.getTSV();
            if (tsv != null && !"".equals(tsv)) {
                COPY_TSV_BUTTON.setResult(tsv);
                buttonList.add(COPY_TSV_BUTTON);
            }

            FRONT_GRID_PANE.setCopyResultButtonColumnIndex(buttonList);

            // set rendered image to renderedImageView
            RENDERED_IMAGE_VIEW.setImage(JLaTeXMathRenderingHelper.render(resultList[0]));

            // set results to corresponded TextFields.
            resultTextFiledList.get(0).setFormattedText(resultList[0]);
            resultTextFiledList.get(1).setFormattedText(resultList[1]);

            if (resultList[2].equals(resultList[1])) {
                resultTextFiledList.get(2).setDisable(true);
                resultTextFiledList.get(3).setDisable(true);
            } else if (resultList[3].equals(resultList[2])) {
                resultTextFiledList.get(2).setFormattedText(resultList[2]);
                resultTextFiledList.get(3).setDisable(true);
            } else {
                resultTextFiledList.get(2).setFormattedText(resultList[2]);
                resultTextFiledList.get(3).setFormattedText(resultList[3]);
            }

            var confidence = response.getLatexConfidence();

            // minimal confidence is set to 3%
            if (confidence > 0 && confidence < 0.03) {
                confidence = 0.03;
            }

            CONFIDENCE_PROGRESS_BAR.setProgress(confidence);

        } else {

            // no response received
            UIUtils.displayError(IOUtils.UNEXPECTED_ERROR);
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
            CLIPBOARD_IMAGE_VIEW.setImage(clipboard.getImage());
        }

        if (CLIPBOARD_IMAGE_VIEW.getImage() != null) {

            for (PressCopyTextField pressCopyTextField : resultTextFiledList) {
                pressCopyTextField.setFormattedText("");
                pressCopyTextField.setDisable(false);
            }

            RENDERED_IMAGE_VIEW.setImage(null);

            // clear last location
            COPIED_BUTTON.setVisible(false);
            COPY_TSV_BUTTON.setVisible(false);
            COPY_MATH_ML_BUTTON.setVisible(false);

            // show waiting label
            WAITING_TEXT_LABEL.setVisible(true);

            var ref = new Object() {
                Task<Response> textOCRTask = null;
            };

            if (IOUtils.getImprovedOCREnableOption()) {
                ref.textOCRTask = new Task<>() {
                    @Override
                    protected Response call() {
                        return IOUtils.concurrentCall(TEXT_RECOGNITION, CLIPBOARD_IMAGE_VIEW.getImage());
                    }
                };
                new Thread(ref.textOCRTask).start();
            }

            Task<Response> legacyOCRTask = new Task<>() {
                @Override
                protected Response call() {
                    return IOUtils.concurrentCall(LEGACY_RECOGNITION, CLIPBOARD_IMAGE_VIEW.getImage());
                }
            };
            legacyOCRTask.setOnSucceeded(event -> {
                Response response = legacyOCRTask.getValue();
                if (ref.textOCRTask != null) {
                    try {
                        Response textOCRResponse = ref.textOCRTask.get();
                        response.setText(textOCRResponse.getText());
                        response.setTSV(textOCRResponse.getTypeFromData("tsv"));
                        response.setMathML(textOCRResponse.getTypeFromData("mathml"));
                        response.setLatexConfidence(textOCRResponse.getConfidence());
                    } catch (InterruptedException | ExecutionException ignored) {
                    }
                }
                responseHandler(response);
                // hide waiting label
                WAITING_TEXT_LABEL.setVisible(false);
            });
            new Thread(legacyOCRTask).start();

        } else {

            // no image in the system clipboard
            UIUtils.displayError(IOUtils.NO_IMAGE_FOUND_IN_THE_CLIPBOARD_ERROR);

        }

        lastRequestCompletionTimestamp = Instant.now().getEpochSecond();

    }

}
