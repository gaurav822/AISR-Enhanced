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
import client.ClientConnection;
import database.DatabaseHelper;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
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
    private static final int SERVER_PORT = 6789;
    
    private Socket socket = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;
   
       
    
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
            if(Utils.isDuplicateStaffId(tfStaffId.getText(),adminStaffs,managementStaffs)) {
                DialogUtils.showErrorDialog("Staff ID already exists. Please use a different Staff ID.");
                return;
            }
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
            
        }
        
        
    }
    
    
private void sendDataToServer() {
    
     ClientConnection clientConnection = ClientConnection.getInstance();
    
     if (clientConnection.getSocket() == null || !clientConnection.getSocket().isConnected()) {
            DialogUtils.showWarningDialog("Client is disconnected. Connect to the Server first");
            System.out.println("Client is disconnected. Connect to the Server first");
            return;
        }

        try {
            for (AdminStaff adminStaff : adminStaffs) {
                String encryptedPassword = EncryptionUtils.encrypt(adminStaff.getPassword());
                adminStaff.setPassword(encryptedPassword);
                clientConnection.getOut().writeObject("ADD_ADMIN"); // Send command to add admin staff
                clientConnection.getOut().writeObject(adminStaff); // Send admin staff object
            }

            for (ManagementStaff managementStaff : managementStaffs) {
                String encryptedPassword = EncryptionUtils.encrypt(managementStaff.getPassword());
                managementStaff.setPassword(encryptedPassword);
                clientConnection.getOut().writeObject("ADD_MANAGEMENT"); // Send command to add management staff
                clientConnection.getOut().writeObject(managementStaff); // Send management staff object
            }
            
            clientConnection.getOut().flush();
            DialogUtils.showSuccessDialog("Staffs Registered Successfully!");
            adminStaffs.clear();
            managementStaffs.clear();

        } catch (EOFException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("readline: " + e.getMessage());
        }
    }

}

// Thread for handling incoming data from the server
class SocketDataIn extends Thread {

    private final ObjectInputStream in;

    public SocketDataIn(ObjectInputStream in) {
        this.in = in;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
                Object objori = in.readObject();
                String command = (String) objori;
                if (command.equals("ADD_ADMIN")) {
                    AdminStaff adminStaff = (AdminStaff) in.readObject();
                    System.out.println(adminStaff);
                } else if (command.equals("ADD_MANAGEMENT")) {
                    ManagementStaff managementStaff = (ManagementStaff) in.readObject();
                    System.out.println(managementStaff);
                }

                //Wait for one sec so it doesn't print too fast
            } 
            catch (SocketException | InterruptedException e) {
                System.out.println("Interrupted:" + e.getMessage() + "\n");
                break;
            } catch (IOException ex) {
//                 System.out.println("""
//                            IO Exception: Server might have been stopped!""");
                break;
            } catch (ClassNotFoundException ex) {
                 System.out.println("Class Not Found:" + ex.getMessage() + "\n");
                break;
            }
            
        }
    }



}



