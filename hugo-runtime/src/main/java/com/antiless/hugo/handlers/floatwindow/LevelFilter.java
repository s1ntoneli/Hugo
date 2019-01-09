package com.antiless.hugo.handlers.floatwindow;

import com.antiless.hugo.log.LogInfo;

/**
 * Created by lixindong2 on 12/29/18.
 */

public class LevelFilter implements Filter<LogInfo> {
    private int currentLevel;

    public LevelFilter(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void level(int level) {
        this.currentLevel = level;
    }

    @Override
    public boolean filter(LogInfo info) {
        return info.level >= currentLevel;
    }
}
