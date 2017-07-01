package com.companyname.moviecat.models;

import android.support.annotation.Nullable;

/**
 * Created by Joe on 3/26/2017.
 */

public abstract class Callback<T>{
    public abstract void success(T t);
    public abstract void failure(@Nullable String message);
}
