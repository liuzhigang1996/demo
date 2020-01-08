<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.srv.stop_check_request">
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="chequeNumber|currentAccount|amount|beneficiaryName|issueDate|expiryDate|stopReason">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
    <tr >
			   <td class="label1"><set:label name="Cheque_Number" defaultvalue="Cheque Number"/></td>
               <td class="content1"><set:data name='chequeNumber' /><input id="chequeNumber" type="hidden" name="chequeNumber" value="<set:data name='chequeNumber'/>"></td>
			 </tr>
			  <tr class="greyline" >
                <td class="label1"><set:label name="Account" defaultvalue="Account"/></td>
				 <td class="content1"><set:data name='currentAccount' /><input id="currentAccount" type="hidden" name="currentAccount" value="<set:data name='currentAccount'/>"></td>
              </tr>
              <tr >
                <td class="label1"><set:label name="Account_Dsecription" defaultvalue="Account Dsecription"/></td>
				<td class="content1"><set:data name='currentAccountCcy' /></td>
              </tr>
		     <tr class="greyline" >
                <td class="label1"><set:label name="Amount" defaultvalue="Amount"/></td>
				<td class="content1"><set:data name='amount' format="amount" /><input id="amount" type="hidden" name="amount" value="<set:data name='amount'/>"></td>
              </tr>
            <tr >
                <td class="label1"><set:label name="Beneficiary_Name" defaultvalue="Beneficiary Name"/></td>
                <td class="content1" colspan="3"><set:data name='beneficiaryName'/><input id="beneficiaryName" type="hidden" name="beneficiaryName" value="<set:data name='beneficiaryName'/>"></td>
              </tr>
            <tr class="greyline">
                <td class="label1"><set:label name="Issue_Date" defaultvalue="Issue date"/></td>
                <td class="content1" colspan="3"><set:data name='issueDate' format="date"/><input id="issueDate" type="hidden" name="issueDate" value="<set:data name='issueDate'/>"></td>
              </tr> 
			  <tr >
                <td class="label1"><set:label name="Expiry_Date" defaultvalue="Expiry date"/></td>
                <td class="content1" colspan="3"><set:data name='expiryDate' format="date"/><input id="expiryDate" type="hidden" name="expiryDate" value="<set:data name='expiryDate'/>"></td>
            </tr> 
			  <tr class="greyline">
              <td class="label1"  rowspan="2"><set:label name="Reason_for_stop" defaultvalue="Reason for stop"/></td>
              <td class="label1" colspan="2"><set:if name='reasonFlag' condition='equals' value='Y'><set:data name='stopReason' rb="app.cib.resource.srv.stop_reason"/></set:if><set:if name='reasonFlag' condition='equals' value='N'><set:data name='stopReason'/></set:if><input id="stopReason" type="hidden" name="stopReason" value="<set:data name='stopReason'/>"></td>
            </tr> 
            <!-- add by lzg 20190624 for pwc-->
				<set:singlesubmit/>
			  <!-- add by lzg end -->
</table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
