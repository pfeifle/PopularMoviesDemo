//MP-OK
package com.example.popularmoviesdemo;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Internet {

    private static String IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static String SIZE_PARAM = "w185";
    private static String LOG_TAG = Internet.class.getSimpleName();

    private static URL makeUrl(String stringUrl) throws MalformedURLException {
        URL url = new URL(stringUrl);
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        if (url == null) {
            return null;
        }
        String jsonReponse = null;
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setReadTimeout(20000);
        urlConnection.setConnectTimeout(20000);
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        if (urlConnection.getResponseCode() == 200) {
            inputStream = urlConnection.getInputStream();
            jsonReponse = readFromStream(inputStream);
        }
        if (inputStream != null) {
            inputStream.close();
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        return jsonReponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL buildUrl(String posterUrl) throws MalformedURLException {
        Uri builtUri = Uri.parse(IMAGE_URL).buildUpon().appendEncodedPath(SIZE_PARAM).appendEncodedPath(posterUrl).build();
        URL url = null;
        url = new URL(builtUri.toString());
        return url;
    }

    private static List<Movie> extractMovieData(String movieJson)  {
        if (TextUtils.isEmpty(movieJson)) {
            return null;
        }
        List<Movie> movies = new ArrayList<>();

        String s_Poster;
        String s_Description;
        String s_Title;
        String s_ReleaseDate;
        double d_VotingAverage;

        JSONObject root = null;
        try {
            root = new JSONObject(movieJson);
            JSONArray resultArray = root.getJSONArray("results");

            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject resultObject = resultArray.getJSONObject(i);
                s_Poster = resultObject.getString("poster_path");
                s_Description = resultObject.getString("overview");
                d_VotingAverage = resultObject.getDouble("vote_average");
                s_ReleaseDate = resultObject.getString("release_date");
                s_Title = resultObject.getString("original_title");
                s_Poster = buildUrl(s_Poster).toString();
                Movie movie = new Movie(s_Title, s_Poster, s_ReleaseDate, d_VotingAverage, s_Description);
                movies.add(movie);
            }
        } catch (JSONException | MalformedURLException e) {
            Log.e(LOG_TAG, "Throw an Exception in extractMovieData", e);
        }
        return movies;
    }

    public static List<Movie> fetchMovieAppData(String requestUrl) {
        String jsonResponse = null;
        try {
            URL url = makeUrl(requestUrl);
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Throw an Exception in fetchMovieAppData", e);
        }
        return extractMovieData(jsonResponse);
    }
}
