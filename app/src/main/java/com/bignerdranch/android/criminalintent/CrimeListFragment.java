package com.bignerdranch.android.criminalintent;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mateusz on 11.02.18.
 */

public class CrimeListFragment extends android.support.v4.app.Fragment{

    private RecyclerView mCrimeRecycleView;
    private  CrimeAdapter mAdapter;
    private int pos=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_crime_list,container,false);//rozwinięcie widoku

        mCrimeRecycleView=(RecyclerView)v.findViewById(R.id.crime_recycle_view);
        mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;

        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater,ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_crime,parent,false));

            mTitleTextView=(TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView=(TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView=(ImageView)itemView.findViewById(R.id.crime_solved);
            itemView.setOnClickListener(this);
        }

        public void Bind(Crime crime){
            mCrime=crime;
            mTitleTextView.setText(mCrime.getTitle());
            Date date=mCrime.getDate();
            SimpleDateFormat sdf= new SimpleDateFormat("E,dd MMMM YYY");

            mDateTextView.setText(sdf.format(date));
            mSolvedImageView.setVisibility(crime.isSolved()?View.VISIBLE:View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            Context c=getActivity();
            Intent intent=CrimeActivity.newIntent(getActivity(),mCrime.getId());
            pos=getAdapterPosition();
            startActivity(intent);
        }
    }


    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }



        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime=mCrimes.get(position);
            holder.Bind(crime);

        }


        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

        private void updateUI(){
            CrimeLab crimeLab=CrimeLab.get(getActivity());
            List<Crime> crimes=crimeLab.getCrimes();
            if(mAdapter==null) {
                mAdapter = new CrimeAdapter(crimes);
                mCrimeRecycleView.setAdapter(mAdapter);
            }
            else{
                mAdapter.notifyItemChanged(pos);
            }
        }

}
