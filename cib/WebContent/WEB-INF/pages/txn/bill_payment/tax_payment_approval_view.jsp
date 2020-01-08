<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="billNo1|billNo2|currency|transferAmount|refNo|fromAccount|debitCurrency|debitAmount|remark">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td width="28%" class="label1"><set:label name="pay_to" defaultvalue="pay_to" rb='app.cib.resource.txn.bill_payment'/></td>
    <td width="72%" class="content1"><set:label name="pay_to2" defaultvalue="pay_to2" rb='app.cib.resource.txn.bill_payment'/>
    </td>
  </tr>
  <tr class="greyline">
    <td width="28%" class="label1"><set:label name="tax" defaultvalue="tax" rb='app.cib.resource.txn.bill_payment'/></td>
    <td width="72%" class="content1"><set:data name='billType' db='tax'/> <input name="billType" type="hidden" id="billType" value="<set:data name='billType'/>"></td>
  </tr>
  <tr>
    <td width="28%" class="label1"><set:label name="nr_conhecimento" defaultvalue="nr_conhecimento" rb='app.cib.resource.txn.bill_payment'/></td>
    <td width="72%" class="content1"><set:data name='billNo1'/> <input name="billNo1" type="hidden" id="billNo1" value="<set:data name='billNo1'/>"></td>
  </tr>
  <tr class="greyline">
    <td width="28%" class="label1"><set:label name="nr_contribuinte" defaultvalue="nr_contribuinte" rb='app.cib.resource.txn.bill_payment'/></td>
    <td width="72%" class="content1"><set:data name='billNo2'/> <input name="billNo2" type="hidden" id="billNo2" value="<set:data name='billNo2'/>"></td>
  </tr>
  <tr>
    <td width="28%" class="label1"><set:label name="payment_amount" defaultvalue="payment_amount" rb='app.cib.resource.txn.bill_payment'/></td>
    <td width="72%" class="content1"><set:data name='currency' db="rcCurrencyCBS"/> - <set:data name='transferAmount' format="amount"/> <input name="currency" type="hidden" id="currency" value="<set:data name='currency'/>">
    <input name="transferAmount" type="hidden" id="transferAmount" value="<set:data name='transferAmount'/>"></td>
  </tr>
  <tr class="greyline">
    <td width="28%" class="label1"><set:label name="reference_No" defaultvalue="reference_No" rb='app.cib.resource.txn.bill_payment'/></td>
    <td width="72%" class="content1"><set:data name='refNo'/>
      <input name="refNo" type="hidden" id="refNo" value="<set:data name='refNo'/>">
    </td>
  </tr>
  <tr class="">
    <td width="28%" class="label1"><set:label name="payment_account" defaultvalue="payment_account" rb='app.cib.resource.txn.bill_payment'/></td>
    <td width="72%" class="content1"><set:data name='fromAccount' db="accountByUser"/>
      <input name="fromAccount" type="hidden" id="fromAccount" value="<set:data name='fromAccount'/>">
    </td>
  </tr>
  <tr class="greyline">
    <td width="28%" class="label1"><set:label name="debit_amount" defaultvalue="debit_amount" rb='app.cib.resource.txn.bill_payment'/></td>
    <td width="72%" class="content1"><set:data name='debitCurrency' db="rcCurrencyCBS"/> - <set:data name='debitAmount' format="amount"/>
      <input name="debitCurrency" type="hidden" id="debitCurrency" value="<set:data name='debitCurrency'/>">
      <input name="debitAmount" type="hidden" id="debitAmount" value="<set:data name='debitAmount'/>">
    </td>
  </tr>
  <tr class="">
    <td width="28%" class="label1"><set:label name="remark" defaultvalue="remark" rb='app.cib.resource.txn.bill_payment'/></td>
    <td width="72%" class="content1"><set:data name='remark'/>
    <input name="remark" type="hidden" id="remark" value="<set:data name='remark'/>"> </td>
  </tr>
</table>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
