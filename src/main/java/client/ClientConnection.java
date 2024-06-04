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
import com.gauravdahal.ais.r.initial.RecruitRegistrationController;
import java.io.EOFException;
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
import javafx.collections.ObservableList;
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
                if ("ADD_ADMIN".equals(command)) {
                    AdminStaff adminStaff = (AdminStaff) in.readObject();
                    System.out.println(adminStaff);
                } else if ("ADD_MANAGEMENT".equals(command)) {
                    ManagementStaff managementStaff = (ManagementStaff) in.readObject();
                    System.out.println(managementStaff);
                } else if ("ADD_RECRUIT".equals(command)) {
                    Recruit recruit = (Recruit) in.readObject();
                    System.out.println(recruit);
                } else if ("RECRUIT_LIST".equals(command)) {
                    try {
                        ArrayList<Recruit> recruits = (ArrayList<Recruit>) in.readObject();
                        AdminDashboardController.updateRecruitTable(recruits);
                        ManagementDashboardController.updateRecruitTable(recruits);// Update the table view
                    } catch (ClassNotFoundException e) {
                        System.out.println("Class not found: " + e.getMessage());
                    }
                } else if ("STAFF_TYPE".equals(command)) {
                    try {
                        String staffType = (String) in.readObject();
                        Platform.runLater(() -> {
                            try {
                                LoginController.navigateToStaffDashboard(staffType); // Update the table view on FX
                                // thread
                            } catch (IOException e) {
                                System.out.println("I/O error: " + e.getMessage());
                            }
                        });
                    } catch (ClassNotFoundException e) {
                        System.out.println("Class not found: " + e.getMessage());
                    }
                } else if ("GET_ADMIN_INFO".equals(command)) {
                    try {
                        AdminStaff adminStaff = (AdminStaff) in.readObject();
                        AdminDashboardController.setAdminStaff(adminStaff); // Update the table view
                    } catch (ClassNotFoundException e) {
                        System.out.println("Class not found: " + e.getMessage());
                    }
                } else if ("ADD_RECRUIT_RESPONSE".equals(command)) {

                    try {
                        boolean isDuplicateEmail = (boolean) in.readObject();
                        String email = (String) in.readObject();

                        Platform.runLater(() -> {
                            RecruitRegistrationController.onResponseFromServer(isDuplicateEmail, email);
                        });
                    } catch (ClassNotFoundException e) {
                        System.out.println("Class not found: " + e.getMessage());
                    }
                } else if ("ADD_RECRUIT_BATCH_RESPONSE".equals(command)) {

                    try {

                        boolean isDuplicateEmail = (boolean) in.readObject();
                        String email = (String) in.readObject();
                        Platform.runLater(() -> {
                            AddrecruitController.onResponseFromServer(isDuplicateEmail, email);
                        });

                    } catch (ClassNotFoundException e) {
                        System.out.println("Class not found: " + e.getMessage());
                    }

                } else if ("RECRUIT_LOGIN_SUCCESS".equals(command)) {
                    Recruit recruit = (Recruit) in.readObject();

                    SessionManager sessionManager = SessionManager.getInstance();
                    SessionUser user = new SessionUser(recruit.getEmailAddress(), recruit); // Assuming you have a User class
                    sessionManager.setCurrentUser(user);

                    Platform.runLater(() -> {
                        try {
                            LoginController.handleRecruitLoginSuccess();
                        } catch (IOException ex) {
                            Logger.getLogger(SocketDataIn.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    });

                } else if ("RECRUIT_LOGIN_FAILED".equals(command)) {
                    Platform.runLater(() -> {
                        LoginController.handleRecruitLoginFailed();
                    });
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
