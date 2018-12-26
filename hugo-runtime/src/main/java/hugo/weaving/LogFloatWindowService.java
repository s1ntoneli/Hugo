package hugo.weaving;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class LogFloatWindowService extends FloatWindowHostService {
    private LayoutInflater mInflater = null;
    public static final String ACTION_APPEND_LOG = "com.antiless.hugo.append_log";
    public static final String EXTRA_TAG = "tag";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    @Override
    public void onCreate() {
        super.onCreate();
        mInflater = LayoutInflater.from(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_APPEND_LOG.equals(intent.getAction())) {
            Log.v("FloatWindow", "appending");
            TextView view = getRootView().findViewById(R.id.log);
            String text = "\n" + intent.getStringExtra(EXTRA_TAG) + "|" + intent.getStringExtra(EXTRA_MESSAGE);
            view.append(text);
            return START_NOT_STICKY;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    @Override
    protected View getView() {
        View v = mInflater.inflate(R.layout.layout_float_window, null);
        return v;
    }

}