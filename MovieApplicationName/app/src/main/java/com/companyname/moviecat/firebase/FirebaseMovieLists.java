package com.companyname.moviecat.firebase;

import android.util.Log;

import com.companyname.moviecat.models.Callback;
import com.companyname.moviecat.models.UserList;
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
 * Created by OPI on 4/7/17.
 */

public class FirebaseMovieLists  extends HashMap<String, UserList>{
    private static String TAG = "USER_LIST";
    private static String FB_NODE = "user_list";

    private static HashMap<String,ValueEventListener> registeredListeners = new HashMap<String,ValueEventListener>(1);

    private String userId;

    public FirebaseMovieLists(){
        //Empty for firebase
    }

    private FirebaseMovieLists(HashMap<String, UserList> events) {
        super(events);
    }

    /**
     * Register to receive the Movie Favorites as they are updated.
     *
     * @param registrationId unique registration ID to track the callback
     * @param callback callback object with success method to return MovioeFavorites as it is updated
     *                 or failure messages as they occur.
     */

    public static void registerForUserList(final String registrationId, final Callback<FirebaseMovieLists> callback) {

        //Get the current user id:
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {

            //There is no currently logged in user!
            callback.failure("User is not logged in!");
        }
        else {

            registerForUserList(registrationId,firebaseUser,callback);
        }

    }

    /**
     * Remove registration to no longer receive HOSEvents as they are updated.
     * @param registrationId
     */

    public static void deregisterMovieListsForCurrentUser(final String registrationId) {

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

    public void add(UserList userList) {
        super.put(userList.getListName(), userList);
        save();
    }


   /*
     * Private worker methods
     */

    private static void registerForUserList(final String registrationId, final FirebaseUser firebaseUser, final Callback<FirebaseMovieLists> callback) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        // Instantiate our new listener:

        ValueEventListener newListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean dirtyList = false;

                Timber.d("onDataChanged hosEventList " + dirtyList + " " + registeredListeners.size());

                GenericTypeIndicator<HashMap<String, UserList>> t = new GenericTypeIndicator<HashMap<String, UserList>>() {};

                HashMap<String, UserList> list = dataSnapshot.getValue(t);
                FirebaseMovieLists FirebaseMovieLists;

                if (list == null) {
                    FirebaseMovieLists = new FirebaseMovieLists();
                    dirtyList = true;
                }
                else {
                    FirebaseMovieLists = new FirebaseMovieLists(list);
                }

                FirebaseMovieLists.userId = firebaseUser.getUid();

                if (dirtyList) {
                    FirebaseMovieLists.save();
                }

                callback.success(FirebaseMovieLists);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
            Timber.d("onDataChanged oldListener not null");
            ref.child(FB_NODE).child(firebaseUser.getUid()).removeEventListener(oldListener);
        }else{
            Timber.d("onDataChanged oldListener null");
        }


        // store the listener:
        registeredListeners.put(registrationId, newListener);

        // attach the listener:
        ref.child(FB_NODE).child(firebaseUser.getUid()).addValueEventListener(newListener);


    }

    public void save() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(FB_NODE).child(this.userId).setValue(this);
    }
}