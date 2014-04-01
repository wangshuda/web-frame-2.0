package com.cintel.frame.web.page;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * 
 * @version 
 * @history 
 *          1.0.0 2011-8-17 wangshuda created
 */
public class GsonTypeAdapter4PageResult implements JsonSerializer {

    public JsonElement serialize(Object pageResultObj, Type typeOfOjb, JsonSerializationContext context) {
        JsonObject jsonObj = new JsonObject();
        //
        PageResult pageResult = (PageResult)(pageResultObj);
        //
        JsonElement pageInfoElement = context.serialize(pageResult.getPageInfo(), PageInfo.class);
        jsonObj.add("pageInfo", pageInfoElement);
        //
        List dataList = pageResult.getPagedList().getPageDataList();
        JsonArray jsonArr = new JsonArray();
        
        JsonElement dataElement = null;
        for(Object dataObj:dataList) {
             dataElement = context.serialize(dataObj, dataObj.getClass());
             jsonArr.add(dataElement);
        }
        //
        jsonObj.add("pageDataList", jsonArr);
        
        return jsonObj;
    }

}
