package com.cintel.frame.log.holder;

import com.cintel.frame.log.LogHandlerFactory;

/**
 * 
 * @file    : LogParametersHolder.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-8-21 wangshuda created
 */
public class LogParametersHolder {
    private static ThreadLocal<Object> holder = new ThreadLocal<Object>();
    //
    public static void clear() {
        holder.set(null);
    }

    public static Object[] load() {
        if (holder.get() == null) {
            holder.set(new String[]{});
        }
        //
        return (String[]) holder.get();
    }

    public static void refresh(Object[] parameters) {
        holder.set(parameters);
    }
        
    public static void refresh(String logKey, Object ... logTextParas) {
        if(LogHandlerFactory.getInstance() != null) {
            LogHandlerFactory.getInstance().log(logKey, logTextParas);
        }
        //
        holder.set(logTextParas);
    }
}
