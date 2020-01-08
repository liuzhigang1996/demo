<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.bat.batch_transfer_macau">
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="fromAccount|fromCurrency|newFromAmount|toCurrency|newExchangeRate|toAmount|totalNumber|">
<table width="100%" border="0" cellspacing="0" cellpadding="3" id="round">
  <tr>
     <td colspan="4"  class="label1"><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
  </tr>
   <tr class="greyline">
         <td  width="28%" class="label1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
          <td  width="72%" class="content1" colspan="2" ><set:data name='fromAccount'/><input name="fromAccount" type="hidden" id="fromAccount" value="<set:data name='fromAccount'/>"></td>
   </tr>
    <tr >
			    <td class="label1"><set:label name="Amount_to_be_debited" defaultvalue="Amount to be debited"/></td>
               <td class="content1"><set:data name='fromCurrency'/>&nbsp;<set:data name='newFromAmount' format="amount"/>
			   <input name="fromCurrency" type="hidden" id="fromCurrency" value="<set:data name='fromCurrency'/>">
			   <input name="newFromAmount" type="hidden" id="newFromAmount" value="<set:data name='newFromAmount'/>">
      </td>
    </tr>
  <tr class="greyline">
    <td width="28%" class="label1"><set:label name="total_amt" defaultvalue="total_amt" rb="app.cib.resource.bat.batch_transfer_macau"/></td>
    <td width="72%" class="content1"><set:data name="toCurrency"/>&nbsp;<set:data name="toAmount" format="amount"/><input name="toCurrency" type="hidden" id="toCurrency" value="<set:data name='toCurrency'/>"><input name="toAmount" type="hidden" id="toAmount" value="<set:data name='toAmount'/>"></td>
  </tr>  
  <tr>
     <td class="label1"><set:label name="Exchange_Rate" defaultvalue="Exchange Rate"/></td>  
     <td class="content1" colspan="2"><set:data name='newExchangeRate' format="rate"/>&nbsp;&nbsp;<span style = "color:red;"><set:label name="Exchange_Rate_Tip" defaultvalue="The Rate is for reference only, the dealing rate will be quoted upon trading."/></span>
     <input id="newExchangeRate" type="hidden" name="newExchangeRate" value="<set:data name='newExchangeRate'/>">
     </td>
   </tr>
  <tr class="greyline">
    <td class="label1"><set:label name="no_of_record" defaultvalue="no_of_record" rb="app.cib.resource.bat.batch_transfer_macau"/></td>
    <td class="content1"><set:data name="totalNumber" /><input name="totalNumber" type="hidden" id="totalNumber" value="<set:data name='totalNumber'/>"></td>
  </tr>
  <tr class="greyline">
                <td colspan="4"  class="label1"><b><set:label name="Transfer_To" defaultvalue="Transfer To"/></b></td>
  </tr>
  </table>
  <table width="100%" border="0" cellspacing="0" cellpadding="3">
			   <tr >
			    <td  class="listheader1"><set:label name="line_in_file" defaultvalue="line_in_file"/></td>
                <td  class="listheader1"><set:label name="TO_ACCOUNT" defaultvalue="TO ACCOUNT"/></td>
				<td align="right"  class="listheader1"><set:label name="TRANSFER_AMOUNT" defaultvalue="TRANSFER AMOUNT"/></td>
				<td  class="listheader1"><set:label name="BENEFICIARY_NAME" defaultvalue="BENEFICIARY NAME"/></td>
			    <td  class="listheader1"><set:label name="Bank_Name" defaultvalue="Bank_Name"/></td>
				<td  class="listheader1"><set:label name="Charge_Account" defaultvalue="Charge_Account"/></td>
			   </tr>
			  <set:list name="recList"  showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
			  <td class="listcontent1"><set:listdata name="lineNo" /></td>
                <td class="listcontent1"><set:listdata name="toAccount"/></td>
			    <td align="right" class="listcontent1"><set:listdata name="transferAmount" format="amount"/></td>
			    <td class="listcontent1"><set:listdata name="beneficiaryName" escapechar="YES"/><br><set:listdata name="beneficiaryName2" escapechar="YES"/></td>
				<td class="listcontent1"><set:listdata name="beneficiaryBank" escapechar="YES"/></td>
			    <td class="listcontent1"><set:listdata name="chargeAccount" /></td>
              </tr>
			  </set:list>
              <tr>
                <td colspan="5" class="label1" ><font color="#FF0000"><b><set:label name="Foot_Note" defaultvalue="* The value in red is the latest rate / amount at this moment."/></b></font></td>
              </tr>			  
			</table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
