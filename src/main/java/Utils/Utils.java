/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 *
 * @author gauravdahal
 */
public class Utils {
    
    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
    
    public static LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }
    
    
    public static boolean isNotValidNumber(String phoneNumber){
        
         String regex = "^04\\d*$";
         
         Pattern pattern = Pattern.compile(regex);
    
        if(phoneNumber.isEmpty()){
            DialogUtils.showErrorDialog("Phone Number cannot be empty");
            return true;
        }

        
        if(!pattern.matcher(phoneNumber).matches()){
            DialogUtils.showErrorDialog("Phone Number must start with 04 and should be digits");
            return true;
        }
        
        if(phoneNumber.length()!=10){
            DialogUtils.showErrorDialog("Phone Number Length must be 10");
            return true;
        }
        
        return false;
    }
    
    public static boolean isNotValidPassword(String password,String rePassword){
        
        if(password.length()<=3){
            DialogUtils.showErrorDialog("Password length must be greater than 3");
            return true;
        }
        
        if(rePassword.length()<=3){
            DialogUtils.showErrorDialog("Re-password length must be greater than 3");
            return true;
        }
        
        if(!password.equals(rePassword)){
            DialogUtils.showErrorDialog("Re-entered password is not equal");
            return true;
        }
    
        return false;
    }
   
    
}
