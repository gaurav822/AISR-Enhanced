/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import Constants.Constants;
import ENUM.Department;
import ENUM.Position;
import Utils.DialogUtils;
import Utils.EncryptionUtils;
import Utils.Utils;
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
/**
 * FXML Controller class
 *
 * @author gauravdahal
 */
public class EditrecruitAdminController implements Initializable {


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
    @FXML
    private Label labelDepartment;
    @FXML
    private Label labelFullNameHeader;
    
    
    
    private String recruitUserName;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         
    }    
    
    @FXML
    private void onUpdateClicked(ActionEvent event) {
        
         DialogUtils.showSuccessDialog("Details updated Successfully");
    }

    @FXML
    private void onUploadMoreClicked(ActionEvent event) {
        
         DialogUtils.showSuccessDialog("Files uploaded Successfully");
    }
    
    public void setData(Recruit recruit){
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
        labelDepartment.setText(recruit.getDepartment());
        this.recruitUserName = recruit.getUserName();
        
    }
    

//    @FXML
//    private void onBackPressed(ActionEvent event) throws IOException {
//            App.setRoot("admin_dashboard");
//
//    }

}
