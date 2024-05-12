package com.example.appointment.models;

public class TeacherStaticModel
{
    private  static int id;
    private  static String first_name;

    private  static String last_name;
    private  static String[] subjects;
    private  static int pin;

    private  static String color_code;
    private static String type;

    public TeacherStaticModel(int id, String first_name, String last_name, String[] subjects, int pin, String color_code, String type) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.subjects = subjects;
        this.pin = pin;
        this.color_code = color_code;
        this.type = type;
    }




    public static int getId() {
        return id;
    }

    public static String getFirst_name() {
        return first_name;
    }

    public static String getLast_name() {
        return last_name;
    }

    public static String[] getSubjects() {
        return subjects;
    }

    public static int getPin() {
        return pin;
    }

    public static String getColor_code() {
        return color_code;
    }

    public static String getType() {
        return type;
    }


}
