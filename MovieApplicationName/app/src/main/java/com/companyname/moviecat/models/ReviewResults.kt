package com.companyname.moviecat.models

/**
 * Created by Joe on 8/6/2017.
 */

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewResults {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("page")
    @Expose
    var page: Int? = null
    @SerializedName("results")
    @Expose
    var results: List<ReviewResult>? = null
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null
    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null

}
