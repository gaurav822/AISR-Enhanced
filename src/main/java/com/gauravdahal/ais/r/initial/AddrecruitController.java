/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import Constants.Constants;
import ENUM.Position;
import ENUM.QualificationLevel;
import ENUM.StaffType;
import Utils.DialogUtils;
import Utils.EncryptionUtils;
import Utils.Utils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import aisr.model.AdminStaff;
import aisr.model.ManagementStaff;
import aisr.model.Recruit;
import client.ClientConnection;
import java.io.EOFException;
import session.SessionManager;

/**
 * FXML Controller class
 *
 * @author gauravdahal
 */
public class AddrecruitController implements Initializable {

    @FXML
    private AnchorPane NewDetailsAnchorPane;
    @FXML
    private TextField tfFullName;
    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfPhoneNumber;
    @FXML
    private TextField tfEmailAddress;
    @FXML
    private TextField tfUserName;
    @FXML
    private ChoiceBox<String> cBoxQualification;
    @FXML
    private DatePicker dPInterViewDate;

    private ArrayList<Recruit> recruits;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private PasswordField pfRePassword;
    @FXML
    private Button btnBack;

    private String adminEmail;

    private AdminStaff adminStaff;
    
  private static AddrecruitController instance;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        recruits = new ArrayList<>();

        cBoxQualification.setItems(FXCollections.observableArrayList(
                QualificationLevel.Bachelors.toString(),
                QualificationLevel.Masters.toString(),
                QualificationLevel.PhD.toString()
        ));

        adminStaff = AdminDashboardController.getAdminStaff(this);

    }

    @FXML
    private void onDatePicked(ActionEvent event) {

    }

    @FXML
    private void onFilesChoosed(ActionEvent event) {

    }

    @FXML
    private void onDataEntered(ActionEvent event) {

        if (areDataValid()) {
            Recruit recruit = new Recruit(
                    tfFullName.getText(),
                    tfAddress.getText(),
                    tfPhoneNumber.getText(),
                    tfEmailAddress.getText(),
                    tfUserName.getText(),
                    pfPassword.getText());
            recruit.setInterviewDate(Utils.formatDate(dPInterViewDate.getValue()));
            recruit.setQualificationLevel(cBoxQualification.getValue());
            recruit.setStaffName(adminStaff.getFullName());
            recruit.setStaffId(adminStaff.getStaffId());
            recruit.setDateDataAdded(Utils.formatDate(LocalDate.now()));

            //encrypting recruit's password
            recruit.setPassword(EncryptionUtils.encrypt(pfPassword.getText()));

            recruits.add(recruit);
            DialogUtils.showSuccessDialog("Recruit Data Added, Don't Forget to Save");
            clearFields();
        }
    }
    
    
    public static void onResponseFromServer(boolean isDuplicateEntry, String email) {
        if (instance != null) {
            if (isDuplicateEntry) {
                DialogUtils.showErrorDialog("Recruit with email " + email + " already exists!");
            }
            
            else{
            DialogUtils.showSuccessDialog("Recruit "+email+" has been registered Successfully");

            }
        }
    }

    private void clearFields() {
        tfFullName.clear();
        tfAddress.clear();
        tfPhoneNumber.clear();
        tfEmailAddress.clear();
        tfUserName.clear();
        pfPassword.clear();
        pfRePassword.clear();
        dPInterViewDate.setValue(null);
        cBoxQualification.setValue(null);
    }

    @FXML
    private void onDataSaved(ActionEvent event) {

        if (!recruits.isEmpty()) {
            sendRecruitsDataToServer();
        } else {
            DialogUtils.showWarningDialog("Please enter the data first");
        }

        recruits.clear();
    }

    public void sendRecruitsDataToServer() {
        ClientConnection clientConnection = ClientConnection.getInstance();
        try {
            clientConnection.getOut().writeObject("ADD_RECRUIT_BATCH");
            clientConnection.getOut().writeObject(recruits);
            clientConnection.getOut().flush();
        } catch (IOException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("Error sending recruits data: " + e.getMessage());
        }
    }

    public boolean areDataValid() {
        if (tfFullName.getText().isEmpty()) {
            DialogUtils.showErrorDialog("Name cannot be empty");
            return false;
        }

        if (tfAddress.getText().isEmpty()) {
            DialogUtils.showErrorDialog("Address cannot be empty");
            return false;
        }

        if (Utils.isNotValidNumber(tfPhoneNumber.getText())) {
            return false;
        }

        if (tfEmailAddress.getText().isEmpty()) {
            DialogUtils.showErrorDialog("Email cannot be empty");
            return false;
        }

        if (tfUserName.getText().isEmpty()) {
            DialogUtils.showErrorDialog("Username cannot be empty");
            return false;
        }

        if (Utils.isNotValidPassword(pfPassword.getText(), pfRePassword.getText())) {
            return false;
        }

        if (dPInterViewDate.getValue() == null) {
            DialogUtils.showErrorDialog("Please select interview date");
            return false;
        }

        if (cBoxQualification.getValue() == null) {
            DialogUtils.showErrorDialog("Please select Qualification");
            return false;
        }

        return true;
    }

    @FXML
    private void handleBackBtn(ActionEvent event) throws IOException {
        App.setRoot("admin_dashboard");
    }

}
