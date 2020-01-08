<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><title>fromAccount,fromCurrency,toAccount</title><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.txn.corp_repatriate">
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="fromAccount|fromCurrency|debitAmount|newFromAmount|toAccount|toCurrency|transferAmount|newToAmount|exchangeRate|newExchangeRate|remark">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr> 
                <td class="groupseperator">&nbsp;</td>
              </tr>
  </table>
<table width="100%" border="0" cellspacing="0" cellpadding="3">
 <tr>
                 <td class="label1"><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="2" ><set:data name='fromAccount'/>
				  <input id="fromAccount" type="hidden" name="fromAccount" value="<set:data name='fromAccount'/>"></td>
              </tr>
			  <tr >
			    <td class="label1"><set:label name="Amount_to_be_debited" defaultvalue="Amount to be debited"/></td>
               <td class="content1"><set:data name='fromCurrency' db="rcCurrencyCBS"/><set:data name='debitAmount' format="amount"/><font color="#FF0000">&nbsp;&nbsp;(<set:data name='newFromAmount' format="amount"/>)</font>
			     <input id="fromCurrency" type="hidden" name="fromCurrency" value="<set:data name='fromCurrency'/>">
			     <input id="debitAmount" type="hidden" name="debitAmount" value="<set:data name='debitAmount'/>">
			     <input id="newFromAmount" type="hidden" name="newFromAmount" value="<set:data name='newFromAmount'/>">
				 </td>
              </tr>
			  <tr class="greyline" >
				<td class="label1" colspan="4"><b><set:label name="Transfer_To" defaultvalue="Transfer to"/></b></td>
              </tr>
			  <tr >
                <td class="label1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="3" ><set:data name='toAccount'/>
				  <input id="toAccount" type="hidden" name="toAccount" value="<set:data name='toAccount'/>"></td>
              </tr >
			   <tr class="greyline">
                <td class="label1"><set:label name="Transfer_Amount" defaultvalue="Transfer Amount"/></td>
              <td class="content1" colspan="2"><set:data name='toCurrency' db="rcCurrencyCBS"/><set:data name='transferAmount' format="amount"/><font color="#FF0000">&nbsp;&nbsp;(<set:data name='newToAmount' format="amount"/>)</font>
                <input id="toCurrency" type="hidden" name="toCurrency" value="<set:data name='toCurrency'/>">
				<input id="transferAmount" type="hidden" name="transferAmount" value="<set:data name='transferAmount'/>">
				<input id="newToAmount" type="hidden" name="newToAmount" value="<set:data name='newToAmount'/>">
				</td>
              </tr>
			   <set:if name="fromCurrency" condition="NOTEQUALS" field="toCurrency">
			  <tr>
                <td class="label1"><set:label name="Exchange_Rate" defaultvalue=" Exchange Rate"/></td>
                <td class="content1" colspan="2"><set:data name='exchangeRate' format="rate"/><font color="#FF0000">&nbsp;&nbsp;(<set:data name='newExchangeRate' format="rate"/>)</font>&nbsp;&nbsp;<span style = "color:red;"><set:label name="Exchange_Rate_Tip" defaultvalue="The Rate is for reference only, the dealing rate will be quoted upon trading."/></span>
                  <input id="exchangeRate" type="hidden" name="exchangeRate" value="<set:data name='exchangeRate'/>">
                  <input id="newExchangeRate" type="hidden" name="newExchangeRate" value="<set:data name='newExchangeRate'/>">
                  </td>
                </tr>
			   </set:if>
              <tr class="greyline">
                <td class="label1" ><set:label name="Remark" defaultvalue="Remark"/></td>
                <td class="content1" colspan="3" ><set:data name='remark'/><input id="remark" type="hidden" name="remark" value="<set:data name='remark'/>">
              </tr>
              <tr>
                <td colspan="2" class="label1" ><font color="#FF0000"><b><set:label name="Foot_Note" defaultvalue="* The value in red is the latest rate / amount at this moment."/></b></font></td>
              </tr>			  
</table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
