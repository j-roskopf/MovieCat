package com.companyname.moviecat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.companyname.moviecat.adapters.MovieSearchResultAdapter;
import com.companyname.moviecat.animations.CatTransition;
import com.companyname.moviecat.data.MovieApiManager;
import com.companyname.moviecat.events.ProgressEvent;
import com.companyname.moviecat.firebase.FirebaseMovieLists;
import com.companyname.moviecat.firebase.MasterFavoriteList;
import com.companyname.moviecat.firebase.MasterRatingsList;
import com.companyname.moviecat.models.MovieSearchResults;
import com.companyname.moviecat.models.MovieSearchResultsList;
import com.moviecat.joe.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

    @BindView(R.id.homeSearchView)
    FloatingSearchView homeSearchView;

    @BindView(R.id.homeRecyclerView)
    RecyclerView homeRecyclerView;

    String searchTerm;

    MovieApiManager movieApiManager;

    FirebaseMovieLists firebaseMovieLists;

    MovieSearchResultAdapter movieSearchResultAdapter;

    /**
     * Holds the current clicked on movie when adding to list
     */
    MovieSearchResults movieSearchResults;

    public HomeFragment() {
        // Required empty public constructor
        searchTerm = "";
        movieApiManager = new MovieApiManager(getContext(), this);

        this.setEnterTransition(CatTransition.slideFromLeft());
        this.setAllowEnterTransitionOverlap(false);
        this.setAllowReturnTransitionOverlap(false);
        this.setExitTransition(CatTransition.slideFromRight());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootview);

        setupSearchView();

        return rootview;
    }

    /**
     * Setup logic for search view
     */
    private void setupSearchView() {
        homeSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {
                search();
            }

            @Override
            public void onMenuClosed() {

            }
        });

        homeSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                //Keep track of search term
                searchTerm = newQuery;
                //get suggestions based on newQuery

                //pass them on to the search view
                //homeSearchView.swapSuggestions(newSuggestions);
            }
        });

        homeSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                searchTerm = currentQuery;
                search();
            }
        });
    }

    /**
     * Makes sure input is not blank, and if not, search!
     */
    private void search() {
        if (TextUtils.isEmpty(searchTerm)) {
            Toast.makeText(getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
        } else {
            EventBus.getDefault().post(new ProgressEvent(true));
            movieApiManager.searchByTitle(searchTerm);
        }
    }

    /**
     * Called by API manager with results
     */
    public void displaySearchResults(MovieSearchResultsList movieSearchResultsList) {
        if (movieSearchResultsList != null && movieSearchResultsList.getResults() != null && movieSearchResultsList.getResults().size() > 0) {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            homeRecyclerView.setLayoutManager(mLayoutManager);
            homeRecyclerView.setItemAnimator(new DefaultItemAnimator());

            movieSearchResultAdapter = new MovieSearchResultAdapter(false,
                    MasterRatingsList.getInstance().getFirebaseMovieRatingsList(),
                    MasterFavoriteList.getInstance().getFirebaseMovieFavoritesList(), getContext(), movieSearchResultsList.getResults());
            homeRecyclerView.setAdapter(movieSearchResultAdapter);

            EventBus.getDefault().post(new ProgressEvent(false));

        } else {

            Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();

            EventBus.getDefault().post(new ProgressEvent(false));
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
