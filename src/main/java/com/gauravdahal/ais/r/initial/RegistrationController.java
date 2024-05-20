/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import Constants.Constants;
import ENUM.BranchName;
import ENUM.ManagementLevel;
import ENUM.Position;
import ENUM.StaffType;
import Utils.DialogUtils;
import Utils.EncryptionUtils;
import Utils.Utils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import aisr.model.AdminStaff;
import aisr.model.ManagementStaff;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.application.Platform;
import javafx.concurrent.Task;
/**
 * FXML Controller class
 *
 * @author gauravdahal
 */
public class RegistrationController implements Initializable {


    @FXML
    private AnchorPane registration;
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
    private PasswordField tfPassword;
    @FXML
    private PasswordField tfRePassword;
    @FXML
    private TextField tfStaffId;
    @FXML
    private ChoiceBox<String> cBoxPosition;
    @FXML
    private ChoiceBox<String> cBoxStaffType;
    @FXML
    private ChoiceBox<String> cBoxMgmtLevel;
    @FXML
    private ChoiceBox<String> cBoxBranchName;
    
    private boolean areAllFieldsValid;
    private StaffType staffType;
    
    private ArrayList<AdminStaff> adminStaffs;
    private ArrayList<ManagementStaff> managementStaffs;
    
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;
    
       
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        adminStaffs = new ArrayList<>();
        managementStaffs = new ArrayList<>();
        staffType = StaffType.ADMIN;
        
        //setting position to choice box
        cBoxPosition.setItems(FXCollections.observableArrayList(
                Position.FULL_TIME.label,
                Position.PART_TIME.label,
                Position.VOLUNTEER.label
        ));
        
        //setting stafftype to choice box (admin or management)
        cBoxStaffType.setItems(FXCollections.observableArrayList(
                StaffType.ADMIN.label,
                StaffType.MANAGEMENT.label
        ));
        
        
        //setting management level to choice box 
         cBoxMgmtLevel.setItems(FXCollections.observableArrayList(
                ManagementLevel.SENIOR.label,
                 ManagementLevel.MIDLEVEL.label,
                 ManagementLevel.SUPERVISOR.label
        ));
         
