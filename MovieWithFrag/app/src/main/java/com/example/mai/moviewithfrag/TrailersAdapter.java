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
 * Created by mai on 04/11/16.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerHolder> {
    private ArrayList<MovieTrailer> data;
    private LayoutInflater mInflater;
    private Context context;

    public TrailersAdapter(ArrayList<MovieTrailer> data, Context context){
        this.data = data;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.trailer_view, parent, false);
        return new TrailersAdapter.TrailerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        final MovieTrailer trailer = (MovieTrailer) data.get(position);
        holder.movieName.setText(trailer.getName());
        final String trailerUrl = trailer.getFullUrl();
        holder.movieName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myWebLink = new Intent(Intent.ACTION_VIEW);
                myWebLink.setData(Uri.parse(trailerUrl));
                context.startActivity(myWebLink);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }

    public static class TrailerHolder extends RecyclerView.ViewHolder {
        public ImageView trailerIcon;
        public TextView movieName;
        public TrailerHolder(View itemView) {
            super(itemView);
            this.trailerIcon = (ImageView)itemView.findViewById(R.id.play);
            movieName = (TextView) itemView.findViewById(R.id.trailer);
        }
    }
}
