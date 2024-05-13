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
import model.Recruit;

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
    private TextField tfStaffName;
    @FXML
    private TextField tfStudentId;
    @FXML
    private ChoiceBox<String> cboxRecruits;
    @FXML
    private ChoiceBox<String> cBoxUniversity;
    
    private List<Recruit> mRecruits;
    @FXML
    private Label labelRecruitNotFound;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
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

    
    public void navigateBackToOriginalView() {
        // Replace the content of anchorPaneDashboard with the original content
        

       
    }

    @FXML
    private void showRecruitList(Event event) {
       
       recruitLayout.getChildren().clear();
       try{
       mRecruits = new ArrayList<>(readRecruitsFromCSV(Constants.RECRUIT_CSV_FILE));
       labelRecruitNotFound.setVisible(false);
       for(int i=0;i<mRecruits.size();i++){
       
           FXMLLoader loader = new FXMLLoader(App.class.getResource("recruititem_manager.fxml")); 
          
           HBox hbox = loader.load();
           RecruitItemManagerController controller = loader.getController();
           controller.setData(mRecruits.get(i));
           recruitLayout.getChildren().add(hbox);
//           
          
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
   
    
    
public ArrayList<Recruit> readRecruitsFromCSV(String filePath) throws IOException {
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
            // Handle file not found exception
            throw e;
         }
        
        catch (IOException e) {
            throw e; 
        }
        
        return recruits;
    }
    
    public boolean validateStudentIdForQualification(String studentId){
        if(studentId.isEmpty()){
            return false;
        }
        
        return studentId.length() >= 5;
    }
    
     public boolean validateUniversityForQualification(String university){
       if(university==null || university.isEmpty()){
           return false;
       }
       return true;
    }
    
    
    public boolean validateRecruitUserNameForQualification(String userName){
        if(userName==null || userName.isEmpty()){
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
        
        if(validateStudentIdForQualification(tfStudentId.getText()) && validateUniversityForQualification(cBoxUniversity.getValue()) && 
                validateRecruitUserNameForQualification(cboxRecruits.getValue())){
           
            DialogUtils.showGenerateReport("Verifying Qualification","Qualification Verified");

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
       
        
        if(mRecruits!=null && !mRecruits.isEmpty()){
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
