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

public class RequestChkAcctCcy extends RequestCheckerBase {

	private HashMap methods = new HashMap();
	private Class types[] = { HttpServletRequest.class, String.class, String.class };
	/**
	 * @author by LINRUI
	 * @date 2019-6-13
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
				String ccy = cond.getChildByName("ccyField").getText();
                ccy = request.getParameter(ccy);
				if (action.contains(curAction) && method.equals(curMethod)
						&& field.equals(curField)) {
					Log.info("Checking process("+check+"), action " + action + "."
							+ method + ", field: "+ field);
					runMethod(check, new Object[] { request, value, ccy });
				}
			}
		}

		return value;
	}

	//add by linrui 20190612 for pwc check currency
	public void checkAccountCurrency(HttpServletRequest request, String account, String currency)
	throws NTBException {
		
		HttpSession session = request.getSession();
		boolean hasPermission = false;
		List accList = (List)session.getAttribute("ResultData$Of$"+account);
		if(accList.contains(currency))
			hasPermission = true;
		if (!hasPermission) {
			throw new NTBException("err.sys.IllegalAccountAccess.errorCurrency");
		}
		
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
			// 锟斤拷锟矫凤拷锟斤拷
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
}
