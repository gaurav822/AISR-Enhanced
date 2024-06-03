/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package ENUM;

/**
 *
 * @author gauravdahal
 */
public enum UserType {
    
    Staff ("Login as Staff"),
    Recruit("Login as Recruit");
    
    public final String label;

    private UserType(String label) {
        this.label = label;
    }
}
