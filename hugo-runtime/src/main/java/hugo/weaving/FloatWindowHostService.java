package hugo.weaving;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;

public abstract class FloatWindowHostService extends Service implements View.OnTouchListener{
    public FloatWindowHostService() {
    }

    protected WindowManager mWindowManager = null;
    protected WindowManager.LayoutParams mParam = null;
    protected View mRootView = null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initWindow();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideWindow();
    }

    protected void initWindow() {
        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        mParam = getParams();
        mScaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
    }

    // implements this method to provide
    // the view for floatwindow
    protected abstract View getView();
    protected abstract void initView(View view);
    protected WindowManager.LayoutParams getParams() {
        int type;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            type = TYPE_SYSTEM_ERROR;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, type,
                FLAG_NOT_TOUCH_MODAL | FLAG_LAYOUT_NO_LIMITS | FLAG_LAYOUT_INSET_DECOR | FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        return params;
    }

    private static int dp2px(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().scaledDensity * dp + 0.5f);
    }

    public void showWindow() {
        mRootView = getView();
        if (mRootView != null) {
//            mRootView.setOnTouchListener(this);
            mWindowManager.addView(mRootView, mParam);
            initView(mRootView);
        }
    }

    public void hideWindow() {
        if (mRootView != null) {
            mWindowManager.removeView(mRootView);
            mRootView = null;
        }
    }

    private int startX, startY;
    private int lastX, lastY;
    private int currentX, currentY;
    private boolean isMoved = false;
    private int mScaledTouchSlop;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("FloatWindowHostService", "onTouch: ");
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isMoved = false;
                startX = (int) event.getRawX();
                startY = (int) event.getRawY();
                currentX = startX;
                currentY = startY;
                break;
            case MotionEvent.ACTION_MOVE:
                lastX = currentX;
                lastY = currentY;
                currentX = (int) event.getRawX();
                currentY = (int) event.getRawY();

                moveBy(currentX - lastX, currentY - lastY);
                break;
            case MotionEvent.ACTION_UP:
                currentX = (int) event.getRawX();
                currentY = (int) event.getRawY();
                if (Math.abs(currentX-startX) > mScaledTouchSlop || Math.abs(currentY-startY) > mScaledTouchSlop) {
                    isMoved = true;
                }
                break;
        }
        return false;
    }

    protected void moveTo(int x, int y) {
        mParam.x = x;
        mParam.y = y;

        if (mRootView != null) {
            mWindowManager.updateViewLayout(mRootView, mParam);
        }
    }
    protected void moveBy(int dx, int dy) {
        moveTo(mParam.x + dx, mParam.y + dy);
    }

    protected View getRootView() {
        return mRootView;
    }
}