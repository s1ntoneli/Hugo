package com.antiless.hugo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.antiless.hugo.log.LogInfo;
import com.antiless.hugo.log.LogLevel;
import com.antiless.hugo.runtime.HugoRuntimeOptions;
import com.antiless.hugo.runtime.LogHandler;

import static com.antiless.hugo.common.Preconditions.checkNotNull;

public class HugoRuntime {
    private static Application sApp;
    private static HugoRuntimeOptions sOptions;

    public static void init(Application app) {
        sApp = checkNotNull(app, "app should not be Null");
    }

    public static Context getApplicationContext() {
        checkNotNull(sApp, "HugoRuntime should be init first");
        return sApp;
    }

    public static void onLogging(String methodName, String tag, String msg) {
        if (sOptions == null) return;

        for (LogHandler handler : sOptions.handlers()) {
            boolean handled = handler.handle(new LogInfo(getLogEventType(methodName), tag, msg, System.currentTimeMillis()));
            if (handled) break;
        }
    }

    public static boolean filter(String methodName, String tag, String msg) {
        return sOptions == null || sOptions.filter().filter(methodName, tag, msg);
    }

    public static void apply(HugoRuntimeOptions options) {
        sOptions = options;
    }

    @LogLevel
    private static int getLogEventType(String methodName) {
        switch (methodName) {
            case "v":
                return Log.VERBOSE;
            case "d":
                return Log.DEBUG;
            case "i":
                return Log.INFO;
            case "w":
                return Log.WARN;
            case "e":
            case "wtf":
                return Log.ERROR;
        }
        return Log.VERBOSE;
    }
}
