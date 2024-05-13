/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import ENUM.ManagementLevel;

/**
 *
 * @author gauravdahal
 */
public class ManagementStaff extends Staff{
    
    private String staffId;
    private ManagementLevel managementLevel;
    private String branchName;
    
    public ManagementStaff(String fullName, String address, String phoneNumber, String emailAddress, String userName,String password) {
        super(fullName, address, phoneNumber, emailAddress, userName,password);
    }


    /**
     * @return the staffId
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * @param staffId the staffId to set
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /**
     * @return the managementLevel
     */
    public ManagementLevel getManagementLevel() {
        return managementLevel;
    }

    /**
     * @param managementLevel the managementLevel to set
     */
    public void setManagementLevel(ManagementLevel managementLevel) {
        this.managementLevel = managementLevel;
    }

    /**
     * @return the branchName
     */
    public String getBranchName() {
        return branchName;
    }

    /**
     * @param branchName the branchName to set
     */
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    @Override
    public String toString() {
            return String.format("%s,\"%s\",\"%s\",\"\",\"%s\",\"%s\"", super.toString(), staffId, "Management Staff", managementLevel.label, branchName);

    }
    
    
    public void viewDetails(){
    
    }
    
    
}
