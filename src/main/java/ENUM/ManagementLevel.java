/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package ENUM;

/**
 *
 * @author gauravdahal
 */
public enum ManagementLevel {
    SENIOR("Senior Manager"),
    MIDLEVEL("Mid-Level Manager"),
    SUPERVISOR("Supervisor");
    
    public final String label;

    private ManagementLevel(String label) {
        this.label = label;
    }
    
    public static ManagementLevel getManagementFromLabel(String label) {
        for (ManagementLevel managementLevel : ManagementLevel.values()) {
            if (managementLevel.label.equals(label)) {
                return managementLevel;
            }
        }
        throw new IllegalArgumentException("No constant with label " + label + " found");
    }
}
