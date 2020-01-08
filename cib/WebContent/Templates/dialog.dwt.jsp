<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- TemplateBeginEditable name="doctitle" -->
<title>Corporation Banking</title>
<!-- TemplateEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- TemplateBeginEditable name="javascirpt" -->
<script language=JavaScript>
function pageLoad(){
}
</script>
<!-- TemplateEndEditable --><!-- TemplateParam name="page_action" type="text" value="/cib/noaction.do" --><!-- TemplateParam name="help_href" type="text" value="#" --><!-- TemplateParam name="resource_file" type="text" value="" -->
</head>
<body onLoad="pageLoad();">
<set:loadrb file="@@(resource_file)@@">
  <table width="500" border="0" cellspacing="0" cellpadding="0" height="40">
    <tr>
      <td class="title1" nowrap><!-- TemplateBeginEditable name="section_title" -->
        <set:label name="functionTitle" defaultvalue=""/>
      <!-- TemplateEndEditable --></td>
      <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="1" bgcolor="#FF0000"><img src="/cib/images/menu_image/spacer.gif" width="1" height="1"></td>
          </tr>
      </table></td>
    </tr>
  </table>
  <table width="500" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
  <tr>
    <td width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
    <td><form action="@@(page_action)@@" method="post" name="form1" id="form1">
      <!-- TemplateBeginEditable name="sectioncontent" -->section_content<!-- TemplateEndEditable -->
    </form></td>
</table>
<table width="500" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
  </tr>
</table>
</set:loadrb>
</body>
</html>
