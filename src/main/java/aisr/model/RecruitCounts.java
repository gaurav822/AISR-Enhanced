/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aisr.model;

import java.util.Arrays;

/**
 *
 * @author gauravdahal
 */
public class RecruitCounts {
    private final int[] departmentRecruitsCounts = new int[2]; // Array to store recruits count for each branch
    private int totalRecruitsCount;
    private final int[] branchRecruitsCount = new int[4]; // Array to store recruits count for each branch
    
    public final static String[] branchNames = {"MELBOURNE", "SYDNEY", "BRISBANE", "ADELAIDE"};


    public int[] getDepartmentRecruitsCount() {
        return departmentRecruitsCounts;
    }

    public int getTotalRecruitsCount() {
        return totalRecruitsCount;
    }

    public int[] getBranchRecruitsCount() {
        return branchRecruitsCount;
    }

    public void incrementDepartmentRecruitsCount(int depIndex) {
        departmentRecruitsCounts[depIndex]++;
    }

    public void incrementTotalRecruitsCount() {
        totalRecruitsCount++;
    }

    public void incrementBranchCount(int branchIndex) {
        branchRecruitsCount[branchIndex]++;
    }
    
    public void printDetails() {
        System.out.println("Department recruits count: " + Arrays.toString(departmentRecruitsCounts));
        System.out.println("Total recruits count: " + totalRecruitsCount);
        System.out.println("Branch recruits count: " + Arrays.toString(branchRecruitsCount));
    }
}

