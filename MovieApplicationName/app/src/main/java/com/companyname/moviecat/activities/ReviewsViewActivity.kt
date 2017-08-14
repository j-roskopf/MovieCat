package com.companyname.moviecat.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.ButterKnife
import com.companyname.moviecat.adapters.ReviewAdapter
import com.companyname.moviecat.data.MovieApiManager
import com.companyname.moviecat.fragments.detail.InfoFragment
import com.companyname.moviecat.kotterknife.bindView
import com.companyname.moviecat.models.Callback
import com.companyname.moviecat.models.ReviewResults
import com.moviecat.joe.R
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred

class ReviewsViewActivity : AppCompatActivity() {

    private var movieId: String? = null

    private val ratingsViewRecyclerView: RecyclerView by bindView(R.id.ratingsViewRecyclerView)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews_view)

        ButterKnife.bind(this)

        if(intent.extras.containsKey(InfoFragment.MOVIE_ID)){
            val movieApiManager: MovieApiManager = MovieApiManager(this)

            movieId = intent.extras[InfoFragment.MOVIE_ID] as String

            fetchReviews(movieApiManager)

        }else{
            finish()
        }
    }

    /**
     * Network call to get recommendations
     */
    private fun fetchReviews(movieApiManager: MovieApiManager): Promise<ReviewResults?, Exception> {
        val deferred = deferred<ReviewResults?, Exception>()

        movieApiManager.getReviews(movieId, object: Callback<ReviewResults>() {
            override fun success(t: ReviewResults?) {
                deferred.resolve(t)
                setupAdapter(t)
            }

            override fun failure(message: String?) {

            }

        })

        return deferred.promise
    }

    /**
     * Called after fetch returns with results
     */
    private fun setupAdapter(t: ReviewResults?) {
        if (t != null) {
            val mLayoutManager = LinearLayoutManager(this)
            ratingsViewRecyclerView.layoutManager = mLayoutManager

            val reviewAdapter = ReviewAdapter(this, t)

            ratingsViewRecyclerView.adapter = reviewAdapter
        }
    }



}
