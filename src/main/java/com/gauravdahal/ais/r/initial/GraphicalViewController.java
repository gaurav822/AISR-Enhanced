package com.gauravdahal.ais.r.initial;

import Utils.EncryptionUtils;
import Utils.Utils;
import aisr.model.RecruitCounts;
import database.DatabaseHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class GraphicalViewController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic if necessary
    }

    public void setDepartment(String department) {
        
        System.out.println("View department is " + department);
        String[] assignedDepartments = department.split(", ");
        
        RecruitCounts recruitCounts = DatabaseHelper.getInstance().getRecruitsCounts(department);
        recruitCounts.printDetails();

        // Create bar charts
        BarChart<String, Number> branchBarChart = createBarChart("Recruit Counts by Branch");
        BarChart<String, Number> departmentBarChart = createBarChart("Recruit Counts by Department");

        // Add branch data to the chart
        XYChart.Series<String, Number> branchSeries = new XYChart.Series<>();
        String[] branchNames = RecruitCounts.branchNames;
        int[] branchCounts = recruitCounts.getBranchRecruitsCount();
        for (int i = 0; i < branchNames.length; i++) {
            branchSeries.getData().add(new XYChart.Data<>(branchNames[i], branchCounts[i]));
        }
        branchBarChart.getData().add(branchSeries);

        // Add department data to the chart
        XYChart.Series<String, Number> departmentSeries = new XYChart.Series<>();
        int[] departmentCounts = recruitCounts.getDepartmentRecruitsCount();
        for (int i = 0; i < assignedDepartments.length; i++) {
            departmentSeries.getData().add(new XYChart.Data<>(assignedDepartments[i], departmentCounts[i]));
        }
        departmentBarChart.getData().add(departmentSeries);


        // Create a VBox to hold the labels and the charts
        VBox vbox = new VBox(10); // 10 is the spacing between elements
        vbox.getChildren().add(branchBarChart);
        vbox.getChildren().add(departmentBarChart);
        // Add the VBox to the anchor pane
        anchorPane.getChildren().add(vbox);
    }

    private BarChart<String, Number> createBarChart(String title) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(title);
        return barChart;
    }
}
