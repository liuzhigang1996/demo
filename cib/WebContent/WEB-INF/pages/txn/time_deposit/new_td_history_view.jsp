<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.time_deposit">
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
	if (arg == 'ok') {
		if ('<set:data name = "returnUrl"/>' == '') {
			document.form1.ActionMethod.value = 'listHistory';
			document.form1.submit();
		} else {
			window.location = '<set:data name = "returnUrl"/>';
		}
		setFormDisabled("form1");
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/timeDeposit.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.time_deposit" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_history" defaultvalue="MDB Corporate Online Banking > Time Deposit > New Time Deposit"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_new" defaultvalue="NEW TIME DEPOSTIT"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/timeDeposit.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="history_detail" defaultvalue="history_detail"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="debit_account" defaultvalue="debit_account"/></td>
                <td width="72%" class="content1"><set:data name='currentAccount'/>&nbsp;<set:data name='currentAccountCcy' db="rcCurrencyCBS"/>
				 </td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="debit_amount" defaultvalue="debit_amount"/></td>
                <td class="content1"><set:data name='currentAccountCcy' db="rcCurrencyCBS"/>&nbsp;<set:data name='currentAccPrincipal' format="amount"/>
				</td>
              </tr>
              <tr>
                <td class="label1"><set:label name="time_deposit_currency" defaultvalue="time_deposit_currency"/></td>
                <td class="content1"><set:data name='currency' db="rcCurrencyCBS"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="period" defaultvalue="period"/></td>
                <td class="content1">
					<set:data name='term' db="period"/>
				</td>
              </tr>
              <tr>
                <td class="label1"><set:label name="time_deposit_amount" defaultvalue="time_deposit_amount"/></td>
                <td class="content1"><set:data name='currency' db="rcCurrencyCBS"/>&nbsp;<set:data name='principal' format="amount"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="interest_rate" defaultvalue="interest_rate"/></td>
                <td class="content1"><set:data name='defaultRate' format='percent' /></td> <!-- pattern="#,##0.0000" -->
              </tr>
              <tr>
                <td class="label1"><set:label name="value_date" defaultvalue="value_date"/></td>
                <td class="content1"><set:data name='valueDate' format='date'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="maturity_date" defaultvalue="maturity_date"/></td>
                <td class="content1"><set:data name='maturityDate' format='date'/></td>
              </tr>
              <!-- add by lzg 20191018 -->
              <tr >
                <td class="label1"><set:label name="Expected_Interest" /></td>
                <td class="content1"><set:data name='expectedInterest' format='amount'/></td>
              </tr>
              <!-- add by lzg end -->
              <!-- add by linrui for add Maturity Instruction 20190424 -->
              <tr class="greyline">
                <td class="label1"><set:label name="Maturity_Instruction" defaultvalue="Maturity Instruction" rb="app.cib.resource.txn.time_deposit"/></td>
                <td class="content1"><set:data name='instCd' rb='app.cib.resource.txn.timedeposit_inst_cd'/></td>
              </tr>
              <!-- end -->
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
					<input id="ok" name="ok" type="button" value="&nbsp;&nbsp;<set:label name='return' defaultvalue='confirm'/>&nbsp;&nbsp;" onClick="doSubmit('ok');">
				  	<input id="ActionMethod" name="ActionMethod" type="hidden" value="listHistory">
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
