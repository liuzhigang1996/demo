package com.neturbo.set.core;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import javax.servlet.http.*;
import javax.xml.rpc.server.*;

import com.neturbo.base.action.*;
import com.neturbo.base.upload.*;
import com.neturbo.set.database.*;
import com.neturbo.set.exception.*;
import com.neturbo.set.utils.*;
import org.apache.commons.beanutils.*;

public abstract class NTBAction extends Action {

    private HttpSession session;
    private HttpServletRequest request;

    private NTBUser user;
    private Map allTxnSessionData;
    private Map txnSessionData;
    private Map resultData;
    private FormFile formFile;

    private String forward;
    private ActionForward actionForward;
    private ActionFormA actionForm;
    private ActionMapping actionMapping;

    private String contextPath;
    private String requestIP;
    private List messageData;
    private List warningMessageData;
    
    public List getWarningMessageData() {
		return warningMessageData;
	}

	public void setWarningMessageData(String warningMessage) {
		
		if (warningMessageData==null){
			warningMessageData = Collections.synchronizedList(new ArrayList());;
    	}
		warningMessageData.add(warningMessage);
	}

	private NTBException errorData;
    private String txnSessionName;
    private String inputPath;
    private String funcPermission;

    private HashMap methods = new HashMap();
    private Class types[] = {};
    private String pageLanguage;

    public abstract void execute() throws NTBException;

    public abstract void writeSysLog(Map inputData) throws NTBException;

    public abstract void handleException(NTBException e);

    protected void processNotLogin() throws NTBException {
        Log.error("User not login from IP: " + requestIP);
//        throw new NTBLoginException("err.sys.NotLogined");
        throw new NTBLoginException("error.kickedout");//mod by linrui 20190822
    }


    public void convertMap2Pojo(Map valueMap, Object pojo) throws NTBException {
        try {
            BeanUtils.populate(pojo, valueMap);
        } catch (Exception e) {
            Log.warn("Error writing values to POJO", e);
        }
    }

    public void convertPojo2Map(Object pojo, Map valueMap) throws NTBException {
        try {
            valueMap.putAll(BeanUtils.describe(pojo));
        } catch (Exception e) {
            Log.warn("Error reading values from POJO", e);
        }
    }

    public List convertPojoList2MapList(List pojoList) throws NTBException {
        try {
            List mapList = new ArrayList();
            for(int i=0; i<pojoList.size(); i++){
                Object pojo = pojoList.get(i);
                mapList.add(BeanUtils.describe(pojo));
            }
            return mapList;
        } catch (Exception e) {
            Log.warn("Error reading values from POJO", e);
        }
        return null;
    }

    public InputStream getUploadFileInputStream() throws FileNotFoundException,
            IOException {

        return (formFile.getInputStream());

    }

