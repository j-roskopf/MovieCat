package com.companyname.moviecat.models.retrofit.movie_find

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Joe on 6/19/2017.
 */

open class Person{
    @SerializedName("profile_path")
    @Expose
    var profilePath: Any? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("gender")
    @Expose
    var gender: Int? = null
    @SerializedName("credit_id")
    @Expose
    var creditId: String? = null
    @SerializedName("department")
    @Expose
    var department: String? = null
    @SerializedName("job")
    @Expose
    var job: String? = null
    @SerializedName("cast_id")
    @Expose
    var castId: Int? = null
    @SerializedName("character")
    @Expose
    var character: String? = null
    @SerializedName("order")
    @Expose
    var order: Int? = null

    var castOrCredit: String? = null
}