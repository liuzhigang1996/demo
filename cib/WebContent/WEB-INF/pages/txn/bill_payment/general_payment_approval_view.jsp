<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="merchant|billNo1|currency|transferAmount|fromAccount|debitCurrency|debitAmount|remark">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td width="28%" class="label1"><set:label name="merchant" defaultvalue="merchant" rb='app.cib.resource.txn.bill_payment'/></td>
    <td width="72%" class="content1"><set:data name='merchant' db="merchant"/> </td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="billNo" defaultvalue="billNo" rb='app.cib.resource.txn.bill_payment'/></td>
    <td class="content1"><set:data name='billNo1'/>
      <input id="billNo1" type="hidden" name="billNo1" value="<set:data name='billNo1'/>">
      <input id="currency" type="hidden" name="currency" value="<set:data name='currency'/>"></td>
  </tr>
  <tr>
    <td class="label1"><set:label name="bill_amount" defaultvalue="bill_amount" rb='app.cib.resource.txn.bill_payment'/></td>
    <td class="content1"><set:data name='currency' db="rcCurrencyCBS"/> - <set:data name='transferAmount' format="amount"/>
      <input id="transferAmount" type="hidden" name="transferAmount" value="<set:data name='transferAmount'/>"></td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="debit_account" defaultvalue="debit_account" rb='app.cib.resource.txn.bill_payment'/></td>
    <td class="content1"><set:data name='fromAccount' db = "accountByUser"/>
      <input id="fromAccount" type="hidden" name="fromAccount" value="<set:data name='fromAccount'/>"></td>
  </tr>
  <tr class="">
    <td width="28%" class="label1"><set:label name="debit_amount" defaultvalue="debit_amount" rb='app.cib.resource.txn.bill_payment'/></td>
    <td width="72%" class="content1"><set:data name='debitCurrency' db="rcCurrencyCBS"/> - <set:data name='debitAmount' format="amount"/>
      <input id="debitCurrency" type="hidden" name="debitCurrency" value="<set:data name='debitCurrency'/>">
      <input id="debitAmount" type="hidden" name="debitAmount" value="<set:data name='debitAmount'/>">
    </td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="remark" defaultvalue="remark" rb='app.cib.resource.txn.bill_payment'/></td>
    <td class="content1"><set:data name='remark'/>
      <input id="remark" type="hidden" name="remark" value="<set:data name='remark'/>"></td>
  </tr>
</table>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
