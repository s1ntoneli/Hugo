package hugo.weaving.log;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseIntArray;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by lixindong2 on 12/29/18.
 */

public class AndroidStudioStyleFormatter implements Formatter<LogInfo> {
    @Override
    public SpannableString format(LogInfo info) {
        SpannableString spannableString = SpannableString.valueOf(String.format("%s %s/%s: %s", formatDateTime(info.createAt),
                info.getLevelName().length() > 0 ? info.getLevelName().substring(0, 1) : "",
                info.tag, info.message));
        spannableString.setSpan(new ForegroundColorSpan(mColorMap.get(info.level)),
                0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private SparseIntArray mColorMap = new SparseIntArray();
    {
        mColorMap.put(Log.VERBOSE, Color.BLACK);
        mColorMap.put(Log.DEBUG, Color.parseColor("#99CCCC"));
        mColorMap.put(Log.INFO, Color.parseColor("#99CC99"));
        mColorMap.put(Log.WARN, Color.parseColor("#996633"));
        mColorMap.put(Log.ERROR, Color.parseColor("#993333"));
        mColorMap.put(Log.ASSERT, Color.parseColor("#993333"));
    }

    public String formatDateTime(long timestamp) {
        return new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(timestamp);
    }
}
