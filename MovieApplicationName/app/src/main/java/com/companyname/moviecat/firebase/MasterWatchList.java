package com.companyname.moviecat.firebase;

import android.support.annotation.Nullable;

import com.companyname.moviecat.models.Callback;
import com.companyname.moviecat.models.MovieSearchResults;
import com.companyname.moviecat.util.MapUtil;

import java.util.ArrayList;

/**
 * Created by Joe on 6/19/2017.
 */

public class MasterWatchList {


    private static MasterWatchList instance;

    private static FirebaseMovieWatched firebaseMovieWatched;

    private static ArrayList<MovieSearchResults> firebaseMovieWatchList;

    private static Callback<Void> initCallback;

    private MasterWatchList() {
    }

    public static void init(final Callback<Void> callback) {

        instance = new MasterWatchList();
        initCallback = callback;

        FirebaseMovieWatched.registerForWatched("MASTER_WATCHED_LIST", new Callback<FirebaseMovieWatched>() {
            @Override
            public void success(FirebaseMovieWatched firebaseMovieWatched) {
                MasterWatchList.firebaseMovieWatched = firebaseMovieWatched;
                MasterWatchList.firebaseMovieWatchList = MapUtil.convertMapToList(firebaseMovieWatched);

                if (initCallback != null) {
                    initCallback.success(null);
                    initCallback = null;
                }
            }

            @Override
            public void failure(@Nullable String message) {
                if (initCallback != null) {
                    initCallback.failure(message);
                    initCallback = null;
                }
            }
        });

    }

    public static MasterWatchList getInstance() {
        return instance;
    }


    public void save() {
        if (firebaseMovieWatched != null) firebaseMovieWatched.save();
    }

    public void add(MovieSearchResults movieSearchResults) {
        firebaseMovieWatched.put(String.valueOf(movieSearchResults.getId()), movieSearchResults);
        firebaseMovieWatched.save();
    }

    public FirebaseMovieWatched getFirebaseMovieWatched() {
        return firebaseMovieWatched;
    }

    public ArrayList<MovieSearchResults> getFirebaseMovieWatchList() {
        return firebaseMovieWatchList;
    }


}
