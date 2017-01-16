package com.aca.amm;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.raizlabs.android.dbflow.config.FlowManager;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Marsel on 28/9/2016.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);

        if (BuildConfig.DEBUG) {
            Crashlytics crashlyticsKit = new Crashlytics.Builder()
                    .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                    .build();

// Initialize Fabric with the debug-disabled crashlytics.
            Fabric.with(this, crashlyticsKit);
        }
    }
}
