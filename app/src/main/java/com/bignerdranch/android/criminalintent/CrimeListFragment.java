package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mateusz on 11.02.18.
 */

public class CrimeListFragment extends android.support.v4.app.Fragment{

    private RecyclerView mCrimeRecycleView;
    private Button mAddButton;
    private TextView mEptyTextViewl;
    private  CrimeAdapter mAdapter;
    private int pos=0;
    private boolean mSubtitleVisible;
    private static final int CODE=123;
    private static final String KEY_VISIBLETITLE="QWeeqw";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_crime_list,container,false);//rozwinięcie widoku
        if(savedInstanceState!=null)
        {
            mSubtitleVisible=savedInstanceState.getBoolean(KEY_VISIBLETITLE);
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
        }
        mCrimeRecycleView=(RecyclerView)v.findViewById(R.id.crime_recycle_view);
        mAddButton=v.findViewById(R.id.bt_addNew);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).add(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
            }
        });
        mEptyTextViewl=v.findViewById(R.id.tv_empty);
        mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        updateView();
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime: {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).add(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            }
            case R.id.show_subtitle:{
                mSubtitleVisible=!mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;}

         default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        updateView();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //wywołania zwrotne
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);

        MenuItem subtileItem=menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            subtileItem.setTitle(R.string.hide_subtitle);
        }else {
            subtileItem.setTitle(R.string.show_subtitle);
        }

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

            Intent intent=CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            pos=getAdapterPosition();
            startActivityForResult(intent,CODE);
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

        public void setCrimes(List<Crime> crimes){mCrimes=crimes;}
    }

        private void updateUI(){
            CrimeLab crimeLab=CrimeLab.get(getActivity());
            List<Crime> crimes=crimeLab.getCrimes();
            if(mAdapter==null) {
                mAdapter = new CrimeAdapter(crimes);
                mCrimeRecycleView.setAdapter(mAdapter);
            }
            else{
                mAdapter.setCrimes(crimes);
                mAdapter.notifyDataSetChanged();

            }
            updateSubtitle();
        }

    private void updateSubtitle(){
            CrimeLab crimeLab=CrimeLab.get(getActivity());
            int crimeCount=crimeLab.getCrimes().size();

            String subtitle=getResources().getQuantityString(R.plurals.criminalCount,crimeCount,crimeCount);
             if(!mSubtitleVisible){
                 subtitle=null;
             }
            AppCompatActivity activity=(AppCompatActivity)getActivity();
            activity.getSupportActionBar().setSubtitle(subtitle);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VISIBLETITLE, mSubtitleVisible);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK)return;
        if(requestCode==CODE){
            Crime tmp=CrimeLab.get(getActivity()).getCrime(CrimeFragment.getDataFromResult(data));
            CrimeLab.get(getActivity()).delete(tmp);
            mAdapter.notifyItemRemoved(pos);
        }
    }

    private void updateView(){
        if(CrimeLab.get(getActivity()).getCrimes().size()==0)
        {
            mAddButton.setVisibility(View.VISIBLE);
            mEptyTextViewl.setVisibility(View.VISIBLE);
            mCrimeRecycleView.setVisibility(View.GONE);
        }
        else
        {
            mAddButton.setVisibility(View.GONE);
            mEptyTextViewl.setVisibility(View.GONE);
            mCrimeRecycleView.setVisibility(View.VISIBLE);
        }
    }
}
