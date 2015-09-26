package com.awaneesh.rohan.kewal.darshan.philips;

import android.content.Context;
import android.content.Intent;
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
public class LoginTask extends AsyncTask<Void, Void, Void> {

    BufferedReader mBufferedInputStream;
    String Response = "";
    Context context;
    String NAME, ID, PIC, TYPE;

    public LoginTask(Context context, String NAME, String ID, String PIC, String TYPE) {
        this.NAME = NAME;
        this.ID = ID;
        this.PIC = PIC;
        this.context = context;
        this.TYPE = TYPE;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url = new URL("http://204.152.203.111/philips/insert_login.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("user_id", ID)
                    .appendQueryParameter("user_name", NAME)
                    .appendQueryParameter("type", TYPE)
                    .appendQueryParameter("pic", PIC);
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
//                parseJSON(Response);

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

        Login.progressDialog.dismiss();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Login)context).finish();

    }
}
