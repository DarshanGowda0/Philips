package com.awaneesh.rohan.kewal.darshan.philips;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnswersTimeline extends AppCompatActivity {

    public static RecyclerView mRecyclerView;
    int pos;
    CircleImageView image;
    TextView tvName, tvQuestion;
    SharedPreferences sharedPreferences;
    public static AnswerstimelineAdapter mAnswersTimelineAdapter;
    FloatingActionButton fab;
    public static ArrayList<AnswersData> answersDatas = new ArrayList<>();
    static ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        answersDatas.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_timeline);
        mAnswersTimelineAdapter = new AnswerstimelineAdapter(AnswersTimeline.this);
        Intent in = getIntent();
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Loading....");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.show();
        sharedPreferences = getSharedPreferences("Yes", MODE_PRIVATE);

        image = (CircleImageView) findViewById(R.id.userImageAnswer);
        tvName = (TextView) findViewById(R.id.userNameAnswer);
        tvQuestion = (TextView) findViewById(R.id.questionAnswer);

        pos = in.getIntExtra("position", 0);

//        image.setImageResource(MainActivity.list.get(pos).IMG);
        tvQuestion.setText(MainActivity.list.get(pos).QUESTION);
        tvName.setText(MainActivity.list.get(pos).NAME);
        new FetchAnswersTask(sharedPreferences.getString("id", "123456"), MainActivity.list.get(pos).QUE_ID).execute();
        setUpRec();

    }

    private void setUpRec() {
        fab = (FloatingActionButton) findViewById(R.id.FabAnswer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call postAnswer
                Intent in = new Intent(AnswersTimeline.this, PostAnswer.class);
                in.putExtra("pos", pos);
                startActivity(in);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewAnswer);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AnswersTimeline.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAnswersTimelineAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answers_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
