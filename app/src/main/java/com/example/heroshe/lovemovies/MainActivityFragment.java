package com.example.heroshe.lovemovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment  {

    private static final String TAG = FetchMovie.class.getSimpleName();
    RecyclerView movieList;
    List<Movie> movies = new ArrayList<>();
    private ProgressBar bar;
    String popUrl, rateUrl;
    private GridImageAdapter adapter;
    private MenuItem pop, rate, fav;
    SharedPreferences prefs;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main, container, false);

        movieList = (RecyclerView) rootView.findViewById(R.id.movieslist);
        movieList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        bar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        prefs = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);


        popUrl="http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=d0ed6826070b1573dab5601f88f4c258";
        rateUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=d0ed6826070b1573dab5601f88f4c258";

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);


        pop = menu.findItem(R.id.action_settings_popularity);
        rate = menu.findItem(R.id.action_settings_rating);
        fav = menu.findItem(R.id.action_settings_favorite);


        if (prefs != null) {
            int state = prefs.getInt("state", 1);
            if (state != -1) {
                if (state == 1) {
                    pop.setChecked(true);
                    new FetchMovie().execute(popUrl);
                } else if (state == 2) {
                    rate.setChecked(true);
                    new FetchMovie().execute(rateUrl);
                } else if (state == 3) {
                    fav.setChecked(true);
                    new FetchFavorite().execute();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // item.setChecked(true);
        if (Utils.isNetworkAvailable(getActivity())) {
            switch (id) {
                //noinspection SimplifiableIfStatement
                case R.id.action_settings_popularity:

                    pop.setChecked(true);
                    Toast.makeText(getActivity(), "Pop!", Toast.LENGTH_SHORT).show();
                    if (prefs != null) {
                        prefs.edit().putInt("state", 1).apply();
                    }
                    new FetchMovie().execute(popUrl);
                    if (adapter != null) {
                        adapter.clear();
                        new FetchMovie().execute(popUrl);
                        adapter.notifyDataSetChanged();
                    }
                    break;

                case R.id.action_settings_rating:
                    rate.setChecked(true);
                    Toast.makeText(getActivity(), "Rate!", Toast.LENGTH_SHORT).show();
                    if (prefs != null) {
                        prefs.edit().putInt("state", 2).apply();
                    }
                    new FetchMovie().execute(rateUrl);
                    if (adapter != null) {
                        adapter.clear();
                        new FetchMovie().execute(rateUrl);
                        adapter.notifyDataSetChanged();
                    }
                    break;

                case R.id.action_settings_favorite:
                    fav.setChecked(true);
                    Toast.makeText(getActivity(), "Fav!", Toast.LENGTH_SHORT).show();
                    if (prefs != null) {
                        prefs.edit().putInt("state", 3).apply();
                    }
                    if (adapter != null) {
                        adapter.clear();
                        new FetchFavorite().execute();
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }else {
            Toast.makeText(getActivity(), "Please check you network!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchMovie extends AsyncTask<String, String, List<Movie>> {

        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            try {
                String response = getJSON(params[0]);
                if (response != null) {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonmovies = jsonArray.getJSONObject(i);
                        String id = jsonmovies.getString("id");
                        String title = jsonmovies.getString("original_title");
                        String overview = jsonmovies.getString("overview");
                        String date = jsonmovies.getString("release_date");
                        String poster = jsonmovies.getString("poster_path");
                        String vote_average = jsonmovies.getString("vote_average");

                        Movie movie = new Movie(id, title, overview, date, poster, vote_average);
                        movies.add(movie);}}
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            adapter = new GridImageAdapter(getActivity(), movies);
            bar.setVisibility(View.GONE);
            movieList.setVisibility(View.VISIBLE);
            movieList.setAdapter(adapter);
        }
    }

    public static String getJSON(String url) {
        HttpURLConnection conn = null;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-length", "0");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.connect();
            int status = conn.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return null;
    }

    public class FetchFavorite extends AsyncTask<Void, String, List<Movie>> {

        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {
            try {
                DB_Helper DB = new DB_Helper(getActivity());
                SQLiteDatabase sqLiteDatabase = DB.getWritableDatabase();
                Cursor cursor=null;
                cursor=DB.getfavouriteMovie(sqLiteDatabase);
                if (cursor.moveToFirst()){
                    do{

                        String id = cursor.getString(cursor.getColumnIndex("movie_id"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String overview = cursor.getString(cursor.getColumnIndex("Overview"));
                        String date = cursor.getString(cursor.getColumnIndex("date"));
                        String poster = cursor.getString(cursor.getColumnIndex("movie_image"));
                        String vote_average = cursor.getString(cursor.getColumnIndex("rating"));
                        Movie movie = new Movie(id, title, overview, date, poster, vote_average);
                        movies.add(movie);
                    }while(cursor.moveToNext()); }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return movies; }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            adapter = new GridImageAdapter(getActivity(), movies);
            bar.setVisibility(View.GONE);
            movieList.setVisibility(View.VISIBLE);
            movieList.setAdapter(adapter);
        }
    }

}