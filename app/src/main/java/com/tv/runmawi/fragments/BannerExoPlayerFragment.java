package com.tv.runmawi.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.leanback.app.VideoFragment;
import androidx.leanback.app.VideoFragmentGlueHost;
import androidx.leanback.media.PlaybackGlue;
import androidx.leanback.widget.PlaybackControlsRow;

import com.tv.runmawi.player.ExoPlayerAdapter;
import com.tv.runmawi.player.MediaMetaData;
import com.tv.runmawi.player.PlaybackSeekDiskDataProvider;
import com.tv.runmawi.player.VideoMediaPlayerGlue;

public class BannerExoPlayerFragment extends VideoFragment {


    public static final String TAG = "VideoConsumptionWithExoPlayer";
    private VideoMediaPlayerGlue<ExoPlayerAdapter> mMediaPlayerGlue;
    final VideoFragmentGlueHost mHost = new VideoFragmentGlueHost(this);
    String v_title,v_desc,v_url;

    static void playWhenReady(PlaybackGlue glue) {
        if (glue.isPrepared()) {
            glue.play();
        } else {
            glue.addPlayerCallback(new PlaybackGlue.PlayerCallback() {
                @Override
                public void onPreparedStateChanged(PlaybackGlue glue) {
                    if (glue.isPrepared()) {
                        glue.removePlayerCallback(this);
                        glue.play();
                    }
                }
            });
        }
    }

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener
            = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int state) {
        }
    };

    @SuppressLint({"WrongConstant", "LongLogTag"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   final Movies movie =
          //      (Movies) getActivity().getIntent().getSerializableExtra(DetailActivity.MOVIE);
        String v_type = getActivity().getIntent().getStringExtra("video_type");
        v_title = getActivity().getIntent().getStringExtra("title");
        v_desc = getActivity().getIntent().getStringExtra("desc");
        v_url = getActivity().getIntent().getStringExtra("video_url");
        ExoPlayerAdapter playerAdapter = new ExoPlayerAdapter(getActivity());
        playerAdapter.setAudioStreamType(AudioManager.USE_DEFAULT_STREAM_TYPE);
        mMediaPlayerGlue = new VideoMediaPlayerGlue(getActivity(), playerAdapter);
        mMediaPlayerGlue.setHost(mHost);
        AudioManager audioManager = (AudioManager) getActivity()
                .getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN) != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.w(TAG, "video player cannot obtain audio focus!");
        }

        mMediaPlayerGlue.setMode(PlaybackControlsRow.RepeatAction.NONE);
        MediaMetaData intentMetaData = getActivity().getIntent().getParcelableExtra(
                TAG);
        if (intentMetaData != null) {
            mMediaPlayerGlue.setTitle(intentMetaData.getMediaTitle());
            mMediaPlayerGlue.setSubtitle(intentMetaData.getMediaArtistName());
            mMediaPlayerGlue.getPlayerAdapter().setDataSource(
                    Uri.parse(intentMetaData.getMediaSourcePath()));
        } else {
            mMediaPlayerGlue.setTitle(v_title);
            mMediaPlayerGlue.setSubtitle(v_desc);
            if (v_type.equals("trailer")){
                Log.i("trailer","trailer");
                mMediaPlayerGlue.getPlayerAdapter().setDataSource(Uri.parse(v_url));


            }else{
                mMediaPlayerGlue.getPlayerAdapter().setDataSource(Uri.parse(v_url));
            }

        }
        PlaybackSeekDiskDataProvider.setDemoSeekProvider(mMediaPlayerGlue);
        playWhenReady(mMediaPlayerGlue);
        setBackgroundType(BG_LIGHT);
    }

    @Override
    public void onPause() {
        if (mMediaPlayerGlue != null) {
            mMediaPlayerGlue.pause();
        }
        super.onPause();
    }

}