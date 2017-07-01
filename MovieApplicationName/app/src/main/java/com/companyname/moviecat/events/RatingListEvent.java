package com.companyname.moviecat.events;

import com.companyname.moviecat.models.MovieSearchResults;

/**
 * Created by OPI on 4/16/17.
 */

public class RatingListEvent {
    public final boolean show;
    public final MovieSearchResults movieSearchResults;

    public RatingListEvent(boolean show, MovieSearchResults movieSearchResults) {
        this.show = show;
        this.movieSearchResults = movieSearchResults;
    }
}
