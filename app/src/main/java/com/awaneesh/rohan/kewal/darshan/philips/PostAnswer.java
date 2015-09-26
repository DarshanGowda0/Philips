package com.awaneesh.rohan.kewal.darshan.philips;

import android.app.ProgressDialog;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class PostAnswer extends ActionBarActivity {

    EditText richEditText1;
    FloatingActionButton fab;
    private static final int SPEECH_REQUEST_CODE = 2222;
    TextView POST;
    int pos;
    String ANSWER,USER_ID="123456",TYPE="DIABETES";
    static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_answer);
        Intent in = getIntent();
        pos = in.getIntExtra("pos",0);
        init();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();
            }
        });

    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "This feature is not compitable with your device", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        richEditText1 = (EditText) findViewById(R.id.answerTv);
        fab = (FloatingActionButton) findViewById(R.id.fab2);
        POST = (TextView) findViewById(R.id.postTv);
        POST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ANSWER = richEditText1.getText().toString();
                progressDialog = new ProgressDialog(PostAnswer.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("Loading....");
                progressDialog.setIndeterminate(true);
                progressDialog.setProgressNumberFormat(null);
                progressDialog.show();
                new PostAnswerTask(PostAnswer.this,MainActivity.list.get(pos).QUE_ID,ANSWER).execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            String present = Html.toHtml(richEditText1.getText());
            richEditText1.setText(Html.fromHtml(present)+spokenText);
        }

    }
}
