/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import java.net.URL;
import java.util.ResourceBundle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author gauravdahal
 */
public class LoginControllerTest {
    
    LoginController instance;
    
    public LoginControllerTest() {
        instance = new LoginController();
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    // Test a scenario where email and password are valid
    
    

    // Test a scenario where email is empty
    @Test
    public void testEmptyEmail() {
         // Set valid email and password
        System.out.println("Email Invalid/Empty ");
        String email = "";
        String password = "123456";
        boolean expResult = false;
        boolean result = instance.emailPassValid(email, password);
        assertEquals(expResult, result);
    }

    // Test a scenario where password is empty
    @Test
    public void testEmptyPassword() {
         // Set valid email and password
        System.out.println("Password Invalid/Empty");
        String email = "test@gmail.com";
        String password = "";
        boolean expResult = false;
        boolean result = instance.emailPassValid(email, password);
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testEmptyEmailAndEmptyPassword() {
         // Set valid email and password
        System.out.println("Email and Password Invalid/Empty");
        String email = "";
        String password = "";
        boolean expResult = false;
        boolean result = instance.emailPassValid(email, password);
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testValidEmailAndPassword() {
        // Set valid email and password
        System.out.println("Email And Password Valid");
        String email = "test@gmail.com";
        String password = "123456";
        boolean expResult = true;
        boolean result = instance.emailPassValid(email, password);
        assertEquals(expResult, result);
    }
    
}
