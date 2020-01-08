package com.neturbo.set.tags;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.neturbo.set.core.*;
import com.neturbo.set.utils.*;

public class ListTag extends BodyTagSupport {
    private String name;
    private List rows;
    int index;
    private String inlevel;
    private Object row;
    private String from;
    private String level;
    private boolean pageFlag = false;
    private boolean noRecordFlag = false;
    private String pageStyle;
    private String pageJsp;
    private int pageTotalNo;
    private int recordTotalNo;
    private int pageCurrentNo;
    private int pageStartRow;
    private int pageEndRow;
    private String showPageRows;
    private String showNoRecord;
    private String pageClass;

    public ListTag() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getRow() {
        return row;
    }

    private void setRow(Object row) {
        this.row = row;
    }

    public int doStartTag() throws JspException {
        try {

            HttpSession session = pageContext.getSession();
            name = TagUtils.runActionMethod(name, session);

            //��session�����������
            Locale locale = (Locale) session.getAttribute("Locale$Of$Neturbo");
            NTBUser user = ((NTBUser) session.getAttribute(
                    "UserObject$Of$Neturbo"));
            if (locale == null) {
                if (user != null) {
                    locale = user.getLanguage();
                }
            }
            if (locale == null) {
                locale = Config.getDefaultLocale();
            }

            Object valueObj = null;
            HashMap parameters =
                    (HashMap) session.getAttribute("Parameters$Of$Neturbo");
            HashMap resultData =
                    (HashMap) session.getAttribute("ResultData$Of$Neturbo");
            HashMap pageData =
                    (HashMap) session.getAttribute("PageData$Of$Neturbo");
            
            Log.info("resultData "+name +" "+resultData.get(name));
            //��ø��б�
            ListTag parent =
                    (ListTag) TagSupport.findAncestorWithClass(this, ListTag.class);

            //���ָ�����б���ѭ���ϲ�
            if (inlevel != null) {
                while (parent != null
                       && !parent.getLevel().equals(this.getInlevel())) {
                    ListTag parent_tmp =
                            (ListTag) TagSupport.findAncestorWithClass(
                                    parent,
                                    ListTag.class);
                    if (parent_tmp != null) {
                        parent = parent_tmp;
                    } else {
                        Log.warn(
                                "No list("
                                + this.getInlevel()
                                + ") exist when processing list Tag");
                        parent = null;
                        break;
                    }
                }
            }

            if (parent != null) {
                //�Ӹ��б��л�õ�ǰ�����
                Object rowData = parent.getRow();
                //�ӵ�ǰ�������ȡ��ָ��ֵ
                if (name != null && rowData != null) {
                    valueObj = ((HashMap) rowData).get(name);
                } else {
                    valueObj = rowData;
                }
            } else {
            	Log.info("ListTag from="+from);
                if (from == null) {
                    //��session������ύ�Ĳ������
                	Log.info("ListTag parameters="+parameters);
                    if (parameters != null) {
                        valueObj = parameters.get(name);
                    }
                    //��session��ý�����
                    Log.info("ListTag get valueObj from resultData="+valueObj);
                    if (valueObj == null) {
                        if (resultData != null) {
                            valueObj = resultData.get(name);
                        }
                    }
                    Log.info("ListTag get valueObj from resultData end="+valueObj);
                } else {
                	Log.info("ListTag from="+from);
                    if (from.toUpperCase().equals("PARAMETERS")) {
                        valueObj = parameters.get(name);
                    }
                    if (from.toUpperCase().equals("RESULTDATA")) {
                        if (resultData != null) {
                            valueObj = resultData.get(name);
                        }
                    }
                }
            }

            //�����������pageData�л�ȡ���
            if(valueObj == null && pageData != null){
               valueObj = pageData.get(name);
            }

            if (valueObj != null) {
                //��ʼ���б�
                rows = (List) valueObj;
            } else {
                rows = new ArrayList();
            }
            int iPageRows = Utils.nullEmpty2Zero(showPageRows);
            pageEndRow = rows.size();
            recordTotalNo = rows.size();
            pageCurrentNo =
                    Utils.nullEmpty2Zero(resultData.get("CURRENT_PAGE_NO"));

            Log.info("ListTag pageEndRow="+pageEndRow);
            Log.info("ListTag recordTotalNo="+recordTotalNo);
            Log.info("ListTag pageCurrentNo="+pageCurrentNo);
            //����б?��Ϊ�㣬�����
            if (pageEndRow > 0) {
                //ȡ��һ��
                pageStartRow = pageCurrentNo * iPageRows;
                index = pageStartRow;
                row = (HashMap) rows.get(index);

                if (showPageRows != null) {
                    pageFlag = true;
                    //�����б����
                    if (pageData == null) {
                        pageData = new HashMap();
                    }
                    pageData.put(name, valueObj);
                    session.setAttribute("PageData$Of$Neturbo", pageData);

                    //�����ҳ��
                    pageTotalNo = rows.size() / iPageRows + 1;
                    if (rows.size() % iPageRows == 0) {
                        pageTotalNo--;
                    }
                    //���ÿҳ����
                    if (pageEndRow > pageStartRow + iPageRows) {
                        pageEndRow = pageStartRow + iPageRows;
                    }
                }
            }

            if (Utils.null2Empty(showNoRecord).toUpperCase().equals("YES")) {
                noRecordFlag = true;
            }
            return EVAL_BODY_TAG;
        } catch (Exception e) {
            Log.warn("Custom Tag Process doStartTag error (List)", e);
        }
        return SKIP_BODY;
    }

