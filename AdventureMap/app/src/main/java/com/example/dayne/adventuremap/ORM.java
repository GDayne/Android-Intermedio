package com.example.dayne.adventuremap;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Brandon Vallejos S on 20/08/2015.
 */
public class ORM extends com.activeandroid.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
