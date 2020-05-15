//MP-OK
package com.example.popularmoviesdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //get Data from Main Activity via Intent
        Intent intent = getIntent();

        String title = intent.getStringExtra("Title");
        TextView tv_title = findViewById(R.id.Title);
        tv_title.setText(title);

        String poster = intent.getStringExtra("PosterUrl");
        ImageView iv_poster = findViewById(R.id.Poster);
        Picasso.with(getApplicationContext()).load(poster).into(iv_poster);

        String releaseDate = intent.getStringExtra("ReleaseDate");
        TextView tv_releaseDate = findViewById(R.id.ReleaseDate);
        tv_releaseDate.setText(releaseDate);

        String decription = intent.getStringExtra("Description");
        TextView tv_description = findViewById(R.id.Description);
        tv_description.setText(decription);

        double voteAverage = intent.getDoubleExtra("VotingAverage", 0.0);
        TextView tv_voteTextView = findViewById(R.id.VotingAverage);
        tv_voteTextView.setText(String.valueOf(voteAverage));
    }
}
