<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/dialog.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
<title>Corporation Banking</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit()
{
	setFormDisabled("form1");
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/transferInOversea.do" --><!-- InstanceParam name="help_href" type="text" value="#" passthrough="true" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.transfer_oversea" -->
</head>
<body onLoad="pageLoad();">
<set:loadrb file="app.cib.resource.txn.transfer_oversea">
  <table width="500" border="0" cellspacing="0" cellpadding="0" height="40">
    <tr>
      <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="TRANSFER TO ACCOUNTS IN BANKS OVERSEAS"/><!-- InstanceEndEditable --></td>
      <!-- <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="1" bgcolor="#FF3333"><img src="/cib/images/menu_image/spacer.gif" width="1" height="1"></td>
          </tr>
      </table></td> -->
    </tr>
  </table>
  <table width="500" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
  <tr>
    <td width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
    <td><form action="/cib/transferInOversea.do" method="post" name="form1" id="form1">
      <!-- InstanceBeginEditable name="sectioncontent" -->
      <table width="500" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td><table width="95%" align="center" border="0" cellpadding="5" cellspacing="0">
              <TR>
                <TD width="10%" height="60" align="center"><img src="/cib/images/Critical.gif" width="33" height="33"></TD>
                <TD><set:label name="err.duplicate.template" rb="app.cib.resource.common.errmsg" defaultvalue="Save Template Fail"/></TD>
              </TR>
              <tr align="center">
                <td height="30" colspan="2" align="center"><input name="buttonReturn" id="buttonReturn" type="button" class="button" value="<set:label name='buttonClose'/>" tabindex="3" onClick="doSubmit();">
                <input id="ActionMethod" name="ActionMethod" type="hidden" value="addLoad">
                </td>
              </tr>
          </table></td>
        </tr>
      </table>
      <!-- InstanceEndEditable -->
    </form></td>
</table>
<table width="500" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
  </tr>
</table>
</set:loadrb>
</body>
<!-- InstanceEnd --></html>
