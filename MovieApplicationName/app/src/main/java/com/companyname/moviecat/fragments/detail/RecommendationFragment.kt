package com.companyname.moviecat.fragments.detail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import butterknife.ButterKnife
import com.companyname.movieapplicationname.R
import com.companyname.moviecat.adapters.MovieSearchResultAdapter
import com.companyname.moviecat.data.MovieApiManager
import com.companyname.moviecat.firebase.FirebaseMovieFavorites
import com.companyname.moviecat.firebase.FirebaseMovieRatings
import com.companyname.moviecat.firebase.MasterFavoriteList
import com.companyname.moviecat.firebase.MasterRatingsList
import com.companyname.moviecat.kotterknife.bindView
import com.companyname.moviecat.models.Callback
import com.companyname.moviecat.models.MovieSearchResults
import com.companyname.moviecat.models.retrofit.movie_find.Recommendation
import com.google.firebase.analytics.FirebaseAnalytics
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 * Use the [RecommendationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecommendationFragment : Fragment() {

    private var movieId: String? = null

    private var recommendations: List<MovieSearchResults>? = null

    private val rec: RecyclerView by bindView(R.id.recommendationRecyclerView)
    private val recommendationNoRecommendationContainer: RelativeLayout by bindView(R.id.recommendationNoRecommendationContainer)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            movieId = arguments.getString(MOVIE_ID)
        }

        FirebaseAnalytics.getInstance(activity).setCurrentScreen(activity, "RecommendationFragment", null /* class override */)

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        removeListeners()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_recommendation, container, false)
        ButterKnife.bind(view)

        val movieApiManager: MovieApiManager = MovieApiManager(context)

        fetchRecommendations(movieApiManager).always {
            activity.runOnUiThread(Runnable{
                setupAdapter(view)
            })
        }



        return view
    }


    /**
     * Network call to get recommendations
     */
    private fun fetchRecommendations(movieApiManager: MovieApiManager): Promise<List<MovieSearchResults>, Exception> {
        val deferred = deferred<List<MovieSearchResults>, Exception>()

        movieApiManager.getMovieRecommendations(movieId, object : Callback<Recommendation>() {
            override fun success(recommendation: Recommendation) {
                recommendations = recommendation.results
                Timber.d("returnDebug recommendations return")
                deferred.resolve(recommendations!!)

                if(recommendations?.size == 0){
                    recommendationNoRecommendationContainer.visibility = View.VISIBLE
                }


            }

            override fun failure(message: String?) {
                message?.let { deferred.reject(Exception(it)) }
            }

        })

        return deferred.promise
    }


    private fun removeListeners() {
        FirebaseMovieFavorites.deregisterFavoritesForCurrentUser(REG_ID)
        FirebaseMovieRatings.deregisterMovieRatingsForCurrentUser(REG_ID)
    }

    /**
     * setup adapter after everything completes
     */
    private fun setupAdapter(view: View) {
        val recycler: RecyclerView = view.findViewById(R.id.recommendationRecyclerView) as RecyclerView

        val mLayoutManager = LinearLayoutManager(context)
        rec.layoutManager = mLayoutManager
        val movieSearchResultAdapter: MovieSearchResultAdapter = MovieSearchResultAdapter(false,
                MasterRatingsList.getInstance().firebaseMovieRatingsList,
                MasterFavoriteList.getInstance().firebaseMovieFavoritesList, context, recommendations)
        rec.adapter = movieSearchResultAdapter
    }

    companion object {
        private val MOVIE_ID = "movie_id"
        private val REG_ID = "RECOMMENDATION_FRAGMENT"

        fun newInstance(movieID: String): RecommendationFragment {
            val fragment = RecommendationFragment()
            val args = Bundle()
            args.putString(MOVIE_ID, movieID)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
