package com.companyname.moviecat.dagger.modules;

import android.content.Context;
import android.location.LocationManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Joe on 3/23/2017.
 */

@Module
public class AppModule {
    private Context context;

    public AppModule(Context context){
        this.context = context;
    }

    @Provides
    public LocationManager provideLocationManager(){
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

/*    @Provides
    public FoodItem[] provideFoodItems(){
        return new FoodItem[] {
                new FoodItem("pizza", "Pizza"),
                new FoodItem("cheeseburger", "Cheeseburger"),
                new FoodItem("burrito", "Burrito"),
                new FoodItem("chinese", "Chinese"),
                new FoodItem("bahn_mi", "Bahn Mi"),
                new FoodItem("coffee", "Coffee"),
                new FoodItem("bakery", "Bakery"),
                new FoodItem("sandwich", "Sandwich")
        };
    }*/

}
