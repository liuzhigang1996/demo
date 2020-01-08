package app.cib.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.blogspot.radialmind.html.HTMLParser;
import com.blogspot.radialmind.xss.XSSFilter;
import com.jsax.core.JsaxService;
import com.neturbo.set.core.Config;
import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.xml.XMLElement;

/**
 * @ClassName: XssHttpServletRequestWrapper
 * @author: by PQ
 * @date 2014-8-1
 * @Description: to filter httpRequest parameters
 */
public class RequestCheckWrapper extends HttpServletRequestWrapper {

	private static XMLElement requestCheckConfig;
	private static List requestCheckerList;

	// 初始化检查器列表
	static {
		requestCheckConfig = Config.getConfigXML("RequestCheckConfig");
		List checkerConfigs = requestCheckConfig.getChildren();
		requestCheckerList = new ArrayList();
		for (int i = 0; i < checkerConfigs.size(); i++) {
			XMLElement checkerConfig = (XMLElement) checkerConfigs.get(i);
			String checkerId = checkerConfig.getAttribute("id");
			String checkerClassName = checkerConfig.getAttribute("class");
			RequestCheckerBase checkerInstance;
			try {
				checkerInstance = (RequestCheckerBase) Class.forName(
						checkerClassName).newInstance();
				XMLElement config = checkerConfig.getChildByName("config");
				checkerInstance.setConfig(config);
				requestCheckerList.add(checkerInstance);
			} catch (Exception e) {
				Log.error("Error creating request checker instance of "
						+ checkerClassName, e);
			}
		}
	}

	HttpServletRequest orgRequest = null;

	public RequestCheckWrapper(HttpServletRequest request) {
		super(request);
		orgRequest = request;
	}

	/**
	 * @author by PQ
	 * @date 2014-8-1
	 * @Description: override method getParameter to do xss filter on parameter
	 *               name and value; if the original value is needed, using the
	 *               super.getParameterValues(name) to get it;
	 *               getParameterNames,getParameterValues and getParameterMap
	 *               may also need to be overrided;
	 */
	@Override
	public String getParameter(String name) {
		String value = null;
		for (int i = 0; i < requestCheckerList.size(); i++) {
			RequestCheckerBase checkerInstanceBase = (RequestCheckerBase) requestCheckerList
					.get(i);
			try {
				value = checkerInstanceBase.getParameter(orgRequest, name,
						value);
			} catch (NTBException e) {
				value = "{exception:" + e.getMessage() + "}";
			}
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
	@Override
	public String getHeader(String name) {
		String value = null;
		for (int i = 0; i < requestCheckerList.size(); i++) {
			RequestCheckerBase checkerInstanceBase = (RequestCheckerBase) requestCheckerList
					.get(i);
			try {
				value = checkerInstanceBase.getHeader(orgRequest, name, value);
			} catch (NTBException e) {
				value = "{exception:" + e.getMessage() + "}";
			}
		}
		return value;
	}

	/**
	 * @author by PQ
	 * @date 2014-8-1
	 * @Description: get original request
	 */
	public HttpServletRequest getOrgRequest() {
		return orgRequest;
	}

	/**
	 * @author by PQ
	 * @date 2014-8-1
	 * @Description: Static method to get original request
	 */
	public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
		if (req instanceof RequestCheckWrapper) {
			return ((RequestCheckWrapper) req).getOrgRequest();
		}
		return req;
	}
}
