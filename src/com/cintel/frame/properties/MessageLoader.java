package com.cintel.frame.properties;

public interface MessageLoader {
    public void loadProperties();
    
    public String getMessage(String key);
}
