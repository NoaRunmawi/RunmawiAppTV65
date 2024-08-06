package com.tv.runmawi.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.app.DetailsSupportFragmentBackgroundController;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tv.runmawi.DetailActivity;
import com.tv.runmawi.EpisodeListActivity;
import com.tv.runmawi.R;
import com.tv.runmawi.WebviewPlayActivity;
import com.tv.runmawi.constants;
import com.tv.runmawi.database.SQLiteHelper;
import com.tv.runmawi.model.Movies;
import com.tv.runmawi.presenter.DetailsDescriptionPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VideoDetailsFragmentold2 extends DetailsSupportFragment {
    private static final String TAG = "VideoDetailsFragment";

    private static final int ACTION_WATCH_TRAILER = 1;
    private static final int ACTION_RENT = 2;
    private static final int ACTION_BUY = 3;

    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;

    private static final int NUM_COLS = 10;

    private Movies mSelectedMovie;

    private ArrayObjectAdapter mAdapter;
    private ClassPresenterSelector mPresenterSelector;

    private DetailsSupportFragmentBackgroundController mDetailsBackground;

    SQLiteHelper db;
    int count = 0;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String user_id = "";
    String user_name = "";
    String user_email = "";
    String user_status = "";
    String user_phone = "";
    String tv_key = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate DetailsFragment");
        super.onCreate(savedInstanceState);

        mDetailsBackground = new DetailsSupportFragmentBackgroundController(this);
        db = new SQLiteHelper(getActivity());
        mSelectedMovie =
                (Movies) getActivity().getIntent().getSerializableExtra(DetailActivity.MOVIE);
        if (mSelectedMovie != null) {
            mPresenterSelector = new ClassPresenterSelector();
            mAdapter = new ArrayObjectAdapter(mPresenterSelector);
            setupDetailsOverviewRow();
            setupDetailsOverviewRowPresenter();

            setAdapter(mAdapter);
            initializeBackground(mSelectedMovie);
            setOnItemViewClickedListener(new ItemViewClickedListener());
        } else {
            //   Intent intent = new Intent(getActivity(), MainActivity.class);
            //  startActivity(intent);
        }
    }

    private void initializeBackground(Movies data) {
        mDetailsBackground.enableParallax();
        Glide.with(getActivity())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.default_background)
                .load(data.getThumbnail())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap,
                                                @Nullable Transition<? super Bitmap> transition) {
                        mDetailsBackground.setCoverBitmap(bitmap);
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                    }
                });
    }

    private void setupDetailsOverviewRow() {
        Log.d(TAG, "doInBackground: " + mSelectedMovie.toString());
        final DetailsOverviewRow row = new DetailsOverviewRow(mSelectedMovie);
        row.setImageDrawable(
                ContextCompat.getDrawable(getActivity(), R.drawable.default_background));
        int width = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_WIDTH);
        int height = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_HEIGHT);
        Glide.with(getActivity())
                .load(mSelectedMovie.getThumbnail())
                .centerCrop()
                .error(R.drawable.default_background)
                .into(new SimpleTarget<Drawable>(width, height) {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable,
                                                @Nullable Transition<? super Drawable> transition) {
                        Log.d(TAG, "details overview card image url ready: " + drawable);
                        row.setImageDrawable(drawable);
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                    }
                });

        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();
        actionAdapter.add(
                new Action(
                        ACTION_WATCH_TRAILER,
                        "Watch Trailer",
                        ""));
        if (mSelectedMovie.getCategory().equals("Premium Movies")){
            count = db.check_pmoviesId(mSelectedMovie.getVideo_id());
            if (count>0){
                actionAdapter.add(
                        new Action(
                                ACTION_RENT,
                                "Play Video",
                                ""));
            }else{
                actionAdapter.add(
                        new Action(
                                ACTION_RENT,
                                "Rent By",
                                "₹ "+mSelectedMovie.getPpv_cost()));
            }

        } else if (mSelectedMovie.getCategory().equals("TV Shows")){
            count = db.check_showId(mSelectedMovie.getVideo_id());
            if (count>0){
                actionAdapter.add(
                        new Action(
                                ACTION_RENT,
                                "View Episodes",
                                ""));
            }else{
                actionAdapter.add(
                        new Action(
                                ACTION_RENT,
                                "Rent By",
                                "₹ "+mSelectedMovie.getPpv_cost()));
            }

        }else if (mSelectedMovie.getCategory().equals("Dubbed Movies")){
            count = db.check_showId(mSelectedMovie.getVideo_id());
            if (count>0){
                actionAdapter.add(
                        new Action(
                                ACTION_RENT,
                                "Play Video",
                                ""));
            }else{
                actionAdapter.add(
                        new Action(
                                ACTION_RENT,
                                "Rent By",
                                "₹ "+mSelectedMovie.getPpv_cost()));
            }

        }else{
            actionAdapter.add(
                    new Action(
                            ACTION_RENT,
                            "Play Video",
                            ""));
        }

        row.setActionsAdapter(actionAdapter);

        mAdapter.add(row);
    }

    private void setupDetailsOverviewRowPresenter() {

        pref = getContext().getSharedPreferences("RunMawiPreferences",MODE_PRIVATE);
        editor = pref.edit();

        user_id = pref.getString("user_id", "x");
        user_name=pref.getString("user_name","x");
        user_email=pref.getString("user_email","x");
        user_status=pref.getString("user_status","x");
        user_phone=pref.getString("user_phone","x");
        tv_key=pref.getString("tv_key","x");


        // Set detail background.
        FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
        detailsPresenter.setBackgroundColor(
                ContextCompat.getColor(getActivity(), R.color.main_background));

        // Hook up transition element.
        FullWidthDetailsOverviewSharedElementHelper sharedElementHelper =
                new FullWidthDetailsOverviewSharedElementHelper();
        sharedElementHelper.setSharedElementEnterTransition(
                getActivity(), DetailActivity.SHARED_ELEMENT_NAME);
        detailsPresenter.setListener(sharedElementHelper);
        detailsPresenter.setParticipatingEntranceTransition(true);

        String urlXXX = constants.BASE_URL+"apis/video_data.php?";
        RequestQueue queueXXX = Volley.newRequestQueue(getContext().getApplicationContext());
        StringRequest requestXXX = new StringRequest(Request.Method.POST, urlXXX, responseXXX -> {

            try {
                JSONObject jsonObjectXXX = new JSONObject(responseXXX);

                String direct_url = jsonObjectXXX.getString("direct_url");


                //   String direct_url = jsonObjectXXX.getString("direct_url");


                detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
                    @Override
                    public void onActionClicked(Action action) {
                        if (action.getId() == ACTION_RENT) {
                            if (mSelectedMovie.getCategory().equals("2")){
                                count = db.check_showId(mSelectedMovie.getVideo_id());
                                if (count>0){
                                    Intent intent = new Intent(getActivity(), EpisodeListActivity.class);
                                    intent.putExtra("video_type", "video");
                                    intent.putExtra("video_id", mSelectedMovie.getVideo_id());
                                    intent.putExtra("title",mSelectedMovie.getTitle());
                                    startActivity(intent);
                                }else{
                                    visitWebDialog();
                                }

                            }else if (mSelectedMovie.getCategory().equals("1")){
                                count = db.check_pmoviesId(mSelectedMovie.getVideo_id());
                                if (db.check_pmoviesId(mSelectedMovie.getVideo_id()) > 0 || db.check_dmoviesId(mSelectedMovie.getVideo_id()) > 0){
//                                    Intent intent = new Intent(getActivity(), BannerVideoExoPlayerActivity.class);
//                                    //  intent.putExtra(DetailActivity.MOVIE, mSelectedMovie);
//                                    intent.putExtra("video_type", "video");
//                                    intent.putExtra("video_url", mSelectedMovie.getDirect_url());
//                                    intent.putExtra("title", mSelectedMovie.getTitle());
//                                    intent.putExtra("desc", mSelectedMovie.getSummary());
//                                    startActivity(intent);

                                    Intent intent = new Intent(getActivity(), WebviewPlayActivity.class);
                                    intent.putExtra("video_id", mSelectedMovie.getVideo_id());
                                    intent.putExtra("url", ""+direct_url);
                                    startActivity(intent);

                                }else{
                                    visitWebDialog();
                                }

                            }
                  /*  else{
                        Intent intent = new Intent(getActivity(), VideoExampleWithExoPlayerActivity.class);
                        intent.putExtra(DetailActivity.MOVIE, mSelectedMovie);
                        intent.putExtra("video_type", "video");
                        startActivity(intent);
                    } */
                        }else if(action.getId() == ACTION_WATCH_TRAILER){
                           // Log.i("trailer",mSelectedMovie.getTrailer_url());
                                Toast.makeText(getActivity(), "Trailer available soon", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getActivity(), action.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } catch (JSONException e) {
                // handler.postDelayed(this::startapp, 1200);
                //   Toast.makeText(getContext(), "No response  ! Retry", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }, error -> {
            // handler.postDelayed(this::startapp, 10000);
            Toast.makeText(getContext(), "Something went wrong  ! Retry", Toast.LENGTH_SHORT).show();

        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> mapXXX = new HashMap<>();
                mapXXX.put("tv_key", ""+tv_key);
                mapXXX.put("user_id", ""+user_id);
                mapXXX.put("video_id", ""+mSelectedMovie.getVideo_id());
                return mapXXX;
            }
        };
        queueXXX.add(requestXXX);
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }
    private void visitWebDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Mobile App/ Website aṭang chiah a lei theih")
                .setMessage("He movie hi i en duh chuan TV-App hi chhuak la Mobile App aṭang emaw kan website runmawi.in tih aṭang emaw  in" +
                        " lei phawt la, chuan TV app ah hian i lut tha leh dawn nia")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //  myApplication.saveIsLogin(false);
                        //  logoutApiCall();



                    }
                })

                .show();
    }

    private int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {

            if (item instanceof Movies) {
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(getResources().getString(R.string.movie), mSelectedMovie);

                Bundle bundle =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(),
                                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                                        DetailActivity.SHARED_ELEMENT_NAME)
                                .toBundle();
                getActivity().startActivity(intent, bundle);
            }
        }
    }
}
