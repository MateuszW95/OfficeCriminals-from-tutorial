package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Singleton
 *
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;
   // private HashMap<UUID,Crime> mCrimes;
    public static CrimeLab get(Context context){
        if(sCrimeLab==null)
        {
            sCrimeLab=new CrimeLab();
        }
        return sCrimeLab;
    }

    private CrimeLab(){
        mCrimes=new ArrayList<>();
        /*for(int i=0;i<100;++i) {
            Crime tmp=new Crime();
            tmp.setTitle("Sprawa #"+i);
            tmp.setSolved(i%2==0);
            mCrimes.add(tmp);

        }*/
    }

    public void add(Crime c){
        mCrimes.add(c);
    }

    public List<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id)
    {
         HashMap<UUID,Crime> tmpCrimes=new HashMap<>();
            for(Crime c: mCrimes){ tmpCrimes.put(c.getId(),c);}

            return tmpCrimes.get(id);


    }

    public void delete(Crime crime){
        mCrimes.remove(crime);
    }
}
