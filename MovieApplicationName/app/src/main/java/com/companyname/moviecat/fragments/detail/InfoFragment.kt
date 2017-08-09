package com.companyname.moviecat.fragments.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.companyname.movieapplicationname.R
import com.companyname.moviecat.activities.ReviewsViewActivity
import com.companyname.moviecat.activities.SimilarViewActivity
import com.companyname.moviecat.data.Const
import com.companyname.moviecat.data.MovieApiManager
import com.companyname.moviecat.firebase.MasterFavoriteList
import com.companyname.moviecat.firebase.MasterWatchList
import com.companyname.moviecat.kotterknife.bindView
import com.companyname.moviecat.models.Callback
import com.companyname.moviecat.models.MovieSearchResults
import com.companyname.moviecat.models.retrofit.movie_find.Movie
import com.companyname.moviecat.models.retrofit.movie_find.SpokenLanguage
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber
import java.text.NumberFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InfoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var movieId: String? = null
    private var movieSearchResult: MovieSearchResults? = null

    /**
     * UI
     */
    private val infoFragmentVoteText: TextView by bindView(R.id.infoFragmentVoteText)
    private val infoFragmentVoteAverageText: TextView by bindView(R.id.infoFragmentVoteAverageText)
    private val infoFragmentIMDBContainer: LinearLayout by bindView(R.id.infoFragmentIMDBContainer)
    private val infoFragmentShareContainer: LinearLayout by bindView(R.id.infoFragmentShareContainer)
    private val infoFragmentWatchedContainer: LinearLayout by bindView(R.id.infoFragmentWatchedContainer)
    private val infoFragmentSimilarContainer: LinearLayout by bindView(R.id.infoFragmentSimilarContainer)
    private val infoFragmentFavoriteContainer: LinearLayout by bindView(R.id.infoFragmentFavoriteContainer)
    private val infoFragmentReviewContainer: LinearLayout by bindView(R.id.infoFragmentReviewContainer)
    private val infoFragmentOverViewText: TextView by bindView(R.id.infoFragmentOverViewText)
    private val infoFragmentRunTimeText: TextView by bindView(R.id.infoFragmentRunTimeText)
    private val infoFragmentBudgetText: TextView by bindView(R.id.infoFragmentBudgetText)
    private val infoFragmentRevenueText: TextView by bindView(R.id.infoFragmentRevenueText)
    private val infoFragmentSpokenLanguagesText: TextView by bindView(R.id.infoFragmentSpokenLanguagesText)
    private val infoFragmentReleasedText: TextView by bindView(R.id.infoFragmentReleasedText)
    private val infoFragmentWatchedText: TextView by bindView(R.id.infoFragmentWatchedText)
    private val infoFragmentFavoriteText: TextView by bindView(R.id.infoFragmentFavoriteText)
    private val infoFragmentWatchedIcon: ImageView by bindView(R.id.infoFragmentWatchedIcon)
    private val infoFragmentFavoriteImage: ImageView by bindView(R.id.infoFragmentFavoriteImage)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            movieId = arguments.getString(MOVIE_ID)
            movieSearchResult = arguments.getParcelable(MOVIE)
        }

        FirebaseAnalytics.getInstance(activity).setCurrentScreen(activity, "InfoFragment", null /* class override */)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_info, container, false)

        fetchDetails()

        return view

    }

    /**
     * Makes call to get movie details
     */
    private fun fetchDetails() {
        val movieApiManager: MovieApiManager = MovieApiManager(context)

        movieApiManager.getMovie(movieId, object : Callback<Movie>() {
            override fun success(movie: com.companyname.moviecat.models.retrofit.movie_find.Movie) {
                displayMovie(movie)
            }

            override fun failure(message: String?) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Display the movie info
     */
    private fun displayMovie(movie: Movie){
        Timber.d("movieInfoDebug " + movie)

        //setupGenres(movie.genres)
        //setupOriginalLanguages(movie.originalLanguage)
        //setupVideo
        setupFavoriteLogic()
        setupReviewButton(movie.id)
        setupSimilarButton(movie.id)
        setupSpokenLanguages(movie.spokenLanguages)
        setupImdb(movie.imdbId)
        setupWatchedButton()
        setupOverView(movie.overview)
        setupVoteAverage(movie.voteAverage)
        setupVoteCount(movie.voteCount)
        setupRevenue(movie.revenue)
        setupBudget(movie.budget)
        setupReleaseDate(movie.releaseDate)
        setupRuntime(movie.runtime)
        //setupPopularity(movie.popularity)
        //setupHomepage(movie.homepage)
        setupShare(movie.id, movie.title)
        //belongsToCollection
        //tagLine
    }

    private fun setupFavoriteLogic() {
        Timber.d("favoriteDebug")
        infoFragmentFavoriteContainer.setOnClickListener {

            if(!movieFavorited(movieSearchResult?.id ?: 0)){
                Timber.d("favoriteDebug is not favorited but is now")
                MasterFavoriteList.getInstance().firebaseMovieFavorites.add(movieSearchResult)
                MasterFavoriteList.getInstance().firebaseMovieFavorites.save()

                //set to fav state automatically.
                infoFragmentFavoriteImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_red_500_48dp))
                infoFragmentFavoriteText.text = "Favorited!"

            }else {
                Timber.d("favoriteDebug movie is favorited but now isnt")
                (0..MasterFavoriteList.getInstance().firebaseMovieFavoritesList.size - 1)
                        .map { MasterFavoriteList.getInstance().firebaseMovieFavoritesList[it] }
                        .filter { it.id == movieSearchResult?.id }
                        .forEach { MasterFavoriteList.getInstance().firebaseMovieFavorites.remove(it.id.toString())}

                MasterFavoriteList.getInstance().save()

                //set to un fav state
                infoFragmentFavoriteImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_black_48dp))
                infoFragmentFavoriteText.text = "Favorite?"
            }
        }

        setFavoriteButtonState()
    }

    private fun setFavoriteButtonState(){
        if(!movieFavorited(movieSearchResult?.id ?: 0)) {
            Timber.d("favoriteDebug in state set - not favorited " + movieSearchResult?.id)
            //not favorited - set logic to prompt the user to favorite it
            infoFragmentFavoriteImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_black_48dp))
            infoFragmentFavoriteText.text = "Favorite?"
        }else{
            Timber.d("favoriteDebug in state set - favorited " + movieSearchResult?.id)

            infoFragmentFavoriteImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_red_500_48dp))
            infoFragmentFavoriteText.text = "Favorited!"
        }
    }

    private fun movieFavorited(id: Int):Boolean {
        return MasterFavoriteList.getInstance().isMovieFavorited(id)
    }

    private fun setupReviewButton(id: Int?) {
        infoFragmentReviewContainer.setOnClickListener {
            var intent: Intent = Intent(activity, ReviewsViewActivity::class.java)
            intent.putExtra(MOVIE_ID, java.lang.String.valueOf(id))
            startActivity(intent)
        }
    }

    private fun setupSimilarButton(id: Int?) {
        //Just kick them over to the similar activity

        infoFragmentSimilarContainer.setOnClickListener {
            var intent: Intent = Intent(activity, SimilarViewActivity::class.java)
            intent.putExtra(MOVIE_ID, java.lang.String.valueOf(id))
            startActivity(intent)
        }

    }

    private fun setupWatchedButton() {
        infoFragmentWatchedContainer.setOnClickListener {
            if(movieWatched()){
                Timber.d("watchedDebug removing")
                MasterWatchList.getInstance().remove(movieSearchResult)
            }else{
                Timber.d("watchedDebug adding")
                MasterWatchList.getInstance().add(movieSearchResult)
            }
            setWatchedButtonState()
        }

        setWatchedButtonState()
    }

    private fun movieWatched(): Boolean {
        return !(MasterWatchList.getInstance().firebaseMovieWatched[java.lang.String.valueOf(movieSearchResult?.id)] == null)

    }

    private fun setWatchedButtonState() {
        if(!movieWatched()) {
            infoFragmentWatchedText.text = "Watched?"
            infoFragmentWatchedIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_help_outline_black_24dp))
        }else{
            infoFragmentWatchedText.text = "Watched!"
            infoFragmentWatchedIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_black_24dp))
        }
    }

    private fun setupReleaseDate(releaseDate: String?) {
        infoFragmentReleasedText.text = releaseDate
    }

    private fun  setupSpokenLanguages(spokenLanguages: List<SpokenLanguage>?) {
        val stringBuilder: StringBuilder = StringBuilder()

        //loop through all the languages
        if (spokenLanguages != null) {
            for(spokenLanguage: SpokenLanguage in spokenLanguages){
                stringBuilder.append(spokenLanguage.name)
                stringBuilder.append(", ")
            }

            if(stringBuilder.isNotEmpty()){
                //delete the space
                stringBuilder.deleteCharAt(stringBuilder.length - 1 )

                //delete the comma
                stringBuilder.deleteCharAt(stringBuilder.length - 1)
            }
        }

        infoFragmentSpokenLanguagesText.text = stringBuilder.toString()
    }

    private fun  setupBudget(budget: Int?) {
        val locale = Locale("en", "US")
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        infoFragmentBudgetText.text = currencyFormatter.format(budget)
    }

    private fun setupRevenue(revenue: Int?) {
        val locale = Locale("en", "US")
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        infoFragmentRevenueText.text = currencyFormatter.format(revenue)
    }

    private fun  setupRuntime(runtime: Int?) {
        infoFragmentRunTimeText.text = java.lang.String.valueOf(runtime) + " minutes"
    }

    /**
     * Set Overview
     */
    private fun  setupOverView(overview: String?) {
        infoFragmentOverViewText.text = overview
    }

    /**
     * Setup share button
     */
    private fun  setupShare(id: Int?, movieTitle: String?) {
        infoFragmentShareContainer.setOnClickListener({
            val intent: Intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, movieTitle)
            intent.putExtra(Intent.EXTRA_TEXT, Const.THE_MOVIE_DB_MOVIE_VIEW + id.toString())
            startActivity(Intent.createChooser(intent, "Check this out!"))
        })
    }

    /**
     * Set onclick for imdb icon
     */
    private fun setupImdb(imdbId: String?) {
        try {
            infoFragmentIMDBContainer.setOnClickListener(View.OnClickListener {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Const.IMDB_URL + imdbId)))
            })
        } catch (e: Exception) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * Set vote average
     */
    private fun  setupVoteAverage(voteAverage: Double?) {
        infoFragmentVoteAverageText.text = java.lang.String.valueOf(voteAverage)
    }

    /**
     * Set vote count
     */
    private fun  setupVoteCount(voteCount: Int?) {
        infoFragmentVoteText.text = java.lang.String.valueOf(voteCount)
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        val MOVIE_ID = "movie_id"
        private val MOVIE = "movie"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment InfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(movieId: String, movie: MovieSearchResults): InfoFragment {
            val fragment = InfoFragment()
            val args = Bundle()
            args.putString(MOVIE_ID, movieId)
            args.putParcelable(MOVIE, movie)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
