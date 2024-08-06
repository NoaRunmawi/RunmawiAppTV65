package com.tv.runmawi.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.leanback.app.RowsFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.tv.runmawi.LoginActivity;
import com.tv.runmawi.PurchaseDubbedMoviesActivity;
import com.tv.runmawi.PurchasedMoviesActivity;
import com.tv.runmawi.PurchasedTVShowActivity;
import com.tv.runmawi.R;
import com.tv.runmawi.constants;
import com.tv.runmawi.database.SQLiteHelper;
import com.tv.runmawi.model.pMovieItem;
import com.tv.runmawi.presenter.GridItemPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class UserSettingFragment extends RowsFragment {
    private final ArrayObjectAdapter mRowsAdapter;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SQLiteHelper db;
    String user_id;
    List<pMovieItem> db_movielist;
    int count_movies,count_shows,count_dmovies;

    public UserSettingFragment() {
        ListRowPresenter selector = new ListRowPresenter();
        selector.setNumRows(1);
        mRowsAdapter = new ArrayObjectAdapter(selector);
        setAdapter(mRowsAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        pref = getActivity().getSharedPreferences("RunMawiPreferences",MODE_PRIVATE);
        db = new SQLiteHelper(getActivity());
        user_id = pref.getString("user_id","");
        count_movies = db.check_movies();
        count_dmovies = db.check_dubbedmovies();
        count_shows = db.check_tvshows();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 200);
        new AsynkPurchasePMovie().execute(constants.BASE_URL+"apis/purchased_premium_movies.php?user_id="+user_id);
        new AsynkPurchaseTVShows().execute(constants.BASE_URL+"apis/purchased_tvshow.php?user_id="+user_id);
        new AsynkPurchaseDMovie().execute(constants.BASE_URL+"apis/purchased_dubbed_movies.php?user_id="+user_id);
        setupEventListeners();
    }
    private void setupEventListeners() {

        setOnItemViewClickedListener(new ItemViewClickedListener());

    }



    private void loadData() {
        if (isAdded()) {
        /*    String json = Utils.inputStreamToString(getResources().openRawResource(
                    R.raw.icon_example));
            CardRow cardRow = new Gson().fromJson(json, CardRow.class);
            mRowsAdapter.add(createCardRow(cardRow));
            getMainFragmentAdapter().getFragmentHost().notifyDataReady(
                    getMainFragmentAdapter()); */
            HeaderItem gridHeader = new HeaderItem( "USER");

            GridItemPresenter mGridPresenter = new GridItemPresenter();
            ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
            gridRowAdapter.add("Purchased Premium Movies");
            gridRowAdapter.add("Purchased TV Shows");
            gridRowAdapter.add("Purchased Dubbed Movies");
            gridRowAdapter.add("Log Out");
            mRowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));

            setAdapter(mRowsAdapter);
        }
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof String) {

                if (((String) item).contains("Purchased Premium Movies")) {
                    if (count_movies>0){
                        Intent intent = new Intent(getActivity(), PurchasedMoviesActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(), "No Movie Purchased", Toast.LENGTH_LONG).show();
                    }

                }else if (((String) item).contains("Purchased TV Shows")) {
                    if (count_shows>0){
                        Intent intent = new Intent(getActivity(), PurchasedTVShowActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(), "No TV Shows Purchased", Toast.LENGTH_LONG).show();
                    }

                }else if (((String) item).contains("Purchased Dubbed Movies")) {
                    if (count_dmovies>0){
                        Intent intent = new Intent(getActivity(), PurchaseDubbedMoviesActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(), "No Dubbed Movie Purchased", Toast.LENGTH_LONG).show();
                    }

                }else if (((String) item).contains("Log Out")) {
                    logOut();

                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void logOut() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.menu_logout))
                .setMessage(getString(R.string.logout_msg))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //  myApplication.saveIsLogin(false);
                        //  logoutApiCall();
                        db.deletepMovies();
                        db.deletepTvShows();
                        db.deletedMovies();
                        editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        // intent.putExtra("isLogout", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.ic_logout)
                .show();
    }
    private class AsynkPurchasePMovie extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            count_movies = db.check_movies();
            Log.i("count", String.valueOf(count_movies));
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection httpURLConnection;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestProperty("Authorization", "Bearer 5c7b64572f28a46f5462aa0b2156e965");
                int statuscode = httpURLConnection.getResponseCode();

                if (statuscode == 200) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bf.readLine()) != null) {
                        response.append(line);
                    }

                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        private void parseResult(String s) {
            Log.i("response",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray orderArray = jsonObject.getJSONArray("orders");
                if(orderArray.length()>0){
                    for (int i=0;i<orderArray.length();i++){
                        JSONObject data = orderArray.getJSONObject(i);
                        String video_id = data.getString("video_id");
                        String purchased_date = data.getString("purchased_date");
                        String validity = data.getString("validity");
                        db.Add_movies(video_id,purchased_date,validity);
                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private class AsynkPurchaseTVShows extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            count_movies = db.check_tvshows();
            Log.i("count", String.valueOf(count_movies));
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection httpURLConnection;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestProperty("Authorization", "Bearer 5c7b64572f28a46f5462aa0b2156e965");
                int statuscode = httpURLConnection.getResponseCode();

                if (statuscode == 200) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bf.readLine()) != null) {
                        response.append(line);
                    }

                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        private void parseResult(String s) {
            Log.i("response",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray orderArray = jsonObject.getJSONArray("tvshow_orders");
                if(orderArray.length()>0){
                    for (int i=0;i<orderArray.length();i++){
                        JSONObject data = orderArray.getJSONObject(i);
                        String shows_id = data.getString("tvshow_id");
                        String video_id = data.getString("video_id");
                        String purchased_date = data.getString("purchased_date");
                        String validity = data.getString("validity");
                        db.Add_shows(shows_id,video_id,purchased_date,validity);
                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private class AsynkPurchaseDMovie extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            count_movies = db.check_movies();
            Log.i("count", String.valueOf(count_movies));
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection httpURLConnection;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestProperty("Authorization", "Bearer 5c7b64572f28a46f5462aa0b2156e965");
                int statuscode = httpURLConnection.getResponseCode();

                if (statuscode == 200) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bf.readLine()) != null) {
                        response.append(line);
                    }

                    parseResult(response.toString());
                    result = 1; // Successful
                } else {
                    result = 0;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        private void parseResult(String s) {
            Log.i("response",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray orderArray = jsonObject.getJSONArray("orders");
                if(orderArray.length()>0){
                    for (int i=0;i<orderArray.length();i++){
                        JSONObject data = orderArray.getJSONObject(i);
                        String video_id = data.getString("video_id");
                        String purchased_date = data.getString("purchased_date");
                        String validity = data.getString("validity");
                        db.Add_dubbedmovies(video_id,purchased_date,validity);
                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
