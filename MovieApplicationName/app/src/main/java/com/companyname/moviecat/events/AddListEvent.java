package com.companyname.moviecat.events;

import com.companyname.moviecat.models.UserList;

/**
 * Created by OPI on 4/16/17.
 */

public class AddListEvent {
    public final UserList userList;

    public AddListEvent(UserList userList) {
        this.userList = userList;
    }
}
