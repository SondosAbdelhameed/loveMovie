package com.example.heroshe.lovemovies;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
public class MovieDetailActivityFragment extends Fragment  {

    private static final String TAG = MovieDetailActivityFragment.class.getSimpleName();
    LinearLayout videoList,reviewList;
    GridReviewAdapter reviewAdapter;
    List<MovieTrailer> movieTrailers=new ArrayList<>();
    List<MovieReview> movieReviews=new ArrayList<>();
    private GridVideoAdapter trailerAdapter;

    private String title,overview,poster_path,poster,date,vote_average,id;
    private Boolean favo;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        TextView titleTv = (TextView) rootView.findViewById(R.id.title);
        TextView overviewTv = (TextView) rootView.findViewById(R.id.overView);
        TextView dateTv = (TextView) rootView.findViewById(R.id.date);
        TextView vote_averageTv = (TextView) rootView.findViewById(R.id.vote_average);
        ImageView imageView=(ImageView) rootView.findViewById(R.id.poster);
        final CheckBox Star=(CheckBox) rootView.findViewById(R.id.favo);
        videoList = (LinearLayout) rootView.findViewById(R.id.trailersList);
        reviewList = (LinearLayout) rootView.findViewById(R.id.reviewsList);

        ////////////////////favorite button/////////////////////
        Star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DB_Helper DB = new DB_Helper(getActivity());
                SQLiteDatabase sqLiteDatabase = DB.getWritableDatabase();
                Boolean check=DB.search(id,sqLiteDatabase);

                if(!check) {
                    DB.insertfavouriteMovie(title, poster, overview, date, vote_average, id, sqLiteDatabase);
                    Star.setChecked(true);
                    Toast.makeText(getActivity(), "Movie Saved", Toast.LENGTH_LONG).show();
                }
                else {
                    DB.delete(id,sqLiteDatabase);
                    Star.setChecked(false);
                    Toast.makeText(getActivity(), "Movie Delete", Toast.LENGTH_LONG).show();
                }
                sqLiteDatabase.close();
                DB.close();

            }
        });

        String url = "http://image.tmdb.org/t/p/w342/";

        if (MainActivity.tablet) {
            if (getArguments() != null) {
                title = getArguments().getString("title");
                titleTv.setText(title);
                overview = getArguments().getString("overview");
                overviewTv.setText(overview);
                date = getArguments().getString("date");
                dateTv.setText(date);
                vote_average = getArguments().getString("vote_average");
                vote_averageTv.setText(vote_average);
                poster_path = url + getArguments().getString("poster_path");
                id = getArguments().getString("id");
                Picasso.with(getActivity()).load(poster_path).into(imageView);
                favo = getArguments().getBoolean("favorite");
                Star.setChecked(favo);
            }
        }
        else {
            if (getActivity().getIntent() != null) {
                final Intent data = getActivity().getIntent();
                title = data.getStringExtra("title");
                titleTv.setText(title);
                overview = data.getStringExtra("overview");
                overviewTv.setText(overview);
                date = data.getStringExtra("date");
                dateTv.setText(date);
                vote_average = data.getStringExtra("vote_average");
                vote_averageTv.setText(vote_average);
                poster_path = url + data.getStringExtra("poster_path");
                poster = data.getStringExtra("poster_path");
                id = data.getStringExtra("id");
                Picasso.with(getActivity()).load(poster_path).into(imageView);
                favo = data.getExtras().getBoolean("favorite");
                Star.setChecked(favo);


            }
        }

        new FetchTrailer().execute();
        new FetchReview().execute();

        return rootView;
    }

    public class FetchTrailer extends AsyncTask<Void, String, List<MovieTrailer>> {

        @Override
        protected List<MovieTrailer> doInBackground(Void... params) {

            try {
                String response1 = getJSON("http://api.themoviedb.org/3/movie/"+id+"/videos?api_key=d0ed6826070b1573dab5601f88f4c258");
                JSONObject jsonObject1 = new JSONObject(response1);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("results");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonmovies1 = jsonArray1.getJSONObject(j);
                    String videoKey = jsonmovies1.getString("key");
                    String videoName = jsonmovies1.getString("name");

                    MovieTrailer movieTrailer=new MovieTrailer(videoKey,videoName);

                    movieTrailers.add(movieTrailer);

                }

            } catch (JSONException e1) {
                e1.printStackTrace();
                return movieTrailers;
            }
            return movieTrailers;
        }
        @Override
        protected void onPostExecute(List<MovieTrailer> videos) {
            trailerAdapter = new GridVideoAdapter(getActivity(), videos);
            for (int i = 0; i < trailerAdapter.getCount(); i++) {
                View v = trailerAdapter.getView(i, null, null);
                videoList.addView(v);
            }

            trailerAdapter.notifyDataSetChanged();
        }
    }

    public class FetchReview extends AsyncTask<Void, String, List<MovieReview>> {

        @Override
        protected List<MovieReview> doInBackground(Void... params) {

            try {
                String response2 = getJSON("http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=d0ed6826070b1573dab5601f88f4c258");
                JSONObject jsonObject2 = new JSONObject(response2);
                JSONArray jsonArray2 = jsonObject2.getJSONArray("results");
                for (int j = 0; j < jsonArray2.length(); j++) {
                    JSONObject jsonmovies2 = jsonArray2.getJSONObject(j);
                    String auther = jsonmovies2.getString("author");
                    String content = jsonmovies2.getString("content");

                    MovieReview movieReview=new MovieReview(auther,content);
                    movieReviews.add(movieReview);
                }
            }
            catch (JSONException e2) {
                e2.printStackTrace();

                return null;
            }

            return movieReviews;
        }

        @Override
        protected void onPostExecute(List<MovieReview> reviews) {
            reviewAdapter = new GridReviewAdapter(getActivity(), reviews);
            for (int i = 0; i < reviewAdapter.getCount(); i++) {
                View v = reviewAdapter.getView(i, null, null);
                reviewList.addView(v);
            }
            reviewAdapter.notifyDataSetChanged();
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

}