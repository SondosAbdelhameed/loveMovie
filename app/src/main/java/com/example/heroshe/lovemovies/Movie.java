package com.example.heroshe.lovemovies;

/**
 * Created by herohe on 25/04/16.
 */
public class Movie {
    String id;
    String title;
    String overview;
    String date;
    String poster_path;
    String vote_average;



    public Movie(String id , String title , String overview, String date , String poster,String vote_average){
        this.id=id;
        this.title=title;
        this.overview=overview;
        this.date=date;
        poster_path=poster;
        this.vote_average=vote_average;

    }



}
