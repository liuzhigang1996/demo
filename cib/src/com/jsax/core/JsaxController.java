/**
 * @author hjs
 * 2006-8-8
 */
package com.jsax.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.utils.RBFactory;
import com.neturbo.set.utils.Utils;

/**
 * @author hjs
 * 2006-8-8
 */
public class JsaxController extends HttpServlet {
	
	private static final long serialVersionUID = 6172963537778679984L;
	private static boolean isSpringVersion = false;
	private static String msgResource = Config.getProperty("MessageResources");//add by linrui 20190820
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		isSpringVersion = "Y".equals(config.getInitParameter("isSpringVersion"));
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		NTBUser user = ((NTBUser) request.getSession().getAttribute("UserObject$Of$Neturbo"));
		String serviceName = Utils.null2EmptyWithTrim(request.getParameter("serviceName"));
    	
		response.setContentType("text/xml; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		RBFactory rbErrMsg = RBFactory.getInstance(msgResource);//add by linrui 20190820
		// get writer
		try {
			out = response.getWriter();
			if(out == null){
				Log.error("[JSAX]Error Getting Writer from Response Object");
				return;
			}
		} catch (IOException e) {
			Log.error("[JSAX]Error Getting Writer from Response Object", e);
			return;
		}
		
		// set charset
		try {
			request.setCharacterEncoding("UTF-8");
			//String strNameEn = java.net.URLDecoder.decode(maForm.nameEn(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.error("[JSAX]Error Setting Character Encoding to Request Object", e);
		}
        
        // get service to call
		if(serviceName==null || "".equals(serviceName=serviceName.trim())){
			Log.error("[JSAX]ServiceName is null or empty");
//			sendErrMsg(out, "System busy, please try again later");
			//mod by linrui for mul-language 20190820
			sendErrMsg(out, rbErrMsg.getString("err.sysbusy"));
			return;
		}

		// invoke service
        try {
        	Log.debug("[JSAX]Receiving request '" + serviceName + "'");
        				
	        JsaxService jsaxService = null;
			// find jsax service
	        if (isSpringVersion) {
		        /*for spring version*/
				Class configClass = Class.forName("com.neturbo.set.core.Config");
				Method configGetAppContextMethod = configClass.getMethod("getAppContext", new Class[]{});
				Object appContextClass = configGetAppContextMethod.invoke(configClass, new Object[]{});
				
				Method appContextGetBeanMethod = appContextClass.getClass().getMethod("getBean", new Class[]{String.class});
				jsaxService = (JsaxService) appContextGetBeanMethod.invoke(appContextClass, new Object[]{serviceName});
				
	        } else {
	    		RBFactory mapping = RBFactory.getInstance("JSAX_CONFIG");
	    		String clazz = mapping.getString(serviceName);
	        	jsaxService = (JsaxService) Class.forName(clazz).newInstance();
	        }

	        if(jsaxService == null){
	        	Log.error("[JSAX]Service '" + serviceName + "' not found");
//				sendErrMsg(out, "System busy, please try again later");
				//mod by linrui for mul-language 20190820
				sendErrMsg(out, rbErrMsg.getString("err.sysbusy"));
	        } else {
	        	if (! (jsaxService instanceof  NotLogined)){
	        		// check user in session
	                if(user == null){
	                	Log.error("[JSAX]User Object in Seesion is null(Session invalidate.)");
//	        			sendErrMsg(out, "Session invalidate.");
	                	//mod by linrui for mul-language 20190820
	                	sendErrMsg(out, rbErrMsg.getString("err.sessioninvalidate"));
	        			return;
	                }
	        	}
	        	String responseXml = jsaxService.performService(request, user);
				out.print(responseXml);
	        }
			
		} catch (Exception e) {
			Log.error("[JSAX]Error invoking service '" + serviceName +"'", e);
//			sendErrMsg(out, "System busy, please try again later");
			//mod by linrui for mul-language 20190820
			sendErrMsg(out, rbErrMsg.getString("err.sysbusy"));
			
		} finally {
			if(out != null){
				out.flush();
				out.close();
			}
		}
	
		
	}
	
	private void sendErrMsg(PrintWriter out, String errMsg) {
		Log.error("[JSAX]Sending error message:"+errMsg);
		/*out.print("<ajax-response><response type=\"object\" id=\"errProcessor\">" + errMsg + "</response></ajax-response>");*/
		out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>") ;
		out.print("<ajax-response requestId=\"\"><response id=\"errProcessor\" type=\"object\"><errMsg><error>" + errMsg + "</error><errorName /><errorLabel /><errorLayer>-1</errorLayer><errorIndex>0</errorIndex></errMsg></response></ajax-response>");
	}


}
