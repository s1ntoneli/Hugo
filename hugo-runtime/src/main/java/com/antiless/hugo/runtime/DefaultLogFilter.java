package com.antiless.hugo.runtime;

/**
 * Created by lixindong2 on 12/25/18.
 */

public class DefaultLogFilter implements LogFilter {
    @Override
    public boolean filter(String methodName, String tag, String msg) {
        return true;
    }
}
