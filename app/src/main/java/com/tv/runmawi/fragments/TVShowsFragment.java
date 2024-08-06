package com.tv.runmawi.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tv.runmawi.R;
import com.tv.runmawi.adapters.NewMaulCarouselAdapter;
import com.tv.runmawi.constants;
import com.tv.runmawi.model.Movies;

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

public class TVShowsFragment extends Fragment implements BrowseFragment.MainFragmentAdapterProvider {
    View mView;
    private BrowseFragment.MainFragmentAdapter mMainFragmentAdapter = new BrowseFragment.MainFragmentAdapter(this);
    HorizontalGridView premovies_rview,tv_shows_rview,dubbedMovies_rview,freeMovie_rview;
    LinearLayoutManager mLayoutManager,mLayoutManager2,mLayoutManager3,mLayoutManager4;
    ArrayList<Movies> preMovieList,tvShowsList,dubbedMovieList,freeMovieList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tv_shows, container, false);

        tv_shows_rview = mView.findViewById(R.id.tv_shows_recyclerview);
        mLayoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        tv_shows_rview.setLayoutManager(mLayoutManager2);

        new AsynkTVShws().execute(constants.BASE_URL+"apis/tv_shows.php");
        return mView;
    }

    @Override
    public BrowseFragment.MainFragmentAdapter getMainFragmentAdapter() {
        return mMainFragmentAdapter;
    }

    private class AsynkTVShws extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                NewMaulCarouselAdapter lmovieAdapter = new NewMaulCarouselAdapter(getActivity(), tvShowsList,"movie", new NewMaulCarouselAdapter.RVClickListener() {
                    @Override
                    public void onSelected(NewMaulCarouselAdapter.RecyclerViewHolder viewHolder, int position) {



                    }
                });
                tv_shows_rview.setAdapter(lmovieAdapter);

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
                tvShowsList = new ArrayList<Movies>();

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
                      //  String length = jObject.getString("length");
                        String direct_url = "";
                        String ppv_cost = jObject.getString("ppv_cost");
                        String ppv_validity = jObject.getString("ppv_validity");
                        String status = "";
                        String priority = jObject.getString("priority");
                        String premier_on = jObject.getString("premier_on");
                        String thumbnail = jObject.getString("thumbnail");
                        String trailer = jObject.getString("url_trailer");

                        item.setTrailer(trailer);
                        item.setVideo_id(vid);
                        item.setTitle(title);
                        item.setCategory("TV Shows");
                        item.setDescription(description);
                        item.setDirector(director);
                        item.setProducer(producer);
                        item.setGenre(genre);
                        item.setLength("");
                        item.setDirect_url(direct_url);
                        item.setPpv_cost(ppv_cost);
                        item.setPpv_validity(ppv_validity);
                        item.setStatus(status);
                        item.setPriority(priority);
                        item.setPremier_on(premier_on);
                        item.setThumbnail(thumbnail);

                        tvShowsList.add(item);

                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}