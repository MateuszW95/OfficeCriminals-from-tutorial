package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

/**
 * Created by mateusz on 16.02.18.
 */

public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callback {
    private static final String EXTRA_CRIME_ID="com.bignerdranch.android.cmininalintent.crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mFirstButton;
    private  Button mLastButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        int pos=-1;
        mViewPager=(ViewPager)findViewById(R.id.crime_view_pager);
        mCrimes=CrimeLab.get(this).getCrimes();
        mFirstButton=findViewById(R.id.bt_first);
        mLastButton=findViewById(R.id.bt_last);
        UUID crimeId=(UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mViewPager.setCurrentItem(0);
                mFirstButton.setEnabled(false);
            }
        });
        mLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCrimes.size()-1);
                mLastButton.setEnabled(false);
            }
        });

        FragmentManager fragmentManager=getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime=mCrimes.get(position);
                if(position==0 )
                    mFirstButton.setEnabled(false);
                else
                    mFirstButton.setEnabled(true);
                if(position==mCrimes.size()-1 )
                    mLastButton.setEnabled(false);
                else
                    mLastButton.setEnabled(true);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for (int i=0;i<mCrimes.size();++i) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }



        }


    public static Intent newIntent(Context packageContext, UUID crimeID){
        Intent intent=new Intent(packageContext,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeID);
        return intent;
    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}