    public int doAfterBody() throws JspException {
        BodyContent body = getBodyContent();
        JspWriter out = body.getEnclosingWriter();
        String name = this.getName();
        if ((index < pageEndRow)) {
            try {
                HttpSession session = pageContext.getSession();
                HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
              //mod by linrui 20190130 for cr230
                Locale locale = (Locale) session.getAttribute("Locale$Of$Neturbo");
                String language = locale.toString();
                out.print(body.getString());
                body.clearBody();
                index++;
                if (index < pageEndRow) {
                    row = (HashMap) rows.get(index);
                }else if("transHistoryView".equals(name.trim())){//mod by linrui 20180125
                	
                } else {
                    if (pageFlag) {
                        String templateJsp = "";
                        pageStyle = Utils.null2Empty(pageStyle).toUpperCase();
                        if("POST".equals(pageStyle)){
                            if (pageCurrentNo == 0) {
                                if (pageCurrentNo == pageTotalNo - 1) {
                                	//templateJsp = "Page_First_PageBar_0.jsp" ;
                                	//mod by linrui for cr230 20190130
                                    templateJsp = (language.equals(app.cib.util.Constants.CN) || language.equals(app.cib.util.Constants.HK))?
                                        "Page_First_PageBar_0_ZH.jsp":"Page_First_PageBar_0.jsp";
                                } else {
                                    //templateJsp = "Page_First_PostBar.jsp";
                                	//mod by linrui for cr230 20190130
                                	templateJsp = (language.equals(app.cib.util.Constants.CN) || language.equals(app.cib.util.Constants.HK))?
                                		"Page_First_PostBar_ZH.jsp":"Page_First_PostBar.jsp";
                                }
                            } else if (pageCurrentNo == pageTotalNo - 1) {
                                //templateJsp = "Page_Last_PostBar.jsp";
                            	//mod by linrui for cr230 20190130
                            	templateJsp = (language.equals(app.cib.util.Constants.CN) || language.equals(app.cib.util.Constants.HK))?
                            		"Page_Last_PostBar_ZH.jsp":"Page_Last_PostBar.jsp";
                            } else {
                                //templateJsp = "Page_PostBar.jsp";
                            	//mod by linrui for cr230 20190130
                            	templateJsp = (language.equals(app.cib.util.Constants.CN) || language.equals(app.cib.util.Constants.HK))?
                            		"Page_PostBar_ZH.jsp":"Page_PostBar.jsp";
                            }
                        }else{
                            if (pageCurrentNo == 0) {
                                if (pageCurrentNo == pageTotalNo - 1) {
                                    //templateJsp = "Page_First_PageBar_0.jsp";
                                	//mod by linrui for cr230 20190130
                                	templateJsp = (language.equals(app.cib.util.Constants.CN) || language.equals(app.cib.util.Constants.HK))?
                                			"Page_First_PageBar_0_ZH.jsp":"Page_First_PageBar_0.jsp";	
                                } else {
                                    //templateJsp = "Page_First_PageBar.jsp";
                                	//mod by linrui for cr230 20190130
                                	templateJsp = (language.equals(app.cib.util.Constants.CN) || language.equals(app.cib.util.Constants.HK))?
                                			"Page_First_PageBar_ZH.jsp":"Page_First_PageBar.jsp";
                                }
                            } else if (pageCurrentNo == pageTotalNo - 1) {
                                //templateJsp = "Page_Last_PageBar.jsp";
                            	//mod by linrui for cr230 20190130
                            	templateJsp = (language.equals(app.cib.util.Constants.CN) || language.equals(app.cib.util.Constants.HK))?
                            			"Page_Last_PageBar_ZH.jsp":"Page_Last_PageBar.jsp";
                            } else {
                                //templateJsp = "Page_PageBar.jsp";
                            	//mod by linrui for cr230 20190130
                            	templateJsp = (language.equals(app.cib.util.Constants.CN) || language.equals(app.cib.util.Constants.HK))?
                            			"Page_PageBar_ZH.jsp":"Page_PageBar.jsp";
                            }
                        }

                        /*String pageBarStr =
                                TemplateFactory
                                .getInstance(templateJsp)
                                .getContent();
                        pageBarStr =
                                Utils.replaceStr(
                                        pageBarStr,
                                        "<%CURRENT_PAGE_NO%>",
                                        String.valueOf(pageCurrentNo));
                        pageBarStr =
                                Utils.replaceStr(
                                        pageBarStr,
                                        "<%CURRENT_PAGE_NO_SHOW%>",
                                        String.valueOf(pageCurrentNo + 1));
                        pageBarStr =
                                Utils.replaceStr(
                                        pageBarStr,
                                        "<%TOTAL_PAGE_NO_SHOW%>",
                                        String.valueOf(pageTotalNo));
                        pageBarStr =
                                Utils.replaceStr(
                                        pageBarStr,
                                        "<%TOTAL_RECORD_NO_SHOW%>",
                                        String.valueOf(recordTotalNo));

                        pageBarStr =
                                Utils.replaceStr(
                                        pageBarStr,
                                        "<%PAGE_CLASS%>",
                                        pageClass);*/
                        String pageBarStr =  TemplateFactory.getInstance(templateJsp).getContent();
                        
                        pageBarStr = Utils.replaceStr(pageBarStr, "<%CURRENT_PAGE_NO%>", String.valueOf(pageCurrentNo));
                        pageBarStr = Utils.replaceStr(pageBarStr, "<%CURRENT_PAGE_NO_SHOW%>", String.valueOf(pageCurrentNo + 1));
                        pageBarStr = Utils.replaceStr(pageBarStr, "<%TOTAL_PAGE_NO_SHOW%>", String.valueOf(pageTotalNo));
                        pageBarStr = Utils.replaceStr(pageBarStr, "<%TOTAL_RECORD_NO_SHOW%>", String.valueOf(recordTotalNo));
                        
                        pageBarStr = Utils.replaceStr(pageBarStr, "<%PAGE_CLASS%>",  pageClass);
                        //mod by linrui for cr230 20190130
                        if(language.equals(app.cib.util.Constants.CN) || language.equals(app.cib.util.Constants.HK)){
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%total%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("total"));
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%records%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("records"));
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%previous_page%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("previous_page"));
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%next_page%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("next_page"));
                            //第  + 页
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%the%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("the"));
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%pageOf%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("pageOf"));
                            //共 + 页
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%all%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("all"));
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%pageOfEnd%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("pageOfEnd"));
                            //first page
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%firstPage%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("firstPage"));
                            //last page
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%lastPage%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("lastPage"));
                        
                        }else{
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%total%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("total"));
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%records%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("records"));
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%previous_page%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("previous_page"));
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%next_page%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("next_page"));
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%page%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("page"));
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%of%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("of"));
                            //first page
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%firstPage%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("firstPage"));
                            //last page
                            pageBarStr = Utils.replaceStr(pageBarStr, "<%lastPage%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("lastPage"));
                        }
                        if (pageJsp == null) {
                            //pageJsp = (String) request.getRequestURI();
                            pageJsp = request.getServletPath();

                        }
                        pageBarStr =
                                Utils.replaceStr(
                                        pageBarStr,
                                        "<%PAGE_JSP%>",
                                        EscapeChar.forURL(pageJsp));
                        out.print(pageBarStr);
                    }
                    //�Ѿ��������һ��
                    return SKIP_BODY;
                }
            } catch (IOException ioe) {
                Log.warn("Custom Tag Process doAfterBody error (List)", ioe);
            }
            return EVAL_BODY_TAG;
        } else if (noRecordFlag && pageEndRow == 0) {
            try {
            	//add by liqun 20171124 for muti-language
            	HttpSession session = pageContext.getSession();
                Locale locale = (Locale) session.getAttribute("Locale$Of$Neturbo");//end
                
                if (showPageRows == null) {
                    String templateJsp = "Page_NoRecord.jsp";
                    String pageBarStr = TemplateFactory.getInstance(templateJsp).getContent();
                  
                    //add by liqun 20171124 for muti-language
                    pageBarStr = Utils.replaceStr(pageBarStr, "<%no_record%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("no_record"));
                    
                    out.print(pageBarStr);
                } else {
                    String templateJsp = "Page_NoRecord_0.jsp";
                    String pageBarStr = TemplateFactory.getInstance(templateJsp).getContent();
                  
                    //add by liqun 20171124 for muti-language
                    pageBarStr = Utils.replaceStr(pageBarStr, "<%no_record%>", RBFactory.getInstance("app.cib.resource.common.operation",locale+"").getString("no_record"));
                    
                    out.print(pageBarStr);
                }
            } catch (IOException ioe) {
                Log.warn("Custom Tag Process doAfterBody error (List)", ioe);
            }
            return SKIP_BODY;
        } else {
            return SKIP_BODY;
        }

    }

    public String getInlevel() {
        return inlevel;
    }

    public void setInlevel(String inlevel) {
        this.inlevel = inlevel;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getIndex() {
        return index;
    }

    public String getPageStyle() {

        return pageStyle;
    }

    public void setPageStyle(String pageStyle) {

        this.pageStyle = pageStyle;
    }

    public String getPageJsp() {

        return pageJsp;
    }

    public String getShowPageRows() {

        return showPageRows;
    }

    public String getShowNoRecord() {

        return showNoRecord;
    }

    public String getPageClass() {

        return pageClass;
    }

    public void setPageJsp(String pageJsp) {

        this.pageJsp = pageJsp;
    }

    public void setShowPageRows(String showPageRows) {

        this.showPageRows = showPageRows;
    }

    public void setShowNoRecord(String showNoRecord) {

        this.showNoRecord = showNoRecord;
    }

    public void setPageClass(String pageClass) {

        this.pageClass = pageClass;
    }

    private void jbInit() throws Exception {
    }
}
