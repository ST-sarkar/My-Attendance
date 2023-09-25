package com.example.myattendance.modul;

public class studView {
    String DEPARTMENT, YEAR, SEMESTER, NAME, ROLL, EMAIL, PASS;

    public studView(String DEPARTMENT, String YEAR, String SEMESTER, String NAME, String ROLL, String EMAIL, String PASS) {
        this.DEPARTMENT = DEPARTMENT;
        this.YEAR = YEAR;
        this.SEMESTER = SEMESTER;
        this.NAME = NAME;
        this.ROLL = ROLL;
        this.EMAIL=EMAIL;
        this.PASS=PASS;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPASS() {
        return PASS;
    }

    public void setPASS(String PASS) {
        this.PASS = PASS;
    }

    public String getDEPARTMENT() {
        return DEPARTMENT;
    }

    public void setDEPARTMENT(String DEPARTMENT) {
        this.DEPARTMENT = DEPARTMENT;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }

    public String getSEMESTER() {
        return SEMESTER;
    }

    public void setSEMESTER(String SEMESTER) {
        this.SEMESTER = SEMESTER;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getROLL() {
        return ROLL;
    }

    public void setROLL(String ROLL) {
        this.ROLL = ROLL;
    }

    public studView() {
    }
}
