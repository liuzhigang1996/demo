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
<table width="647" border="4" cellspacing="0" cellpadding="0">
<tr>
<td width="637"><table width="100%" border="0" cellspacing="0" cellpadding="3">
<tr>
<td colspan="4" class="STYLE1">Business Online Banking Transfer Acknowledgement</td>
</tr>
<tr>
<td class="STYLE3">Request Type</td>
<td class="STYLE2" colspan="3">BATCH TRANSFER - ACCOUNTS IN BANK - Multi to Single</td>
</tr>
<tr>
<td class="STYLE3">Request Date</td>
<td class="STYLE2" colspan="3"><%lastUpdateTime||format@date%>&nbsp;<%lastUpdateTime||format@time%></td>
</tr>
<tr>
<td class="STYLE2"><strong>BANK Reference No.</strong></td>
<td class="STYLE2" colspan="3"><%batchId%></td>
</tr>
<tr>
<td class="STYLE2"><strong>Transfer From</strong></td>
<td class="STYLE2" colspan="3">&nbsp;</td>
</tr>
<tr>
<td class="STYLE2">Total Debit Amount</td>
<td class="STYLE2" colspan="3"><%fromCurrency%>&nbsp;<%fromAmount||format@amount%></td>
</tr>
<tr>
<td class="STYLE2">Number of Records</td>
<td class="STYLE2" colspan="3"><%totalNumber%></td>
</tr>
<tr>
<td class="STYLE2">Account No</td>
<td class="STYLE2">Amount</td>
<td class="STYLE2" colspan="2">Payment Instruction</td>
</tr>
<%beneList||list@fromAccount-string,debitAmount-amount,remark-string%>
<tr>
<td class="STYLE2"><strong>Transfer To</strong></td>
<td class="STYLE2" colspan="3">&nbsp;</td>
</tr>
<tr>
<td class="STYLE2">Account Name</td>
<td class="STYLE2" colspan="3"><%toAccountName%></td>
</tr>
<tr>
<td class="STYLE2">Account Number</td>
<td class="STYLE2" colspan="3"><%toAccount%></td>
</tr>
<tr>
<td class="STYLE2">Total Transfer Amount</td>
<td class="STYLE2" colspan="3"><%toCurrency%>&nbsp;<%toAmount||format@amount%></td>
</tr>
</table></td>
</tr>
</table>
