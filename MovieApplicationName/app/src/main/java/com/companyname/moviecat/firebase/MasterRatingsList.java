package com.companyname.moviecat.firebase;

import android.support.annotation.Nullable;

import com.companyname.moviecat.models.Callback;
import com.companyname.moviecat.models.MovieSearchResults;
import com.companyname.moviecat.util.MapUtil;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Joe on 6/19/2017.
 */

public class MasterRatingsList {


    private static MasterRatingsList instance;

    private static FirebaseMovieRatings firebaseMovieRatings;

    private static ArrayList<MovieSearchResults> firebaseMovieRatingsList;

    private static Callback<Void> initCallback;

    private MasterRatingsList() {
    }

    public static void init(final Callback<Void> callback) {

        instance = new MasterRatingsList();
        initCallback = callback;

        firebaseMovieRatings = new FirebaseMovieRatings();
        firebaseMovieRatingsList = new ArrayList<>();

        Timber.d("masterRatings in init in master ratings");

        FirebaseMovieRatings.registerForRatingsList("MASTER_RATINGS_LIST", new Callback<FirebaseMovieRatings>() {
            @Override
            public void success(FirebaseMovieRatings movieSearchResults) {
                MasterRatingsList.firebaseMovieRatings = movieSearchResults;
                MasterRatingsList.firebaseMovieRatingsList = MapUtil.convertMapToList(movieSearchResults);

                Timber.d("masterRatings success");

                if (initCallback != null) {
                    initCallback.success(null);
                    initCallback = null;
                }
            }

            @Override
            public void failure(@Nullable String message) {
                Timber.d("masterRatings fail");

                if (initCallback != null) {
                    initCallback.failure(message);
                    initCallback = null;
                }
            }
        });
    }

    public static MasterRatingsList getInstance() {
        return instance;
    }


    public void save() {
        if (firebaseMovieRatings != null) firebaseMovieRatings.save();
    }

    public void add(MovieSearchResults movieSearchResults) {
        firebaseMovieRatings.put(String.valueOf(movieSearchResults.getId()), movieSearchResults);
        firebaseMovieRatings.save();
    }

    public FirebaseMovieRatings getFirebaseMovieFavorites() {
        return firebaseMovieRatings;
    }

    public ArrayList<MovieSearchResults> getFirebaseMovieRatingsList() {
        if(firebaseMovieRatingsList == null) {
            firebaseMovieRatingsList = new ArrayList<>();
        }
        return firebaseMovieRatingsList;
    }


}
