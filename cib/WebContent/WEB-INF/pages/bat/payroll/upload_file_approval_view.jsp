<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="currency|totalAmount|totalNumber|startValueDate|remark">
<table width="100%" border="0" cellspacing="0" cellpadding="3" id="round">
  <tr class="">
    <td width="28%" class="label1"><set:label name="total_amt" defaultvalue="total_amt" rb="app.cib.resource.bat.payroll"/></td>
    <td width="72%" class="content1"><set:data name="currency"/>
      <set:data name="totalAmount" format="amount"/>
      <input id="currency" type="hidden" name="currency" value="<set:data name='currency'/>">
      <input id="amount" type="hidden" name="amount" value="<set:data name='amount'/>">
	</td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="no_of_record" defaultvalue="no_of_record" rb="app.cib.resource.bat.payroll"/></td>
    <td class="content1"><set:data name="totalNumber" />
      <input id="totalNumber" type="hidden" name="totalNumber" value="<set:data name='totalNumber'/>"></td>
  </tr>
  <tr class="">
    <td class="label1"><set:label name="pay_day" defaultvalue="pay_day" rb="app.cib.resource.bat.payroll"/></td>
    <td class="content1"><set:data name="startValueDate" format="date"/>
      <input id="startValueDate" type="hidden" name="startValueDate" value="<set:data name='startValueDate'/>">
	</td>
  </tr>
</table>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
