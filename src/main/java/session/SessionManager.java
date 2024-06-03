/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package session;

import aisr.model.SessionUser;

/**
 *
 * @author gauravdahal
 */
public class SessionManager {
    private static SessionManager instance;
    private SessionUser currentUser;

    private SessionManager() {
        // Private constructor to prevent instantiation
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(SessionUser user) {
        this.currentUser = user;
    }

    public SessionUser getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void invalidateSession() {
        currentUser = null;
    }
    
}

