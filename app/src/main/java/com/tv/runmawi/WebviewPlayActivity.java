package com.tv.runmawi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class WebviewPlayActivity extends FragmentActivity {
    public static final String SHARED_ELEMENT_NAME = "hero";
    public static final String MOVIE = "Movie";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String user_id = "";
    String user_name = "";
    String user_email = "";
    String user_status = "";
    String user_phone = "";
    String tv_key = "";
    int show=0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_play);
        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.details_fragment, new WebviewPlayFragment())
//                    .commitNow();

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

            String url =  getIntent().getStringExtra("url");

            String video_id =  getIntent().getStringExtra("video_id");

            pref = WebviewPlayActivity.this.getSharedPreferences("RunMawiPreferences",MODE_PRIVATE);
            editor = pref.edit();

            user_id = pref.getString("user_id", "x");
            user_name=pref.getString("user_name","x");
            user_email=pref.getString("user_email","x");
            user_status=pref.getString("user_status","x");
            user_phone=pref.getString("user_phone","x");
            tv_key=pref.getString("tv_key","x");


            TextView watermark_txt=findViewById(R.id.watermark_txt);
            watermark_txt.setSelected(true);
            watermark_txt.setText(""+user_name+" ||  ID : "+user_id+" Ph : "+user_phone+"");

            final Handler handler = new Handler();
            final int delay = 10000; //
            // 1000 milliseconds == 1 second


            handler.postDelayed(new Runnable() {
                public void run() {
                    show++;
                    if(show==2){
                        watermark_txt.setVisibility(View.VISIBLE);
                        show=0;
                    }else{
                        watermark_txt.setVisibility(View.GONE);
                    }
                    handler.postDelayed(this, delay);
                }
            }, delay);


            WebView myWebView=findViewById(R.id.webview);

            WebSettings webSettings = myWebView.getSettings();
            webSettings.setAllowContentAccess(true);
            webSettings.setJavaScriptEnabled(true); // Enable JavaScript
            webSettings.setDomStorageEnabled(true); // Enable DOM storage
            webSettings.setMediaPlaybackRequiresUserGesture(false); //// Allows auto-play of media
            webSettings.setMediaPlaybackRequiresUserGesture(false); // This might be necessary for autoplay
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); // Allowing mixed content
            myWebView.setWebChromeClient(new WebChromeClient()); // Required for some DRM features

            myWebView.loadUrl(""+url); // Load your DRM-protected content here

        }


    }



}