<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.pdf_download">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
<title>Corporation Banking</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language="JavaScript" src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	var form1 = document.form1;
	var date_range = '<set:data name="date_range"/>';
	if (date_range !== 'range'){
		form1.date_range[0].checked = true;
		form1.date_range[0].onclick();
	} else 
		form1.date_range[1].checked = true;
		form1.date_range[1].onclick();
	}
}
function doSubmit(arg) {
	if(validate_enquiry(document.getElementById("form1"))){
		//document.getElementById("submit1").disabled=true;
		if (arg == 'list') {
			document.form1.ActionMethod.value = 'listFile';
		}
		document.getElementById("form1").submit();
		setFormDisabled("form1");
		//setFieldEnabled("add");
	}
}
function changeRange(range){
	if(range == 'all'){
		document.getElementById("dateFrom").value='';
		document.getElementById("dateTo").value='';
		document.getElementById("dateFrom").disabled = true;
		document.getElementById("dateTo").disabled = true;
		document.getElementById("dateRange").value=0;
		document.getElementById("dateRange").disabled = true;
	}else{
		document.getElementById("dateFrom").disabled = false;
		document.getElementById("dateTo").disabled = false;
		document.getElementById("dateRange").disabled = false;
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/pdfDownload.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.pdf_download" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="MDB Corporate Online Banking > Pay Bills >  Payment History"/><!-- InstanceEndEditable --></td>
    <td class="buttonprint" style="background-image:url(images/button-print_<%=session.getAttribute("Locale$Of$Neturbo")%>.gif)"><a href="#" onClick="printPage('pageheader');"><img src="/cib/images/shim.gif" width="61" height="18" border="0"></a></td>
	<!--
    <td class="buttonhelp"><a href="#"><img src="/cib/images/shim.gif" width="36" height="18" border="0"></a></td>
	-->
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="EDC64A">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="white">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="40">
  <tr>
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="PAYMENT HISTORY"/><!-- InstanceEndEditable --></td>
    <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="1" bgcolor="white"><img src="/cib/images/shim.gif" width="1" height="1"></td>
        </tr>
      </table></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="19" class="topborderlong"><img src="/cib/images/table_top_long.gif" width="100%" height="19"></td>
  </tr>
  <tr>
    <td align="center" bgcolor="#999999"><table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
        <tr>
          <td width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
          <td>
		  <form action="/cib/pdfDownload.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="enquiry" form="form1" file="pdf_download" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupinput"><!--<set:label name="search_timedeposit_history" defaultvalue="search_deposit_history"/>-->&nbsp;</td>
                </tr>
              </table>
              <table border=0 bgcolor=#808080 cellpadding=0 cellspacing=1 width=100%>
                <tr>
                  <td bgcolor=#FFFFFF class=content><table border=0 cellpadding=5 cellspacing=0 width=100% bgcolor=#FFFFFF>
                      <tr>
                        <td><set:label name="pdf_type" defaultvalue="pdf_type"/></td>
                        <td><select name="category" id="category" nullable="0">
                            <option value="0" selected>--<set:label name="sel_category"/>--</option>
                            <set:rblist db="pdfCategory"> <set:rboption name="category"/> </set:rblist>
                          </select>
                        </td>
                      </tr>
                      <tr>
                        <td class=content valign=top width=140><set:label name="period" defaultvalue="period"/></td>
                        <td class=content colspan="2"><input id="date_range" name="date_range" type="radio" value="all" checked onClick="changeRange('all')">
&nbsp; <set:label name="all" defaultvalue="all" rb="app.cib.resource.txn.bill_payment"/> <br>
                          <input id="date_range" name="date_range" type="radio" value="range" onClick="changeRange('range')">
&nbsp; <set:label name='from' defaultvalue='from'/>&nbsp;
                          <input name="dateFrom" type="text" id="dateFrom" size="12" maxlength="10" value="<set:data name='dateFrom'/>" disabled>
                          <span class="content1"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language);}" ></span> &nbsp;&nbsp;&nbsp; <set:label name='to' defaultvalue='to'/>&nbsp;
                          <input name="dateTo" type="text" id="dateTo" size="12" maxlength="10" value="<set:data name='dateTo'/>" disabled>
                          <span class="content1"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language);}" ></span> <select id="dateRange" name="dateRange" disabled="disabled" onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'))">
					<option value='0'><set:label name="Select_Date_Short_Cut" defaultvalue="----- Select a Date Short-cut ------"/></option>
					<set:rblist file="app.cib.resource.common.date_selection">
                    	<set:rboption/>
					</set:rblist>
					</select>		</td>
                      </tr>
                    </table></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="53%" height="40" class="sectionbutton"><div align="right">
                      <input id="search" type="button" value="&nbsp;<set:label name='enquiry' defaultvalue='enquiry'/>&nbsp;" name="search" onClick="doSubmit('list')">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="list">
                    </div></td>
                  <td width="47%" height="40" class="sectionbutton">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1" align="left"><set:label name="report_id" defaultvalue="report_id"/></td>
                  <td class="listheader1" align="center"><set:label name="generation_date" defaultvalue="generation_date"/></td>
                  <td class="listheader1" align="left"><set:label name="pdf_file_name" defaultvalue="pdf_file_name"/></td>
                </tr>
                <set:list name="fileList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1" align="left"><set:listdata name="REPORT_ID"/></td>
                  <td class="listcontent1" align="center"><set:listdata name="GENERATION_DATE" format="date"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="pdfFileLink"/></td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" class="sectionbutton">&nbsp;</td>
                </tr>
              </table>
              <!-- InstanceEndEditable -->
		  </form>
		  </td>
          <td width="1%"><img src="/cib/images/shim.gif" width="10" height="1"></td>
      </table></td>
    <td align="right" width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="999999">
    <td><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="white">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="2"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
  </tr>
</table>
</body>
</set:loadrb>
<!-- InstanceEnd --></html>
