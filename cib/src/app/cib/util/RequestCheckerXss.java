package app.cib.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import com.blogspot.radialmind.html.HTMLParser;
import com.blogspot.radialmind.xss.XSSFilter;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.utils.Utils;
import com.neturbo.set.xml.XMLElement;

public class RequestCheckerXss extends RequestCheckerBase {

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
			value = request.getParameter(xssEncode(name));
		}
		if (value != null) {
			value = xssEncode(value);
			//value = xssEncode1(value);
		}
		return value;
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
			String value) throws NTBException {

		if (value == null) {
			value = request.getHeader(xssEncode(name));
		}
		if (value != null) {
			value = xssEncode(value);
		}
		return value;
	}

	/**
	 * @author by PQ
	 * @date 2014-8-1
	 * @Description: replace Half Angle characters which may case xss
	 *               vulnerability to Angle characters
	 */
	private static synchronized String xssEncode(String s) {
		{
			if (s == null || s.isEmpty()) {
				return s;
			}
			try {
				//s = Utils.replaceStr(s, "/**/","");
				if(s.contains("<")){
					s = s.toLowerCase();
				}
				
				s = "<b>" + s + "</b>";
				StringReader reader = new StringReader(s);
				StringWriter writer = new StringWriter();
				HTMLParser.process(reader, writer, new XSSFilter(), true);
				String res = writer.toString();
				res = res.substring(3, res.length()-4);
				
				for (int i = 0; i < 100; i++) {
					// some other avoid check
					String temRes = res;
					res = xssEncode1(res);
					if(temRes.equals(res))break;
						
				}
				return res;
			} catch(Exception ex){
				ex.printStackTrace();
			}
			return null;
		}

	}

	private static String xssEncode1(String value) 
	{
		//Log.info("xssEncode1(String) start");
		
		
		if (value != null) 
		{
			//Log.info("replaced before:value="+value);
			// NOTE: It's highly recommended to use the ESAPI library and
			// uncomment the following line to
			// avoid encoded attacks.
			// value = ESAPI.encoder().canonicalize(value);

			// Avoid null characters
			// value = value.replaceAll("", "");\
			try 
			{
				//value=java.net.URLDecoder.decode(value,   "utf-8");
				value=value.replace("%3B", ";");
				value=value.replace("%2F", "/");
				value=value.replace("%22", "\"");
				value=value.replace("%3C", "<");
				value=value.replace("%3E", ">");
			} 
			catch (Exception e1) 
			{
				Log.error("XSS java.net.URLDecoder.decode error value= "+value);
			}
			if (value.contains("\";"))
			{
				try
				{
					value=value.replace(value.substring(value.indexOf("\";")),"");
				}
				catch(Exception e)
				{
					Log.error("cut String A failed the string= "+value);
				}
			}
			
			Pattern scriptPattern = null;
			
			
			
			
			
			value = value.replaceAll("", "");
			scriptPattern = Pattern.compile(";(.*?)//",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");
			
			// Avoid anything between /* */ tags
			 scriptPattern = Pattern.compile("/\\*(.*?)\\*/",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");
			
			// Avoid anything between script tags
			scriptPattern = Pattern.compile("<script>(.*?)</script>",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			//Avoid anything in a src='...' type of expression
			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Remove any lonesome </script> tag
			scriptPattern = Pattern.compile("</script>",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Remove any lonesome <script ...> tag
			scriptPattern = Pattern.compile("<script(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid eval(...) expressions
			scriptPattern = Pattern.compile("eval\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");
			
			// Avoid alert expressions
			scriptPattern = Pattern.compile("alert\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");
			
			// Avoid confirm expressions
			scriptPattern = Pattern.compile("confirm\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");
			
			// Avoid prompt expressions
			scriptPattern = Pattern.compile("prompt\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");
			
			// Avoid document.location expressions
			scriptPattern = Pattern.compile("document.",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			// Remove the checker for url "/WEB-INF/pages/srv/document_archive/*", update by li_zd at 20171214
			if (!value.contains("document_archive")) {
				value = scriptPattern.matcher(value).replaceAll("");
			}
			
			// Avoid expression(...) expressions
			scriptPattern = Pattern.compile("expression\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid javascript:... expressions
			scriptPattern = Pattern.compile("javascript:",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid vbscript:... expressions
			scriptPattern = Pattern.compile("vbscript:",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid onload= expressions
			scriptPattern = Pattern.compile("onload(.*?)=",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");
			
			
			
			if (value.contains("\"")&&value.contains("//"))
			{
				try
				{
					if(value.indexOf("\"")<=value.indexOf("//"))
					{
						value=value.replace(value.substring(value.indexOf("\""),value.indexOf("//")+2), "");
					}
					else
					{
						value=value.replace(value.substring(value.indexOf("//"),value.indexOf("\"")+1), "");	
					}
				}
				catch(Exception e)
				{
					Log.error("cut String B failed the string= "+value);
				}
			}
			
			
			XMLElement rootElement=Config.getConfigXML("ScriptCheckConfig");
			List<XMLElement> children=rootElement==null?null:rootElement.getChildren();
			if(children!=null)
			{
				for(XMLElement child:children)
				{
					String pattern=child.getText();
					scriptPattern=Pattern.compile(pattern,Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
					value = scriptPattern.matcher(value).replaceAll("");
				}
				
			}
			/*modify by linrui for test temp 20190508*/
			/*String patterns[] = "document.location,alert".split(",");
			for(int i =0 ; i<2 ; i++){
				String pattern=patterns[i];
				scriptPattern=Pattern.compile(pattern,Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
				value = scriptPattern.matcher(value).replaceAll("");
			}*/
			/*mod by linrui end*/
		}
		
		
		return value;
	}


	public static void main(String[] args) {
//		long t1 = new Date().getTime();
		//for (int i = 0; i < 10000; i++) {
			//String src = "aaa bb " +"\";ghdfhfghdfhdfhdf//";
		String src="http://localhost:8888/ebank/bank/login.jsp?lang=E<scralertipt>alealertrt(1)</scr<script>ipt>&loginMethod=login\";confirm(\"http://www.google.com\")//";
			/*String res = src;
			System.out.println("at first "+res);
			res = xssEncode(res);
			System.out.println("second time "+res);
			res = xssEncode1(res);
			System.out.println("third time "+res);
		//}
		long t2 = new Date().getTime();
		System.out.println("Time:" + String.valueOf(t2-t1));*/
		src = xssEncode(src);
		System.out.println("aasdsss"+src);
//		System.out.println(xssEncode1(src));
	
	
	}
}
