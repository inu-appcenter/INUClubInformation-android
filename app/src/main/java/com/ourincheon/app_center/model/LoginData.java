package com.ourincheon.app_center.model;

public class LoginData {
    private String id;
    private String pw;

    public LoginData(String id, String pw){
        this.id = id;
        this.pw = pw;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }
}
