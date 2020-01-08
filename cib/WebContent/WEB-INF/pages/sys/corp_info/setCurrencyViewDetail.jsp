<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.common.set_currency">
  <table width="100%" border="0" cellspacing="0" cellpadding="3">
    <tr>
      <td colspan="2" class="listcontent1"><set:label name="functionTitle_set_currency"  rb="app.cib.resource.common.set_currency"/></td>
    </tr>
    
    <tr>
      <td class="listheader1"><set:label name="label_Currency" defaultvalue="Currency" rb="app.cib.resource.common.set_currency"/></td>
      <td class="listheader1"><set:label name="Avaliable_Flag" defaultvalue="Avaliable Flag" rb="app.cib.resource.common.set_currency"/></td>
    </tr>
	<set:list name="ccyShowList">
    <tr class="<set:listclass class1='' class2='greyline'/>">
      <td class="listcontent1"><set:listdata name="ccyCode"/></td>
      <td class="listcontent1"><set:listdata name="availableFlag" rb="app.cib.resource.common.yes_no"/></td>
    </tr>
	</set:list>
  </table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
