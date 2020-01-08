/*
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.neturbo.set.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author panwen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class EncodingFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub

		filterConfig = config;
		encoding = filterConfig.getInitParameter("encoding");
		String value = filterConfig.getInitParameter("ignore");
		if (value == null) {
			ignore = true;
		} else if (
			value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")) {
			ignore = true;
		} else {
			ignore = false;
		}

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(
		ServletRequest request,
		ServletResponse response,
		FilterChain chain)
		throws IOException, ServletException {
		// TODO Auto-generated method stub

		if (ignore || (request.getCharacterEncoding() == null)) {
			request.setCharacterEncoding(selectEncoding(request));

			if (request instanceof HttpServletRequest) {
				((HttpServletRequest) request).getSession().setAttribute(
					"Charset$Of$Neturbo",
					request.getCharacterEncoding());
			}
		}

		chain.doFilter(request, response);

	}

	/**
		* Take this filter out of service.
		*/
	public void destroy() {
		encoding = null;
		filterConfig = null;
	}

	/**
	* Select an appropriate character encoding to be used, based on the
	* characteristics of the current request and/or filter initialization
	* parameters. If no character encoding should be set, return
	* <code>null</code>.
	* <p>
	* The default implementation unconditionally returns the value configured
	* by the <strong>encoding</strong> initialization parameter for this
	* filter.
	*
	* @param request The servlet request we are processing
	*/
	protected String selectEncoding(ServletRequest request) {
		return (encoding);
	}

	/**
	* Returns the filterConfig.
	* @return FilterConfig
	*/
	public FilterConfig getFilterConfig() {
		return filterConfig;
	}

	/**
	* Sets the filterConfig.
	* @param filterConfig The filterConfig to set
	*/
	public void setFilterConfig(FilterConfig config) {
		filterConfig = config;
	}

	/**
	* The default character encoding to set for requests that pass through
	* this filter.
	*/
	protected String encoding = null;

	/**
	* The filter configuration object we are associated with. If this value
	* is null, this filter instance is not currently configured.
	*/
	protected FilterConfig filterConfig = null;

	/**
	* Should a character encoding specified by the client be ignored?
	*/
	protected boolean ignore = true;

}
