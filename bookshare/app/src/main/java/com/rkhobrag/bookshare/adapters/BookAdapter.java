package com.rkhobrag.bookshare.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.rkhobrag.bookshare.BookDetailsActivity;
import com.rkhobrag.bookshare.R;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by rakesh on 19/10/17.
 */

public class BookAdapter extends ArrayAdapter implements View.OnClickListener{
    private ArrayList<BookModel> dataSet;
    Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    // View lookup cache
    private static class ViewHolder {
        TextView txtTitle;
        TextView txtAuthor;
        TextView txtGenre;
        TextView txtRating;
        NetworkImageView img;
    }

    public BookAdapter(ArrayList<BookModel> data, Context context) {
        super(context, R.layout.book_item, data);
        this.dataSet = data;
        this.mContext=context;
        mRequestQueue = Volley.newRequestQueue(getContext());
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        BookModel dataModel=(BookModel)object;
        openBookDetailsPage(dataModel);
        switch (v.getId())
        {
            case R.id.thumbnail:

                break;
        }
    }

    private void openBookDetailsPage(BookModel dataModel) {
        Intent intent = new Intent(getContext(), BookDetailsActivity.class);
        intent.putExtra("bookId", dataModel.getId());
        startActivity(getContext(), intent, null);
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BookModel bookModel = (BookModel) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.book_item, parent, false);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.txtAuthor = (TextView) convertView.findViewById(R.id.author);
            viewHolder.txtGenre = (TextView) convertView.findViewById(R.id.genre);
            viewHolder.txtRating = (TextView) convertView.findViewById(R.id.rating);
            viewHolder.img = (NetworkImageView) convertView.findViewById(R.id.thumbnail);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtTitle.setText(bookModel.getTitle());
        viewHolder.txtAuthor.setText(bookModel.getAuthor());
        viewHolder.txtGenre.setText(bookModel.getGenre());
        viewHolder.txtRating.setText(String.valueOf(bookModel.getRating()));
        viewHolder.img.setOnClickListener(this);
        viewHolder.img.setImageUrl(bookModel.getImgUrl(), getImageLoader());
        // Return the completed view to render on screen
        return convertView;
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
            mRequestQueue = Volley.newRequestQueue(getContext());
        }

        return mRequestQueue;
    }
}

