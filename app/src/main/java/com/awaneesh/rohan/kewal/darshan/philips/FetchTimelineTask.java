package com.awaneesh.rohan.kewal.darshan.philips;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by darshan on 26/09/15.
 */
public class FetchTimelineTask extends AsyncTask<Void, Void, Void> {

    BufferedReader mBufferedInputStream;
    String Response = "";
    int page_no;
    String type;

    public FetchTimelineTask(String type, int page_no) {
        this.type = type;
        this.page_no = page_no;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url = new URL("http://204.152.203.111/philips/timeline.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("type", type)
                    .appendQueryParameter("page_no", "" + page_no);
//            Log.d("pageno", "" + page_no);

            String query = builder.build().getEncodedQuery();

            OutputStream os = httpURLConnection.getOutputStream();

            BufferedWriter mBufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            mBufferedWriter.write(query);
            mBufferedWriter.flush();
            mBufferedWriter.close();
            os.close();

            httpURLConnection.connect();


            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                mBufferedInputStream = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inline;
                while ((inline = mBufferedInputStream.readLine()) != null) {
                    Response += inline;
                }
                mBufferedInputStream.close();

//                parseJson(Response);
                Log.d("KEWAL", Response);
                parseJSON(Response);

            } else {
                Log.d("darshan", "something wrong");

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainActivity.mTimelineAdapter.notifyDataSetChanged();
    }

    private void parseJSON(String response) {
        JSONObject jsonObject;
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObject = jsonArray.getJSONObject(i);

                TimelineData mTimelineData = new TimelineData();

                mTimelineData.NAME = jsonObject.getString("user_name");
                mTimelineData.IMG = jsonObject.getString("user_img");
                mTimelineData.QUE_ID = jsonObject.getString("que_id");
                mTimelineData.QUESTION = jsonObject.getString("question");

                MainActivity.list.add(mTimelineData);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
