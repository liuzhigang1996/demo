<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
  <title><set:label name="documentTitle_LoginBank"/></title>
  <link href="/cib/css/main.css" rel="stylesheet" type="text/css">
  <SCRIPT language=JavaScript src="/cib/javascript/common.js"></SCRIPT>
  <SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
  <SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
  <script language="JavaScript">
  var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
<!--hide
function pageLoad()
{
	//When session invalid，forward to login，jsp，need to show it in top frame.
	if(window.parent.frames.length > 1){
		window.parent.frames['topFrame'].document.getElementById("exitActionFlag").value="existByTimeout";
		 window.parent.location="/cib/bankLogin.do?ActionMethod=load";
	}
	document.getElementById("loginId").focus();
}
function doSubmit()
{
	if(validate_login(document.getElementById("form1"))){
		setFormDisabled("form1");
		document.getElementById("form1").submit();
	}
}
//endhide-->
</script>
  </head>
  <body onLoad="javascript:formFocus();">
<set:loadrb file="app.cib.resource.sys.login" alias="">
  <table width="100%" border=0 cellspacing=0 cellpadding=0>
    <tr>
      <%--<td width=161><img src="/cib/images/new_logo.gif" width=161 height=82></td>
      <TD width="100%"><img height=82 src="/cib/images/new_red-line.gif" width="100%"></TD>
    --%>
    <td height="65" align="right" background="/cib/images/logo-page.gif" style="background-repeat:no-repeat;-moz-background-size:100% 100%;background-size:auto 100%;">
    </TD>
    </tr>
  </table>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td width="100%" bgcolor="#0F659B"><img src="/cib/images/shim.gif" width="10" height="18"></td>
    </tr>
    <tr>
      <td colspan="4"><img src="/cib/images/shim.gif" width="1" height="1"></td>
    </tr>
    <tr bgcolor="EDC64A">
      <td colspan="4"><img src="/cib/images/shim.gif" width="1" height="1"></td>
    </tr>
    <tr>
      <td colspan="4"><img src="/cib/images/shim.gif" width="1" height="1"></td>
    </tr>
    <tr bgcolor="white">
      <td colspan="4"><img src="/cib/images/shim.gif" width="1" height="1"></td>
    </tr>
  </table>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" height="40">
    <tr>
      <td nowrap class="title1"><set:label name="documentTitle_LoginBank"/> </td>
      <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FF0000">
          <tr>
            <td height="1"><img src="/cib/images/shim.gif" width="1" height="1"></td>
          </tr>
        </table></td>
    </tr>
  </table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="19"><img src="/cib/images/table_top_long.gif" width="100%" height="19"></td>
  </tr>
  <tr>
    <td align="center" bgcolor="#999999"><table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
        <tr>
          <td width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
          <td><table width="100%" border="0" cellspacing="0" cellpadding="5">
            <tr>
              <td width="60%" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><strong><set:label name="label_ChangePin_2"/> </strong></td>
                  </tr>
                </table>
                  <form action="/cib/bankLogin.do" method="post" name="form1" target="_top" id="form1">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="login" form="form1" file="login" />
                        </td>
                      </tr>
                    </table>
                    <table width="100%" align="center" border="0" cellpadding="5" cellspacing="0">
                      <TR>
                        <TD width="25%" align="center"><img src="/cib/images/Ok.gif" width="33" height="33"></TD>
                        <TD><set:label name="label_ChangePin_3"/>  </TD>
                      </TR>
                      <tr align="center">
                        <td height="30" align="center">&nbsp;</td>
                        <td height="30" align="left"><input name="buttonReturn" id="buttonReturn" type="button" class="button" value="<set:label name='buttonReturn'/>" tabindex="3" onClick="doSubmit();">
                            <input id="ActionMethod" name="ActionMethod" type="hidden" value="load">
                            <input id="userId" name="userId" type="hidden" value="<set:data name='userId'/>">
                            <input id="PageLanguage" name="PageLanguage" type="hidden" value="<set:data name='PageLanguage'/>"></td>
                      </tr>
                    </table>
                  </form>
                </td>
              <td width="45%"><table width="400" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td><img src="/cib/images/arrow3.gif" width="8" height="9">
                      <set:label name="label_FirstLogin_title"/></td>
                </tr>
                <tr>
                  <td><table width="100%" cellpadding="0" cellspacing="0">
                      <TR>
                        <TD vAlign="top" width="1%">1. </TD>
                        <TD><set:label name="label_FirstLogin_1"/></TD>
                      </TR>
                      <TR>
                        <TD vAlign="top" width="1%">2. </TD>
                        <TD><set:label name="label_FirstLogin_2"/></TD>
                      </TR>
                      <TR>
                        <TD vAlign="top" width="1%">3. </TD>
                        <TD><set:label name="label_FirstLogin_3"/></TD>
                      </TR>
                      <TR>
                        <TD vAlign="top" width="1%">4. </TD>
                        <TD><set:label name="label_FirstLogin_4"/></TD>
                      </TR>
                  </table></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
              </table></td>
            </tr>
          </table>		  </td>
          <td width="1%"><img src="/cib/images/shim.gif" width="10" height="1"></td>
      </table></td>
    <td align="right" width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="999999">
    <td><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td colspan="3"><img src="/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="white">
    <td colspan="3"><img src="/images/shim.gif" width="1" height="2"></td>
  </tr>
</table>
  </set:loadrb>
  </body>
</html>
