/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import Utils.DialogUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import aisr.model.Recruit;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gauravdahal
 */
public class RecruitItemManagerController implements Initializable {

    @FXML
    private Label labelName;
    @FXML
    private Label labelDate;
    @FXML
    private HBox hBoxEachItem;
    
    
    private Recruit recruit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onItemView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editrecruit.fxml"));
            Parent root = loader.load();

            EditrecruitController controller = loader.getController();
            controller.setData(recruit);

            // Get the current stage
            Stage stage = (Stage) hBoxEachItem.getScene().getWindow();
            // Set the new scene root
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
          
    }

    @FXML
    private void onItemDelete(ActionEvent event) {
        
    }
    
    public void setData(Recruit recruit){
        labelName.setText(recruit.getFullName());
        labelDate.setText(recruit.getDateDataAdded());
        this.recruit = recruit;
    }

    @FXML
    private void onReportGenerate(ActionEvent event) {
        
        DialogUtils.showGenerateReport("Generating Report","Report Generated Successfully!");
    }
    
   
    
}
