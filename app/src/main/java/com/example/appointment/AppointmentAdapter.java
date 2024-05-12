package com.example.appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.appointment.models.AppointmentModel;

import java.util.List;

public class AppointmentAdapter extends BaseAdapter
{
    Context context;
    TextView tv_name, tv_email, tv_phone, tv_subject, tv_date, tv_time, tv_purpose;
    List<AppointmentModel> appointmentModelList;


    public AppointmentAdapter(Context context) {
        this.context = context;
    }

    public void setAppointmentModelList(List<AppointmentModel> appointmentModelList) {
        this.appointmentModelList = appointmentModelList;
    }

    @Override
    public int getCount() {
        return appointmentModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return appointmentModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
      if(view == null)
      {
          view = LayoutInflater.from(context).inflate(R.layout.lv_appointments_layout, viewGroup, false);
      }

      tv_name = view.findViewById(R.id.tv_name_lv);
      tv_time = view.findViewById(R.id.tv_time_lv);
      tv_email = view.findViewById(R.id.tv_email_lv);
      tv_phone = view.findViewById(R.id.tv_phone_lv);
      tv_purpose = view.findViewById(R.id.tv_purpose_lv);

      tv_subject = view.findViewById(R.id.tv_Subject_lv);

        tv_time.setText(appointmentModelList.get(i).getTime());
//        tv_name.setText(appointmentModelList.get(i).getTeacher_name()));
//        tv_email.setText(appointmentModelList.get(i).getE()));
//        tv_phone.setText(appointmentModelList.get(i).getPurpose()));
        tv_purpose.setText(appointmentModelList.get(i).getPurpose());
        tv_subject.setText(appointmentModelList.get(i).getSubject());




      return view;
    }
}
