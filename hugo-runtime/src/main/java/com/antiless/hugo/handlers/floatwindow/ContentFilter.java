package com.antiless.hugo.handlers.floatwindow;

import com.antiless.hugo.log.LogInfo;

/**
 * Created by lixindong2 on 12/29/18.
 */

public class ContentFilter implements Filter<LogInfo> {
    private final Formatter<LogInfo> formatter;
    private String content = "";

    public ContentFilter(Formatter<LogInfo> formatter) {
        this.formatter = formatter;
    }
    public void content(String content) {
        this.content = content;
    }

    @Override
    public boolean filter(LogInfo logInfo) {
        return formatter.format(logInfo).toString().contains(content);
    }
}
