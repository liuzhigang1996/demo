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
}
*/
function pageLoad()
{
	//When session invalid，forward to login，jsp，need to show it in top frame.
	if(window.parent.frames.length > 1){
		window.parent.frames['topFrame'].document.getElementById("exitActionFlag").value="existByTimeout";
		 window.parent.location="/cib/bankLogin.do?ActionMethod=load";
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
	window.location="/cib/bankLogin.do?ActionMethod=load&PageLanguage=<set:data name='PageLanguage'/>";
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
  <body onLoad="javascript:formFocus();">
<set:loadrb file="app.cib.resource.sys.login" alias="">
  <table width="100%" border=0 cellspacing=0 cellpadding=0>
    <tr>
      <td width=161><img src="/cib/images/new_logo.gif" width=161 height=82></td>
      <TD width="100%"><img height=82 src="/cib/images/new_red-line.gif" width="100%"></TD>
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
          <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td><strong>
                <set:label name="label_Enter_Pin"/>
              </strong></td>
            </tr>
          </table>
          <table width="100%" border="0" cellspacing="0" cellpadding="5">
            <tr>
              <td width="60%" valign="top"><form action="/cib/bankLogin.do" method="post" name="form1" target="_top" id="form1" autocomplete="off">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="login" form="form1" file="login" />                        </td>
                      </tr>
                    </table>
                    <table width="100%" align="center" border="0" cellpadding="5" cellspacing="0">
                      <TR>
                        <TD width="40%" align="right"><set:label name="Login_Id"/>                        </TD>
                        <TD><input id="loginId" name="loginId" type="text" value="<set:data name='loginId'/>" size="20" maxlength="20" readonly="readonly" style="width:100">                        </TD>
                      </TR>
                      <TR>
                        <TD align="right"><set:label name="PIN"/></TD>
                        <TD><input name="password" id="password" type="password" value="" size="20" maxlength="20" style="width:100" autocomplete="off" onKeyPress="checkCapLock(event);">                        </TD>
                      </TR>
                      <tr align="center">
                        <td height="30" colspan="2"><input name="buttonForceLogin" id="buttonForceLogin" type="button" class="button" value="<set:label name='buttonForceLogin'/>" tabindex="3" onClick="doSubmit();">
                          <input name="buttonReturnNormal" id="buttonReturnNormal" type="button" class="button" value="<set:label name='buttonReturnNormal'/>" onClick="doReturn();">
                          <input id="buttonReset" name="buttonReset" type="reset" class="button" value="<set:label name='buttonReset'/>" tabindex="4">
                          <input id="ActionMethod" name="ActionMethod" type="hidden" value="forceLogin">
                          <input id="PageLanguage" name="PageLanguage" type="hidden" value="<set:data name='PageLanguage'/>">
                          <input id="ForceLoginFlag" name="ForceLoginFlag" type="hidden" value="YES">
                          <input name="capsLockOnFlag" type="hidden" id="capsLockOnFlag" value="N"></td>
                      </tr>
                    </table>
                  </form>                </td>
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
