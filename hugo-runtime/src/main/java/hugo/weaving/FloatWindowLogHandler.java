package hugo.weaving;

import android.content.Context;
import android.content.Intent;

import static hugo.weaving.Predictions.checkNotNull;

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
    public boolean handle(LogEventInfo info) {
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
        context.startService(intent);
    }

    public void append(LogEventInfo info) {
        Context context = checkNotNull(HugoRuntime.getApplicationContext(), "HugoRuntime should be init first");
        Intent intent = new Intent(context, LogFloatWindowService.class);
        intent.setAction(LogFloatWindowService.ACTION_APPEND_LOG);
        intent.putExtra(LogFloatWindowService.EXTRA_TAG, info.tag);
        intent.putExtra(LogFloatWindowService.EXTRA_MESSAGE, info.message);
        intent.putExtra(LogFloatWindowService.EXTRA_TYPE, info.type);
        context.startService(intent);
    }
}
