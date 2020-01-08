<title>User Blocked</title>
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
        <td colspan="2" class="STYLE1">User Blocked</td>
      </tr>
      <tr>
        <td class="STYLE2">Request Type</td>
        <td class="STYLE2">Block User</td>
      </tr>
      <tr>
        <td class="STYLE2">Request Date</td>
        <td class="STYLE2"><%lastUpdateTime||format@date%>
/
  <%lastUpdateTime||format@time%></td>
      </tr>
      <tr>
        <td class="STYLE2"> Corpration User</td>
        <td class="STYLE2"><%userId%>
-
  <%userName%></td>
      </tr>
      <tr>
        <td class="STYLE2">User Role</td>
        <td class="STYLE2"><%roleId||rb@app.cib.resource.common.corp_role%></td>
      </tr>
      <tr>
        <td class="STYLE2">Ordered by </td>
        <td class="STYLE2"><%corpName%></td>
      </tr>
    </table></td>
  </tr>
</table>
