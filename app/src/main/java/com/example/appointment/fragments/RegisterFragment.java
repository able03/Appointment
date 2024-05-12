package com.example.appointment.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appointment.DBHelper;
import com.example.appointment.R;

import com.example.appointment.util.AppData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class RegisterFragment extends Fragment {

    MaterialButton btn_register;
    TextInputEditText et_fname,et_lname,et_phone,et_email;
    TextInputLayout lo_fname, lo_lname,lo_phone,lo_email;

    String fn,ln,phone,email;

    DBHelper db;
    LoginFragment loginFragment;

    TextView tv_statusProgress;

    String receiver_email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DBHelper(getContext());

        btn_register = view.findViewById(R.id.btn_register);

        et_fname = view.findViewById(R.id.et_fname_register);
        et_lname = view.findViewById(R.id.et_lname_register);
        et_phone = view.findViewById(R.id.et_phone_register);
        et_email = view.findViewById(R.id.et_email_register);


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fn = et_fname.getText().toString();
                ln = et_lname.getText().toString();
                phone = et_phone.getText().toString();
                email = et_email.getText().toString();

                Random r = new Random();
                String str = String.format("%04d", r.nextInt(10003));
                db.createStudentAccount(fn,ln,phone,email, Integer.valueOf(str),getRandomColor());
                sendEmail("Your 4-digit PIN Code: ", "From: Android Studio App: Your 4 digit pin code: " + str, et_email.getText().toString());

                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.progress_bar, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogView);
                builder.setCancelable(false);

                tv_statusProgress = dialogView.findViewById(R.id.tv_status);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                tv_statusProgress.setText(" Email sending... ");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_statusProgress.setText("");
                    }
                }, 2000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_statusProgress.setText("Email sent!");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                                Toast.makeText(getContext(), "Email sent", Toast.LENGTH_SHORT).show();

                                Fragment fragmentB = new LoginFragment();
                                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                                transaction.replace(R.id.flMain, fragmentB);
                                transaction.addToBackStack(null);  // Optional: Adds the transaction to the back stack
                                transaction.commit();

                            }
                        }, 2000);
                    }
                }, 3000);




            }
        });



    }

    public void sendEmail(String subject, String content, String to_email){

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", AppData.Gmail_Host);
        properties.put("mail.smtp,port", "465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth", "true");

        Log.d("Email sent to: " , "Email: " + to_email);
        Session session = javax.mail.Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(AppData.Sender_Email_Address, AppData.Sender_Email_Password);

            }
        });
        MimeMessage message = new MimeMessage(session);
       try {
           message.addRecipient(Message.RecipientType.TO, new InternetAddress(to_email));
           message.setSubject(subject);
           message.setText(content);
           Thread thread = new Thread(new Runnable() {
               @Override
               public void run() {
                   try{
                       Transport.send(message);

                   }catch (MessagingException e){
                       e.printStackTrace();
                   }
               }
           });
           thread.start();
       }catch (MessagingException e){
           throw new RuntimeException(e);
       }
    }


    public int getRandomColor()
    {
        Random r = new Random();
        int[] colors = {R.color.color1, R.color.color2, R.color.color3, R.color.color4,
                R.color.color5, R.color.color6, R.color.color7, R.color.color8};

        int colorIndex = r.nextInt(colors.length);

        return ContextCompat.getColor(getContext(), colors[colorIndex]);
    }
}