package com.gauravdahal.ais.r.initial;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import ENUM.QualificationLevel;
import aisr.model.Recruit;
import client.ClientConnection;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import session.SessionManager;

/**
 * Controller class for the Recruit Dashboard.
 */
public class RecruitDashboardController implements Initializable {

    @FXML
    private Label fullNameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label interviewDateLabel;
    @FXML
    private Label qualificationLevelLabel;
    @FXML
    private Text bioText;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private DatePicker interviewDatePicker;
    @FXML
    private ComboBox<QualificationLevel> qualificationLevelComboBox;
    @FXML
    private Button editButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Button uploadButton;
    @FXML
    private TextArea bioTextArea;
    @FXML
    private Button downloadButton;

    private Connection connection;
    private ClientConnection clientConnection;
    byte[] imageData;

    Recruit currentRecruit = null;

    /**
     * Initializes the controller class.
     *
     * @param url the location used to resolve relative paths for the root
     * object, or null if the location is not known.
     * @param rb the resources used to localize the root object, or null if the
     * root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clientConnection = ClientConnection.getInstance();
        currentRecruit = SessionManager.getInstance().getCurrentUser().getRecruit();
        loadRecruitInformation();
        qualificationLevelComboBox.setItems(FXCollections.observableArrayList(QualificationLevel.values()));
    }

    /**
     * Loads the recruit information into the form fields.
     */
    private void loadRecruitInformation() {
        if (currentRecruit != null) {
            fullNameLabel.setText(currentRecruit.getFullName());
            bioText.setText(currentRecruit.getBio());
            addressLabel.setText(currentRecruit.getAddress());
            phoneNumberLabel.setText(currentRecruit.getPhoneNumber());
            emailLabel.setText(currentRecruit.getEmailAddress());
            usernameLabel.setText(currentRecruit.getUserName());
            passwordLabel.setText("*******");
            interviewDateLabel.setText(currentRecruit.getInterviewDate());
            qualificationLevelLabel.setText(currentRecruit.getQualificationLevel());

            ByteArrayInputStream inputStream = new ByteArrayInputStream(currentRecruit.getImageData());
            Image image = new Image(inputStream);
            imageView.setImage(image);
        }
    }

    /**
     * Handles the action of clicking the edit button.
     */
    @FXML
    private void handleEditAction() {
        toggleEditMode(true);
    }

    /**
     * Handles the action of clicking the save button.
     *
     * @throws IOException if an I/O error occurs.
     */
    @FXML
    private void handleSaveAction() throws IOException {
        Recruit recruit = new Recruit(
                bioText.getText(),
                fullNameField.getText(),
                addressField.getText(),
                phoneNumberField.getText(),
                emailField.getText(),
                usernameField.getText(),
                passwordField.getText(),
                qualificationLevelComboBox.getValue().toString(),
                "",
                imageData);
        clientConnection.getOut().writeObject("UPDATE_RECRUIT");
        clientConnection.getOut().writeObject(recruit);
        clientConnection.getOut().flush();
        toggleEditMode(false);
    }

    /**
     * Handles the action of clicking the cancel button.
     */
    @FXML
    private void handleCancelAction() {
        if (fullNameField.isVisible()) {
            Image originalImage = new Image(getClass().getResourceAsStream("/assets/empty.jpg"));
            imageData = null;
            imageView.setImage(originalImage);
        }
        toggleEditMode(false);
    }

    /**
     * Toggles the edit mode of the form.
     *
     * @param isEditMode true to enable edit mode, false to disable.
     */
    private void toggleEditMode(boolean isEditMode) {
        fullNameField.setVisible(isEditMode);
        addressField.setVisible(isEditMode);
        phoneNumberField.setVisible(isEditMode);
        emailField.setVisible(isEditMode);
        usernameField.setVisible(isEditMode);
        passwordField.setVisible(isEditMode);
        interviewDatePicker.setVisible(isEditMode);
        qualificationLevelComboBox.setVisible(isEditMode);
        bioTextArea.setVisible(isEditMode);
        uploadButton.setVisible(isEditMode);

        fullNameLabel.setVisible(!isEditMode);
        addressLabel.setVisible(!isEditMode);
        phoneNumberLabel.setVisible(!isEditMode);
        emailLabel.setVisible(!isEditMode);
        usernameLabel.setVisible(!isEditMode);
        passwordLabel.setVisible(!isEditMode);
        interviewDateLabel.setVisible(!isEditMode);
        qualificationLevelLabel.setVisible(!isEditMode);
        bioText.setVisible(!isEditMode);

        editButton.setVisible(!isEditMode);
        downloadButton.setVisible(!isEditMode);
        saveButton.setVisible(isEditMode);
        cancelButton.setVisible(isEditMode);

        if (isEditMode) {
            fullNameField.setText(fullNameLabel.getText());
            addressField.setText(addressLabel.getText());
            phoneNumberField.setText(phoneNumberLabel.getText());
            emailField.setText(emailLabel.getText());
            usernameField.setText(usernameLabel.getText());
            interviewDatePicker.setValue(LocalDate.parse(interviewDateLabel.getText()));
            qualificationLevelComboBox.setValue(QualificationLevel.valueOf(qualificationLevelLabel.getText()));
            bioTextArea.setText(bioText.getText());
        }
    }

    /**
     * Handles the action of clicking the upload button.
     *
     * @param event the event that triggered the action.
     * @throws IOException if an I/O error occurs.
     */
    @FXML
    private void handleUploadAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        Stage stage = (Stage) imageView.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            imageData = Files.readAllBytes(file.toPath());
            Image image = new Image(new ByteArrayInputStream(imageData));
            imageView.setImage(image);
        }
    }

    /**
     * Handles the action of clicking the download button to generate a PDF.
     */
    @FXML
    private void handleDownloadPDFAction() {
        generateRecruitPDF();
    }

    /**
     * Generates a PDF file with the recruit's information.
     */
    private void generateRecruitPDF() {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 36);
                contentStream.newLineAtOffset(100, 700);

                contentStream.showText("Recruit Information");

                contentStream.endText();
                contentStream.moveTo(100, 695);
                contentStream.lineTo(500, 695);
                contentStream.stroke();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, 600);

                contentStream.showText("Full Name: " + fullNameLabel.getText());

                String text = "Bio: " + bioText.getText();
                wrapText(contentStream, text, 100, 640, 400);

                contentStream.showText("Address: " + addressLabel.getText());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Phone Number: " + phoneNumberLabel.getText());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Email Address: " + emailLabel.getText());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Username: " + usernameLabel.getText());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Qualification Level: " + qualificationLevelLabel.getText());
                contentStream.endText();
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            Stage stage = (Stage) imageView.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                document.save(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wraps text to fit within a specified width on a PDF page.
     *
     * @param contentStream the content stream to write to.
     * @param text the text to wrap.
     * @param x the x-coordinate of the starting position.
     * @param y the y-coordinate of the starting position.
     * @param width the width to wrap text within.
     * @throws IOException if an I/O error occurs.
     */
    private void wrapText(PDPageContentStream contentStream, String text, float x, float y, float width) throws IOException {
        float leading = 14.5f;
        PDType1Font font = PDType1Font.HELVETICA;
        float fontSize = 12;
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            String tempLine = line + word + " ";
            float textWidth = font.getStringWidth(tempLine) / 1000 * fontSize;
            if (textWidth > width) {
                contentStream.newLineAtOffset(0, -leading);
                contentStream.showText(line.toString().trim());
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }
        }

        if (line.length() > 0) {
            contentStream.newLineAtOffset(0, -leading);
            contentStream.showText(line.toString().trim());
        }
    }
}
