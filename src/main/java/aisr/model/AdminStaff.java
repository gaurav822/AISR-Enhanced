/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aisr.model;

import ENUM.Position;

/**
 *
 * @author gauravdahal
 */
public class AdminStaff extends Staff {
    
    private String staffId;
    private Position positionType;
    
    public AdminStaff(String fullName, String address, String phoneNumber, String emailAddress, String userName,String password) {
        super(fullName, address, phoneNumber, emailAddress, userName,password);
    }
    
//     public AdminStaff(String fullName, String address, String phoneNumber, String emailAddress, String userName,String password,String staffId,Position positionType) {
//        super(fullName, address, phoneNumber, emailAddress, userName,password);
//        this.staffId=staffId;
//        this.positionType=positionType;
//    }

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
     * @return the positionType
     */
    public Position getPositionType() {
        return positionType;
    }

    /**
     * @param positionType the positionType to set
     */
    public void setPositionType(Position positionType) {
        this.positionType = positionType;
    }
    
    @Override
    public String toString() {
         return String.format("%s,\"%s\",\"%s\",\"%s\",\"\",\"\"", super.toString(), staffId, "Administration Staff", positionType.label);
    }
    
    
    public void viewDetails(){
    
    }
    
   
    
}
