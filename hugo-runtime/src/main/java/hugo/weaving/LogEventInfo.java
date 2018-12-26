package hugo.weaving;

import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lixindong2 on 12/25/18.
 */

public class LogEventInfo {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN, Log.ERROR, Log.ASSERT})
    @interface LogEventType {}

    final @LogEventType int type;
    final String tag;
    final String message;
    final Throwable e;

    public LogEventInfo(@LogEventType int type, String tag, String message, Throwable e) {
        this.type = type;
        this.tag = tag;
        this.message = message;
        this.e = e;
    }
    public LogEventInfo(@LogEventType int type, String tag, String message) {
        this(type, tag, message, null);
    }
}
