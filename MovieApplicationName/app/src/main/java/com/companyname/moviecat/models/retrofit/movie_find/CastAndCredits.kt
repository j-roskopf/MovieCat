package com.companyname.moviecat.models.retrofit.movie_find

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Joe on 6/19/2017.
 */

class CastAndCredits {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("cast")
    @Expose
    var cast: List<Cast>? = null
    @SerializedName("crew")
    @Expose
    var crew: List<Crew>? = null

}