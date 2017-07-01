package com.companyname.moviecat.data;

import android.content.Context;

import com.companyname.moviecat.fragments.HomeFragment;
import com.companyname.moviecat.models.MovieSearchResultsList;
import com.companyname.moviecat.models.retrofit.movie_find.CastAndCredits;
import com.companyname.moviecat.models.retrofit.movie_find.Movie;
import com.companyname.moviecat.models.retrofit.movie_find.MovieImage;
import com.companyname.moviecat.models.retrofit.movie_find.Recommendation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import timber.log.Timber;

/**
 * Created by Geoffrey on 3/25/17.
 */

public class MovieApiManager {

    private final MovieService movieService;
    private Context context;
    private HomeFragment fragment;

    public MovieApiManager(Context context, HomeFragment fragment) {
        this.context = context;
        this.fragment = fragment;

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Const.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        movieService = retrofit.create(MovieService.class);
    }

    public MovieApiManager(Context context) {
        this.context = context;

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Const.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        movieService = retrofit.create(MovieService.class);
    }

    public interface MovieService {
        @GET("/3/search/movie")
        Call<MovieSearchResultsList> searchByTitle(@Query("query") String query, @Query("api_key") String api_key);

        @GET("/3/movie/{id}")
        Call<Movie> getMovie(@Path("id") String id, @Query("api_key") String api_key);

        @GET("/3/movie/{id}/images")
        Call<MovieImage> getMovieImages(@Path("id") String id, @Query("api_key") String api_key);

        @GET("/3/movie/{id}/recommendations")
        Call<Recommendation> getMovieRecommendations(@Path("id") String id, @Query("api_key") String api_key);

        @GET("/3/movie/{id}/credits")
        Call<CastAndCredits> getCastAndCredits(@Path("id") String id, @Query("api_key") String api_key);
    }

    /**
     * Search for movie by title
     *
     * @param title
     */
    public void searchByTitle(String title) {
        Timber.d("resultDebug calling search " + title);
        movieService.searchByTitle(title, Const.API_KEY).enqueue(new Callback<MovieSearchResultsList>() {
            @Override
            public void onResponse(Call<MovieSearchResultsList> call, Response<MovieSearchResultsList> response) {
                fragment.displaySearchResults(response.body());
            }

            @Override
            public void onFailure(Call<MovieSearchResultsList> call, Throwable t) {
                Timber.d("searchFailed with message " + t.getLocalizedMessage());
            }
        });

    }


    /**
     * Get movie by id
     *
     * @param id
     */
    public void getMovie(String id, final com.companyname.moviecat.models.Callback<Movie> movieCallback) {
        try {
            String encodedId = URLEncoder.encode(id, "UTF-8");
            Timber.d("resultDebug calling find by id");
            movieService.getMovie(encodedId, Const.API_KEY).enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    movieCallback.success(response.body());
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    movieCallback.failure(t.getLocalizedMessage());
                }
            });

        } catch (UnsupportedEncodingException e) {
            Timber.d("resultDebug with exception = " + e.getLocalizedMessage());
        }
    }

    /**
     * Get movie by id
     *
     * @param id
     */
    public void getMovieImages(String id, final com.companyname.moviecat.models.Callback<MovieImage> movieCallback) {
        try {
            String encodedId = URLEncoder.encode(id, "UTF-8");
            Timber.d("resultDebug calling images find by id");
            movieService.getMovieImages(encodedId, Const.API_KEY).enqueue(new Callback<MovieImage>() {
                @Override
                public void onResponse(Call<MovieImage> call, Response<MovieImage> response) {
                    movieCallback.success(response.body());
                }

                @Override
                public void onFailure(Call<MovieImage> call, Throwable t) {
                    movieCallback.failure(t.getLocalizedMessage());
                }
            });

        } catch (UnsupportedEncodingException e) {
            Timber.d("resultDebug with exception = " + e.getLocalizedMessage());
        }
    }

    /**
     * Get movie recommendations
     *
     * @param id
     */
    public void getMovieRecommendations(String id, final com.companyname.moviecat.models.Callback<Recommendation> movieCallback) {
        try {
            String encodedId = URLEncoder.encode(id, "UTF-8");
            Timber.d("resultDebug calling images find by id");
            movieService.getMovieRecommendations(encodedId, Const.API_KEY).enqueue(new Callback<Recommendation>() {
                @Override
                public void onResponse(Call<Recommendation> call, Response<Recommendation> response) {
                    movieCallback.success(response.body());
                }

                @Override
                public void onFailure(Call<Recommendation> call, Throwable t) {
                    movieCallback.failure(t.getLocalizedMessage());
                }
            });

        } catch (UnsupportedEncodingException e) {
            Timber.d("resultDebug with exception = " + e.getLocalizedMessage());
        }
    }

    /**
     * Get movie cast and credits
     *
     * @param id
     */
    public void getMovieCastAndCredits(String id, final com.companyname.moviecat.models.Callback<CastAndCredits> castAndCreditsCallback) {
        movieService.getCastAndCredits(id, Const.API_KEY).enqueue(new Callback<CastAndCredits>() {
            @Override
            public void onResponse(Call<CastAndCredits> call, Response<CastAndCredits> response) {
                castAndCreditsCallback.success(response.body());
            }

            @Override
            public void onFailure(Call<CastAndCredits> call, Throwable t) {
                castAndCreditsCallback.failure(t.getLocalizedMessage());
            }
        });


    }
}
