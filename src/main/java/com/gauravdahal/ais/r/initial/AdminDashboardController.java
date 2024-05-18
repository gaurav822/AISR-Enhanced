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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import aisr.model.Recruit;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gauravdahal
 */
public class AdminDashboardController implements Initializable {

   
   private VBox recruitLayout;
    
    @FXML
    private TableView<Recruit> tableViewRecruit;
    
    @FXML
    private TableColumn<Recruit, String> columnFullName = new TableColumn<>("Full Name");
    @FXML    
    private TableColumn<Recruit, String> columnPhoneNumberR = new TableColumn<>("Phone Number");
    @FXML
    private TableColumn<Recruit, String> columnEmail = new TableColumn<>("Email Address");
    @FXML
    private TableColumn<Recruit, String> columnAddress = new TableColumn<>("Address");
    
    @FXML
    private TableColumn<Recruit, String> columnDepartment = new TableColumn<>("Department");
    @FXML
    private TableColumn<Recruit, String> columnQualification = new TableColumn<>("Qualification");
     
    @FXML
    private TableColumn<Recruit, String> columnUserName = new TableColumn<>("Username");
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
         setupRecruitTable();
         
         //load data
         
         //setup factory
         setupRowFactoryForRecruit();
        
    }    
    
    @FXML
    private void addNewRecruit(ActionEvent event) throws IOException {
        
        App.setRoot("addrecruit");
    }

   
    
    
    private void setupRecruitTable() {
        tableViewRecruit.getColumns().add(columnFullName);
        tableViewRecruit.getColumns().add(columnPhoneNumberR);
        tableViewRecruit.getColumns().add(columnEmail);
        tableViewRecruit.getColumns().add(columnAddress);
        tableViewRecruit.getColumns().add(columnDepartment);
        tableViewRecruit.getColumns().add(columnQualification);
        tableViewRecruit.getColumns().add(columnUserName);

        columnFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        columnPhoneNumberR.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        columnQualification.setCellValueFactory(new PropertyValueFactory<>("qualificationLevel"));
        columnUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
    }

    @FXML
    private void showRecruitList(Event event) throws IOException {
       

       ObservableList<Recruit> recruits = (readRecruitsFromCSV(Constants.RECRUIT_CSV_FILE));  
       tableViewRecruit.setItems(recruits);
    }
    
    private void setupRowFactoryForRecruit() {
        tableViewRecruit.setRowFactory(tv -> {
            TableRow<Recruit> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Recruit selectedRecruit = row.getItem();
                    showRecruitDetails(selectedRecruit);
                }
            });
            return row;
        });
    }
    
     private void showRecruitDetails(Recruit recruit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editrecruit_admin.fxml"));
            Parent root = loader.load();

            EditrecruitAdminController controller = loader.getController();
            controller.setData(recruit);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Recruit");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    
    
    public static ObservableList<Recruit> readRecruitsFromCSV(String filePath) throws IOException {
        ObservableList<Recruit> recruits = FXCollections.observableArrayList();
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
