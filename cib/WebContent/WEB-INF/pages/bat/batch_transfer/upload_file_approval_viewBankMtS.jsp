<!-- InstanceBegin template="/Templates/detail.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<!-- InstanceBeginEditable name="DetailInfoArea" -->
<set:loadrb file="app.cib.resource.bat.batch_transfer_bank">
<input id="fieldsToBeSigned" type="hidden" name="fieldsToBeSigned" value="toAccount|fromCurrency|newToAmount|toCurrency|toAmount|newExchangeRate|totalNumber|">
<table width="100%" border="0" cellspacing="0" cellpadding="3" id="round">
  <tr>
                <td colspan="4"  class="label1"><b><set:label name="Transfer_To" defaultvalue="Transfer To"/></b></td>
    </tr>
              <tr class="greyline">
                <td  width="28%" class="label1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                <td  width="72%" class="content1" colspan="2" ><set:data name='toAccount'/>
				<input name="toAccount" type="hidden" id="toAccount" value="<set:data name='toAccount'/>">
				</td>
              </tr>
			    <tr >
			    <td class="label1"><set:label name="TO_ACCOUNT_NAME" defaultvalue="To Account Name"/></td>
               <td class="content1"><set:data name='toAccountName'/><br><set:data name='toAccountName2'/>
			   <input name="toAccountName" type="hidden" id="toAccountName" value="<set:data name='toAccountName'/>"> 
			   <input name="toAccountName2" type="hidden" id="toAccountName2" value="<set:data name='toAccountName2'/>">
			    </td>
              </tr>
			  <tr class="greyline">
			    <td class="label1"><set:label name="Transfer_Amount" defaultvalue="Transfer Amount"/></td>
               <td class="content1"><set:data name='fromCurrency' db='rcCurrencyCBS'/>&nbsp;<set:data name='newToAmount' format="amount"/>
			   <input name="fromCurrency" type="hidden" id="fromCurrency" value="<set:data name='fromCurrency'/>">
			   <input name="newToAmount" type="hidden" id="newToAmount" value="<set:data name='newToAmount'/>">
			    </td>
              </tr>
  <tr>
    <td width="28%" class="label1"><set:label name="total_amt" defaultvalue="total_amt" rb="app.cib.resource.bat.batch_transfer_bank"/></td>
    <td width="72%" class="content1"><set:data name="toCurrency" db="rcCurrencyCBS"/>&nbsp;<set:data name="toAmount" format="amount"/>
	<input name="toCurrency" type="hidden" id="toCurrency" value="<set:data name='toCurrency'/>">
	<input name="toAmount" type="hidden" id="toAmount" value="<set:data name='toAmount'/>"></td>
  </tr>
  <tr class="greyline">
     <td class="label1"><set:label name="Exchange_Rate" defaultvalue="Exchange Rate"/></td>  
     <td class="content1" colspan="2"><set:data name='newExchangeRate' format="rate"/>&nbsp;&nbsp;<span style = "color:red;"><set:label name="Exchange_Rate_Tip" defaultvalue="The Rate is for reference only, the dealing rate will be quoted upon trading."/></span>
     <input id="newExchangeRate" type="hidden" name="newExchangeRate" value="<set:data name='newExchangeRate'/>">
     </td>
   </tr>
  <tr>
    <td class="label1"><set:label name="no_of_record" defaultvalue="no_of_record" rb="app.cib.resource.bat.batch_transfer_bank"/></td>
    <td class="content1"><set:data name="totalNumber" /><input name="totalNumber" type="hidden" id="totalNumber" value="<set:data name='totalNumber'/>"></td>
  </tr>
   <tr class="greyline">
                <td colspan="4"  class="label1"><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
    </tr>
</table>
 <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                   <td class="listheader1"><set:label name="line_in_file" defaultvalue="line_in_file"/></td>
                  <td class="listheader1"><set:label name="FROM_ACCOUNT" defaultvalue="FROM ACCOUNT"/></td>
                  <td align="right" class="listheader1"><set:label name="TRANSFER_AMOUNT" defaultvalue="TRANSFER AMOUNT"/></td>
				  <td class="listheader1"><set:label name="REMARK" defaultvalue="REMARK"/></td>
                </tr>
                <set:list name="recList"  showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                 <td class="listcontent1"><set:listdata name="lineNo" /></td>
                  <td class="listcontent1"><set:listdata name="fromAccount" /></td>
                  <td align="right" class="listcontent1"><set:listdata name="debitAmount" format="amount"/></td>
                  <td class="listcontent1"><set:listdata name="remark" escapechar="YES"/></td>
                </tr>
                </set:list>
              <tr>
                <td colspan="5" class="label1" ><font color="#FF0000"><b><set:label name="Foot_Note" defaultvalue="* The value in red is the latest rate / amount at this moment."/></b></font></td>
              </tr>			  
			 </table>
</set:loadrb>
<!-- InstanceEndEditable --><!-- InstanceEnd -->
