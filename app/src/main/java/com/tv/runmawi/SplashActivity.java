package com.tv.runmawi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.tv.runmawi.database.SQLiteHelper;

public class SplashActivity extends Activity {
    SharedPreferences pref;
    String user_id="";
    SQLiteHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_splash);

        pref = getSharedPreferences("RunMawiPreferences",MODE_PRIVATE);
        db = new SQLiteHelper(SplashActivity.this);
        user_id = pref.getString("user_id","");
        //   Log.i("user_id",user_id);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user_id.isEmpty() || user_id.equals("")){
                    Intent home = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(home);
                }else{
                    db.deletepMovies();
                    db.deletepTvShows();
                    db.deletedMovies();
                    Intent home = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(home);
                }

            }
        },3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}