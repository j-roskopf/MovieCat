package com.companyname.moviecat.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.widget.FrameLayout
import butterknife.ButterKnife
import com.companyname.movieapplicationname.R
import com.companyname.moviecat.adapters.MovieSearchResultAdapter
import com.companyname.moviecat.adapters.MyListAdapter
import com.companyname.moviecat.data.MovieApiManager
import com.companyname.moviecat.events.AddListEvent
import com.companyname.moviecat.events.RatingListEvent
import com.companyname.moviecat.firebase.MasterFavoriteList
import com.companyname.moviecat.firebase.MasterRatingsList
import com.companyname.moviecat.firebase.MasterUserList
import com.companyname.moviecat.fragments.detail.InfoFragment
import com.companyname.moviecat.kotterknife.bindView
import com.companyname.moviecat.models.Callback
import com.companyname.moviecat.models.MovieSearchResults
import com.companyname.moviecat.models.MovieSearchResultsList
import com.companyname.moviecat.util.ListUtil
import com.google.firebase.analytics.FirebaseAnalytics
import com.mancj.slideup.SlideUp
import kotlinx.android.synthetic.main.rating_view.*
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SimilarViewActivity : AppCompatActivity() {

    private var movieId: String? = null

    lateinit var slideUp: SlideUp
    lateinit var movieSearchResults: MovieSearchResults

    private var similarMovieList: List<MovieSearchResults>? = null

    private val similarViewRecyclerView: RecyclerView by bindView(R.id.similarViewRecyclerView)
    private val dimView: FrameLayout by bindView(R.id.dimView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_similar_view)
        ButterKnife.bind(this)

        FirebaseAnalytics.getInstance(this).setCurrentScreen(this, "SimilarViewActivity", null /* class override */)

        if(intent.extras.containsKey(InfoFragment.Companion.MOVIE_ID)){
            val movieApiManager: MovieApiManager = MovieApiManager(this)

            movieId = intent.extras[InfoFragment.Companion.MOVIE_ID] as String

            fetchSimilar(movieApiManager)

            setupSlideView()
        }else{
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()

        EventBus.getDefault().unregister(this)
    }

    /**
     * Network call to get recommendations
     */
    private fun fetchSimilar(movieApiManager: MovieApiManager): Promise<MovieSearchResultsList?, Exception> {
        val deferred = deferred<MovieSearchResultsList?, Exception>()

        movieApiManager.getSimilarMovies(movieId, object: Callback<MovieSearchResultsList>() {
            override fun success(t: MovieSearchResultsList?) {
                deferred.resolve(t)
                setupAdapter(t)

                setupSlideRecyclerView()
            }

            override fun failure(message: String?) {

            }

        })

        return deferred.promise
    }

    /**
     * Called after fetch returns with results
     */
    private fun setupAdapter(t: MovieSearchResultsList?) {
        val mLayoutManager = LinearLayoutManager(this)
        similarViewRecyclerView.layoutManager = mLayoutManager
        val movieSearchResultAdapter: MovieSearchResultAdapter = MovieSearchResultAdapter(false,
                MasterRatingsList.getInstance().firebaseMovieRatingsList,
                MasterFavoriteList.getInstance().firebaseMovieFavoritesList, this, t?.results)
        similarViewRecyclerView.adapter = movieSearchResultAdapter
    }

    private fun setupSlideView() {
        slideUp = SlideUp.Builder(ratingView)
                .withListeners(object : SlideUp.Listener {
                    override fun onSlide(percent: Float) {
                        dimView.setAlpha(1 - percent / 100)
                    }

                    override fun onVisibilityChanged(visibility: Int) {}
                })
                .withStartGravity(Gravity.BOTTOM)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: RatingListEvent) {
        if (event.show) {
            movieSearchResults = event.movieSearchResults
            slideUp.show()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(addListEvent: AddListEvent) {
        if (movieSearchResults != null)
            ListUtil.addMovieToList(dimView, addListEvent.userList.listName, movieSearchResults, MasterUserList.getInstance().firebaseMovieListslist)
    }

    /**
     * Prepares a list of all the users list to be displayed in the slide up view
     */
    private fun setupSlideRecyclerView() {
        val mLayoutManager = LinearLayoutManager(this)
        ratingList.layoutManager = mLayoutManager
        ratingList.itemAnimator = DefaultItemAnimator()
        ratingList.adapter = MyListAdapter(true,
                MasterUserList.getInstance().firebaseMovieListslist,
                this)
    }
}
