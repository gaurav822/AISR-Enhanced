package com.gauravdahal.ais.r.initial;

import ENUM.Position;
import Utils.DialogUtils;
import Utils.Utils;
import aisr.model.AdminStaff;
import aisr.model.ManagementStaff;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import aisr.model.Recruit;
import client.ClientConnection;
import java.io.EOFException;
import java.util.LinkedList;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logger.Logger;
import session.SessionManager;

/**
 * FXML Controller class
 *
 * @author gauravdahal
 */
public class AdminDashboardController implements Initializable {

    private VBox recruitLayout;

    @FXML
    private TableView<Recruit> tableViewRecruit;

    private TableColumn<Recruit, String> columnFullName = new TableColumn<>("Full Name");
    private TableColumn<Recruit, String> columnPhoneNumberR = new TableColumn<>("Phone Number");
    private TableColumn<Recruit, String> columnEmail = new TableColumn<>("Email Address");
    private TableColumn<Recruit, String> columnAddress = new TableColumn<>("Address");

    private TableColumn<Recruit, String> columnDepartment = new TableColumn<>("Department");
    private TableColumn<Recruit, String> columnQualification = new TableColumn<>("Qualification");

    private TableColumn<Recruit, String> columnUserName = new TableColumn<>("Username");

    private static AdminDashboardController instance;

    private static AdminStaff adminStaff;

    private TableColumn<ManagementStaff, String> columnMName = new TableColumn<>("Full Name");
    private TableColumn<ManagementStaff, String> columnMPhoneNumber = new TableColumn<>("Phone Number");
    private TableColumn<ManagementStaff, String> columnMEmail = new TableColumn<>("Email Address");
    private TableColumn<ManagementStaff, String> columnMStaffId = new TableColumn<>("Staff Id");
    private TableColumn<ManagementStaff, String> columnMUserName = new TableColumn<>("Username");
    private TableColumn<ManagementStaff, String> columnMAddress = new TableColumn<>("Address");
    private TableColumn<ManagementStaff, String> columnMbranch = new TableColumn<>("Branch");

    @FXML
    private TextField tfFullName;
    @FXML
    private TextField tfPhoneNumber;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfStaffId;
    @FXML
    private ChoiceBox<String> cBoxPositionType;
    @FXML
    private ChoiceBox<String> choiceBoxAnalytics;

    private LinkedList<Recruit> mRecruits;
    @FXML
    private TableView<ManagementStaff> tableViewManagement;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cBoxPositionType.setItems(FXCollections.observableArrayList(
                Position.FULL_TIME.label,
                Position.PART_TIME.label,
                Position.VOLUNTEER.label
        ));

        choiceBoxAnalytics.setItems(FXCollections.observableArrayList(
                "Recruits by Last Name",
                "Recruits by Qualification",
                "None"
        ));

        choiceBoxAnalytics.setOnAction(event -> {
            String selectedStaffType = choiceBoxAnalytics.getValue();
            if (selectedStaffType.equals("Recruits by Last Name")) {
                Logger.log("Viewed Recruits by Last Name : " + adminStaff.getFullName() + " : " + adminStaff.getStaffId());

                ObservableList<Recruit> observableRecruits
                        = FXCollections.observableList(
                                Utils.getRecruitsSortedByLastNameGroupedByLocation(mRecruits));
                instance.tableViewRecruit.setItems(observableRecruits);
            } else if (selectedStaffType.equals("Recruits by Qualification")) {
                Logger.log("Viewed Recruits by Qualificatione : " + adminStaff.getFullName() + " : " + adminStaff.getStaffId());

                ObservableList<Recruit> observableRecruits
                        = FXCollections.observableList(
                                Utils.getRecruitsSortedByQualification(mRecruits));
                instance.tableViewRecruit.setItems(observableRecruits);

            } else {
                ObservableList<Recruit> observableRecruits
                        = FXCollections.observableList(mRecruits);
                instance.tableViewRecruit.setItems(observableRecruits);

            }
            // You can perform any other actions based on the selected position here
        });

        setupRecruitTable();
        setupManagementTable();