    public ActionForward perform(
            ActionMapping actionMapping,
            ActionForm actionForm,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        try {
            this.actionForm = (ActionFormA) actionForm;
            this.actionMapping = actionMapping;

            request = httpServletRequest;
            requestIP = httpServletRequest.getRemoteAddr();
            contextPath = httpServletRequest.getContextPath();
            session = httpServletRequest.getSession();

            formFile = this.actionForm.getUploadFile();
            this.doPerform(session);
        } catch (NTBLoginException e) {
//            session.setAttribute("ErrorData$Of$Neturbo", e);
            ActionForwards forwards = Config.getGlobalForwards();
            ActionForward loginForward = forwards.findForward("LoginPath");
            //Special for BANK, Modified by Nabai 20060814
            if(actionMapping.getName().toLowerCase().indexOf("login")>0){
            	session.setAttribute("ErrorData$Of$Neturbo", e);
                loginForward = new ActionForward(inputPath);
            }
            if(funcPermission != null){
            	session.setAttribute("ErrorData$Of$Neturbo", e);
                GenericJdbcDao jdbcDao = (GenericJdbcDao)Config.getAppContext().getBean("genericJdbcDao");
                try{
                    Object roleId = jdbcDao.querySingleValue(
                            "SELECT ROLE_ID FROM CORP_PERMISSION WHERE PERMISSION_RESOURCE=? AND STATUS='0'",
                            new Object[] {funcPermission});
                    if(Utils.nullEmpty2Zero(roleId) > 5){
                        loginForward = new ActionForward(Utils.replaceStr(loginForward.getPath(), "common", "common/bank"));
                    }
                }catch(Exception e1){
                    Log.warn("Error getting login path", e1);
                }
            }

            return loginForward;
        } catch (NTBSystemException e) {
            session.setAttribute("ErrorData$Of$Neturbo", e);
            ActionForwards forwards = Config.getGlobalForwards();
            ActionForward errorForward = forwards.findForward("ErrorReporter");
            return errorForward;
        } catch (NTBWarningException e) {
            handleException(e);
            session.setAttribute("WarningData$Of$Neturbo", e);
            ActionForward inputForward = new ActionForward(inputPath);
            return inputForward;
        } catch (NTBException e) {
            handleException(e);
            session.setAttribute("ErrorData$Of$Neturbo", e);
            ActionForward inputForward = new ActionForward(inputPath);
            return inputForward;
        } catch (Exception e) {
            Log.error(
                    "System error when perform Servlet Action("
                    + actionMapping.getName()
                    + ") ",
                    e);
            session.setAttribute(
                    "ErrorData$Of$Neturbo",
                    new NTBException("err.sys.GeneralError"));
            String input = actionMapping.getInput();
            ActionForward inputForward = new ActionForward(input);
            return inputForward;
        }

        return actionForward;
    }

    public HashMap perform1(
            ActionMapping actionMapping,
            ActionForm actionForm,
            ServletEndpointContext serviceContext) throws Exception {
        HashMap retData = new HashMap();
        try {
            this.actionForm = (ActionFormA) actionForm;
            this.actionMapping = actionMapping;

            requestIP = "127.0.0.1";
            session = serviceContext.getHttpSession();

            this.doPerform(session);
        } catch (NTBLoginException e) {
            retData.put("ErrorData$Of$Neturbo", e.getErrorCode());
        } catch (NTBSystemException e) {
            retData.put("ErrorData$Of$Neturbo", e.getErrorCode());
        } catch (NTBException e) {
            retData.put("ErrorData$Of$Neturbo", e.getErrorCode());
        } catch (Exception e) {
            Log.error(
                    "System error when perform WebService Action("
                    + actionMapping.getName()
                    + ")",
                    e);
            retData.put(
                    "ErrorData$Of$Neturbo",
                    String.valueOf(9999));
        }
        return retData;

    }

