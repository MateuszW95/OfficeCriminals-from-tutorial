package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by mateusz on 08.02.18.
 */


public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID="crime_id";
    private static final String DIALOG_DATE="DialogDate";
    private static final String DIALOG_TIME="DialogTime";
    private static final String DISPALY_PHOTO_DIALOG="DisplayDialog";
    private static final int REQUEST_CODE=0;
    private static final int REQUEST_CODE_TIME=1;
    private static final int REQUEST_CONTACT=11;
    private static final int READ_CONTACT_REQUEST_CODE =88 ;
    private static final int REQUEST_PHOTO=2;
    private Crime mCrime;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mTimeButton;
    private Button mDeleteButton;
    private Button mReportButton;
    private Button mCallSuspectButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callback mCallback;

    private boolean ifdeleted=false;
    private static String EXTRA_IFDELETED="99##@!@!@";
    private static String EXTRA_IDCRIME="33@#@!";

    public interface Callback {
        void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback =(Callback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback =null;
    }
    private void updateCrime(){
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallback.onCrimeUpdated(mCrime);

    }

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
        mPhotoFile=CrimeLab.get(getContext()).getPhotoFile(mCrime);
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
        mReportButton=v.findViewById(R.id.crime_report);
        updateDate();
        mTimeButton.setText(mCrime.getDate().toString());
        updateTime();


        final Intent pickContact=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        mSuspectButton=v.findViewById(R.id.crime_suspect);
        mCallSuspectButton=v.findViewById(R.id.bt_call_suspect);
        if(mCrime.getSuspect()==null){
            mCallSuspectButton.setEnabled(false);
        }
        mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+mCrime.getSuspectNumber()));
                startActivity(i);
                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(i);
                }
            }
        });

        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_DENIED){
                    requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS},
                            READ_CONTACT_REQUEST_CODE);
                }
                else
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });
        if(mCrime.getSuspect()!=null){
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager=getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY)==null){
            mSuspectButton.setEnabled(false);
        }

        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i=new Intent(Intent.ACTION_SEND);
//                i.setType("text/type");
//                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
//                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
//                i=Intent.createChooser(i,getString(R.string.sent_report));
//                startActivity(i);

                Intent i= ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/type")
                        .setChooserTitle(R.string.sent_report)
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .createChooserIntent();

                startActivity(i);
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
               CrimeLab.get(getContext()).delete(mCrime);
               onDestroy();
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
                updateCrime();
            }
        });
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mCrime.setTitle(s.toString());
                        updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPhotoButton=v.findViewById(R.id.crime_camera);
        mPhotoView=v.findViewById(R.id.crime_photo);

        final Intent captureImage=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto= mPhotoFile !=null && captureImage.resolveActivity(packageManager)!=null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri= FileProvider.getUriForFile(getContext(),"com.bignerdranch.android.criminalintent.fileprovider",mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                List<ResolveInfo> cameraActivities=getActivity().getPackageManager().queryIntentActivities(captureImage,PackageManager.MATCH_DEFAULT_ONLY);

                for(ResolveInfo a:cameraActivities){
                    getActivity().grantUriPermission(a.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });

        mPhotoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                updatePhotoView(mPhotoView);
            }
        });
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path=mPhotoFile.getPath();
                FragmentManager fragmentManager=getFragmentManager();
                if(mPhotoFile!=null && mPhotoFile.exists()){
                    DisplayPhotoFragment dialog=DisplayPhotoFragment.newInstance(path);
                    dialog.show(fragmentManager,DISPALY_PHOTO_DIALOG);
                }
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
            updateCrime();
        }
        if(requestCode==REQUEST_CODE_TIME){
            Date date=(Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.getDate().setHours(date.getHours());
            mCrime.getDate().setMinutes(date.getMinutes());
            updateTime();
            updateCrime();
        }
        else if(requestCode==REQUEST_CONTACT){
            Uri contactUri=data.getData();
            //okreśła pola których wartości zostaną zwrócone przez zapytanie
            String[] queryFields=new String[]{ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts._ID};

            //wykonanie zapytania-contactUri spełania tutaj role where

            Cursor c=getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);
            try{
                if(c.getCount()==0){
                    return;
                }

                c.moveToFirst();
                String suspect=c.getString(0);
                long id=c.getLong(1);
                //zapytanie do tabeli CommonDataKinds
                c=getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+id,
                        null,
                        null);

                if(c.getCount()==0)
                    c.close();
                c.moveToFirst();
                mCrime.setSuspectNumber(c.getString(0));
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
                updateCrime();
            }
            finally {
                c.close();
            }

        }
        else if(requestCode==REQUEST_PHOTO){
            Uri uri=FileProvider.getUriForFile(getContext(),"com.bignerdranch.android.criminalintent.fileprovider",mPhotoFile);

            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView(mPhotoView);
            updateCrime();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==READ_CONTACT_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(getContext(),"READ_CONTACT permission denied",Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getContext(),"READ_CONTACT permission granted",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mCrime.getSuspectNumber()==null) mCallSuspectButton.setEnabled(false); else mCallSuspectButton.setEnabled(true);
    }

    private void updateDate() {
        SimpleDateFormat spf=new SimpleDateFormat("d.M.YYYY");
        mDateButton.setText(spf.format(mCrime.getDate()));
    }

    private void updateTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("k:m");
        mTimeButton.setText(sdf.format(mCrime.getDate()));
    }

    private String getCrimeReport(){
        String solvedString=null;
        if(mCrime.isSolved()){
            solvedString=getString(R.string.crime_report_solved);
        }
        else {
            solvedString=getString(R.string.crime_report_unsolved);
        }

        String dateFormat="EEE, MMM dd";
        String dateString= DateFormat.format(dateFormat,mCrime.getDate()).toString();

        String suspect=mCrime.getSuspect();
        if(suspect==null){
            suspect=getString(R.string.crime_report_no_suspect);
        }
        else {
            suspect=getString(R.string.crime_report_suspect);
        }
        String report=getString(R.string.crime_report,mCrime.getTitle(),dateString,solvedString,suspect);
        return report;
    }

    private void updatePhotoView(ImageView container){
        if(mPhotoFile==null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }
        else
        {
            Bitmap bitmap=null;
            if(container==null)
                bitmap=PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            else
                bitmap=PictureUtils.getScaledBitmap(mPhotoFile.getPath(),container);
            mPhotoView.setImageBitmap(bitmap);
        }
    }


}
