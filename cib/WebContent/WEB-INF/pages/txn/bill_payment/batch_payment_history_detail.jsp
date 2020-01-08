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
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit(arg) {
	if (arg == 'return') {
		document.form1.ActionMethod.value = 'listHistory';
		document.form1.submit();
		setFormDisabled("form1");
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/templatePayment.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.bill_payment" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_bat_history" defaultvalue="MDB Corporate Online Banking > Pay Bills >  FREQUENT PAYMENT LIST"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_bat_history" defaultvalue="FREQUENT PAYMENT LIST"/><!-- InstanceEndEditable --></td>
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
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="template" form="form1" file="bill_payment" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td colspan="2" class="groupinput"><set:label name="detail_info" defaultvalue="detail_info"/></td>
                </tr>
                <tr>
                  <td width="17%" class="label1"><set:label name="debit_account" defaultvalue="debit_account"/></td>
                  <td class="content1"><set:data name="fromAccount2" db="caoasaAccountByUser"/></td>
                </tr>
                <tr class="greyline">
                  <td width="17%" class="label1"><set:label name="debit_ccy" defaultvalue="debit_ccy"/></td>
                  <td class="content1"><set:data name="fromCurrency" db="rcCurrencyCBS"/></td>
                </tr>
                <tr>
                  <td class="label1"><set:label name="total_transfer_amount" defaultvalue="total_transfer_amount"/></td>
                  <td class="content1">MOP - <set:data name="transferAmount" format="amount"/></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1" align="left"><set:label name="merchant" defaultvalue="merchant"/></td>
                  <td class="listheader1" align="left"><set:label name="billNoAndCardNo" defaultvalue="billNoAndCardNo"/></td>
                  <td class="listheader1" align="center"><set:label name="ccy" defaultvalue="ccy"/></td>
                  <td class="listheader1" align="right"><set:label name="payment_amount" defaultvalue="payment_amount"/></td>
                  <td class="listheader1" align="left"><set:label name="status" defaultvalue="status"/></td>
                  <td class="listheader1" align="left"><set:label name="upload_status" rb="app.cib.resource.bat.payroll"/></td>
                </tr>
                <set:list name="detailList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1" align="left" valign="middle"><set:listdata name="merchant" db="merchantAll"/>&nbsp;</td>
                  <td class="listcontent1" align="left" valign="middle"><set:listdata name="billNo1"/>&nbsp; <set:listif name="billName" value="" condition="notequals"><br>
                    <set:listdata name="billName"/>&nbsp;</set:listif> <set:listif name="remark" value="" condition="notequals"><br>
                    <set:listdata name="remark"/>&nbsp;</set:listif> </td>
                  <td class="listcontent1" align="center" valign="middle"><set:listdata name="currency" db="rcCurrencyCBS"/></td>
                  <td class="listcontent1" align="right" valign="middle"><set:listdata name='transferAmount' format='amount'/></td>
                  <td class="listcontent1" valign="middle"><div align="left"><set:listdata name="status" rb="app.cib.resource.common.status"/></div></td>
                  <td class="listcontent1" align="left" valign="middle"><set:listdata name='detailResult' rb='app.cib.resource.txn.batch_payment_result'/></td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input name="return" id="return" type="button" value="&nbsp;&nbsp;<set:label name='return' defaultvalue=' return '/>&nbsp;&nbsp;" onClick="doSubmit('return')">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="add">
                    <input id="date_range" name="date_range" type="hidden" value="<set:data name='date_range'/>">
                    <input id="dateFrom" name="dateFrom" type="hidden" value="<set:data name='dateFrom'/>">
                  <input id="dateTo" name="dateTo" type="hidden" value="<set:data name='dateTo'/>">
                  <input id="fromAccount" name="fromAccount" type="hidden" value="<set:data name='fromAccount'/>"></td>
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
