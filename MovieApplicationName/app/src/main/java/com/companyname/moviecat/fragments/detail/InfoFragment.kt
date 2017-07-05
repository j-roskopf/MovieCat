package com.companyname.moviecat.fragments.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.companyname.movieapplicationname.R
import com.companyname.moviecat.data.Const
import com.companyname.moviecat.data.MovieApiManager
import com.companyname.moviecat.kotterknife.bindView
import com.companyname.moviecat.models.Callback
import com.companyname.moviecat.models.retrofit.movie_find.Movie
import com.companyname.moviecat.models.retrofit.movie_find.SpokenLanguage
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

    /**
     * UI
     */
    private val infoFragmentVoteText: TextView by bindView(R.id.infoFragmentVoteText)
    private val infoFragmentVoteAverageText: TextView by bindView(R.id.infoFragmentVoteAverageText)
    private val infoFragmentIMDBContainer: LinearLayout by bindView(R.id.infoFragmentIMDBContainer)
    private val infoFragmentShareContainer: LinearLayout by bindView(R.id.infoFragmentShareContainer)
    private val infoFragmentOverViewText: TextView by bindView(R.id.infoFragmentOverViewText)
    private val infoFragmentRunTimeText: TextView by bindView(R.id.infoFragmentRunTimeText)
    private val infoFragmentBudgetText: TextView by bindView(R.id.infoFragmentBudgetText)
    private val infoFragmentRevenueText: TextView by bindView(R.id.infoFragmentRevenueText)
    private val infoFragmentSpokenLanguagesText: TextView by bindView(R.id.infoFragmentSpokenLanguagesText)
    private val infoFragmentReleasedText: TextView by bindView(R.id.infoFragmentReleasedText)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            movieId = arguments.getString(MOVIE_ID)
        }
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
        setupSpokenLanguages(movie.spokenLanguages)
        setupImdb(movie.imdbId)
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
    private fun  setupImdb(imdbId: String?) {
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
        private val MOVIE_ID = "movie_id"

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
        fun newInstance(movieId: String): InfoFragment {
            val fragment = InfoFragment()
            val args = Bundle()
            args.putString(MOVIE_ID, movieId)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
