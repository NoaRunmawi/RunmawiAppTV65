package com.tv.runmawi;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.tv.runmawi.fragments.VideoBannerDetailsFragment;

public class BannerDetailActivity extends FragmentActivity {
    public static final String SHARED_ELEMENT_NAME = "hero";
    public static final String MOVIE = "Movie";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment, new VideoBannerDetailsFragment())
                    .commitNow();
        }
    }

}