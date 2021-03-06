package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by mateusz on 11.02.18.
 */

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callback, CrimeFragment.Callback {

    Crime selectedCrime=null;
    @Override
    protected android.support.v4.app.Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detail_fragment_container)==null){
            Intent intent=CrimePagerActivity.newIntent(this,crime.getId());
            startActivity(intent);
        }
        else {
            selectedCrime=crime;
            android.support.v4.app.Fragment newDetail=CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeDeleted(Crime crime) {
        if(crime!=null) {
            if (selectedCrime.getId().equals(crime.getId())) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                    if (fragment != null && fragment instanceof CrimeFragment)
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
        }

    }

    public void onCrimeUpdated(Crime crime){
        CrimeListFragment crimeListFragment=(CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUI();
    }
}
