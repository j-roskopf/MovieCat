package com.companyname.moviecat.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by OPI on 4/7/17.
 */

public class UserList implements Parcelable {
    private String listName;

    HashMap<String, MovieSearchResults> movieSearchResults;

    public UserList(){
        //blank for firebase
        movieSearchResults = new HashMap<>();
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public HashMap<String, MovieSearchResults> getMovieSearchResults() {
        return movieSearchResults;
    }

    public void setMovieSearchResults(HashMap<String, MovieSearchResults> movieSearchResults) {
        this.movieSearchResults = movieSearchResults;
    }

    protected UserList(Parcel in) {
        listName = in.readString();
        movieSearchResults = (HashMap) in.readValue(HashMap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(listName);
        dest.writeValue(movieSearchResults);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserList> CREATOR = new Parcelable.Creator<UserList>() {
        @Override
        public UserList createFromParcel(Parcel in) {
            return new UserList(in);
        }

        @Override
        public UserList[] newArray(int size) {
            return new UserList[size];
        }
    };
}