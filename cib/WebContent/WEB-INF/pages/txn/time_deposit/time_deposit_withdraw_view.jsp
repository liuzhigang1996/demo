<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="tdAccount|term|currency|principal|maturityDate|currency|interest|penalty|netInterestAmt|netCreditAmt|currentAccount">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td width="28%" class="label1"><set:label name='time_deposit_No' defaultvalue="time_deposit_No" rb="app.cib.resource.txn.time_deposit"/></td>
    <td width="72%" class="content1"><set:data name="tdAccount" db='tdAccountByUser'/>(<set:data name="term" />)
    <input id="tdAccount" type="hidden" name="tdAccount" value="<set:data name='tdAccount'/>">
    <input id="term" type="hidden" name="term" value="<set:data name='term'/>"></td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="principal" defaultvalue="principal" rb="app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"><set:data name="currency" db="rcCurrencyCBS"/> <set:data name="principal" format="amount"/>
    <input id="currency" type="hidden" name="currency" value="<set:data name='currency'/>">
    <input id="principal" type="hidden" name="principal" value="<set:data name='principal'/>"></td>
  </tr>
 <!-- add by lzg 20190718 -->
   <tr>
     <td class="label1"><set:label name="period" defaultvalue="Period" rb="app.cib.resource.txn.time_deposit"/></td>
     <td class="content1"><set:data name="term" db="period"/></td>
   </tr>
 <!-- add by lzg end -->
  <tr class="greyline">
    <td class="label1"><set:label name="maturity_date" defaultvalue="maturity_date" rb="app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"><set:data name="maturityDate" format='date'/>
    <input id="maturityDate" type="hidden" name="maturityDate" value="<set:data name='maturityDate'/>"></td>
  </tr>
  <tr >
    <td class="label1"><set:label name="interest_rate" defaultvalue="interest_rate" rb="app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"> <set:data name="interest" format="percent"/>
    <input id="interest" type="hidden" name="interest" value="<set:data name='interest'/>"></td>
  </tr>
  <!-- add by lzg 20190610 -->
  <tr class="greyline">
    <td class="label1"><set:label name="interest_accrued" defaultvalue="interest_accrued" rb = "app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"> <set:data name="currency" db="rcCurrencyCBS"/> <set:data name="netCreditAmount" format='amount'/></td>
  </tr>
<!-- add by lzg 20190718 -->
  <tr >
    <td class="label1"><set:label name="value_date" defaultvalue="Value Date" rb = "app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"><set:data name="valueDate" format='date'/></td>
  </tr>
<!-- add by lzg end -->
  <!-- add by lzg end -->
  <!--<tr class="greyline">
    <td class="label1"><set:label name="penalty_for_early_withdrawal" defaultvalue="penalty_for_early_withdrawal" rb="app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"><set:data name="currency" db="rcCurrencyCBS"/> <font color="#FF0000">-<set:data name="penalty" format="amount"/></font>
    <input id="penalty" type="hidden" name="penalty" value="<set:data name='penalty'/>"></td>
  </tr>
  --><%--<tr class="greyline">
    <td class="label1"><set:label name="net_interest_amount" defaultvalue="net_interest_amount" rb="app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"><set:data name="currency" db="rcCurrencyCBS"/> <set:data name="netCreditAmount" format="amount"/>
    <input id="netInterestAmt" type="hidden" name="netInterestAmt" value="<set:data name='netInterestAmt'/>"></td>
  </tr>
  --%>
  <input id="netCreditAmt" type="hidden" name="netCreditAmt" value="<set:data name='netCreditAmt'/>">
  <tr class="greyline">
    <td class="label1"><set:label name="credit_ccount" defaultvalue="credit_ccount" rb="app.cib.resource.txn.time_deposit"/></td>
    <%--<td class="content1"><set:data name="currentAccount" db="accountByUser"/> mod by linrui 20190601--%>
    <td class="content1"><set:data name="currentAccount"/>&nbsp;<set:data name="currentAccountCcy"/>
    <input id="currentAccount" type="hidden" name="currentAccount" value="<set:data name='currentAccount'/>"></td>
  </tr>
  <!-- add by lzg 20190610 -->
  <tr >
   <td class="label1"><set:label name="principal" rb = "app.cib.resource.txn.time_deposit"/>&nbsp;+&nbsp;<set:label name="interest" rb = "app.cib.resource.txn.time_deposit"/></td>
   <td class="content1"><set:data name="currency" db="rcCurrencyCBS"/> <set:data name='netCreditAmt' format="amount"/></td>
 </tr>
 <!-- add by lzg end -->
  <!-- add by linrui for add Maturity Instruction 20190424 -->
  <tr  class="greyline">
     <td class="label1"><set:label name="Maturity_Instruction" defaultvalue="Maturity Instruction" rb="app.cib.resource.txn.time_deposit"/></td>
     <td class="content1"><set:data name='instCd' rb='app.cib.resource.txn.timedeposit_inst_cd'/></td>
  </tr>
  <!-- add by lzg 20190624 for pwc-->
	<set:singlesubmit/>
  <!-- add by lzg end -->
  <!-- end -->
</table>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
