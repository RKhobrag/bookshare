package com.rkhobrag.bookshare;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;
import static java.security.AccessController.getContext;

public class BooksListActivity extends AppCompatActivity {

    private ArrayList<BookModel> data = new ArrayList<>();
    ArrayAdapter bookAdapter;
    private String q="Fiction";
    FrameLayout progressBarHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            q=query;
        }

        ListView booklistView = (ListView) findViewById(R.id.book_list);

        bookAdapter = new BookAdapter(data, getBaseContext());
        booklistView.setAdapter(bookAdapter);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        getData();

        booklistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookModel dataModel = data.get(position);;
                openBookDetailsPage(dataModel);
            }
        });
    }
    private void openBookDetailsPage(BookModel dataModel) {
        Intent intent = new Intent(getApplicationContext(), BookDetailsActivity.class);
        intent.putExtra("bookId", dataModel.getId());
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()) );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
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
        String url ="https://www.googleapis.com/books/v1/volumes?q="+ Uri.encode(q)+"&maxResults=40";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                    int totalItems = response.getInt("totalItems");
                    for(int i=1;i<items.length();i++)
                    {
                        String title="",author="",genre="",img="",id="",epub="",webReader="";
                        int rating=0;
                        try {
                            title = items.getJSONObject(i).getJSONObject("volumeInfo").getString("title");
                            id = items.getJSONObject(i).getString("id");
                            author = items.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("authors").getString(0);
                            rating = items.getJSONObject(i).getJSONObject("volumeInfo").getInt("averageRating");
                            genre = items.getJSONObject(i).getJSONObject("volumeInfo").getJSONArray("categories").getString(0);
                            img = items.getJSONObject(i).getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail");
                            webReader = items.getJSONObject(i).getJSONObject("accessInfo").getString("webReaderLink");
                            data.add(new BookModel(id, title, author, rating, genre, img, epub, webReader));
                            System.out.print(title);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            data.add(new BookModel(id, title, author, rating, genre, img, epub, webReader));
                        }

                    }
                    bookAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBarHolder.setVisibility(View.INVISIBLE);
                }
                progressBarHolder.setVisibility(View.INVISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
        progressBarHolder.setVisibility(View.VISIBLE);
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
            data.add(new BookModel("",cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_TITLE)),
                    cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_Author)),
                    cursor.getInt(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_Rating)),
                    cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_NAME_Genre)),
                    "","",""
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
