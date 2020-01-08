package com.neturbo.set.tags;

import java.util.*;
import javax.servlet.http.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;
import com.neturbo.base.action.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class TagUtils {
    public TagUtils() {
    }

    public static String runActionMethod(String name, HttpSession session){
        if(!name.startsWith("#")){
            return name;
        }
        name = name.substring(1);
        String[] nameArray = Utils.splitStr(name, ".");
        if(nameArray.length != 2){
            return name;
        }

        try{
            ActionMappings mappings = Config.getActionMappings();
            String actionName = "/" + nameArray[0];
            ActionMapping actionMapping = mappings.findMapping(actionName);
            String className = actionMapping.getBean();
            Class clazz = Class.forName(className);
            NTBAction action = (NTBAction) clazz.newInstance();
            String methodName = nameArray[1];
            methodName = methodName.substring(0,1 ).toUpperCase() + methodName.substring(1);
            methodName = "get" + methodName;
            action.dispatchMethod(methodName);
            Map resultData = (Map)session.getAttribute("ResultData$Of$Neturbo");
            Map resultData1 = action.getResultData();
            if(resultData !=null && resultData1 != null){
                resultData.putAll(resultData1);
            }
            session.setAttribute("ResultData$Of$Neturbo", resultData);

        }catch(Exception e){

        }

        return nameArray[1];
    }
}
