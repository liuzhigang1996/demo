<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.srv.protection_check_request">
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="totalNumber">
<table width="100%" border="0" cellspacing="0" cellpadding="3" id="round">
  <tr>
    <td width="28%"  class="label1"><set:label name="no_of_record" defaultvalue="no_of_record"/></td>
    <td  width="72%" class="content1"><set:data name="totalNumber" />
      <input name="totalNumber" type="hidden" id="totalNumber" value="<set:data name='totalNumber'/>"></td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="groupinput"><set:label name="nor_record_below" defaultvalue="nor_record_below"/> </td>
  </tr>
</table>
<div align="center">
  <table width="100%" border="0" cellspacing="0" cellpadding="3">
    <tr>
      <td class="listheader1"><set:label name="line_in_file" defaultvalue="line_in_file"/></td>
        <td class="listheader1"><set:label name="ref_no" defaultvalue="ref_no"/></td>
        <td class="listheader1"><set:label name="payee_acc" defaultvalue="payee_acc"/></td>
        <td align="right" class="listheader1"><set:label name="amt" defaultvalue="amt"/></td>
   	    <td class="listheader1"><set:label name="cheque_style" defaultvalue="Cheque Style"/></td>
	    <td class="listheader1"><set:label name="cheque_number" defaultvalue="Cheque Number"/></td>
      </tr>
    <set:list name="recList"  showNoRecord="YES">
      <tr class="<set:listclass class1='' class2='greyline'/>">
        <td class="listcontent1"><set:listdata name="lineNo" /></td>
        <td class="listcontent1"><set:listdata name="transId"/></td>
	     <td class="listcontent1"><set:listdata name="account" /></td>
        <td align="right" class="listcontent1"><set:listdata name="amount" format="amount"/></td>
	    <td class="listcontent1"><set:listdata name="chequeStyle" /></td>
	    <td class="listcontent1"><set:listdata name="chequeNumber" /></td>
      </tr>
      </set:list>
  </table>
</div>
</set:loadrb> <!-- InstanceEndEditable --><!-- InstanceEnd -->
