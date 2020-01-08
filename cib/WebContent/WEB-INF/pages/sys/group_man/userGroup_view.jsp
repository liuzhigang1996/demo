<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td class="groupinput" colspan="5"><set:label name="User_Group_Info" defaultvalue="User Group Information" rb="app.cib.resource.sys.group_man"/></td>
  </tr>
  <tr>
    <td class="content1" width="5%">&nbsp;</td>
    <td class="content1" width="25%"><set:label name="User_Group_Name" defaultvalue="User Group Name" rb="app.cib.resource.sys.group_man"/></td>
    <td class="content1" colspan="2"><set:data name="groupName"/></td>
    <td class="content1" width="5%">&nbsp;</td>
  </tr>
  <tr class="greyline">
    <td class="content1">&nbsp;</td>
    <td class="content1" ><set:label name="Role" defaultvalue="Role" rb="app.cib.resource.sys.group_man"/></td>
    <td class="content1" colspan="2"><set:data name="roleId" rb="app.cib.resource.common.corp_role"/></td>
    <td class="content1">&nbsp;</td>
  </tr>
  <tr>
    <td class="content1">&nbsp;</td>
    <td class="content1" ><set:label name="Account_List" defaultvalue="Account List" rb="app.cib.resource.sys.group_man"/></td>
    <td colspan="2"><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <set:list name="item_accountList">
        <tr>
          <td class="content1" width="40%"><img src="/cib/images/check1.gif" border="0" align="absmiddle"> <set:listdata name='accountNo'/></td>
          <td class="content1"><set:listdata name='accountName'/> </td>
		  <td class="content1"><set:listdata name='accountType' rb="app.cib.resource.common.account_type_desc"/></td>
        </tr>
             		 <tr>
               	 		<td colspan="3" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
      </set:list>
    </table></td>
    <td class="content1">&nbsp;</td>
  </tr>
  <tr class="greyline">
    <td class="content1">&nbsp;</td>
    <td class="label1"><set:label name="Function_List" defaultvalue="Function List" rb="app.cib.resource.sys.group_man"/></td>
    <td colspan="2">
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <set:list name="item_functionList">
          <tr>
            <td class="content1" width="40%"><img src="/cib/images/check1.gif" border="0" align="absmiddle"> <set:listdata name='permissionResource' rb="app.cib.resource.sys.functionList"/></td>
            <td class="content1" width="28%">&nbsp;</td>
          </tr>
             		 <tr>
               	 		<td colspan="3" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
        </set:list>
      </table>
    </td>
    <td class="content1">&nbsp;</td>
  </tr>
  <tr>
    <td class="content1" width="5%">&nbsp;</td>
    <td class="content1" width="25%"><set:label name="Description" defaultvalue="Description" rb="app.cib.resource.sys.group_man"/></td>
    <td class="content1" colspan="2"><set:data name="groupDesc"/></td>
    <td class="content1" width="5%">&nbsp;</td>
  </tr>
</table>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
