package com.companyname.moviecat.fragments;

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

import com.companyname.moviecat.adapters.MovieSearchResultAdapter;
import com.companyname.moviecat.animations.CatTransition;
import com.companyname.moviecat.firebase.MasterFavoriteList;
import com.companyname.moviecat.firebase.MasterRatingsList;
import com.companyname.moviecat.models.UserList;
import com.companyname.moviecat.util.MapUtil;
import com.moviecat.joe.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by OPI on 4/7/17.
 */

public class ListViewFragment extends Fragment {

    public static final String REG_ID = "LIST_VIEW_FRAGMENT";

    @BindView(R.id.listRecyclerView)
    RecyclerView listRecyclerView;

    @BindView(R.id.listNoMoviesContainer)
    RelativeLayout listNoMoviesContainer;

    private String listName;
    private UserList userList;

    public ListViewFragment() {
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
        View view = inflater.inflate(R.layout.activity_list_view, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    private void setupAdapter(){
        if(userList.getMovieSearchResults().size() == 0){
            listNoMoviesContainer.setVisibility(View.VISIBLE);
        }else{
            listNoMoviesContainer.setVisibility(View.GONE);
            listRecyclerView.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            listRecyclerView.setLayoutManager(mLayoutManager);
            listRecyclerView.setItemAnimator(new DefaultItemAnimator());
            listRecyclerView.setAdapter(new MovieSearchResultAdapter(false,
                    MasterRatingsList.getInstance().getFirebaseMovieRatingsList(),
                    MasterFavoriteList.getInstance().getFirebaseMovieFavoritesList(), getContext(), MapUtil.convertMapToList(userList.getMovieSearchResults())));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MasterFavoriteList.getInstance().getFirebaseMovieFavoritesList() != null && userList != null)
            setupAdapter();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void setUserList(UserList userList, String name){
        this.listName = name;
        this.userList = userList;
    }

}
