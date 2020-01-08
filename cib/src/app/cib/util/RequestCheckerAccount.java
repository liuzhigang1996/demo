package app.cib.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import app.cib.bo.bnk.BankUser;
import app.cib.bo.sys.CorpPermission;
import app.cib.bo.sys.CorpUser;
import app.cib.service.sys.CorpUserService;
import app.cib.service.txn.CorpTransferService;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.xml.XMLElement;

public class RequestCheckerAccount extends RequestCheckerBase {

	private HashMap methods = new HashMap();
	private Class types[] = { HttpServletRequest.class, String.class };
    private static int TD_LENGTH = 15;
	/**
	 * @author by PQ
	 * @date 2014-8-1
	 * @Description: override method getParameter to do xss filter on parameter
	 *               name and value; if the original value is needed, using the
	 *               super.getParameterValues(name) to get it;
	 *               getParameterNames,getParameterValues and getParameterMap
	 *               may also need to be overrided;
	 */
	String getParameter(HttpServletRequest request, String name, String value)
			throws NTBException {
		if (value == null) {
			value = request.getParameter(name);
		}
		
		String curAction = request.getRequestURI();
		curAction = curAction.substring(5, curAction.length() - 3);
		String curMethod = request.getParameter("ActionMethod");
		String curField = name;

		if(request.getRequestURI().endsWith("jsax")){
			curAction = request.getRequestURI();
			curAction = curAction.substring(5);
			curMethod = request.getParameter("serviceName");
		}
		
		XMLElement conditions = config.getChildByName("condition-list");
		if (conditions != null) {
			List conditionList = conditions.getChildren();
			for (int i = 0; i < conditionList.size(); i++) {
				XMLElement cond = (XMLElement) conditionList.get(i);
				String action = cond.getChildByName("action").getText();
				String method = cond.getChildByName("method").getText();
				String field = cond.getChildByName("field").getText();
				String check = cond.getChildByName("check").getText();

				if (action.equals(curAction) && method.equals(curMethod)
						&& field.equals(curField)) {
					Log.info("Checking process("+check+"), action " + action + "."
							+ method + ", field: "+ field);
					runMethod(check, new Object[] { request, value });
				}
			}
		}

		return value;
	}

	public void checkAccount(HttpServletRequest request, String account)
			throws NTBException {
		HttpSession session = request.getSession();
		NTBUser user = ((NTBUser) session.getAttribute("UserObject$Of$Neturbo"));
		if (user == null) {
			throw new NTBException("err.sys.NotLogined");
		}
		if (user instanceof BankUser) {
			return;
		}
		
		if(null==account){
			return ;
		}
		CorpUser corpUser = (CorpUser) user;

		
		
		
		boolean hasPermission = false;
		List accList = corpUser.getAccountList();
		CorpPermission obj = null;
		for (int i = 0; i < accList.size(); i++) {
			obj = (CorpPermission) accList.get(i);
			if (obj.getPermissionResource().trim().equals(account)) {
				hasPermission = true;
			}
		}
		if("9999999999".equals(account)){
			hasPermission = true;
		}

		if(!hasPermission){
			//security update 
			String corpId = corpUser.getCorpId() ;
			
			RcCorporation rcCorp = new RcCorporation(
					RcCorporation.SHOW_CORP_WITHOUT_ROOT);
			rcCorp.setArgs(user);
			List corpList = getCorpList(corpId, "", rcCorp);
			
			CorpTransferService corpTransferService = (CorpTransferService) Config
			.getAppContext().getBean("CorpTransferService");
			if("".equals(account)){
				return ;
			}
			String fromCorpId = corpTransferService.getCorpIdByAccount(account) ;
			
			for(int i=0;i<corpList.size();i++){
				Map corpMap = (Map)corpList.get(i) ;
				String coId = (String)corpMap.get("CORP_ID") ;
				String paentCorpId = (String)corpMap.get("PARENT_CORP") ;
				if(coId.equals(fromCorpId)){
					hasPermission = true ;
					break ;
				}else if(paentCorpId.equals(fromCorpId)){
					hasPermission = true ;
					break ;
				}
			}
		}
		
		if (!hasPermission) {
			if (TD_LENGTH == account.length()) {//add by linrui 20190531
				throw new NTBException("err.sys.IllegalAccountAccess.timedeposit");
			} else {
				throw new NTBException("err.sys.IllegalAccountAccess");
			}
		}

	}

