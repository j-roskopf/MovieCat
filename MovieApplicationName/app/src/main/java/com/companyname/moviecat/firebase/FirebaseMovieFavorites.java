package com.companyname.moviecat.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.companyname.moviecat.models.Callback;
import com.companyname.moviecat.models.MovieSearchResults;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import timber.log.Timber;

/**
 * Created by Joe on 3/26/2017.
 */

public class FirebaseMovieFavorites  extends HashMap<String,MovieSearchResults>{
    private static String TAG = "FAVORITES";
    private static String FB_NODE = "favorites";

    private static HashMap<String,ValueEventListener> registeredListeners = new HashMap<String,ValueEventListener>(1);

    private static String userId;

    public FirebaseMovieFavorites(){
        //Empty for firebase
    }

    private FirebaseMovieFavorites(HashMap<String, MovieSearchResults> events) {
        super(events);
    }

    /**
     * Register to receive the Movie Favorites as they are updated.
     *
     * @param registrationId unique registration ID to track the callback
     * @param callback callback object with success method to return MovioeFavorites as it is updated
     *                 or failure messages as they occur.
     */

    public static void registerForFavorites(final String registrationId, final Callback<FirebaseMovieFavorites> callback) {

        //Get the current user id:
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        FirebaseMovieFavorites.userId = firebaseUser.getUid();

        if (firebaseUser == null) {

            //There is no currently logged in user!
            callback.failure("User is not logged in!");
        }
        else {
            Timber.d("favoriteDebug going to register!");
            registerForFavorites(registrationId,firebaseUser,callback);
        }

    }

    /**
     * Remove registration to no longer receive HOSEvents as they are updated.
     * @param registrationId
     */

    public static void deregisterFavoritesForCurrentUser(final String registrationId) {

        // Pull the listener:
        ValueEventListener listener = registeredListeners.remove(registrationId);

        // Make sure we have one:
        if (listener != null) {

            //Get the current user id:
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser != null) {

                // Remove the event listener:
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child(FB_NODE).child(firebaseUser.getUid()).removeEventListener(listener);
            }
        }

    }

    public void add(MovieSearchResults movieSearchResults) {

        // Put the event in the list.
        super.put(String.valueOf(movieSearchResults.getId()), movieSearchResults);
        // Save the event list.
        save();
    }


    public int removeItemAndReturnPosition(Object o){
        int positionRemoved = -1;
        MovieSearchResults movieSearchResults = (MovieSearchResults)o;
        for(int i = 0; i < this.size(); i++){
            MovieSearchResults msr = this.get(i);
            if(msr.getId().equals(movieSearchResults.getId())){
                remove(i);
                positionRemoved = i;
                break;
            }
        }

        save();
        return positionRemoved;
    }

   /*
     * Private worker methods
     */

    private static void registerForFavorites(final String registrationId, final FirebaseUser firebaseUser, final Callback<FirebaseMovieFavorites> callback) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        // Instantiate our new listener:

        Timber.d("favoriteDebug with current ref = " + ref.toString());

        ValueEventListener newListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Timber.d("favoriteDebug data change!!! " + dataSnapshot.toString());

                boolean dirtyList = false;

                Timber.d("onDataChanged hosEventList " + dirtyList + " " + registeredListeners.size());

                GenericTypeIndicator<HashMap<String, MovieSearchResults>> t = new GenericTypeIndicator<HashMap<String, MovieSearchResults>>() {};

                HashMap<String, MovieSearchResults> list = dataSnapshot.getValue(t);
                FirebaseMovieFavorites firebaseMovieFavorites;

                if (list == null) {
                    firebaseMovieFavorites = new FirebaseMovieFavorites();
                    dirtyList = true;
                }
                else {
                    firebaseMovieFavorites = new FirebaseMovieFavorites(list);
                }

                firebaseMovieFavorites.userId = firebaseUser.getUid();

                if (dirtyList) {
                    firebaseMovieFavorites.save();
                }

                callback.success(firebaseMovieFavorites);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Timber.d("favoriteDebug errorrrrrrr!");
                // Log the database error:
                Log.w(TAG, "onDataChanged onCancelled: " + registrationId, new Exception(": ValueEventListener cancelled: " + databaseError.getMessage()) );
                // Remove this listener:
                ref.child(FB_NODE).child(firebaseUser.getUid()).removeEventListener(this);
                // Remove the record of registration:
                registeredListeners.remove(registrationId);
                // Send the error:
                callback.failure(databaseError.getMessage());

            }
        };

        Timber.d("onDataChanged size " + registeredListeners.size());

        // remove any old listeners for this id:
        ValueEventListener oldListener = registeredListeners.remove(registrationId);
        if (oldListener != null) {
            Timber.d("favoriteDebug old listener is not null!");
            ref.child(FB_NODE).child(firebaseUser.getUid()).removeEventListener(oldListener);
        }else{
            Timber.d("favoriteDebug old listener is null!");
            Timber.d("onDataChanged oldListener null");
        }


        // store the listener:
        registeredListeners.put(registrationId, newListener);

        Timber.d("favoriteDebug registering at " + FB_NODE+"/"+firebaseUser.getUid());
        // attach the listener:
        ref.child(FB_NODE).child(firebaseUser.getUid()).addValueEventListener(newListener);


    }

    String n;
    DatabaseReference number;
    DatabaseReference dataRef;

    public void save() {

        Timber.d("favoriteDebug saving at " + FB_NODE+"/"+this.userId);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(FB_NODE).child(this.userId).setValue(this);
        ref.child("FAVORITE_TEST").child("ALSO_TEST").setValue("ASDF").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Timber.d("favoriteDebug with task = " + task.isSuccessful());
            }
        });
    }
}
