package com.gauravdahal.ais.r.initial;

import ENUM.ManagementLevel;
import ENUM.Position;
import ENUM.StaffType;
import ENUM.UserType;
import Utils.DialogUtils;
import aisr.model.AdminStaff;
import aisr.model.ManagementStaff;
import aisr.model.StaffLists;
import aisr.model.Token;
import client.ClientConnection;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import logger.Logger;

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

    private UserType selectedUserType;
    private StaffType staffType;
    @FXML
    private Button connectionButton;
    @FXML
    private Button btnRegister1;
    @FXML
    private ChoiceBox<String> cBoxUserType;
    @FXML
    private Text labelToken;
    @FXML
    private TextField tfoneTimeToken;
    /**
     * Initializes the controller class.
     */

    private boolean isTokenRequested = false;

    private ClientConnection clientConnection;

    private static LoginController instance;

    private static String oneTimeToken = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        //set choicebox user login type
        selectedUserType = UserType.Staff;
        cBoxUserType.setItems(FXCollections.observableArrayList(
                UserType.Staff.label,
                UserType.Recruit.label
        ));

        if (ClientConnection.getInstance().getSocket() != null) {
            connectionButton.setText("Disconnect from Server");
        } else {
            connectionButton.setText("Connect to Server");
        }

        cBoxUserType.setValue(UserType.Staff.label);

        cBoxUserType.setOnAction(event -> {
            String userType = cBoxUserType.getValue();
            if (userType.equals(UserType.Staff.label)) {
                hideTokenFields();
                selectedUserType = UserType.Staff;

            } else {
                showTokenFields();
                selectedUserType = UserType.Recruit;
            }
            // You can perform any other actions based on the selected position here
        });

    }

    @FXML
    private void onLoginClicked(ActionEvent event) throws IOException {
        String email = tfEmail.getText();
        String password = tfPassword.getText();

        if (!emailPassValid(email, password)) {
            DialogUtils.showWarningDialog("Email or Password cannot be blank");
            return;
        }

        if (selectedUserType.equals(UserType.Recruit) && !isTokenRequested) {
            sendTokenToServer(tfEmail.getText());
            return;
        }

        if (selectedUserType.equals(UserType.Recruit) && isTokenRequested) {

            if (tfoneTimeToken.getText().isEmpty()) {
                DialogUtils.showWarningDialog("Please enter one time token");
            } else {
                verifyLogin(email, password, UserType.Recruit);
            }
            //handle recruit login
        } else {

            //handle staff login
            if (!isClientConnected()) {
                return;
            }

            verifyLogin(email, password, UserType.Staff);

        }
    }

    public static void navigateToStaffDashboard(String staffType) throws IOException {

        if (instance != null) {
            if (staffType != null && staffType.equals("ADMIN")) {
                Logger.log("LOGIN SUCCESS");
                DialogUtils.showSuccessDialog("Login Success");
                App.setRoot("admin_dashboard");
            } else if (staffType != null && staffType.equals("MANAGEMENT")) {
                DialogUtils.showSuccessDialog("Login Success");
                App.setRoot("management_dashboard");
            } else {
                Logger.log("LOGIN FAILED");
                DialogUtils.showErrorDialog("Invalid Credentials");
            }
        }

    }

    public static void handleStaffIntializationComplete() {
        DialogUtils.showSuccessDialog("For the first use of the program, the system initializes the initial user from a CSV file.");
    }

    private void sendTokenToServer(String email) {
        if (!isClientConnected()) {
            return;
        }

        try {
            Token token = new Token(email);
            clientConnection.getOut().writeObject("SEND_TOKEN"); // Send command to add admin staff
            clientConnection.getOut().writeObject(token); // Send admin staff object
            clientConnection.getOut().flush();
            tfoneTimeToken.clear();
            DialogUtils.showSuccessDialog("Token Requested Successfully");
            isTokenRequested = true;
            oneTimeToken = token.getGenerated_token();
            tfoneTimeToken.setDisable(false);
            btnLogin.setText("Login");
        } catch (EOFException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("readline: " + e.getMessage());
        }
    }

    private void verifyLogin(String email, String password, UserType userType) {
        try {
            clientConnection.getOut().writeObject("VERIFY_LOGIN_" + userType.toString()); // Send command to add admin staff
            clientConnection.getOut().writeObject(email); // Send admin staff object
            clientConnection.getOut().writeObject(password); // Send admin staff object
            clientConnection.getOut().flush();
        } catch (EOFException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            DialogUtils.showErrorDialog(e.getMessage());
            System.out.println("readline: " + e.getMessage());
        }
    }

    private boolean isClientConnected() {
        clientConnection = ClientConnection.getInstance();

        if (clientConnection.getSocket() == null || !clientConnection.getSocket().isConnected()) {
            DialogUtils.showWarningDialog("Client is disconnected. Connect to the Server first");
            System.out.println("Client is disconnected. Connect to the Server first");
            return false;
        }

        return true;
    }

    @FXML
    private void onRegistrationClicked(ActionEvent event) throws IOException {
        if (!isClientConnected()) {
            return;
        }
        App.setRoot("registration");
    }

    /**
     * Validates email and password fields.
     *
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     * @return True if email and password are valid, false otherwise.
     */
    public boolean emailPassValid(String email, String password) {
        if (email.isEmpty()) {
            return false;
        }

        return !password.isEmpty();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onConnect(ActionEvent event) throws IOException {
        ClientConnection clientConnection = ClientConnection.getInstance();
        boolean isConnected = clientConnection.toggleConnection();
        if (isConnected) {
            initializeStaff();
            connectionButton.setText("Disconnect from Server");

        } else {
            connectionButton.setText("Connect to Server");
        }

    }

    public void initializeStaff() throws IOException {
        if (!isClientConnected()) {
            System.out.println("Server is not connected!!!");
        }

        StaffLists staffLists = loadCSVData("staff.csv");

        clientConnection.getOut().writeObject("INTIALIZE_STAFFS");
        clientConnection.getOut().writeObject(staffLists);
        clientConnection.getOut().flush();
    }

    /**
     * Loads staff data from a CSV file.
     *
     * @param filePath the file path of the CSV file
     */
    private StaffLists loadCSVData(String filePath) {
        List<AdminStaff> adminStaffList = new ArrayList<>();
        List<ManagementStaff> managementStaffList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();  // Skip the header line
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] data = line.split(",");
                if (data.length < 11) {
                    data = Arrays.copyOf(data, 11);
                    for (int i = 0; i < data.length; i++) {
                        if (data[i] == null) {
                            data[i] = "";
                        }
                    }
                }
                String fullName = data[0];
                String address = data[1];
                String phoneNumber = data[2];
                String email = data[3];
                String userName = data[4];
                String password = data[5];
                String staffId = data[6];
                String staffType = data[7].trim();
                String positionType = data[8].trim();
                String managementLvl = data[9].trim();
                String branch = data[10];

                if (staffType.equals(StaffType.ADMIN.getLabel())) {
                    AdminStaff adminStaff = new AdminStaff(fullName, address, phoneNumber, email, userName, password, staffId, "".equals(positionType) ? null : Position.getPositionFromLabel(positionType));
                    adminStaffList.add(adminStaff);
                } else {
                    ManagementStaff managementStaff = new ManagementStaff(fullName, address, phoneNumber, address, userName, password, staffId, "".equals(managementLvl) ? null : ManagementLevel.getManagementFromLabel(managementLvl), branch);
                    managementStaffList.add(managementStaff);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new StaffLists(adminStaffList, managementStaffList);
    }

    @FXML
    private void onRecruitRegistrationClicked(ActionEvent event) throws IOException {
        if (!isClientConnected()) {
            return;
        }
        App.setRoot("recruit_registration");
    }

    private void showTokenFields() {
        if (!isTokenRequested) {
            btnLogin.setText("Request Token");
        }
        labelToken.setVisible(true);
        tfoneTimeToken.setVisible(true);
    }

    private void hideTokenFields() {
        btnLogin.setText("Login");
        labelToken.setVisible(false);
        tfoneTimeToken.setVisible(false);
    }

    public static void handleRecruitLoginSuccess() throws IOException {
        if (instance.tfoneTimeToken.getText().equals(oneTimeToken)) {
            instance.tfoneTimeToken.clear();
            DialogUtils.showSuccessDialog("Login Success");
            App.setRoot("recruit_dashboard");
        } else {
            DialogUtils.showErrorDialog("Invalid Token");
        }

    }

    public static void handleRecruitLoginFailed() {
        DialogUtils.showErrorDialog("Invalid Credentials");
    }
}
