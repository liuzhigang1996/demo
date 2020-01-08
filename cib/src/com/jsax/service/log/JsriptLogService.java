/**
 * @author hjs
 * 2006-9-21
 */
package com.jsax.service.log;

import java.lang.reflect.Method;

import com.jsax.core.JsaxAction;
import com.jsax.core.JsaxService;
import com.neturbo.set.core.Log;

/**
 * @author hjs
 * 2006-9-21
 */
public class JsriptLogService extends JsaxAction implements JsaxService {

	/* (non-Javadoc)
	 * @see com.jsax.core.JsaxBean#doTransaction()
	 */
	public void doTransaction() throws Exception {
		String logLevel = this.getParameter("logLevel");
		String logContent = this.getParameter("logContent");
		try{
			if (!logLevel.equals("")) {
				Class logClass = Class.forName("com.neturbo.set.core.Log");
				Method logMethod = logClass.getMethod(logLevel, new Class[] {Object.class});
				logMethod.invoke(logClass, new Object[] {logContent});
			}
		} catch (Exception e) {
			Log.error("JsriptLogService Exception", e);
		}
	}

}
