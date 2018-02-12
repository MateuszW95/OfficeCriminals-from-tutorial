package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Singleton
 *
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;
    public static CrimeLab get(Context context){
        if(sCrimeLab==null)
        {
            sCrimeLab=new CrimeLab();
        }
        return sCrimeLab;
    }

    private CrimeLab(){
        mCrimes=new ArrayList<>();
        for(int i=0;i<100;++i) {
            Crime tmp=new Crime();
            tmp.setTitle("Sprawa #"+i);
            tmp.setSolved(i%2==0);
            mCrimes.add(tmp);

        }
    }

    public List<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id)
    {
        for(Crime c: mCrimes){
            if(c.getId().equals(id)) return c;
        }
        return null;
    }
}
