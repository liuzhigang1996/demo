<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="fromAccount|fromCurrency|currency|transferAmount">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="17%" class="label1"><set:label name="debit_account" defaultvalue="debit_account" rb='app.cib.resource.txn.bill_payment'/></td>
    <td class="content1"><set:data name="fromAccount" db="caoasaAccountByUser"/><input type="hidden" name="fromAccount" id="fromAccount" value="<set:data name='fromAccount'/>"></td>
  </tr>
  <tr class="greyline">
    <td width="17%" class="label1"><set:label name="debit_ccy" defaultvalue="debit_ccy" rb='app.cib.resource.txn.bill_payment'/></td>
    <td class="content1"><set:data name="fromCurrency" db="rcCurrencyCBS"/><input type="hidden" name="fromCurrency" id="fromCurrency" value="<set:data name='fromCurrency'/>"></td>
  </tr>
  <tr>
    <td class="label1"><set:label name="total_transfer_amount" defaultvalue="total_transfer_amount" rb='app.cib.resource.txn.bill_payment'/></td>
    <td class="content1">MOP - <set:data name="transferAmount" format="amount"/><input type="hidden" name="currency" id="currency" value="MOP"><input type="hidden" name="transferAmount" id="transferAmount" value="<set:data name='transferAmount'/>"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td class="listheader1" align="left"><set:label name="merchant" defaultvalue="merchant" rb='app.cib.resource.txn.bill_payment'/></td>
    <td class="listheader1" align="left"><set:label name="billNoAndCardNo" defaultvalue="billNoAndCardNo" rb='app.cib.resource.txn.bill_payment'/></td>
    <td class="listheader1" align="center"><set:label name="ccy" defaultvalue="ccy" rb='app.cib.resource.txn.bill_payment'/></td>
    <td class="listheader1" align="right"><set:label name="payment_amount" defaultvalue="payment_amount" rb='app.cib.resource.txn.bill_payment'/></td>
  </tr>
  <set:list name="ptList2Jsp" showNoRecord="YES">
  <tr class="<set:listclass class1='' class2='greyline'/>">
    <td class="listcontent1" align="left" valign="middle"><set:listdata name="merchant" db="merchantAll"/>&nbsp;</td>
    <td class="listcontent1" align="left" valign="middle"><set:listdata name="billNo1"/>&nbsp; <set:listif name="billName" value="" condition="notequals"><br>
      <set:listdata name="billName"/>&nbsp;</set:listif> <set:listif name="remark" value="" condition="notequals"><br>
      <set:listdata name="remark"/>&nbsp;</set:listif> </td>
    <td class="listcontent1" align="center" valign="middle"><set:listdata name="currency" db="rcCurrencyCBS"/></td>
    <td class="listcontent1" align="right" valign="middle"><set:listdata name='transferAmount' format='amount'/></td>
  </tr>
  </set:list>
</table>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