         cBoxBranchName.setItems(FXCollections.observableArrayList(
                BranchName.MELBOURNE.toString(),
                 BranchName.SYDNEY.toString(),
                 BranchName.BRISBANE.toString(),
                 BranchName.ADELAIDE.toString()
        ));
         
         
        
        
        cBoxStaffType.setOnAction(event -> {
         String selectedStaffType = cBoxStaffType.getValue();
           if(selectedStaffType.equals(StaffType.MANAGEMENT.label)){
               staffType = StaffType.MANAGEMENT;
               cBoxPosition.setDisable(true);
               cBoxMgmtLevel.setDisable(false);
               cBoxBranchName.setDisable(false);
           }
           else{
               staffType = StaffType.ADMIN;
               cBoxPosition.setDisable(false);
                cBoxMgmtLevel.setDisable(true);
               cBoxBranchName.setDisable(true);
           }
            // You can perform any other actions based on the selected position here
        });
        
        
    }    
    
    private void saveDataToCSV() {
        try {
            File file = new File(Constants.STAFF_CSV_FILE);
            boolean isNewFile = !file.exists();
            FileWriter writer = new FileWriter(file, true); // true for append mode
            if (isNewFile) {
                // If it's a new file, write the header
                writer.write("Full Name,Address,Phone Number,Email Address,Username,Password,Staff ID,Staff Type,Position,Management Level,Branch Name\n");
            }
             
            for(AdminStaff staff:adminStaffs){
                String encryptedPassword = EncryptionUtils.encrypt(staff.getPassword()); 
                staff.setPassword(encryptedPassword);
               writer.write(staff.toString() + "\n");
            }
            for(ManagementStaff staff:managementStaffs){
                String encryptedPassword = EncryptionUtils.encrypt(staff.getPassword()); // Encrypt the password
                staff.setPassword(encryptedPassword); 
               writer.write(staff.toString() + "\n");
            }
      
            writer.close();
            DialogUtils.showSuccessDialog("Staff's has been registered Successfully !");
            App.setRoot("login");
        } catch (IOException e) {
            DialogUtils.showErrorDialog("Error occurred while saving data to CSV file.");
        }
    }
    
    public  AdminStaff getAdminDetails(){
        AdminStaff staff = new AdminStaff(tfFullName.getText(),
                            tfAddress.getText(),
                            tfPhoneNumber.getText(),
                            tfEmailAddress.getText(),
                            tfUserName.getText(),
                            tfPassword.getText());
        staff.setStaffId(tfStaffId.getText());
        staff.setPositionType(
                Position
                        .getPositionFromLabel(cBoxPosition.getValue()));
        return staff;
    }
    
    public ManagementStaff getManagementStaffDetails(){
        
        ManagementStaff staff = new ManagementStaff(tfFullName.getText(),
                            tfAddress.getText(),
                            tfPhoneNumber.getText(),
                            tfEmailAddress.getText(),
                            tfUserName.getText(),
                            tfPassword.getText());
        staff.setStaffId(tfStaffId.getText());
        staff.setManagementLevel(
                ManagementLevel
                        .getManagementFromLabel(cBoxMgmtLevel.getValue()));
        staff.setBranchName(cBoxBranchName.getValue());
        return staff;
        
    }

    @FXML
    private void onLoginClicked(ActionEvent event)throws IOException {
        App.setRoot("login");
    }
    
    public boolean checkForValidation(){
        
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
        
        if(Utils.isNotValidPassword(tfPassword.getText(), tfRePassword.getText())){
            return false;
        }
   
        if(cBoxStaffType.getValue()==null){
             DialogUtils.showErrorDialog("Staff Type cannot be empty");
              return false;
        }
        
        if(staffType==StaffType.ADMIN){
            if(cBoxPosition.getValue()==null){
                DialogUtils.showErrorDialog("Please Select Position type");
                return false;
            }
        }
        
        if(staffType==StaffType.MANAGEMENT){
            if(cBoxMgmtLevel.getValue()==null){
                DialogUtils.showErrorDialog("Please Select Management Level");
                 return false;
            }
            
            if(cBoxBranchName.getValue()==null){
                DialogUtils.showErrorDialog("Please Select Branch Name");
                return false;
            }
        }
        
      
        
        return true;
    }

    @FXML
    private void onStaffDetailsEntered(ActionEvent event) {
        if(checkForValidation()){
            
            if(staffType == StaffType.ADMIN){
                adminStaffs.add(getAdminDetails());
                DialogUtils.showSuccessDialog("Admin Staff Details Added, Don't Forget to save !!!");
            }
            
            else{
                managementStaffs.add(getManagementStaffDetails());
                DialogUtils.showSuccessDialog("Management Staff Details Added, Added, Don't Forget to save !!!");
            }
            clearFields();
        }
    }
    
    public void clearFields(){
        tfFullName.clear();
        tfAddress.clear();
        tfPhoneNumber.clear();
        tfEmailAddress.clear();
        tfUserName.clear();
        tfPassword.clear();
        tfRePassword.clear();
        tfStaffId.clear();
        cBoxStaffType.setValue(StaffType.ADMIN.label);
        cBoxPosition.setValue(null);
        cBoxMgmtLevel.setValue(null);
        cBoxBranchName.setValue(null);
        
    }

    @FXML
    private void onStaffDataSaved(ActionEvent event) {
        
        if(adminStaffs.isEmpty() && managementStaffs.isEmpty()){
            DialogUtils.showWarningDialog("Please enter the data first to arraylist");
        }
        
        else{
            try{
                sendDataToServer();
            }
            
            catch(Exception e){
                System.out.println(e);
            }
            adminStaffs.clear();
            managementStaffs.clear();
        }
        
        
    }
    
    
private void sendDataToServer() {
    Task<Void> task = new Task<>() {
        @Override
        protected Void call() throws Exception {
            try {
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Example: Adding admin staff
                for (AdminStaff adminStaff : adminStaffs) {
                    out.writeObject("ADD_ADMIN"); // Send command to add admin staff
                    out.writeObject(adminStaff); // Send admin staff object
                    out.flush();

                    // Wait for acknowledgment from the server
                    String response = in.readLine();
                    if (response != null && response.equals("SUCCESS")) {
                        Platform.runLater(() -> {
                            DialogUtils.showSuccessDialog("Admin Staff added successfully!");
                        });
                    } else {
                        Platform.runLater(() -> {
                            DialogUtils.showErrorDialog("Failed to add Admin Staff!");
                        });
                    }
                }

                // Close resources
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    Thread thread = new Thread(task);
    thread.setDaemon(true);
    thread.start();
}



}
