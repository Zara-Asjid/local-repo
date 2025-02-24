package com.sait.tawajudpremiumplusnewfeatured.items;

public class SpinnerItem {
    String title;
    int emp_id;

    public String getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(String emp_no) {
        this.emp_no = emp_no;
    }

    String emp_no;
    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }


    public SpinnerItem(String title,int emp_id,String emp_no) {
        this.title = title;
        this.emp_id = emp_id;

        this.emp_no = emp_no;

    }


    public String getTitle() {
        return title;
    }






}
