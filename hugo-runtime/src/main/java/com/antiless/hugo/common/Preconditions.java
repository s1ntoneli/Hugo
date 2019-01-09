package com.antiless.hugo.common;

/**
 * Created by lixindong2 on 12/25/18.
 */

public class Preconditions {
    public static <T> T checkNotNull(T o, String msg) {
        if (o == null) {
            throw new RuntimeException(msg);
        }
        return o;
    }
}
