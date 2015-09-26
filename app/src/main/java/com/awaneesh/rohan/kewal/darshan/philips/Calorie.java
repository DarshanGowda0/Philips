package com.awaneesh.rohan.kewal.darshan.philips;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;


public class Calorie extends ActionBarActivity {
    TextView TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie);
        TV = (TextView) findViewById(R.id.ctv);
        ArrayList<String> data = readFile(Calorie.this);

        double min = 63.58, max = 57.22, avg = 0, calorie = 0;

//        Log.d("Kewal", "" + Login.weightValue.size() + " " + data);
        for (int i = 0; i < data.size(); i++) {
            if ((i % 2) != 0) {
                Log.d("Kewal", "no i=" + i + ": " + Double.parseDouble(data.get(i)));
                continue;
            } else {
                avg += Double.parseDouble(data.get(i));
                Log.d("Kewal", "yes i=" + i + ": " + Double.parseDouble(data.get(i)));
            }
        }
        avg = avg / (data.size() / 2);
        Log.d("weight", "" + avg);
        calorie = 655 + (19.6 * avg) + (1.8 * 170) - (4.7 * 20);
        int a=(int)calorie;
        if (avg >= min && avg <= max) {
            Toast.makeText(this, "Healthy!! Can consume upto " + a + " calories", Toast.LENGTH_LONG).show();
            TV.setText( a + "");
        } else if (avg > max) {
            Toast.makeText(this, "Overweight!! Gotta reduce.. Should consume at max " + (a - 400) + " calories", Toast.LENGTH_LONG).show();
            TV.setText("" + (a - 400) );
        } else {
            Toast.makeText(this, "Underweight!! Gotta Increase..Can consume upto " + a + " calories", Toast.LENGTH_LONG).show();
            TV.setText(""+ calorie );
        }
    }

    private ArrayList<String> readFile(Context context) {
        String ret = null;
        ArrayList<String> myList = new ArrayList<String>();
        try {
            InputStream inputStream = context.
                    openFileInput(Login.FILENAME);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();

                String replace = ret.replace("[", "");
                String replace1 = replace.replace("]", "");
                myList = new ArrayList<String>(Arrays.asList(replace1.split(",")));

                return myList;
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myList;
    }
}
