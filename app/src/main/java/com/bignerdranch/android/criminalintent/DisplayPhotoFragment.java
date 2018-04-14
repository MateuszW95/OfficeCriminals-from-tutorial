package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by mateusz on 12.04.18.
 */

public class DisplayPhotoFragment extends android.support.v4.app.DialogFragment {

    private static File file=null;
    private ImageView mImageView=null;
    public static String ARGS_PATH_STRING ="URI_STRING";

    public static DisplayPhotoFragment newInstance(String uriString)
    {
        Bundle args=new Bundle();
        args.putString(ARGS_PATH_STRING,uriString);
        DisplayPhotoFragment fragment=new DisplayPhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String path=getArguments().getString(ARGS_PATH_STRING);
        final Bitmap bitmap = BitmapFactory.decodeFile(path);
        final View v= LayoutInflater.from(getActivity()).inflate(R.layout.display_photo_fragment,null);

        mImageView=v.findViewById(R.id.IV_photo);
        mImageView.setMinimumWidth(bitmap.getWidth());
        mImageView.setMinimumWidth(bitmap.getHeight());
        mImageView.setMaxWidth(bitmap.getWidth());
        mImageView.setMaxHeight(bitmap.getHeight());
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                mImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //bitmap[0] =PictureUtils.getScaledBitmap(path,mImageView.getMaxWidth(),mImageView.getMaxHeight());
                mImageView.setImageBitmap(bitmap);
            }
        });



        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }
}
///data/user/0/com.bignerdranch.android.criminalintent/files/IMG_6806ce45-6e5a-450e-96a4-a8f99f829719.jpg
///data/user/0/com.bignerdranch.android.criminalintent/files/IMG_6806ce45-6e5a-450e-96a4-a8f99f829719.jpg