package com.rkhobrag.bookshare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rkhobrag.bookshare.R;

import java.util.ArrayList;

/**
 * Created by rakesh on 19/10/17.
 */

public class BookAdapter extends ArrayAdapter implements View.OnClickListener{
    private ArrayList<BookModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtTitle;
        TextView txtAuthor;
        TextView txtGenre;
        ImageView img;
    }

    public BookAdapter(ArrayList<BookModel> data, Context context) {
        super(context, R.layout.book_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        BookModel dataModel=(BookModel)object;

        switch (v.getId())
        {
            case R.id.img:

                break;
        }
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
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);

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
        viewHolder.img.setOnClickListener(this);
        viewHolder.img.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}

