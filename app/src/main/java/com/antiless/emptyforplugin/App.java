package com.antiless.emptyforplugin;

import android.app.Application;

import com.antiless.hugo.handlers.floatwindow.FloatWindowLogHandler;
import com.antiless.hugo.HugoRuntime;
import com.antiless.hugo.runtime.HugoRuntimeOptions;

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
