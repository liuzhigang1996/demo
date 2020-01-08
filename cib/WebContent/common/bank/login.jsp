<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<set:loadrb file="app.cib.resource.sys.login" alias="">
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="0">
<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8;IE=7; IE=EDGE">
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
	savePassword = "";
 	countShowPass = 0;
 	str = "";
	document.getElementById("password").value = "";
	document.getElementById("showPassword").value = "";
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
	document.getElementById("password").value = savePassword;
	/*if (!checkPin()) {
		document.form1.password.focus();
		return false;
	}*/
	 <!-- add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB begin-->
	/*
	if(validate_login(document.getElementById("form1"))){
		setFormDisabled("form1");
		document.getElementById("form1").submit();
	}
	*/
	
	if(validate_login(document.getElementById("form1"))){
		setFormDisabled("form1");
		return true ;
	}
	enabled();
	return false ;
}
 <!-- add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB end-->
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
 
 <!-- add by lzg not prompt for passwords when logging in  -->
 <script type="text/javascript">
 	var savePassword = "";
 	var countShowPass = 0;
 	var str = "";
 	function replaceContent(e){
 		if((e.keyCode >=48 && e.keyCode <= 57) || e.keyCode == 20 || (e.keyCode <= 90 && e.keyCode >=65) ||(e.keyCode <= 105 &&e.keyCode >= 96) ||e.keyCode == 229){
	 		var showPassword = document.getElementById("showPassword");
	 		var showPasswordValue = showPassword.value;
	 		if(countShowPass > showPasswordValue.length){
 				countShowPass = showPasswordValue.length;
 				savePassword = savePassword.substring(0,countShowPass);
 				str = str.substring(0,countShowPass);
 			}
	 		if((countShowPass+1) == showPasswordValue.length){
	 			savePassword = savePassword + showPasswordValue.substring(countShowPass);
	 			str = str + "•";
	 		}else{
	 			var len = showPasswordValue.length - countShowPass;
	 			for(var i = 0; i < len; i++){
	 				str = str + "•";
	 			}
	 			savePassword = savePassword + showPasswordValue.substring(countShowPass);
	 		}
	 		showPassword.value = str;
	 		countShowPass = str.length;
 		}else if(e.keyCode == 8){
// 			document.getElementById("password").value = "";
// 			document.getElementById("showPassword").value = "";
//			savePassword = "";
// 			countShowPass = 0;
//			str = "";
			var showPassword = document.getElementById("showPassword");
			var showPasswordValue = showPassword.value;
			countShowPass = showPasswordValue.length;
			savePassword = savePassword.substring(0,countShowPass);
			str = str.substring(0,countShowPass);
 		}else{
 			var showPassword = document.getElementById("showPassword");
 			var showPasswordValue = showPassword.value;
 			if(countShowPass == 0){
 				showPassword.value = "";
 			}else{
 				showPassword.value = showPasswordValue.substring(0,countShowPass);
 			}
 		}
 	}
 	function clearPassForIE(){
 		var showPassword = document.getElementById("showPassword");
 		var showPasswordValue = showPassword.value;
 		if(showPasswordValue == ""){
 			countShowPass = 0;
 			str = "";
 			savePassword = "";
 		}
 	}
 </script>
 <!-- add by lzg end -->
 
  </head>
  <body onLoad="pageLoad();">
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
      <td width="100%" bgcolor="#0F659B"><img src="/cib/images/shim.gif" width="10" height="18">

        </td>
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
                  <%--<tr>
                    <td><set:label name="label_Enter_Pin"/></td>
                  </tr>
                --%></table>
                <!-- add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB begin-->
                  <!--<form action="/cib/bankLogin.do" method="post" name="form1" target="_top" id="form1" autocomplete="off">-->
                  <form action="/cib/bankLogin.do?ActionMethod=login" method="post" name="form1" target="_top" id="form1" autocomplete="off">
                  <!-- add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB end-->
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="login" form="form1" file="login_bank" />
                        </td>
                      </tr>
                    </table>
                    <table width="100%" align="center" border="0" cellpadding="5" cellspacing="0">
                      <TR>
                        <TD width="40%" align="right"><set:label name="Login_Id"/>
                        </TD>
                        <TD><input id="loginId" name="loginId" type="text" value="<set:data name='loginId' />" size="20" maxlength="20" style="width:100">
                        </TD>
                      </TR>
                      <TR>
                        <TD align="right"><set:label name="PIN"/></TD>
                        <!-- modified by lzg for pwc not prompt for passwords when logging in -->
                        <TD>
                        	<input id = "showPassword" onpaste="return false" oncopy="return false" oncut="return false" type="text" value="" size="20" maxlength="20" style="width:100;-webkit-text-security:disc;text-security:disc;"  autocomplete="off" onkeypress="checkCapLock(event);" onkeydown="clearPassForIE()" onkeyup="replaceContent(event)">
                        	<input name="password" id="password" type="hidden">
                        </TD>
                        <!-- modified by lzg end -->
                      </TR>
                      <tr align="center">
                        <td height="30" colspan="2"><p>
                        	<!-- add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB begin-->
                            <!--<input name="buttonLogin" id="buttonLogin" type="button" class="button" value="<set:label name='buttonLogin'/>" tabindex="3" onClick="doSubmit();">-->
                            <input name="buttonLogin" id="buttonLogin" type="submit" class="button" value="<set:label name='buttonLogin'/>" tabindex="3">
                            <!-- add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB begin-->
                            <input id="buttonReset" name="buttonReset" type="reset" class="button" value="<set:label name='buttonReset'/>" tabindex="4">
                            <input id="ActionMethod" name="ActionMethod" type="hidden" value="login">
                            <input id="PageLanguage" name="PageLanguage" type="hidden" value="<set:data name='PageLanguage'/>">
                            <input name="capsLockOnFlag" type="hidden" id="capsLockOnFlag" value="N">
                        </p></td>
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
  </body>
  </set:loadrb>
</html>
