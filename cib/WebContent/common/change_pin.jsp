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
function checkPin(flag) {
	var newPinValue = "";
	if (flag == 'securityCode')
		newPinValue = document.form1.newSecurityCode.value;
	else
		newPinValue = document.form1.newPassword.value;
	var checkUpperNum = checkChar(newPinValue,'upper');
	var checkLowerNum = checkChar(newPinValue,'lower');
	document.form1.checkUpperNum.value = checkUpperNum;
    document.form1.checkLowerNum.value = checkLowerNum;
	/*
	if(checkChar(newPinValue,'upper') < pin_upper){
		alert("New Password requires at least " + pin_upper + " upper letter(s)");
		document.form1.newPassword.focus();
		return false;
	}
	else if(checkChar(newPinValue,'lower') < pin_lower){
		alert("New Password shall have at least " + pin_lower + " digits long lower letter");
		document.form1.newPassword.focus();
		return false;
	}
	else
		return true;
	*/
}
/*function checkSecurityCode(){
	var newSecurityCode = document.form1.newSecurityCode.value;
	var confirmSecurityCode = document.form1.confirmSecurityCode.value;
	if (newSecurityCode=='') {
		alert('<set:label name="err.newSecurityCode.blank" rb="app.cib.resource.common.errmsg"/>');
		document.form1.newSecurityCode.focus();
		return false;
	}
	if (newSecurityCode != confirmSecurityCode) {
		alert('<set:label name="err.securityCode.different" rb="app.cib.resource.common.errmsg"/>');
		document.form1.confirmSecurityCode.focus();
		return false;
	}
	else if (newSecurityCode.length < pin2_min || newSecurityCode.length > pin2_max) {
		alert("Your Security Code shall have at least " + pin2_min + " but not exceeding " + pin2_max + " digits long");
		document.form1.newSecurityCode.focus();
		return false;
	}
	else if(checkChar(newSecurityCode,'upper') < pin2_upper){
		alert("Your Security Code shall have at least " + pin2_upper + " digits long upper letter");
		document.form1.newSecurityCode.focus();
		return false;
	}
	else if(checkChar(newSecurityCode,'lower') < pin2_lower){
		alert("Your Security Code shall have at least " + pin2_lower + " digits long lower letter");
		document.form1.newSecurityCode.focus();
		return false;
	}
	else
		return true;
}*/
function pageLoad()
{   
	//add by lzg not prompt for passwords when logging in
	saveNewPassword = "";
	countShowNewPass = 0;
	newPasswordStr = "";
	document.getElementById("newPassword").value = "";
	document.getElementById("showNewPassword").value = "";
	
	saveConfirmPassword = "";
	countShowConfirmPass = 0;
	confirmPasswordStr = "";
	document.getElementById("confirmPassword").value = "";
	document.getElementById("showConfirmPassword").value = "";
	//add by lzg end
	
	//When session invalid，forward to login，jsp，need to show it in top frame.
	if(window.parent.frames.length > 1){
		 window.parent.frames['topFrame'].document.getElementById("exitActionFlag").value="existByTimeout";
		 window.parent.location="/cib/login.do?ActionMethod=load";
	}
	//modified by lzg 20190521
	//document.getElementById("loginId").focus();
	//modified by lzg end
}
function doSubmit()
{	

	//add by lzg not prompt for passwords when logging in
	document.getElementById("newPassword").value = saveNewPassword;
	document.getElementById("confirmPassword").value = saveConfirmPassword;
	//add by lzg end
	var flag = false;
	var pin2Flag = false;
	<set:if name="showChangeScode" value="Y" condition="equals">
		pin2Flag = true;
	</set:if>
	checkPin('password');
	/*if(validate_changePin(document.getElementById("form1"))){
		flag = true;
		if(pin2Flag){
			flag = false;
			checkPin('securityCode');
			if(validate_changePin2(document.getElementById("form1"))){
				flag = true;
			}
		}
		if(flag){
			setFormDisabled("form1");
			document.getElementById("form1").submit();
		}
	}
	*/
	/*var flag = "password";
	<set:if name="showChangeScode" value="Y" condition="equals">
		flag = "securityCode";
	</set:if>
	checkPin(flag);
	if (flag == "securityCode") {
		if(validate_changePin2(document.getElementById("form1"))){
			setFormDisabled("form1");
			document.getElementById("form1").submit();
		}
	}else{
		if(validate_changePin(document.getElementById("form1"))){
			setFormDisabled("form1");
			document.getElementById("form1").submit();
		}
	}*/
	<!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin-->
	
	if(validate_changePin(document.getElementById("form1"))){
		
		flag = true;
		if(pin2Flag){
			flag = false;
			checkPin('securityCode');
			if(validate_changePin2(document.getElementById("form1"))){
				flag = true;
			}
		}
		if(flag){
			setFormDisabled("form1");
			return true;//document.getElementById("form1").submit();
		}
	}
	enabled();
	return false ;
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
function checkCharNum(str){
	var patten = "^[A-Za-z0-9]+$" ;
	var re = new RegExp(patten) ;
	str = str + "" ;
	var matched = re.exec(str);
	if(null == matched ){
		return true ;
	}
	return false ;
}
</script>
<!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin-->
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
 <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end-->
 
 <!-- add by lzg not prompt for passwords when logging in  -->
 <script type="text/javascript">
	var saveNewPassword = "";
 	var countShowNewPass = 0;
 	var newPasswordStr = "";
 	function replaceNewPassword(e){
 		if((e.keyCode >=48 && e.keyCode <= 57) || e.keyCode == 20 || (e.keyCode <= 90 && e.keyCode >=65) ||(e.keyCode <= 105 &&e.keyCode >= 96) ||e.keyCode == 229){
	 		var showNewPassword = document.getElementById("showNewPassword");
	 		var showNewPasswordValue = showNewPassword.value;
	 		if(countShowNewPass > showNewPasswordValue.length){
 				countShowNewPass = showNewPasswordValue.length;
 				saveNewPassword = saveNewPassword.substring(0,countShowNewPass);
 				newPasswordStr = newPasswordStr.substring(0,countShowNewPass);
 			}
	 		if((countShowNewPass+1) == showNewPasswordValue.length){
	 			saveNewPassword = saveNewPassword + showNewPasswordValue.substring(countShowNewPass);
	 			newPasswordStr = newPasswordStr + "•";
	 		}else{
	 			var len = showNewPasswordValue.length - countShowNewPass;
	 			for(var i = 0; i < len; i++){
	 				newPasswordStr = newPasswordStr + "•";
	 			}
	 			saveNewPassword = saveNewPassword + showNewPasswordValue.substring(countShowNewPass);
	 		}
	 		showNewPassword.value = newPasswordStr;
	 		countShowNewPass = newPasswordStr.length;
 		}else if(e.keyCode == 8){
// 			document.getElementById("newPassword").value = "";
// 			document.getElementById("showNewPassword").value = "";
// 			saveNewPassword = "";
// 			countShowNewPass = 0;
// 			newPasswordStr = "";
			var showNewPassword = document.getElementById("showNewPassword");
			var showNewPasswordValue = showNewPassword.value;
			countShowNewPass = showNewPasswordValue.length;
			saveNewPassword = saveNewPassword.substring(0,countShowNewPass);
			newPasswordStr = newPasswordStr.substring(0,countShowNewPass);
 		}else{
 			var showNewPassword = document.getElementById("showNewPassword");
 			var showNewPasswordValue = showNewPassword.value;
 			if(countShowNewPass == 0){
 				showNewPassword.value = "";
 			}else{
 				showNewPassword.value = showNewPasswordValue.substring(0,countShowNewPass);
 			}
 		}
 	}
 	
 	
	var saveConfirmPassword = "";
 	var countShowConfirmPass = 0;
 	var confirmPasswordStr = "";
 	function replaceConfirmPassword(e){
 		if((e.keyCode >=48 && e.keyCode <= 57) || e.keyCode == 20 ||(e.keyCode <= 90 && e.keyCode >=65) ||(e.keyCode <= 105 &&e.keyCode >= 96) ||e.keyCode == 229){
	 		var showConfirmPassword = document.getElementById("showConfirmPassword");
	 		var showConfirmPasswordValue = showConfirmPassword.value;
	 		if(countShowConfirmPass > showConfirmPasswordValue.length){
 				countShowConfirmPass = showConfirmPasswordValue.length;
 				saveConfirmPassword = saveConfirmPassword.substring(0,countShowConfirmPass);
 				confirmPasswordStr = confirmPasswordStr.substring(0,countShowConfirmPass);
 			}
	 		if((countShowConfirmPass+1) == showConfirmPasswordValue.length){
	 			saveConfirmPassword = saveConfirmPassword + showConfirmPasswordValue.substring(countShowConfirmPass);
	 			confirmPasswordStr = confirmPasswordStr + "•";
	 		}else{
	 			var len = showConfirmPasswordValue.length - countShowConfirmPass;
	 			for(var i = 0; i < len; i++){
	 				confirmPasswordStr = confirmPasswordStr + "•";
	 			}
	 			saveConfirmPassword = saveConfirmPassword + showConfirmPasswordValue.substring(countShowConfirmPass);
	 		}
	 		showConfirmPassword.value = confirmPasswordStr;
	 		countShowConfirmPass = confirmPasswordStr.length;
 		}else if(e.keyCode == 8){
// 			document.getElementById("confirmPassword").value = "";
// 			document.getElementById("showConfirmPassword").value = "";
// 			saveConfirmPassword = "";
//			countShowConfirmPass = 0;
//			confirmPasswordStr = "";
			var showConfirmPassword = document.getElementById("showConfirmPassword");
			var showConfirmPasswordValue = showConfirmPassword.value;
			countShowConfirmPass = showConfirmPasswordValue.length;
			saveConfirmPassword = saveConfirmPassword.substring(0,countShowConfirmPass);
			confirmPasswordStr = newPasswordStr.substring(0,countShowConfirmPass);
 		}else{
 			var showConfirmPassword = document.getElementById("showConfirmPassword");
 			var showConfirmPasswordValue = showConfirmPassword.value;
 			if(countShowConfirmPass == 0){
 				showConfirmPassword.value = "";
 			}else{
 				showConfirmPassword.value = showConfirmPasswordValue.substring(0,countShowConfirmPass);
 			}
 		}
 	}
 	
function clearPassword(){
 		countShowNewPass = 0;
	 	saveNewPassword = "";
	 	newPasswordStr = "";
	 	countShowConfirmPass = 0;
	 	saveConfirmPassword = "";
	 	confirmPasswordStr = "";
}
function clearPassForIE(){
 	var showNewPassword = document.getElementById("showNewPassword");
	var showNewPasswordValue = showNewPassword.value;
	if(showNewPasswordValue == ""){
		countShowNewPass = 0;
		saveNewPassword = "";
		newPasswordStr = "";
	}
	
	var showConfirmPassword = document.getElementById("showConfirmPassword");
	var showConfirmPasswordValue = showConfirmPassword.value;
	if(showConfirmPasswordValue == ""){
		countShowConfirmPass = 0;
		saveConfirmPassword = "";
		confirmPasswordStr = "";
	}
}
 </script>
 <!-- add by lzg end -->
 
  </head>
  <body onLoad="pageLoad();">
  <table width="100%" border=0 cellspacing=0 cellpadding=0>
    <tr>
      <%--<td width="161"><img src="/cib/images/new_logo.gif" width="161" height="82"></td> 
      <TD width="100%"><img height=82 src="/cib/images/new_red-line.gif" width="100%"></TD>
      --%>
      
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
                  <td><strong> <set:label name="label_ChangePin_1"/> </strong></td>
                </tr>
              </table></td>
              <td width="45%" rowspan="2" valign="top"><table width="400" border="0" cellspacing="0" cellpadding="3">
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
                      <!--<TR>
                        <TD vAlign="top" width="1%">4. </TD>
                        <TD><set:label name="label_FirstLogin_4"/></TD>
                      </TR>
                  --></table></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
              </table>
                <br>
                <br></td>
            </tr>
            <tr>
              <td width="60%" valign="top">
                   <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin-->
                   <!-- <form action="/cib/login.do" method="post" name="form1" target="_top" id="form1"> -->
                    <form action="/cib/login.do?ActionMethod=changePin" method="post" name="form1" target="_top" id="form1">
                    <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end-->
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="login" form="form1" file="login" />                        </td>
                      </tr>
                    </table>
                    <table width="100%" align="center" border="0" cellpadding="5" cellspacing="0">
                      <TR>
                        <TD width="25%" align="right"><set:label name="CIF_NO"/></TD>
                        <TD>
                        <!--<input id="cifNo" name="cifNo" type="text" value="<>" size="20" maxlength="20" style="width:100" autocomplete="off">                        
                        -->
                        <input type = "hidden" name = "cifNo" id = "cifNo" value = "<set:data name="cifNo"/>"/>
                        <set:data name="cifNo"/>  
                        </TD>
                      </TR>
                       <TR>
                        <TD width="25%" align="right"><set:label name="User_Id"/></TD>
                        <TD><set:data name="userId"/>  </TD>
                      </TR>
                      <input id="oldPassword" name="oldPassword" id = "oldPassword" type="hidden" value = "<set:data name='oldPassword'/>">
                      <!--<TR>
                        <TD width="25%" align="right"><set:label name="Initail_PIN"/></TD>
                         modified by lzg 20190521 
                        <TD>
                        	<input id = "showOldPassword" onpaste="return false" oncopy="return false" oncut="return false" type="text" value="" size="20" maxlength="20" style="width:100;-webkit-text-security:disc;text-security:disc;"  autocomplete="off" onkeyup="replaceOldPassword(event)">
                        
                        <set:data name="oldPassword"/>
                        </TD>
                         modified by lzg end 
                      </TR>
                      --><TR>
                        <TD align="right"><set:label name="New_PIN"/></TD>
                        <!-- modified by lzg 20190521 -->
                        <TD>
                        	<input id="newPassword" name="newPassword" type="hidden">
                        	<input id = "showNewPassword" onpaste="return false" oncopy="return false" oncut="return false" type="text" value="" size="20" maxlength="20" style="width:100;-webkit-text-security:disc;text-security:disc;"  autocomplete="off" onkeyup="replaceNewPassword(event)" onkeydown="clearPassForIE()">
                        </TD>
                        <!-- modified by lzg end -->
                      </TR>
                      <TR>
                        <TD align="right"><set:label name="New_PIN_Confirm"/></TD>
                         <!-- modified by lzg 20190521 -->
                        <TD>
                        	<input id="confirmPassword" name="confirmPassword" type="hidden">
                        	<input id = "showConfirmPassword" onpaste="return false" oncopy="return false" oncut="return false" type="text" value="" size="20" maxlength="20" style="width:100;-webkit-text-security:disc;text-security:disc;"  autocomplete="off"  onkeyup="replaceConfirmPassword(event)" onkeydown="clearPassForIE()">
                        </TD>
                        <!-- modfieid by lzg end -->
                      </TR>
					  <set:if name="showChangeScode" value="Y" condition="equals">
					  <TR>
                        <TD width="25%" align="right">&nbsp;</TD>
                        <TD>&nbsp;</TD>
                      </TR>
                      <TR>
                        <TD align="right"><set:label name="Old_S_Code"/></TD>
                        <TD><input id="OldSecurityCode" name="OldSecurityCode" type="password" value="" size="20" maxlength="20" style="width:100" autocomplete="off">                        </TD>
                      </TR>
                      <TR>
                        <TD align="right"><set:label name="New_S_Code"/></TD>
                        <TD><input id="newSecurityCode" name="newSecurityCode" type="password" value="" size="20" maxlength="20" style="width:100" autocomplete="off">                        </TD>
                      </TR>
                      <TR>
                        <TD align="right"><set:label name="New_S_Code_Confirm"/></TD>
                        <TD><input id="confirmSecurityCode" name="confirmSecurityCode" type="password" value="" size="20" maxlength="20" style="width:100" autocomplete="off">                        </TD>
                      </TR>
					  </set:if>
					  <tr align="center">
                        <td height="30" align="center">&nbsp;</td>
                        <td height="30" align="left">
                        <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin-->
                        <!--<input name="buttonChangePin" id="buttonChangePin" type="button" class="button" value="<set:label name='buttonChangePin'/>" tabindex="3" onClick="doSubmit();">-->
                        <input name="buttonChangePin" id="buttonChangePin" type="submit" class="button" value="<set:label name='buttonChangePin'/>" tabindex="3" >
                        <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end-->
                          <input id="buttonReset" name="buttonReset" type="reset" class="button" value="<set:label name='buttonReset'/>" tabindex="4" onclick="clearPassword()">
                          <input id="ActionMethod" name="ActionMethod" type="hidden" value="changePin">
                          <input id="userId" name="userId" type="hidden" value="<set:data name='userId'/>">
						  <input name="checkUpperNum" id="checkUpperNum" type="hidden" value="0">
						  <input name="checkLowerNum" id="checkLowerNum" type="hidden" value="0">
                          
                          <!-- add by long_zg 2015-06-24 for CR210 New policy to BOB/BOL password -->
                          <input name="idNo" type="hidden" id="idNo" value="<set:data name='idNo'/>">
                          <set:singlesubmit/>
						  </td>
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
