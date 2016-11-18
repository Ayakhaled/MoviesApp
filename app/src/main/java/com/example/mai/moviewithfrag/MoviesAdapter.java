package com.example.mai.moviewithfrag;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by mai on 17/10/16.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder> {
    private ArrayList<MovieDetails> data;
    private LayoutInflater mInflater;
    private Context context;

    public MoviesAdapter(ArrayList<MovieDetails> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public static class MovieHolder extends RecyclerView.ViewHolder {
        public ImageView poster;

        public MovieHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.image);
        }
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        return new MovieHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        final MovieDetails movie = (MovieDetails) data.get(position);
        Picasso.with(context)
                .load(movie.getPoster())
                .placeholder(R.color.colorAccent)
                .into(holder.poster);
        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                String title = movie.getTitle();
                System.out.println("Title: "+title);
                intent.putExtra("MovieD", movie);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }
}