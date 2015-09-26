package com.awaneesh.rohan.kewal.darshan.philips;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
public class PostAnswerTask extends AsyncTask<Void,Void,Void> {

    SharedPreferences sharedPreferences;
    String ans,user_id,que_id;
    BufferedReader mBufferedInputStream;
    String Response = "";

    public PostAnswerTask(Context context,String que_id,String ans){
        sharedPreferences = context.getSharedPreferences("Yes",Context.MODE_PRIVATE);
        this.ans=ans;
        this.que_id=que_id;
        user_id = sharedPreferences.getString("id","123456");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PostAnswer.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        PostAnswer.progressDialog.setMessage("Loading....");
        PostAnswer.progressDialog.setIndeterminate(true);
        PostAnswer.progressDialog.setProgressNumberFormat(null);
        PostAnswer.progressDialog.show();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        PostAnswer.progressDialog.dismiss();
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url = new URL("http://204.152.203.111/philips/post_answer.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("ans", ans)
                    .appendQueryParameter("id",user_id)
                    .appendQueryParameter("que_id",que_id);
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

                Log.d("KEWAL", Response);

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
}
