<title>Transaction Rejected </title>
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
-->
</style>
<table width="500" border="4" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="3">
      <tr>
        <td colspan="2" class="STYLE1">Transaction Rejected</td>
      </tr>
      <tr>
        <td class="STYLE2">Transaction Status</td>
        <td class="STYLE2">Rejected</td>
      </tr>
      <tr>
        <td class="STYLE2">Transaction Type</td>
        <td class="STYLE2"><%txnType%></td>
      </tr>
      <tr>
        <td class="STYLE2">Reference No.</td>
        <td class="STYLE2"><%transNo%></td>
      </tr>
      <tr>
        <td height="33" class="STYLE2">Approved by</td>
        <td class="STYLE2"><%userId%>
-
  <%userName%></td>
      </tr>
      <tr>
        <td class="STYLE2">Approved Date</td>
        <td class="STYLE2"><%requestTime||format@date%>
/
  <%requestTime||format@time%></td>
      </tr>
    </table></td>
  </tr>
</table>
