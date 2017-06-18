package com.example.heroshe.lovemovies;

import android.provider.BaseColumns;

/**
 * Created by fatma adel on 5/1/2016.
 */
public class Movie_Contract {
    public class FavouriteMovie implements BaseColumns {

        public static final String TABLE_NAME = "favourite";
        public static final String id = "ID";
        public static final String MOVIE_ID = "movie_id";
        public static final String NAME = "title";
        public static final String POSTER="movie_image";
        public static final String OVER = "Overview";
        public static final String RATE = "rating";
        public static final String DATE = "date";
    }

    public static final String Create_TB="CREATE TABLE "+FavouriteMovie.TABLE_NAME+
            " ( "+FavouriteMovie.id+" integer PRIMARY KEY AUTOINCREMENT , " +
            FavouriteMovie.NAME+" TEXT NOT NULL , "+
            FavouriteMovie.POSTER+" TEXT NOT NULL , " +
            FavouriteMovie.OVER+" TEXT NOT NULL,  " +
            FavouriteMovie.DATE+" TEXT NOT NULL , " +
            FavouriteMovie.MOVIE_ID+" TEXT NOT NULL ," +
            FavouriteMovie.RATE+" TEXT NOT NULL) ";


    public static  final String Drop_TB=" DROP TABLE IF EXISTS  "+FavouriteMovie.TABLE_NAME+" ";

}

