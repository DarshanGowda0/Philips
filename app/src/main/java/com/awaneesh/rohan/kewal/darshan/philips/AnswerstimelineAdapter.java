package com.awaneesh.rohan.kewal.darshan.philips;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by darshan on 26/09/15.
 */
public class AnswerstimelineAdapter extends RecyclerView.Adapter<AnswerstimelineAdapter.Holder> {

    Context context;

    public AnswerstimelineAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_answer, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.userNameTv.setText(AnswersTimeline.answersDatas.get(position).user_name);
        holder.answerTv.setText(AnswersTimeline.answersDatas.get(position).answer);
        holder.upvotesTv.setText(AnswersTimeline.answersDatas.get(position).up_vote);
        holder.downvotes.setText(AnswersTimeline.answersDatas.get(position).down_vote);

    }

    @Override
    public int getItemCount() {
        return AnswersTimeline.answersDatas.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView userNameTv, answerTv, upvotesTv, downvotes;
        ImageView tu, td;


        public Holder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.userImage_answer);
            userNameTv = (TextView) itemView.findViewById(R.id.userName_answer);
            answerTv = (TextView) itemView.findViewById(R.id.answer);
            upvotesTv = (TextView) itemView.findViewById(R.id.upCount);
            downvotes = (TextView) itemView.findViewById(R.id.downCount);
            tu = (ImageView) itemView.findViewById(R.id.up_votes);
            td = (ImageView) itemView.findViewById(R.id.down_votes);
        }
    }
}
