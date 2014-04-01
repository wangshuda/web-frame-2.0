package com.cintel.frame.log;

public interface LogHandler {

    public void log(String key, String text);
    
    public void log(String key);
    
    public void log(String key, Object[] parameters);
}
