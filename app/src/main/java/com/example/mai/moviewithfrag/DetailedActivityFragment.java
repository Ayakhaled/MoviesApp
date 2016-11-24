package com.example.mai.moviewithfrag;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class DetailedActivityFragment extends Fragment {
    private String TAG = DetailedActivity.class.getSimpleName();
    private String id;
    private static String urlTrailer;
    private static String urlReview;
    private TrailersAdapter trailersAdapter;
    private ReviewAdapter reviewAdapter;
    private ArrayList<MovieTrailer> trailers;
    private ArrayList<MovieReview> reviews;
    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;
    private Button favBtn;
    private Favourite favDB;
    private MovieDetails movieDetails;

    public DetailedActivityFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detailed, container, false);

        favDB = new Favourite(getActivity());

        Intent intent = getActivity().getIntent();
        movieDetails = (MovieDetails) intent.getSerializableExtra("MovieD");

        TextView title = (TextView) rootView.findViewById(R.id.movieTitle);
        title.setText(movieDetails.getTitle());

        ImageView poster = (ImageView) rootView.findViewById(R.id.moviePoster);
        Picasso.with(getActivity())
                .load(movieDetails.getPoster())
                .placeholder(R.color.colorAccent)
                .into(poster);

        id = movieDetails.getMovieID();
        urlTrailer = "http://api.themoviedb.org/3/movie/"+id+"/videos?api_key=";
        urlReview = "http://api.themoviedb.org/3/movie/"+id+"/reviews?api_key=";

        TextView plotSynopsis = (TextView) rootView.findViewById(R.id.plotSynopsis);
        plotSynopsis.setText(movieDetails.getPlotSynopsis());

        TextView rating = (TextView) rootView.findViewById(R.id.movieRating);
        rating.setText(movieDetails.getRating());

        TextView releaseDate = (TextView) rootView.findViewById(R.id.movieReleaseDate);
        releaseDate.setText(movieDetails.getReleaseDate());

        trailers = new ArrayList<>();
        trailerRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailers_list);
        trailerRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        trailerRecyclerView.setItemAnimator(new DefaultItemAnimator());

        favBtn = (Button) rootView.findViewById(R.id.favBtn);


        new GetTrailers().execute();

        reviews = new ArrayList<>();
        reviewRecyclerView = (RecyclerView) rootView.findViewById(R.id.review_list);
        reviewRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (movieDetails.getFavourite())
            favBtn.setText(getString(R.string.remove_from_fav));
        else
            favBtn.setText(getString(R.string.add_to_fav));

        new GetReviews().execute();
        
        addToFavourite();

        return rootView;
    }

    public void addToFavourite(){
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favBtn.getText().equals(getString(R.string.add_to_fav))){
                    boolean isInserted = favDB.insertData(movieDetails.getTitle(), movieDetails.getPos(), movieDetails.getPlotSynopsis(), movieDetails.getRating(), movieDetails.getReleaseDate(), movieDetails.getMovieID());
                    if(isInserted == true){
                        Toast.makeText(getActivity(),"Added to favourite",Toast.LENGTH_LONG).show();
                        favBtn.setText(getString(R.string.remove_from_fav));
                        movieDetails.setFavourite(true);
                    }
                    else
                        Toast.makeText(getActivity(),"Not Added",Toast.LENGTH_LONG).show();
                }
                else if(favBtn.getText().equals(getString(R.string.remove_from_fav))){
                    removeFromFav();
                }

            }
        });
    }

    public void removeFromFav(){
        Integer deletedRows = favDB.removeFav(movieDetails.getMovieID());
        if(deletedRows > 0){
            Toast.makeText(getActivity(),"Removed",Toast.LENGTH_LONG).show();
            favBtn.setText(getString(R.string.add_to_fav));
            movieDetails.setFavourite(false);
        }
        else
            Toast.makeText(getActivity(),"Not Removed",Toast.LENGTH_LONG).show();

    }

    private class GetTrailers extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler handler = new HttpHandler();
            String api_key = "fb48e030a42dd2d9e680d5e62ec06b65";
            String jsonStr = handler.makeServiceCall(urlTrailer +api_key);

            Log.e(TAG, "Response from urlTrailer: " + jsonStr);
            if(jsonStr != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.e(TAG, "Results: " + results.length());

                    for (int i = 0; i < results.length(); i++){
                        JSONObject trailerRes = results.getJSONObject(i);

                        String movieId = id;
                        String movieName = trailerRes.getString("name");
                        String key = trailerRes.getString("key");

                        trailers.add(new MovieTrailer(movieId, key, movieName));
                    }

                }catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            trailersAdapter = new TrailersAdapter(trailers, getActivity());
            trailerRecyclerView.setAdapter(trailersAdapter);
        }
    }

    private class GetReviews extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler handler = new HttpHandler();
            String api_key = "fb48e030a42dd2d9e680d5e62ec06b65";
            String jsonStr = handler.makeServiceCall(urlReview +api_key);

            Log.e(TAG, "Response from urlReview: " + jsonStr);
            if(jsonStr != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.e(TAG, "Results: " + results.length());

                    for (int i = 0; i < results.length(); i++){
                        JSONObject reviewRes = results.getJSONObject(i);

                        String movieId = id;
                        String author = reviewRes.getString("author");
                        String content = reviewRes.getString("content");

                        reviews.add(new MovieReview(movieId, author, content));
                    }

                }catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            reviewAdapter = new ReviewAdapter(reviews, getActivity());
            reviewRecyclerView.setAdapter(reviewAdapter);
        }
    }
}
