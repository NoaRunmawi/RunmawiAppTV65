package com.tv.runmawi;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class PurchaseDubbedMoviesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_purchase_dubbed_movies);
    }
}