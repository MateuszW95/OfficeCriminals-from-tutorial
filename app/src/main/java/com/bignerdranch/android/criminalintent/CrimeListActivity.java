package com.bignerdranch.android.criminalintent;

import android.app.Fragment;

/**
 * Created by mateusz on 11.02.18.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected android.support.v4.app.Fragment createFragment() {
        return new CrimeListFragment();
    }
}
