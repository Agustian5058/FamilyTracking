package com.android.familytracking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PC13 on 8/4/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "FamilyTracking";
    // Contacts table name
    private static final String TABLE_MEMBER = "Member";
    // Shops Table Columns names
    private static final String MEMBER_ID = "MemberId";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_MEMBER + "(" + MEMBER_ID + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);
        onCreate(db);
    }

    // Adding new member
    public boolean insert(String MemberId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MEMBER_ID, MemberId);
        long result = db.insert(TABLE_MEMBER, null, values);
        db.close();
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    // Getting Member
    public String getMember() {
        String MemberId = "";

        String selectQuery = "SELECT * FROM " + TABLE_MEMBER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToNext()) {
            do {
                MemberId = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return MemberId;
    }
}
