package com.companyname.moviecat.fragments.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.companyname.moviecat.adapters.CastAndCreditsAdapter
import com.companyname.moviecat.data.MovieApiManager
import com.companyname.moviecat.kotterknife.bindView
import com.companyname.moviecat.models.Callback
import com.companyname.moviecat.models.retrofit.movie_find.Cast
import com.companyname.moviecat.models.retrofit.movie_find.CastAndCredits
import com.companyname.moviecat.models.retrofit.movie_find.Crew
import com.companyname.moviecat.models.retrofit.movie_find.Person
import com.moviecat.joe.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CastAndCreditFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CastAndCreditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CastAndCreditFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var movieId: String? = null
    private val castAndCreditRecyclerView: RecyclerView by bindView(R.id.castAndCreditRecyclerView)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            movieId = arguments.getString(MOVIE_ID)
        }
    }



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_cast_and_credit, container, false)

        fetchCastAndCredits()

        return view
    }

    private fun fetchCastAndCredits(){
        val movieApiManager: MovieApiManager = MovieApiManager(context)

        movieApiManager.getMovieCastAndCredits(movieId, object : Callback<CastAndCredits>() {
            override fun success(castAndCredits: CastAndCredits) {
                val people: ArrayList<Person> = ArrayList()

                if(castAndCredits.cast != null){
                    for(castPerson: Cast in castAndCredits.cast!!){
                        castPerson.castOrCredit = "Cast"
                        people.add(castPerson)
                    }
                }

                if(castAndCredits.crew != null){
                    for(crewPerson: Crew in castAndCredits.crew!!){
                        crewPerson.castOrCredit = "Crew"
                        people.add(crewPerson)
                    }
                }

                setupAdapter(people)

            }

            override fun failure(message: String?) {

            }

        })
    }

    private fun setupAdapter(people: ArrayList<Person>){

        val mLayoutManager = LinearLayoutManager(context)
        castAndCreditRecyclerView.layoutManager = mLayoutManager
        val adapter: CastAndCreditsAdapter = CastAndCreditsAdapter(context, people)
        castAndCreditRecyclerView.adapter = adapter
    }

    companion object {
        private val MOVIE_ID = "movie_id"


        fun newInstance(id: String): CastAndCreditFragment {
            val fragment = CastAndCreditFragment()
            val args = Bundle()
            args.putString(MOVIE_ID, id)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
