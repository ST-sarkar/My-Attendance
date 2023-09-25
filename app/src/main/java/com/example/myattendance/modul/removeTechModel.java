package com.example.myattendance.modul;

public class removeTechModel {
    String tuid,name,subject;

    public removeTechModel(String tuid, String name, String subject) {
        this.tuid = tuid;
        this.name = name;
        this.subject = subject;
    }

    public removeTechModel() {
    }

    public String getTuid() {
        return tuid;
    }

    public void setTuid(String tuid) {
        this.tuid = tuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
