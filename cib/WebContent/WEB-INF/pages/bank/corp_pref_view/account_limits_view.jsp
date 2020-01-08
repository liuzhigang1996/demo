<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bnk.corp_account">
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
	var allLimits = '<set:data name='allLimits'/>';
	if (allLimits != '') {
		document.form1.allTransType.checked = true;
		document.all('limit1').innerHTML='';
	}
}
function doSubmit(arg) {
	if (arg == 'return') {
		document.form1.ActionMethod.value = 'listCorpAccount';
	} 
	document.getElementById("form1").submit();
	setFormDisabled("form1");
	//setFieldEnabled("buttonReturn");
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpAccountLimitsForSupervisor.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.corp_account" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_limits" defaultvalue="MDB Corporate Online Banking > Corp Account > Account Limits"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_limits_view" defaultvalue="ACCOUNT LIMITS"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/corpAccountLimitsForSupervisor.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="acc_load" form="form1" file="corp_account" />
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="view_limits" defaultvalue="view_limits"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="corp_Id" defaultvalue="corp_Id"/></td>
                <td width="72%" class="content1"><set:data name='corpId' /><input name="corpId" type="hidden" id="corpId" value="<set:data name='corpId' />"></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="corp_name" defaultvalue="corp_name"/></td>
                <td class="content1"><set:data name='corpName'/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="account" defaultvalue="account"/></td>
                <td width="72%" class="content1">
                	<set:if name='account' condition="equals" value="9999999999">
                		<set:label name="all_accounts" defaultvalue="all_accounts"/>
                	</set:if>
                	<set:if name='account' condition="notequals" value="9999999999">
                		<set:data name='account'/>
                	</set:if>
                </td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td width="28%" class=""><input name="allTransType" type="checkbox" id="allTransType" onClick="disText();" value="checked" disabled>
                  <set:label name="all_trans_type" defaultvalue="all_trans_type"/></td>
              	<td width="72%" class="">
				<div align="right">
				  	<set:if condition='notequals' name='txnType' value=''>
						<set:label name="maximum_daily_limit"/>
              	  		<set:data name='allLimits' format='amount'/>
				  		<br>
				  		<set:label name="outstanding_limit"/>: 
				  		<set:data name='oLimit' format='amount'/>
				  	</set:if>
              	</div></td>
              </tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="groupinput"><set:label name="Trans_money" defaultvalue="Trans_money"/></td>
              	<td class="groupinput"><div align="right"><set:label name="maximum_daily_limit"/>(<set:data name='currency' db='rcCurrencyCBS'/>)</div></td>
              	<td class="groupinput"><div align="right"><set:label name="outstanding_limit"/> (<set:data name='currency' db='rcCurrencyCBS'/>)</div></td>
              </tr>
              <tr>
                <td width="58%" class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="online_own_accounts" defaultvalue="online_own_accounts"/>--><set:label name="TO_MY_ACCOUNT" defaultvalue="To My Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                <td width="21%" class="content1"><div align="right" id="limit1"><set:data name='limit1' format='amount'/></div></td>
                <td width="21%" class="content1"><div align="right" id=""><set:data name='oLimit1' format='amount'/></div></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="accounts_in_bank" defaultvalue="accounts_in_bank"/>--><set:label name="TO_MDB_BANK_ACCOUNT" defaultvalue="To MDB Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                <td class="content1"><div align="right"><set:data name='limit2' format='amount'/></div></td>
                <td width="21%" class="content1"><div align="right" id=""><set:data name='oLimit2' format='amount'/></div></td>
              </tr>
              <tr>
                <td width="58%" class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="accounts_in_macau" defaultvalue="accounts_in_macau"/>--><set:label name="TO_LOCAL_BANK_ACCOUNT" defaultvalue="To Local Bank Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                <td width="21%" class="content1"><div align="right"><set:data name='limit3' format='amount'/></div></td>
                <td width="21%" class="content1"><div align="right" id=""><set:data name='oLimit3' format='amount'/></div></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="accounts_in_overseas" defaultvalue="accounts_in_overseas"/>--><set:label name="TO_OVERSEAS_BANK_ACCOUNT" defaultvalue="To Overseas Bank Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                <td class="content1"><div align="right"><set:data name='limit4' format='amount'/></div></td>
                <td width="21%" class="content1"><div align="right" id=""><set:data name='oLimit4' format='amount'/></div></td>
              </tr>
              <tr >
                <td class="label1"><set:label name="time_deposit" defaultvalue="time_deposit"/></td>
                <td class="content1"><div align="right"><set:data name='limit7' format='amount'/></div></td>
                <td width="21%" class="content1"><div align="right" id=""><set:data name='oLimit7' format='amount'/></div></td>
              </tr>
              <%--<tr>
                <td width="58%" class="label1"><set:label name="corp_fund_allocation" defaultvalue="corp_fund_allocation"/></td>
                <td width="21%" class="content1"><div align="right"><set:data name='limit5' format='amount'/></div></td>
                <td width="21%" class="content1"><div align="right" id=""><set:data name='oLimit5' format='amount'/></div></td>
              </tr>
            --%></table>
            <%--<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="3" class="groupinput"><set:label name="making_payment" defaultvalue="making_payment"/></td>
              </tr>
              <!--<tr>
                <td width="58%" class="label1"><set:label name="bill_payment" defaultvalue="bill_payment"/></td>
                <td width="21%" class="content1"><div align="right"><set:data name='limit6' format='amount'/></div></td>
                <td width="21%" class="content1"><div align="right" id=""><set:data name='oLimit6' format='amount'/></div></td>
              </tr>
              <tr >
                <td class="label1"><set:label name="time_deposit" defaultvalue="time_deposit"/></td>
                <td class="content1"><div align="right"><set:data name='limit7' format='amount'/></div></td>
                <td width="21%" class="content1"><div align="right" id=""><set:data name='oLimit7' format='amount'/></div></td>
              </tr>
              <tr class="">
                <td class="label1"><set:label name="payroll" defaultvalue="payroll"/></td>
                <td class="content1"><div align="right"><set:data name='limit8' format='amount'/></div></td>
                <td width="21%" class="content1"><div align="right" id=""><set:data name='oLimit8' format='amount'/></div></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="bank_draft" defaultvalue="bank_draft"/></td>
                <td class="content1"><div align="right"><set:data name='limit9' format='amount'/></div></td>
                <td width="21%" class="content1"><div align="right" id=""><set:data name='oLimit9' format='amount'/></div></td>
              </tr>
              <tr class="">
                <td class="label1"><set:label name="cashier_order" defaultvalue="cashier_order"/></td>
                <td class="content1"><div align="right"><set:data name='limit10' format='amount'/></div></td>
                <td width="21%" class="content1"><div align="right" id=""><set:data name='oLimit10' format='amount'/></div></td>
              </tr>
            --></table>
            --%><table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
					<input id="ok" name="ok" type="button" value="&nbsp;&nbsp;<set:label name='buttonReturn' defaultvalue=' return '/>&nbsp;&nbsp;" onClick="doSubmit('return')">
                  	<input id="ActionMethod" name="ActionMethod" type="hidden" value="return">
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
