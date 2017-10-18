package com.rkhobrag.bookshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openBookListPage();
    }

    private void openBookListPage()
    {
        Intent intent = new Intent(this, BooksListActivity.class);
        startActivity(intent);
    }
}
