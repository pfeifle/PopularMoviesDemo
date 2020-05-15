//MP-OK
package com.example.popularmoviesdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends ArrayAdapter<Movie> {

    private Context mcontext;
    public MovieAdapter(Context context, ArrayList<Movie> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View v_Movie = convertView;
         if (convertView == null) {
            v_Movie = LayoutInflater.from(getContext()).inflate(R.layout.activity_item, parent, false);
        }
        Movie movieApp = getItem(position);
        ImageView posterImageView = v_Movie.findViewById(R.id.poster_iv);
        Picasso.with(getContext()).load(movieApp.getS_PosterUrl()).into(posterImageView);
        return v_Movie;
    }
}
