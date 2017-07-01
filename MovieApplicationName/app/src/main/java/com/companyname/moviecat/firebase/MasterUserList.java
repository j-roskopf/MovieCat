package com.companyname.moviecat.firebase;

import android.support.annotation.Nullable;

import com.companyname.moviecat.models.Callback;
import com.companyname.moviecat.models.UserList;
import com.companyname.moviecat.util.MapUtil;

import java.util.ArrayList;

/**
 * Created by Joe on 6/19/2017.
 */

public class MasterUserList {


    private static MasterUserList instance;

    private static FirebaseMovieLists firebaseMovieLists;

    private static ArrayList<UserList> firebaseMovieListslist;

    private static Callback<Void> initCallback;

    private MasterUserList() {
    }

    public static void init(final Callback<Void> callback) {

        instance = new MasterUserList();
        initCallback = callback;

        FirebaseMovieLists.registerForUserList("MASTER_USER_LIST", new Callback<FirebaseMovieLists>() {
            @Override
            public void success(FirebaseMovieLists firebaseMovieLists) {
                MasterUserList.firebaseMovieLists = firebaseMovieLists;
                MasterUserList.firebaseMovieListslist = MapUtil.convertMapToList(firebaseMovieLists);

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

    public static MasterUserList getInstance() {
        return instance;
    }


    public void save() {
        if (firebaseMovieLists != null) firebaseMovieLists.save();
    }

    public void add(UserList userList) {
        firebaseMovieLists.put(String.valueOf(userList.getListName()), userList);
        firebaseMovieLists.save();
    }

    public FirebaseMovieLists getFirebaseMovieList() {
        return firebaseMovieLists;
    }

    public ArrayList<UserList> getFirebaseMovieListslist() {
        return firebaseMovieListslist;
    }


}
