<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<set:loadrb file="app.cib.resource.sys.login" alias="">
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta http-equiv="Cache-Control" content="no-cache">
  <meta http-equiv="Pragma" content="no-cache">
  <title>Login - MDB Corporate Online Banking</title>
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
		 window.parent.location="/cib/login.do?ActionMethod=load";
	}
	document.getElementById("loginId").focus();
}
function doSubmit()
{
	if(validate_login(document.getElementById("form1"))){
		setFormDisabled("form1");
		return true;
		//document.getElementById("form1").submit();
	}
}
//endhide-->
</script>
<script type="text/javascript" src="/cib/javascript/jquery-1.7.2.min.js"></script>
  <script type="text/javascript" src="/cib/javascript/jquery.jcryption.3.1.0.js"></script>
  <script type="text/javascript">
	$(function() {
		var options ={
			beforeEncryption: doSubmit,
			getKeysURL: "/cib/EncryptionServlet?getPublicKey=true",
    		handshakeURL: "/cib/EncryptionServlet?handshake=true"
			};
		$("#form1").jCryption(options);
	});
</script>
  </head>
  <body onLoad="pageLoad();">
  <table width="100%" border=0 cellspacing=0 cellpadding=0>
    <tr>
      <!--<td width="161"><img src="/cib/images/new_logo.gif" width="161" height="82"></td>
      <TD width="100%"><img height=82 src="/cib/images/new_red-line.gif" width="100%"></TD>
    -->
    <TD background="/cib/images/top_logo.gif" height="76" align="right" style="background-repeat:no-repeat"></TD>
    </tr>
  </table>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
      <td width="100%" bgcolor="CCCCCC"><img src="/cib/images/shim.gif" width="10" height="18"></td>
	  <!--
      <td width="34" align="right" height="18"><a href="/ebank/bank/login.jsp?lang=C"><img src="/cib/images/button-lang_C.gif" width="34" height="18" border="0"></a></td>
      <td width="69" height="18"><a href="/ebank/bank/login.jsp?lang=P"><img src="/cib/images/button-lang_P.gif" width="69" height="18" border="0"></a></td>
      <td><img src="/cib/images/horn.gif" width="19" height="18"></td>
	  -->
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
      <td nowrap class="title1"><set:label name="documentTitle_Login"/></td>
      <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
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
              <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td align="left"><strong> <set:label name="label_ChangePin_2"/></strong></td>
                </tr>
              </table></td>
              <td width="20%" rowspan="2" valign="top"><table width="50" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td><%--<img src="/cib/images/arrow3.gif" width="8" height="9">
                      <set:label name="label_FirstLogin_title"/>--%>
                  </td>
                </tr>
                <tr>
                  <td><table width="100%" cellpadding="0" cellspacing="0">
                      <%--
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
                      --%>
                  </table></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
              </table>
                <br>
                <br></td>
            </tr>
            <tr>
              <td width="75%" valign="top">
              <%--<form action="/cib/login.do" method="post" name="form1" target="_top" id="form1">--%>
                  <form action="/cib/login.do?ActionMethod=changePinLogin" method="post" name="form1" target="_top" id="form1" autocomplete="off">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="login" form="form1" file="login" />                        </td>
                      </tr>
                    </table>
                    <table width="100%" align="center" border="0" cellpadding="5" cellspacing="0">
                      <TR>
                        <TD width="30%" align="center">
                          <img src="/cib/images/Ok.gif" width="33" height="33"></TD>
                        <TD><%--<set:label name="label_ChangePin_3"/>--%></TD>
                      </TR>
					  <tr align="left">
                        <td height="50" align="center"><input name="buttonReturn" id="buttonReturn" type="submit" class="button" value="<set:label name='buttonReturn'/>" tabindex="3" onClick="doSubmit();"></td>
                        <td height="30" align="center">
                          <input id="ActionMethod" name="ActionMethod" type="hidden" value="load">
                          <input id="userId" name="userId" type="hidden" value="<set:data name='userId'/>">
                          <input id="PageLanguage" name="PageLanguage" type="hidden" value="<set:data name='PageLanguage'/>">
                          <input id="cifNo" name="cifNo" type="hidden" value="<set:data name='cifNo'/>">
                          <input id="loginId" name="loginId" type="hidden" value="<set:data name='loginId'/>">
                          <input id="password" name="password" type="hidden" value="<set:data name='password'/>">
                          <set:singlesubmit/></td>
                      </tr>
                    </table>
                  </form>
                </td>
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
  </body>
  </set:loadrb>
</html>
