/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import Utils.EncryptionUtils;
import Utils.Utils;
import aisr.model.Recruit;
import aisr.model.RecruitCounts;
import database.DatabaseHelper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author gauravdahal
 */
public class GraphicalViewController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Create RecruitCounts instance (replace this with your actual data)
        
    }

    public void setDepartment(String department) {
        RecruitCounts recruitCounts = DatabaseHelper.getInstance().getRecruitsCounts(department);
        recruitCounts.printDetails();

        // Create a bar chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        // Add data to the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        String[] branchNames = RecruitCounts.branchNames;
        int[] branchCounts = recruitCounts.getBranchRecruitsCount();
        for (int i = 0; i < branchNames.length; i++) {
            series.getData().add(new XYChart.Data<>(branchNames[i], branchCounts[i]));
        }

        // Set chart title and data
        barChart.setTitle("Recruit Counts by Branch");
        barChart.getData().add(series);

        // Add the chart to the anchor pane
        anchorPane.getChildren().add(barChart);
    }


}
