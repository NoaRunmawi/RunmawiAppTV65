package com.tv.runmawi;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends Activity {

    EditText phone,tv_key;
    TextView login;
    private String p_number;
    private String key;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    String user_id = "";
    String user_name = "";
    String user_email = "";
    String user_status = "";
    String user_phone = "";


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getSharedPreferences("RunMawiPreferences",MODE_PRIVATE);
        phone = findViewById(R.id.phone_number);
        tv_key = findViewById(R.id.tv_key);
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkvalidation()) {
                    p_number = phone.getText().toString();
                    key = tv_key.getText().toString();
                    new LoginAsynk().execute(constants.BASE_URL+"apis/login.php?phone="+p_number+"&tv_key="+key);
                }
            }
        });
    }
    private boolean checkvalidation() {
        if (phone.getText().toString().length() <= 0) {
            phone.setError("Enter Phone Number");
            return false;
        }

        if (tv_key.getText().toString().length() <= 0) {
            tv_key.setError("Enter TV Key");
            return false;
        }

        return true;
    }

    private class LoginAsynk extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                Log.i("user_id",user_id);
                if (user_id.equals(null) || user_id.equals("null")){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Please check Phone/TV key",Toast.LENGTH_LONG).show();
                }else{
                    //4
                    new UserDetailAsynk().execute(constants.BASE_URL+"apis/user.php?id="+user_id);
                }

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
            Log.i("response login",s);
            try {
                JSONObject jsonObject = new JSONObject(s);
//                JSONArray jsonArray = new JSONArray(s);
                //  videolist = new ArrayList<videoList>();
                JSONObject userObject = jsonObject.getJSONObject("users");
                user_id = userObject.getString("user_id");


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class UserDetailAsynk extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                progressDialog.dismiss();
                Intent main = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(main);
                finish();

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
                JSONObject userObject = jsonObject.getJSONObject("users");
                user_id = userObject.getString("user_id");
                user_name = userObject.getString("user_name");
                user_email = userObject.getString("user_email");
                user_status = userObject.getString("user_status");
                user_phone = userObject.getString("user_phone");

                editor = pref.edit();
                editor.putString("user_id",user_id);
                editor.putString("user_name",user_name);
                editor.putString("user_email",user_email);
                editor.putString("user_status",user_status);
                editor.putString("user_phone",user_phone);
                editor.putString("tv_key",key);
                editor.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}