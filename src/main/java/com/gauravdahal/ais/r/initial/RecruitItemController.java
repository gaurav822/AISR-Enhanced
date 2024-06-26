/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import aisr.model.Recruit;

/**
 * FXML Controller class
 *
 * @author gauravdahal
 */
public class RecruitItemController implements Initializable {

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editrecruit_admin.fxml"));
        AnchorPane newScrollPane = loader.load();
        
        // Pass the recruit data to the controller of the new FXML file
        EditrecruitAdminController controller = loader.getController();
        controller.setData(recruit);

        // Replace the parent AnchorPane wsith the new AnchorPane
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
    
}
