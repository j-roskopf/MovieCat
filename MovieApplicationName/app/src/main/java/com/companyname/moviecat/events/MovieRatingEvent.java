package com.companyname.moviecat.events;

import com.companyname.moviecat.models.MovieSearchResults;

/**
 * Created by OPI on 6/11/17.
 */

public class MovieRatingEvent {
    private MovieSearchResults movieSearchResults;

    public MovieRatingEvent(MovieSearchResults movieSearchResults ){
        this.movieSearchResults = movieSearchResults;
    }

    public MovieSearchResults getMovieSearchResults() {
        return movieSearchResults;
    }

    public void setMovieSearchResults(MovieSearchResults movieSearchResults) {
        this.movieSearchResults = movieSearchResults;
    }

}
