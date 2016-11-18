package com.example.mai.moviewithfrag;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.popular, menu);
        MenuItem menuItem = menu.findItem(R.id.sort_by);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                data.clear();
                movieListTitle.setText(movieListsTitles[0]);
                popular();
                new GetMovies().execute();
                System.out.println("Menu Item: pop");
                return true;
            case R.id.top_rated:
                data.clear();
                movieListTitle.setText(movieListsTitles[1]);
                topRated();
                new GetMovies().execute();
                System.out.println("Menu Item: top rated");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void popular(){
        url = "https://api.themoviedb.org/3/movie/popular?api_key=";
    }

    private void topRated(){
        url = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    }

    private class GetMovies extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();
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
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            moviesAdapter = new MoviesAdapter(data, getActivity());
            recyclerView.setAdapter(moviesAdapter);
        }
    }
}
