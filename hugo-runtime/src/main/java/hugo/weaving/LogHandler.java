package hugo.weaving;

public interface LogHandler {
    /**
     * handle log info
     * @return true would intercept the handling of log
     */
    boolean handle(LogEventInfo info);
}
