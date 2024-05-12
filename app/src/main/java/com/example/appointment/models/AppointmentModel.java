package com.example.appointment.models;

public class AppointmentModel
{
    private int id;
    private String year;
    private String month;
    private String day;
    private String time;
    private String subject;
    private String teacher_name;
    private String purpose;
    private int teacher_id;
    private int student_id;
    private boolean isExpanded;

    public AppointmentModel(int id, String year, String month, String day, String time, String subject, String teacher_name, String purpose, int teacher_id, int student_id) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.time = time;
        this.subject = subject;
        this.teacher_name = teacher_name;
        this.purpose = purpose;
        this.teacher_id = teacher_id;
        this.isExpanded = false;
        this.student_id = student_id;
    }


    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public int getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public int getStudent_id()
    {
        return student_id;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public String getPurpose() {
        return purpose;
    }

    public int getTeacher_id() {
        return teacher_id;
    }
}
