package com.example.mai.moviewithfrag;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mai on 18/11/16.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewsHolder> {
    private ArrayList<MovieReview> data;
    private LayoutInflater mInflater;
    private Context context;

    public ReviewAdapter(ArrayList<MovieReview> data, Context context){
        this.data = data;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public ReviewAdapter.ReviewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.review_view, parent, false);
        return new ReviewAdapter.ReviewsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewsHolder holder, int position) {
        final MovieReview review = (MovieReview) data.get(position);
        holder.author.setText(review.getAuthor());
        holder.review.setText(review.getReview());
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public static class ReviewsHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public TextView review;
        public ReviewsHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author);
            review = (TextView) itemView.findViewById(R.id.review);
        }
    }
}
