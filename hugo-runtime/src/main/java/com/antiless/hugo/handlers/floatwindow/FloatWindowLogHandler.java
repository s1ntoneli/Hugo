package com.antiless.hugo.handlers.floatwindow;

import android.content.Context;
import android.content.Intent;

import com.antiless.hugo.runtime.LogHandler;
import com.antiless.hugo.log.LogInfo;
import com.antiless.hugo.HugoRuntime;

import static com.antiless.hugo.common.Preconditions.checkNotNull;

/**
 * Created by lixindong2 on 12/25/18.
 */

public class FloatWindowLogHandler implements LogHandler {
    private static FloatWindowLogHandler sInstance;
    public static synchronized FloatWindowLogHandler getInstance() {
        if (sInstance == null) {
            sInstance = new FloatWindowLogHandler();
        }
        return sInstance;
    }

    private boolean isShowing = false;
    @Override
    public boolean handle(LogInfo info) {
        if (isShowing) append(info);
        return false;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void show() {
        if (isShowing) return;
        isShowing = true;

        Context context = checkNotNull(HugoRuntime.getApplicationContext(), "app should not be Null");
        Intent intent = new Intent(context, LogFloatWindowService.class);
        intent.setAction(LogFloatWindowService.ACTION_SHOW_WINDOW);
        context.startService(intent);
    }

    public void hide() {
        if (!isShowing) return;
        isShowing = false;

        Context context = checkNotNull(HugoRuntime.getApplicationContext(), "app should not be Null");
        Intent intent = new Intent(context, LogFloatWindowService.class);
        intent.setAction(LogFloatWindowService.ACTION_HIDE_WINDOW);
        context.startService(intent);
    }

    public void clear() {
        if (!isShowing) return;
        isShowing = false;

        Context context = checkNotNull(HugoRuntime.getApplicationContext(), "app should not be Null");
        Intent intent = new Intent(context, LogFloatWindowService.class);
        intent.setAction(LogFloatWindowService.ACTION_CLEAR_LOG);
        context.startService(intent);
    }

    public void append(LogInfo info) {
        Context context = checkNotNull(HugoRuntime.getApplicationContext(), "HugoRuntime should be init first");
        Intent intent = new Intent(context, LogFloatWindowService.class);
        intent.setAction(LogFloatWindowService.ACTION_APPEND_LOG);
        intent.putExtra(LogFloatWindowService.EXTRA_TAG, info.tag);
        intent.putExtra(LogFloatWindowService.EXTRA_MESSAGE, info.message);
        intent.putExtra(LogFloatWindowService.EXTRA_LEVEL, info.level);
        context.startService(intent);
    }
}
