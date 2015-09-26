package com.awaneesh.rohan.kewal.darshan.philips;

import android.app.ProgressDialog;
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
public class PostQuestionTask extends AsyncTask<Void,Void,Void> {

    String user_id,question,type;
    BufferedReader mBufferedInputStream;
    String Response = "";

    public PostQuestionTask(String user_id,String question,String type){
        this.user_id = user_id;
        this.question = question;
        this.type = type;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL("http://204.152.203.111/philips/post_question.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("type", type)
                    .appendQueryParameter("user_id",user_id)
                    .appendQueryParameter("user_question",question);
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

                Log.d("KEWAL",Response);

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
    protected void onPreExecute() {
        super.onPreExecute();
        PostQuestion.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        PostQuestion.progressDialog.setMessage("Loading....");
        PostQuestion.progressDialog.setIndeterminate(true);
        PostQuestion.progressDialog.setProgressNumberFormat(null);
        PostQuestion.progressDialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        PostQuestion.progressDialog.dismiss();
    }
}