    public void doPerform(HttpSession session) throws Exception {

        this.setActionFileds();

        String localStr = this.getParameter("PageLanguage");
       if(localStr!=null && !localStr.equals("")){
        this.setPageLanguage(localStr);
       }

        String methodName = this.getParameter("ActionMethod");
        if (methodName != null) {
            ActionForward af = findForward(methodName);
            if (af != null) {
                inputPath = af.getInput();
            }
        }
        if (inputPath == null || inputPath.equals("")) {
            inputPath = actionMapping.getInput();
        }

        funcPermission = actionMapping.getPermission();

        ActionForward methodForward = findForward(methodName);
        if(methodForward !=null){
            String funcPerm1 = methodForward.getPermission();
            if (funcPerm1 != null) {
                funcPermission = funcPerm1;
            }
        }
        String sessionId = session.getId();

        user = ((NTBUser) session.getAttribute("UserObject$Of$Neturbo"));
        if (user == null) {
        	Map resultDataLogin = new HashMap();
            resultDataLogin.put("logoutType", "S");
//            resultDataLogin.put("isLoad", "Y");
            setResultData(resultDataLogin);
            this.processNotLogin();
        }else if( !Utils.null2Empty(user.getSessionId()).equals(sessionId)){
        	Map resultDataLogin = new HashMap();
            resultDataLogin.put("logoutType", "T");
//            resultDataLogin.put("isLoad", "Y");
            setResultData(resultDataLogin);
        	this.processNotLogin();
        	
        }
        
        if (user != null) {
            boolean hasRight = false;
            List userFunctionList = user.getFunctionList();
            if (funcPermission == null || "".equals(funcPermission)) {
                hasRight = true;
            } else {
                String[] funcPerms = Utils.splitStr(funcPermission , ",");
                for (int i = 0; i < userFunctionList.size(); i++) {
                    NTBPermission perm = (NTBPermission) userFunctionList.get(i);
                    String funcID = perm.getPermissionResource();
                    for(int j=0;j<funcPerms.length;j++){
                    if (funcPerms[j].startsWith(funcID)) {
                        hasRight = true;
                        break;
                    }
                }
                }

                if (!hasRight) {
                    Log.error(
                            "User "
                            + user.getUserId()
                            +
                            " has not privilege from Action with function permission: "
                            + funcPermission);
                    throw new NTBSystemException("err.sys.NoPermission");
                }
            }

        }

        if (this.actionMapping.getSyslog()) {
            Map inputData = this.getParameterValuesMap();
            writeSysLog(inputData);
        }

        txnSessionName = actionMapping.getTxnSession();
        if (txnSessionName == null) {
            txnSessionName = "Default$TxnSession";
        }
        allTxnSessionData =
                ((HashMap) session.getAttribute("TxnSessionData$Of$Neturbo"));
        if (allTxnSessionData != null) {
            txnSessionData = (HashMap) allTxnSessionData.get(txnSessionName);
        } else {
            allTxnSessionData = new HashMap(5);
            session.setAttribute("TxnSessionData$Of$Neturbo", allTxnSessionData);
        }
        if (txnSessionData == null) {
            txnSessionData = new HashMap();
            allTxnSessionData.put(txnSessionName, txnSessionData);
        }

        String clearTxnSessionFlag = actionMapping.getClearTxnSession();
        if ("Y".equalsIgnoreCase(clearTxnSessionFlag)) {
            txnSessionData.clear();
        }

        resultData =
                ((HashMap) session.getAttribute("ResultData$Of$Neturbo"));

        String forwardByMethod = dispatchMethod(methodName);
        session = this.session;

        try {
            if (resultData != null) {
                session.setAttribute("ResultData$Of$Neturbo", resultData);
            }
            if (txnSessionData != null) {
                if (allTxnSessionData == null) {
                    allTxnSessionData = new HashMap(5);
                }
                allTxnSessionData.put(txnSessionName, txnSessionData);
                allTxnSessionData.put("Current$TxnSession$Name", txnSessionName);
                session.setAttribute("TxnSessionData$Of$Neturbo",
                                     allTxnSessionData);
            }
            if (messageData != null) {
                session.setAttribute("MessageData$Of$Neturbo", messageData);
            }
            if (warningMessageData != null) {
            	session.setAttribute("WarningMessageData$Of$Neturbo", warningMessageData);
            }
            if (errorData != null) {
                session.setAttribute("ErrorData$Of$Neturbo", errorData);
            }
            session.removeAttribute("Parameters$Of$Neturbo");
            session.removeAttribute("ParameterValues$Of$Neturbo");

        } catch (Exception e) {
            Log.debug("User logout, Session invalidated");
        }
        if (actionForward == null) {
            forward = Utils.null2Empty(forward).trim();
            if (forward.equals("")) {
                forward = Utils.null2Empty(forwardByMethod).trim();
            }
            actionForward = findForward(forward);
        }

        if (actionForward == null) {
            Log.error(
                    "Forward("
                    + forward
                    + ") not exist in Action("
                    + actionMapping.getName()
                    + ")");
        }
    }

    private ActionForward findForward(String name) {
        ActionForward af = null;
        if (name == null || name.equals("")) {
            String[] forwards = actionMapping.findForwards();
            if (forwards.length > 0) {
                af = actionMapping.findForward(forwards[0]);
            } else {
                Log.error(
                        "No forward in Action(" + actionMapping.getName() +
                        ")");
            }
        } else {
            af = actionMapping.findForward(name);
        }
        return af;
    }

