package com.example.myattendance.modul;

public class studView {
    String department,year,semester,name,roll;

    public studView() {
    }

    public studView(String department, String year, String semester, String name, String roll) {
        this.department = department;
        this.year = year;
        this.semester = semester;
        this.name = name;
        this.roll = roll;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }
}
