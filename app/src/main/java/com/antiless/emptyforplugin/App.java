package com.antiless.emptyforplugin;

import android.app.Application;

import hugo.weaving.FloatWindowLogHandler;
import hugo.weaving.HugoRuntime;
import hugo.weaving.HugoRuntimeOptions;

/**
 * Created by lixindong2 on 12/25/18.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        HugoRuntime.init(this);
        HugoRuntime
                .apply(HugoRuntimeOptions.on().addLogHandler(FloatWindowLogHandler.getInstance())
                .build());
    }
}
