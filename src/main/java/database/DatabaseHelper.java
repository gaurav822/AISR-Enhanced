package database;

/**
 *
 * @author gauravdahal
 */
import Constants.Constants;
import ENUM.ManagementLevel;
import ENUM.Position;
import Utils.EncryptionUtils;
import aisr.model.AdminStaff;
import aisr.model.ManagementStaff;
import aisr.model.Recruit;
import aisr.model.RecruitCounts;
import aisr.model.SessionUser;
import aisr.model.Token;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import logger.Logger;
import session.SessionManager;

public class DatabaseHelper {

    private static DatabaseHelper instance;
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "pass";

    private Connection connection;

    // Private constructor to prevent instantiation from outside
    public DatabaseHelper(String databaseUrl, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(databaseUrl, username, password);
            createDatabaseIfNotExists();
            selectDatabase();
            createRecruitTableIfNotExists();
            createStaffTableIfNotExists();
            createTokenTableIfNotExists();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public DatabaseHelper() {
        this(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
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
                + "bio TEXT,"
                + "address VARCHAR(255),"
                + "phone_number VARCHAR(20),"
                + "email_address VARCHAR(255) UNIQUE,"
                + "username VARCHAR(255),"
                + "password VARCHAR(255),"
                + "interview_date VARCHAR(255),"
                + "qualification_level VARCHAR(255),"
                + "department VARCHAR(255),"
                + "branch VARCHAR(255),"
                + "staff_id VARCHAR(255),"
                + "staff_name VARCHAR(255),"
                + "date_data_added DATE,"
                + "staff_branch VARCHAR(255),"
                + "image_data MEDIUMBLOB"
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

    public void createTokenTableIfNotExists() {
        String createTokenTable = "CREATE TABLE IF NOT EXISTS " + Constants.DATABASE_NAME + ".tokens ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "recruit_email VARCHAR(255), "
                + "token_generated VARCHAR(255)"
                + ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTokenTable);
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
            Logger.log("REGISTRATION SUCCESS : " + adminStaff.getFullName() + " : " + adminStaff.getStaffId());
        } catch (SQLException e) {
            Logger.log("REGISTRATION FAILED : " + adminStaff.getFullName() + " : " + adminStaff.getStaffId());
            e.printStackTrace();
        } catch (Exception e) {
            Logger.log("REGISTRATION FAILED : " + adminStaff.getFullName() + " : " + adminStaff.getStaffId());
            e.printStackTrace();
        }
    }

    public void insertManagementStaff(ManagementStaff managementStaff) {
        String query = "INSERT INTO staff (staff_id, full_name, address, phone_number, email_address, username, password, staff_type, management_level, staff_branch) VALUES (?, ?, ?, ?, ?, ?, ?, 'MANAGEMENT', ?, ?)";

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

    public boolean insertRecruit(Recruit recruit) {

        String query = "INSERT INTO recruits (full_name, address, phone_number, email_address, username, password, interview_date,qualification_level,bio,department,branch,staff_id,staff_name,date_data_added,staff_branch) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, recruit.getFullName());
            statement.setString(2, recruit.getAddress());
            statement.setString(3, recruit.getPhoneNumber());
            statement.setString(4, recruit.getEmailAddress());
            statement.setString(5, recruit.getUserName());
            statement.setString(6, recruit.getPassword());
            statement.setString(7, recruit.getInterviewDate());
            statement.setString(8, recruit.getQualificationLevel());
            statement.setString(9, recruit.getBio());
            statement.setString(10, recruit.getDepartment());
            statement.setString(11, recruit.getBranch());
            statement.setString(12, recruit.getStaffId());
            statement.setString(13, recruit.getStaffName());
            statement.setString(14, recruit.getDateDataAdded());
            statement.setString(15, recruit.getStaffBranch());
            statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // MySQL error code for duplicate entry
                return true;
            }
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    public LinkedList<Recruit> getRecruits() {
        LinkedList<Recruit> recruits = new LinkedList<Recruit>();
        String query = "SELECT * FROM recruits";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String fullName = resultSet.getString("full_name");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");
                String emailAddress = resultSet.getString("email_address");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String interviewDate = resultSet.getString("interview_date");
                String qualificationLevel = resultSet.getString("qualification_level");
                String bio = resultSet.getString("bio");
                String department = resultSet.getString("department");
                String branch = resultSet.getString("branch");
                String staffId = resultSet.getString("staff_id");
                String staffName = resultSet.getString("staff_name");
                String dateDataAdded = resultSet.getString("date_data_added");
                String staffBranch = resultSet.getString("staff_branch");

                Recruit recruit = new Recruit(fullName, address, phoneNumber, emailAddress, username, password);
                recruit.setInterviewDate(interviewDate);
                recruit.setQualificationLevel(qualificationLevel);
                recruit.setDepartment(department);
                recruit.setBranch(branch);
                recruit.setBio(bio);
                recruit.setStaffId(staffId);
                recruit.setStaffName(staffName);
                recruit.setDateDataAdded(dateDataAdded);
                recruit.setStaffBranch(staffBranch);

                recruits.add(recruit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recruits;
    }

    public ManagementStaff getManagementDetail(String email) {
        ManagementStaff mgmtStaff = null;
        String query = "SELECT * FROM staff WHERE email_address = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String staffId = resultSet.getString("staff_id");
                String fullName = resultSet.getString("full_name");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String managementLevel = resultSet.getString("management_level");
                String branchName = resultSet.getString("staff_branch");

                mgmtStaff = new ManagementStaff(fullName, address, phoneNumber, email, username, password);
                mgmtStaff.setStaffId(staffId);
                mgmtStaff.setBranchName(branchName);
                mgmtStaff.setManagementLevel(ManagementLevel.getManagementFromLabel(managementLevel));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("The mgmt staff is " + mgmtStaff);
        return mgmtStaff;
    }

    public int getAdminStaffCountByType(String type) {
        int count = 0;
        String query = "SELECT COUNT(*) count FROM staff WHERE staff_type = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, type);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public AdminStaff getAdminDetail(String email) {
        AdminStaff staff = null;
        String query = "SELECT * FROM staff WHERE email_address = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String staffId = resultSet.getString("staff_id");
                String fullName = resultSet.getString("full_name");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String positionType = resultSet.getString("position_type");
                String managementLevel = resultSet.getString("management_level");
                String branchName = resultSet.getString("staff_branch");
                staff = new AdminStaff(fullName, address, phoneNumber, email, username, password);
                staff.setStaffId(staffId);
                staff.setPositionType(Position.getPositionFromLabel(positionType));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staff;
    }

    public void insertToken(Token token) {

        String query = "INSERT INTO tokens (recruit_email,token_generated) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, token.getRecruit_email());
            statement.setString(2, token.getGenerated_token());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String verifyStaff(String email, String password) {
        // Fetch the encrypted password stored in the database
        String staffType = verifyEmailPasswordFromStaff(email, password);

        return staffType;
    }

    public Recruit verifyRecruit(String email, String encryptedPassword) {
        // Fetch the encrypted password stored in the database
        String storedEncryptedPassword = fetchEncryptedPasswordFromDatabase(email);
        if (storedEncryptedPassword == null) {
            return null; // Email not found in the database
        }

        // Decrypt the stored encrypted password
        String decryptedStoredPassword = EncryptionUtils.decrypt(storedEncryptedPassword);
        if (decryptedStoredPassword == null) {
            return null; // Decryption failed, return false
        }

        // Compare the decrypted stored password with the provided encrypted password
        if (decryptedStoredPassword.equals(encryptedPassword)) {
            Recruit recruit = getRecruitUser(email);
            return recruit;
        }

        return null;
    }

    private String fetchEncryptedPasswordFromDatabase(String email) {
        String query = "SELECT password FROM recruits WHERE email_address = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Email not found in the database
    }

    private String verifyEmailPasswordFromStaff(String email, String password) {
        String query = "SELECT password,staff_type,email_address FROM staff WHERE email_address = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String dbPassword = resultSet.getString("password");
                String decryptedStoredPassword = EncryptionUtils.decrypt(dbPassword);
                System.out.println(decryptedStoredPassword);
                System.out.println(password);
                if (password.equals(decryptedStoredPassword)) {

                    // Creating a session object with user information
                    SessionManager sessionManager = SessionManager.getInstance();
                    SessionUser user = new SessionUser(email); // Assuming you have a User class
                    sessionManager.setCurrentUser(user);
                    return resultSet.getString("staff_type");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Email not found in the database
    }

    public boolean updateRecruit(Recruit recruitToUpdate) {
        String query = "UPDATE recruits SET bio = ?, full_name = ?, address = ?, phone_number = ?, email_address = ?, username = ?, qualification_level = ?, image_data = ? WHERE email_address = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, recruitToUpdate.getBio());
            statement.setString(2, recruitToUpdate.getFullName());
            statement.setString(3, recruitToUpdate.getAddress());
            statement.setString(4, recruitToUpdate.getPhoneNumber());
            statement.setString(5, recruitToUpdate.getEmailAddress());
            statement.setString(6, recruitToUpdate.getUserName());
            statement.setString(7, recruitToUpdate.getQualificationLevel());

            byte[] imageData = recruitToUpdate.getImageData();
            if (imageData != null) {
                Blob imageDataBlob = connection.createBlob();
                imageDataBlob.setBytes(1, imageData);
                statement.setBlob(8, imageDataBlob);
            } else {
                statement.setNull(8, java.sql.Types.BLOB);
            }

            statement.setString(9, recruitToUpdate.getEmailAddress());
            int rowsUpdated = statement.executeUpdate();

//            Blob imageDataBlob = connection.createBlob();
//
//            imageDataBlob.setBytes(1, recruitToUpdate.getImageData());
//            statement.setBlob(8, imageDataBlob);
//            statement.setString(9, recruitToUpdate.getEmailAddress());
//            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRecruitFromStaff(Recruit recruit) {
        String query = "UPDATE recruits SET full_name = ?, address = ?, phone_number = ?, email_address = ?, username = ?, password = ?, qualification_level = ?, department = ?,branch = ?, staff_id = ?, staff_name = ?, staff_branch = ? WHERE email_address = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, recruit.getFullName());
            statement.setString(2, recruit.getAddress());
            statement.setString(3, recruit.getPhoneNumber());
            statement.setString(4, recruit.getEmailAddress());
            statement.setString(5, recruit.getUserName());
            statement.setString(6, recruit.getPassword());
            statement.setString(7, recruit.getQualificationLevel());
            statement.setString(8, recruit.getDepartment());
            statement.setString(9, recruit.getBranch());
            statement.setString(10, recruit.getStaffId());
            statement.setString(11, recruit.getStaffName());
            statement.setString(12, recruit.getStaffBranch());
            statement.setString(13, recruit.getEmailAddress());

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRecruitPassword(Recruit recruitToUpdate) {
        String query = "UPDATE recruits SET password = ? WHERE email_address = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, recruitToUpdate.getPassword());
            statement.setString(2, recruitToUpdate.getEmailAddress());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Recruit getRecruitUser(String email) {
        Recruit recruit = null;
        String query = "SELECT * FROM recruits WHERE email_address = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String bio = resultSet.getString("bio");
                String fullName = resultSet.getString("full_name");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String qualificationLevel = resultSet.getString("qualification_level");
                Blob imgBlob = resultSet.getBlob("image_data");
                String interviewDate = resultSet.getString("interview_date");
                byte[] imageData = null;
                if (imgBlob != null) {
                    imageData = imgBlob.getBytes(1l, (int) imgBlob.length());
                }
                recruit = new Recruit(bio, fullName, address, phoneNumber, email, username, password,
                        qualificationLevel, interviewDate, imageData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recruit;
    }

    // Retrieve recruit data based on department
    public RecruitCounts getRecruitsCounts(String department) {
        String query = "SELECT * from recruits";

        RecruitCounts recruitCounts = new RecruitCounts();

        String[] inputDepartments = department.split(", ");

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                recruitCounts.incrementTotalRecruitsCount();

                String departmentData = resultSet.getString("department");
                String branch = resultSet.getString("branch");

                // Process department data
                if (departmentData != null) {
                    String[] departmentList = departmentData.split(", ");
                    for (String dep : departmentList) {
                        for (int i = 0; i < inputDepartments.length; i++) {
                            if (inputDepartments[i].equals(dep)) {
                                recruitCounts.incrementDepartmentRecruitsCount(i);
                            }
                        }
                    }
                }

                // Process branch data
                if (branch != null) {
                    switch (branch) {
                        case "MELBOURNE":
                            recruitCounts.incrementBranchCount(0);
                            break;
                        case "SYDNEY":
                            recruitCounts.incrementBranchCount(1);
                            break;
                        case "BRISBANE":
                            recruitCounts.incrementBranchCount(2);
                            break;
                        case "ADELAIDE":
                            recruitCounts.incrementBranchCount(3);
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions
        }

        return recruitCounts;
    }

    public LinkedList<ManagementStaff> getManagementStaffs() {
        LinkedList<ManagementStaff> managementStaffs = new LinkedList<>();
        String query = "SELECT * FROM staff WHERE staff_type = 'MANAGEMENT'";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String staffId = resultSet.getString("staff_id");
                String fullName = resultSet.getString("full_name");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");
                String emailAddress = resultSet.getString("email_address");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String managementLevel = resultSet.getString("management_level");
                String branchName = resultSet.getString("staff_branch");

                ManagementStaff managementStaff = new ManagementStaff(fullName, address, phoneNumber, emailAddress, username, password);
                managementStaff.setStaffId(staffId);
                managementStaff.setManagementLevel(ManagementLevel.getManagementFromLabel(managementLevel));
                managementStaff.setBranchName(branchName);

                managementStaffs.add(managementStaff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return managementStaffs;
    }

}
