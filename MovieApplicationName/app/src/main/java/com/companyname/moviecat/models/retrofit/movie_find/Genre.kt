package com.companyname.moviecat.models.retrofit.movie_find

/**
 * Created by Joe on 6/18/2017.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Genre {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

}