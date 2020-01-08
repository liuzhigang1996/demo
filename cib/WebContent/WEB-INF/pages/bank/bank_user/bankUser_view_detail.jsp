<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.bnk.bank_user">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="User_Info" defaultvalue="User Information"/></td>
              </tr>
   <tr>
                <td width="28%" class="label1"><set:label name="User_Id" defaultvalue="User Id"/></td>
                <td width="72%" class="content1"><set:data name='userId'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="User_Name" defaultvalue="User Name"/></td>
                <td class="content1"><set:data name='userName'/></td>
              </tr>
              <tr>
                <td class="label1"><set:label name="Email" defaultvalue="Email"/></td>
                <td class="content1"><set:data name='email'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Role" defaultvalue="Role"/></td>
                <td class="content1"><set:data name="roleId" rb="app.cib.resource.common.bank_role" /></td>
              </tr>
</table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
