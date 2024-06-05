package client;

/**
 *
 * @author gauravdahal
 */
import aisr.model.AdminStaff;
import aisr.model.ManagementStaff;
import aisr.model.Recruit;
import aisr.model.SessionUser;
import com.gauravdahal.ais.r.initial.AddrecruitController;
import com.gauravdahal.ais.r.initial.AdminDashboardController;
import com.gauravdahal.ais.r.initial.LoginController;
import com.gauravdahal.ais.r.initial.ManagementDashboardController;
import com.gauravdahal.ais.r.initial.RecruitDashboardController;
import com.gauravdahal.ais.r.initial.RecruitRegistrationController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import session.SessionManager;

public class ClientConnection {

    private static ClientConnection instance;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private SocketDataIn socketDataIn;

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6789;

    private ClientConnection() {
        // Private constructor to prevent instantiation
    }

    public static synchronized ClientConnection getInstance() {
        if (instance == null) {
            instance = new ClientConnection();
        }
        return instance;
    }

    public boolean toggleConnection() {
        if (socket != null && socket.isConnected()) {
            disconnect();
            return false;
        } else {
            connect();
            return true;
        }
    }

    private void connect() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            socketDataIn = new SocketDataIn(in);
            socketDataIn.start();
            System.out.println("Connected to the server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + e.getMessage());
        } catch (ConnectException e) {
            System.out.println("Connection refused: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }

    private void disconnect() {
        try {

            if (socketDataIn != null) {
                socketDataIn.interrupt(); // Interrupt the data reading thread

            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
            System.out.println("Disconnected from the server.");
        } catch (IOException e) {
            System.out.println("Error while disconnecting: " + e.getMessage());
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

}

class SocketDataIn extends Thread {

    private final ObjectInputStream in;

    public SocketDataIn(ObjectInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Object obj = in.readObject();
                String command = (String) obj;

                switch (command) {
                    case "ADD_ADMIN":
                        AdminStaff adminStaff = (AdminStaff) in.readObject();
                        System.out.println(adminStaff);
                        break;

                    case "ADD_MANAGEMENT":
                        ManagementStaff managementStaff = (ManagementStaff) in.readObject();
                        System.out.println(managementStaff);
                        break;

                    case "ADD_RECRUIT":
                        Recruit recruit = (Recruit) in.readObject();
                        System.out.println(recruit);
                        break;

                    case "RECRUIT_LIST":
                        ArrayList<Recruit> recruits = (ArrayList<Recruit>) in.readObject();
                        Platform.runLater(() -> {
                            AdminDashboardController.updateRecruitTable(recruits);
                            ManagementDashboardController.updateRecruitTable(recruits);
                        });
                        break;

                    case "STAFF_TYPE":
                        String staffType = (String) in.readObject();
                        System.out.println("The staff type is "+staffType);
                        Platform.runLater(() -> {
                            try {
                                LoginController.navigateToStaffDashboard(staffType);
                            } catch (IOException e) {
                                System.out.println("I/O error: from here " + e.getMessage());
                            }
                        });
                        break;

                    case "GET_ADMIN_INFO":
                        AdminStaff adminInfo = (AdminStaff) in.readObject();
                        Platform.runLater(() -> AdminDashboardController.setAdminStaff(adminInfo));
                        break;
                    
                    case "GET_MANAGEMENT_INFO":
                        ManagementStaff mgmtInfo = (ManagementStaff) in.readObject();
                     System.out.println("The mgmt staff from client is "+mgmtInfo);

                        Platform.runLater(() -> ManagementDashboardController.setManagementStaff(mgmtInfo));
                        break;

                    case "ADD_RECRUIT_RESPONSE":
                        boolean isDuplicateEmail = (boolean) in.readObject();
                        String email = (String) in.readObject();
                        Platform.runLater(() -> RecruitRegistrationController.onResponseFromServer(isDuplicateEmail, email));
                        break;

                    case "ADD_RECRUIT_BATCH_RESPONSE":
                        boolean isDuplicateBatchEmail = (boolean) in.readObject();
                        String batchEmail = (String) in.readObject();
                        Platform.runLater(() -> AddrecruitController.onResponseFromServer(isDuplicateBatchEmail, batchEmail));
                        break;

                    case "RECRUIT_LOGIN_SUCCESS":
                        Recruit loginRecruit = (Recruit) in.readObject();
                        SessionManager sessionManager = SessionManager.getInstance();
                        SessionUser user = new SessionUser(loginRecruit.getEmailAddress(), loginRecruit);
                        sessionManager.setCurrentUser(user);
                        Platform.runLater(() -> {
                            try {
                                LoginController.handleRecruitLoginSuccess();
                            } catch (IOException ex) {
                                Logger.getLogger(SocketDataIn.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        break;

                    case "RECRUIT_LOGIN_FAILED":
                        Platform.runLater(LoginController::handleRecruitLoginFailed);
                        break;

                    case "UPDATE_RECRUIT_PASSWORD_RESPONSE":
                        boolean updateSuccess = (boolean) in.readObject();
                        Platform.runLater(() -> RecruitDashboardController.handlePasswordUpdateResponse(updateSuccess));
                        break;

                    case "UPDATE_RECRUIT_RESPONSE":
                        boolean recruitUpdateSuccess = (boolean) in.readObject();
                        Platform.runLater(() -> RecruitDashboardController.handleRecruitUpdateResponse(recruitUpdateSuccess));
                        break;

                    default:
                        System.out.println("Unknown command: " + command);
                        break;
                }
            } catch (SocketException e) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Socket closed, stopping data input thread.");
                    break;
                }
                System.out.println("Socket exception: " + e.getMessage());
            } catch (IOException e) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Socket closed, stopping data input thread.");
                    break;
                }
                System.out.println("I/O error: " + e.getMessage());
                break;
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found: " + e.getMessage());
                break;
            }
        }
    }
}
