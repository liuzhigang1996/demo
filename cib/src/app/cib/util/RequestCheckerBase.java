package app.cib.util;

import javax.servlet.http.HttpServletRequest;

import com.neturbo.set.exception.NTBException;
import com.neturbo.set.xml.XMLElement;

public abstract class RequestCheckerBase {
	XMLElement config;
	
	public XMLElement getConfig() {
		return config;
	}

	public void setConfig(XMLElement config) {
		this.config = config;
	}

	abstract String getParameter(HttpServletRequest request, String name, String preValue) throws NTBException;
	abstract String getHeader(HttpServletRequest request, String name, String preValue)throws NTBException;
}
