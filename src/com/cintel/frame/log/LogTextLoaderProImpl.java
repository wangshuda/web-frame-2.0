package com.cintel.frame.log;

import com.cintel.frame.properties.PropertiesUtils;

public class LogTextLoaderProImpl implements LogTextLoader {

    public String getText(String key) {
        return PropertiesUtils.getMessage(key);
    }
}
