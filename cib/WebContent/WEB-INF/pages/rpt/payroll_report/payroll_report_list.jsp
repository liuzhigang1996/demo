<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.rpt.payroll_report">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
<title>Corporation Banking</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/common1.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	parseInt
}
function doSubmit(arg) {
	if (arg == 'return') {
		document.form1.ActionMethod.value = 'listReportLoad';
	}
	document.form1.submit();
	setFormDisabled("form1");
	//setFieldEnabled("buttonReturn");
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/payrollReport.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.rpt.payroll_report" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_payroll" defaultvalue="MDB Corporate Online Banking > Online Report >  Bill Payment Report"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_payroll" defaultvalue="BILL PAYMENT REPORT"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/payrollReport.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
               <tr>
			   <td height="40" class="content1"  colspan="7" align="right"><table width="100" border="0" cellspacing="0" cellpadding="0">
                  <%--<tr>
                    <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=historyList&columnTitles=<set:label name='upload_date' rb='app.cib.resource.bat.payroll'/>,<set:label name='total_amt' rb='app.cib.resource.bat.payroll'/>,<set:label name='no_of_record' rb='app.cib.resource.bat.payroll'/>,<set:label name='status' rb='app.cib.resource.bat.payroll'/>,<set:label name='upload_status' rb='app.cib.resource.bat.payroll'/>&columnNames=requestTime||format@datetime,totalAmount||format@amount,totalNumber,status||rb@app.cib.resource.common.status,batchResult||rb@app.cib.resource.bat.payroll_result"><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>
                  </tr>
                --%></table></td>
			   </tr>
                <tr>
                  <td class="listheader1"><set:label name="upload_date" rb="app.cib.resource.bat.payroll"/></td>
                  <td class="listheader1" align="right"><set:label name="total_amt" rb="app.cib.resource.bat.payroll"/></td>
                  <td class="listheader1"><set:label name="no_of_record" rb="app.cib.resource.bat.payroll"/></td>
                  <td class="listheader1"><set:label name="status" rb="app.cib.resource.bat.payroll"/></td>
                  <td class="listheader1"><set:label name="upload_status" rb="app.cib.resource.bat.payroll"/></td>
                  <td class="listheader1" align="center"><set:label name="view_detail" rb="app.cib.resource.bat.payroll"/></td>
                </tr>
                <set:list name="historyList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1" align="left"><set:listdata name="requestTime" format="datetime"/></td>
                  <td class="listcontent1" align="right"><set:listdata name="totalAmount" format="amount"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="totalNumber"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="status" rb="app.cib.resource.common.status"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="batchResult" rb="app.cib.resource.bat.payroll_result"/></td>
                  <td class="listcontent1" align="center"><a onClick="postToMainFrame('/cib/payrollReport.do?ActionMethod=viewReportDetail',{payrollId:'<set:listdata name='payrollId'/>',date_range:'<set:data name='date_range'/>',dateFrom:'<set:data name='dateFrom'/>',dateTo:'<set:data name='dateTo'/>'})" href="#"><set:label name="view_detail"defaultvalue="view_detail" rb="app.cib.resource.bat.payroll"/></a></td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input id="return" name="return" type="button" value="&nbsp;&nbsp;<set:label name='return' defaultvalue=' return ' rb='app.cib.resource.bat.payroll'/>&nbsp;&nbsp;" onClick="doSubmit('return')">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="listTimeDepositReportLoad">
                  </td>
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
