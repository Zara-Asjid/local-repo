package com.sait.tawajudpremiumplusnewfeatured.items;

public class SpinnerItem_BO_Type {

    int formId;

    String desc_en;

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public String getDesc_en() {
        return desc_en;
    }

    public void setDesc_en(String desc_en) {
        this.desc_en = desc_en;
    }

    public String getDesc_ar() {
        return desc_ar;
    }

    public void setDesc_ar(String desc_ar) {
        this.desc_ar = desc_ar;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    String desc_ar;

    String requestType;

    public SpinnerItem_BO_Type(int formId, String desc_en,String desc_ar, String requestType) {
        this.formId = formId;
        this.desc_en = desc_en;
        this.desc_ar = desc_ar;
        this.requestType = requestType;


    }





}
