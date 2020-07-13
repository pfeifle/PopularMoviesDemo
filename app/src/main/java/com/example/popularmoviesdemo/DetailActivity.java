package com.example.popularmoviesdemo;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    public Intent m_intent;
    public static String m_key=null;
    public static TextView tv_review;
    public static String s_review;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //get Data from Main Activity via Intent
        m_intent = getIntent();

        String title = m_intent.getStringExtra("Title");
        TextView tv_title = findViewById(R.id.Title);
        tv_title.setText(title);

        String poster = m_intent.getStringExtra("PosterUrl");
        ImageView iv_poster = findViewById(R.id.Poster);
        Picasso.with(getApplicationContext()).load(poster).into(iv_poster);

        String releaseDate = m_intent.getStringExtra("ReleaseDate");
        TextView tv_releaseDate = findViewById(R.id.ReleaseDate);
        tv_releaseDate.setText(releaseDate);

        String decription = m_intent.getStringExtra("Description");
        TextView tv_description = findViewById(R.id.Description);
        tv_description.setText(decription);

        double voteAverage = m_intent.getDoubleExtra("VotingAverage", 0.0);
        TextView tv_voteTextView = findViewById(R.id.VotingAverage);
        tv_voteTextView.setText(String.valueOf(voteAverage));


        // Trailer
        TrailerAsyncTask trailerTask=new TrailerAsyncTask();
        trailerTask.execute(getString(R.string.MovieRequest)+ m_intent.getStringExtra("ID")  +"/videos?"+getString(R.string.apikey));
        Button button= (Button) findViewById(R.id.PlayTrailer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.VideoRequest)+m_key));
                startActivity(i);
            }
        });

        // Review
        tv_review=(TextView)findViewById(R.id.Review);
        ReviewAsyncTask reviewTask=new ReviewAsyncTask();
        reviewTask.execute(getString(R.string.MovieRequest)+ m_intent.getStringExtra("ID")  +"/reviews?"+getString(R.string.apikey));
    }

    public void onClickAddFavourite(View view) {
        ContentValues values=new ContentValues();
        values.put(Contract.MovieEntry.COLUMN_NAME,m_intent.getStringExtra("Title"));
        values.put(Contract.MovieEntry.COLUMN_MOVIEID,m_intent.getStringExtra("ID"));
        getContentResolver().insert(Contract.MovieEntry.CONTENT_URI,values);
        Toast.makeText(this, m_intent.getStringExtra("Title") + " was added to your favourite movies!", Toast.LENGTH_LONG).show();
    }

    public void onClickDeleteFavourite(View view) {
        this.getContentResolver().delete(
                Contract.MovieEntry.CONTENT_URI,
                Contract.MovieEntry.COLUMN_MOVIEID+"="+m_intent.getStringExtra("ID"),
                null
        );
        Toast.makeText(this, m_intent.getStringExtra("Title") + " was deleted from your favourite movies!", Toast.LENGTH_LONG).show();
    }




    /************************** TRAILER *************************************/

    private class TrailerAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }
            try {
                fetchTrailerData(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return m_key;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public void fetchTrailerData(String requestUrl) throws MalformedURLException {
        URL url = Internet.makeUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = Internet.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("", "Error in fetchTrailerData", e);
        }
        extractTrailerData(jsonResponse);
    }


    private void extractTrailerData(String movieJson) {
        if (TextUtils.isEmpty(movieJson)) {
            return;
        }
        m_key =null;
        try {
            JSONObject root = new JSONObject(movieJson);
            JSONArray resultArray = root.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject resultObject = resultArray.getJSONObject(i);
                m_key=resultObject.getString("key");
            }
        } catch (JSONException e) {
            Log.e("", "Error in ExtractTrailerData", e);
        }
    }


    /************************** Review *************************************/
    private class ReviewAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length < 1 || strings[0] == null) {
                return null;
            }
            try {
                fetchReviewData(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            tv_review.setText(s_review);
        }
    }

    public static void fetchReviewData(String requestUrl) throws MalformedURLException {
        URL url = Internet.makeUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = Internet.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("", "Error in fetchReviewData", e);
        }
        extractReviewData(jsonResponse);
    }

    private static void extractReviewData(String movieJson) {
        if (TextUtils.isEmpty(movieJson)) {
            return;
        }
        s_review=null;
        try {
            JSONObject root = new JSONObject(movieJson);
            JSONArray resultArray = root.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject resultObject = resultArray.getJSONObject(i);
                s_review=resultObject.getString("content");
            }
        } catch (JSONException e) {
            Log.e("", "Error in extractReviewData", e);
        }
    }

}
