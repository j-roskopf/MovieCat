package com.companyname.moviecat.models

/**
 * Created by Joe on 8/6/2017.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewResult {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("author")
    @Expose
    var author: String? = null
    @SerializedName("content")
    @Expose
    var content: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null

}
