package com.example.mai.moviewithfrag;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private String url = "https://api.themoviedb.org/3/movie/popular?api_key=";
    private TextView movieListTitle;
    private MoviesAdapter moviesAdapter;
    ArrayList<MovieDetails> data;
    private RecyclerView recyclerView;
    String[] movieListsTitles = {"Popular movies", "Top rated movies", "Favourited movies"};

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movieListTitle = (TextView) rootView.findViewById(R.id.movieListTitle);
        movieListTitle.setText(movieListsTitles[0]);

        data = new ArrayList<MovieDetails>();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        System.out.println(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new GetMovies().execute();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String defSort = sharedPreferences.getString(getString(R.string.pref_sortBy_key), getString(R.string.pref_sortBy_pop));
        System.out.println("Aya From Fragment " + defSort);
        if (defSort.equals("tr"))
            topRated();
        else
            popular();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.set_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.sort_by);

    }

    private void popular(){
        data.clear();
        movieListTitle.setText(movieListsTitles[0]);
        url = "https://api.themoviedb.org/3/movie/popular?api_key=";
        new GetMovies().execute();
        System.out.println("Aya Menu Item: "+movieListsTitles[0]);

    }

    private void topRated(){
        data.clear();
        movieListTitle.setText(movieListsTitles[1]);
        url = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
        new GetMovies().execute();
        System.out.println("Aya Menu Item:"+movieListsTitles[1]);
    }

    private class GetMovies extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler handler = new HttpHandler();
            String api_key = "fb48e030a42dd2d9e680d5e62ec06b65";
            String jsonStr = handler.makeServiceCall(url+api_key);

            Log.e(TAG, "Response from url: " + jsonStr);
            if(jsonStr != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.e(TAG, "Results: " + results);

                    for (int i = 0; i < results.length(); i++){
                        JSONObject moviesRes = results.getJSONObject(i);

                        String poster = moviesRes.getString("poster_path");
                        String overview = moviesRes.getString("overview");
                        String release_date = moviesRes.getString("release_date");
                        String title = moviesRes.getString("title");
                        String rating = moviesRes.getString("vote_average");
                        String id = moviesRes.getString("id");

                        data.add(new MovieDetails(title, poster, overview, rating, release_date, id));
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
            moviesAdapter = new MoviesAdapter(data, getActivity());
            recyclerView.setAdapter(moviesAdapter);
        }
    }
}
