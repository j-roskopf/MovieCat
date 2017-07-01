package com.companyname.moviecat.models.retrofit.movie_find

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Joe on 6/18/2017.
 */

open class Image{
    @SerializedName("aspect_ratio")
    @Expose
    var aspectRatio: Double? = null
    @SerializedName("file_path")
    @Expose
    var filePath: String? = null
    @SerializedName("height")
    @Expose
    var height: Int? = null
    @SerializedName("iso_639_1")
    @Expose
    var iso6391: String? = null
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null
    @SerializedName("vote_count")
    @Expose
    var voteCount: Int? = null
    @SerializedName("width")
    @Expose
    var width: Int? = null
}
