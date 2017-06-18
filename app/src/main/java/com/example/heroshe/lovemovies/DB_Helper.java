package com.example.heroshe.lovemovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by fatma adel on 5/1/2016.
 */
public class DB_Helper extends SQLiteOpenHelper {

    static final String DB_Name="favourite_Movies.db";
    static final int DB_Version=3;

    public DB_Helper(Context context) {
        super(context, DB_Name, null, DB_Version);
        Log.v(" database operation", "database created / opened");
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Movie_Contract.Create_TB);
        Log.v(" database operation", "database created !");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(Movie_Contract.Drop_TB);
        onCreate(sqLiteDatabase);
    }

    public void insertfavouriteMovie(String title,String poster,  String overview ,String date ,String rating, String movie_id, SQLiteDatabase db){
        ContentValues contentValues=new ContentValues();

        contentValues.put(Movie_Contract.FavouriteMovie.NAME,title);
        contentValues.put(Movie_Contract.FavouriteMovie.POSTER,poster);
        contentValues.put(Movie_Contract.FavouriteMovie.OVER,overview);
        contentValues.put(Movie_Contract.FavouriteMovie.RATE,rating);
        contentValues.put(Movie_Contract.FavouriteMovie.DATE,date);
        contentValues.put(Movie_Contract.FavouriteMovie.MOVIE_ID,movie_id);

        db.insert(Movie_Contract.FavouriteMovie.TABLE_NAME, null, contentValues);

        Log.v(" database operation", "favourite movie inserted !");
        Log.d("ddatabaseeeeeeeeeee",""+db);
    }

    public Cursor getfavouriteMovie(SQLiteDatabase db){
        Cursor cursor;
        String[] projections ={ Movie_Contract.FavouriteMovie.NAME,Movie_Contract.FavouriteMovie.POSTER,
                Movie_Contract.FavouriteMovie.OVER,Movie_Contract.FavouriteMovie.RATE,Movie_Contract.FavouriteMovie.DATE,Movie_Contract.FavouriteMovie.MOVIE_ID};
        cursor= db.query(Movie_Contract.FavouriteMovie.TABLE_NAME, projections, null, null,null,null, null,null);

        return cursor;
    }

    public Boolean search(String Id,SQLiteDatabase db) {
        Boolean check=false;
        Cursor cursor=null;
        cursor = db.query(Movie_Contract.FavouriteMovie.TABLE_NAME,new String[]{Movie_Contract.FavouriteMovie.id}
                , Movie_Contract.FavouriteMovie.MOVIE_ID  + "=?",
                new String[]{Id},null,null,null,null);
        cursor.moveToFirst();
        int x=cursor.getCount();
        if (x>0) {
                check = true;
            }
        return check;


    }

    public void delete(String Id,SQLiteDatabase db) {
        db.execSQL("delete from "+Movie_Contract.FavouriteMovie.TABLE_NAME+" where "+Movie_Contract.FavouriteMovie.MOVIE_ID + "='"+Id+"'");
    }




}




