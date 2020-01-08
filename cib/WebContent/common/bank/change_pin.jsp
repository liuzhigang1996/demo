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
	var newPinValue = document.form1.newPassword.value;
	var checkUpperNum = checkChar(newPinValue,'upper');
	var checkLowerNum = checkChar(newPinValue,'lower');
	document.form1.checkUpperNum.value = checkUpperNum;
    document.form1.checkLowerNum.value = checkLowerNum;
	/*var currentLen = 6;
	if(pin_min < 6) currentLen = pin_min;
	var oldPinValue = document.form1.oldPassword.value;
	var newPinValue = document.form1.newPassword.value;
	var confirmPin = document.form1.confirmPassword.value;
	if (oldPinValue=='') {
		alert('<set:label name="err.InitialPin.blank" rb="app.cib.resource.common.errmsg"/>');
		document.form1.oldPassword.focus();
		return false;
	}
	else if (newPinValue=='') {
		alert('<set:label name="err.newPin.blank" rb="app.cib.resource.common.errmsg"/>');
		document.form1.newPassword.focus();
		return false;
	}
	else if (newPinValue != confirmPin) {
		alert('<set:label name="err.pin.different" rb="app.cib.resource.common.errmsg"/>');
		document.form1.confirmPassword.focus();
		return false;
	}
	else if (newPinValue == oldPinValue) {
		alert('<set:label name="err.pin.sameAsOld" rb="app.cib.resource.common.errmsg"/>');
		document.form1.newPassword.focus();
		return false;
	}
	else if (oldPinValue.length < currentLen) {
		alert("Old Password shall have at least " + currentLen + " digits long");
		document.form1.oldPassword.focus();
		return false;
	}
	else if (newPinValue.length < pin_min || newPinValue.length > pin_max) {
		alert("New Password shall have at least " + pin_min + " but not exceeding " + pin_max + " digits long");
		document.form1.newPassword.focus();
		return false;
	}
	else if(checkChar(newPinValue,'upper') < pin_upper){
		alert("New Password shall have at least " + pin_upper + " digits long upper letter");
		document.form1.newPassword.focus();
		return false;
	}
	else if(checkChar(newPinValue,'lower') < pin_lower){
		alert("New Password shall have at least " + pin_lower + " digits long lower letter");
		document.form1.newPassword.focus();
		return false;
	}
	else
		return true;*/
}
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
	//update by xyf 20090417
	//checkPin();
	//if(validate_changePin(document.getElementById("form1"))){
	/*if(validate_bank_changePin(document.getElementById("form1"))){
		setFormDisabled("form1");
		document.getElementById("form1").submit();
	}*/
	<!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin-->
	if(validate_bank_changePin(document.getElementById("form1"))){
		setFormDisabled("form1");
		return true;//document.getElementById("form1").submit();
	}
	enabled();
	return false;
}
function enabled(){
	
	$("form[id='form1'] :text").attr("disabled",false); 
	$("form[id='form1'] :password").attr("disabled",false); 
	$("form[id='form1'] :submit").attr("disabled",false); 
	$("form[id='form1'] :hidden").attr("disabled",false); 
	$("#form1").attr("disabled",false); 
	var options ={
			beforeEncryption: doSubmit
			//submitEvent:"submit"
			};
	$("#form1").jCryption(options);
	
}
<!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end-->
//endhide-->
</script>
<!-- add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB begin-->
<script type="text/javascript" src="/cib/javascript/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/cib/javascript/jquery.jcryption.3.1.0.js?v=20150105"></script>
<script type="text/javascript">
	$(function() {
		var options ={
			beforeEncryption: doSubmit,
			getKeysURL: "/cib/EncryptionServlet?getPublicKey=true",
    		handshakeURL: "/cib/EncryptionServlet?handshake=true"
			};
		$("#form1").jCryption(options);
		//$("#form1").jCryption();
	});
 </script>
 <!-- add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB end-->
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
                    <td><strong> <set:label name="label_ChangePin_1"/> </strong></td>
                  </tr>
                </table>
                  <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin-->
                  <!--<form action="/cib/bankLogin.do" method="post" name="form1" target="_top" id="form1">-->
                  <form action="/cib/bankLogin.do?ActionMethod=changePin" method="post" name="form1" target="_top" id="form1">
                  <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end-->
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="login" form="form1" file="login" />
                        </td>
                      </tr>
                    </table>
                    <table width="100%" align="center" border="0" cellpadding="5" cellspacing="0">
                      <TR>
                        <TD width="25%" align="right"><set:label name="User_Id"/></TD>
                        <TD><set:data name="userId"/>  </TD>
                      </TR>
                      <TR>
                        <TD width="25%" align="right"><set:label name="Initail_PIN"/></TD>
                        <TD><input id="oldPassword" name="oldPassword" type="password" value="" size="20" maxlength="20" style="width:100" autocomplete="off">
                        </TD>
                      </TR>
                      <TR>
                        <TD align="right"><set:label name="New_PIN"/></TD>
                        <TD><input id="newPassword" name="newPassword" type="password" value="" size="20" maxlength="20" style="width:100" autocomplete="off">
                        </TD>
                      </TR>
                      <TR>
                        <TD align="right"><set:label name="New_PIN_Confirm"/></TD>
                        <TD><input id="confirmPassword" name="confirmPassword" type="password" value="" size="20" maxlength="20" style="width:100" autocomplete="off">
                        </TD>
                      </TR>
                      <tr align="center">
                        <td height="30" align="center">&nbsp;</td>
                        <td height="30" align="left">
                        <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin-->
                        <!--<input name="buttonChangePin" id="buttonChangePin" type="button" class="button" value="<set:label name='buttonChangePin'/>" tabindex="3" onClick="doSubmit();">-->
                        <input name="buttonChangePin" id="buttonChangePin" type="submit" class="button" value="<set:label name='buttonChangePin'/>" tabindex="3">
                        <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end-->
                            <input id="buttonReset" name="buttonReset" type="reset" class="button" value="<set:label name='buttonReset'/>" tabindex="4">
                            <input id="ActionMethod" name="ActionMethod" type="hidden" value="changePin">
                            <input id="userId" name="userId" type="hidden" value="<set:data name='userId'/>"></td>
							<input name="checkUpperNum" id="checkUpperNum" type="hidden" value="0">
						    <input name="checkLowerNum" id="checkLowerNum" type="hidden" value="0">
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
