package hugo.weaving;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import hugo.weaving.log.LogInfo;
import hugo.weaving.log.LogView;

public class LogFloatWindowService extends FloatWindowHostService {
    private LayoutInflater mInflater = null;
    public static final String ACTION_APPEND_LOG = "com.antiless.hugo.append_log";
    public static final String EXTRA_TAG = "tag";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_LEVEL = "level";

    public static final String ACTION_CLEAR_LOG = "com.antiless.hugo.clear_log";
    public static final String ACTION_SHOW_WINDOW = "com.antiless.hugo.show_window";
    public static final String ACTION_HIDE_WINDOW = "com.antiless.hugo.hide_window";

    @Override
    public void onCreate() {
        super.onCreate();
        mInflater = LayoutInflater.from(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case ACTION_CLEAR_LOG:
                mLogView.clearLog();
                break;
            case ACTION_APPEND_LOG:
                Log.v("FloatWindow", "appending");
                appendLog(intent.getIntExtra(EXTRA_LEVEL, Log.VERBOSE), intent.getStringExtra(EXTRA_TAG), intent.getStringExtra(EXTRA_MESSAGE));
                break;
            case ACTION_SHOW_WINDOW:
                showWindow();
                break;
            case ACTION_HIDE_WINDOW:
                hideWindow();
                break;
        }
        return START_NOT_STICKY;
    }

    private void appendLog(int type, String tag, String message) {
        mLogCache.append(new LogInfo(type, tag, message));
        if (mLogView != null) mLogView.updateData(mLogCache);
    }

    private TapDetector tapDetector;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouch(v, event);
    }

    private TextView mConsole;
    private LogView mLogView;

    private LogView.LogCache mLogCache = new LogView.LogCache(400);

    @Override
    protected void initView(final View view) {
        mConsole = view.findViewById(R.id.console);
        mLogView = view.findViewById(R.id.logView);
        mConsole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LogFloatWindowService", "onClick: ");
                mLogView.setVisibility(View.VISIBLE);
                mLogView.updateData(mLogCache);
            }
        });
        mConsole.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("LogFloatWindowService", "onTouch: console");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tapDetector = TapDetector.obtain(getApplicationContext(), event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        tapDetector.onEvent(event);
                        moveBy(tapDetector.offsetX(), tapDetector.offsetY());
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!tapDetector.moved()) {
                            Log.i("LogFloatWindowService", "onTouch: singleTap");
                            v.performClick();
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected View getView() {
        View v = mInflater.inflate(R.layout.layout_float_window, null);
        return v;
    }

    public static class TapDetector {
        private int downX, downY;
        private int lastX, lastY;
        private int offsetX, offsetY;
        private int scaledTouchSlop;
        private boolean moved = false;
        public static TapDetector obtain(Context context, MotionEvent event) {
            return new TapDetector(context, (int) event.getRawX(), (int) event.getRawY());
        }

        public TapDetector(Context context, int downX, int downY) {
            this.downX = downX;
            this.downY = downY;
            this.lastX = downX;
            this.lastY = downY;
            this.scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            Log.i("TapDetector", "TapDetector: " + scaledTouchSlop);
        }

        public void onEvent(MotionEvent event) {
            int diffX = Math.abs((int) (event.getRawX() - downX));
            int diffY = Math.abs((int) (event.getRawY() - downY));
            offsetX = (int) (event.getRawX() - lastX);
            offsetY = (int) (event.getRawY() - lastY);
            lastX = (int) event.getRawX();
            lastY = (int) event.getRawY();
            moved = diffX > scaledTouchSlop || diffY > scaledTouchSlop;
            Log.i("TapDetector", "onEvent: moved " + moved);
        }

        public int offsetX() {
            return offsetX;
        }

        public int offsetY() {
            return offsetY;
        }

        public boolean moved() {
            return moved;
        }
    }
}