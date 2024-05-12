package com.example.appointment;

import java.util.ArrayList;
import java.util.List;

public class ChipArrayList
{
    List<String> quibic_subject_list;
    List<String> barron_subject_list;
    List<String> iruguin_subject_list;

    public List<String> getQuibicSubjectList()
    {
        quibic_subject_list = new ArrayList<>();

        quibic_subject_list.add("Math");
        quibic_subject_list.add("Programming 1");
        quibic_subject_list.add("Networking 1");

        return quibic_subject_list;
    }


    public List<String> getBarronSubjectList()
    {
        barron_subject_list = new ArrayList<>();

        barron_subject_list.add("Programming 2");
        barron_subject_list.add("Networking 2");


        return barron_subject_list;
    }


    public List<String> getIruguinSubjectList()
    {
        iruguin_subject_list = new ArrayList<>();

        iruguin_subject_list.add("Cybersecurity");
        iruguin_subject_list.add("Programming 3");


        return iruguin_subject_list;
    }
}
