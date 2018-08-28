package com.ourincheon.app_center.model;

public class EventList {
    private int eventnum;
    private String clubname;
    private String eventname;
    private String date;
    private String time;
    private String location;

    public EventList(int eventnum, String clubname, String eventname, String date, String time, String location){
        this.eventnum = eventnum;
        this.clubname = clubname;
        this.eventname = eventname;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public int getEventnum() {
        return eventnum;
    }

    public String getClubname() {
        return clubname;
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

    public String getLocation() {
        return location;
    }
}
