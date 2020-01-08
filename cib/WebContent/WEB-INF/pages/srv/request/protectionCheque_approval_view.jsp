<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.srv.protection_check_request">
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="account|chequeNumber|chequeStyle|amount|beneficiaryName|issueDate">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
   <tr >
                <td class="label1"><set:label name="Account" defaultvalue="Account"/></td>
				 <td class="content1"><set:data name='account' /><input id="account" type="hidden" name="account" value="<set:data name='account'/>"></td>
              </tr>
			   <tr class="greyline">
                <td class="label1"><set:label name="Account_Dsecription" defaultvalue="Account Dsecription"/></td>
				<td class="content1"><set:data name='accountDescription' /></td>
              </tr>
			  <tr   >
			   <td class="label1"><set:label name="Cheque_Number" defaultvalue="Cheque Number"/></td>
               <td class="content1"><set:data name='chequeNumber' /><input id="chequeNumber" type="hidden" name="chequeNumber" value="<set:data name='chequeNumber'/>"></td>
			 </tr>
			  <tr class="greyline">
                <td class="label1"><set:label name="Cheque_Style" defaultvalue="Cheque Style"/></td>
				 <td class="content1"><set:data name='chequeStyle' /><input id="chequeStyle" type="hidden" name="chequeStyle" value="<set:data name='chequeStyle'/>"></td>
              </tr>
		     <tr >
                <td class="label1"><set:label name="Amount" defaultvalue="Amount"/></td>
				<td class="content1"><set:data name='amount' format="amount" /><input id="amount" type="hidden" name="amount" value="<set:data name='amount'/>"></td>
              </tr>
            <tr class="greyline">
                <td class="label1"><set:label name="Beneficiary_Name" defaultvalue="Beneficiary Name"/></td>
                <td class="content1" colspan="3"><set:data name='beneficiaryName'/><input id="beneficiaryName" type="hidden" name="beneficiaryName" value="<set:data name='beneficiaryName'/>"></td>
              </tr>
            <tr >
                <td class="label1"><set:label name="Issue_date" defaultvalue="Issue date"/></td>
                <td class="content1" colspan="3"><set:data name='issueDate' format="date"/><input id="issueDate" type="hidden" name="issueDate" value="<set:data name='issueDate'/>"></td>
              </tr> 
</table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
