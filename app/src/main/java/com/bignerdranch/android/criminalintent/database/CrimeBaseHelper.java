package com.bignerdranch.android.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mateusz on 23.03.18.
 */

/*
Class responsible for create dataBase
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DATABASE_NAME="crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+CrimeDbSchema.CrimeTable.NAME+"("+
                CrimeDbSchema.CrimeTable.Cols.UUID+","+
                CrimeDbSchema.CrimeTable.Cols.TITLE+","+
                CrimeDbSchema.CrimeTable.Cols.DATE+","+
                CrimeDbSchema.CrimeTable.Cols.SOLVED+","+
                CrimeDbSchema.CrimeTable.Cols.SUSPECT+","+
                CrimeDbSchema.CrimeTable.Cols.SUSPECT_NUMBER+")"
                );
        db.execSQL("create index index_id on "+CrimeDbSchema.CrimeTable.NAME+" ("+CrimeDbSchema.CrimeTable.Cols.UUID+")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
