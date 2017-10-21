package com.rkhobrag.bookshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.rkhobrag.bookshare.adapters.BookModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        int bookId = getIntent().getIntExtra("bookId", -1);
         
        getBookDetails(bookId);
        
    }

    private void getBookDetails(int bookId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://www.googleapis.com/books/v1/volumes/"+String.valueOf(bookId);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                        String title="",author="",genre="",img="",description="";
                        int rating=0,id=0;
                        try {
                            title = response.getJSONObject("volumeInfo").getString("title");
                            author = response.getJSONObject("volumeInfo").getJSONArray("authors").getString(0);
                            rating = response.getJSONObject("volumeInfo").getInt("averageRating");
                            genre = response.getJSONObject("volumeInfo").getJSONArray("categories").getString(0);
                            img = response.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail");
                            description = response.getJSONObject("volumeInfo").getString("description");

                            NetworkImageView imgView = (NetworkImageView) findViewById(R.id.thumbnail);
                            TextView txtTitle = (TextView) findViewById(R.id.title);
                            TextView txtRating = (TextView) findViewById(R.id.rating);
                            TextView txtAuthor = (TextView) findViewById(R.id.author);
                            TextView txtGenre = (TextView) findViewById(R.id.genre);
                            TextView txtDescription = (TextView) findViewById(R.id.description);

                            txtAuthor.setText(author);
                            txtTitle.setText(title);
                            txtRating.setText(String.valueOf(rating));
                            txtGenre.setText(genre);
                            txtDescription.setText(description);
                        }
                        catch (JSONException e)
                        {
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
    }
}
