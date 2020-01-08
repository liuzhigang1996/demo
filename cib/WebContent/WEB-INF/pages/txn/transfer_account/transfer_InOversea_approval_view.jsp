<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.txn.transfer_oversea">
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="fromCurrency|debitAmount|newFromAmount|fromAccount|toAccount|beneficiaryName1|beneficiaryName2|beneficiaryCountry|beneficiaryCity|beneficiaryBank1|beneficiaryBank2|beneficiaryBank3|beneficiaryBank4|swiftAddress|spbankCode|correspondentBankLine1|correspondentBankLine2|correspondentBankLine3|correspondentBankLine4|correspondentBankAccount|toCurrency|transferAmount|newToAmount|exchangeRate|newExchangeRate|transferDate|messsage|messsage2|chargeAccount|chargeCurrency|chareBy|remark">
<!-- add by lw 20110101 -->
<set:if name="needProof" condition="EQUALS" value="Y">
<table>
  <tr >
    <td class="label1" colspan="2"><font color="#FF0000"><set:label name="Need_Proof_Message" defaultvalue="* Need to provide Proof Document."/></font></td>
  </tr>
</table>
</set:if>
<!-- add by lw end -->
<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr class="greyline">
                <td colspan="2"  class="label1"><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
              </tr>
			  <tr >
			    <td class="label1" width="28%"><set:label name="Debit_Amount" defaultvalue="Debit Amount"/></td>
               <td class="content1"><set:data name='fromCurrency' db="rcCurrencyCBS"/><set:data name='debitAmount' format="amount"/><font color="#FF0000">&nbsp;&nbsp;(<set:data name='newFromAmount' format="amount"/>)</font>
			   <input id="fromCurrency" type="hidden" name="fromCurrency" value="<set:data name='fromCurrency'/>">
			   <input id="debitAmount" type="hidden" name="debitAmount" value="<set:data name='debitAmount'/>">
			   <input id="newFromAmount" type="hidden" name="newFromAmount" value="<set:data name='newFromAmount'/>">
			    </td>
              </tr>
			  <tr class="greyline">
			    <td class="label1"><set:label name="Transfer_Account" defaultvalue="Transfer Account"/></td>
               <td class="content1"><set:data name='fromAccount'/><input id="fromAccount" type="hidden" name="fromAccount" value="<set:data name='fromAccount'/>">
			    </td>
              </tr>
			  <tr >
				<td class="label1"  colspan="3"><b><set:label name="Transfer_To" defaultvalue="Transfer to"/></b></td>
              </tr>
			   <tr class="greyline">
			    <td class="label1"><set:label name="Beneficiary_Account_Name" defaultvalue="Beneficiary Account Name"/></td>
               <td class="content1"><set:data name='beneficiaryName1'/><input id="beneficiaryName1" type="hidden" name="beneficiaryName1" value="<set:data name='beneficiaryName1'/>"><br><set:data name='beneficiaryName2'/><input id="beneficiaryName2" type="hidden" name="beneficiaryName2" value="<set:data name='beneficiaryName2'/>">
			    </td>
              </tr>
              <tr>
			    <td class="label1"><set:label name="Beneficiary_Account" defaultvalue="Account Number"/></td>
               <td class="content1"><set:data name='toAccount'/><input id="toAccount" type="hidden" name="toAccount" value="<set:data name='toAccount'/>">
			    </td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Transfer_Amount" defaultvalue="Transfer Amount"/></td>
                <td class="content1" colspan="2" ><set:data name='toCurrency' db="rcCurrencyCBS"/><set:data name='transferAmount' format="amount"/><font color="#FF0000">&nbsp;&nbsp;(<set:data name='newToAmount' format="amount"/>)</font>					
				<input id="toCurrency" type="hidden" name="toCurrency" value="<set:data name='toCurrency'/>">
				<input id="transferAmount" type="hidden" name="transferAmount" value="<set:data name='transferAmount'/>">
				<input id="newToAmount" type="hidden" name="newToAmount" value="<set:data name='newToAmount'/>">
				</td>
              </tr>
			  <set:if name="fromCurrency" condition="NOTEQUALS" field="toCurrency">
			   <tr >
			    <td class="label1"><set:label name="Exchange_Rate" defaultvalue="Exchange Rate"/></td>
               <td class="content1"><set:data name='exchangeRate' format="rate"/><font color="#FF0000">&nbsp;&nbsp;(<set:data name='newExchangeRate' format="rate"/>)</font>&nbsp;&nbsp;<span style = "color:red;"><set:label name="Exchange_Rate_Tip" defaultvalue="The Rate is for reference only, the dealing rate will be quoted upon trading."/></span>
			    <input id="newExchangeRate" type="hidden" name="newExchangeRate" value="<set:data name='newExchangeRate'/>"></td>
              </tr>
			  </set:if>
			  <!--------------added by xyf 20090721 begin--------------->
			  <tr class="greyline">
			    <td class="label1"><set:label name="Beneficiary_Address" defaultvalue="Beneficiary Address"/></td>
                <td class="content1"><set:data name='beneficiaryName3'/><input id="beneficiaryName3" type="hidden" name="beneficiaryName3" value="<set:data name='beneficiaryName3'/>"><br><set:data name='beneficiaryName4'/><input id="beneficiaryName4" type="hidden" name="beneficiaryName4" value="<set:data name='beneficiaryName4'/>">
			    </td>
              </tr>
			  <!---------------added by xyf 20090721 end--------------->
			   <tr>
			    <td class="label1"><set:label name="Country_of_Beneficiary_Bank" defaultvalue="Country of Beneficiary Bank"/></td>
               <td class="content1"><set:data name='beneficiaryCountry' db="country"/><input id="beneficiaryCountry" type="hidden" name="beneficiaryCountry" value="<set:data name='beneficiaryCountry'/>">
			    </td>
              </tr>
			  <tr  class="greyline">
			    <td class="label1"><set:label name="Beneficiary_City" defaultvalue="City of Beneficiary Bank"/></td>
               <td class="content1"><set:if name='cityFlag' condition='equals' value='N'><set:data name='beneficiaryCity' db="city"/></set:if>
			   <set:if name='cityFlag' condition='equals' value='Y'><set:data name='beneficiaryCity' /></set:if><input id="beneficiaryCity" type="hidden" name="beneficiaryCity" value="<set:data name='beneficiaryCity'/>">
			    </td>
              </tr>
			  <tr>
			    <td class="label1"><set:label name="Beneficiary_Bank" defaultvalue="Name of Beneficiary Bank"/></td>
               <td class="content1"><set:if name='bankFlag' condition='equals' value='N'><set:data name='beneficiaryBank1' db="overseaBank"/></set:if>
			   <set:if name='bankFlag' condition='equals' value='Y'><set:data name='beneficiaryBank1'/></set:if><input id="beneficiaryBank1" type="hidden" name="beneficiaryBank1" value="<set:data name='beneficiaryBank1'/>">
			    </td>
              </tr>
			   <tr  style = "display:none;">
			    <td class="label1"><set:label name="Beneficiary_Bank_Address" defaultvalue="Address of Beneficiary Bank"/></td>
               <td class="content1"><set:data name='beneficiaryBank2'/>
			   <br><set:data name='beneficiaryBank3'/>
				  <br><set:data name='beneficiaryBank4'/><input id="beneficiaryBank2" type="hidden" name="beneficiaryBank2" value="<set:data name='beneficiaryBank2'/>"><input id="beneficiaryBank3" type="hidden" name="beneficiaryBank3" value="<set:data name='beneficiaryBank3'/>"><input id="beneficiaryBank4" type="hidden" name="beneficiaryBank4" value="<set:data name='beneficiaryBank4'/>">
			    </td>
              </tr>
			  <tr class="greyline">
			    <td class="label1"><set:label name="SWIFT_Address" defaultvalue="Address of SWIFT"/></td>
               <td class="content1"><set:data name='swiftAddress'/><input id="swiftAddress" type="hidden" name="swiftAddress" value="<set:data name='swiftAddress'/>"></td>
              </tr>
		  
			 <!-- add by lzg for Correspondent Bank -->
			  <tr >
			     <td class="label1"><b><set:label name="Correspondent_Bank" defaultvalue="Correspondent Bank"/></b></td>
                 <td class="content1">&nbsp;</td>
              </tr>
              <tr class="greyline">
			    <td class="label1"><set:label name="Correspondent_Bank_Name" /></td>
               <td class="content1"><set:data name='correspondentBankName'/></td>
              </tr>
              <tr >
			    <td class="label1"><set:label name="Correspondent_Bank_Code" /></td>
               <td class="content1"><set:data name='correspondentBankCode'/></td>
              </tr>
			  	<!-- add by lzg end -->
			  	
			 <tr class="greyline">
			    <td class="label1"><set:label name="Transfer_Date" defaultvalue="Transfer Date"/></td>
               <td class="content1"><set:data name='transferDate'  format="date"/><input id="transferDate" type="hidden" name="transferDate" value="<set:data name='transferDate'/>">
			    </td>
              </tr>
			  <tr >
			    <td class="label1"><set:label name="Message_Send" defaultvalue="Message to be sent"/></td>
               <td class="content1"><set:data name='messsage'/>
			   <br><set:data name='messsage2'/>
			   <br><set:data name='messsage3'/>
			   <input id="messsage" type="hidden" name="messsage" value="<set:data name='messsage'/>">
			   <input id="messsage2" type="hidden" name="messsage2" value="<set:data name='messsage2'/>">
			   <input id="messsage3" type="hidden" name="messsage3" value="<set:data name='messsage3'/>">
			    </td>
              </tr>
              <!-- modified by lzg add set if 20190601 -->
              <set:if name="chareBy" condition="NOTEQUALS" value="S">
			  <tr class="greyline">
			  <td class="label1"><set:label name="Deduct_Charge_from_Account" defaultvalue="Deduct Charge from  Account "/></td>
              <td class="content1" colspan="2" ><set:data name='chargeAccount'/>
				</td>
              </tr>
              </set:if>
              <!-- add by lzg end -->
              <input id="chargeAccount" type="hidden" name="chargeAccount" value="<set:data name='chargeAccount'/>">
			   <tr >
			    <td class="label1"><set:label name="Charge_Amount" defaultvalue="Charge Amount"/></td>
               <td class="content1"><set:data name='toCurrency' db="rcCurrencyCBS"/> <set:data name='chargeAmount' format="amount"/><input id="chargeCurrency" type="hidden" name="chargeCurrency" value="<set:data name='toCurrency'/>"><input id="chargeAmount" type="hidden" name="chargeAmount" value="<set:data name='chargeAmount'/>">
			    </td>
              </tr>
			   <tr class="greyline">
			    <td class="label1"><set:label name="Overseas_charges_to_be_paid_by" defaultvalue="Overseas charges to be paid by"/></td>
               <td class="content1"><set:data name='chareBy' rb="app.cib.resource.txn.charge_name"/><input id="chareBy" type="hidden" name="chareBy" value="<set:data name='chareBy'/>">
			    </td>
              </tr>
              <!-- modified by lzg 20190602 -->
			   <!--<set:if name="showPurpose" condition="EQUALS" value="true">
			  <tr>
               	<td class="label1"><set:label name="Purpose" defaultvalue="Purpose"/></td>
               <td class="content1"><set:data name='purpose'/></td>
             </tr >	
			 </set:if> 
			 -->
			 <tr>
               	<td class="label1"><set:label name="Purpose" defaultvalue="Purpose"/></td>
               <td class="content1"><set:data name='purposeCode' rb = "app.cib.resource.txn.purposecode"/></td>
             </tr >
			 <!-- modified by lzg end -->			  
              <tr class="greyline">
                <td class="label1"><set:label name="Remark" defaultvalue="Remark"/></td>
                <td class="content1"><set:data name='remark'/> <input id="remark" type="hidden" name="remark" value="<set:data name='remark'/>">
              </tr>
              <!-- add by lzg 20190613 -->
             <set:if name="serialNumber" condition="NOTEQUALS" value="">
             <tr>
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
