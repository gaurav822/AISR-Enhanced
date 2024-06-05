package aisr.model;

import java.io.Serializable;
import java.util.List;

public class StaffLists implements Serializable {

    private final List<AdminStaff> adminStaffList;
    private final List<ManagementStaff> managementStaffList;

    public StaffLists(List<AdminStaff> adminStaffList, List<ManagementStaff> managementStaffList) {
        this.adminStaffList = adminStaffList;
        this.managementStaffList = managementStaffList;
    }

    public List<AdminStaff> getAdminStaffList() {
        return adminStaffList;
    }

    public List<ManagementStaff> getManagementStaffList() {
        return managementStaffList;
    }
}
