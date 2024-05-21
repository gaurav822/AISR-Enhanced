/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aisr.model;

import java.io.Serializable;

/**
 *
 * @author gauravdahal
 */
public class Recruit extends Staff implements Serializable{
    
    private String interviewDate;
    private String qualificationLevel;
    private String department;
    private String branch;
    private String staffId;
    private String staffName;
    private String dateDataAdded;
    private String staffBranch;
            
    
    public Recruit(String fullName, String address, String phoneNumber, String emailAddress, String userName, String password) {
        super(fullName, address, phoneNumber, emailAddress, userName, password);
    }
    

    /**
     * @return the interviewDate
     */
    public String getInterviewDate() {
        return interviewDate;
    }

    /**
     * @param interviewDate the interviewDate to set
     */
    public void setInterviewDate(String interviewDate) {
        this.interviewDate = interviewDate;
    }

    /**
     * @return the qualificationLevel
     */
    public String getQualificationLevel() {
        return qualificationLevel;
    }

    /**
     * @param qualificationLevel the qualificationLevel to set
     */
    public void setQualificationLevel(String qualificationLevel) {
        this.qualificationLevel = qualificationLevel;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the branch
     */
    public String getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    @Override
    public String toString() {
         return String.format("%s,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\""
                 , super.toString(), 
                 interviewDate, qualificationLevel, department,branch, getStaffId(), getStaffName(), getDateDataAdded(), getStaffBranch());
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
     * @return the staffName
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * @param staffName the staffName to set
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    /**
     * @return the dateDataAdded
     */
    public String getDateDataAdded() {
        return dateDataAdded;
    }

    /**
     * @param dateDataAdded the dateDataAdded to set
     */
    public void setDateDataAdded(String dateDataAdded) {
        this.dateDataAdded = dateDataAdded;
    }

    /**
     * @return the staffBranch
     */
    public String getStaffBranch() {
        return staffBranch;
    }

    /**
     * @param staffBranch the staffBranch to set
     */
    public void setStaffBranch(String staffBranch) {
        this.staffBranch = staffBranch;
    }
    
    
}
