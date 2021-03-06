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

public class MasterFavoriteList {


    private static MasterFavoriteList instance;

    private static FirebaseMovieFavorites firebaseMovieFavorites;

    private static ArrayList<MovieSearchResults> firebaseMovieFavoritesList;

    private static Callback<Void> initCallback;

    private MasterFavoriteList() {
    }

    public static void init(final Callback<Void> callback) {

        instance = new MasterFavoriteList();
        initCallback = callback;

        firebaseMovieFavorites = new FirebaseMovieFavorites();
        firebaseMovieFavoritesList = new ArrayList<>();

        Timber.d("favoriteDebug favorite init done!");

        FirebaseMovieFavorites.registerForFavorites("MASTER_FAVORITE_LIST", new Callback<FirebaseMovieFavorites>() {
            @Override
            public void success(FirebaseMovieFavorites firebaseMovieFavorites) {
                Timber.d("favoriteDebug success return!");
                MasterFavoriteList.firebaseMovieFavorites = firebaseMovieFavorites;
                MasterFavoriteList.firebaseMovieFavoritesList = MapUtil.convertMapToList(firebaseMovieFavorites);

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

    public static MasterFavoriteList getInstance() {
        return instance;
    }


    public void save() {
        if (firebaseMovieFavorites != null) firebaseMovieFavorites.save();
    }

    public void add(MovieSearchResults movieSearchResults) {
        firebaseMovieFavorites.put(String.valueOf(movieSearchResults.getId()), movieSearchResults);
        firebaseMovieFavorites.save();
    }

    public FirebaseMovieFavorites getFirebaseMovieFavorites() {
        return firebaseMovieFavorites;
    }

    public ArrayList<MovieSearchResults> getFirebaseMovieFavoritesList() {
        return firebaseMovieFavoritesList;
    }

    public boolean isMovieFavorited(int id) {
        for(MovieSearchResults movieSearchResults: getFirebaseMovieFavoritesList()) {
            if(movieSearchResults.getId() == id) {
                return true;
            }
        }

        return false;
    }



}
