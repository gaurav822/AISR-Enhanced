/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aisr.model;

/**
 *
 * @author gauravdahal
 */
public class SessionUser {

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String email;

    public SessionUser(String email) {
        this.email = email;
    }
    
}
