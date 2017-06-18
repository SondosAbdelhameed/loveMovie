package com.example.heroshe.lovemovies;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by herohe on 22/04/16.
 */
public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.ViewHolder> {
    private static final String TAG = GridImageAdapter.class.getSimpleName();
    private Context context;
    private List<Movie> movies;

    public GridImageAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_grid, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Movie movie = movies.get(position);
        final Bundle args =new Bundle();

        String url = "http://image.tmdb.org/t/p/w342/" + movie.poster_path;

        Picasso.with(context).load(url).into(holder.imageGrid);
        holder.imageGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent= new Intent(v.getContext(), MovieDetailActivity.class);

                args .putString("title", movie.title);
                args .putString("date",movie.date);
                args .putString("poster_path",movie.poster_path);
                args.putString("vote_average",movie.vote_average);
                args.putString("id",movie.id);
                args.putString("overview", movie.overview);
                args.putBoolean("favorite", checkFavorite(movie.id));

                if(MainActivity.tablet) {
                    //Toast.makeText(v.getContext(),args.toString(),Toast.LENGTH_LONG).show();
                    MovieDetailActivityFragment detialMovieFragment=new MovieDetailActivityFragment();

                    FragmentTransaction ft = ((Activity) context).getFragmentManager().beginTransaction();
                    ft.replace(R.id.tablet_frame,detialMovieFragment);
                    detialMovieFragment.setArguments(args);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else {
                    intent.putExtras(args);
                    v.getContext().startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageGrid;

        public ViewHolder(View itemView) {
            super(itemView);
            imageGrid = (ImageView) itemView.findViewById(R.id.imageGrid);
        }
    }

    public Boolean checkFavorite(String ID){
        DB_Helper DB = new DB_Helper(context);
        SQLiteDatabase sqLiteDatabase = DB.getWritableDatabase();
        Boolean check=DB.search(ID,sqLiteDatabase);
        sqLiteDatabase.close();
        DB.close();
        return check;
    }

    public void clear() {
        if (movies != null) {
            int size = movies.size();
            movies.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

}