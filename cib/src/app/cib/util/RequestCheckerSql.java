package app.cib.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.neturbo.set.core.Log;
import com.neturbo.set.exception.NTBException;
import com.neturbo.set.xml.XMLElement;

public class RequestCheckerSql extends RequestCheckerBase {
	private static List parameteres;

	private void init() {
		XMLElement conditions = config.getChildByName("condition-list");
		if (conditions != null) {
			parameteres = new ArrayList();
			List conditionList = conditions.getChildren();
			for (int i = 0; i < conditionList.size(); i++) {
				XMLElement cond = (XMLElement) conditionList.get(i);
				String parameter = cond.getChildByName("parameter").getText();
				parameteres.add(parameter);
			}
		}
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
	String getParameter(HttpServletRequest request, String name, String value)
			throws NTBException {
		if (parameteres == null) {
			init();
		}
		if (value == null) {
			value = request.getParameter(name);
		}
		if (value != null && !"".equals(value) && parameteres.contains(name)) {
			value = sqlValidate(name, value, request);
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
			value = request.getHeader(name);
		}
		return value;
	}


	//效验
	private static String sqlValidate(String name, String value, HttpServletRequest request) throws NTBException {
		String valueLower = value.toLowerCase();//统一转为小写
	    String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
	    		"char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
	    		"table|from|grant|use|group_concat|column_name|" +
	            "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
	            "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可以手动添加
//	    String badStr1 = "'<>and<>exec<>insert<>select<>delete<>update<>count<>*<>%<>chr<>mid<>master<>truncate<>char<>declare<>;<>or<>+<>,";
	    String[] badStrs = badStr.split("\\|");
	    for (int i = 0; i < badStrs.length; i++) {
	        if (valueLower.indexOf(badStrs[i]) >= 0) {
				Log.error("Request filtered with error: [SQL Injection], the param=" + name + ",value=" + valueLower + " is illegal");
				valueLower = valueLower.replaceAll(badStrs[i], "");
	        }
	    }
	    if (valueLower.equals(value.toLowerCase())) {
	    	valueLower = value;
	    }
	    return valueLower;
	}
}
