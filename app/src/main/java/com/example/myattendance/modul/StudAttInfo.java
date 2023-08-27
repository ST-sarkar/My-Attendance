package com.example.myattendance.modul;

public class StudAttInfo {
    private String sname,sroll,suid;
    private  boolean isPresent;

    public StudAttInfo(String sname, String sroll, String suid) {
        this.sname = sname;
        this.sroll = sroll;
        this.suid = suid;
        this.isPresent=true;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public StudAttInfo() {
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSroll() {
        return sroll;
    }

    public void setSroll(String sroll) {
        this.sroll = sroll;
    }

    public String getSuid() {
        return suid;
    }

    public void setSuid(String uid) {
        this.suid = suid;
    }
}
