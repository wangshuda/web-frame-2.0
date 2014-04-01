package com.cintel.frame.serial.json;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public class SerializerUtils {

    public static JSONObject xmlTxt2JsonObj(String xmlTxt) {
        XMLSerializer xMLSerializer = new XMLSerializer();
        //
        JSONObject origJsonObj = (JSONObject)xMLSerializer.read(xmlTxt);
        
        return origJsonObj;
    }
    
    public static JsonObjWrapper xmlTxt2JsonWrapper(String xmlTxt) {
        JSONObject origJsonObj = xmlTxt2JsonObj(xmlTxt);
        return new JsonObjWrapper(origJsonObj);
    }
    
    public static void  main(String args[]) {
        String xmlTxt = "<mmc version='1.0'><service name='mmc_send_to_wg'><mmc_send_to_wg><type>00</type>  <userId>00wangshuda-bj</userId><vccPublicId>00gh_2331c5aa1288</vccPublicId> <msgType>voice</msgType>  <msgUrl>http://192.168.2.136:9999/mms/888888/00gh_2331c5aa1288/20140301/SESSION20140304160141CAD1017503032593/</msgUrl> <msgseq>2</msgseq><timeStamp>20140301090000</timeStamp>  </mmc_send_to_wg></service></mmc>";
        XMLSerializer xMLSerializer = new XMLSerializer();
        //
        String multiLevelKey = "service.@name";
        
        String subMultiLevelKey = multiLevelKey.substring(multiLevelKey.indexOf(".") + 1);
        System.out.println(subMultiLevelKey);
        //
        JSONObject origJsonObj = (JSONObject)xMLSerializer.read(xmlTxt);
        //
        JsonObjWrapper jsonObjWrapper = new JsonObjWrapper(origJsonObj);
        //
        System.out.println(jsonObjWrapper.toString());
        System.out.println(jsonObjWrapper.getMultiLevelValue(multiLevelKey));
        
    }
}
