/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author gauravdahal
 */
import Constants.Constants;
import aisr.model.AdminStaff;
import aisr.model.ManagementStaff;
import aisr.model.Recruit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    private static DatabaseHelper instance;
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";

    private Connection connection;

    // Private constructor to prevent instantiation from outside
    public DatabaseHelper() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            createDatabaseIfNotExists();
            selectDatabase();
            createRecruitTableIfNotExists();
            createStaffTableIfNotExists();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get the singleton instance of DatabaseHelper
    public static DatabaseHelper getInstance() {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper();
                }
            }
        }
        return instance;
    }

    // Method to create the database if it does not exist
    private void createDatabaseIfNotExists() throws SQLException {
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE DATABASE IF NOT EXISTS " + Constants.DATABASE_NAME;
            statement.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    // Method to select database
    private void selectDatabase() throws SQLException {
        try {
            Statement statement = connection.createStatement();
            String sql = "Use " + Constants.DATABASE_NAME;
            statement.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    // Method to create the tables if they do not exist
    private void createRecruitTableIfNotExists() throws SQLException {

        String createRecruitsTable = "CREATE TABLE IF NOT EXISTS " + Constants.DATABASE_NAME + ".recruits ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "full_name VARCHAR(255) NOT NULL,"
                + "address VARCHAR(255),"
                + "phone_number VARCHAR(20),"
                + "email_address VARCHAR(255) UNIQUE,"
                + "username VARCHAR(255),"
                + "password VARCHAR(255),"
                + "interviewDate VARCHAR(255),"
                + "qualificationLevel VARCHAR(255),"
                + "department VARCHAR(255),"
                + "branch VARCHAR(255),"
                + "staff_id VARCHAR(255),"
                + "staff_name VARCHAR(255),"
                + "date_data_added DATE,"
                + "staff_branch VARCHAR(255)"
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createRecruitsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createStaffTableIfNotExists() throws SQLException {
        String createStaffTable = "CREATE TABLE IF NOT EXISTS " + Constants.DATABASE_NAME + ".staff ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "staff_id VARCHAR(255), "
                + "full_name VARCHAR(255) NOT NULL, "
                + "address VARCHAR(255) NOT NULL, "
                + "phone_number VARCHAR(20) NOT NULL, "
                + "email_address VARCHAR(255) NOT NULL, "
                + "username VARCHAR(255) NOT NULL, "
                + "password VARCHAR(255) NOT NULL, "
                + "staff_type VARCHAR (255) NOT NULL, "
                + "position_type VARCHAR(255), "
                + "management_level VARCHAR(255), "
                + "staff_branch VARCHAR(255)"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createStaffTable);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertAdminStaff(AdminStaff adminStaff) {
        String query = "INSERT INTO staff (staff_id, full_name, address, phone_number, email_address, username, password, staff_type, position_type) VALUES (?, ?, ?, ?, ?, ?, ?, 'ADMIN', ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, adminStaff.getStaffId());
            statement.setString(2, adminStaff.getFullName());
            statement.setString(3, adminStaff.getAddress());
            statement.setString(4, adminStaff.getPhoneNumber());
            statement.setString(5, adminStaff.getEmailAddress());
            statement.setString(6, adminStaff.getUserName());
            statement.setString(7, adminStaff.getPassword());
            statement.setString(8, adminStaff.getPositionType().label);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertManagementStaff(ManagementStaff managementStaff) {
        String query = "INSERT INTO staff (staff_id, full_name, address, phone_number, email_address, username, password, staff_type, management_level, branch_name) VALUES (?, ?, ?, ?, ?, ?, ?, 'MANAGEMENT', ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, managementStaff.getStaffId());
            statement.setString(2, managementStaff.getFullName());
            statement.setString(3, managementStaff.getAddress());
            statement.setString(4, managementStaff.getPhoneNumber());
            statement.setString(5, managementStaff.getEmailAddress());
            statement.setString(6, managementStaff.getUserName());
            statement.setString(7, managementStaff.getPassword());
            statement.setString(8, managementStaff.getManagementLevel().label);
            statement.setString(9, managementStaff.getBranchName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     public void insertRecruit(Recruit recruit) {
         
        String query = "INSERT INTO recruits (full_name, address, phone_number, email_address, username, password, interviewDate,qualificationLevel,department,branch,staff_id,staff_name,date_data_added,staff_branch) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, recruit.getFullName());
            statement.setString(2, recruit.getAddress());
            statement.setString(3, recruit.getPhoneNumber());
            statement.setString(4, recruit.getEmailAddress());
            statement.setString(5, recruit.getUserName());
            statement.setString(6, recruit.getPassword());
            statement.setString(7, recruit.getInterviewDate());
            statement.setString(8, recruit.getQualificationLevel());
            statement.setString(9, recruit.getDepartment());
            statement.setString(10, recruit.getBranch());
            statement.setString(11, recruit.getStaffId());
            statement.setString(12, recruit.getStaffName());
            statement.setString(13, recruit.getDateDataAdded());
            statement.setString(14, recruit.getStaffBranch());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
