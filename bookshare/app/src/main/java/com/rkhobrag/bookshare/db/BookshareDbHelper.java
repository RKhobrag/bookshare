package com.rkhobrag.bookshare.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rkhobrag.bookshare.contracts.BookContract;

/**
 * Created by rakesh on 20/10/17.
 */

public class BookshareDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BookShare.db";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BookContract.BookEntry.TABLE_NAME + " (" +
                    BookContract.BookEntry._ID + " INTEGER PRIMARY KEY," +
                    BookContract.BookEntry.COLUMN_NAME_TITLE + " TEXT," +
                    BookContract.BookEntry.COLUMN_NAME_Genre + " TEXT," +
                    BookContract.BookEntry.COLUMN_NAME_Rating + " INTEGER," +
                    BookContract.BookEntry.COLUMN_NAME_Author + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BookContract.BookEntry.TABLE_NAME;

    public BookshareDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
