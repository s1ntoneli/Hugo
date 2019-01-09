package com.antiless.hugo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.antiless.hugo.HugoRuntime;

@Aspect
public class Hugo {
    private static volatile boolean enabled = true;

    @Pointcut("within(com.antiless.hugo.*)")
    public void withinPlugin() {}

    @Pointcut("call(static * android.util.Log.*(String,String))")
    public void log() {}

    @Pointcut("log() && !withinPlugin()")
    public void logInApp() {}

    public static void setEnabled(boolean enabled) {
        Hugo.enabled = enabled;
    }

    @Around("logInApp()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String tag = (String) joinPoint.getArgs()[0];
        String msg = (String) joinPoint.getArgs()[1];
        HugoRuntime.onLogging(methodName, tag, msg);
        return HugoRuntime.filter(methodName, tag, msg) ? joinPoint.proceed() : 0;
    }
}