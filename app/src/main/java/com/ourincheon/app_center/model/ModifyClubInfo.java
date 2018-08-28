package com.ourincheon.app_center.model;

public class ModifyClubInfo {
    private String representative;
    private String phone;
    private String application;
    private String contents;

    public ModifyClubInfo(String representative, String phone, String application, String contents){
        this.representative = representative;
        this.phone = phone;
        this.application = application;
        this.contents = contents;
    }

    public String getRepresentative() {
        return representative;
    }

    public String getPhone() {
        return phone;
    }

    public String getApplication() {
        return application;
    }

    public String getContents() {
        return contents;
    }
}
