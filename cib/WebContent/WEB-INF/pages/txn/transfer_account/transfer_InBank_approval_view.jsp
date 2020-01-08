<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><title>fromAccount,fromCurrency,toAccount</title><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.txn.transfer_bank">
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="fromAccount|fromCurrency|debitAmount|newFromAmount|toAccount|toCurrency|transferAmount|newToAmount|exchangeRate|newExchangeRate|remark">
<table width="100%" border="0" cellspacing="0" cellpadding="3">
 <tr>
                 <td class="label1" colspan="3"><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
              </tr>
              <tr class="greyline">
                <td class="label1" width="28%"><set:label name="From_Account" defaultvalue="Account Number"/></td>
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
                <td class="label1"><set:label name="To_Account" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="3" ><set:data name='toAccount'/>
				  <input id="toAccount" type="hidden" name="toAccount" value="<set:data name='toAccount'/>"></td>
              </tr >
              
              <!-- add by linrui 20181106 -->
              <tr class="greyline">
                <td class="label1"><set:label name="To_Name" defaultvalue="Beneficiary Name"/></td>
                <td class="content1" colspan="3" ><set:data name='toName' format='name'/>
				</td>
              </tr>
              <!-- end -->
              
			   <tr >
                <td class="label1"><set:label name="Transfer_Amount" defaultvalue="Transfer Amount"/></td>
              <td class="content1" colspan="2"><set:data name='toCurrency' db="rcCurrencyCBS"/><set:data name='transferAmount' format="amount"/><font color="#FF0000">&nbsp;&nbsp;(<set:data name='newToAmount' format="amount"/>)</font>
                <input id="toCurrency" type="hidden" name="toCurrency" value="<set:data name='toCurrency'/>">
				<input id="transferAmount" type="hidden" name="transferAmount" value="<set:data name='transferAmount'/>"></td>
				<input id="newToAmount" type="hidden" name="newToAmount" value="<set:data name='newToAmount'/>"></td>
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
              <tr  class="greyline">
                <td class="label1" ><set:label name="Remark" defaultvalue="Remark"/></td>
                <td class="content1" colspan="3" ><set:data name='remark'/><input id="remark" type="hidden" name="remark" value="<set:data name='remark'/>">
              </tr>
              <!-- ADD BY LINRUI 20190911 -->
              <tr class="label1">
                <td class="label1" ><set:label name="Remark_Host" defaultvalue="Remark"/></td>
                <td class="content1" colspan="3" ><set:data name='remarkHost'/><input id="remarkHost" type="hidden" name="remarkHost" value="<set:data name='remarkHost'/>">
              </tr>
              <!-- end -->
              <!-- add by lzg 20190613 -->
             <set:if name="serialNumber" condition="NOTEQUALS" value="">
             <tr class="greyline">
				<td class="label1"><set:label name="Serial_Number" defaultvalue="Serial Number"/></td>
               <td class="content1"><set:data name='serialNumber'/></td>
             </tr >
             </set:if>
             <!-- add by lzg end -->
              <tr>
                <td colspan="2" class="label1" ><font color="#FF0000"><b><set:label name="Foot_Note" defaultvalue="* The value in red is the latest rate / amount at this moment."/></b></font></td>
              </tr>
              <!-- add by lzg 20190624 for pwc-->
					<set:singlesubmit/>
				  <!-- add by lzg end -->			  
</table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
