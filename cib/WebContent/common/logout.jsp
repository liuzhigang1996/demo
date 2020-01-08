<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %> 
<html>
<set:loadrb file="app.cib.resource.sys.login" alias="">
<head>
<title><set:label name="documentTitle_Exit"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="refresh" content="1000;url=JavaScript:exit()">
<script language="JavaScript">
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function runLogout(form){
	document.form1.submit();
}
</script>
</head>
<body onLoad="runLogout()" >
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr> 
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td height="200" valign="top"> 
      <form name="form1" method="post" action="/cib/login.do?ActionMethod=logout&exitActionFlag=exitByCloseWindow">
        <set:label name="documentTitle_Exit"/> 
       	<input id="buttonClose" name="buttonClose" type="button" value="Close">
      </form>
	</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
  </tr>
</table>
</body>
</set:loadrb>
</html>
