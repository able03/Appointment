package com.example.appointment.activities;

import android.animation.Animator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.example.appointment.AppointmentAdapter;
import com.example.appointment.DBHelper;
import com.example.appointment.R;
import com.example.appointment.adapters.AppointAdapter;
import com.example.appointment.adapters.AppointStudentAdapter;
import com.example.appointment.models.AppointmentModel;
import com.example.appointment.models.TeacherStaticModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class TeacherMainActivity extends AppCompatActivity {

    CalendarView calendarView;
    RecyclerView listView;
    TextView tv_teacher_name, tv_profile, tv_count;
    List<CalendarDay> calendarList;
    ImageView iv_logout;
    DBHelper dbHelper;
    List<CalendarDay> calendarDayList;
    List<AppointmentModel> appointmentModelList;
    AppointmentAdapter appointmentAdapter;
    AppointAdapter appointAdapter;
    AppointStudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initValues();
//        setAppointmentDates();
        setListener();

        setData();
        populateRV();

        setStaticData();
        getAppointedDates();



        int color = dbHelper.getTeacherColorCode(TeacherStaticModel.getId());
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(300);
        gradientDrawable.setColor(color);


        tv_profile.setBackground(gradientDrawable);

        String newFirstName = Character.toString(TeacherStaticModel.getFirst_name().charAt(0)).toUpperCase();
        String newLastName = Character.toString(TeacherStaticModel.getLast_name().charAt(0)).toUpperCase();

        tv_profile.setText(newFirstName+newLastName);







        //set count
        int count = dbHelper.getAppointmentCount(TeacherStaticModel.getId());
        if(count > 0)
        {
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText(String.valueOf(count));
        }
        else
        {
            tv_count.setVisibility(View.INVISIBLE);
        }

        dbHelper.close();

    }

    private void initValues() {
        calendarView = findViewById(R.id.calendar_teacher);
        listView = findViewById(R.id.lv_my_appointments);
        tv_teacher_name = findViewById(R.id.tv_teacher_name);
        calendarList = new ArrayList<>();
        dbHelper = new DBHelper(this);
        calendarDayList = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(this);
        tv_profile = findViewById(R.id.tv_teacher_profile);
        tv_count = findViewById(R.id.tv_teacher_counter);
        iv_logout = findViewById(R.id.iv_logout);

    }

    private void setStaticData()
    {
        tv_teacher_name.setText(TeacherStaticModel.getFirst_name() + "  " + TeacherStaticModel.getLast_name());
    }


    private void setListener()
    {
        calendarView.setOnCalendarDayClickListener(new OnCalendarDayClickListener() {
            @Override
            public void onClick(@NonNull CalendarDay calendarDay) {
                if(calendarDayList.contains(calendarDay))
                {
                    Log.d("Debugging", "contains running");
                    Calendar c = calendarDay.getCalendar();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    String str = year + "/" + (month+1) + "/" + day;
                    Log.d("Debugging", str);
                    filteredList(str);

                }
                else if(!calendarDayList.contains(calendarDay))
                {
                    Log.d("Debugging", "contains not running");
                    setData();
                    populateRV();
                }
            }
        });




        iv_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(TeacherMainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });



    }

    private void filteredList(String text)
    {
        Log.d("Debugging", "running: filteredList()");
        List<AppointmentModel> temp = new ArrayList<>();
        for(AppointmentModel model : appointmentModelList)
        {
            String date = model.getYear() + "/" + model.getMonth() + "/" + model.getDay();
            Log.d("Debugging", "FilteredList: " +date);
            if(date.contains(text))
            {
                Log.d("Debugging", "running: adding to temp list");
                temp.add(model);
            }
        }

        Log.d("Debugging", "running: set adapter");
        studentAdapter = new AppointStudentAdapter(this);
        studentAdapter.setAppointmentModelList(temp);
        populateRV();
    }

    private void setData()
    {
        appointmentModelList = dbHelper.getTeacherAppointmentList(TeacherStaticModel.getId());
        studentAdapter = new AppointStudentAdapter(this);
        studentAdapter.setAppointmentModelList(appointmentModelList);

    }

    private void populateRV()
    {
        listView.setAdapter(studentAdapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
    }





    private void getAppointedDates() {
        Log.d("Debugging", "Getting appointed dates");
        Cursor cursor = dbHelper.getTeacherAppointment(TeacherStaticModel.getId());
        Log.d("Debugging", "Teacher id: " + TeacherStaticModel.getId());

        calendarDayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Log.d("Debugging", "Adding calendar day");
            int year = cursor.getInt(1);
            int month = cursor.getInt(2);
            int day = cursor.getInt(3);

            Calendar c1 = Calendar.getInstance();
            c1.set(year, month-1, day);
            CalendarDay day1 = new CalendarDay(c1);
            day1.setImageResource(R.drawable.ic_circle);
            calendarDayList.add(day1);
        }

        Log.d("Debugging", "Setting calendar days");
        calendarView.setCalendarDays(calendarDayList);
    }





}
