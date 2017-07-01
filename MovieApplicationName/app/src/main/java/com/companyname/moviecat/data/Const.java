package com.companyname.moviecat.data;

import android.content.res.Resources;

/**
 * Created by Geoffrey on 3/25/17.
 */

public class Const {

    public static String API_KEY = "f1c9e9f42afb80bdef74821bef41b4ca";

    public static String API_BASE_URL = "http://api.themoviedb.org";

    public static String BASE_IMAGE_PATH = "https://image.tmdb.org/t/p/w500/";

    public static String BASE_PERSON_PATH = "https://image.tmdb.org/t/p/w500/";

    public static String IMDB_URL = "http://www.imdb.com/title/";

    public static String THE_MOVIE_DB_MOVIE_VIEW = "https://www.themoviedb.org/movie/";

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
