/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author gauravdahal
 */

import aisr.model.AdminStaff;
import aisr.model.ManagementStaff;
import database.DatabaseHelper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedServer {

    private static final int PORT = 1234;
  

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {

    private Socket clientSocket;


    public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            Object inputObject;
            while ((inputObject = in.readObject()) != null) {
                String command = (String) inputObject;
                if (command.equals("ADD_ADMIN")) {
                    AdminStaff adminStaff = (AdminStaff) in.readObject();
                    DatabaseHelper.getInstance().insertAdminStaff(adminStaff);
                    out.println("SUCCESS");
                } else if (command.equals("ADD_MANAGEMENT")) {
                    ManagementStaff managementStaff = (ManagementStaff) in.readObject();
                    DatabaseHelper.getInstance().insertManagementStaff(managementStaff);
                    out.println("SUCCESS");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

