package com.example.appointment.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appointment.DBHelper;
import com.example.appointment.R;
import com.example.appointment.RandomColorGenerator;
import com.example.appointment.fragments.LoginFragment;
import com.example.appointment.fragments.RegisterFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btn_login, btn_register;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initValues();
        setListeners();
        setFragment(new LoginFragment());


        Cursor cursor = dbHelper.getTeacherCount();
        cursor.moveToFirst();
        if(cursor.getCount() == 0)
        {
            dbHelper.createTeacherAccount("Randy B.", "Quibic", "4827", getRandomColor());
            dbHelper.createTeacherAccount("Ivan", "Barron", "0314", getRandomColor());
            dbHelper.createTeacherAccount("Zyrel", "Iruguin", "5276",getRandomColor());
        }
        dbHelper.close();
    }

    private void initValues()
    {
        btn_login = findViewById(R.id.btn_main_login);
        btn_register = findViewById(R.id.btn_main_register);
        dbHelper = new DBHelper(this);
    }

    private void setListeners()
    {
        btn_login.setOnClickListener(login -> {
            setFragment(new LoginFragment());
        });

        btn_register.setOnClickListener(register -> {
            setFragment(new RegisterFragment());
        });
    }

    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMain, fragment);
        fragmentTransaction.commit();
    }


    public int getRandomColor()
    {
        Random r = new Random();
        int[] colors = {R.color.color1, R.color.color2, R.color.color3, R.color.color4,
                R.color.color5, R.color.color6, R.color.color7, R.color.color8};

        int colorIndex = r.nextInt(colors.length);

        return ContextCompat.getColor(this, colors[colorIndex]);
    }


}