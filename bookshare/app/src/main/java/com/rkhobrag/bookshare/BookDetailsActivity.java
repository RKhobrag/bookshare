package com.rkhobrag.bookshare;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.rkhobrag.bookshare.adapters.BitmapCache;
import com.rkhobrag.bookshare.adapters.BookModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookDetailsActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        String bookId = getIntent().getStringExtra("bookId");
         
        getBookDetails(bookId);

    }

    private void getBookDetails(String bookId) {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://www.googleapis.com/books/v1/volumes/"+String.valueOf(bookId);
        final RelativeLayout detailsLayout = (RelativeLayout) findViewById(R.id.details);

        // Request a string response from the provided URL.
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                        String title="",author="",genre="",img="",description="",webReader="";
                        int rating=0,id=0;
                        try {
                            title = response.getJSONObject("volumeInfo").getString("title");
                            author = response.getJSONObject("volumeInfo").getJSONArray("authors").getString(0);
                            rating = response.getJSONObject("volumeInfo").getInt("averageRating");
                            img = response.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("smallThumbnail");
                            description = response.getJSONObject("volumeInfo").getString("description");
                            genre = response.getJSONObject("volumeInfo").getJSONArray("categories").getString(0);
                            webReader = response.getJSONObject("accessInfo").getString("webReaderLink");
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        NetworkImageView imgView = (NetworkImageView) findViewById(R.id.thumbnail);
                        TextView txtTitle = (TextView) findViewById(R.id.title);
                        TextView txtRating = (TextView) findViewById(R.id.rating);
                        TextView txtAuthor = (TextView) findViewById(R.id.author);
                        TextView txtGenre = (TextView) findViewById(R.id.genre);
                        TextView txtDescription = (TextView) findViewById(R.id.description);
                        TextView weblink = (TextView) findViewById(R.id.web_link);
                        txtAuthor.setText(author);
                        txtTitle.setText(title);
                        txtRating.setText(String.valueOf(rating));
                        txtGenre.setText(genre);
                        txtDescription.setText(Html.fromHtml(description));
                        imgView.setImageUrl(img, getImageLoader());
                        final String finalWebReader = webReader;
                        if(!finalWebReader.isEmpty()) {
                            weblink.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalWebReader));
                                    startActivity(browserIntent);
                                }
                            });
                        }else{
                            weblink.setText("This book is not available for reading");
                        }
                detailsLayout.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
        detailsLayout.setVisibility(View.INVISIBLE);
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new BitmapCache(cacheSize));
        }
        return this.mImageLoader;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
}
