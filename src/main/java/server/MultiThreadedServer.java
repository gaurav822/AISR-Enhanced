/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author gauravdahal
 */
import Utils.DialogUtils;
import aisr.model.AdminStaff;
import aisr.model.ManagementStaff;
import database.DatabaseHelper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
