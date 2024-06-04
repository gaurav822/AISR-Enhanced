package server;

/**
 *
 * @author gauravdahal
 */
import aisr.model.AdminStaff;
import aisr.model.ManagementStaff;
import aisr.model.Recruit;
import aisr.model.Token;
import database.DatabaseHelper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultiThreadedServer {

    public void start() {
        System.out.println("------------");

        // Start a new thread to handle client connections
        new Thread(() -> {
            try {
                int serverPort = 6789;
                ServerSocket listenSocket = new ServerSocket(serverPort);

                System.out.println("ComputeServer: Waiting for connections...");
                while (true) {
                    Socket clientSocket = listenSocket.accept();
                    Connection c = new Connection(clientSocket);
                    c.start();
                }
            } catch (IOException e) {
                System.out.println("Error in server socket: " + e.getMessage());
            }
        }).start();

        System.out.println("The server is listening on 6789.");
    }
}

/**
 * Represents a connection with a client.
 */
class Connection extends Thread {

    ObjectInputStream in;
    ObjectOutputStream out;
    Socket clientSocket;
    private String classPath;

    /**
     * Constructs a Connection object with the given client socket.
     *
     * @param aClientSocket The client socket.
     */
    public Connection(Socket aClientSocket) {
        this.classPath = System.getProperty("java.class.path");

        try {
            clientSocket = aClientSocket;
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error in connection setup: " + e.getMessage());
        }
    }

    /**
     * The main execution logic for the connection thread.
     */
    public void run() {
        while (true) {
            try {
                Object inputObject = in.readObject();
                if (inputObject == null) {
                    // End of stream reached, break out of the loop
                    break;
                }
                String command = (String) inputObject;
                if (command.equals("ADD_ADMIN")) {
                    AdminStaff adminStaff = (AdminStaff) in.readObject();
                    DatabaseHelper.getInstance().insertAdminStaff(adminStaff);
                } else if (command.equals("ADD_MANAGEMENT")) {
                    ManagementStaff managementStaff = (ManagementStaff) in.readObject();
                    DatabaseHelper.getInstance().insertManagementStaff(managementStaff);
                } else if (command.equals("ADD_RECRUIT")) {
                    Recruit recruit = (Recruit) in.readObject();
                    boolean isDuplicateEntry = DatabaseHelper.getInstance().insertRecruit(recruit);
                    out.writeObject("ADD_RECRUIT_RESPONSE");
                    out.writeObject(isDuplicateEntry);
                    out.writeObject(recruit.getEmailAddress());
                    out.flush();

                } else if (command.equals("ADD_RECRUIT_BATCH")) {
                    ArrayList<Recruit> recruits = (ArrayList<Recruit>) in.readObject();
                    for (Recruit recruit : recruits) {
                        boolean isDuplicateEntry = DatabaseHelper.getInstance().insertRecruit(recruit);
                        out.writeObject("ADD_RECRUIT_BATCH_RESPONSE");
                        out.writeObject(isDuplicateEntry);
                        out.writeObject(recruit.getEmailAddress());
                    }
                    out.flush();
                } else if (command.equals("SEND_TOKEN")) {
                    Token token = (Token) in.readObject();
                    System.out.println("Token from server: " + token.getGenerated_token());
                    DatabaseHelper.getInstance().insertToken(token);
                } else if (command.equals("VERIFY_LOGIN_Recruit")) {
                    String email = (String) in.readObject();
                    String password = (String) in.readObject();

                    Recruit recruit = DatabaseHelper.getInstance().verifyRecruit(email, password);

                    if (recruit != null) {
                        out.writeObject("RECRUIT_LOGIN_SUCCESS");
                        out.writeObject(recruit);
                    } else {
                        out.writeObject("RECRUIT_LOGIN_FAILED");
                    }
                    out.flush();

                } else if (command.equals("VERIFY_LOGIN_Staff")) {
                    String email = (String) in.readObject();
                    String password = (String) in.readObject();

                    String staffType = DatabaseHelper.getInstance().verifyStaff(email, password);
                    out.writeObject("STAFF_TYPE");
                    out.writeObject(staffType);
                    out.flush();
                } else if (command.equals("GET_RECRUITS")) {
                    ArrayList<Recruit> recruits = DatabaseHelper.getInstance().getRecruits();
                    out.writeObject("RECRUIT_LIST");
                    out.writeObject(recruits);
                    out.flush();
                } else if (command.equals("GET_ADMIN_INFO")) {
                    String email = (String) in.readObject();
                    AdminStaff adminStaff = DatabaseHelper.getInstance().getAdminDetail(email);
                    out.writeObject("GET_ADMIN_INFO");
                    out.writeObject(adminStaff);
                    out.flush();
                } else if (command.equals("UPDATE_RECRUIT")) {
                    Recruit recruitToUpdate = (Recruit) in.readObject();
                    boolean updateSuccess = DatabaseHelper.getInstance().updateRecruit(recruitToUpdate);

                    out.writeObject("UPDATE_RECRUIT_RESPONSE");
                    out.writeObject(updateSuccess);
                    out.flush();
                } else if (command.equals("UPDATE_RECRUIT_PASSWORD")) {
                    Recruit recruitToUpdate = (Recruit) in.readObject();
                    boolean updateSuccess = DatabaseHelper.getInstance().updateRecruitPassword(recruitToUpdate);

                    out.writeObject("UPDATE_RECRUIT_PASSWORD_RESPONSE");
                    out.writeObject(updateSuccess);
                    out.flush();
                }

            } catch (EOFException e) {
                // End of stream reached, break out of the loop
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }

    }
}
