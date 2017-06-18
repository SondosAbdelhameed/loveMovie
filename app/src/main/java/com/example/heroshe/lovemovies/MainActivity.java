package com.example.heroshe.lovemovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
   // private static final String TAG = MainActivity.class.getSimpleName();
    SharedPreferences sharedpreferences;
    public static boolean tablet =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.tablet_frame) != null) {
            if (savedInstanceState != null) {
                return;
            }
            this.tablet = true;
            MainActivityFragment MovieFragment = new MainActivityFragment();
            MovieFragment.setArguments(getIntent().getExtras());
           // getSupportFragmentManager().beginTransaction().add(R.id.tablet_frame, MovieFragment).commit();

        }



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);

        sharedpreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
    }
}
