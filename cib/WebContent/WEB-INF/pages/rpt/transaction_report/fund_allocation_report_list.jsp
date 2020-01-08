<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.corp_history">
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
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit(arg) {
	if (arg == 'return') {
		document.form1.ActionMethod.value = 'listFundAllocationReportLoad';
	}
	document.form1.submit();
	setFormDisabled("form1");
	//setFieldEnabled("buttonReturn");
}
var listNameToBeChange ="";
function showTree(listName) {
	listNameToBeChange = listName;
	var url="/cib/corporation.do?ActionMethod=listCorpForTree";
	var popWin1= window.open(url,"temp1","toolbar=no,location=no,top=50,left=50,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=530,height=400");
	popWin1.focus();
}
function changeCorp(arg) {
	putFieldValues(listNameToBeChange, arg);
	if(listNameToBeChange=='fromCorporation'){
		showList($(listNameToBeChange),$('fromAccount'));
	}else{
		showList($(listNameToBeChange),$('toAccount'));
	}
}
function changeToAcc(toAcc){
	if ((toAcc != null) && (toAcc != '')) {
		var url = '/cib/jsax?serviceName=AccInTxnService&showToAcc=' + toAcc + '&language=' + language;
   		//registerElement('showToCurrency');
		getMsgToElement(url, toAcc, '', null,false,true,language);
	}
}
function showList(originSelect, targetSelect, callback){
	if ((originSelect.value != null) && (originSelect.value != '')) {
		var params = getParams(originSelect, targetSelect);
		var url = '/cib/jsax?serviceName=CorpAccListService&'+ params;
		getMsgToSelect(url,'', callback,true,language);
  	} else {
		targetSelect.options.length = 0;
		var optElement = document.createElement('option');
		optElement.setAttribute('value', '');
		optElement.innerHTML = '----- Select an Account ------';
		targetSelect.appendChild(optElement);
	}
}
function getParams(originSelect, targetSelect) {
	var targetType = 'targetType=object';
	var targetId = 'targetId=' + originSelect.id;
	var originValue = 'corpId=' + originSelect.value;
	var subListId = 'subListId=' + targetSelect.id;
	var params = '';
	params = params + targetType + '&' + targetId + '&' + originValue + '&' + subListId;
	return params;
}
function changeFromAcc(fromAcc){
	if ((fromAcc != null) && (fromAcc != '')) {
		var url = '/cib/jsax\?serviceName=AccInTxnService&showFromAcc=' + fromAcc + '&language=' + language;
   		//registerElement('showAmount');
   		//registerElement('showFromCurrency');
		getMsgToElement(url, fromAcc, '', null,true,language);
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
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/txnReport.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.corp_history" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_fund" rb="app.cib.resource.rpt.txn_report"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_fund" rb="app.cib.resource.rpt.txn_report"/><!-- InstanceEndEditable --></td>
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
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/>
				   <set:fieldcheck name="corpHistory" form="form1" file="corp_transfer" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr align="center">
                  <td height="40" align="right"><table width="100" border="0" cellspacing="0" cellpadding="0"><%--
                    <tr>
                      <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=reportList&fileName=corpTransfer_history&columnTitles=<set:label name='BUSINESS_TYPE'/>,<set:label name='REQUEST_DATE'/>,<set:label name='PAYMENT_CORP'/>,<set:label name='PAYMENT_ACCOUNT'/>,<set:label name='PAYEE_CORP'/>,<set:label name='PAYEE_ACCOUNT'/>,<set:label name='CURRENCY_TYPE'/>,<set:label name='AMOUNT'/>,<set:label name='STATE'/>&columnNames=recordType||rb@app.cib.resource.txn.corp_type,requestTime||format@datetime,fromCorporation||db@corpName,fromAccount,toCorporation||db@corpName,toAccount,currency||db@currency,transferAmount||format@amount,status||rb@app.cib.resource.common.status"><set:label name="download" rb="app.cib.resource.common.operation"/></a> </td>
                        </tr>
                  --%></table></td>
                  </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr class="greyline">
                  <td  class="listheader1"><div align="left"><set:label name="REQUEST_DATE" defaultvalue="REQUEST DATE"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="BUSINESS_TYPE" defaultvalue="BUSINESS TYPE"/></div></td>
                  <td  class="listheader1"><div align="left"><set:label name="PAYMENT_CORP" defaultvalue="PAYMENT CORP"/></div></td>
                  <td  class="listheader1"><div align="left"><set:label name="PAYMENT_ACCOUNT" defaultvalue="PAYMENT ACCOUNT"/></div></td>
                  <td  class="listheader1"><div align="left"><set:label name="PAYEE_CORP" defaultvalue="PAYEE CORP"/></div></td>
                  <td  class="listheader1"><div align="left"><set:label name="PAYEE_ACCOUNT" defaultvalue="PAYEE ACCOUNT"/></div></td>
                  <td  class="listheader1"><div align="center"><set:label name="CURRENCY_TYPE" defaultvalue="CURRENCY TYPE"/></div></td>
                  <td  class="listheader1"><div align="right"><set:label name="AMOUNT" defaultvalue="AMOUNT"/></div></td>
                  <td  class="listheader1"><div align="left"><set:label name="STATE" defaultvalue="STATE"/></div></td>
                <set:list name="reportList"  showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1"><div align="left"><set:listdata name="requestTime" format="date"/></div></td>
                  <td class="listcontent1"><div align="left"><set:listdata name="recordType" rb="app.cib.resource.txn.corp_type"/></div></td>
                  <td class="listcontent1"><div align="left"><set:listdata name="fromCorporation" db="corpName"/></div></td>
                  <td class="listcontent1"><div align="left"><set:listdata name="fromAccount"/></div></td>
                  <td class="listcontent1"><div align="left"><set:listdata name="toCorporation" db="corpName"/></div></td>
                  <td class="listcontent1"><div align="left"><set:listdata name="toAccount"/></div></td>
                  <td class="listcontent1"><div align="center"><set:listif name="inputAmountFlag" condition="EQUALS" value="1"><set:listdata name="toCurrency" /></set:listif><set:listif name="inputAmountFlag" condition="EQUALS" value="0"><set:listdata name="fromCurrency" /></set:listif></div></td>
                  <td class="listcontent1"><div align="right"><set:listif name="inputAmountFlag" condition="EQUALS" value="1"><set:listdata name="transferAmount" format="amount"/></set:listif><set:listif name="inputAmountFlag" condition="EQUALS" value="0"><set:listdata name="debitAmount" format="amount"/></set:listif></div></td>
                  <td class="listcontent1"><div align="left"><set:listdata name="status" rb="app.cib.resource.common.status"/></div></td>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input id="return" name="return" type="button" value="&nbsp;&nbsp;<set:label name='return' rb='app.cib.resource.rpt.txn_report'/>&nbsp;&nbsp;" onClick="doSubmit('return')">
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
