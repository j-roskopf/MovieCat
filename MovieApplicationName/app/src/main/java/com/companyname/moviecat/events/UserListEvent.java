package com.companyname.moviecat.events;

/**
 * Created by Joe on 8/27/2017.
 */

public class UserListEvent {
    private boolean show;

    public UserListEvent(boolean show){
        this.show = show;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
