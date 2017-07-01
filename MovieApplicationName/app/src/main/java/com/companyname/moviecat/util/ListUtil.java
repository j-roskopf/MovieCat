package com.companyname.moviecat.util;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.companyname.moviecat.firebase.FirebaseMovieFavorites;
import com.companyname.moviecat.firebase.MasterUserList;
import com.companyname.moviecat.models.MovieSearchResults;
import com.companyname.moviecat.models.UserList;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Joe on 4/30/2017.
 */

public class ListUtil {
    public static void addMovieToList(View view, String list, MovieSearchResults movie, ArrayList<UserList> firebaseMovieLists) {
        for (UserList userList : firebaseMovieLists) {
            if (userList.getListName().equals(list)) {
                if (!movieInList(MapUtil.convertMapToList(userList.getMovieSearchResults()), movie)) {
                    userList.getMovieSearchResults().put(String.valueOf(movie.getId()), movie);

                    MasterUserList.getInstance().add(userList);
                    MasterUserList.getInstance().save();
                    Snackbar.make(view, "Added!", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, "Movie already in that list", Snackbar.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    /**
     * Checks to see if a movie is already in the search result list
     *
     * @param movies
     * @param movieToCheck
     * @return
     */
    public static boolean movieInList(ArrayList<MovieSearchResults> movies, MovieSearchResults movieToCheck) {
        boolean toReturn = false;

        for (MovieSearchResults movie : movies) {
            if (movie.getId().equals(movieToCheck.getId())) {
                toReturn = true;
                break;
            }
        }

        return toReturn;
    }


    /**
     * Updates the rating in the rating list
     *
     * @param firebaseMovieRatings
     * @param movieSearchResults
     */
    public static void updateRating(ArrayList<MovieSearchResults> firebaseMovieRatings, MovieSearchResults movieSearchResults) {
        for (MovieSearchResults movie : firebaseMovieRatings) {
            if (movie.getId().equals(movieSearchResults.getId())) {
                movie.setUserRating(movieSearchResults.getUserRating());
                break;
            }
        }
    }

    /**
     * Removes movie from favorites
     *
     * @param firebaseMovieFavorites
     * @param movieSearchResults
     */
    public static void removeFavoriteFromList(FirebaseMovieFavorites firebaseMovieFavorites, MovieSearchResults movieSearchResults) {
        Timber.d("favoriteDebug removing from favorites");

        int positionToRemove = -1;
        for(int i = 0; i < firebaseMovieFavorites.size(); i++){
            if (firebaseMovieFavorites.get(i).getId().equals(movieSearchResults.getId())) {
                Timber.d("favoriteDebug found one");
                positionToRemove = i;
                break;
            }
        }

        if(positionToRemove != -1){
            firebaseMovieFavorites.remove(positionToRemove);
            firebaseMovieFavorites.save();
        }

    }
}
