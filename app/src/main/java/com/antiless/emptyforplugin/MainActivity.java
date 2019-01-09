package com.antiless.emptyforplugin;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.antiless.hugo.handlers.floatwindow.FloatWindowLogHandler;
import com.antiless.hugo.common.TapTapHelper;
import com.com.antiless.emptyforplugin.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yes("Antiless", "NaN");

        TextView body = findViewById(R.id.body);
        body.setOnClickListener(new View.OnClickListener() {
            TapTapHelper taptap =  TapTapHelper.obtain();
            @Override
            public void onClick(View v) {
                if (FloatWindowLogHandler.getInstance().isShowing()) return;

                taptap.tap();
                if (taptap.times() > 3) {
                    FloatWindowLogHandler.getInstance().show();
                    taptap.clear();
                    sendLogs();
                }
            }

            private void sendLogs() {
                int n = 40;
                Handler handler = new Handler(Looper.getMainLooper());
                while (n -- != 0) {
                    final int num = n;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("MainActivity", "inject " + num);
                            Log.d("MainActivity", "inject " + num);
                            Log.i("MainActivity", "inject " + num);
                            Log.w("MainActivity", "inject " + num);
                            Log.e("MainActivity", "inject " + num);
                            Log.wtf("MainActivity", "inject " + num);
                        }
                    }, n * 1000);
                }
            }
        });

    }

    public static void yes(String first, String last) {
    }
}