        //load data
        //setup factory
        setupRowFactoryForRecruit();
        setupRowFactoryForManagement();
        instance = this;
        requestDataFromServer("GET_RECRUITS", null);// Set the instance
        requestDataFromServer("GET_ADMIN_INFO", SessionManager.getInstance().getCurrentUser().getEmail());

    }

    @FXML
    private void addNewRecruit(ActionEvent event) throws IOException {

        App.setRoot("addrecruit");
    }

    public static void updateRecruitTable(LinkedList<Recruit> recruits) {
        if (instance != null) {
            instance.mRecruits = recruits;
            ObservableList<Recruit> observableRecruits = FXCollections.observableList(recruits);
            instance.tableViewRecruit.setItems(observableRecruits);
        }
    }

    public static void setAdminStaff(AdminStaff staff) {
        adminStaff = staff;
        if (instance != null) {

            instance.tfFullName.setText(staff.getFullName());
            instance.tfEmail.setText(staff.getEmailAddress());
            instance.tfPhoneNumber.setText(staff.getPhoneNumber());
            instance.tfUserName.setText(staff.getUserName());
            instance.tfStaffId.setText(staff.getStaffId());
            instance.cBoxPositionType.setValue(staff.getPositionType().label);

        }
    }

    public static AdminStaff getAdminStaff(AddrecruitController controller) {
        return adminStaff;
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

    private void setupManagementTable() {
        tableViewManagement.getColumns().add(columnMName);
        tableViewManagement.getColumns().add(columnMPhoneNumber);
        tableViewManagement.getColumns().add(columnMEmail);
        tableViewManagement.getColumns().add(columnMStaffId);
        tableViewManagement.getColumns().add(columnMUserName);
        tableViewManagement.getColumns().add(columnMAddress);
        tableViewManagement.getColumns().add(columnMbranch);

        columnMName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        columnMPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        columnMEmail.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));
        columnMStaffId.setCellValueFactory(new PropertyValueFactory<>("staffId"));
        columnMUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        columnMAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnMbranch.setCellValueFactory(new PropertyValueFactory<>("branchName"));

    }

    @FXML
    private void showRecruitList(Event event) throws IOException {
        requestDataFromServer("GET_RECRUITS", null);// Set the instance
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

    private void setupRowFactoryForManagement() {
        tableViewManagement.setRowFactory(tv -> {
            TableRow<ManagementStaff> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
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

    private void requestDataFromServer(String requestObject, String email) {
        ClientConnection clientConnection = ClientConnection.getInstance();
        try {
            clientConnection.getOut().writeObject(requestObject);
            if (email != null && !email.isEmpty()) {
                clientConnection.getOut().writeObject(email);
            }
            clientConnection.getOut().flush();
        } catch (EOFException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("readline: " + e.getMessage());
        }
    }

    @FXML
    private void showProfile(Event event) {

        Logger.log("VIEWED PROFILE DETAILS : " + adminStaff.getFullName() + " : " + adminStaff.getStaffId());

    }

    @FXML
    private void showSettings(Event event) {
        Logger.log("VIEWED SETTINGS : " + adminStaff.getFullName() + " : " + adminStaff.getStaffId());

    }

    @FXML
    private void onLogout(ActionEvent event) throws IOException {
        Logger.log("LOGOUT SUCCESS : " + adminStaff.getFullName() + " : " + adminStaff.getStaffId());
        SessionManager.getInstance().setCurrentUser(null);
        DialogUtils.showSuccessDialog("Logout Successful");
        App.setRoot("login");
    }

    @FXML
    private void onProfileUpdate(ActionEvent event) {
        Logger.log("PROFILE UPDATED : " + adminStaff.getFullName() + " : " + adminStaff.getStaffId());
        DialogUtils.showSuccessDialog("Profile Updated Successfully");
    }

    @FXML
    private void onPasswordChange(ActionEvent event) {
        Logger.log("PASSWORD CHANGED : " + adminStaff.getFullName() + " : " + adminStaff.getStaffId());
        DialogUtils.showSuccessDialog("Password Changed Successfully");
    }

    @FXML
    private void onAccountDelete(ActionEvent event) throws IOException {
        DialogUtils.showSuccessDialog("Account deleted");
        App.setRoot("login");
    }

    @FXML
    private void showManagementList(Event event) {
        Logger.log("VIEWED MANAGEMENT STAFF DETAILS : " + adminStaff.getFullName() + " : " + adminStaff.getStaffId());
        ObservableList<ManagementStaff> staffs = FXCollections.observableArrayList(Utils.getManagementStaff());
        instance.tableViewManagement.setItems(staffs);
    }

}
