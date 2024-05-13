/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.gauravdahal.ais.r.initial;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import model.Recruit;
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
public class ManagementDashboardControllerTest {
    
    ManagementDashboardController instance;
    
    public ManagementDashboardControllerTest() {
        instance = new ManagementDashboardController();
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
    
    
    /**
     * Test of readRecruitsFromCSV method, of class DashboardController.
     */
    
    
    @Test
    public void testNumberOfRecruitsFromCSV() throws IOException  {
        List<Recruit> recruits = instance.readRecruitsFromCSV("recruits.csv");
        assertNotNull(recruits);
        assertEquals(2, recruits.size()); // Assuming you have two recruits in your test CSV file
    }
    
    
     /**
     * Test of validateStudentIdForQualification method, of class DashboardController.
     */
    
    
    @Test
    public void emptyStudentIdForQualification(){
        String studentId = "";
        boolean expResult = false;
        boolean result = instance.validateStudentIdForQualification(studentId);
        assertEquals(expResult, result);
    }
    
     /**
     * Test of validateStudentIdForQualification method, of class DashboardController.
     */
    
    
    @Test
    public void InvalidStudentIdNotValidForQualification(){
        String studentId = "123";
        boolean expResult = false;
        boolean result = instance.validateStudentIdForQualification(studentId);
        assertEquals(expResult, result);
    }
    
    
     /**
     * Test of validateStudentIdForQualification method, of class DashboardController.
     */
    
    
    @Test
    public void validStudentIdForQualification(){
        String studentId = "12208688";
        boolean expResult = true;
        boolean result = instance.validateStudentIdForQualification(studentId);
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void emptyUniversityForQualification(){
        String universityName = "";
        boolean expResult = false;
        boolean result = instance.validateUniversityForQualification(universityName);
        assertEquals(expResult, result);
    }
    
    
    
    @Test
    public void validUniversityForQualification(){
        String universityName = "CQUniversity";
        boolean expResult = true;
        boolean result = instance.validateUniversityForQualification(universityName);
        assertEquals(expResult, result);
    }
    
    
    
    @Test
    public void emptyRecruitUserNameForQualification(){
        String userName = "";
        boolean expResult = false;
        boolean result = instance.validateRecruitUserNameForQualification(userName);
        assertEquals(expResult, result);
    }
    
    
    
    @Test
    public void validUserNameForQualification(){
        String userName = "123123";
        boolean expResult = true;
        boolean result = instance.validateRecruitUserNameForQualification(userName);
        assertEquals(expResult, result);
    }
    
    
    
    
}
