package com.antiless.hugo.log;

import android.util.Log;

/**
 * Created by lixindong2 on 12/25/18.
 */

public class LogInfo {
    public final @LogLevel
    int level;
    public final String tag;
    public final String message;
    public final Throwable e;
    public final long createAt;

    // TODO record create time

    public LogInfo(@LogLevel int level, String tag, String message, Throwable e, long createAt) {
        this.level = level;
        this.tag = tag;
        this.message = message;
        this.e = e;
        this.createAt = createAt;
    }
    public LogInfo(@LogLevel int level, String tag, String message, Throwable e) {
        this(level, tag, message, e, System.currentTimeMillis());
    }
    public LogInfo(@LogLevel int level, String tag, String message) {
        this(level, tag, message, null);
    }
    public LogInfo(@LogLevel int level, String tag, String message, long createAt) {
        this(level, tag, message, null, createAt);
    }

    public String getLevelName() {
        switch (level) {
            case Log.VERBOSE:
                return "Verbose";
            case Log.DEBUG:
                return "Debug";
            case Log.INFO:
                return "Info";
            case Log.WARN:
                return "Warn";
            case Log.ERROR:
                return "Error";
            case Log.ASSERT:
                return "Assert";
            default:
                return "";
        }
    }
}
