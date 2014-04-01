package com.cintel.frame.web.dwr;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cintel.frame.util.StringUtils;
import com.cintel.frame.util.WebUtils;
import com.cintel.frame.web.action.BaseAction;

/**
 * 
 * @file    : DwrDisposeAction.java
 * @version : 1.0
 * @desc    : 
 * @history : 
 *          1) 2009-9-1 wangshuda created
 */
public class DwrDisposeAction extends BaseAction {

    private Object dwrTargetObj;
    
    private final static String _PARA_PREFIX = "para.";

    private static Method getMethodWithName(Class cls, String methodName) {
        Method[] methodsArr = cls.getMethods();

        for (Method method : methodsArr) {
            if (methodName.equals(method.getName())) {
                return method;
            }
        }
        //
        return null;
    }

    public ActionForward remoteCall(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        Object rtnResultObj = null;
        
        String callMethodName = request.getParameter("callMethodName");
        if(!StringUtils.hasText(callMethodName)) {
            log.error("There is no parameter with name 'callMethodName'!");
        }
        
        try {
            Method callMethod = getMethodWithName(dwrTargetObj.getClass(), callMethodName);
            
            //
            Map paraMap = WebUtils.getParametersStartingWith(request, _PARA_PREFIX);

            Object[] argsArr = null;

            if (paraMap != null && !paraMap.isEmpty()) {
                int argsCount = paraMap.size();

                argsArr = new Object[argsCount];
                for (int i = 0; i < argsCount; i++) {
                    argsArr[i] = paraMap.get(String.valueOf(i));
                }
            }
            //
            if (callMethod != null) {
                rtnResultObj = callMethod.invoke(dwrTargetObj, argsArr);
            }
            //
            super.writeWithGson(rtnResultObj, response);
        }
        catch(Exception ex) {
            log.error("remoteCall error!", ex);
        }
        return null;
    }

    // --------------------- get/set methods ------------------------
    public Object getDwrTargetObj() {
        return dwrTargetObj;
    }

    public void setDwrTargetObj(Object dwrTargetObj) {
        this.dwrTargetObj = dwrTargetObj;
    }

}
