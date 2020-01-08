<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" --><%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.srv.cash_order_request">
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="fromAccount|chargeAccount|toAmount|totalChargesamount">
<!-- add by lw 20101007 -->
<set:if name="needProof" condition="EQUALS" value="Y">
<table>
  <tr >
    <td class="label1" colspan="2"><font color="#FF0000"><set:label name="Need_Proof_Message" defaultvalue="* Need to provide Proof Document."/></font></td>
	</td>
  </tr>
</table>
</set:if>
<!-- add by lw end -->
<table width="100%" border="0" cellspacing="0" cellpadding="3">
   <tr >
			   <td class="label1"><set:label name="Debit_Account" defaultvalue="Account to be debited "/></td>
               <td class="content1"><set:data name='fromAccount' /><input id="fromAccount" type="hidden" name="fromAccount" value="<set:data name='fromAccount'/>"></td></td>
			 </tr>
			  <tr class="greyline" >
                <td class="label1"><set:label name="Charge_Account" defaultvalue="Account to be debited for Charges"/></td>
				 <td class="content1"><set:data name='chargeAccount' /><input id="chargeAccount" type="hidden" name="chargeAccount" value="<set:data name='chargeAccount'/>"></td></td>
              </tr>
</table>
 <table width="100%" border="0" cellspacing="0" cellpadding="3">
			  <tr >
                 <td width="12%" align="left" class="listheader1"><set:label name="Beneficiary_Name" defaultvalue="Beneficiary Name"/></td>
                 <td width="12%" align="right" class="listheader1"><set:label name="Amount_confirm" defaultvalue="Amount"/>(in <set:data name='currency' db="rcCurrencyCBS"/>)</td>
				  <td width="12%" align="right" class="listheader1"><set:label name="Charges" defaultvalue="Charges"/>(in <set:data name='chargeCurrency' db="rcCurrencyCBS"/>)</td>
				  <!-- add by lw 20100902 -->
				  <td width="12%" class="listheader1" align="right"><set:label name="Purpose" defaultvalue="Purpose"/></td>
				  <!-- add by lw end -->
              </tr>
			  <set:list name="inputList"  showNoRecord="YES">
			  <tr class="<set:listclass class1='' class2='greyline'/>">
			    <td align="left" class="listcontent1"><set:listdata name='beneficiaryName'/><br><set:listdata name='beneficiaryName2'/><br><set:listdata name='beneficiaryName3'/></td>
                <td align="right" class="listcontent1"><set:listdata name='cashierAmount' format="amount"/></td>
				<td align="right" class="listcontent1"><set:listdata name='chargeAmount' format="amount"/></td>
				<!-- Add by lw 20100902 -->
    			<td align="right" class="listcontent1">
					<div name="purpose" id="purpose">
					<set:listdata name='otherPurpose'/>
					</div>
				</td>
				<!-- Add by lw end -->
			  </tr>
			   </set:list>
			   <tr >
                 <td width="12%" align="center" class="label1"><b><set:label name="Total_Amount" defaultvalue="Total Amount"/></b></td>
                 <td width="12%" align="right" class="content1"><set:data name='toAmount' format="amount"/><input id="toAmount" type="hidden" name="toAmount" value="<set:data name='toAmount'/>"></td></td>
				 <td width="12%" align="right" class="content1"><set:data name='totalChargesamount' format="amount"/><input id="totalChargesamount" type="hidden" name="totalChargesamount" value="<set:data name='totalChargesamount'/>"></td></td>
			   </tr>
            </table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
