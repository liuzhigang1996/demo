<title>Business Online Banking Transfer Acknowledgement</title>
<style type="text/css">
<!--
.STYLE1 {	font-family: Arial, Helvetica, sans-serif;
	font-weight: bold;
	BACKGROUND-COLOR: #999999;
	color: #FFFFFF;
	FONT-SIZE: 10pt;
}
.STYLE2 {	font-family: Arial, Helvetica, sans-serif;
	FONT-SIZE: 10pt;
}
.STYLE3 {font-family: Arial, Helvetica, sans-serif; FONT-SIZE: 10pt; font-weight: bold; }
-->
</style>
<table width="500" border="4" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="3">
<tr>
<td colspan="2" class="STYLE1">Business Online Banking Transfer Acknowledgement</td>
</tr>
<tr>
<td class="STYLE3">Request Type</td>
<td class="STYLE2">ACCOUNTS IN OTHER BANKS OVERSEAS</td>
</tr>
<tr>
<td class="STYLE3">Request Date</td>
<td class="STYLE2"><%lastUpdateTime||format@date%>&nbsp;<%lastUpdateTime||format@time%></td>
</tr>
<tr>
<td class="STYLE2"><b>BANK Reference No.</b></td>
<td class="STYLE2"><%transId%></td>
</tr>
<tr>
<td class="STYLE2"><b>Transfer From</b></td>
<td class="STYLE2">&nbsp;</td>
</tr>
<tr>
<td class="STYLE2">Amount to be debited</td>
<td class="STYLE2"><%fromCurrency%>&nbsp;<%debitAmount||format@amount%></td>
</tr>
<tr>
<td class="STYLE2"><b>Transfer To</b></td>
<td class="STYLE2">&nbsp;</td>
</tr>
<tr>
<td class="STYLE2">Beneficiary Name</td>
<td class="STYLE2"><%beneficiaryName1%></td>
</tr>
<tr>
<td class="STYLE2">Account Number</td>
<td class="STYLE2"><%toAccount%></td>
</tr>
<tr>
<td class="STYLE2">Beneficiary Bank</td>
<td class="STYLE2"><%beneficiaryBank1||db@overseaBank%></td>
</tr>
<tr>
<td class="STYLE2">Account Number</td>
<td class="STYLE2"><%toAccount%></td>
</tr>
<tr>
<td class="STYLE2">Country of Beneficiary Bank</td>
<td class="STYLE2"><%beneficiaryCountry||db@country%></td>
</tr>
<tr>
<td class="STYLE2">City of Beneficiary Bank</td>
<td class="STYLE2"><%beneficiaryCity||db@city%></td>
</tr>
<tr>
<td class="STYLE2">Branch Address</td>
<td class="STYLE2"><%beneficiaryBank2%><br><%beneficiaryBank3%></td>
</tr>
<tr>
<td class="STYLE2">SWIFT Address</td>
<td class="STYLE2"><%swiftAddress%></td>
</tr>
<tr>
<td class="STYLE2">FW/ABA</td>
<td class="STYLE2"><%spbankCode%></td>
</tr>
<tr>
<td class="STYLE2">Correspondent Bank</td>
<td class="STYLE2"><%correspondentBankLine1%></td>
</tr>
<tr>
<td class="STYLE2">Correspondent Bank Account</td>
<td class="STYLE2"><%correspondentBankAccount%></td>
</tr>
<tr>
<td class="STYLE2"><b>Transfer Amount</b></td>
<td class="STYLE2"><%toCurrency%>&nbsp;<%transferAmount||format@amount%></td>
</tr>
<tr>
<td class="STYLE2"><b>Exchange Rate</b></td>
<td class="STYLE2"><%exchangeRate%></td>
</tr>
<tr>
<td class="STYLE2"><b>Transfer Date</b></td>
<td class="STYLE2"><%requestTime||format@datetime%></td>
</tr>
<tr>
<td class="STYLE2"><b>Deduct Charges from Account</b></td>
<td class="STYLE2"><%chargeAccount%></td>
</tr>
<tr>
<td class="STYLE2"><b>Charges Amount</b></td>
<td class="STYLE2"><%chargeCurrency%>&nbsp;<%chargeAmount||format@amount%></td>
</tr>
<tr>
<td class="STYLE2"><b>Message to be sent</b></td>
<td class="STYLE2"><%messsage%><br><%messsage2%></td>
</tr>
<tr>
<td class="STYLE2"><b>Overseas bank charges</b></td>
<td class="STYLE2"><%chareBy||rb@app.cib.resource.txn.charge_name%></td>
</tr>
</table></td>
</tr>
</table>
