package com.companyname.moviecat.models.retrofit.movie_find

/**
 * Created by Joe on 6/18/2017.
 */


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SpokenLanguage {

    @SerializedName("iso_639_1")
    @Expose
    var iso6391: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

}