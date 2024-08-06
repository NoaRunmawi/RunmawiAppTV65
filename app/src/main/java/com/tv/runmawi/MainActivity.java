package com.tv.runmawi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.tv.runmawi.database.SQLiteHelper;

public class MainActivity extends Activity { //
    SQLiteHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new SQLiteHelper(this);
        //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
        //           WindowManager.LayoutParams.FLAG_SECURE);
     /*   if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_browse_fragment, new NewMainFragment())
                    .commitNow();
        } */
    }

    @Override
    public void onBackPressed() {
        //   super.onBackPressed();
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //  myApplication.saveIsLogin(false);
                        //  logoutApiCall();
                        db.deletepMovies();
                        db.deletepTvShows();
                        db.deletedMovies();
                        finish();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }
}