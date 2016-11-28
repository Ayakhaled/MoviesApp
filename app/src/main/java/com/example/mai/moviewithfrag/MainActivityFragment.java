package com.example.mai.moviewithfrag;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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
    private String url = "https://api.themoviedb.org/3/movie/popular?api_key=";
    private TextView movieListTitle;
    private MoviesAdapter moviesAdapter;
    ArrayList<MovieDetails> data;
    private RecyclerView recyclerView;
    String[] movieListsTitles = {"Popular movies", "Top rated movies", "Favourited movies"};
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private Favourite favDB;
    SharedPreferences sharedPreferences;
    String defSort;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        favDB = new Favourite(getActivity());
        ArrayList<MovieDetails> favouriteMovies = new ArrayList<>(getFav());

        movieListTitle = (TextView) rootView.findViewById(R.id.movieListTitle);
        movieListTitle.setText(movieListsTitles[0]);
        data = new ArrayList<MovieDetails>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        defSort = sharedPreferences.getString(getString(R.string.pref_sortBy_key), getString(R.string.pref_sortBy_pop));
        if (defSort.equals("tr"))
            topRated();
        else if (defSort.equals("fav"))
            viewFavourites();
        else
            popular();
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
        restoreInstanceOnResume(defSort);
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
    }

    private void topRated(){
        data.clear();
        movieListTitle.setText(movieListsTitles[1]);
        url = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
        new GetMovies().execute();
    }

    public void viewFavourites() {
        data.clear();
        data.addAll(getFav());
        for (int j = 0; j < getFav().size(); j++) {
            data.get(j).setFavourite(true);
        }
        moviesAdapter = new MoviesAdapter(data, getActivity());
        recyclerView.setAdapter(moviesAdapter);
        movieListTitle.setText(movieListsTitles[2]);
    }

    public ArrayList<MovieDetails> getFav(){
        ArrayList<MovieDetails> favData = new ArrayList<>();
        Cursor res = favDB.getAllFavourites();
        while (res.moveToNext()) {
            MovieDetails mDetails = new MovieDetails(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5));
            favData.add(mDetails);
        }
        return favData;
    }


    @Override
    public void onPause() {
        super.onPause();
        saveInstanceOnPause();
    }

    public void saveInstanceOnPause(){
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);

    }

    public void restoreInstanceOnResume(String pref){

        if(pref.equals(defSort)) {
            if (mBundleRecyclerViewState != null) {
                Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        }
        else{
            if (defSort.equals("tr"))
                topRated();
            else if (defSort.equals("fav"))
                viewFavourites();
            else
                popular();
            if (mBundleRecyclerViewState != null) {
                Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        }
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
                        MovieDetails newMovDet = new MovieDetails(title, poster, overview, rating, release_date, id);

                        data.add(newMovDet);
                        for (int j = 0; j < getFav().size(); j++) {
                            if (id.equals(getFav().get(j).getMovieID()))
                                newMovDet.setFavourite(true);
                        }
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
                Log.e(TAG, "Couldn't get data from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Couldn't get json from server.",
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
