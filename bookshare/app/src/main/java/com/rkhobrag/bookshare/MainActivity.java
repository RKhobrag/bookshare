package com.rkhobrag.bookshare;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rkhobrag.bookshare.contracts.BookContract;
import com.rkhobrag.bookshare.db.BookshareDbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        insertData();
        openBookListPage();
    }

    private void insertData() {
        BookshareDbHelper dbHelper = new BookshareDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(dbHelper.SQL_DELETE_ENTRIES);
        db.execSQL(dbHelper.SQL_CREATE_ENTRIES);
        for (int i =1 ;i <=5;i++) {
            ContentValues values = new ContentValues();
            values.put(BookContract.BookEntry.COLUMN_NAME_TITLE, "Harry Potter "+i);
            values.put(BookContract.BookEntry.COLUMN_NAME_Author, "J.K.Rowling");
            values.put(BookContract.BookEntry.COLUMN_NAME_Genre, "fiction");
            values.put(BookContract.BookEntry.COLUMN_NAME_Rating, 3);

            // Insert the new row, returning the primary key value of the new row
            db.insert(BookContract.BookEntry.TABLE_NAME, null, values);
        }
    }

    private void openBookListPage()
    {
        Intent intent = new Intent(this, BooksListActivity.class);
        startActivity(intent);
    }
}
