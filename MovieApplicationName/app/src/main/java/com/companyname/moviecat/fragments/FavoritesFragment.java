package com.companyname.moviecat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.companyname.movieapplicationname.R;
import com.companyname.moviecat.adapters.MovieSearchResultAdapter;
import com.companyname.moviecat.animations.CatTransition;
import com.companyname.moviecat.firebase.FirebaseMovieFavorites;
import com.companyname.moviecat.firebase.MasterFavoriteList;
import com.companyname.moviecat.firebase.MasterRatingsList;
import com.companyname.moviecat.models.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesFragment extends Fragment {

    private static final String REG_ID = "FAVORITES_ACTIVITY";

    @BindView(R.id.favoritesRecyclerView)
    RecyclerView favoritesRecyclerView;

    @BindView(R.id.favoritesNoFavoritesContainer)
    RelativeLayout favoritesNoFavoritesContainer;

    Context context;


    public FavoritesFragment() {
        this.setEnterTransition(CatTransition.slideFromLeft());
        this.setAllowEnterTransitionOverlap(false);
        this.setAllowReturnTransitionOverlap(false);
        this.setExitTransition(CatTransition.slideFromRight());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_favorites, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerForFavoritesListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterForFavoritesListener();
    }

    private void unregisterForFavoritesListener(){
        FirebaseMovieFavorites.deregisterFavoritesForCurrentUser(REG_ID);
    }

    private void registerForFavoritesListener(){
        FirebaseMovieFavorites.registerForFavorites(REG_ID, new Callback<FirebaseMovieFavorites>() {
            @Override
            public void success(FirebaseMovieFavorites movieSearchResultses) {
                setAdapter(movieSearchResultses);
            }

            @Override
            public void failure(@Nullable String message) {
            }
        });
    }

    private void setAdapter(FirebaseMovieFavorites firebaseMovieFavorites){

        if(firebaseMovieFavorites.size() == 0){
            favoritesNoFavoritesContainer.setVisibility(View.VISIBLE);
        }else{
            favoritesNoFavoritesContainer.setVisibility(View.GONE);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext()
            );
            favoritesRecyclerView.setLayoutManager(mLayoutManager);
            favoritesRecyclerView.setItemAnimator(new DefaultItemAnimator());
            favoritesRecyclerView.setAdapter(new MovieSearchResultAdapter(true,
                    MasterRatingsList.getInstance().getFirebaseMovieRatingsList(),
                    MasterFavoriteList.getInstance().getFirebaseMovieFavoritesList(), getContext(),
                    MasterFavoriteList.getInstance().getFirebaseMovieFavoritesList()));
        }
    }

}
