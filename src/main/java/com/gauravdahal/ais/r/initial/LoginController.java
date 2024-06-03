/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import Constants.Constants;
import ENUM.QualificationLevel;
import ENUM.StaffType;
import ENUM.UserType;
import Utils.DialogUtils;
import Utils.EncryptionUtils;
import aisr.model.Recruit;
import aisr.model.Token;
import client.ClientConnection;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class for handling login functionality.
 *
 * @author gauravdahal
 */
public class LoginController implements Initializable {

    @FXML
    private TextField tfEmail;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;
    
    private UserType selectedUserType;
    private StaffType staffType;
    @FXML
    private Button connectionButton;
    @FXML
    private Button btnRegister1;
    @FXML
    private ChoiceBox<String> cBoxUserType;
    @FXML
    private Text labelToken;
    @FXML
    private TextField tfoneTimeToken;
    /**
     * Initializes the controller class.
     */
    
    private boolean isTokenRequested = false;
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //set choicebox user login type
        selectedUserType = UserType.Staff;
        cBoxUserType.setItems(FXCollections.observableArrayList(
                UserType.Staff.label,
                 UserType.Recruit.label
        ));
        
        if(ClientConnection.getInstance().getSocket()!=null){
             connectionButton.setText("Disconnect from Server");
        }
        else{
             connectionButton.setText("Connect to Server");
        }
        
        cBoxUserType.setValue(UserType.Staff.label);

        cBoxUserType.setOnAction(event -> {
            String userType = cBoxUserType.getValue();
            if (userType.equals(UserType.Staff.label)) {
                hideTokenFields();
                selectedUserType = UserType.Staff;
          
            } else {
                showTokenFields();
                selectedUserType = UserType.Recruit;
            }
            // You can perform any other actions based on the selected position here
        });
        
        
        
        
    }    

    @FXML
    private void onLoginClicked(ActionEvent event)  {
        String email = tfEmail.getText();
        String password = tfPassword.getText();
        
        if(!emailPassValid(email,password)){
            DialogUtils.showWarningDialog("Email or Password cannot be blank");
            return;
        }
        
        
        if(selectedUserType.equals(UserType.Recruit) && !isTokenRequested){
            sendTokenToServer(tfEmail.getText());
            return;
        }
        
        if(selectedUserType.equals(UserType.Recruit) && isTokenRequested){
            
            if(tfoneTimeToken.getText().isEmpty()){
                DialogUtils.showWarningDialog("Please enter one time token");
            }
            else{
                System.out.print("Recruit Login");

            }
            //handle recruit login
        }
        
        else {
             System.out.print("Staff Login");
        }
    }
    
    
    private void sendTokenToServer(String email) {
        ClientConnection clientConnection = ClientConnection.getInstance();

        if (clientConnection.getSocket() == null || !clientConnection.getSocket().isConnected()) {
            DialogUtils.showWarningDialog("Client is disconnected. Connect to the Server first");
            System.out.println("Client is disconnected. Connect to the Server first");
            return;
        }
        
        try {
            clientConnection.getOut().writeObject("SEND_TOKEN"); // Send command to add admin staff
            clientConnection.getOut().writeObject(new Token(email)); // Send admin staff object
            clientConnection.getOut().flush();
            tfoneTimeToken.clear();
            DialogUtils.showSuccessDialog("Token Requested Successfully");
            isTokenRequested = true;
            tfoneTimeToken.setDisable(false);
            btnLogin.setText("Login");
        } catch (EOFException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("readline: " + e.getMessage());
        }
    }
    
    @FXML
    private void onRegistrationClicked(ActionEvent event) throws IOException {
        App.setRoot("registration");
    }
     
    
     /**
     * Validates email and password fields.
     * 
     * @param email    The email entered by the user.
     * @param password The password entered by the user.
     * @return True if email and password are valid, false otherwise.
     */
     public boolean emailPassValid(String email,String password){
        if(email.isEmpty()) return false;
        
        return !password.isEmpty();
    }
     
    /**
     * Authenticates user credentials.
     * 
     * @param email    The email entered by the user.
     * @param password The password entered by the user.
     * @return True if authentication is successful, false otherwise.
     * @throws IOException If an I/O error occurs while reading the staff data.
     */
    private boolean authenticate(String email, String password) throws IOException {
        String line = "";  
        String splitBy = ",";  

        try (BufferedReader br = new BufferedReader(new FileReader(Constants.STAFF_CSV_FILE))) {
        // Skip the first line (assumed to be headers)
         br.readLine(); 

            while ((line = br.readLine()) != null) {
             String[] staff = line.split(splitBy); 

              String emailFromCSV = staff[3].trim().replaceAll("^\"|\"$", ""); // Remove leading and trailing quotes
               String passwordFromCSV = staff[5].trim().replaceAll("\"", "");

         

                if (email.equals(emailFromCSV) && password.equals(EncryptionUtils.decrypt(passwordFromCSV))) {
                 if(staff[7].trim().replaceAll("^\"|\"$", "").equals(StaffType.ADMIN.label)){
                    staffType = StaffType.ADMIN;
                 }
                
                else{
                    staffType = StaffType.MANAGEMENT;
                }
                return true;
            } 
        }
    } 
        catch(FileNotFoundException e){
            throw e;
        }
        catch (IOException e) {
        // Handle any IO exception appropriately
        throw e;
    }

    // No matching credentials found, return false
    return false;
    }
    

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onConnect(ActionEvent event) {
        ClientConnection clientConnection = ClientConnection.getInstance();
        boolean isConnected = clientConnection.toggleConnection();
        if(isConnected){
            connectionButton.setText("Disconnect from Server");
        }
        else{
            connectionButton.setText("Connect to Server");
        }
      
    }

    @FXML
    private void onRecruitRegistrationClicked(ActionEvent event) throws IOException {
        App.setRoot("recruit_registration");
    }
    
    private void showTokenFields(){
        if(!isTokenRequested){
           btnLogin.setText("Request Token");
        }
        labelToken.setVisible(true);
        tfoneTimeToken.setVisible(true);
    }
    
    private void hideTokenFields(){
        btnLogin.setText("Login");
        labelToken.setVisible(false);
        tfoneTimeToken.setVisible(false);
    }
    
}
