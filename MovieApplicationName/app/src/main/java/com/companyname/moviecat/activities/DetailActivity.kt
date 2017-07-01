package com.companyname.moviecat.activities

import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Toast
import butterknife.ButterKnife
import com.companyname.movieapplicationname.R
import com.companyname.moviecat.adapters.MovieSearchResultAdapter
import com.companyname.moviecat.adapters.detail.DetailViewFragmentViewPagerAdapter
import com.companyname.moviecat.adapters.detail.DetailViewImageViewPagerAdapter
import com.companyname.moviecat.data.MovieApiManager
import com.companyname.moviecat.fragments.detail.CastAndCreditFragment
import com.companyname.moviecat.fragments.detail.InfoFragment
import com.companyname.moviecat.fragments.detail.RecommendationFragment
import com.companyname.moviecat.kotterknife.bindView
import com.companyname.moviecat.models.Callback
import com.companyname.moviecat.models.MovieSearchResults
import com.companyname.moviecat.models.retrofit.movie_find.Backdrop
import com.companyname.moviecat.models.retrofit.movie_find.Image
import com.companyname.moviecat.models.retrofit.movie_find.Poster
import com.mancj.slideup.SlideUp
import me.relex.circleindicator.CircleIndicator

class DetailActivity : AppCompatActivity() {

    /**
     * UI
     */
    private val detailViewFloatingActionButton: FloatingActionButton by bindView(R.id.detailViewFloatingActionButton)
    private val detailViewViewPager: ViewPager by bindView(R.id.detailViewViewPager)
    private val detailViewFragmentViewPager: ViewPager by bindView(R.id.detailViewFragmentViewPager)
    private val detailViewTabLayout: TabLayout by bindView(R.id.detailViewTabLayout)
    private val detailViewViewPagerIndicator: CircleIndicator by bindView(R.id.detailViewViewPagerIndicator)
    private val ratingView: RelativeLayout by bindView(R.id.ratingView)
    private val dimView: FrameLayout by bindView(R.id.dimView)
    private val detailViewAppBarLayout: AppBarLayout by bindView(R.id.detailViewAppBarLayout)
    private val toolbar: Toolbar by bindView(R.id.toolbar)

    private lateinit var context: Context

    private lateinit var slideUp: SlideUp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        ButterKnife.bind(this)

        context = this

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movie: MovieSearchResults = intent.extras.getParcelable<MovieSearchResults>(MovieSearchResultAdapter.MOVIE)

        val movieApiManager: MovieApiManager = MovieApiManager(this)

        fetchDetails(movie.id, movieApiManager)

        fetchImages(movie.id, movieApiManager)

        fetchCastAndCredits(movie.id, movieApiManager)

        displayMovie(movie)

        prepareRatingView()

        setupFab()

        setupFragmentViewPager(movie.id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Display movie
     */
    private fun displayMovie(movieSearchResults: MovieSearchResults) {
        displayTitle(movieSearchResults.title)
    }

    /**
     * Set title in toolbar
     */
    private fun displayTitle(title: String?) {
        supportActionBar?.title = title
    }

    /**
     * Makes call to get movie details
     */
    private fun fetchDetails(id: Int, movieApiManager: MovieApiManager) {

        movieApiManager.getMovie(java.lang.String.valueOf(id), object : Callback<com.companyname.moviecat.models.retrofit.movie_find.Movie>() {
            override fun success(movie: com.companyname.moviecat.models.retrofit.movie_find.Movie) {
                //Toast.makeText(context, movie.title, Toast.LENGTH_SHORT).show()
            }

            override fun failure(message: String?) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Fetch images for movie
     */
    private fun fetchImages(id: Int, movieApiManager: MovieApiManager) {
        movieApiManager.getMovieImages(java.lang.String.valueOf(id), object : Callback<com.companyname.moviecat.models.retrofit.movie_find.MovieImage>() {
            override fun success(movie: com.companyname.moviecat.models.retrofit.movie_find.MovieImage) {
                setupImageViewPager(movie.posters, movie.backdrops)
            }

            override fun failure(message: String?) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchCastAndCredits(id: Int, movieApiManager: MovieApiManager) {
    }

    /**
     * Display image
     */
    private fun setupImageViewPager(posters: List<Poster>?, backdrops: List<Backdrop>?) {
        val toPass: ArrayList<Image> = ArrayList()

        if (posters != null) {
            toPass.addAll(posters)
        }

        if (backdrops != null) {
            toPass.addAll(backdrops)
        }

        val detailViewImageViewPagerAdapter: DetailViewImageViewPagerAdapter = DetailViewImageViewPagerAdapter(this, toPass)
        detailViewViewPager.adapter = detailViewImageViewPagerAdapter
        detailViewViewPagerIndicator.setViewPager(detailViewViewPager)
    }

    /**
     * Setup fragment viewpager
     */
    private fun setupFragmentViewPager(id: Int) {
        val adapter: DetailViewFragmentViewPagerAdapter = DetailViewFragmentViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(InfoFragment(), "Information")
        adapter.addFragment(CastAndCreditFragment.newInstance(java.lang.String.valueOf(id)), "Cast")
        adapter.addFragment(RecommendationFragment.newInstance(java.lang.String.valueOf(id)), "Recommendation")
        detailViewFragmentViewPager.adapter = adapter
        detailViewFragmentViewPager.offscreenPageLimit = 3

        detailViewTabLayout.setupWithViewPager(detailViewFragmentViewPager)
    }

    private fun prepareRatingView() {
        slideUp = SlideUp.Builder(ratingView)
                .withListeners(object : SlideUp.Listener {
                    override fun onSlide(percent: Float) {
                        dimView.alpha = 1 - percent / 100
                    }

                    override fun onVisibilityChanged(visibility: Int) {
                        if (visibility == View.VISIBLE) {
                            detailViewFloatingActionButton.visibility = View.GONE
                            detailViewAppBarLayout.setExpanded(false)
                        } else {
                            detailViewFloatingActionButton.visibility = View.VISIBLE
                            detailViewAppBarLayout.setExpanded(true)
                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build()

    }


    /**
     * OnClick for floatingActionButton
     */
    private fun setupFab() {
        detailViewFloatingActionButton.setOnClickListener {
            slideUp.show()
        }
    }
}
