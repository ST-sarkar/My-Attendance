package com.example.myattendance.modul;

public class removeUserModel {
    String roll,name,uid,email,pass;

    public removeUserModel(String roll, String name, String uid, String email, String pass) {
        this.roll = roll;
        this.name = name;
        this.uid = uid;
        this.email=email;
        this.pass=pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public removeUserModel() {
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
