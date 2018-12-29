package hugo.weaving.log;

import android.text.SpannableString;

/**
 * Created by lixindong2 on 12/29/18.
 */

public interface Formatter<T> {
    SpannableString format(T t);
}
