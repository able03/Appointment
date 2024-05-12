package com.example.appointment.models;



public class StudentStaticModel {
    private static int id;
    private static String first_name;
    private static String last_name;
    private static String contact;
    private static String email;
    private static int pin;

    private static String color_code;
    private static String type;

    public StudentStaticModel(int id, String first_name, String last_name, String contact, String email, int pin, String color_code, String type) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.contact = contact;
        this.email = email;
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

    public static String getContact() {
        return contact;
    }

    public static String getEmail() {
        return email;
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
