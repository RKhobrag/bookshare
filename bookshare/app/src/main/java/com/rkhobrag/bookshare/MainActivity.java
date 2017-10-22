package com.rkhobrag.bookshare;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rkhobrag.bookshare.contracts.BookContract;
import com.rkhobrag.bookshare.db.BookshareDbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
//        insertData();
//        openBookListPage();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)findViewById(R.id.widget_search);
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()) );
        searchView.onActionViewExpanded();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.appbar, menu);
////        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
////        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
////        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()) );
//
//        return true;
//    }

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
