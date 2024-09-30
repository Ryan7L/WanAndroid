package per.goweii.basic.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;

import per.goweii.ponyo.log.LogBody;
import per.goweii.ponyo.log.Ponlog;


public final class LogUtils {
    private static boolean DEBUGGABLE = BuildConfig.DEBUG;
    public static void httpi(String msg) {
        if (!DEBUGGABLE) return;
        Logger.i(System.currentTimeMillis() + Thread.currentThread().getName() +  msg);
    }

    public static void httpe(String msg) {
        if (!DEBUGGABLE) return;
        Logger.e(System.currentTimeMillis() + Thread.currentThread().getName() +  msg);
    }

    public static void v(String tag, Object msg) {
        Logger.v(tag, msg);
    }

    public static void d(String tag, Object msg) {
        Logger.d(tag, msg);
    }

    public static void i(String tag, Object msg) {
        Logger.i(tag, msg);
    }

    public static void w(String tag, Object msg) {
        Logger.w(tag, msg);
    }

    public static void e(String tag, Object msg) {
        Logger.e(tag, msg);
    }

    public static void a(String tag, Object msg) {
        Logger.wtf(tag, msg);
    }
}