    /**
     * Dispatch to the specified method.
     *
     * @since Struts 1.1
     */
    public String dispatchMethod(String name) throws NTBException {

        if (name == null) {
            this.execute();
            return null;
        }

        Method method = null;
        try {
            method = getMethod(name);
        } catch (Exception e) {
            Log.error("Method not found " + name, e);
            throw new NTBException("err.sys.GeneralError");
        }

        try {
        	String classStr = this.getClass().getName() + "." + name + "(" + this.hashCode() + ")";
            Log.info("Runinng " + classStr);                                                                                             
            
            Object args[] = {};
            method.invoke(this, args);
            
            Log.info("Ended " + classStr);  
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof NTBException) {
                Log.error("Error running method " + name, cause);
                throw (NTBException) cause;
            } else {
                Log.error("Error running method " + name, cause);
                throw new NTBException("err.sys.GeneralError");
            }
        } catch (IllegalArgumentException ex) {
            Log.error("Error running method " + name, ex);
            throw new NTBException("err.sys.GeneralError");
        } catch (IllegalAccessException ex) {
            Log.error("Error running method " + name, ex);
            throw new NTBException("err.sys.GeneralError");
        }

        return name;
    }

    protected Method getMethod(String name) throws NoSuchMethodException {
        synchronized (methods) {
            Method method = (Method) methods.get(name);
            if (method == null) {
                method = this.getClass().getMethod(name, types);
                methods.put(name, method);
            }
            return (method);
        }
    }

    private void setActionFileds() {
        Map fieldMap = this.getParameters();
        Iterator imap = fieldMap.entrySet().iterator();
        while (imap.hasNext()) {
            Map.Entry entry = (Map.Entry) imap.next();
            String key = (String) entry.getKey();
            String value = entry.getValue().toString();
            setField(key, value);
        }
    }

    private void setField(String name, String value) {
        Field fld = null;
        try {
            fld = this.getClass().getField(name);
        } catch (Exception e) {

        }
        if (fld != null) {
            try {
                fld.set(name, value);
            } catch (Exception e) {

            }
        }
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public void setForward(ActionForward forwardObj) {
        this.actionForward = forwardObj;
    }

    public String getParameter(String key) {
        return actionForm.getParameter(key);
    }

    public void setParameters(Map para) {
    	if(actionForm == null){
    		actionForm = new ActionFormA();
    	}
        actionForm.setParameters(para);
    }

    public Map getParameters() {
        return actionForm.getParameters();
    }

    public String[] getParameterValues(String key) {
        return actionForm.getParameterValues(key);
    }

    public Map getParameterValuesMap() {
        return actionForm.getParameterValuesMap();
    }

    public String getAttribute(String key) {
        return actionForm.getAttribute(key);
    }

    public Map getAttributes() {
        return actionForm.getAttributes();
    }

    public void setResultData(Map resultData) {
        this.resultData = resultData;
        if (session != null) {
            session.setAttribute("ResultData$Of$Neturbo", resultData);
        }
    }

    public Map getResultData() {
        return resultData;
    }

    public NTBUser getUser() {
        return user;
    }

    public String getUploadFileName() {
        return formFile.getFileName();
    }

    public int getUploadFileSize() {
        return formFile.getFileSize();
    }

    public byte[] getUploadFileData() throws FileNotFoundException, IOException {
        return formFile.getFileData();
    }

    public boolean hasUploadFile() {
        return (formFile != null);
    }

    public void setCurrentTxnSessionDataValue(Object key, Object value) {
        if (txnSessionData == null) {
            txnSessionData = new HashMap();
        }
        txnSessionData.put(key, value);
    }

    public void setUsrSessionDataValue(Object key, Object value) {
        HashMap usrSessionData =
                (HashMap) session.getAttribute("UsrSessionData$Of$Neturbo");
        if (usrSessionData == null) {
            usrSessionData = new HashMap();
        }
        usrSessionData.put(key, value);
        session.setAttribute("UsrSessionData$Of$Neturbo", usrSessionData);
    }

    public void setCurrentTxnSessionData(Map map) {
        if (txnSessionData == null) {
            txnSessionData = new HashMap();
        }
        if (map != null) {
            txnSessionData.putAll(map);
        }
    }

    public void setCurrentTxnSessionData(HashMap map) {
        if (txnSessionData == null) {
            txnSessionData = new HashMap();
        }
        if (map == null) {
            map = new HashMap();
        }
        txnSessionData.putAll(map);
    }

    public void setUser(NTBUser user) {
        if(user != null){
            user.setSessionId(session.getId());
            session.setAttribute("UserObject$Of$Neturbo", user);
            this.user = user;
        }else{
            try{
                session.removeAttribute("UserObject$Of$Neturbo");
            }catch(Exception e){
                Log.warn("Error removing user from session");
            }
            this.user = user;
        }
    }

    public void setUsrSessionData(Map map) {
        HashMap usrSessionData =
                (HashMap) session.getAttribute("UsrSessionData$Of$Neturbo");
        if (usrSessionData == null) {
            usrSessionData = new HashMap();
        }
        if (map != null) {
            usrSessionData.putAll(map);
        }
        session.setAttribute("UsrSessionData$Of$Neturbo", usrSessionData);
    }

    public void clearUsrSessionDataValue() {
        HashMap usrSessionData =
                (HashMap) session.getAttribute("UsrSessionData$Of$Neturbo");
        if (usrSessionData != null) {
            usrSessionData.clear();
        }
        usrSessionData = null;
    }

    public Object getCurrentTxnSessionDataValue(Object key) {
        if (txnSessionData != null) {
            return txnSessionData.get(key);
        } else {
            return null;
        }

    }

    public Map getCurrentTxnSessionData() {
        return txnSessionData;
    }

    public Map getOtherTxnSessionData(String name) {
        if (allTxnSessionData != null) {
            return (Map) allTxnSessionData.get(name);
        }
        return null;
    }

    public Object getUsrSessionDataValue(Object key) {
        HashMap usrSessionData =
                (HashMap) session.getAttribute("UsrSessionData$Of$Neturbo");
        if (usrSessionData != null) {
            return usrSessionData.get(key);
        } else {
            return null;
        }

    }

    public HashMap getUsrSessionData() {
        HashMap usrSessionData =
                (HashMap) session.getAttribute("UsrSessionData$Of$Neturbo");
        if (usrSessionData != null) {
            return usrSessionData;
        } else {
            return null;
        }

    }

    /**
     * @deprecated use removeTxnSessionData(Object key),removeUsrSessionData(Object key).
     */
    public void clearCurrentTxnSessionData() {
        if (txnSessionData != null) {
            txnSessionData.clear();
        }
        txnSessionData = null;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpSession getSession() {
        return session;
    }

    public HttpSession newSession() {
        String oldSession = session.getId();
        session.invalidate();
        session = request.getSession(true);
        String newSession = session.getId();
        Log.info("Session from '" + oldSession + "' to '" + newSession + "'");
        return session;
    }

    public ActionMapping getActionMapping() {
        return actionMapping;
    }

    public String getRequestIP() {
        return requestIP;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getCurrentTxnSessionName() {
        return txnSessionName;
    }

    public void setError(NTBException errorData) {
        this.errorData = errorData;
    }

    public void setMessage(String message) {
    	if (messageData==null){
    		messageData = Collections.synchronizedList(new ArrayList());;
    	}
        messageData.add(message);
    }
    
    public List getMessage() {
        return messageData;
    }

    public String getPageLanguage() {
        Locale pageLocale = (Locale)session.getAttribute("Locale$Of$Neturbo");
        if(pageLocale!=null){
            return pageLocale.getLanguage() + "_" + pageLocale.getCountry();
        }
        return null;
    }

    public void setPageLanguage(String localStr){
        if (localStr != null && !localStr.equals("")) {
            try{
                Locale pageLocale = new Locale(localStr.substring(0, 2),
                                               localStr.substring(3, 5));
                session.setAttribute("Locale$Of$Neturbo", pageLocale);
            }catch(Exception e){
                session.setAttribute("Locale$Of$Neturbo", Config.getDefaultLocale());
                Log.error("Error reading page language", e);
            }
        }
    }

}