	public void checkCorp(HttpServletRequest request, String corpId)
			throws NTBException {
		HttpSession session = request.getSession();
		NTBUser user = ((NTBUser) session.getAttribute("UserObject$Of$Neturbo"));
		if (user == null) {
			throw new NTBException("err.sys.NotLogined");
		}
		if (user instanceof BankUser) {
			return;
		}
		CorpUser corpUser = (CorpUser) user;

		boolean hasPermission = false;
		String userCorpId = corpUser.getCorpId();
		if (userCorpId.equals(corpId)) {
			hasPermission = true;
		}
		if(!hasPermission){
			RcCorporation rcCorp = new RcCorporation(
					RcCorporation.SHOW_CORP_WITHOUT_ROOT);
			rcCorp.setArgs(user);
			List corpList = getCorpList(userCorpId, "", rcCorp);
			for(int i=0;i<corpList.size();i++){
				Map corpMap = (Map)corpList.get(i) ;
				String coId = (String)corpMap.get("CORP_ID") ;
				if(coId.equals(corpId)){
					hasPermission = true ;
					break ;
				}
			}
		}
		
		if (!hasPermission) {
			throw new NTBException("err.sys.IllegalAccountAccess");
		}

	}

	public void checkUser(HttpServletRequest request, String userId) throws NTBException{
		HttpSession session = request.getSession();
		NTBUser user = ((NTBUser) session.getAttribute("UserObject$Of$Neturbo"));
		if (user == null) {
			throw new NTBException("err.sys.NotLogined");
		}
		if (user instanceof BankUser) {
			return;
		}
		CorpUser corpUser = (CorpUser) user;
		
		CorpUserService corpUserService = (CorpUserService) Config
		.getAppContext().getBean("corpUserService");
		//modified by lzg for GAPMC-EB-001-0040
		//CorpUser loadUser = corpUserService.load(userId);
		CorpUser loadUser = corpUserService.loadWithCorpId(userId,corpUser.getCorpId());
		//modified by lzg end
		if(null == loadUser || !(corpUser.getCorpId().equals(loadUser.getCorpId()))){
			throw new NTBException("err.sys.IllegalUserIdAccess");
		}
	}
	
	public void checkGroup(HttpServletRequest request, String corpId){
		
	}
	
	public void checkBank(HttpServletRequest request, String corpId){
		
	}

	/**
	 * @author by PQ
	 * @date 2014-8-1
	 * @Description: override method getHeader to do xss filter on parameter
	 *               name and value; if the original value is needed, using the
	 *               super.getHeaders(name) to get it; getHeaderNames may also
	 *               need to be overrided;
	 */
	public String getHeader(HttpServletRequest request, String name,
			String value) {
		if (value == null) {
			value = request.getHeader(name);
		}
		return value;
	}

	private void runMethod(String name, Object args[]) throws NTBException {
		synchronized (methods) {
			Method method = (Method) methods.get(name);
			try {
				if (method == null) {
					method = this.getClass().getMethod(name, types);
					methods.put(name, method);
				}
				method.invoke(this, args);
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
			} catch (SecurityException ex) {
				Log.error("Error running method " + name, ex);
				throw new NTBException("err.sys.GeneralError");
			} catch (NoSuchMethodException ex) {
				Log.error("Error running method " + name, ex);
				throw new NTBException("err.sys.GeneralError");
			}
		}

	}
	
	
	
	public List getCorpList(String corpId, String subCorpId, RcCorporation rcCorp) throws NTBException{
		boolean hasRight = false;
		if(subCorpId.equals("")){
			hasRight = true;
		}
		List corpList = new ArrayList();
		List keyList = rcCorp.getKeys();
		for (int i = 0; i < keyList.size(); i++) {
			String key = (String) keyList.get(i);
			if (!key.equals(corpId)) {
				Map corpMap = rcCorp.getObject(key);
				corpList.add(corpMap);
				if(subCorpId.equals(corpMap.get("CORP_ID"))){
					hasRight = true;
				}
			}
		}
		if(!hasRight){
			throw new NTBException("err.sys.IllegalInput");
		}
		return corpList;

	}
}
