package com.example.appointment.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appointment.DBHelper;
import com.example.appointment.activities.MainActivity;
import com.example.appointment.activities.StudentDashboard;
import com.example.appointment.models.StudentStaticModel;
import com.example.appointment.models.TeacherStaticModel;
import com.example.appointment.R;
import com.example.appointment.RandomColorGenerator;
import com.example.appointment.activities.StudentMainActivity;
import com.example.appointment.activities.TeacherMainActivity;
import com.example.appointment.models.TeacherStaticModel;
import com.google.android.material.button.MaterialButton;


public class LoginFragment extends Fragment {


    MaterialButton btn_login;

    DBHelper db;

    EditText et1, et2, et3, et4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValues();
        setPin();

        Log.d("COLOR CODE: " , "Color code: " + RandomColorGenerator.getRandomColorCode());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPin = et1.getText().toString() + et2.getText().toString() + et3.getText().toString() + et4.getText().toString();
                Log.d("New pin: ", "new Pin: " + newPin);
                Cursor c = db.FindTeacherAccount(newPin);
                c.moveToFirst();
                Log.d("CGETCOUNT: " , " C.GetCount() : " + c.getCount());
                if(c.moveToFirst()){
                    int teacher_id = c.getInt(0);
                    String first_name = c.getString(1);
                    String last_name = c.getString(2);
                    int teacher_pin = c.getInt(3);
                    String teacher_color_code = c.getString(4);

                    new TeacherStaticModel(teacher_id, first_name, last_name, null, teacher_pin, teacher_color_code, "teacher");
                    Intent i = new Intent(getContext(), TeacherMainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else{
                    Cursor c1 = db.FindStudentAccount(Integer.valueOf(newPin));
                    if(c1.moveToFirst()){
                        Log.d("Student: " , "student login running");
                        int student_id = c1.getInt(0);
                        String first_name = c1.getString(1);
                        String last_name = c1.getString(2);
                        String contact = c1.getString(3);
                        String email = c1.getString(4);
                        int pin = c1.getInt(5);
                        String color_code = c1.getString(6);

                        new StudentStaticModel(student_id, first_name, last_name, contact,email,pin,color_code, "student");
                        Intent i = new Intent(getContext(), StudentDashboard.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);


                    }


                }

            }
        });
    }




    private void initValues()
    {

        db = new DBHelper(getContext());
        et1 = getView().findViewById(R.id.et1);
        et2 = getView().findViewById(R.id.et2);
        et3 = getView().findViewById(R.id.et3);
        et4 = getView().findViewById(R.id.et4);
        btn_login = getView().findViewById(R.id.btn_login);
    }

    private void setPin()
    {
        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 1)
                {
                    et2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 1)
                {
                    et3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 1)
                {
                    et4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 1)
                {
                    et2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }






}