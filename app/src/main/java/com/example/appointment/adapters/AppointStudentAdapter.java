package com.example.appointment.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointment.DBHelper;
import com.example.appointment.R;
import com.example.appointment.models.AppointmentModel;
import com.example.appointment.models.StudentStaticModel;
import com.example.appointment.models.TeacherStaticModel;

import java.util.List;

public class AppointStudentAdapter extends RecyclerView.Adapter<AppointStudentAdapter.MyViewHolder>
{

    Context context;
    List<AppointmentModel> appointmentModelList;

    public AppointStudentAdapter(Context context) {
        this.context = context;
    }

    public void setAppointmentModelList(List<AppointmentModel> appointmentModelList) {
        this.appointmentModelList = appointmentModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DBHelper dbHelper = new DBHelper(context);
        int i = position;

        int id = appointmentModelList.get(i).getId();
        int teacher_id = appointmentModelList.get(i).getTeacher_id();

        holder.tv_id.setText(String.valueOf(id));
        holder.tv_time.setText(appointmentModelList.get(i).getTime());
        String year = appointmentModelList.get(i).getYear();
        String month = appointmentModelList.get(i).getMonth();
        String day = appointmentModelList.get(i).getDay();

        String date = year +"/" + month + "/" +  day;
        holder.tv_date.setText(date);
        holder.tv_purpose.setText(appointmentModelList.get(i).getPurpose());
        holder.tv_subject.setText(appointmentModelList.get(i).getSubject());



        Cursor cursor = dbHelper.findStudent(appointmentModelList.get(i).getStudent_id());
        if(cursor.moveToFirst())
        {
            String fname = cursor.getString(1);
            String lname = cursor.getString(2);
            String phone = cursor.getString(3);
            String email = cursor.getString(4);

            holder.tv_name.setText(fname +" " + lname);
            holder.tv_email.setText(email);
            holder.tv_phone.setText(phone);


            int color = dbHelper.getStudentColorCode(appointmentModelList.get(i).getStudent_id());
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            gradientDrawable.setCornerRadius(300);
            gradientDrawable.setColor(color);

            holder.tv_profile.setBackground(gradientDrawable);

            String str1 = Character.toString(fname.charAt(0)).toUpperCase();
            String str2 = Character.toString(lname.charAt(0)).toUpperCase();
            holder.tv_profile.setText(str1+str2);
        }




        boolean isExpanded = appointmentModelList.get(i).isExpanded();
        holder.expandableLayoutBody.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.expandableLayoutHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AppointmentModel model = appointmentModelList.get(i);
                        model.setExpanded(!model.isExpanded());
                        notifyItemChanged(i);
                    }
                });


        Cursor cursor1 = dbHelper.getTeacher(teacher_id);
        if(cursor1.moveToFirst())
        {
            String fname = cursor1.getString(cursor1.getColumnIndexOrThrow("first_name"));
            String lname = cursor1.getString(cursor1.getColumnIndexOrThrow("last_name"));

            holder.tv_teacher_name.setText(fname+" "+lname);
        }




        dbHelper.close();
    }

    @Override
    public int getItemCount() {
        return appointmentModelList.size();
    }


     class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name, tv_email, tv_phone, tv_subject, tv_date, tv_time,
                tv_purpose, tv_id, tv_profile, tv_teacher_name;
        LinearLayout expandableLayoutBody;
        RelativeLayout expandableLayoutHeader;
        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name_rv);
            tv_time = itemView.findViewById(R.id.tv_time_rv);
            tv_email = itemView.findViewById(R.id.tv_email_rv);
            tv_phone = itemView.findViewById(R.id.tv_phone_rv);
            tv_purpose = itemView.findViewById(R.id.tv_purpose_rv);
            tv_date = itemView.findViewById(R.id.tv_date_rv);
            tv_subject = itemView.findViewById(R.id.tv_Subject_rv);
            tv_id = itemView.findViewById(R.id.tv_id_rv);
            tv_profile = itemView.findViewById(R.id.tv_profile);
            tv_teacher_name = itemView.findViewById(R.id.tv_teacher_name_rv);

            expandableLayoutBody = itemView.findViewById(R.id.rv_expandable_body);
            expandableLayoutHeader = itemView.findViewById(R.id.rv_expandable_header);


        }
    }
}
