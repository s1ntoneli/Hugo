package com.antiless.emptyforplugin;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import hugo.weaving.DebugLog;
import hugo.weaving.FloatWindowLogHandler;
import hugo.weaving.TapTapHelper;

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
                            Log.i("MainActivity", "inject " + num);
                        }
                    }, n * 1000);
                }
            }
        });

    }

    @DebugLog
    public static void yes(String first, String last) {
    }
}
