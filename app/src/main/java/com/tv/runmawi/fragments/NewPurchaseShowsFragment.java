package com.tv.runmawi.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tv.runmawi.R;
import com.tv.runmawi.adapters.EpisodeAdapter;
import com.tv.runmawi.adapters.NewMaulCarouselAdapter;
import com.tv.runmawi.constants;
import com.tv.runmawi.database.SQLiteHelper;
import com.tv.runmawi.model.Movies;
import com.tv.runmawi.model.pShowsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewPurchaseShowsFragment extends Fragment {
    View mView;
    ProgressDialog progressDialog;
    List<pShowsItem> dbshows;
    ArrayList<Movies> pmoviesList,preMoviesList;
    ArrayList<pShowsItem> dmoviesList;
    SQLiteHelper db;

    Intent i;
    String t_name;
    RecyclerView EpisodesRview;
    LinearLayoutManager mLayoutmanager;
    TextView shows_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_episode_list, container, false);
        db = new SQLiteHelper(getActivity());

        preMoviesList = new ArrayList<>();
        dmoviesList = new ArrayList<>();
        dbshows = db.getShowsList();
        for (pShowsItem cn : dbshows) {

            cn.getV_id();
            cn.getP_date();
            cn.getValidity();

            dmoviesList.add(cn);
        }

        EpisodesRview = mView.findViewById(R.id.tv_shows_recyclerview);
        mLayoutmanager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        EpisodesRview.setLayoutManager(mLayoutmanager);

        shows_name = mView.findViewById(R.id.shows_title);
        shows_name.setText("Purchased TV Shows");
        //  picassoBackgroundManager = new PicassoBackgroundManager(getActivity());
        new AsynkPremiumMovie().execute(constants.BASE_URL+"apis/tv_shows.php");
    //    new AsynkEpisodes().execute("https://runmawi.com/apis/tvshow_episodes.php?id="+mSelectedMovie.getVideo_id());
        return mView;
    }
    private class AsynkPremiumMovie extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                Log.i("loaded","onpost");
                progressDialog.dismiss();


                for(int d=0;d<dmoviesList.size();d++){
                    for(int p=0;p<pmoviesList.size();p++){
                        Log.i("for",d+" "+p);
                        if (dmoviesList.get(d).getShows_id().equals(pmoviesList.get(p).getVideo_id())){
                            Movies item = pmoviesList.get(p);
                            preMoviesList.add(item);
                        }

                    }
                }

             //   setAdapter(mAdapter);
                NewMaulCarouselAdapter lmovieAdapter = new NewMaulCarouselAdapter(getActivity(), preMoviesList,"movie", new NewMaulCarouselAdapter.RVClickListener() {
                    @Override
                    public void onSelected(NewMaulCarouselAdapter.RecyclerViewHolder viewHolder, int position) {



                    }
                });
                EpisodesRview.setAdapter(lmovieAdapter);
            }
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
//                JSONArray jsonArray = new JSONArray(s);
                //  videolist = new ArrayList<videoList>();
                //  listpreMoviewRowAdapter = new ArrayObjectAdapter(cardPresenter);
                pmoviesList = new ArrayList<>();
                if (jsonObject.getString("status").equals("true")){
                    Log.i("status","true");
                    JSONArray moviesArray = jsonObject.getJSONArray("tv_shows");
                    for (int i = 0; i < moviesArray.length(); i++) {
                        Movies item = new Movies();
                        JSONObject jObject = moviesArray.getJSONObject(i);

                        String vid = jObject.getString("tvshow_id");
                        String title = jObject.getString("title");
                        String description = jObject.getString("description");
                        String director = jObject.getString("director");
                        String producer = jObject.getString("producer");
                        String genre = jObject.getString("genre");
                        String length = jObject.getString("length");
                        String direct_url = "";
                        String ppv_cost = jObject.getString("ppv_cost");
                        String ppv_validity = jObject.getString("ppv_validity");
                        String status = "";
                        String priority = jObject.getString("priority");
                        String premier_on = jObject.getString("premier_on");
                        String thumbnail = jObject.getString("thumbnail");

                        item.setVideo_id(vid);
                        item.setTitle(title);
                        item.setCategory("TV Shows");
                        item.setDescription(description);
                        item.setDirector(director);
                        item.setProducer(producer);
                        item.setGenre(genre);
                        item.setLength(length);
                        item.setDirect_url(direct_url);
                        item.setPpv_cost(ppv_cost);
                        item.setPpv_validity(ppv_validity);
                        item.setStatus(status);
                        item.setPriority(priority);
                        item.setPremier_on(premier_on);
                        item.setThumbnail(thumbnail);

                        pmoviesList.add(item);
                        //    listTvShowsRowAdapter.add(item);

                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private class AsynkEpisodes extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                Log.i("loaded","onpost");
                progressDialog.dismiss();

                EpisodeAdapter lmovieAdapter = new EpisodeAdapter(getActivity(), pmoviesList,"movie");
                EpisodesRview.setAdapter(lmovieAdapter);
                EpisodesRview.requestFocus();
                EpisodesRview.scrollToPosition(0);
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection httpURLConnection;
            try {
                URL url = new URL(params[0]);
                Log.i("url",params[0]);
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
//                JSONArray jsonArray = new JSONArray(s);
                pmoviesList = new ArrayList<Movies>();
            //    listpreMoviewRowAdapter = new ArrayObjectAdapter(cardPresenter);
                if (jsonObject.getString("status").equals("true")){
                    Log.i("status","true");
                    JSONArray moviesArray = jsonObject.getJSONArray("tvhsow_episodes");
                    for (int i = 0; i < moviesArray.length(); i++) {
                        Movies item = new Movies();
                        JSONObject jObject = moviesArray.getJSONObject(i);

                        String vid = jObject.getString("video_id");
                        String title = jObject.getString("title");
                        String description = jObject.getString("description");
                        String director = jObject.getString("director");
                        String producer = jObject.getString("producer");
                        String genre = jObject.getString("genre");
                        String length = jObject.getString("length");
                        String direct_url = jObject.getString("url_main");
                        String ppv_cost = jObject.getString("ppv_cost");
                        String ppv_validity = jObject.getString("ppv_validity");
                        String status = jObject.getString("status");
                        String priority = jObject.getString("priority");
                        String premier_on = jObject.getString("premier_on");
                        String thumbnail = jObject.getString("thumbnail");


                        item.setVideo_id(vid);
                        item.setTitle(title);
                        item.setCategory("TV Shows");
                        item.setDescription(description);
                        item.setDirector(director);
                        item.setProducer(producer);
                        item.setGenre(genre);
                        item.setLength(length);
                        item.setDirect_url(direct_url);
                        item.setPpv_cost(ppv_cost);
                        item.setPpv_validity(ppv_validity);
                        item.setStatus(status);
                        item.setPriority(priority);
                        item.setPremier_on(premier_on);
                        item.setThumbnail(thumbnail);

                        pmoviesList.add(item);

                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}