package com.companyname.moviecat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.companyname.moviecat.activities.BaseActivity;
import com.companyname.moviecat.activities.SignInActivity;
import com.companyname.moviecat.adapters.MyListAdapter;
import com.companyname.moviecat.events.AddListEvent;
import com.companyname.moviecat.events.ListViewEvent;
import com.companyname.moviecat.events.MovieRatingEvent;
import com.companyname.moviecat.events.ProgressEvent;
import com.companyname.moviecat.events.RatingListEvent;
import com.companyname.moviecat.events.RefreshUserListEvent;
import com.companyname.moviecat.firebase.FirebaseMovieLists;
import com.companyname.moviecat.firebase.MasterFavoriteList;
import com.companyname.moviecat.firebase.MasterRatingsList;
import com.companyname.moviecat.firebase.MasterUserList;
import com.companyname.moviecat.firebase.MasterWatchList;
import com.companyname.moviecat.fragments.FavoritesFragment;
import com.companyname.moviecat.fragments.HomeFragment;
import com.companyname.moviecat.fragments.ListViewFragment;
import com.companyname.moviecat.fragments.MyListFragment;
import com.companyname.moviecat.fragments.WatchedFragment;
import com.companyname.moviecat.models.Callback;
import com.companyname.moviecat.models.MovieSearchResults;
import com.companyname.moviecat.util.ListUtil;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.mancj.slideup.SlideUp;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.moviecat.joe.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    private static final String REG_ID = "MAIN_ACTIVITY";

    private static final int SETTINGS_ITEM_ID = 1234;
    private static final int WATCH_LIST_ITEM_ID = 1235;
    private static final int OWN_LIST_ITEM_ID = 1236;
    private static final int OTHER_LIST_ITEM_ID = 1237;
    private static final int FAVORITE_ITEM_ID = 1238;
    private static final int LOGOUT_ITEM_ID = 1239;
    private static final int HOME_ITEM_ID = 1240;

    /**
     * UI
     */
    @BindView(R.id.mainToolbar)
    Toolbar mainToolbar;

    @BindView(R.id.mainProgress)
    ProgressBar mainProgress;

    @BindView(R.id.ratingView)
    RelativeLayout ratingView;

    @BindView(R.id.dimView)
    FrameLayout dimView;

    @BindView(R.id.ratingList)
    RecyclerView ratingList;

    Drawer drawer;

    SlideUp slideUp;

    /**
     * NON UI
     */
    Context context;

    MovieSearchResults movieSearchResults;

    /**
     * Fragments
     */
    private HomeFragment homeFragment = new HomeFragment();
    private FavoritesFragment favoritesFragment = new FavoritesFragment();
    private WatchedFragment watchedFragment = new WatchedFragment();
    private MyListFragment myListFragment = new MyListFragment();
    private ListViewFragment listViewFragment = new ListViewFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());

        ButterKnife.bind(this);

        initVars();

        setupToolbar();

        setupFragments();

        setupDrawer();

        prepareRatingView();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Unregisters for favorite list
     */
    private void unregisterForFavoritesListener() {
        FirebaseMovieLists.deregisterMovieListsForCurrentUser(REG_ID);
    }

        /**
     * Initialize variables
     */
    private void initVars() {
        context = this;

        MasterFavoriteList.init(new Callback<Void>() {
            @Override
            public void success(Void aVoid) {

            }

            @Override
            public void failure(@Nullable String message) {

            }
        });

        Timber.d("masterRatings in initVars");

        MasterRatingsList.init(new Callback<Void>() {
            @Override
            public void success(Void aVoid) {
                Timber.d("masterRatings in main success");
            }

            @Override
            public void failure(@Nullable String message) {
                Timber.d("masterRatings in main fail");
            }
        });

        MasterUserList.init(new Callback<Void>() {
            @Override
            public void success(Void aVoid) {
                setupSlideRecyclerView();
            }

            @Override
            public void failure(@Nullable String message) {

            }
        });

        MasterWatchList.init(new Callback<Void>() {
            @Override
            public void success(Void aVoid) {

            }

            @Override
            public void failure(@Nullable String message) {

            }
        });
    }

    /**
     * Setup logic for toolbar
     */
    private void setupToolbar() {
        mainToolbar.setTitleTextColor(ContextCompat.getColor(context, android.R.color.white));
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_hamburger);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.app_name));

            mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer();
                }
            });
        }
    }

    private void prepareRatingView() {
        slideUp = new SlideUp.Builder(ratingView)
                .withListeners(new SlideUp.Listener() {
                    @Override
                    public void onSlide(float percent) {
                        dimView.setAlpha(1 - (percent / 100));
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();

    }

    private void resetToolbarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_hamburger);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.app_name));
        }
    }

    /**
     * Setup drawer
     */
    private void setupDrawer() {
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //now playing
        //popular
        //top rated
        //upcoming

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.material_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(username).withEmail(email).withIcon(R.drawable.ic_launcher)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        PrimaryDrawerItem homeItem = new PrimaryDrawerItem()
                .withIdentifier(HOME_ITEM_ID)
                .withIcon(R.drawable.ic_home)
                .withName("Home");

        PrimaryDrawerItem watchListeItem = new PrimaryDrawerItem()
                .withIdentifier(WATCH_LIST_ITEM_ID)
                .withIcon(R.drawable.ic_list_black_18dp)
                .withName("Watched List");

        PrimaryDrawerItem otherListItem = new PrimaryDrawerItem()
                .withIdentifier(OTHER_LIST_ITEM_ID)
                .withIcon(R.drawable.ic_list_black_18dp)
                .withName("My Lists");

        PrimaryDrawerItem favoritesItem = new PrimaryDrawerItem()
                .withIdentifier(FAVORITE_ITEM_ID)
                .withIcon(R.drawable.ic_favorite_black_18dp)
                .withName("Favorites");

        SecondaryDrawerItem logoutItem = new SecondaryDrawerItem()
                .withIdentifier(LOGOUT_ITEM_ID)
                .withIcon(R.drawable.ic_thumb_down_black_18dp)
                .withName("Logout");

        drawer = new DrawerBuilder()
                .withToolbar(mainToolbar)
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        homeItem,
                        watchListeItem,
                        otherListItem,
                        favoritesItem,
                        new DividerDrawerItem(),
                        logoutItem
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.executePendingTransactions();

                        switch ((int) drawerItem.getIdentifier()) {
                            case HOME_ITEM_ID:
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container, homeFragment)
                                        .commit();
                                break;
                            case FAVORITE_ITEM_ID:
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container, favoritesFragment)
                                        .commit();
                                break;
                            case OTHER_LIST_ITEM_ID:
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container, myListFragment)
                                        .commit();
                                break;
                            case LOGOUT_ITEM_ID:
                                logoutUser();
                                break;
                            case WATCH_LIST_ITEM_ID:
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container, watchedFragment)
                                        .commit();
                                break;
                        }

                        resetToolbarTitle();

                        return false;
                    }
                })
                .build();

    }

    private void setupFragments() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, homeFragment).commit();
        fm.executePendingTransactions();
    }

    /**
     * Log that user out
     */
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Prepares a list of all the users list to be displayed in the slide up view
     */
    private void setupSlideRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        ratingList.setLayoutManager(mLayoutManager);
        ratingList.setItemAnimator(new DefaultItemAnimator());
        ratingList.setAdapter(new MyListAdapter(true,
                MasterUserList.getInstance().getFirebaseMovieListslist(),
                this));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ProgressEvent progressEvent) {
        //triggers the main progress bar in the toolbar
        if (progressEvent.show) {
            mainProgress.setVisibility(View.VISIBLE);
        } else {
            mainProgress.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ListViewEvent listViewEvent) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(listViewEvent.listName);

        listViewFragment.setUserList(listViewEvent.userList, listViewEvent.listName);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.executePendingTransactions();
        fragmentManager.beginTransaction()
                .replace(R.id.container, listViewFragment).addToBackStack(ListViewFragment.REG_ID)
                .commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RatingListEvent event) {
        if (event.show) {
            movieSearchResults = event.movieSearchResults;
            slideUp.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddListEvent addListEvent) {
        if (movieSearchResults != null)
            ListUtil.addMovieToList(dimView, addListEvent.userList.getListName(), movieSearchResults, MasterUserList.getInstance().getFirebaseMovieListslist());
    }

    /**
     * Called when a new list is added in list fragment
     * @param refreshUserList
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshUserListEvent refreshUserList) {
        if (refreshUserList.getRefresh()){
            setupSlideRecyclerView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MovieRatingEvent movieRatingEvent) {
        Timber.d("onMessageEvent MainActivity with movieRatingEvent");

        if(MasterRatingsList.getInstance().getFirebaseMovieRatingsList() != null){
            if(!ListUtil.movieInList(MasterRatingsList.getInstance().getFirebaseMovieRatingsList(), movieRatingEvent.getMovieSearchResults())){
                Timber.d("onMessageEvent MainActivity with movieRatingEvent not in list");

                MasterRatingsList.getInstance().getFirebaseMovieFavorites().add(movieRatingEvent.getMovieSearchResults());
                MasterRatingsList.getInstance().getFirebaseMovieFavorites().save();
            }else{
                Timber.d("onMessageEvent MainActivity with movieRatingEvent in list");

                ListUtil.updateRating(MasterRatingsList.getInstance().getFirebaseMovieRatingsList(), movieRatingEvent.getMovieSearchResults());

                //the event is already updated in the list. just save it.
                MasterRatingsList.getInstance().getFirebaseMovieFavorites().save();
            }
        }
    }

}
