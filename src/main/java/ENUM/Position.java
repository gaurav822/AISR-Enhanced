/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package ENUM;

/**
 *
 * @author gauravdahal
 */
public enum Position {
    
    FULL_TIME("Full Time"),
    PART_TIME("Part Time"),
    VOLUNTEER("Volunteer");
    
    public final String label;

    private Position(String label) {
        this.label = label;
    }
    
    public static Position getPositionFromLabel(String label) {
        for (Position position : Position.values()) {
            if (position.label.equals(label)) {
                return position;
            }
        }
        throw new IllegalArgumentException("No constant with label " + label + " found");
    }
  
    
}
