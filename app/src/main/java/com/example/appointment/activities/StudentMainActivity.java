    package com.example.appointment.activities;

    import android.app.AlarmManager;
    import android.app.PendingIntent;
    import android.content.Context;
    import android.content.Intent;
    import android.database.Cursor;
    import android.graphics.Bitmap;
    import android.graphics.Color;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.EditText;
    import android.widget.TimePicker;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import com.applandeo.materialcalendarview.CalendarDay;
    import com.applandeo.materialcalendarview.CalendarView;
    import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;

    import com.example.appointment.DBHelper;
    import com.example.appointment.MyBroadcastReceiver;
    import com.example.appointment.models.StudentStaticModel;
    import com.example.appointment.R;

    import com.example.appointment.util.AppData;
    import com.google.android.material.button.MaterialButton;
    import com.google.android.material.chip.Chip;
    import com.google.android.material.chip.ChipGroup;
    import com.google.zxing.BarcodeFormat;
    import com.google.zxing.WriterException;
    import com.google.zxing.common.BitMatrix;
    import com.google.zxing.qrcode.QRCodeWriter;

    import java.io.ByteArrayOutputStream;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.List;
    import java.util.Properties;

    import javax.activation.DataHandler;
    import javax.activation.DataSource;
    import javax.mail.Authenticator;
    import javax.mail.Message;
    import javax.mail.MessagingException;
    import javax.mail.Multipart;
    import javax.mail.PasswordAuthentication;
    import javax.mail.Session;
    import javax.mail.Transport;
    import javax.mail.internet.InternetAddress;
    import javax.mail.internet.MimeBodyPart;
    import javax.mail.internet.MimeMessage;
    import javax.mail.internet.MimeMultipart;
    import javax.mail.util.ByteArrayDataSource;

    public class StudentMainActivity extends AppCompatActivity {

        CalendarView calendarView;
        List<Calendar> calendarList;
        List<String> dateList;
        TimePicker timePicker;
        String date, time;
        ChipGroup chipGroupTeacher, chipGroupSubjects;

        List<String> subjectList;

        int setYear, setMonth, setDay;


        EditText et_purpose;

        MaterialButton btn_saveAppointment;
        String finalTeacher, finalSubject;

        DBHelper db;
        String fname, lname;
        AlarmManager alarmManager;




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_student_main);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });




            db = new DBHelper(this);



            initValues();

            setCalendarSelected();
            setTimeSelected();
            setChipTeachers();
            setSelectedSubjects();



            btn_saveAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cursor c = db.getTeacher(fname);
                    c.moveToFirst();
                    int teacher_id = c.getInt(0);

                    if (db.createAppointment(setYear, setMonth, setDay, time, finalSubject, fname, et_purpose.getText().toString(), teacher_id, StudentStaticModel.getId()))
                    {

                        Toast.makeText(StudentMainActivity.this, "Created appointment", Toast.LENGTH_SHORT).show();


                        String email_text = date +"\n" +time + "\n" + fname +  " " + lname;
                        sendEmailWithQRCode("Appointment set", email_text, StudentStaticModel.getEmail());


                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();

                        setAlarm(hour, minute);

                        Intent intent = new Intent(StudentMainActivity.this, StudentDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }
            });
        }

        private void initValues()
        {
            btn_saveAppointment = findViewById(R.id.btn_save_appointment);
            calendarView = findViewById(R.id.calendar);
            calendarList = new ArrayList<>();
            dateList = new ArrayList<>();
            timePicker = findViewById(R.id.time_picker);
            chipGroupTeacher = findViewById(R.id.chip_group_teacher);
            chipGroupSubjects = findViewById(R.id.chip_group_subjects);
            et_purpose = findViewById(R.id.et_purpose);






            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(year, month, day-1);
            calendarView.setMinimumDate(calendar);

        }

        private void setAlarm(int hour, int minute)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            String student_name = StudentStaticModel.getFirst_name() + "" + StudentStaticModel.getLast_name();
            String teacher_name = fname+" " +lname;
            int requesCode = (int) System.currentTimeMillis();

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent z = new Intent(StudentMainActivity.this, MyBroadcastReceiver.class);
            z.putExtra("teacher", teacher_name);
            z.putExtra("student", student_name);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(StudentMainActivity.this,  requesCode, z, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }


        public void sendEmailWithQRCode(String subject, String content, String to_email) {

            Bitmap qrCodeBitmap = encodeAsBitmap(content);


            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", AppData.Gmail_Host);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");


            Log.d("Email sent to: ", "Email: " + to_email);


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


                Multipart multipart = new MimeMultipart();


                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(content);


                multipart.addBodyPart(textPart);


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                MimeBodyPart imagePart = new MimeBodyPart();
                DataSource qrCodeSource = new ByteArrayDataSource(byteArray, "image/png");
                imagePart.setDataHandler(new DataHandler(qrCodeSource));
                imagePart.setHeader("Content-ID", "<qr_code_image>");


                multipart.addBodyPart(imagePart);

                message.setContent(multipart);


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Transport.send(message);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }


        private void setCalendarSelected()
        {
            calendarView.setOnCalendarDayClickListener(new OnCalendarDayClickListener() {
                @Override
                public void onClick(@NonNull CalendarDay calendarDay) {
                    Calendar c = calendarDay.getCalendar();

                    String year = String.format("%04d",  c.get(Calendar.YEAR));
                    String month =  String.format("%02d",  c.get(Calendar.MONTH)+1);
                    String dayOfMonth =  String.format("%02d",  c.get(Calendar.DAY_OF_MONTH));

                    setYear = Integer.valueOf(year);
                    setMonth = Integer.valueOf(month);
                    setDay = Integer.valueOf(dayOfMonth);

                    date = String.format("%s/%s/%s", year, month, dayOfMonth);


                   for(int i=0; i<dateList.size(); i++)
                   {
                       Log.d("Debugging", (i+1) + ". " + dateList.get(i));

                   }
                }
            });

        }


        private void setTimeSelected()
        {
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker timePicker, int hours, int minutes) {
                    String hour = String.format("%02d", timePicker.getHour());
                    String minute = String.format("%02d", timePicker.getMinute());

                    time = String.format("%s:%s", hour, minute);

                    Log.d("Debugging", "Time: " + time);
                }
            });
        }


        public Bitmap encodeAsBitmap(String str)
        {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = null;
            try
            {
                bitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, 400, 400);
            } catch (WriterException e)
            {
                throw new RuntimeException(e);
            }

            int w = bitMatrix.getWidth();
            int h = bitMatrix.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    pixels[y * w + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        }


        private void setChipTeachers() {
            chipGroupTeacher.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull ChipGroup chipGroup, int checkedId) {
                    Chip selectedChip = findViewById(checkedId);
                    if (selectedChip != null) {
                        chipGroupSubjects.removeAllViews();

                        if (selectedChip.getId() == R.id.chip_randy) {
                            finalTeacher = "Randy B. Quibic";
                            fname = "Randy B.";
                            lname = "Quibic";
                            addSubjectChips("Math", "Programming 1", "Networking 1");
                        } else if (selectedChip.getId() == R.id.chip_ivan) {
                            finalTeacher = "Ivan Barron";
                            fname = "Ivan";
                            lname = " Barron";
                            addSubjectChips("Programming 2", "Networking 2");
                        } else if (selectedChip.getId() == R.id.chip_zyrel) {
                            finalTeacher = "Zyrel Iruguin";
                            fname = "Zyrel";
                            lname = " Iruguin";
                            addSubjectChips("Cybersecurity", "Programming 3");
                        }

                        Log.d("Debugging", finalTeacher);
                    }
                }
            });
        }

        private void addSubjectChips(String... subjects) {
            for (String subject : subjects) {
                Chip chip = new Chip(StudentMainActivity.this);
                chip.setText(subject);
                chip.setCheckable(true);
                chipGroupSubjects.addView(chip);
            }
        }

        private void setSelectedSubjects()
        {
            chipGroupSubjects.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull ChipGroup chipGroup, @NonNull List<Integer> list) {
                    for(Integer i : list)
                    {
                        Chip chip = findViewById(i);
                        finalSubject = String.valueOf(chip.getText());
                        Log.d("Debugging", finalSubject);
                    }
                }
            });
        }


    }