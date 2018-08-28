package com.ourincheon.app_center.model;

public class UpdateEvent {
    private String eventname;
    private String date;
    private String time;
    private String location;

    public UpdateEvent(String eventname, String date, String time, String location){
        this.eventname = eventname;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public String getEventname() {
        return eventname;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getlocation() {
        return location;
    }
}
