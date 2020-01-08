<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %> 
<html>
<head>
<title>Exiting...BANK Corporation i-Banking System</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="refresh" content="1000;url=JavaScript:closeWindow()">
<script language="JavaScript">
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function closeWindow(form){
		setTimeout("window.close()",0);
}
</script>
</head>
<body onLoad="closeWindow()" >
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr> 
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td height="200" valign="top"> 
        <p align="center">Exit...BANK Corporation i-Banking System</p>
	</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
  </tr>
</table>
</body>
</html>
