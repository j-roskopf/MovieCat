package com.companyname.moviecat.models.retrofit.movie_find

/**
 * Created by Joe on 6/18/2017.
 */


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieImage {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("backdrops")
    @Expose
    var backdrops: List<Backdrop>? = null
    @SerializedName("posters")
    @Expose
    var posters: List<Poster>? = null

}