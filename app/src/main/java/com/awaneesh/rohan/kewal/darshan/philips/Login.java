package com.awaneesh.rohan.kewal.darshan.philips;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class Login extends ActionBarActivity implements View.OnClickListener {

    EditText userName, pwd;
    Button login;
    TextView incorrect;
    String access_token;
    int status;
    JSONObject details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText) findViewById(R.id.Username);
        pwd = (EditText) findViewById(R.id.Password);
        login = (Button) findViewById(R.id.Login);
        incorrect = (TextView) findViewById(R.id.status);
        login.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (pwd.getText().toString().equals("")||userName.getText().toString().equals("")){
            incorrect.setText("Enter both username and password");
        }
        else {
            new verify().execute();
        }
    }

    public class verify extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            try {
                URL url = new URL("https://gateway.api.pcftest.com:9004/v1/oauth2/token?grant_type=client_credentials");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Authorization", "Basic TkNwbDVzdmdaTklHT0h6NDRRcUF3amlGb1RFSDBvR3g6djRuOXh5R1hibldGcmppYQ==");
                urlConnection.connect();
                StringBuffer output = new StringBuffer("");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(in));
                String s = "";
                while ((s = buffer.readLine()) != null)
                    output.append(s);
                String result = output.toString();
                Log.d("access token", result);

                JSONObject object = new JSONObject(result);
                access_token = object.getString("access_token");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                StringBuffer output = new StringBuffer("");
                URL url = new URL("https://gateway.api.pcftest.com:9004/v1/oauth2/authorize/login");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Authorization", "Bearer "+access_token);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JSONObject person = new JSONObject();
                try {
                    person.put("password",pwd.getText().toString());
                    person.put("username",userName.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream(),"UTF-8");
                wr.write(person.toString());
                wr.close();
                Log.d("DarshanRohanTesting",person.toString());

                urlConnection.connect();


                status = urlConnection.getResponseCode();
                Log.d("DarshanRohanTesting", "" + status);

                if (status != 200) {
                    InputStream in = new BufferedInputStream(urlConnection.getErrorStream());
                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(in));
                    String s = "";
                    while ((s = buffer.readLine()) != null)
                        output.append(s);
                    String result = output.toString();
                    Log.d("DarshanRohanTesting", result);
                } else {

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(in));
                    String s = "";
                    while ((s = buffer.readLine()) != null)
                        output.append(s);
                    String result = output.toString();
                    Log.d("DarshanRohanTesting", result);
                    details = new JSONObject(result);
                }
                //return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (status==200){
                SharedPreferences preferences = getSharedPreferences("Yes", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("Check", 1);
                try {
                    editor.putString("picture",details.getString("picture"));
                    editor.putString("name",details.getString("name"));
                    editor.putString("id",details.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.commit();
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                incorrect.setText("Invalid Username or Password");
                incorrect.setTextColor(Color.RED);
            }
        }
    }

}
