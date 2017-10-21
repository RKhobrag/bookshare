package com.rkhobrag.bookshare.contracts;

import android.provider.BaseColumns;

/**
 * Created by rakesh on 20/10/17.
 */

public final class BookContract {
    private BookContract(){}

    public static class BookEntry implements BaseColumns {
        public static final String TABLE_NAME = "book";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_Author = "author";
        public static final String COLUMN_NAME_Rating = "rating";
        public static final String COLUMN_NAME_Genre = "genre";
    }
}
