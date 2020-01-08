package app.cib.util;

import java.util.*;
import java.text.*;
import com.neturbo.base.action.*;
import com.neturbo.set.utils.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.core.NTBAction;
import com.neturbo.set.core.Config;

public class PageProcessor extends NTBAction {

    public void writeSysLog(Map inputData) {

    }

    public void handleException(NTBException e) {

    }

    /**
     * @see com.neturbo.set.core.NTBAction#execute()
     */
    public void execute() throws NTBException {
        String sPageActionType = com.neturbo.set.utils.Utils.null2Empty(this.
                getParameter("PAGE_ACTION_TYPE"));
        String sPageJsp = com.neturbo.set.utils.Utils.null2Empty(this.
                getParameter("PAGE_JSP"));
        sPageJsp = EscapeChar.fromURL(sPageJsp);

        String sPageClass = com.neturbo.set.utils.Utils.null2Empty(this.
                getParameter("PAGE_CLASS"));
        int iMoveToPageNo = com.neturbo.set.utils.Utils.nullEmpty2Zero(this.
                getParameter("MOVE_TO_PAGE_NO"));
        int iCurrentPageNo = com.neturbo.set.utils.Utils.nullEmpty2Zero(this.
                getParameter("CURRENT_PAGE_NO"));

        try{
            ActionMappings mappings = Config.getActionMappings();
            String actionName = "/" + sPageClass;
            ActionMapping actionMapping = mappings.findMapping(actionName);
            String className = actionMapping.getBean();
            Class clazz = Class.forName(className);
            PageActionHandler handler = (PageActionHandler) clazz.newInstance();
            handler.processPageAction(this);
        }catch(Exception e){
        }

        Map resultData = this.getResultData();

        if (sPageActionType.equals("MOVETO")) {
            iCurrentPageNo = iMoveToPageNo-1;
        }

        if (sPageActionType.equals("NEXT")) {
            iCurrentPageNo++;
        }

        if (sPageActionType.equals("PREV")) {
            iCurrentPageNo--;
        }

        resultData.put("CURRENT_PAGE_NO", String.valueOf(iCurrentPageNo));
        setResultData(resultData);

        ActionForward pageForward = new ActionForward(sPageJsp);
        this.setForward(pageForward);

    }

}
