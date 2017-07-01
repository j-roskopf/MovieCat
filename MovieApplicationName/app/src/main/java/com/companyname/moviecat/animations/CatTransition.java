package com.companyname.moviecat.animations;

import android.transition.Slide;
import android.view.Gravity;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Geoffrey on 3/26/17.
 */

public class CatTransition {

    public static Slide slideFromTopMain() {
        Slide g = new Slide();
        g.setSlideEdge(Gravity.TOP);
        g.setDuration(300);
        return g;
    }
    public static Slide slideFromBottom() {
        Slide g = new Slide();
        g.setSlideEdge(Gravity.BOTTOM);
        return g;
    }
    public static Slide slideFromRight() {
        Slide g = new Slide();
        g.setSlideEdge(Gravity.RIGHT);
        g.setInterpolator(new OvershootInterpolator());
        return g;
    }

    public static Slide slideFromLeft() {
        Slide g = new Slide();
        g.setSlideEdge(Gravity.LEFT);
        g.setInterpolator(new OvershootInterpolator());
        return g;
    }
    /*
    public static Explode activityExplode() {
        Explode e = new Explode();
        e.setDuration(1000);

        e.getPathMotion();
        return e;
    }
    */
}
