<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<set:loadrb file="app.cib.resource.sys.login" alias="">
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta http-equiv="Cache-Control" content="no-cache">
  <meta http-equiv="Pragma" content="no-cache">
  <title><set:label name="documentTitle_Login"/></title>
  <link href="/cib/css/main.css" rel="stylesheet" type="text/css">
  <SCRIPT language=JavaScript src="/cib/javascript/common.js"></SCRIPT>
  <SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
  <SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
  <script language="JavaScript">
  var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
<!--hide
//----------add by xyf 20081219 begin--------------
var pin_min = parseInt(<set:data name='pinMin' />);
var pin_max = parseInt(<set:data name='pinMax' />);
var pin_upper = parseInt(<set:data name='pinUpper' />);
var pin_lower = parseInt(<set:data name='pinLower' />);
var pin2_min = parseInt(<set:data name='pin2Min' />);
var pin2_max = parseInt(<set:data name='pin2Max' />);
var pin2_upper = parseInt(<set:data name='pin2Upper' />);
var pin2_lower = parseInt(<set:data name='pin2Lower' />);
/*
//xyf 20081219
if(pin_min >6) pin_min = 6;
if(pin2_min >6) pin2_min = 6;
if(pin_max <12) pin_max = 12;
if(pin2_max <12) pin2_max = 12
function checkChar(checkText, flag) {
	var i = 0;
	var reNum = 0;
	var validStr = '';
	if(flag=='upper'){
		validStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
		for(i=0; i< checkText.length; i++){
			var the_char = checkText.charAt(i);
			if(validStr.indexOf(the_char) != -1){
				reNum = reNum + 1;
			}
  		}
	}else if(flag=='lower'){
		validStr = 'abcdefghijklmnopqrstuvwxyz';
		for(i=0; i<checkText.length; i++){
			var the_char = checkText.charAt(i);
			if(validStr.indexOf(the_char) != -1){
				reNum = reNum + 1;
			}
  		}
	}else {
		validStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
		for(i=0; i< checkText.length; i++){
			var the_char = checkText.charAt(i);
			if(validStr.indexOf(the_char) != -1){
				reNum = reNum + 1;
			}
  		}
	}
  	return reNum;
}
function checkPin() {
	var pinValue = document.form1.password.value;
	if (pinValue=='') {
		alert('<set:label name="err.pin.blank" rb="app.cib.resource.common.errmsg"/>');
		return false;
	}
	else if (pinValue.length < pin_min || pinValue.length > pin_max) {
		alert("Your Password shall have at least " + pin_min + " but not exceeding " + pin_max + " digits long");
		return false;
	}
	else
		return true;
}*/
function pageLoad()
{
	//When session invalid，forward to login，jsp，need to show it in top frame.
	if(window.parent.frames.length > 1){
		window.parent.frames['topFrame'].document.getElementById("exitActionFlag").value="existByTimeout";
		 window.parent.location="/cib/login.do?ActionMethod=load";
	}
	document.getElementById("loginId").focus();
	showCapsLockWarningMsg('<set:data name="capsLockErrFlag"/>');
}
function doSubmit()
{
	/*if (!checkPin()) {
		document.form1.password.focus();
		return false;
	}*/
	if(validate_login(document.getElementById("form1"))){
		setFormDisabled("form1");
		document.getElementById("form1").submit();
	}
}
function doReturn()
{
	setFormDisabled("form1");
	window.location="/cib/login.do?ActionMethod=load&PageLanguage=<set:data name='PageLanguage'/>";
}
function selCert()
{
	var strTmp;
	retVal = document.applets['CertApplet'].selCert();
	if(retVal !=0){
		return;
	}else{
		document.getElementById("UserCert").value=document.applets['CertApplet'].getCert();
		document.getElementById("certTitle").value=document.applets['CertApplet'].getCertInfo(9);
	}
}
function checkCapLock(e){
	kc = e.keyCode?e.keyCode:e.which;
	sk = e.shiftKey?e.shiftKey:((kc == 16)?true:false);
	if(((kc >= 65 && kc <= 90) && !sk)||((kc >= 97 && kc <= 122) && sk)){
		document.getElementById('capsLockOnFlag').value = 'Y';
	}
}
function showCapsLockWarningMsg(errFlag){
	if(errFlag == 'Y'){
		alert('<set:label name="err.sys.CapsLockOn" rb="app.cib.resource.common.errmsg"/>');
		document.getElementById('password').select();
	}
}
//endhide-->
</script>
  </head>
  <body onLoad="pageLoad();">
  <table width="100%" border=0 cellspacing=0 cellpadding=0>
    <tr>
      <td width="161"><img src="/cib/images/new_logo.gif" width="161" height="82"></td>
      <TD width="100%"><img height=82 src="/cib/images/new_red-line.gif" width="100%"></TD>
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
      <td nowrap class="title1"><set:label name="label_Login_title"/> </td>
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
              <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:label name="label_Enter_Pin"/></td>
                </tr>
              </table></td>
              <td width="45%" rowspan="2" valign="top"><table width="400" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td><img src="/cib/images/arrow3.gif" width="8" height="9">  <set:label name="label_FirstLogin_title"/></td>
                </tr>
                <tr>
                  <td><table width="100%" cellpadding="0" cellspacing="0">
                      <TR>
                        <TD vAlign="top" width="1%">1. </TD>
                        <TD><set:label name="label_FirstLogin_1"/></TD>
                      </TR>
                      <TR>
                        <TD vAlign="top" width="1%">2. </TD>
                        <TD> <set:label name="label_FirstLogin_2"/></TD>
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
              </table>
                <br>
                <br></td>
            </tr>
            <tr>
              <td width="60%" valign="top"><form action="/cib/login.do" method="post" name="form1" target="_top" id="form1" autocomplete="off">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="login" form="form1" file="login" />                        </td>
                      </tr>
                    </table>
                    <table width="100%" align="center" border="0" cellpadding="5" cellspacing="0">
                      <TR>
                        <TD width="25%" align="right"><set:label name="Login_Id"/>                        </TD>
                        <TD><input id="loginId" name="loginId" type="text" value="<set:data name='loginId'/>" size="20" maxlength="20"  readonly="readonly" style="width:100">                        </TD>
                      </TR>
                      <TR>
                        <TD align="right"><set:label name="CIF_NO"/></TD>
                        <TD><input id="cifNo" name="cifNo" type="text" value="" size="20" maxlength="20" style="width:100" autocomplete="off">                        </TD>
                      </TR>
                      <TR>
                        <TD align="right"><set:label name="PIN"/></TD>
                        <TD><input id="password" name="password" type="password" value="" size="20" maxlength="20" style="width:100" autocomplete="off">                        </TD>
                      </TR>
                      <TR>
                        <TD align="right"><div style="display:none">
      <applet  code="app.cib.cert.applet.CertApplet.class" name="CertApplet" width="0" height="0" align="absmiddle" MAYSCRIPT archive="/cib/applet/CertApplet.jar" id="CertApplet">
            </applet></div>
                          <input name="buttonSelCert" id="buttonSelCert" type="button" class="button" value="<set:label name='buttonSelCert'/>" onClick="selCert();"></TD>
                        <TD><input id="certTitle" class="certificate" name="certTitle" type="text" value="" size="60" maxlength="60" style="width:300" autocomplete="off" readonly="readonly"></TD>
                      </TR>
                      <tr align="center">
                        <td height="30" align="center">&nbsp;</td>
                        <td height="30" align="left"><input name="buttonForceLogin" id="buttonForceLogin" type="button" class="button" value="<set:label name='buttonForceLogin'/>" onClick="doSubmit();">
                          <input name="buttonReturnNormal" id="buttonReturnNormal" type="button" class="button" value="<set:label name='buttonReturnNormal'/>" onClick="doReturn();">
                          <input id="buttonReset" name="buttonReset" type="reset" class="button" value="<set:label name='buttonReset'/>">
                          <input id="ActionMethod" name="ActionMethod" type="hidden" value="forceLogin">
                          <input id="UserCert" name="UserCert" type="hidden">
                          <input id="PageLanguage" name="PageLanguage" type="hidden" value="<set:data name='PageLanguage'/>">
                          <input id="ForceLoginFlag" name="ForceLoginFlag" type="hidden" value="YES">
                          <input name="capsLockOnFlag" type="hidden" id="capsLockOnFlag" value="N">
                          <set:singlesubmit/></td>
                      </tr>
                    </table>
                  </form>
<!--				  
                <table width="100%" border="0" cellspacing="0" cellpadding="5">
                    <tr>
                      <td width="120"><img src="/cib/images/aLock_Animation2_E.gif" width="120" height="55"></td>
                      <td><set:label name="label_Security_Advices"/></td>
                    </tr>
                </table>
-->				
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
