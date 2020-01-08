<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.rpt.txn_report">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
<title>Corporation Banking</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit(arg) {
	if (arg == 'return') {
		document.form1.ActionMethod.value = 'listTimeDepositReportLoad';
	}
	document.form1.submit();
	setFormDisabled("form1");
	//setFieldEnabled("buttonReturn");
}
function viewDetail(tdType, transId){
	if (tdType == '1') {
		window.location = 'txnReport.do?ActionMethod=viewNewHistory&transId=' + transId;
	} else if (tdType == '2') {
		window.location = 'txnReport.do?ActionMethod=viewWithdrawalHistory&transId=' + transId;
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/txnReport.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.rpt.txn_report" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_td" defaultvalue="MDB Corporate Online Banking > Online Report >  Bill Payment Report"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_td" defaultvalue="BILL PAYMENT REPORT"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/txnReport.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
               <tr>
			   <td height="40" class="content1"  colspan="6"align="right"><table width="100" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <%--<td class="buttonexcel"><a href="/cib/DownloadCVS?listName=historyList&columnTitles=<set:label name='date' rb='app.cib.resource.txn.time_deposit'/>,<set:label name='trans_type' rb='app.cib.resource.txn.time_deposit'/>,<set:label name='currency' rb='app.cib.resource.txn.time_deposit'/>,<set:label name='amount' rb='app.cib.resource.txn.time_deposit'/>,<set:label name='status' rb='app.cib.resource.txn.time_deposit'/>&columnNames=requestTime||format@datetime,tdType||rb@app.cib.resource.txn.td_type,currency,principal||format@amount,status||rb@app.cib.resource.common.status"><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>--%>
                  </tr>
                </table></td>
			   </tr>
                <tr>
                  <td class="listheader1"><div align="left"><set:label name="date" defaultvalue="date" rb="app.cib.resource.txn.time_deposit"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="trans_type" defaultvalue="trans_type" rb="app.cib.resource.txn.time_deposit"/></div></td>
                  <td class="listheader1"><div align="center"><set:label name="currency" defaultvalue="currency" rb="app.cib.resource.txn.time_deposit"/></div></td>
                  <td class="listheader1"><div align="right"><set:label name="amount" defaultvalue="amount" rb="app.cib.resource.txn.time_deposit"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="status" defaultvalue="status" rb="app.cib.resource.txn.time_deposit"/></div></td>
<!--
                  <td class="listheader1"><div align="center"><set:label name="view_detail" defaultvalue="view_detail" rb="app.cib.resource.txn.time_deposit"/></div></td>
-->
                </tr>
                <set:list name="historyList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1"><div align="left"><set:listdata name="requestTime" format="datetime"/></div></td>
                  <td class="listcontent1"><div align="left"><set:listdata name="tdType" rb="app.cib.resource.txn.td_type"/></div></td>
                  <td class="listcontent1"><div align="center"><set:listdata name="currency"/></div></td>
                  <td class="listcontent1"><div align="right"><set:listdata name="principal" format="amount"/></div></td>
                  <td class="listcontent1"><div align="left"><set:listdata name="status" rb="app.cib.resource.common.status"/></div></td>
<!--
                  <td class="listcontent1"><div align="center"><a href="#" onClick="viewDetail('<set:listdata name="tdType"/>', '<set:listdata name="transId"/>');"><set:label name="view_detail" defaultvalue="view_detail" rb="app.cib.resource.txn.time_deposit"/></a></div></td>
-->
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input id="return" name="return" type="button" value="&nbsp;&nbsp;<set:label name='return' defaultvalue=' return '/>&nbsp;&nbsp;" onClick="doSubmit('return')">
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
