package com.companyname.moviecat.dagger.components;

import com.companyname.moviecat.MainActivity;
import com.companyname.moviecat.activities.SignInActivity;
import com.companyname.moviecat.dagger.modules.AppModule;

import dagger.Component;

/**
 * Created by Joe on 3/23/2017.
 */

@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(SignInActivity activity);

}
