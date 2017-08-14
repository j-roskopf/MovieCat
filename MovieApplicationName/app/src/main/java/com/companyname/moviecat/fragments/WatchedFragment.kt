package com.companyname.moviecat.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.moviecat.joe.R
import com.companyname.moviecat.adapters.MovieSearchResultAdapter
import com.companyname.moviecat.firebase.MasterFavoriteList
import com.companyname.moviecat.firebase.MasterRatingsList
import com.companyname.moviecat.firebase.MasterWatchList
import com.companyname.moviecat.models.MovieSearchResults
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WatchedFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [WatchedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WatchedFragment : Fragment() {

    private lateinit var watchedRecyclerView: RecyclerView
    private lateinit var watchedNoWatchedContainer: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_watched, container, false)

        watchedRecyclerView = view.findViewById(R.id.watchedRecyclerView) as RecyclerView
        watchedNoWatchedContainer = view.findViewById(R.id.watchedNoWatchedContainer) as RelativeLayout


        fetchWatched()
        // Inflate the layout for this fragment
        return view
    }

    private fun fetchWatched() {
        setupAdapter(MasterWatchList.getInstance().firebaseMovieWatchList)
    }

    private fun setupAdapter(firebaseMovieWatchList: ArrayList<MovieSearchResults>) {
        if (firebaseMovieWatchList.size == 0) {
            watchedNoWatchedContainer.visibility = View.VISIBLE
        } else {
            watchedNoWatchedContainer.visibility = View.GONE

            val mLayoutManager = LinearLayoutManager(context)
            watchedRecyclerView.layoutManager = mLayoutManager
            watchedRecyclerView.itemAnimator = DefaultItemAnimator()
            watchedRecyclerView.adapter = MovieSearchResultAdapter(true,
                    MasterRatingsList.getInstance().firebaseMovieRatingsList,
                    MasterFavoriteList.getInstance().firebaseMovieFavoritesList, context,
                    MasterWatchList.getInstance().firebaseMovieWatchList)
        }

    }


}// Required empty public constructor
