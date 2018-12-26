package hugo.weaving;

/**
 * Created by lixindong2 on 12/25/18.
 */

public interface LogFilter {
    /**
     * if the log should be printed by raw Log system
     * @param methodName the name of the Log method
     * @param tag tag param in Log.*
     * @param msg msg param in Log.*
     * @return true if should print
     */
    boolean filter(String methodName, String tag, String msg);
}
