package com.example.appointment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.appointment.models.AppointmentModel;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    Context context;
    String sql;
    public DBHelper(@Nullable Context context) {
        super(context, "AppointmentDatabase", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sql = "CREATE TABLE IF NOT EXISTS student(id INTEGER PRIMARY KEY AUTOINCREMENT, first_name VARCHAR, last_name VARCHAR, contact VARCHAR, email VARCHAR, pin VARCHAR, color_code INTEGER)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS teacher(teacher_id INTEGER PRIMARY KEY AUTOINCREMENT, first_name VARCHAR, last_name VARCHAR, pin VARCHAR , color_code INTEGER)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS subject(subject_id INTEGER PRIMARY KEY AUTOINCREMENT, subject_name VARCHAR, teacher_id INTEGER)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS appointment(appointment_id INTEGER PRIMARY KEY AUTOINCREMENT, year VARCHAR, month VARCHAR, day VARCHAR, time VARCHAR, subject VARCHAR, teacher_name VARCHAR, purpose VARCHAR, teacher_id INTEGER, student_id INTEGER)";
        db.execSQL(sql);
    }



    public boolean createStudentAccount(String fn, String ln, String contact, String email, int pin, int color_code){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("first_name", fn);
        cv.put("last_name", ln);
        cv.put("contact", contact);
        cv.put("email", email);
        cv.put("pin", pin);
        cv.put("color_code", color_code);
        long res = db.insert("student", null, cv);
        if(res == -1){
            return false;
        }else{
            return true;
        }

    }

    public Cursor getTeacherCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM teacher", null);
    }

    public boolean createTeacherAccount(String first_name,String last_name, String pin, int color_code){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("first_name", first_name);
        cv.put("last_name", last_name);
        cv.put("pin", pin);
        cv.put("color_code", color_code);
        long res = db.insert("teacher", null, cv);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean createSubject(String subject_name, int teacher_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("subject_name", subject_name);
        cv.put("teacher_id", teacher_id);
        long res = db.insert("subject", null, cv);
        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean createAppointment(int year, int month, int day, String time, String subject, String teacher_name, String purpose, int teacher_id, int student_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("year", year);
        cv.put("month", month);
        cv.put("day", day);
        cv.put("time", time);
        cv.put("subject", subject);
        cv.put("teacher_name", teacher_name);
        cv.put("purpose", purpose);
        cv.put("teacher_id", teacher_id);
        cv.put("student_id", student_id);
        long res = db.insert("appointment", null, cv);
        if(res == -1){
            return false;
        }else{
            return true;
        }

    }

    public Cursor FindStudentAccount(int pin){
        SQLiteDatabase db = this.getWritableDatabase();
        sql = "SELECT * FROM student WHERE pin = '"+pin+"'";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor FindTeacherAccount(String pin){
        SQLiteDatabase db = this.getWritableDatabase();
        sql = "SELECT * FROM teacher WHERE pin = '"+pin+"'";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getTeacher(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        sql = "SELECT * FROM teacher WHERE first_name = '"+name+"'";
        Cursor c = db.rawQuery(sql,null);
        return c;
    }

    public Cursor getTeacher(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM teacher WHERE teacher_id = '"+id+"'", null);
    }


    public Cursor getStudent(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM student WHERE first_name = '"+name+"'", null);
    }


    public Cursor findStudent(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM student WHERE id = '"+id+"'", null);
    }



    public Cursor getTeacherAppointment(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM appointment WHERE teacher_id = '"+id+"'", null);
    }

    public int getTeacherColorCode(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM teacher WHERE teacher_id = '" + id + "'", null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndexOrThrow("color_code"));
    }


    public int getStudentColorCode(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM student WHERE id = '" + id + "'", null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndexOrThrow("color_code"));
    }


    public List<AppointmentModel> getTeacherAppointmentList(int id)
    {
        List<AppointmentModel> appointmentModelList = new ArrayList<>();

        Cursor cursor = getTeacherAppointment(id);
        while(cursor.moveToNext())
        {
            int app_id = cursor.getInt(0);
            String year = cursor.getString(1);
            String month = cursor.getString(2);
            String day = cursor.getString(3);
            String time= cursor.getString(4);
            String sub= cursor.getString(5);
            String tname= cursor.getString(6);
            String pur= cursor.getString(7);
            int tid= cursor.getInt(8);
            int sid = cursor.getInt(cursor.getColumnIndexOrThrow("student_id"));

            appointmentModelList.add(new AppointmentModel(app_id, year, month, day, time,sub ,tname, pur, tid, sid));
        }


        return appointmentModelList;
    }

    public Cursor getStudentAppointment(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM appointment WHERE student_id = '"+id+"'", null);
    }

    public List<AppointmentModel> getStudentAppointmentList(int id)
    {
        List<AppointmentModel> appointmentModelList = new ArrayList<>();

        Cursor cursor = getStudentAppointment(id);
        while(cursor.moveToNext())
        {
            int app_id = cursor.getInt(0);
            String year = cursor.getString(1);
            String month = cursor.getString(2);
            String day = cursor.getString(3);
            String time= cursor.getString(4);
            String sub= cursor.getString(5);
            String tname= cursor.getString(6);
            String pur= cursor.getString(7);
            int tid = cursor.getInt(8);
            int sid = cursor.getInt(cursor.getColumnIndexOrThrow("student_id"));
            appointmentModelList.add(new AppointmentModel(app_id, year, month, day, time,sub ,tname, pur, tid, sid));
        }


        return appointmentModelList;
    }


    public int getAppointmentCount(int teacher_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM appointment WHERE teacher_id = '"+teacher_id+"'", null);
        if(cursor.moveToFirst())
        {
            return cursor.getInt(0);
        }
        return 0;
    }




    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
