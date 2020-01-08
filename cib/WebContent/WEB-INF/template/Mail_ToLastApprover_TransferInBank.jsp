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
<td class="STYLE2">ACCOUNTS IN BANK</td>
</tr>
<tr>
<td class="STYLE3">Request Date</td>
<td class="STYLE2"><%lastUpdateTime||format@date%>&nbsp;<%lastUpdateTime||format@time%></td>
</tr>
<tr>
<td class="STYLE2"><strong>BANK Reference No.</strong></td>
<td class="STYLE2"><%transId%></td>
</tr>
<tr>
<td class="STYLE2"><strong>Transfer From</strong></td>
<td class="STYLE2">&nbsp;</td>
</tr>
<tr>
<td class="STYLE2">Account  Number</td>
<td class="STYLE2"><%fromAccount%></td>
</tr>
<tr>
<td class="STYLE2">Amount  to be debited</td>
<td class="STYLE2"><%fromCurrency%>&nbsp;<%debitAmount||format@amount%></td>
</tr>
<tr>
<td class="STYLE2">Transfer  Amount</td>
<td class="STYLE2"><%toCurrency%>&nbsp;<%transferAmount||format@amount%></td>
</tr>
<tr>
<td class="STYLE2">Exchange  Rate</td>
<td class="STYLE2"><%exchangeRate%></td>
</tr>
<tr>
<td class="STYLE2"><strong>Transfer To </strong></td>
<td class="STYLE2">&nbsp;</td>
</tr>
<tr>
<td class="STYLE2">Account  Number</td>
<td class="STYLE2"><%toAccount%></td>
</tr>
<tr>
<td class="STYLE2"><strong>Transfer Date</strong></td>
<td class="STYLE2"><%requestTime||format@datetime%></td>
</tr>
</table></td>
</tr>
</table>
