// TODO: refactor and harmonize with MovieLoader
package com.example.popularmoviesdemo;

import android.os.AsyncTask;

import java.util.List;

public class FavouriteLoader extends AsyncTask<String, Void, List<Movie>> {

    MovieAdapter mMovieAdapter;

    FavouriteLoader(MovieAdapter adapter) {
        super();
        mMovieAdapter = adapter;
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        if (strings.length < 1 || strings[0] == null) {
            return null;
        }
        return Internet.fetchFavouriteMovieData(strings[0]);
    }
    @Override
    protected void onPostExecute(List<Movie> movieApps) {
        mMovieAdapter.clear();
        if (movieApps != null && !movieApps.isEmpty()) {
            mMovieAdapter.addAll(movieApps);
        }
    }
}
