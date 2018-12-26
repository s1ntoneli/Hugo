package hugo.weaving;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

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
    }

    // implements this method to provide
    // the view for floatwindow
    protected abstract View getView();
    protected WindowManager.LayoutParams getParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return params;
    }

    public void showWindow() {
        mRootView = getView();
        if (mRootView != null) {
            mRootView.setOnTouchListener(this);
            mWindowManager.addView(mRootView, mParam);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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

                mParam.x += currentX - lastX;
                mParam.y += currentY - lastY;

                if (mRootView != null) {
                    mWindowManager.updateViewLayout(mRootView, mParam);
                }
                break;
            case MotionEvent.ACTION_UP:
                currentX = (int) event.getRawX();
                currentY = (int) event.getRawY();
                if (Math.abs(currentX-startX) >= 1 || Math.abs(currentY-startY) >= 1) {
                    isMoved = true;
                }
                break;
        }
        return false;
    }

    protected View getRootView() {
        return mRootView;
    }
}