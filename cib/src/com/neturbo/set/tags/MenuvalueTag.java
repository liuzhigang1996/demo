package com.neturbo.set.tags;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.*;
import java.util.*;
import com.neturbo.set.xml.*;
import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

import javax.servlet.jsp.tagext.TagSupport;

public class MenuvalueTag extends TagSupport {
	private String inlevel;

	private String property;

	private static String menuResource = Config.getProperty("MenuResources");

	public MenuvalueTag() {
	}

	public int doStartTag() {
		try {
			// 获得输出
			JspWriter out = pageContext.getOut();
			// 从session获得语言种类
			HttpSession session = pageContext.getSession();
			Locale locale = (Locale) session.getAttribute("Locale$Of$Neturbo");
			NTBUser user = ((NTBUser) session
					.getAttribute("UserObject$Of$Neturbo"));
			if (locale == null) {
				if (user != null) {
					locale = user.getLanguage();
				}
			}
			if (locale == null) {
				locale = Config.getDefaultLocale();
			}

			// 查询父列表
			MenuTag parent = (MenuTag) TagSupport.findAncestorWithClass(this,
					MenuTag.class);

			if (inlevel != null) {
				while (parent != null
						&& !parent.getLevel().equals(this.getInlevel())) {
					MenuTag parent_tmp = (MenuTag) TagSupport
							.findAncestorWithClass(parent, MenuTag.class);
					if (parent_tmp != null) {
						parent = parent_tmp;
					} else {
						Log.warn("No Menu(" + this.getInlevel()
								+ ") exist when processing menuvalue Tag");
						parent = null;
						break;

					}
				}
			}

			if (parent != null) {
				XMLElement parentElement = parent.getElement();
				String value = parentElement.getAttribute(property);
				if (property.equals("label")) {
					String localeName = locale.toString();
					//if (!localeName.equals("en_US")) {
						RBFactory rbMenu = RBFactory.getInstance(menuResource,
								localeName);
						String tempName = Utils.replaceStr(value, " ", "_");
						value = rbMenu.getString(tempName);
						value = " " + value + " ";
					//}
				} else if (property.equals("target")) {
					for (XMLElement ancestorElement = parentElement; value == null
							&& ancestorElement != null; ancestorElement = ancestorElement
							.getParent()) {
						value = ancestorElement.getAttribute(property);
					}
				} 
				/*add by linrui 20180730 for CR230 different language dispatch different URL*/
				   else if(parentElement.getAttribute("label").equalsIgnoreCase("Contact Us") ||
						parentElement.getAttribute("label").equalsIgnoreCase("Forms Download") ||
						parentElement.getAttribute("label").equalsIgnoreCase("Macau Holiday") ||
						parentElement.getAttribute("label").equalsIgnoreCase("Service Schedule")) {
					   String labelName = parentElement.getAttribute("label").toString().trim();
					   String localeName = locale.toString();
					   RBFactory rbMenu = RBFactory.getInstance(menuResource,
							localeName);
					   value = rbMenu.getString(labelName.replaceAll(" ", "_") + "_URL");
				}//end
                 
				out.print(value);
			} else {
				Log.warn("No menu exist when processing menuvalue Tag");
			}
		} catch (Exception e) {
			Log.warn("Custom Tag Process error (ListData)", e);
		}
		return (SKIP_BODY);

	}

	public String getInlevel() {
		return inlevel;
	}

	public void setInlevel(String inlevel) {
		this.inlevel = inlevel;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
