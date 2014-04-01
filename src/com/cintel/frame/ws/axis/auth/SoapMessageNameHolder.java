package com.cintel.frame.ws.axis.auth;


/**
 * 
 * @file    : SoapMessageNameHolder.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-8-21 wangshuda created
 */
public class SoapMessageNameHolder {
    private static ThreadLocal<String> holder = new ThreadLocal<String>();
    //
    public static void clear() {
        holder.set(null);
    }

    public static String get() {
        return holder.get();
    }

    public static void save(String msgName) {
        holder.set(msgName);
    }
}
