/**
 * @author hjs
 * 2006-8-23
 */
package com.jsax.core;

import com.neturbo.set.core.Log;
import com.neturbo.set.xml.XMLElement;

/**
 * @author hjs
 * 2006-8-23
 */
public class SubElement extends XMLElement {
	
	public SubElement() {
		super();
	}
	
	public SubElement(String tagName) {
		super(tagName);
	}
	
	// TODO 未完成方法
	public String element2String() {
		Log.warn("This Method has not been completed, please used it later");
		return "";
	}
}
