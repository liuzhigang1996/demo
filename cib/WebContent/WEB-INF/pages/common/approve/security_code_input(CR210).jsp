<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="../../../../Templates/dialog.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
<title>BANK Macau Development Banking</title>
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
function doSubmit(){

		var codeval = document.getElementById("securityCode").value;
		if(validate_inputSecurityCode(document.getElementById("form1"))){
			window.opener.setSecurityCode(codeval);
			window.close();
		}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="#" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.flow.approve" -->
</head>
<body onLoad="pageLoad();">
<set:loadrb file="app.cib.resource.flow.approve">
  <table width="500" border="0" cellspacing="0" cellpadding="0" height="40">
    <tr>
      <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" -->
     <!--update by liang_ly for CR204 2015-4-9-->

      	<set:label name="functionTitle_securitycode" defaultvalue=""/>
        
      <!-- InstanceEndEditable --></td>
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
    <td><form action="#" method="post" name="form1" id="form1" onSubmit="javascript:return false;">
      <!-- InstanceBeginEditable name="sectioncontent" -->
      <!-- modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB begin -->
    	<set:messages width="100%" cols="1" align="center"/>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/><set:fieldcheck name="inputSecurityCode" form="form1" file="login" /></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="2" class="groupinput"><set:label name="input_info"/></td>
                </tr>
                <tr>
                  <td width="28%" class="label1"><set:label name="security_code"/></td>
                  <td width="72%" class="content1"><input type="password" name="securityCode" id="securityCode" maxlength="12" size="20" value="<set:data name='securityCode'/>"></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input id="ok" name="ok" type="button" value="&nbsp;&nbsp;<set:label name='buttonOK'/>&nbsp;&nbsp;" onClick="doSubmit()">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="ok">
                    <!-- add by long_zg 2015-06-24 for CR210 New policy to BOB/BOL password -->
                  <input name="userId" type="hidden" id="userId" value="<set:data name='userId'/>">
                  <input name="idNo" type="hidden" id="idNo" value="<set:data name='idNo'/>">
                  </td>
                </tr>
              </table>
       <!-- modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB end -->  
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
