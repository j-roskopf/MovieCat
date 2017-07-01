package com.companyname.moviecat.events;

import com.companyname.moviecat.models.UserList;

/**
 * Created by Joe on 4/30/2017.
 */

public class ListViewEvent {
    public String listName;
    public UserList userList;

    public ListViewEvent(String listName, UserList userList){
        this.listName = listName;
        this.userList = userList;
    }
}
