/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package ENUM;

/**
 *
 * @author gauravdahal
 */
public enum StaffType {

    ADMIN("Administration Staff"),
    MANAGEMENT("Management Staff");

    private final String label;

    private StaffType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
