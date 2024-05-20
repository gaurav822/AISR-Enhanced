/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import Constants.Constants;
import ENUM.StaffType;
import Utils.DialogUtils;
import Utils.EncryptionUtils;
import java.io.BufferedReader;
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
import javafx.scene.control.Alert;

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

    private StaffType staffType;
    /**
     * Initializes the controller class.
     */
    
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/AISRDB";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "gauravdahal";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onLoginClicked(ActionEvent event)  {
        String email = tfEmail.getText();
        String password = tfPassword.getText();
        
        if(!emailPassValid(email,password)){
            DialogUtils.showWarningDialog("Email or Password cannot be blank");
            return;
        }

        try {
            if (authenticate(email, password)) {
                DialogUtils.showSuccessDialog("Welcome");
                if(staffType.equals(StaffType.MANAGEMENT)){
                    App.setRoot("management_dashboard");
                }
                else{
                    App.setRoot("admin_dashboard");
                }
                
            } else {
                 DialogUtils.showErrorDialog("Invalid email or password");
            }
        } 
        
        catch (FileNotFoundException e) {
            DialogUtils.showErrorDialog("staff.csv not found, Please register staff first");
        }
        
        catch (IOException e) {
            DialogUtils.showErrorDialog("Error reading staff data");
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
    
    
     private void testConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
                showAlert(Alert.AlertType.INFORMATION, "Connection Successful", "Connection to the database was successful.");
            }
        } catch (ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Driver Not Found", "MySQL JDBC Driver not found.\n" + e.getMessage());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Connection Failed", "Failed to connect to the database.\n" + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onConnectionTest(ActionEvent event) {
        testConnection();
    }
    
}
