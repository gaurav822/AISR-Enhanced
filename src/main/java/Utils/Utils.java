/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import aisr.model.AdminStaff;
import aisr.model.ManagementStaff;
import aisr.model.Recruit;
import database.DatabaseHelper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    
    
     public static boolean isDuplicateStaffId(String staffId, LinkedList<AdminStaff> adminStaffs, LinkedList<ManagementStaff> managementStaffs) {
        for (AdminStaff adminStaff : adminStaffs) {
            if (adminStaff.getStaffId().equals(staffId)) {
                return true;
            }
        }
        for (ManagementStaff managementStaff : managementStaffs) {
            if (managementStaff.getStaffId().equals(staffId)) {
                return true;
            }
        }
        return false;
    }
     
     
     public static boolean containsElement(String[] array, String element) {
        for (String str : array) {
            if (str.equals(element)) {
                return true;
            }
        }
        return false;
    }
     
     public static String getLastName(String fullName) {
        String[] parts = fullName.split(" ");
        return parts.length > 1 ? parts[parts.length - 1] : fullName;
    }
     
     
     public static LinkedList<Recruit> getRecruitsSortedByLastNameGroupedByLocation(LinkedList<Recruit> recruits) {
         
        // Sort by last name in descending order
        recruits.sort((r1, r2) -> {
            String lastName1 = getLastName(r1.getFullName());
            String lastName2 = getLastName(r2.getFullName());
            return lastName2.compareTo(lastName1); // For descending order
        });

        // Group by location (branch)
        Map<String, List<Recruit>> recruitsByLocation = recruits.stream()
                .collect(Collectors.groupingBy(Recruit::getBranch));

        // Combine grouped lists maintaining the order
        LinkedList<Recruit> sortedAndGroupedRecruits = new LinkedList<>();
        recruitsByLocation.values().forEach(sortedAndGroupedRecruits::addAll);

        return sortedAndGroupedRecruits;
    }
     
     
     public static LinkedList<Recruit> getRecruitsSortedByQualification(LinkedList<Recruit> recruits) {
        // Define the order of qualifications
        Map<String, Integer> qualificationOrder = new HashMap<>();
        qualificationOrder.put("PhD", 1);
        qualificationOrder.put("Masters", 2);
        qualificationOrder.put("Bachelors", 3);

        // Sort the recruits by qualification level
        recruits.sort((r1, r2) -> {
            Integer order1 = qualificationOrder.getOrDefault(r1.getQualificationLevel(), Integer.MAX_VALUE);
            Integer order2 = qualificationOrder.getOrDefault(r2.getQualificationLevel(), Integer.MAX_VALUE);
            return order1.compareTo(order2);
        });

        return recruits;
    }
     
     
     
      public static LinkedList<ManagementStaff> getManagementStaff() {
        // Define the order of qualifications
        
        LinkedList<ManagementStaff> staffs = DatabaseHelper.getInstance().getManagementStaffs();
        
        return staffs;
    }
   
    
}
