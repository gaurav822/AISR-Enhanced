/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import Constants.Constants;
import ENUM.BranchName;
import ENUM.ManagementLevel;
import Utils.DialogUtils;
import aisr.model.AdminStaff;
import aisr.model.ManagementStaff;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import aisr.model.Recruit;
import client.ClientConnection;
import java.io.EOFException;
import javafx.collections.ObservableList;
import session.SessionManager;

/**
 * FXML Controller class
 *
 * @author gauravdahal
 */
public class ManagementDashboardController implements Initializable {

    private AnchorPane anchorPaneDashboard;
    @FXML
    private VBox recruitLayout;

    @FXML
    private AnchorPane recruitListAnchorPane;
    @FXML
    private Label labelStaffName;
    @FXML
    private TextField tfStudentId;
    @FXML
    private ChoiceBox<String> cboxRecruits;
    @FXML
    private ChoiceBox<String> cBoxUniversity;

    private ObservableList<Recruit> mRecruits;
    @FXML
    private Label labelRecruitNotFound;

    private static ManagementDashboardController instance;
    
    private static ManagementStaff mgmtStaff;
    
    
    @FXML
    private TextField fullName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField email;
    @FXML
    private TextField userName;
    @FXML
    private TextField staffId;
    @FXML
    private ChoiceBox<String> cBoxManagement;
    @FXML
    private ChoiceBox<String> cBoxBranch;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        cBoxManagement.setItems(FXCollections.observableArrayList(
                ManagementLevel.SENIOR.label,
                ManagementLevel.MIDLEVEL.label,
                ManagementLevel.SUPERVISOR.label
        ));

        cBoxBranch.setItems(FXCollections.observableArrayList(
                BranchName.MELBOURNE.toString(),
                BranchName.SYDNEY.toString(),
                BranchName.BRISBANE.toString(),
                BranchName.ADELAIDE.toString()
        ));
        instance = this;
        requestDataFromServer("GET_RECRUITS", null);// Set the instance
        requestDataFromServer("GET_MANAGEMENT_INFO", SessionManager.getInstance().getCurrentUser().getEmail());


    }

    public void navigateBackToOriginalView() {
        // Replace the content of anchorPaneDashboard with the original content

    }

    @FXML
    private void showRecruitList(Event event) {

        requestDataFromServer("GET_RECRUITS", null);// Set the instance
    }

    private void requestDataFromServer(String requestObject, String email) {
        ClientConnection clientConnection = ClientConnection.getInstance();
        try {
            clientConnection.getOut().writeObject(requestObject);
            if (email != null && !email.isEmpty()) {
                clientConnection.getOut().writeObject(email);
            }
            clientConnection.getOut().flush();
        } catch (EOFException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("readline: " + e.getMessage());
        }
    }
    
    
     public static void setManagementStaff(ManagementStaff staff) {
         mgmtStaff = staff;
        if (instance != null) {

            instance.fullName.setText(staff.getFullName());
            instance.email.setText(staff.getEmailAddress());
            instance.phoneNumber.setText(staff.getPhoneNumber());
            instance.userName.setText(staff.getUserName());
            instance.staffId.setText(staff.getStaffId());
            instance.cBoxManagement.setValue(staff.getManagementLevel().label);
            instance.cBoxBranch.setValue(staff.getBranchName());
        }
    }

    public static void updateRecruitTable(ArrayList<Recruit> recruits) {

        if(instance!=null){
        instance.recruitLayout.getChildren().clear();
        try {
            instance.labelRecruitNotFound.setVisible(false);
            for (int i = 0; i < recruits.size(); i++) {

                FXMLLoader loader = new FXMLLoader(App.class.getResource("recruititem_manager.fxml"));

                HBox hbox = loader.load();
                RecruitItemManagerController controller = loader.getController();
                controller.setData(recruits.get(i));
                instance.recruitLayout.getChildren().add(hbox);
//           

            }

        } catch (IOException e) {
            System.out.println("IOException Occured");

        }
        }

    }

    public boolean validateStudentIdForQualification(String studentId) {
        if (studentId.isEmpty()) {
            return false;
        }

        return studentId.length() >= 5;
    }

    public boolean validateUniversityForQualification(String university) {
        if (university == null || university.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean validateRecruitUserNameForQualification(String userName) {
        if (userName == null || userName.isEmpty()) {
            return false;
        }
        return true;
    }

    @FXML
    private void showProfile(Event event) {

    }

    @FXML
    private void showSettings(Event event) {
    }

    @FXML
    private void onLogout(ActionEvent event) throws IOException {
        DialogUtils.showSuccessDialog("Logout Successful");
        App.setRoot("login");
    }

    @FXML
    private void onVerificationStarted(ActionEvent event) {

        if (validateStudentIdForQualification(tfStudentId.getText()) && validateUniversityForQualification(cBoxUniversity.getValue())
                && validateRecruitUserNameForQualification(cboxRecruits.getValue())) {

            DialogUtils.showGenerateReport("Verifying Qualification", "Qualification Verified");

        }

    }

    @FXML
    private void onAccountUpdate(ActionEvent event) {

        DialogUtils.showSuccessDialog("Profile Updated Successfully");

    }

    @FXML
    private void onPasswordChange(ActionEvent event) {
        DialogUtils.showSuccessDialog("Password Changed Successfully");
    }

    @FXML
    private void onAccountDelete(ActionEvent event) throws IOException {
        DialogUtils.showSuccessDialog("Account deleted");
        App.setRoot("login");
    }

    @FXML
    private void showQualVerification(Event event) {

        if (mRecruits != null && !mRecruits.isEmpty()) {
            List<String> recruitNames = mRecruits.stream()
                    .map(Recruit::getUserName) // Assuming 'getName' is the method to get the name from Recruit class
                    .collect(Collectors.toList());
            cboxRecruits.setItems(FXCollections.observableArrayList(recruitNames));
        }

        cBoxUniversity.setItems(FXCollections.observableArrayList(
                "CQUniversity",
                "Macquire University",
                "UTS Sydney",
                "Western Sydney University",
                "Victorial University"
        ));

    }

}
