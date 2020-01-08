<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="currentAccountCcy|currentAccount|currentAccountCcy|currentAccPrincipal|currency|term|principal|defaultRate|maturityDate">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td width="28%" class="label1"><set:label name="debit_account" defaultvalue="debit_account" rb="app.cib.resource.txn.time_deposit"/></td>
    <td width="72%" class="content1"><set:data name='currentAccountCcy' db="rcCurrencyCBS"/>&nbsp;<set:data name='currentAccount'/> <input id="currentAccountCcy" type="hidden" name="currentAccountCcy" value="<set:data name='currentAccountCcy'/>">
    <input id="currentAccount" type="hidden" name="currentAccount" value="<set:data name='currentAccount'/>"></td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="debit_amount" defaultvalue="debit_amount" rb="app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"><set:data name='currentAccountCcy' db="rcCurrencyCBS"/>&nbsp;<set:data name='currentAccPrincipal' format="amount"/> <input id="currentAccountCcy" type="hidden" name="currentAccountCcy" value="<set:data name='currentAccountCcy'/>">
    <input id="currentAccPrincipal" type="hidden" name="currentAccPrincipal" value="<set:data name='currentAccPrincipal'/>"></td>
  </tr>
  <tr>
    <td class="label1">
		<set:if name="status" value="1" condition="equals">
			<set:label name="time_deposit_currency" defaultvalue="time_deposit_currency" rb="app.cib.resource.txn.time_deposit"/>
		</set:if>
		<set:if name="newTdAccount" value="" condition="notequals">
			<set:label name="time_deposit_No" defaultvalue="time_deposit_No" rb="app.cib.resource.txn.time_deposit"/>
		</set:if>
	</td>
    <td class="content1"><set:data name='currency' db="rcCurrencyCBS"/>
    	<input id="currency" type="hidden" name="currency" value="<set:data name='currency'/>">
		<set:if name="newTdAccount" value="" condition="notequals">
			-&nbsp;<set:data name="newTdAccount"/>
		</set:if>
	</td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="period" defaultvalue="period" rb="app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"><set:data name='term' db="period"/> <input id="term" type="hidden" name="term" value="<set:data name='term'/>"></td>
  </tr>
  <tr>
    <td class="label1"><set:label name="time_deposit_amount" defaultvalue="time_deposit_amount" rb="app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"><set:data name='currency' db="rcCurrencyCBS"/>&nbsp;<set:data name='principal' format="amount"/>
    <input id="principal" type="hidden" name="principal" value="<set:data name='principal'/>"></td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="interest_rate" defaultvalue="interest_rate" rb="app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"><set:data name='defaultRate' format='percent'/> <!-- pattern="#,##0.0000" -->
    <input id="defaultRate" type="hidden" name="defaultRate" value="<set:data name='defaultRate'/>"></td>
  </tr>
  <tr>
    <td class="label1"><set:label name="value_date" defaultvalue="value_date" rb="app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"><set:data name='valueDate' format='date'/></td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="maturity_date" defaultvalue="maturity_date" rb="app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"><set:data name='maturityDate' format='date'/>
    <input id="maturityDate" type="hidden" name="maturityDate" value="<set:data name='maturityDate'/>"></td>
  </tr>
  <!-- add by lzg 20191018 -->
  <tr >
    <td class="label1"><set:label name="Expected_Interest" /></td>
    <td class="content1"><set:data name='expectedInterest' format='amount'/></td>
  </tr>
  <!-- add by lzg end -->
  <!-- add by linrui for add Maturity Instruction 20190424 -->
  <tr class="greyline">
    <td class="label1"><set:label name="Maturity_Instruction" defaultvalue="Maturity Instruction" rb="app.cib.resource.txn.time_deposit"/></td>
    <td class="content1"><set:data name='instCd' rb='app.cib.resource.txn.timedeposit_inst_cd'/></td>
  </tr>
  <!-- end -->
  <!-- add by lzg 20190624 for pwc-->
	<set:singlesubmit/>
  <!-- add by lzg end -->
</table>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
