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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.AdminStaff;
import model.ManagementStaff;
import model.Recruit;

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
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        recruits = new ArrayList<>();
        
        cBoxQualification.setItems(FXCollections.observableArrayList(
                QualificationLevel.Bachelors.toString(),
                 QualificationLevel.Masters.toString(),
                 QualificationLevel.PhD.toString()
        ));
    }    

    @FXML
    private void onDatePicked(ActionEvent event) {
        
    }

    @FXML
    private void onFilesChoosed(ActionEvent event) {
        
    }

    @FXML
    private void onDataEntered(ActionEvent event) {
        
        if(areDataValid()){
            Recruit recruit = new Recruit(
            tfFullName.getText(),
                    tfAddress.getText(),
                    tfPhoneNumber.getText(),
                       tfEmailAddress.getText(),
                       tfUserName.getText(),
                       pfPassword.getText());
            recruit.setInterviewDate(Utils.formatDate(dPInterViewDate.getValue()));
            recruit.setBranch("ADELAIDE");
            recruit.setQualificationLevel("Bachelors");
            recruit.setStaffName("Gaurav Dahal");
            recruit.setStaffId("112323");
            recruit.setStaffBranch("SYDNEY");
            recruit.setDateDataAdded(Utils.formatDate(LocalDate.now()));
            
            //encrypting recruit's password
            recruit.setPassword(EncryptionUtils.encrypt(pfPassword.getText()));
            
            //empty because manager will assign the department
            recruit.setDepartment("");
            
            
            
            recruits.add(recruit);
            DialogUtils.showSuccessDialog("Recruit Data Added, Don't Forget to Save");
            clearFields();
        }
    }
    
    private void clearFields(){
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
        
        if(!recruits.isEmpty()){
            saveDataToCSV();
        }
        
        else{
            DialogUtils.showWarningDialog("Please enter the data first");
        }
       
        recruits.clear();
    }
    
    private void saveDataToCSV(){
         try {
            File file = new File(Constants.RECRUIT_CSV_FILE);
            boolean isNewFile = !file.exists();
            FileWriter writer = new FileWriter(file, true); // true for append mode
            if (isNewFile) {
                writer.write("Full Name,Address,Phone Number,Email Address,Username,Password,Interview Date, Qualification Level, Department, Branch, Staff ID,Staff Name,Date and Time,Staff Branch\n");
            }
            
            
            for(Recruit recruit:recruits){
                writer.write(recruit.toString() + "\n");
            }
            
            writer.close();
            DialogUtils.showSuccessDialog("Recruits Details Added Successfully");
        
        } catch (IOException e) {
            DialogUtils.showErrorDialog("Error occurred while saving data to CSV file.");
        }
    }
    
    public boolean areDataValid(){
        if(tfFullName.getText().isEmpty()){
            DialogUtils.showErrorDialog("Name cannot be empty");
            return false;
        }
        
        if(tfAddress.getText().isEmpty()){
            DialogUtils.showErrorDialog("Address cannot be empty");
            return false;
        }
        
        if(Utils.isNotValidNumber(tfPhoneNumber.getText())){
            return false;
        }
        
        if(tfEmailAddress.getText().isEmpty()){
            DialogUtils.showErrorDialog("Email cannot be empty");
            return false;
        }
        
        if(tfUserName.getText().isEmpty()){
            DialogUtils.showErrorDialog("Username cannot be empty");
            return false;
        }
        
        if(Utils.isNotValidPassword(pfPassword.getText(), pfRePassword.getText())){
            return false;
        }
        
        if(dPInterViewDate.getValue()==null){
            DialogUtils.showErrorDialog("Please select interview date");
            return false;
        }
        
        if(cBoxQualification.getValue()==null){
            DialogUtils.showErrorDialog("Please select Qualification");
            return false;
        }
        
        return true;
    }
    
}
