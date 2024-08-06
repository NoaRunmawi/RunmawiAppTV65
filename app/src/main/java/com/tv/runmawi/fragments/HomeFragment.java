package com.tv.runmawi.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.HorizontalGridView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.tv.runmawi.R;
import com.tv.runmawi.adapters.NewMaulCarouselAdapter;
import com.tv.runmawi.adapters.ViewPagerAdapter;
import com.tv.runmawi.constants;
import com.tv.runmawi.database.SQLiteHelper;
import com.tv.runmawi.model.Banneritem;
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
import java.util.Timer;


public class HomeFragment extends Fragment implements BrowseFragment.MainFragmentAdapterProvider {
    private BrowseFragment.MainFragmentAdapter mMainFragmentAdapter = new BrowseFragment.MainFragmentAdapter(this);
    View mView;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    int currentPage = 0;
    Timer timer;
    long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    long PERIOD_MS = 5000; // time in milliseconds between successive task executions.
    ArrayList<Banneritem> SliderList;
    HorizontalGridView premovies_rview,tv_shows_rview,dubbedMovies_rview,freeMovie_rview;
    LinearLayoutManager mLayoutManager,mLayoutManager2,mLayoutManager3,mLayoutManager4;
    ArrayList<Movies> preMovieList,tvShowsList,dubbedMovieList,freeMovieList;
    SQLiteHelper db;
    String user_id;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int count_movies,count_shows,count_dmovies;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) mView.findViewById(R.id.viewPager);
       // pref = getActivity().getSharedPreferences("runmawi_app",MODE_PRIVATE);
        pref = getActivity().getSharedPreferences("RunMawiPreferences",MODE_PRIVATE);
        db = new SQLiteHelper(getActivity());
        user_id = pref.getString("user_id","");

        premovies_rview = mView.findViewById(R.id.pre_Movies_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        premovies_rview.setLayoutManager(mLayoutManager);

        tv_shows_rview = mView.findViewById(R.id.tv_shows_recyclerview);
        mLayoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        tv_shows_rview.setLayoutManager(mLayoutManager2);

        dubbedMovies_rview = mView.findViewById(R.id.dubbedmovie_recyclerview);
        mLayoutManager3 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        dubbedMovies_rview.setLayoutManager(mLayoutManager3);

        freeMovie_rview = mView.findViewById(R.id.freemovie_recyclerview);
        mLayoutManager4 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        freeMovie_rview.setLayoutManager(mLayoutManager4);

        sliderDotspanel = (LinearLayout) mView.findViewById(R.id.SliderDots);
        viewPager.setFocusable(true);
        viewPager.requestFocus();
        new AsynkBanner().execute(constants.BASE_URL+"apis/banner.php");
        new AsynkPremiumMovie().execute(constants.BASE_URL+"apis/premium_movies.php");
        new AsynkTVShws().execute(constants.BASE_URL+"apis/tv_shows.php");
        new AsynkDubbedMovie().execute(constants.BASE_URL+"apis/dubbed_movies.php");
        new AsynkFreeMovie().execute(constants.BASE_URL+"apis/free_movies.php");

        db = new SQLiteHelper(getActivity());
        user_id = pref.getString("user_id","");
        count_movies = db.check_movies();
        count_dmovies = db.check_dubbedmovies();
        count_shows = db.check_tvshows();

        new AsynkPurchasePMovie().execute(constants.BASE_URL+"apis/purchased_premium_movies.php?user_id="+user_id);
        new AsynkPurchaseTVShows().execute(constants.BASE_URL+"apis/purchased_tvshow.php?user_id="+user_id);
        new AsynkPurchaseDMovie().execute(constants.BASE_URL+"apis/purchased_dubbed_movies.php?user_id="+user_id);

        return mView;
    }

    @Override
    public BrowseFragment.MainFragmentAdapter getMainFragmentAdapter() {
        return mMainFragmentAdapter;
    }

    private class AsynkBanner extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
     /*       progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show(); */
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //   progressDialog.dismiss();

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(),SliderList);

            viewPager.setAdapter(viewPagerAdapter);

            dotscount = viewPagerAdapter.getCount();
            dots = new ImageView[dotscount];

            for(int i = 0; i < dotscount; i++){

                dots[i] = new ImageView(getActivity());
                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(8, 0, 8, 0);

                sliderDotspanel.addView(dots[i], params);

            }

            dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    for(int i = 0; i< dotscount; i++){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
       /*     final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == dotscount) {
                        currentPage = 0;
                    }
                    viewPager.setCurrentItem(currentPage++, true);
                }
            };

            timer = new Timer(); // This will create a new Thread
            timer.schedule(new TimerTask() { // task to be scheduled
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS); */



   /*

            NewMaulCarouselAdapter tvseriesAdapter = new NewMaulCarouselAdapter(HomeActivity.this, tvseriesArraylist,"tvseries", new NewMaulCarouselAdapter.RVClickListener() {
                @Override
                public void onSelected(NewMaulCarouselAdapter.RecyclerViewHolder viewHolder, int position) {



                }
            });
            tvseries_rview.setAdapter(tvseriesAdapter);

            HomeVideoAdapter RAdapter = new HomeVideoAdapter(HomeActivity.this, GenreMovieList);
            vehList_RecyclerView.setAdapter(RAdapter); */

            //   vehList_RecyclerView.requestFocus();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection httpURLConnection;
            String response_str = null;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                int statuscode = httpURLConnection.getResponseCode();
                if (statuscode == 200) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder Sb = new StringBuilder();
                    String line;
                    while ((line = bf.readLine()) != null) {
                        Sb.append(line);
                    }
                    String response = String.valueOf(Sb);
                    response_str = parseVideoResponse(response);
                } else {
                    //  response_str = ParseResponse("null");

                    response_str = "false";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response_str;
        }

        private String parseVideoResponse(String jsonResponse) {
            Log.i("response",jsonResponse);
            String response = null;
            try {

                //  GenreMovieList = new LinkedHashMap<>();
                SliderList = new ArrayList<>();
                String video_url;
                String cat_name;
                JSONObject mainObject = new JSONObject(jsonResponse);
                //   JSONObject sliderObj = mainObject.getJSONObject("slider");
                JSONArray SliderArray = mainObject.getJSONArray("banner");
                for(int s=0;s<SliderArray.length();s++){
                    JSONObject slideObj = SliderArray.getJSONObject(s);
                    Banneritem sItem = new Banneritem();
                    sItem.setBanner_id(slideObj.getString("banner_id"));
                    sItem.setTitle(slideObj.getString("title"));
                    sItem.setDirector(slideObj.getString("director"));
                    sItem.setRelease_year(slideObj.getString("release_year"));
                    sItem.setTrailer_url(slideObj.getString("trailer_url"));
                    sItem.setGenre(slideObj.getString("genre"));
                    sItem.setImg(slideObj.getString("img"));
                    sItem.setPpv_cost(slideObj.getString("ppv_cost"));
                    sItem.setDirect_url(slideObj.getString("direct_url"));
                    sItem.setVideo_id(slideObj.getString("video_id"));
                    sItem.setSummary(slideObj.getString("summary"));
                    sItem.setCategory(slideObj.getString("category"));



                    SliderList.add(sItem);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private class AsynkPremiumMovie extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                Log.i("loaded","onpost");

                NewMaulCarouselAdapter lmovieAdapter = new NewMaulCarouselAdapter(getActivity(), preMovieList,"movie", new NewMaulCarouselAdapter.RVClickListener() {
                    @Override
                    public void onSelected(NewMaulCarouselAdapter.RecyclerViewHolder viewHolder, int position) {



                    }
                });
                premovies_rview.setAdapter(lmovieAdapter);
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
                preMovieList = new ArrayList<Movies>();
                if (jsonObject.getString("status").equals("true")){
                    Log.i("status","true");
                    JSONArray moviesArray = jsonObject.getJSONArray("premium_movies");
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
                        String direct_url = jObject.getString("direct_url");
                        String ppv_cost = jObject.getString("ppv_cost");
                        String ppv_validity = jObject.getString("ppv_validity");
                        String status = jObject.getString("status");
                        String priority = jObject.getString("priority");
                        String premier_on = jObject.getString("premier_on");
                        String thumbnail = jObject.getString("thumbnail");
                        String trailer = jObject.getString("url_trailer");
                        item.setTrailer(trailer);
                        item.setVideo_id(vid);
                        item.setTitle(title);
                        item.setCategory("Premium Movies");
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

                        preMovieList.add(item);

                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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

    private class AsynkDubbedMovie extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                Log.i("loaded","onpost");
                NewMaulCarouselAdapter lmovieAdapter = new NewMaulCarouselAdapter(getActivity(), dubbedMovieList,"movie", new NewMaulCarouselAdapter.RVClickListener() {
                    @Override
                    public void onSelected(NewMaulCarouselAdapter.RecyclerViewHolder viewHolder, int position) {



                    }
                });
                dubbedMovies_rview.setAdapter(lmovieAdapter);
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
                dubbedMovieList = new ArrayList<Movies>();

                if (jsonObject.getString("status").equals("true")){
                    Log.i("status","true");
                    JSONArray moviesArray = jsonObject.getJSONArray("premium_movies");
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
                        String direct_url = jObject.getString("direct_url");
                        String ppv_cost = jObject.getString("ppv_cost");
                        String ppv_validity = jObject.getString("ppv_validity");
                        String status = jObject.getString("status");
                        String priority = jObject.getString("priority");
                        String premier_on = jObject.getString("premier_on");
                        String thumbnail = jObject.getString("thumbnail");
                        String trailer = jObject.getString("url_trailer");
                        item.setTrailer(trailer);


                        item.setVideo_id(vid);
                        item.setTitle(title);
                        item.setCategory("Dubbed Movies");
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

                        dubbedMovieList.add(item);

                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class AsynkFreeMovie extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                Log.i("loaded","onpost");
                NewMaulCarouselAdapter lmovieAdapter = new NewMaulCarouselAdapter(getActivity(), freeMovieList,"movie", new NewMaulCarouselAdapter.RVClickListener() {
                    @Override
                    public void onSelected(NewMaulCarouselAdapter.RecyclerViewHolder viewHolder, int position) {



                    }
                });
                freeMovie_rview.setAdapter(lmovieAdapter);
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
                freeMovieList = new ArrayList<Movies>();

                if (jsonObject.getString("status").equals("true")){
                    Log.i("status","true");
                    JSONArray moviesArray = jsonObject.getJSONArray("free_movies");
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
                        String direct_url = jObject.getString("direct_url");
                        String ppv_cost = jObject.getString("ppv_cost");
                        String ppv_validity = jObject.getString("ppv_validity");
                        String status = jObject.getString("status");
                        String priority = jObject.getString("priority");
                        String premier_on = jObject.getString("premier_on");
                        String thumbnail = jObject.getString("thumbnail");
                        //  String trailer = jObject.getString("url_trailer");
                        String trailer = "";
                        item.setTrailer(trailer);


                        item.setVideo_id(vid);
                        item.setTitle(title);
                        item.setCategory("Free Movies");
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

                        freeMovieList.add(item);

                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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
            Log.i("response purchased",s);
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