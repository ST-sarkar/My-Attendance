package com.example.myattendance.modul;

public class StudAttendace {

    private String date,roll,name;
    private boolean present;

    public StudAttendace(String date, boolean present,String roll,String name) {
        this.date = date;
        this.present = present;
        this.name=name;
        this.roll=roll;
    }

    public StudAttendace() {
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
