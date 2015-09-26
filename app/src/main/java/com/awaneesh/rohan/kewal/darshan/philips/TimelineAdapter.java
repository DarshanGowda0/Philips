package com.awaneesh.rohan.kewal.darshan.philips;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by darshan on 26/09/15.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.Holder> {

    Context context;

    public TimelineAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_view, parent, false);
        Holder holder = new Holder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = MainActivity.recyclerView.getChildPosition(v);
                Intent in = new Intent(context,AnswersTimeline.class);
                in.putExtra("position",position);
                context.startActivity(in);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.questionTv.setText(MainActivity.list.get(position).QUESTION);
        holder.nameTv.setText(MainActivity.list.get(position).NAME);
        holder.imageView.setImageResource(R.drawable.user);
        //set random images


    }

    @Override
    public int getItemCount() {
        return MainActivity.list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

                CircleImageView imageView;
        TextView nameTv, questionTv;

        public Holder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.userImage);
            nameTv = (TextView) itemView.findViewById(R.id.userName);
            questionTv = (TextView) itemView.findViewById(R.id.question);
        }
    }

}
