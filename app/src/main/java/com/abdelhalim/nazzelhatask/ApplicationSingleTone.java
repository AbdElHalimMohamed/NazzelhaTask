package com.abdelhalim.nazzelhatask;

import android.app.Application;

/**
 * Created by AbdEl-Halim on 10/15/2015.
 */
public class ApplicationSingleTone extends Application {
    private static ApplicationSingleTone applicationSingleTone;

    public ApplicationSingleTone() {

    }

    public static ApplicationSingleTone getInstance() {
        return applicationSingleTone;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationSingleTone = this;
    }
}
