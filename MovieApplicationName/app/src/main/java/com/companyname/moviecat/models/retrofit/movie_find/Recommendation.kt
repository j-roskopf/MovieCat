package com.companyname.moviecat.models.retrofit.movie_find

import com.companyname.moviecat.models.MovieSearchResults
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Joe on 6/18/2017.
 */

class Recommendation {

    @SerializedName("page")
    @Expose
    var page: Int? = null
    @SerializedName("results")
    @Expose
    var results: List<MovieSearchResults>? = null
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null
    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null

}