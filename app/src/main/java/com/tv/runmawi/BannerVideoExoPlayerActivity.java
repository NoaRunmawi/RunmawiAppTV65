package com.tv.runmawi;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.tv.runmawi.fragments.BannerExoPlayerFragment;
import com.tv.runmawi.fragments.VideoConsumptionExampleWithExoPlayerFragment;

public class BannerVideoExoPlayerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_banner_video_exo_player);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.videoFragment, new BannerExoPlayerFragment(),
                VideoConsumptionExampleWithExoPlayerFragment.TAG);
        ft.commit();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // This part is necessary to ensure that getIntent returns the latest intent when
        // VideoExampleActivity is started. By default, getIntent() returns the initial intent
        // that was set from another activity that started VideoExampleActivity. However, we need
        // to update this intent when for example, user clicks on another video when the currently
        // playing video is in PIP mode, and a new video needs to be started.
        setIntent(intent);
    }
}