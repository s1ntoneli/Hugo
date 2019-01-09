package com.antiless.hugo.runtime;

import com.antiless.hugo.log.LogInfo;

public interface LogHandler {
    /**
     * handle log info
     * @return true would intercept the handling of log
     */
    boolean handle(LogInfo info);
}
