package com.example.mai.moviewithfrag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aya on 11/21/2016.
 */

public class Favourite extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favM.db";
    public static final String TABLE_NAME = "fav_movies_table";

    public static final String COL_1 = "TITLE";
    public static final String COL_2 = "POSTER";
    public static final String COL_3 = "PLOTSYNOPSIS";
    public static final String COL_4 = "RATING";
    public static final String COL_5 = "RELEADEDDATE";
    public static final String COL_6 = "ID";

    public Favourite(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (TITLE TEXT, POSTER TEXT, PLOTSYNOPSIS TEXT, RATING TEXT, RELEADEDDATE TEXT, ID TEXT PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String title, String poster, String plotS, String rating, String relDate, String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, title);
        contentValues.put(COL_2, poster);
        contentValues.put(COL_3, plotS);
        contentValues.put(COL_4, rating);
        contentValues.put(COL_5, relDate);
        contentValues.put(COL_6, id);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllFavourites() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Integer removeFav(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

}
