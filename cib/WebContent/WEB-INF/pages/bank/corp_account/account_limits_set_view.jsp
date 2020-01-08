<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td width="28%" class="label1"><set:label name="corp_Id" defaultvalue="corp_Id" rb="app.cib.resource.bnk.corp_account"/></td>
    <td width="72%" class="content1"><set:data name='corpId'/> </td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="corp_name" defaultvalue="corp_name" rb="app.cib.resource.bnk.corp_account"/></td>
    <td class="content1"><set:data name='corpName'/> </td>
  </tr>
  <tr>
    <td width="28%" class="label1"><set:label name="account" defaultvalue="account" rb="app.cib.resource.bnk.corp_account"/></td>
    <td width="72%" class="content1"><set:if name='account' condition="equals" value="9999999999"> All Accounts </set:if> <set:if name='account' condition="notequals" value="9999999999"> <set:data name='account'/> </set:if> </td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td width="28%" class=""><input name="allTransType" type="checkbox" id="allTransType" onClick="disText();" value="checked" disabled>
      <set:label name="all_trans_type" defaultvalue="all_trans_type" rb="app.cib.resource.bnk.corp_account"/></td>
    <td width="72%" class=""><div align="right"> <set:data name='allLimits' format='amount'/> </div></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td class="groupinput"><set:label name="Trans_money" defaultvalue="Trans_money" rb="app.cib.resource.bnk.corp_account"/></td>
    <td class="groupinput"><div align="right"><set:label name="maximum_daily_limit" rb="app.cib.resource.bnk.corp_account"/>(<set:data name='currency' db='rcCurrencyCBS'/>)</div></td>
  </tr>
  <tr>
    <td width="53%" class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="online_own_accounts" defaultvalue="online_own_accounts" rb="app.cib.resource.bnk.corp_account"/>--><set:label name="TO_MY_ACCOUNT" defaultvalue="To My Account" rb="app.cib.resource.bnk.corp_account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
    <td width="47%" class="content1"><div align="right" id="limit1"><set:data name='limit1' format='amount'/></div></td>
  </tr>
  <tr class="greyline">
    <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="accounts_in_bank" defaultvalue="accounts_in_bank" rb="app.cib.resource.bnk.corp_account"/>--><set:label name="TO_MDB_BANK_ACCOUNT" defaultvalue="To MDB Account" rb="app.cib.resource.bnk.corp_account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
    <td class="content1"><div align="right"><set:data name='limit2' format='amount'/></div></td>
  </tr>
  <tr>
    <td width="53%" class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="accounts_in_macau" defaultvalue="accounts_in_macau" rb="app.cib.resource.bnk.corp_account"/>--><set:label name="TO_LOCAL_BANK_ACCOUNT" defaultvalue="To Local Bank Account" rb="app.cib.resource.bnk.corp_account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
    <td width="47%" class="content1"><div align="right"><set:data name='limit3' format='amount'/></div></td>
  </tr>
  <tr class="greyline">
    <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="accounts_in_overseas" defaultvalue="accounts_in_overseas" rb="app.cib.resource.bnk.corp_account"/>--><set:label name="TO_OVERSEAS_BANK_ACCOUNT" defaultvalue="To Overseas Bank Account" rb="app.cib.resource.bnk.corp_account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
    <td class="content1"><div align="right"><set:data name='limit4' format='amount'/></div></td>
  </tr>
  <tr>
    <td class="label1"><set:label name="time_deposit" defaultvalue="time_deposit" rb="app.cib.resource.bnk.corp_account"/></td>
    <td class="content1"><div align="right"><set:data name='limit7' format='amount'/></div></td>
  </tr>
  <%--<tr>
    <td width="53%" class="label1"><set:label name="corp_fund_allocation" defaultvalue="corp_fund_allocation" rb="app.cib.resource.bnk.corp_account"/></td>
    <td width="47%" class="content1"><div align="right"><set:data name='limit5' format='amount'/></div></td>
  </tr>
--%></table>
<%--<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td colspan="2" class="groupinput"><set:label name="making_payment" defaultvalue="making_payment" rb="app.cib.resource.bnk.corp_account"/></td>
  </tr>
  <!--<tr>
    <td width="53%" class="label1"><set:label name="bill_payment" defaultvalue="bill_payment" rb="app.cib.resource.bnk.corp_account"/></td>
    <td width="47%" class="content1"><div align="right"><set:data name='limit6' format='amount'/></div></td>
  </tr>
  <tr>
    <td class="label1"><set:label name="time_deposit" defaultvalue="time_deposit" rb="app.cib.resource.bnk.corp_account"/></td>
    <td class="content1"><div align="right"><set:data name='limit7' format='amount'/></div></td>
  </tr>
  <tr class="">
    <td class="label1"><set:label name="payroll" defaultvalue="payroll" rb="app.cib.resource.bnk.corp_account"/></td>
    <td class="content1"><div align="right"><set:data name='limit8' format='amount'/></div></td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="bank_draft" defaultvalue="bank_draft" rb="app.cib.resource.bnk.corp_account"/></td>
    <td class="content1"><div align="right"><set:data name='limit9' format='amount'/></div></td>
  </tr>
  <tr class="">
    <td class="label1"><set:label name="cashier_order" defaultvalue="cashier_order" rb="app.cib.resource.bnk.corp_account"/></td>
    <td class="content1"><div align="right"><set:data name='limit10' format='amount'/></div></td>
  </tr>
--></table>
--%><script language="javascript">
	var allLimits = '<set:data name='allLimits'/>';
	if (allLimits != '') {
		document.all('allTransType').checked = true;
		document.all('limit1').innerHTML='';
	}
</script>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
