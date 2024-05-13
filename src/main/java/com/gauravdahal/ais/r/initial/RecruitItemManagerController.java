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
import model.Recruit;

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
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editrecruit.fxml"));
        AnchorPane newScrollPane = loader.load();
        
        // Pass the recruit data to the controller of the new FXML file
        EditrecruitController controller = loader.getController();
        controller.setData(recruit);

        // Replace the parent AnchorPane with the new AnchorPane
        AnchorPane parentAnchorPane = (AnchorPane) hBoxEachItem.getParent().getParent(); // Assuming the parent is an AnchorPane
        parentAnchorPane.getChildren().setAll(newScrollPane);
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
