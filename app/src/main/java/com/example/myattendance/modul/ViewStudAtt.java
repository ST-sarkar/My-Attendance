package com.example.myattendance.modul;

public class ViewStudAtt {
    private String roll,name;
    private String precent;


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

    public String getPrecent() {
        return precent;
    }

    public void setPrecent(String precent) {
        this.precent = precent;
    }

    public ViewStudAtt(String roll, String name, String precent) {
        this.roll = roll;
        this.name = name;
        this.precent = precent;
    }
}
