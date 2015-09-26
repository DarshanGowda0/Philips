package com.awaneesh.rohan.kewal.darshan.philips;

import android.app.ProgressDialog;
import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


public class Login extends ActionBarActivity implements View.OnClickListener {

    public static   String FILENAME = "KEWAL";
    EditText userName, pwd;
    Button login;
    TextView incorrect;
    String access_token;
    int status;
    public static JSONObject innerJson, Observation;
    String username, password;
    String pic = null;
    public static ArrayList<String> weightValue = new ArrayList<>();
    static ProgressDialog progressDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


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
        if (pwd.getText().toString().equals("") || userName.getText().toString().equals("")) {
            incorrect.setText("Enter both username and password");

        } else {
            username = userName.getText().toString();
            password = pwd.getText().toString();
            new verify().execute();
        }
    }

    public class verify extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Loading....");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressNumberFormat(null);
            progressDialog.show();
        }

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
                urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                JSONObject person = new JSONObject();
                try {
                    person.put("password", password);
                    person.put("username", username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
                wr.write(person.toString());
                wr.close();
                Log.d("DarshanRohanTesting", person.toString());

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
                    JSONObject details = new JSONObject(result);
                    innerJson = new JSONObject(details.getString("user"));

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
            if (status == 200) {
                preferences = getSharedPreferences("Yes", Context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putInt("Check", 1);

                try {
                    pic = innerJson.getString("picture");
                    pic = pic.replace("\\", "");
                    editor.putString("picture", pic);
                    editor.putString("name", innerJson.getString("name"));
                    editor.putString("id", innerJson.getString("id"));
                    editor.putString("type", "diabetes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.commit();

//                try {
                    new observe().execute();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            } else {
                incorrect.setText("Invalid Username or Password");
                incorrect.setTextColor(Color.RED);
            }
        }
    }

    public class observe extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                StringBuffer output = new StringBuffer("");
                URL url = new URL("https://gateway.api.pcftest.com:9004/v1/fhir_rest/Observation?subject._id=a101");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(10000);
//                urlConnection.setDoInput(true);
//                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();


                int status1 = urlConnection.getResponseCode();
                Log.d("DarshanRohanTesting", "observation" + status1);

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Log.d("Testing ob error", in.toString());
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(in));
                String s = "";
                while ((s = buffer.readLine()) != null)
                    output.append(s);
                String result = output.toString();
                Log.d("DarshanRohanTesting", result);
                JSONObject Obs = new JSONObject(result);
                JSONArray entry = new JSONArray(Obs.getString("entry"));
                for (int i = 0; i < entry.length(); i++) {
                    JSONObject inner = new JSONObject(String.valueOf(entry.getJSONObject(i)));
                    JSONObject content = new JSONObject(inner.getString("content"));
//                    JSONObject name = new JSONObject(content.getString("name"));
                    JSONObject valueQty = new JSONObject(content.getString("valueQuantity"));
                    weightValue.add(valueQty.getString("value"));
                    Log.d("testing", weightValue.get(i));
                }
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
            // testing puash
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                writeTo(weightValue.toString());
                new LoginTask(Login.this,innerJson.getString("name"),innerJson.getString("id"),pic,"diabetes").execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    void writeTo(String str){
        try {
            FileOutputStream fos = openFileOutput(FILENAME, MODE_PRIVATE);
            fos.write(str.getBytes());
            fos.close();
            Log.d("CALORIE","written to "+FILENAME+" successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
