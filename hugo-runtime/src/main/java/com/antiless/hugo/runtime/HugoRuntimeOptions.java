package com.antiless.hugo.runtime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixindong2 on 12/25/18.
 */

public class HugoRuntimeOptions {
    private final List<LogHandler> handlers;
    private final LogFilter filter;

    public List<LogHandler> handlers() {
        return handlers;
    }
    public LogFilter filter() {
        return filter;
    }

    private HugoRuntimeOptions(List<LogHandler> handlers, LogFilter filter) {
        this.handlers = handlers;
        this.filter = filter;
    }

    public static Builder on() {
        return new Builder();
    }

    public static class Builder {
        private List<LogHandler> handlers = new ArrayList<>();
        private LogFilter filter;

        public Builder addLogHandler(LogHandler handler) {
            this.handlers.add(handler);
            return this;
        }

        public Builder filter(LogFilter filter) {
            this.filter = filter;
            return this;
        }

        public HugoRuntimeOptions build() {
            if (filter == null) filter = new DefaultLogFilter();
            return new HugoRuntimeOptions(handlers, filter);
        }
    }
}
