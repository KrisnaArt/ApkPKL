package com.example.ApkPKL;

import android.app.Application;
import android.content.Context;


/**
 * Created by Ali Asadi on 10/03/2018.
 */
public class App extends Application {

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

}

