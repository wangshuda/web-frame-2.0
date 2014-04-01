package com.cintel.frame.serial.json;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

public class JsonObjWrapper implements Map {
    private static Log log = LogFactory.getLog(JsonObjWrapper.class);
    
    private final static String _NULL_JSON_OBJ_TIPS = "Error:json obj is null!";
    
    private final static JSONObject _EMPTY_JSON_OBJ = JSONObject.fromObject("{}");
    private final static JSONArray _EMPTY_JSON_ARR = JSONArray.fromObject("[]");
    
    private JSONObject jsonObj;
    
    //
    public JsonObjWrapper() {
        this(null);
    }
    //
    public JsonObjWrapper(JSONObject jsonObj) {
        if(jsonObj == null) {
            jsonObj = _EMPTY_JSON_OBJ;
        }
        //
        this.setJsonObj(jsonObj);
    }

    public static JsonObjWrapper fromObject(String resJsonTxt) {
        JsonObjWrapper jsonObjWrapper = new JsonObjWrapper(_EMPTY_JSON_OBJ);
        //
        if(StringUtils.hasText(resJsonTxt)) {
            try {
                jsonObjWrapper = new JsonObjWrapper(JSONObject.fromObject(resJsonTxt));
            }
            catch(JSONException ex) {
                jsonObjWrapper = new JsonObjWrapper(_EMPTY_JSON_OBJ);
            } 
        }
        //
        return jsonObjWrapper;
    }
       
    public JSONArray getJSONArray(String key) {
        JSONArray rtnArr = _EMPTY_JSON_ARR;
        Object rtnObj = jsonObj.get(key);
        if(rtnObj != null) {
            if(rtnObj instanceof JSONArray) {
                rtnArr = (JSONArray)rtnObj;
            }
        }
        return rtnArr;
    }
    
    private boolean assertValidKey(String key) {
        boolean rtnValue = jsonObj.containsKey(key);
        if(!rtnValue) {
            log.warn(MessageFormat.format("Can not find key:{0}, in Obj:{1}", key, jsonObj));
        }
        return rtnValue;
    }
    public JsonObjWrapper get(String key) {
        JsonObjWrapper rtnJsonObjWrapper = new JsonObjWrapper(_EMPTY_JSON_OBJ);
        //
        if(this.assertValidKey(key)) {
            Object dataObj = jsonObj.get(key);
            if(dataObj instanceof JSONObject) {
                rtnJsonObjWrapper = new JsonObjWrapper((JSONObject)dataObj);
            }
        }
        return rtnJsonObjWrapper;
    }

    public JsonObjWrapper getMultiLevelWraper(String multiLevelKey) {
        JsonObjWrapper rtnJsonObjWrapper = new JsonObjWrapper(_EMPTY_JSON_OBJ);
        //
        String[] keyArr = StringUtils.delimitedListToStringArray(multiLevelKey, ".");
        if(keyArr != null && keyArr.length > 1) {
            String objKey = keyArr[0];
            //
            JsonObjWrapper jsonObjWrapper = this.get(objKey);
            //
            String subMultiLevelKey = multiLevelKey.substring(multiLevelKey.indexOf(".") + 1);
            //
            return jsonObjWrapper.getMultiLevelWraper(subMultiLevelKey);
        }
        else {
            rtnJsonObjWrapper = this.get(multiLevelKey);
        }
        return rtnJsonObjWrapper;
    }
    
    public String getMultiLevelValue(String multiLevelKey) {
        String rtnValue = "";
        String[] keyArr = StringUtils.delimitedListToStringArray(multiLevelKey, ".");
        if(keyArr != null && keyArr.length > 1) {
            String objKey = keyArr[0];
            //
            JsonObjWrapper jsonObjWrapper = this.get(objKey);
            //
            String subMultiLevelKey = multiLevelKey.substring(multiLevelKey.indexOf(".") + 1);
            //
            return jsonObjWrapper.getMultiLevelValue(subMultiLevelKey);
        }
        else {
            rtnValue = this.getString(multiLevelKey);
        }
        return rtnValue;
    }
    
    @Override
    public String toString() {
        return jsonObj.toString();
    }
    
    public String getString(String key) {
        String rtnValue = "";
        try {
            if(this.assertValidKey(key)) {
                rtnValue = jsonObj.getString(key);
            }
        }
        catch(JSONException ex) {
            log.debug(jsonObj != null ? jsonObj.toString() :_NULL_JSON_OBJ_TIPS , ex);
        }
        return rtnValue;
    }

    public int getInt(String key) {
        int rtnValue = 0;
        try {
            if(this.assertValidKey(key)) {
                rtnValue = jsonObj.getInt(key);
            }
        }
        catch(JSONException ex) {
            log.debug(jsonObj != null ? jsonObj.toString() :_NULL_JSON_OBJ_TIPS, ex);
        }
        return rtnValue;
    }

    public long getLong(String key) {
        long rtnValue = 0;
        try {
            if(this.assertValidKey(key)) {
                rtnValue = jsonObj.getLong(key);
            }
        }
        catch(JSONException ex) {
            log.info(jsonObj != null ? jsonObj.toString() :_NULL_JSON_OBJ_TIPS);
            if(log.isDebugEnabled()) {
                log.debug("", ex);
            }
        }
        return rtnValue;
    }
    
//  ----------------------------------------- map methods -----------------------------------------
    public void clear() {
        this.jsonObj.clear();
    }

    public boolean containsKey(Object key) {
        return this.jsonObj.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.jsonObj.containsKey(value);
    }

    public Set entrySet() {
        return this.jsonObj.entrySet();
    }

    public Object get(Object key) {
        return this.jsonObj.get(key);
    }

    public boolean isEmpty() {
        return this.jsonObj.isEmpty();
    }

    public Set keySet() {
        return this.jsonObj.keySet();
    }

    public Object put(Object key, Object value) {
        return this.jsonObj.put(key, value);
    }

    public void putAll(Map map) {
        this.jsonObj.putAll(map);
    }

    public Object remove(Object key) {
        return this.jsonObj.remove(key);
    }

    public int size() {
        return this.jsonObj.size();
    }

    public Collection values() {
        return this.jsonObj.values();
    }
    
    // --------------------------------- get/set methods ---------------------------------
    public JSONObject getJsonObj() {
        return jsonObj;
    }

    public void setJsonObj(JSONObject jsonObj) {
        this.jsonObj = jsonObj;
    }
}
