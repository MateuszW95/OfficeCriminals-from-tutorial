package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by mateusz on 08.02.18.
 */

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID="crime_id";
    private static final String DIALOG_DATE="DialogDate";
    private static final String DIALOG_TIME="DialogTime";
    private static final int REQUEST_CODE=0;
    private static final int REQUEST_CODE_TIME=1;
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mTimeButton;
    private Button mDeleteButton;
    private boolean ifdeleted=false;
    private static String EXTRA_IFDELETED="99##@!@!@";
    private static String EXTRA_IDCRIME="33@#@!";


    public  static UUID getDataFromResult(Intent intent){
        if(intent.getBooleanExtra(EXTRA_IFDELETED,false)){
            return (UUID)intent.getSerializableExtra(EXTRA_IDCRIME);
        }
        else return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId =(UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);

    }
    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args= new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);
        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_crime,container,false);

        mTitleField=(EditText)v.findViewById(R.id.crime_title);
        mDateButton=(Button)v.findViewById(R.id.crime_date);
        mTimeButton=v.findViewById(R.id.crime_time);
        mDeleteButton=v.findViewById(R.id.bt_deleteCrime);
        updateDate();
        mTimeButton.setText(mCrime.getDate().toString());
        updateTime();

        mDeleteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ifdeleted=true;

                    Intent data=new Intent();
                    data.putExtra(EXTRA_IFDELETED,ifdeleted);
                    data.putExtra(EXTRA_IDCRIME,mCrime.getId());

                    getActivity().setResult(Activity.RESULT_OK,data);

                getActivity().finish();
            }
        });
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=getFragmentManager();
                TimePickerFragment timePickerFragment=TimePickerFragment.newInstance(mCrime.getDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this,REQUEST_CODE_TIME);
                timePickerFragment.show(manager,DIALOG_TIME);

            }
        });
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager=getFragmentManager();
                DatePickerFragment dialog=DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_CODE);
                dialog.show(manager,DIALOG_DATE);
            }
        });
        mTitleField.setText(mCrime.getTitle());


        mSolvedCheckBox=(CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK) return;

        if(requestCode==REQUEST_CODE){
            Date date =(Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
        if(requestCode==REQUEST_CODE_TIME){
            Date date=(Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.getDate().setHours(date.getHours());
            mCrime.getDate().setMinutes(date.getMinutes());
            updateTime();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private void updateDate() {
        SimpleDateFormat spf=new SimpleDateFormat("d.M.YYYY");
        mDateButton.setText(spf.format(mCrime.getDate()));
    }

    private void updateTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("k:m");
        mTimeButton.setText(sdf.format(mCrime.getDate()));
    }


}
