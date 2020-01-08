<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.srv.bank_draft_request">
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="toCurrency|toAmount|totalNumber">
<!-- add by lw 20101016 -->
<set:if name="needProof" condition="EQUALS" value="Y">
<table>
  <tr >
    <td class="label1" colspan="2"><font color="#FF0000"><set:label name="Need_Proof_Message" defaultvalue="* Need to provide Proof Document."/></font></td>
	</td>
  </tr>
</table>
</set:if>
<!-- add by lw end -->
<table width="100%" border="0" cellspacing="0" cellpadding="3" id="round">
   <tr class="">
    <td width="28%" class="label1"><set:label name="total_amt" defaultvalue="total_amt"/></td>
    <td width="72%" class="label1"><set:data name="toCurrency"/> <set:data name="toAmount" format="amount"/>
      <input name="toCurrency" type="hidden" id="toCurrency" value="<set:data name='toCurrency'/>">
      <input name="toAmount" type="hidden" id="toAmount" value="<set:data name='toAmount'/>">
    </td>
  </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="no_of_record" defaultvalue="no_of_record"/></td>
    <td class="content1"><set:data name="totalNumber" />
      <input name="totalNumber" type="hidden" id="totalNumber" value="<set:data name='totalNumber'/>">
    </td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="groupinput"><set:label name="nor_record_below" defaultvalue="nor_record_below"/> </td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td class="listheader1"><set:label name="line_in_file" defaultvalue="line_in_file"/></td>
    <td class="listheader1"><set:label name="ref_no" defaultvalue="ref_no"/></td>
    <td align="right" class="listheader1"><set:label name="amt" defaultvalue="amt"/></td>
    <td class="listheader1"><set:label name="BENEFICIARY_NAME" defaultvalue="BENEFICIARY NAME"/></td>
	<td class="listheader1"><set:label name="Beneficiary_bank_address1" defaultvalue="Beneficiary bank address1"/></td>
	<td class="listheader1"><set:label name="Charge_Account1" defaultvalue="Charge Account"/></td>
    <!-- Add by heyongjiang 20100830 -->
   <td class="listheader1"><set:label name="Purpose" defaultvalue="Purpose"/></td>
    <!-- Add by heyongjiang end -->
  </tr>
 <set:list name="recList"  showNoRecord="YES">
  <tr class="<set:listclass class1='' class2='greyline'/>">
    <td class="listcontent1"><set:listdata name="lineNo" /></td>
	 <td class="listcontent1"><set:listdata name="transId" /></td>
    <td align="right" class="listcontent1"><set:listdata name="draftAmount" format="amount"/></td>
    <td class="listcontent1"><set:listdata name="beneficiaryName"  escapechar="YES"/></td>
	<td class="listcontent1"><set:listdata name="bankAddress1"  escapechar="YES"/></td>
	<td class="listcontent1"><set:listdata name="chargeAccount" /></td>
	<!-- Add by heyongjiang 20100830 -->
    <td class="listcontent1"><set:listdata name="purpose" /></td>
	<!-- Add by heyongjiang end -->
  </tr>
  </set:list>
  <tr>
    <td class="" colspan="3">&nbsp;</td>
  </tr>
</table>
</set:loadrb> <!-- InstanceEndEditable --><!-- InstanceEnd -->
