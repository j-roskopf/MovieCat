package com.companyname.moviecat.models.retrofit.movie_find

/**
 * Created by Joe on 6/18/2017.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductionCompany {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null

}