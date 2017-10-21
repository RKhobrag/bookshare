package com.rkhobrag.bookshare;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rkhobrag.bookshare.adapters.BookAdapter;
import com.rkhobrag.bookshare.adapters.BookModel;
import com.rkhobrag.bookshare.contracts.BookContract;
import com.rkhobrag.bookshare.db.BookshareDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BooksListActivity extends AppCompatActivity {

    private ArrayList<BookModel> data = new ArrayList<>();
    ArrayAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ListView booklistView = (ListView) findViewById(R.id.book_list);

        bookAdapter = new BookAdapter(data, getBaseContext());
        booklistView.setAdapter(bookAdapter);
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public ArrayList<BookModel> getData(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://www.googleapis.com/books/v1/volumes?q=algorithm&maxResults=40";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                    int totalItems = response.getInt("totalItems");
                    for(int i=1;i<items.length();i++)
                    {
                        String title="",author="",genre="";
                        int rating=0;
                        try {
                            title = items.getJSONObject(i).getJSONObject("volumeInfo").getString("title");
                            author = items.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("authors").getString(0);
                            rating = items.getJSONObject(i).getJSONObject("volumeInfo").getInt("averageRating");
                            genre = items.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("categories").getString(0);
                            data.add(new BookModel(title, author, rating, genre));
                            System.out.print(title);
                        }
                        catch (JSONException e)
                        {
//                            data.add(new BookModel(title, author, rating, genre));
                            continue;
                        }

                    }
                    bookAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
        return data;
    }

    public ArrayList<BookModel> getDataFromDB() {
        BookshareDbHelper dbHelper = new BookshareDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_NAME_TITLE,
                BookContract.BookEntry.COLUMN_NAME_Author,
                BookContract.BookEntry.COLUMN_NAME_Genre,
                BookContract.BookEntry.COLUMN_NAME_Rating
        };

//        String selection = BookContract.BookEntry.COLUMN_NAME_TITLE + " = ?";
//        String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                BookContract.BookEntry.COLUMN_NAME_TITLE + " DESC";

        Cursor cursor = db.query(
                BookContract.BookEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            itemIds.add(cursor.getLong(cursor.getColumnIndexOrThrow(BookContract.BookEntry._ID)));
            data.add(new BookModel(cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_Author)),
                    cursor.getInt(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_Rating)),
                    cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_Genre))
                    ));
        }
        cursor.close();


//        data.add(new BookModel("Harry Potter", "J.K.Rowaling", 3, "fiction"));
//        data.add(new BookModel("Harry Potter", "J.K.Rowaling", 3, "fiction"));
//        data.add(new BookModel("Harry Potter", "J.K.Rowaling", 3, "fiction"));
//        data.add(new BookModel("Harry Potter", "J.K.Rowaling", 3, "fiction"));
//        data.add(new BookModel("Harry Potter", "J.K.Rowaling", 3, "fiction"));
//        data.add(new BookModel("Harry Potter", "J.K.Rowaling", 3, "fiction"));
//        data.add(new BookModel("Harry Potter", "J.K.Rowaling", 3, "fiction"));
//        data.add(new BookModel("Harry Potter", "J.K.Rowaling", 3, "fiction"));
        return data;
    }
}
