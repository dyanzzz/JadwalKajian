package com.hyundaimobil.jadwalkajian.config;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by User HMI on 9/19/2017.
 */

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;

    public DatePickerFragment() {}

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    @SuppressLint("NewApi")
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year    = c.get(Calendar.YEAR);
        int month   = c.get(Calendar.MONTH);
        int day     = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
        DatePicker dp = dpd.getDatePicker();
        //dp.setMinDate(c.getTimeInMillis());//get the current day
        //c.add(Calendar.DAY_OF_MONTH,14);
        //dp.setMaxDate(c.getTimeInMillis());
        return dpd;
    }



}