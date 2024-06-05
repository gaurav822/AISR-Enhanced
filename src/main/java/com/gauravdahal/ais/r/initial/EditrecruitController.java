/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import Constants.Constants;
import ENUM.BranchName;
import ENUM.Department;
import ENUM.Position;
import Utils.DialogUtils;
import Utils.EncryptionUtils;
import Utils.Utils;
import aisr.model.ManagementStaff;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import aisr.model.Recruit;
import aisr.model.RecruitCounts;
import client.ClientConnection;
import database.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import logger.Logger;

/**
 * FXML Controller class
 *
 * @author gauravdahal
 */
public class EditrecruitController implements Initializable {

    @FXML
    private TextField tfFullName;
    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfNumber;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfUserName;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private PasswordField tfRepassword;
    @FXML
    private ChoiceBox<String> cBoxQualification;
    @FXML
    private DatePicker interviewDatePicker;
    private Label labelDepartment;
    @FXML
    private Label labelFullNameHeader;

    private String recruitUserName;
    @FXML
    private ChoiceBox<String> choiceBoxDepart1;
    @FXML
    private ChoiceBox<String> choiceBoxDepart2;
    @FXML
    private ChoiceBox<String> choiceBoxBranch;
    @FXML
    private CheckBox departCheckBox;

    private String finalDepartment;

    private boolean hasSecondDepartment = false;

    private Recruit mRecruit;

    private ManagementStaff mgmtStaff;
    @FXML
    private TextArea textAreaBio;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choiceBoxDepart1.setItems(FXCollections.observableArrayList(
                Department.SOFTWARE.toString(),
                Department.MECHANICAL.toString(),
                Department.ELECTRONICS.toString(),
                Department.AEROSPACE.toString()
        ));

        choiceBoxDepart2.setItems(FXCollections.observableArrayList(
                Department.SOFTWARE.toString(),
                Department.MECHANICAL.toString(),
                Department.ELECTRONICS.toString(),
                Department.AEROSPACE.toString()
        ));

        choiceBoxBranch.setItems(FXCollections.observableArrayList(
                BranchName.MELBOURNE.toString(),
                BranchName.SYDNEY.toString(),
                BranchName.BRISBANE.toString(),
                BranchName.ADELAIDE.toString()
        ));

        departCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                hasSecondDepartment = true;
                choiceBoxDepart2.setDisable(false);
            } else {
                hasSecondDepartment = false;
                choiceBoxDepart2.setDisable(true);
            }
        });

        mgmtStaff = ManagementDashboardController.getManagementStaff(this);

    }

    @FXML
    private void onUpdateClicked(ActionEvent event) {
        if (choiceBoxDepart1.getValue() == null) {
            DialogUtils.showWarningDialog("Please select primary department");
            return;
        }

        if (choiceBoxBranch.getValue() == null) {
            DialogUtils.showWarningDialog("Please select recruit branch");
            return;
        }

        if (hasSecondDepartment && choiceBoxDepart2.getValue() == null) {
            DialogUtils.showWarningDialog("Please select secondary department");
            return;
        }

        finalDepartment = choiceBoxDepart1.getValue();
        if (hasSecondDepartment) {
            finalDepartment = finalDepartment + " , " + choiceBoxDepart2.getValue();
        }

        mRecruit.setBranch(choiceBoxBranch.getValue());
        mRecruit.setDepartment(finalDepartment);
        mRecruit.setStaffId(mgmtStaff.getStaffId());
        mRecruit.setStaffName(mgmtStaff.getFullName());
        mRecruit.setStaffBranch(mgmtStaff.getBranchName());
        mRecruit.setBio(textAreaBio.getText());

        updateRecruitDataServer();

    }

    private void updateRecruitDataServer() {
        ClientConnection clientConnection = ClientConnection.getInstance();
        try {
            clientConnection.getOut().writeObject("UPDATE_RECRUIT_STAFF");
            clientConnection.getOut().writeObject(mRecruit);
            clientConnection.getOut().flush();
        } catch (IOException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("Error sending recruits data: " + e.getMessage());
        }
    }
    
    public static void handleRecruitUpdateResponse(boolean updateSuccess) {
        if (updateSuccess) {
            DialogUtils.showSuccessDialog("Success : Recruit data updated successfully.");
        } else {
            DialogUtils.showErrorDialog("Failed to update recruit data.");
        }
    }

    @FXML
    private void onUploadMoreClicked(ActionEvent event) {

        DialogUtils.showSuccessDialog("Files uploaded Successfully");
    }

    public void setData(Recruit recruit) {
        mRecruit = recruit;
        labelFullNameHeader.setText(recruit.getFullName());
        tfFullName.setText(recruit.getFullName());
        tfAddress.setText(recruit.getAddress());
        tfNumber.setText(recruit.getPhoneNumber());
        tfUserName.setText(recruit.getUserName());
        tfEmail.setText(recruit.getEmailAddress());
        tfPassword.setText(EncryptionUtils.decrypt(recruit.getPassword()));
        tfRepassword.setText(EncryptionUtils.decrypt(recruit.getPassword()));
        cBoxQualification.setValue(recruit.getQualificationLevel());
        interviewDatePicker.setValue(Utils.parseDate(recruit.getInterviewDate()));
        textAreaBio.setText(recruit.getBio());
        if (recruit.getDepartment() != null && !recruit.getDepartment().isEmpty()) {
            if (recruit.getDepartment().contains(",")) {
                hasSecondDepartment = true;
                departCheckBox.setSelected(true);
                choiceBoxDepart2.setDisable(false);
                // Split the department string by comma
                String[] departments = recruit.getDepartment().split(",");

                // Set values to the choice boxes
                if (departments.length > 0) {
                    choiceBoxDepart1.setValue(departments[0].trim());
                }
                if (departments.length > 1) {
                    choiceBoxDepart2.setValue(departments[1].trim());
                }
            } else {
                choiceBoxDepart1.setValue(recruit.getDepartment());
            }
        }
        if (recruit.getBranch() != null && !recruit.getBranch().isEmpty()) {
            choiceBoxBranch.setValue(recruit.getBranch());
        }
        if(recruit.getBio()!=null && !recruit.getBio().isEmpty()){
            textAreaBio.setText(recruit.getBio());
        }
        this.recruitUserName = recruit.getUserName();
        
        finalDepartment = recruit.getDepartment();

    }

    private void updateDepartment(String username, String newDepartment) throws IOException {

    }

    @FXML
    private void onBackPressed(ActionEvent event) throws IOException {
        App.setRoot("management_dashboard");
    }

    @FXML
    private void onGraphicalViewClick(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("graphicalView.fxml"));
            Parent root = loader.load();

            GraphicalViewController controller = loader.getController();
            controller.setDepartment(finalDepartment);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Graphical View");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
