package com.example.popularmoviesdemo;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
   private String m_popularMovie;
   private String m_topRatedMovie;
   private MovieAdapter mMovieAdapter;
   private SimpleCursorAdapter mFavouriteAdapter;
   private GridView gv_MovieList;
   public static MainActivity activity ;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       activity= this;

       m_popularMovie = getString(R.string.MovieRequest)+"popular?" + getString(R.string.apikey);
       m_topRatedMovie = getString(R.string.MovieRequest)+"top_rated?" + getString(R.string.apikey);

       setContentView(R.layout.activity_main);
       gv_MovieList = findViewById(R.id.gridview);

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

               String s_ID = mMovieAdapter.getItem(i).getID();
               intent.putExtra("ID", s_ID);

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


    public Boolean CheckFavourite (String sID){
        boolean res;

        String selection = Contract.MovieEntry.COLUMN_MOVIEID + " = ?";
        String[] selectionArgs = { sID };

        Cursor cursor = getContentResolver().query(Contract.MovieEntry.CONTENT_URI, new String[] {Contract.MovieEntry._ID,
                Contract.MovieEntry.COLUMN_NAME}, selection, selectionArgs, null);
        if (cursor.getCount()>0)
            res= true;
        else
            res= false;
        cursor.close();
        return res;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.favourite) {
            if (id == R.id.favourite) {
                Toast.makeText(this, "Your Favourite Movies ", Toast.LENGTH_LONG).show();
                gv_MovieList.setAdapter(mMovieAdapter);
                new FavouriteLoader(mMovieAdapter).execute(m_popularMovie);
            }
        }
        if (id == R.id.rating) {
            Toast.makeText(this, "Best Rated Movies ", Toast.LENGTH_LONG).show();
            gv_MovieList.setAdapter(mMovieAdapter);
            new MovieLoader(mMovieAdapter).execute(m_topRatedMovie);
        }
        if (id == R.id.popularity) {
            Toast.makeText(this, "Most Popular Movies ", Toast.LENGTH_LONG).show();
            gv_MovieList.setAdapter(mMovieAdapter);
            new MovieLoader(mMovieAdapter).execute(m_popularMovie);
        }
        return super.onOptionsItemSelected(item);
    }

}
