/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import Constants.Constants;
import Utils.DialogUtils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Recruit;

/**
 * FXML Controller class
 *
 * @author gauravdahal
 */
public class AdminDashboardController implements Initializable {

    @FXML
    private Tab tabDashboard;
    @FXML
    private AnchorPane anchorPaneDashboard;
    @FXML
    private VBox recruitLayout;
    
    @FXML
    private AnchorPane recruitListAnchorPane;
    @FXML
    private Label labelRecruitNotFound;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    private void addNewRecruit(ActionEvent event) {
        
        try {
            // Load the new FXML file containing the AnchorPane
            FXMLLoader loader = new FXMLLoader(App.class.getResource("addrecruit.fxml"));
            Parent newAnchorPaneRoot = loader.load();

            // Retrieve the AnchorPane from the loaded FXML
            AnchorPane newAnchorPane = (AnchorPane) newAnchorPaneRoot;

            // Replace the content of anchorPaneDashboard with the content of the loaded AnchorPane
            anchorPaneDashboard.getChildren().setAll(newAnchorPane.getChildren());
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @FXML
    private void showDashboard(Event event) {
        
    }
    
    public void navigateBackToOriginalView() {
        // Replace the content of anchorPaneDashboard with the original content
        

       
    }

    @FXML
    private void showRecruitList(Event event) {
       
       recruitLayout.getChildren().clear();
       try{
       List<Recruit> recruits = new ArrayList<>(readRecruitsFromCSV(Constants.RECRUIT_CSV_FILE));  
       labelRecruitNotFound.setVisible(false);
       for(int i=0;i<recruits.size();i++){
       
           FXMLLoader loader = new FXMLLoader(App.class.getResource("recruititem.fxml")); 
           
               HBox hbox = loader.load();
               RecruitItemController controller = loader.getController();
               controller.setData(recruits.get(i));
               recruitLayout.getChildren().add(hbox);
           
         }
       
       } 
       
       catch(FileNotFoundException e){
             labelRecruitNotFound.setVisible(true);
              System.out.println("File not found");
           
       }
       
       catch(IOException e){
              System.out.println("IOException Occured");
           
       }
    }
   
    
    
    public static ArrayList<Recruit> readRecruitsFromCSV(String filePath) throws IOException {
        ArrayList<Recruit> recruits = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Flag to skip the header line
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }
                String[] data = line.split(","); // Assuming CSV fields are comma-separated
                if (data.length == 14) { // Ensure correct number of fields
                    String fullName = data[0].trim().replaceAll("^\"|\"$", "");
                    String address = data[1].trim().replaceAll("^\"|\"$", "");
                    String phoneNumber = data[2].trim().replaceAll("^\"|\"$", "");
                    String emailAddress = data[3].trim().replaceAll("^\"|\"$", "");
                    String userName = data[4].trim().replaceAll("^\"|\"$", "");
                    String password = data[5].trim().replaceAll("^\"|\"$", "");
                    String interviewDate = data[6].trim().replaceAll("^\"|\"$", "");
                    String qualificationLevel = data[7].trim().replaceAll("^\"|\"$", "");
                    String department = data[8].trim().replaceAll("^\"|\"$", "");
                    String branch = data[9].trim().replaceAll("^\"|\"$", "");
                    String staffId = data[10].trim().replaceAll("^\"|\"$", "");
                    String staffName = data[11].trim().replaceAll("^\"|\"$", "");
                    String dateDataAdded = data[12].trim().replaceAll("^\"|\"$", "");
                    String staffBranch = data[13].trim().replaceAll("^\"|\"$", "");
                    
                    // Create Recruit object and add it to the list
                    Recruit recruit = new Recruit(fullName, address, phoneNumber, emailAddress, userName, password);
                    recruit.setInterviewDate(interviewDate);
                    recruit.setQualificationLevel(qualificationLevel);
                    recruit.setDepartment(department);
                    recruit.setBranch(branch);
                    recruit.setStaffId(staffId);
                    recruit.setStaffName(staffName);
                    recruit.setDateDataAdded(dateDataAdded);
                    recruit.setStaffBranch(staffBranch);
                    
                    recruits.add(recruit);
                }
            }
        } 
        
         catch (FileNotFoundException e) {
             throw e;
     }
        
        
        catch (IOException e) {
           throw e;
        }
        
        return recruits;
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
    private void onProfileUpdate(ActionEvent event) {
        
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

}
