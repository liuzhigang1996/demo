<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<set:loadrb file="app.cib.resource.sys.corp_user">
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta http-equiv="Cache-Control" content="no-cache">
  <meta http-equiv="Pragma" content="no-cache">
  <title>Corporation Banking</title>
  <link rel="stylesheet" type="text/css" href="/cib/css/page.css">
  <SCRIPT language=JavaScript src="/cib/javascript/common.js"></SCRIPT>
  <SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
  <SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
  <script language=JavaScript>
  var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
</script>
  </head>
  <body onLoad="pageLoad();">
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
    </tr>
  </table>
  <set:messages width="90%" cols="1" />
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
    </tr>
  </table>
  </body>
</set:loadrb>
</html>
