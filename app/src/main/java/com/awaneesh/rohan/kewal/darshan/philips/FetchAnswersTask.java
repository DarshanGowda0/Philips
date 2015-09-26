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
public class FetchAnswersTask extends AsyncTask<Void,Void,Void> {

    String USER_ID,QUE_ID;
    BufferedReader mBufferedInputStream;
    String Response = "";

    public FetchAnswersTask(String USER_ID,String QUE_ID){
        this.USER_ID = USER_ID;
        this.QUE_ID = QUE_ID;
    }

    @Override
    protected Void doInBackground(Void... params) {


        try {
            URL url = new URL("http://204.152.203.111/philips/answers_page.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("user_id", USER_ID)
                    .appendQueryParameter("que_id", "" + QUE_ID);
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

    private void parseJSON(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AnswersData manswersData = new AnswersData();
                manswersData.ans_id = jsonObject.getString("ans_id");
                manswersData.answer = jsonObject.getString("answer");
                manswersData.user_name = jsonObject.getString("user_name");
                manswersData.down_bool = jsonObject.getString("down_bool");
                manswersData.up_bool = jsonObject.getString("up_bool");
                manswersData.usr_img = jsonObject.getString("user_img");
                manswersData.voted = jsonObject.getString("voted");
                manswersData.up_vote = jsonObject.getString("up_vote");
                manswersData.down_vote = jsonObject.getString("down_vote");

                AnswersTimeline.answersDatas.add(manswersData);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        AnswersTimeline.mAnswersTimelineAdapter.notifyDataSetChanged();
    }
}
