package com.companyname.moviecat.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.companyname.moviecat.adapters.MyListAdapter;
import com.companyname.moviecat.animations.CatTransition;
import com.companyname.moviecat.data.Const;
import com.companyname.moviecat.events.RefreshUserListEvent;
import com.companyname.moviecat.firebase.FirebaseMovieLists;
import com.companyname.moviecat.firebase.MasterUserList;
import com.companyname.moviecat.models.Callback;
import com.companyname.moviecat.models.UserList;
import com.companyname.moviecat.util.MapUtil;
import com.moviecat.joe.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by OPI on 4/7/17.
 */

public class MyListFragment extends Fragment {

    private static final String REG_ID = "MY_LIST_FRAGMENT";

    View baseView;

    @BindView(R.id.myListRecyclerView)
    RecyclerView myListRecyclerView;

    ArrayList<UserList> firebaseMovieLists;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MyListFragment() {
        this.setEnterTransition(CatTransition.slideFromLeft());
        this.setAllowEnterTransitionOverlap(false);
        this.setAllowReturnTransitionOverlap(false);
        this.setExitTransition(CatTransition.slideFromRight());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        baseView = inflater.inflate(R.layout.fragment_my_list, container, false);
        ButterKnife.bind(this, baseView);

        setupAdapter(MasterUserList.getInstance().getFirebaseMovieListslist());

        return baseView;
    }

    private void setupAdapter(ArrayList<UserList> firebaseMovieLists){

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        myListRecyclerView.setLayoutManager(mLayoutManager);
        myListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        myListRecyclerView.setAdapter(new MyListAdapter(false, firebaseMovieLists, getContext()));
    }


    @Override
    public void onResume() {
        super.onResume();
        FirebaseMovieLists.registerForUserList(REG_ID, new Callback<FirebaseMovieLists>() {
            @Override
            public void success(FirebaseMovieLists list) {
                firebaseMovieLists = MapUtil.convertMapToList(list);
                setupAdapter(firebaseMovieLists);

                //let main activity know to refresh the user list!
                EventBus.getDefault().post(new RefreshUserListEvent(true));
            }

            @Override
            public void failure(@Nullable String message) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Check if list name already exists
     * @param listToAdd
     * @return
     */
    private boolean listNameAlreadyExists(String listToAdd){
        boolean toReturn = false;

        if(firebaseMovieLists != null){
            for(UserList userListItem: firebaseMovieLists){
                Timber.d("listDebug comparing " + userListItem.getListName() + " " + listToAdd);

                if(userListItem.getListName().equals(listToAdd)){
                    toReturn = true;
                }
            }
        }

        if(toReturn){
            Snackbar.make(baseView.findViewById(R.id.myListBaseLayout),
                    "List name already exists", Snackbar.LENGTH_SHORT).show();
        }

        return toReturn;
    }

    /**
     * Add list to user defined lists
     * @param list
     */
    private void addList(String list){
        UserList userList = new UserList();
        userList.setListName(list);

        MasterUserList.getInstance().add(userList);
        MasterUserList.getInstance().save();

        //show the user it was added!
        setupAdapter(MasterUserList.getInstance().getFirebaseMovieListslist());
    }

    @OnClick(R.id.myListFloatingActionButton)
    public void myListFloatingActionButtonClicked(){

        final EditText input = new EditText(getContext());
        input.setSingleLine();
        input.setHint("Enter a list name");
        input.setTextColor(ContextCompat.getColor(getContext(), R.color.md_white_1000));
        FrameLayout container = new FrameLayout(getContext());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.leftMargin = Const.dpToPx(16);
        params.rightMargin = Const.dpToPx(16);

        input.setLayoutParams(params);
        container.addView(input);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                .setTitle("Add List")
                .setMessage("Enter a name for your list!")
                .setView(container)

                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(!listNameAlreadyExists(input.getText().toString())){
                            addList(input.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create();

        alertDialog.show();
    }
}
