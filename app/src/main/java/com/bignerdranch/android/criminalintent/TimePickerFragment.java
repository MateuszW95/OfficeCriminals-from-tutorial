package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by mateusz on 19.02.18.
 */

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_TIME="time";
    public static final String EXTRA_TIME="com.bignerdranch.android.cmininalintent.time";
    private TimePicker mTimePicker;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Date date=(Date)getArguments().getSerializable(ARG_TIME);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int hour,minutes;
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minutes=calendar.get(Calendar.MINUTE);



        View v= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);
        mTimePicker=v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minutes);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_piker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour,min;
                        hour=mTimePicker.getHour();
                        min=mTimePicker.getMinute();

                        Date date= new GregorianCalendar(0,0,0,hour,min).getTime();
                        setResults(Activity.RESULT_OK,date);
                    }
                })
                .create();
    }

    public static  TimePickerFragment newInstance(Date date){
        Bundle args=new Bundle();
        args.putSerializable(ARG_TIME,date);
        TimePickerFragment timePickerFragment=new TimePickerFragment();
        timePickerFragment.setArguments(args);

        return timePickerFragment;

    }

    private void setResults(int resultCode,Date date){
        if(getTargetFragment()==null) return;
        Intent intent=new Intent();
        intent.putExtra(EXTRA_TIME,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
