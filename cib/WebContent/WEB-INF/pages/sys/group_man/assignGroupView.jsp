<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td colspan="4" class="groupinput"><set:label name="Group_Assignment" rb="app.cib.resource.sys.group_man"/></td>
  </tr>
  <tr>
    <td class="listheader1"><set:label name="User_ID" rb="app.cib.resource.sys.group_man"/></td>
    <td class="listheader1"><set:label name="User_Name" rb="app.cib.resource.sys.group_man"/></td>
    <td class="listheader1"><set:label name="Role" rb="app.cib.resource.sys.group_man"/></td>
    <td class="listheader1"><set:label name="User_Group_Name" rb="app.cib.resource.sys.group_man"/></td>
  </tr>
  <set:list name="newUserList">
    <tr class="<set:listclass class1='' class2='greyline'/>">
      <td class="listcontent1"><set:listdata name="userId"/></td>
      <td class="listcontent1"><set:listdata name="userName"/></td>
      <td class="listcontent1"><set:listdata name="roleId" rb="app.cib.resource.common.corp_role"/></td>
      <td class="listcontent1"><set:listdata name="groupId" db="groupByCorp"/></td>
    </tr>
  </set:list>
</table>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
