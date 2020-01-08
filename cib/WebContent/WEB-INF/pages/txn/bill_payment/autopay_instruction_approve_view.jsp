<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="apsCode|contractNo|paymentLimit|payCurrency|payAcct">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td width="28%" class="label1"><set:label name="PAYMENT_TYPE" defaultvalue="PAYMENT_TYPE" rb='app.cib.resource.txn.autopay_instruction'/></td>
    <td width="72%" class="content1">
    <set:data name='apsCode' db="autopay"/>
    </td>
  </tr>
  <tr >
    <td width="28%" class="label1" valign="top">
    <set:if name='labelContact' condition='equals' value='CN'><set:label name="CONTRACT_NUMBER" defaultvalue="CONTRACT_NUMBER" rb='app.cib.resource.txn.autopay_instruction'/></set:if>
    <set:if name='labelContact' condition='equals' value='CCN'><set:label name='CREDIT_CARD_NO' defaultvalue='CREDIT_CARD_NO' rb='app.cib.resource.txn.autopay_instruction'/></set:if>
    </td>
    <td width="72%" class="content1">
    <set:data name='contractNo' />
    </td>
  </tr>
  <tr class="">
    <td width="28%" class="label1" valign="top"><set:label name="PAYMENT_LIMINT" defaultvalue="PAYMENT_LIMINT" rb='app.cib.resource.txn.autopay_instruction'/></td>
    <td width="72%" class="content1">
    <set:if name='paymentLimit' condition='equals' value='9.999999999E9'>
        <set:label name="FULL_PAYMENT" defaultvalue="Full Payment" rb='app.cib.resource.txn.autopay_instruction'/>
    </set:if>
    <set:if name='paymentLimit' condition='notequals' value='9.999999999E9'>
        <set:if name='paymentLimit' condition='equals' value='0.0'>
            <set:label name="Minimum_Payment" defaultvalue="Minimum Payment" rb='app.cib.resource.txn.autopay_instruction'/>
        </set:if>
        <set:if name='paymentLimit' condition='notequals' value='0.0'>
            <set:data name='paymentLimit' format='amount' pattern='#' />
        </set:if>
    </set:if>
    </td>
  </tr>
  <tr class="">
    <td width="28%" class="label1"><set:label name="PAYMENT_ACCOUNT" defaultvalue="PAYMENT_ACCOUNT" rb='app.cib.resource.txn.autopay_instruction'/></td>
    <td width="72%" class="content1">
    <set:data name='payCurrency' db="rcCurrencyCBS"/> - <set:data name='payAcct' />
    </td>
  </tr>
</table>
<!-- InstanceEndEditable -->
<!-- InstanceEnd -->