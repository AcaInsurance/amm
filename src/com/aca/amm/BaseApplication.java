package com.aca.amm;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Marsel on 28/9/2016.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
