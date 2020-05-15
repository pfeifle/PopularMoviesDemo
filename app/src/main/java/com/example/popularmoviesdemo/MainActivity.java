//MP-OK
package com.example.popularmoviesdemo;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private String m_popularMovie =
           "http://api.themoviedb.org/3/movie/popular?api_key=";
   private String m_topRatedMovie =
           "https://api.themoviedb.org/3/movie/top_rated?api_key=";

   private MovieAdapter mMovieAdapter;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       m_popularMovie = m_popularMovie + getString(R.string.apikey);
       m_topRatedMovie = m_topRatedMovie + getString(R.string.apikey);

       setContentView(R.layout.activity_main);
       GridView gv_MovieList = findViewById(R.id.gridview);

       mMovieAdapter = new MovieAdapter(this, new ArrayList<Movie>());
       gv_MovieList.setAdapter(mMovieAdapter);

       gv_MovieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent = new Intent(MainActivity.this, DetailActivity.class);

               String s_Title = mMovieAdapter.getItem(i).getS_Title();
               intent.putExtra("Title", s_Title);

               String s_Decription = mMovieAdapter.getItem(i).getS_Descrption();
               intent.putExtra("Description", s_Decription);

               String s_PosterUrl = mMovieAdapter.getItem(i).getS_PosterUrl();
               intent.putExtra("PosterUrl", s_PosterUrl);

               String s_ReleaseDate = mMovieAdapter.getItem(i).getS_ReleaseData();
               intent.putExtra("ReleaseDate", s_ReleaseDate);

               double s_VotingAverage = mMovieAdapter.getItem(i).getVotingAverage();
               intent.putExtra("VotingAverage", s_VotingAverage);

               startActivity(intent);
           }
       });
       MovieLoader task = new MovieLoader(mMovieAdapter);
       task.execute(m_popularMovie);
   }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.rating) {
            new MovieLoader(mMovieAdapter).execute(m_topRatedMovie);
        }
        if (id == R.id.popularity) {
            new MovieLoader(mMovieAdapter).execute(m_popularMovie);
        }
        return super.onOptionsItemSelected(item);
    }

}
