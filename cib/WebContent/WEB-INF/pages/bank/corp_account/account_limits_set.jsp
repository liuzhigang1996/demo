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
var flag = false;
function pageLoad(){
	var allLimits = '<set:data name='allLimits'/>';
	if (allLimits != '') {
		document.form1.allTransType.checked = true;
		disText();
		document.getElementById('limit1').value='';
	}
	/*
	for (i=0; i<form.elements.length; i++){
		if (form.elements[i].type == 'text') {
			if (form.elements[i].name == 'allLimits') {
				form.elements[i].disabled = true;
			} else {
				form.elements[i].disabled = false;
			}
		}
	}
	*/
}
function doSubmit(arg) {
	if (arg == 'apply') {
		document.form1.ActionMethod.value = 'setAccountLimits';
		if (document.form1.allTransType.checked) {
			if (validate_all_limits_set(document.getElementById("form1"))) {
				document.getElementById("form1").submit();
				setFormDisabled("form1");
				setFieldEnabled("return");
			}
		} else {
			if (validate_limit_set(document.getElementById("form1"))) {
				document.getElementById("form1").submit();
				setFormDisabled("form1");
				setFieldEnabled("return");
			}
		}
	} else if(arg == 'return'){
		document.form1.ActionMethod.value = 'listCorpAccount';
		document.getElementById("form1").submit();
		setFormDisabled("form1");
		//setFieldEnabled("buttonReturn");
	}
}
function disText(){
	var form = document.form1;
	for (i=0; i<form.elements.length; i++){
		if (form.elements[i].type == 'text') {
			if (form.elements[i].name == 'allLimits') {
				form.elements[i].disabled = flag;
				if(flag) {
					form.elements[i].value = '';
				}
				flag = !flag;
			} else {
				form.elements[i].disabled = flag;
				if(flag) {
					form.elements[i].value = '';
				}
			}
		}
	}
}

