/**
 * @author hjs
 * 2006-8-8
 */
package com.jsax.core;

import javax.servlet.http.HttpServletRequest;

import com.neturbo.set.core.NTBUser;

/**
 * @author hjs
 * 2006-8-8
 */
public interface JsaxService {
	
	public String performService(HttpServletRequest request, NTBUser user);

}
