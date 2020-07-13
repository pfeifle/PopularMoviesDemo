package com.example.popularmoviesdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import static android.provider.BaseColumns._ID;

    public class SQLiteDB4MoviesContentProvider extends ContentProvider {
        private static final int MOVIE = 200;
        private static final int MOVIE_ID = 201;
        private static final UriMatcher sUriMatcher = buildUriMatcher();
        private Helper mOpenHelper;



        @Override
        public boolean onCreate() {
            mOpenHelper = new Helper(getContext());
            return true;
        }

        public static UriMatcher buildUriMatcher() {
            String content = Contract.CONTENT_AUTHORITY;

            // All paths to the UriMatcher have a corresponding code to return
            // when a match is found (the ints above).
            UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
            matcher.addURI(content, Contract.PATH_MOVIE, MOVIE);
            matcher.addURI(content, Contract.PATH_MOVIE + "/#", MOVIE_ID);
            return matcher;
        }

        @Override
        public String getType(Uri uri) {
            switch (sUriMatcher.match(uri)) {
                case MOVIE:
                    return Contract.MovieEntry.CONTENT_TYPE;
                case MOVIE_ID:
                    return Contract.MovieEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            Cursor retCursor;
            switch (sUriMatcher.match(uri)) {
                case MOVIE:
                    retCursor = db.query(
                            Contract.MovieEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder
                    );
                    break;
                case MOVIE_ID:
                    long _id = ContentUris.parseId(uri);
                    retCursor = db.query(
                            Contract.MovieEntry.TABLE_NAME,
                            projection,
                            Contract.MovieEntry._ID + " = ?",
                            new String[]{String.valueOf(_id)},
                            null,
                            null,
                            sortOrder
                    );
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
            return retCursor;
        }

        @Override
        public Uri insert(Uri uri, ContentValues values) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            long _id;
            Uri returnUri;
            switch (sUriMatcher.match(uri)) {
                case MOVIE:
                    _id = db.insert(Contract.MovieEntry.TABLE_NAME, null, values);
                    if (_id > 0) {
                        returnUri = Contract.MovieEntry.buildMovieUri(_id);
                    } else {
                        throw new UnsupportedOperationException("Provider Unable to insert rows into: " + uri);
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            // Use this on the URI passed into the function to notify any observers that the uri has
            // changed.
            getContext().getContentResolver().notifyChange(uri, null);
            return returnUri;
        }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            int rows; // Number of rows effected

            switch (sUriMatcher.match(uri)) {

                case MOVIE:
                    rows = db.delete(Contract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            // Because null could delete all rows:
            if (selection == null || rows != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }

            return rows;
        }

        @Override
        public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            int rows;
            switch (sUriMatcher.match(uri)) {
                case MOVIE:
                    rows = db.update(Contract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            if (rows != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }

            return rows;
        }

    }

class Contract {
    public static final String CONTENT_AUTHORITY = "com.example.popularmoviesdemo";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";


    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_MOVIE;
        public static final String TABLE_NAME = "movieTable";
        public static final String COLUMN_NAME = "movieName";
        public static final String COLUMN_MOVIEID = "movieId";
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}

    class Helper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "movieList.db";


        public Helper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + Contract.MovieEntry.TABLE_NAME + "("
                    + _ID + " INTEGER PRIMARY KEY," + Contract.MovieEntry.COLUMN_NAME + " TEXT,"
                    + Contract.MovieEntry.COLUMN_MOVIEID + " TEXT" + ")";
            db.execSQL(CREATE_CONTACTS_TABLE);    }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    }


