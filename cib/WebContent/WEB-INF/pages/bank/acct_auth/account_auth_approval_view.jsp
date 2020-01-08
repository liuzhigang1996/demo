<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.sys.account_authorization">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td colspan="2" class="groupinput"><set:label name="Acct_Auth_Info" rb="app.cib.resource.sys.account_authorization"/></td>
  </tr>  
  <tr>
    <td width="28%" class="label1"><set:label name="Corp_Id" defaultvalue="Company Id"/></td>
    <td width="72%" class="content1"><set:data name='corpId'/></td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="Corp_Name" defaultvalue="Company Name"/></td>
    <td class="content1"><set:data name='corpName'/></td>
  </tr>
  <tr>
	<td class="label1"><set:label name="Acct_No" defaultvalue="Account No"/></td>
	<td class="content1"><set:data name='account'/></td>
  </tr>
  <tr class="greyline">
	<td class="label1"><set:label name="Auth_User" defaultvalue="Authorization User"/></td>
	<td class="content1"><set:data name='authUser'/></td>
  </tr>
</table>
</set:loadrb> <!-- InstanceEndEditable --><!-- InstanceEnd -->
