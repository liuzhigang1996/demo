<title>User's Group Updated</title>
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
        <td colspan="2" class="STYLE1">User's Group Updated</td>
      </tr>
      <tr>
        <td class="STYLE2">Request Type</td>
        <td class="STYLE2">Update User's Group </td>
      </tr>
      <tr>
        <td class="STYLE2">Request Date</td>
        <td class="STYLE2"><%requestTime||format@date%>
/
  <%requestTime||format@time%></td>
      </tr>
      <tr>
        <td class="STYLE2">Group Name</td>
        <td class="STYLE2"><%groupId||db@groupByCorp%></td>
      </tr>
      <tr>
        <td class="STYLE2">Group Role</td>
        <td class="STYLE2"><%roleId||rb@app.cib.resource.common.corp_role%></td>
      </tr>
      <tr>
        <td class="STYLE2">Ordered by </td>
        <td class="STYLE2"><%corpName%></td>
      </tr>
    </table></td>
  </tr>
</table>
