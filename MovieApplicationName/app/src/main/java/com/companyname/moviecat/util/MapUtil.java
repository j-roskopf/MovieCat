package com.companyname.moviecat.util;

import com.companyname.moviecat.firebase.FirebaseMovieFavorites;
import com.companyname.moviecat.firebase.FirebaseMovieLists;
import com.companyname.moviecat.firebase.FirebaseMovieRatings;
import com.companyname.moviecat.models.MovieSearchResults;
import com.companyname.moviecat.models.UserList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Joe on 6/19/2017.
 */

public class MapUtil {
    public static ArrayList<MovieSearchResults> convertMapToList(FirebaseMovieFavorites firebaseMovieFavorites) {
        ArrayList<MovieSearchResults> toReturn = new ArrayList<>();
        Iterator it = firebaseMovieFavorites.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            MovieSearchResults movieSearchResults = (MovieSearchResults) pair.getValue();
            toReturn.add(movieSearchResults);
        }
        return toReturn;
    }

    public static ArrayList<UserList> convertMapToList(FirebaseMovieLists firebaseMovieLists) {
        ArrayList<UserList> toReturn = new ArrayList<>();
        Iterator it = firebaseMovieLists.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            UserList userList = (UserList) pair.getValue();
            toReturn.add(userList);
        }
        return toReturn;
    }

    public static ArrayList<MovieSearchResults> convertMapToList(FirebaseMovieRatings firebaseMovieRatings) {
        ArrayList<MovieSearchResults> toReturn = new ArrayList<>();
        Iterator it = firebaseMovieRatings.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            MovieSearchResults movieSearchResults = (MovieSearchResults) pair.getValue();
            toReturn.add(movieSearchResults);
        }
        return toReturn;
    }

    public static ArrayList<MovieSearchResults> convertMapToList(HashMap<String, MovieSearchResults> map) {
        ArrayList<MovieSearchResults> toReturn = new ArrayList<>();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            MovieSearchResults movieSearchResults = (MovieSearchResults) pair.getValue();
            toReturn.add(movieSearchResults);
        }
        return toReturn;
    }
}
