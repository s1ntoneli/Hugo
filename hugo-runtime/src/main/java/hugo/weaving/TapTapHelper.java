package hugo.weaving;

/**
 * Created by lixindong2 on 12/18/18.
 */

public class TapTapHelper {
    private int times = 0;
    private final static int TimeOutTime = 500;
    private long lastTapTime = 0;

    public void tap() {
        long current = System.currentTimeMillis();
        synchronized (this) {
            if (current - lastTapTime < TimeOutTime) {
                times++;
            } else {
                times = 1;
            }
            lastTapTime = current;
        }
    }
    public static TapTapHelper obtain() {
        return new TapTapHelper();
    }
    public int times() {
        return times;
    }

    public void clear() {
        synchronized (this) {
            times = 0;
        }
    }
}
