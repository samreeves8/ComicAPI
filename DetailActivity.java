package com.example.comicapi;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    WebView webView;
    TextView textViewName;
    TextView textViewNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ComicSQLiteHelper comicSQLiteHelper = new ComicSQLiteHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = comicSQLiteHelper.getReadableDatabase();

        webView = findViewById(R.id.webViewDetail);
        textViewName = findViewById(R.id.textViewName);
        textViewNum = findViewById(R.id.textViewNum);

        int pos = getIntent().getIntExtra("position", 0);

        Cursor cursor = sqLiteDatabase.query("comics", new String[] {"name", "number", "URL"}, null, null, null, null, null);
        if (cursor.moveToPosition(pos)){
            webView.loadUrl(cursor.getString(2));
            textViewNum.setText(cursor.getString(0));
            textViewName.setText(cursor.getString(1));
        }
    }
}