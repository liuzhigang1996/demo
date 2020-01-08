<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.bill_payment">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
<title>Corporation Banking</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/common1.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language="JavaScript" src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	if('<set:data name="date_range"/>' != 'range') {
		document.form1.date_range[0].checked = true;
		document.form1.date_range[0].onclick();
	} else {
		document.form1.date_range[1].checked = true;
		document.form1.date_range[1].onclick();
	}
}
function doSubmit(arg) {
	if (arg == 'search') {
		if(validate_payment_history(document.getElementById("form1"))){
			document.form1.ActionMethod.value = 'listHistory';
			document.form1.submit();
			setFormDisabled("form1");
		}
	} else if (arg == 'view') {
		document.form1.ActionMethod.value = 'viewHistoryDetail';
		document.form1.submit();
		setFormDisabled("form1");
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
	} else {
		document.getElementById("dateFrom").disabled = false;
		document.getElementById("dateTo").disabled = false;
		document.getElementById("dateRange").disabled = false;
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/templatePayment.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.bill_payment" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_bat_history" defaultvalue="MDB Corporate Online Banking > Pay Bills >  Payment History"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_bat_history" defaultvalue="PAYMENT HISTORY"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/templatePayment.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="payment_history" form="form1" file="bill_payment" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator"><!--<set:label name="search_payment_history" defaultvalue="search_payment_history"/>-->
&nbsp;</td>
                </tr>
              </table>
              <table border=0 bgcolor=#808080 cellpadding=0 cellspacing=1 width=100%>
                <tr>
                  <td bgcolor=#FFFFFF class=content><table border=0 cellpadding=5 cellspacing=0 width=100% bgcolor=#FFFFFF>
                      <tr >
                        <td class=content valign=top width=140><set:label name="payment_period" defaultvalue="payment_period"/></td>
                        <td class=content colspan="4"><input id="date_range" name="date_range" type="radio" value="all" checked onClick="changeRange('all')">
&nbsp; <set:label name="all" defaultvalue="all"/> <br>
                          <input id="date_range" name="date_range" type="radio" value="range" onClick="changeRange('range')">
&nbsp; <set:label name='date_from' defaultvalue='date_from'/>&nbsp;
                          <input name="dateFrom" type="text" id="dateFrom" size="12" maxlength="10" disabled value="<set:data name='dateFrom'/>">
                          <span class="content1"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" ></span>&nbsp;&nbsp;&nbsp; <set:label name='date_to' defaultvalue='date_to'/>&nbsp;
                          <input name="dateTo" type="text" id="dateTo" size="12" maxlength="10" disabled value="<set:data name='dateTo'/>">
                          <span class="content1"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" ></span>
                          <select id="dateRange" name="dateRange" disabled="disabled" onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'));">
                            <option value='0'><set:label name="Select_Date_Short_Cut" defaultvalue="----- Select a Date Short-cut ------"/></option>
                            <set:rblist file="app.cib.resource.common.date_selection"> <set:rboption/> </set:rblist>
                          </select></td>
                      </tr>
                      <tr class="greyline">
                        <td class=content valign=top><set:label name="debit_account" defaultvalue="debit_account"/></td>
                        <td class=content colspan="2"><select name="fromAccount" id="fromAccount" nullable="0" >
                            <option value="0" selected><set:label name="all"/></option>
                            <set:rblist db="caoasaAccountByUser"> <set:rboption name="fromAccount"/> </set:rblist>
                          </select>
                        </td>
                      </tr>
                    </table></td>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="55%" height="40" class="sectionbutton"><div align="right"><span class="content">
                      <input id="search" type="button" value="&nbsp;<set:label name='search' defaultvalue='search'/>&nbsp;" name="search" onClick="doSubmit('search')">
                      </span>
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="listBatchPaymentHistory">
                      <input name="batchId" id="batchId" type="hidden" value="">
                    </div></td>
                  <td height="40" valign="middle" class="content1" align="right"><table width="100" border="0" cellspacing="0" cellpadding="0">
                      <%--<tr>
                        <td class="buttonexcel"><a onClick="toTargetForm1('/cib/DownloadCVS?listName=historyList&fileName=bat_payment_history&columnTitles=<set:label name='date_paid'/>,<set:label name='debit_account'/>,<set:label name='debit_ccy'/>,<set:label name='debit_amount'/>,<set:label name='status'/>&columnNames=requestTime||format@datetime,fromAccount,fromCurrency,fromAmount||format@amount,status||rb@app.cib.resource.common.status"><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>                      
                      </tr>
                    --%></table></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1"><div align="left"><set:label name="date_paid" defaultvalue="date_paid"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="debit_account" defaultvalue="debit_account"/></div></td>
                  <td class="listheader1"><div align="center"><set:label name="debit_ccy" defaultvalue="debit_ccy"/></div></td>
                  <td class="listheader1"><div align="right"><set:label name="debit_amount" defaultvalue="debit_amount"/></div></td>
                  <td class="listheader1"><div align="center"><set:label name="change_approver" defaultvalue="change_approver"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="status" defaultvalue="status"/></div></td>
                  <td class="listheader1"><div align="left"><set:label name="upload_status" rb="app.cib.resource.bat.payroll"/></div></td>
                  <td class="listheader1"><div align="center"><set:label name="view_detail" defaultvalue="view_detail"/></div></td>
                </tr>
                <set:list name="historyList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1" valign="middle" align="left"><set:listdata name="requestTime" format='datetime'/></td>
                  <td class="listcontent1" valign="middle"><div align="left"><set:listdata name="fromAccount"/></div></td>
                  <td class="listcontent1" valign="middle"><div align="center"><set:listdata name="fromCurrency" db="rcCurrencyCBS"/></div></td>
                  <td class="listcontent1" valign="middle"><div align="right"><set:listdata name="fromAmount" format="amount"/></div></td>
                  <td class="listcontent1" valign="middle"><div align="center"> <set:listif name="changeFlag" condition="equals" value="Y"> <a onClick="postToMainFrame('/cib/approve.do?ActionMethod=changeApproverLoad',{txnTypeToChange:'<set:listdata name='txnType'/>',transNoToChange:'<set:listdata name='batchId'/>'})" href="#"> <set:label name="change" defaultvalue="change"/> </a> </set:listif> <set:listif name="changeFlag" condition="notequals" value="Y">-</set:listif> </div></td>
                  <td class="listcontent1" valign="middle"><div align="left"><set:listdata name="status" rb="app.cib.resource.common.status"/></div></td>
                  <td class="listcontent1" align="left"><set:listdata name="batchResult" rb="app.cib.resource.txn.batch_payment_result"/></td>
                  <td class="listcontent1" valign="middle"><div align="center"><a href="#" onClick="getElementById('batchId').value='<set:listdata name='batchId'/>';doSubmit('view');"><set:label name="view_detail" defaultvalue="view_detail"/></a></div></td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton">&nbsp;</td>
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