</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpAccountLimits.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.corp_account" -->
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_limits_set" defaultvalue="ACCOUNT LIMITS"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/corpAccountLimits.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="all_limits_set" form="form1" file="corp_account" />
						<set:fieldcheck name="limit_set" form="form1" file="corp_account" />
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="setup_limits" defaultvalue="setup_limits"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="corp_Id" defaultvalue="corp_Id"/></td>
                <td width="72%" class="content1"><set:data name='corpId'/><input name="corpId" type="hidden" id="corpId" value="<set:data name='corpId' />">
                                         
                <input name="dailyLimit" type="hidden" id="dailyLimit" value="<set:data name='dailyLimit' />">
                         
                </td>
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
                <td width="28%" class=""><input name="allTransType" type="checkbox" id="allTransType" onClick="disText();" value="checked">
                  <set:label name="all_trans_type" defaultvalue="all_trans_type"/></td>
              	<td width="72%" class="">
				<div align="right">
              	  <input name="allLimits" type="text" disabled id="allLimits" value="<set:data name='allLimits' format='amount'/>" placeholder="<set:label name="maximum_transaction_amount" defaultvalue="maximum_transaction_amount"/><set:data name='dailyLimit' format='amount'/>">
              	</div></td>
              </tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="groupinput"><set:label name="Trans_money" defaultvalue="Trans_money"/></td>
              	<td class="groupinput"><div align="right"><set:label name="maximum_daily_limit"/> (<set:data name='currency' db='rcCurrencyCBS'/>)</div></td>
              </tr>
              <tr>
                <td width="53%" class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="online_own_accounts" defaultvalue="online_own_accounts"/>--><set:label name="TO_MY_ACCOUNT" defaultvalue="To My Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                <td width="27%" class="content1"><div align="right"><input type="text"  name="limit1" id="limit1" value = "<set:data name='limit1' format='amount'/>" placeholder="<set:label name="maximum_transaction_amount" defaultvalue="maximum_transaction_amount"/><set:data name='dailyLimit' format='amount'/>"></div></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="accounts_in_bank" defaultvalue="accounts_in_bank"/>--><set:label name="TO_MDB_BANK_ACCOUNT" defaultvalue="To MDB Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                <td class="content1"><div align="right"><input id="limit2" type="text" name="limit2" value="<set:data name='limit2' format='amount'/>" placeholder="<set:label name="maximum_transaction_amount" defaultvalue="maximum_transaction_amount"/><set:data name='dailyLimit' format='amount'/>"></div></td>
              </tr>
              <tr>
                <td width="53%" class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="accounts_in_macau" defaultvalue="accounts_in_macau"/>--><set:label name="TO_LOCAL_BANK_ACCOUNT" defaultvalue="To Local Bank Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                <td width="47%" class="content1"><div align="right"><input id="limit3" type="text" name="limit3" value="<set:data name='limit3' format='amount'/>" placeholder="<set:label name="maximum_transaction_amount" defaultvalue="maximum_transaction_amount"/><set:data name='dailyLimit' format='amount'/>"></div></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="accounts_in_overseas" defaultvalue="accounts_in_overseas"/>--><set:label name="TO_OVERSEAS_BANK_ACCOUNT" defaultvalue="To Overseas Bank Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                <td class="content1"><div align="right"><input id="limit4" type="text" name="limit4" value="<set:data name='limit4' format='amount'/>" placeholder="<set:label name="maximum_transaction_amount" defaultvalue="maximum_transaction_amount"/><set:data name='dailyLimit' format='amount'/>"></div></td>
              </tr>
              <tr>
                <td class="label1"><set:label name="time_deposit" defaultvalue="time_deposit"/></td>
                <td class="content1"><div align="right"><input id="limit7" type="text" name="limit7" value="<set:data name='limit7' format='amount'/>" placeholder="<set:label name="maximum_transaction_amount" defaultvalue="maximum_transaction_amount"/><set:data name='dailyLimit' format='amount'/>"></div></td>
              </tr>
              <%--<tr>
                <td width="53%" class="label1"><set:label name="corp_fund_allocation" defaultvalue="corp_fund_allocation"/></td>
                <td width="47%" class="content1"><div align="right"><input id="limit5" type="text" name="limit5" value="<set:data name='limit5' format='amount'/>" placeholder="<set:label name="maximum_transaction_amount" defaultvalue="maximum_transaction_amount"/><set:data name='dailyLimit' format='amount'/>"></div></td>
              </tr>
            --%></table>
            <%--<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="making_payment" defaultvalue="making_payment"/></td>
              </tr>
              <!--<tr>
                <td width="53%" class="label1"><set:label name="bill_payment" defaultvalue="bill_payment"/></td>
                <td width="47%" class="content1"><div align="right"><input id="limit6" type="text" name="limit6" value="<set:data name='limit6' format='amount'/>" placeholder="<set:label name="maximum_transaction_amount" defaultvalue="maximum_transaction_amount"/><set:data name='dailyLimit' format='amount'/>""></div></td>
              </tr>
              <tr>
                <td class="label1"><set:label name="time_deposit" defaultvalue="time_deposit"/></td>
                <td class="content1"><div align="right"><input id="limit7" type="text" name="limit7" value="<set:data name='limit7' format='amount'/>" placeholder="<set:label name="maximum_transaction_amount" defaultvalue="maximum_transaction_amount"/><set:data name='dailyLimit' format='amount'/>"></div></td>
              </tr>
              <tr class="">
                <td class="label1"><set:label name="payroll" defaultvalue="payroll"/></td>
                <td class="content1"><div align="right"><input id="limit8" type="text" name="limit8" value="<set:data name='limit8' format='amount'/>" placeholder="<set:label name="maximum_transaction_amount" defaultvalue="maximum_transaction_amount"/><set:data name='dailyLimit' format='amount'/>"></div></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="bank_draft" defaultvalue="bank_draft"/></td>
                <td class="content1"><div align="right"><input id="limit9" type="text" name="limit9" value="<set:data name='limit9' format='amount'/>" placeholder="<set:label name="maximum_transaction_amount" defaultvalue="maximum_transaction_amount"/><set:data name='dailyLimit' format='amount'/>"></div></td>
              </tr>
              <tr class="">
                <td class="label1"><set:label name="cashier_order" defaultvalue="cashier_order"/></td>
                <td class="content1"><div align="right"><input id="limit10"  type="text" name="limit10" value="<set:data name='limit10' format='amount'/>" placeholder="<set:label name="maximum_transaction_amount" defaultvalue="maximum_transaction_amount"/><set:data name='dailyLimit' format='amount'/>"></div></td>
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
					<input id="apply" name="apply" type="button" value="<set:label name='buttonOK'/>" onClick="doSubmit('apply')">
                  	<input name="return" id="return" type="button" value="<set:label name='buttonReturn' />" onClick="doSubmit('return')">
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
