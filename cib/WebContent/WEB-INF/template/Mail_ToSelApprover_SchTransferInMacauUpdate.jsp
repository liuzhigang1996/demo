<title>Approval Request </title>
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
        <td height="25" colspan="2" class="STYLE1">Approval Request</td>
      </tr>
      <tr>
        <td class="STYLE2">Requested by:</td>
        <td class="STYLE2"><%userID%>
-
  <%userName%></td>
      </tr>
      <tr>
        <td class="STYLE2"> Requested Date/Time:</td>
        <td class="STYLE2"><%requestTime||format@date%>
        -
          <%requestTime||format@time%></td>
      </tr>
      <tr>
        <td class="STYLE2">Transaction Type:</td>
        <td class="STYLE2">Update Scedule Transfer - Account in other Banks in Macau</td>
      </tr>
    </table></td>
  </tr>
</table>
