package com.example.comicapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.URL;

public class ComicSQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "comics.sqlite";
    private static final int DB_VERSION = 1;

    public ComicSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    void insert(String name, int number, String url){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("number", number);
        contentValues.put("URL", String.valueOf(url));
        sqLiteDatabase.insert("comics", null, contentValues);
    }

    int getCount(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM comics", null);
        return cursor.getCount();
    }

    public MyComic getComic(int position) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("comics", new String[]{"name", "number", "URL"},
                null, null, null, null, null);
        if(cursor.moveToPosition(position)){
            MyComic myComic = new MyComic(cursor.getString(0), cursor.getInt(1), cursor.getString(2));
            return myComic;
        }
        return null;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE comics (_id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, number INTEGER, URL STRING);";
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
