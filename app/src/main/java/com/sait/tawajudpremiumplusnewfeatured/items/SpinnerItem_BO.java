package com.sait.tawajudpremiumplusnewfeatured.items;

public class SpinnerItem_BO {

    int id;
    Boolean hasFlexiblePermission;
    Integer maxDuration;
    int employee_id;


    String title;
   public Boolean hasFullDayPermission,hasPermissionForPeriod,attachmentIsMandatory,remarksIsMandatory;

    public SpinnerItem_BO(int id, int employee_id, String title,
                          Boolean hasFullDayPermission, Boolean hasPermissionForPeriod,Boolean hasFlexiblePermission,Integer maxDuration,Boolean attachmentIsMandatory,Boolean remarksIsMandatory) {
        this.id = id;
        this.title = title;
        this.employee_id = employee_id;
        this.hasPermissionForPeriod = hasPermissionForPeriod;
        this.hasFullDayPermission = hasFullDayPermission;
        this.hasFlexiblePermission=hasFlexiblePermission;
        this.maxDuration=maxDuration;
        this.attachmentIsMandatory = attachmentIsMandatory;
        this.remarksIsMandatory= remarksIsMandatory;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public Boolean getHasFullDayPermission() {
        return hasFullDayPermission;
    }

    public Boolean getHasPermissionForPeriod() {
        return hasPermissionForPeriod;
    }
    public Boolean getHasFlexiblePermission() {
        return hasFlexiblePermission;
    }

    public void setHasFlexiblePermission(Boolean hasFlexiblePermission) {
        this.hasFlexiblePermission = hasFlexiblePermission;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public void setHasFullDayPermission(Boolean hasFullDayPermission) {
        this.hasFullDayPermission = hasFullDayPermission;
    }

    public void setHasPermissionForPeriod(Boolean hasPermissionForPeriod) {
        this.hasPermissionForPeriod = hasPermissionForPeriod;
    }

}
