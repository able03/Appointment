package com.example.appointment.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.appointment.DBHelper;
import com.example.appointment.FixedSpeedScroller;
import com.example.appointment.R;
import com.example.appointment.adapters.AppointStudentAdapter;
import com.example.appointment.adapters.ViewPagerAdapter;
import com.example.appointment.models.AppointmentModel;
import com.example.appointment.models.StudentStaticModel;
import com.example.appointment.models.TeacherStaticModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StudentDashboard extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter adapter;
    TextView tv_name, tv_profile;
    RecyclerView recyclerView;
    ImageView iv_logout;
    List<AppointmentModel> appointmentModelList;
    DBHelper dbHelper;
    AppointStudentAdapter studentAdapter;
    FloatingActionButton mAddAlarmFab; //mAddPersonFab;

    ExtendedFloatingActionButton mAddFab;

    TextView addAlarmActionText;// addPersonActionText;
    Boolean isAllFabsVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initValues();
        tv_name.setText(StudentStaticModel.getFirst_name() + " "+ StudentStaticModel.getLast_name());

        int color = dbHelper.getStudentColorCode(StudentStaticModel.getId());
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(300);
        gradientDrawable.setColor(color);


        tv_profile.setBackground(gradientDrawable);

        String newFirstName = Character.toString(StudentStaticModel.getFirst_name().charAt(0)).toUpperCase();
        String newLastName = Character.toString(StudentStaticModel.getLast_name().charAt(0)).toUpperCase();

        tv_profile.setText(newFirstName+newLastName);


        iv_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(StudentDashboard.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        setViewPager();
        setData();
        populateRV();
    }

    private void initValues()
    {
        viewPager = findViewById(R.id.view_pager);
        tv_name = findViewById(R.id.tv_student_name);
        recyclerView = findViewById(R.id.rv_student_appointments);
        dbHelper = new DBHelper(this);

        iv_logout = findViewById(R.id.iv_logout_student);

        tv_profile = findViewById(R.id.tv_student_profile);

        mAddFab = findViewById(R.id.add_fab);

        mAddAlarmFab = findViewById(R.id.add_alarm_fab);
//        mAddPersonFab = findViewById(R.id.add_person_fab);

        addAlarmActionText = findViewById(R.id.add_alarm_action_text);
//        addPersonActionText = findViewById(R.id.add_person_action_text);


        mAddAlarmFab.setVisibility(View.GONE);
//        mAddPersonFab.setVisibility(View.GONE);
        addAlarmActionText.setVisibility(View.GONE);
//        addPersonActionText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mAddFab.shrink();

        mAddFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            mAddAlarmFab.show();
//                            mAddPersonFab.show();
                            addAlarmActionText.setVisibility(View.VISIBLE);
//                            addPersonActionText.setVisibility(View.VISIBLE);

                            mAddFab.extend();


                            isAllFabsVisible = true;
                        } else {


                            mAddAlarmFab.hide();
//                            mAddPersonFab.hide();
                            addAlarmActionText.setVisibility(View.GONE);
//                            addPersonActionText.setVisibility(View.GONE);

                            mAddFab.shrink();


                            isAllFabsVisible = false;
                        }
                    }
                });

       /* mAddPersonFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(StudentDashboard.this, "Person Added", Toast.LENGTH_SHORT).show();
                    }
                });*/

        mAddAlarmFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(StudentDashboard.this, StudentMainActivity.class);
                        startActivity(intent);
                    }
                });

    }



    private void setData()
    {
        appointmentModelList = dbHelper.getStudentAppointmentList(StudentStaticModel.getId());

    }

    private void populateRV()
    {
        studentAdapter = new AppointStudentAdapter(this);
        studentAdapter.setAppointmentModelList(appointmentModelList);
        recyclerView.setAdapter(studentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setViewPager()
    {
        int[] images = new int[]{R.drawable.bg1_banner, R.drawable.bg2, R.drawable.bg3};
        adapter = new ViewPagerAdapter(this, images);
        viewPager.setAdapter(adapter);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable(){
                    @Override
                    public void run() {
                        viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%images.length);
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 3000, 7000);

        try {
            AccelerateInterpolator sInterpolator = new AccelerateInterpolator();
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), sInterpolator);
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}