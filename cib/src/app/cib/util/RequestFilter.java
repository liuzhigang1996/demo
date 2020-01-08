package app.cib.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.core.NTBUser;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.NTBProperties;
import com.neturbo.set.utils.RBFactory;

/**
 * @ClassName: XSSFilter
 * @author by PQ
 * @date 2014-8-1
 * @Description: TODO
 */
public class RequestFilter implements Filter {
	private String errorPage;//
	protected String encoding = null;

	public void init(FilterConfig filterConfig) throws ServletException {
		errorPage = filterConfig.getInitParameter("errorPage");
		encoding = filterConfig.getInitParameter("encoding");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException{
		request.setCharacterEncoding(encoding);
		RequestCheckWrapper requestCheck = new RequestCheckWrapper(
				(HttpServletRequest) request);
		Enumeration paras = request.getParameterNames();
		String paraName = null;
		String errorValue = null;

		//add by mjt Set-cookie:Secure
		Cookie[] cookies = ((HttpServletRequest) request).getCookies();
		if (cookies != null) {
            Cookie cookie = cookies[0];
            if (cookie != null) {
            	/*cookie.setMaxAge(3600);
            	cookie.setSecure(true);
            	resp.addCookie(cookie);*/	
            	//Servlet 2.5
            	String value = cookie.getValue();
            	StringBuilder builder = new StringBuilder();
            	builder.append("JSESSIONID=" + value + "; ");
            	builder.append("Secure; ");
            	builder.append("HttpOnly; ");
            	Calendar cal = Calendar.getInstance();
            	cal.add(Calendar.HOUR, 1);
            	Date date = cal.getTime();
            	Locale locale = Locale.CHINA;
            	SimpleDateFormat sdf = 
            			new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",locale);
            	builder.append("Expires=" + sdf.format(date));
            	((HttpServletResponse) response).setHeader("Set-Cookie", builder.toString());
            }
    }

		//end
		
		while (paras.hasMoreElements()) {
			paraName = (String) paras.nextElement();
			String paraValue = requestCheck.getParameter(paraName);
			if (paraValue.startsWith("{exception:")) {
				errorValue = paraValue.substring(11, paraValue.length()-1);
				break;
			}
		}
		if (errorValue != null) {
			/*
			response.getWriter()
					.write("Critical Error: Illegal input on field:" + paraName);
			*/
            HttpSession session = ((HttpServletRequest)request).getSession();
            session.setAttribute("ErrorData$Of$Neturbo", new NTBException(errorValue));
			HttpServletResponse res = (HttpServletResponse) response;
			String contextPath = ((HttpServletRequest)request).getContextPath();
			Log.error("Request filtered with error: " + String.valueOf(errorValue));
			
			//modify by long_zg 2014-08-28 for BOB_security_update
			/*res.sendRedirect(contextPath + errorPage);*/
			HttpServletRequest httpRequest = (HttpServletRequest)request ;
			String url = httpRequest.getRequestURI() ;
			if(url.endsWith("jsax")){
				NTBUser user = ((NTBUser) httpRequest.getSession().getAttribute("UserObject$Of$Neturbo"));
				Locale pageLocale = null ;
	            if (null != user) {
	            	   pageLocale = user.getLanguage();
	            }
	            if(pageLocale == null){
	            	pageLocale = Config.getDefaultLocale();
	            }

				try {
					RBFactory rb = RBFactory.getInstance("app.cib.resource.common.errmsg",pageLocale.toString());
					//NTBProperties properties = new NTBProperties("app.cib.resource.common.errmsg",pageLocale) ;
					errorValue  = rb.getString(errorValue) ;
				} catch (Exception e) {
					
				}
				
				HttpServletResponse httpResponse = (HttpServletResponse)response ;
				httpResponse.setContentType("text/xml; charset=utf-8");
				httpResponse.setHeader("Cache-Control", "no-cache");
				PrintWriter out = httpResponse.getWriter();
				sendErrMsg(out, errorValue) ;
			} else {
				res.sendRedirect(contextPath + errorPage);
			}
			
		} else {
			
			//Log.info("Request filtered sucessfullly");
			//modified by lzg for CSRF attack
			HttpServletRequest req = (HttpServletRequest) request;  
	        HttpServletResponse res = (HttpServletResponse) response;
			String referer = req.getHeader("referer");
			Log.info("****referer****" + referer);
			//add by linrui for XSS request 20190505
	        res.setHeader("X-Frame-Options", "SAMEORIGIN");
	        res.setHeader("X-xss-protection", "1;mode=block");
	        res.setHeader("X-Content-Type-Options", "nosniff ");
	        //end
			if (referer != null && !referer.contains(req.getServerName())) {  
	            req.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);  
	        } else {
	        	chain.doFilter(requestCheck, response);
	        }
			//modified by lzg end
		}

	}

	public void destroy() {

	}

	public String getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}
	
	private void sendErrMsg(PrintWriter out, String errMsg) {
		
		Log.error("[JSAX]Sending error message:"+errMsg);
//		out.print("<ajax-response><response type=\"object\" id=\"errProcessor\">" + errMsg + "</response></ajax-response>");
		out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>") ;
		out.print("<ajax-response requestId=\"\"><response id=\"errProcessor\" type=\"object\"><errMsg><error>" + errMsg + "</error><errorName /><errorLabel /><errorLayer>-1</errorLayer><errorIndex>0</errorIndex></errMsg></response></ajax-response>");
	}

}
