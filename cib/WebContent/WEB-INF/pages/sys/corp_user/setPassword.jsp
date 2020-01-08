<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.sys.corp_user">
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
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax3.2.js?v=20190520"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
//----------add by xyf 20081219 begin--------------
var pin_min = parseInt(<set:data name='pinMin' />);
var pin_max = parseInt(<set:data name='pinMax' />);
var pin_upper = parseInt(<set:data name='pinUpper' />);
var pin_lower = parseInt(<set:data name='pinLower' />);
var pin2_min = parseInt(<set:data name='pin2Min' />);
var pin2_max = parseInt(<set:data name='pin2Max' />);
var pin2_upper = parseInt(<set:data name='pin2Upper' />);
var pin2_lower = parseInt(<set:data name='pin2Lower' />);
var corp_type = parseInt(<set:data name='corpType' />);
var hasMobile = "<set:data name="hasMobile" />";
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

function getKey(Eve){
	if (Eve.keyCode == 13) {
             document.getElementById("securityCode").value = document.getElementById("showSecurityCode").value;
    }
}

function checkPin() {
	var newPinValue = document.form1.newPassword.value;
	var checkUpperNum = checkChar(newPinValue,'upper');
	var checkLowerNum = checkChar(newPinValue,'lower');
	document.form1.checkUpperNum.value = checkUpperNum;
    document.form1.checkLowerNum.value = checkLowerNum;
/*	var currentLen = 6;
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
function pageLoad(){
	if (hasMobile=="N") {
		document.getElementById("submit1").disabled = true;
	}
	var flag = "<set:data name = 'secErrorFlag' />";
	if(flag == "Y"){
		document.getElementById("oldPassword").focus();
		document.getElementById("newPassword").focus();
		document.getElementById("confirmPassword").focus();
	}
}
function doSafe(){//CR202 20151126 chen_y
  setFormDisabled("form1");
  return true;	
}
function doSubmit()
{
	var otp_flag = "<set:data name='OtpFlag' />";
	 checkPin();
	 if(validate_setPassword(document.getElementById("form1"))){
		 var checkFlag = "<set:data name = 'checkFlag'/>";
		if(checkFlag == "C"){
			var submitFlag = document.getElementById("submitFlag").value;
			if(submitFlag == "Y"){
				var smsFlowNo = document.getElementById("smsFlowNo");
				var otp = document.getElementById("otp");
				var url = "/cib/jsax?serviceName=OTPConfirmService&operatorFlag=Y&smsFlowNo=" + smsFlowNo.value + "&otp=" + otp.value;
				getMsgToElement(url,'', '',null, false,language) ;
				var checkOTPFlag = document.getElementById("checkOTPFlag").value;
				if(checkOTPFlag == "N"){
					document.getElementById('buttonConfirm1').click();
					setFormDisabled("form1");
					return true;
				}
			}else{
				document.getElementById('otp_tr').style.display = '';
				document.getElementById("submit1").disabled = true;
				document.getElementById("submitFlag").value = "Y";
				sendOtp();
			}
		}else if(checkFlag == "S"){
			var securityCodeFlag = document.getElementById("securityCodeFlag").value;
			if(securityCodeFlag == "Y"){
				document.getElementById("sec_tr").style.display = '';
				document.getElementById("securityCodeFlag").value = 'N';
				return false;
			}else{
				document.getElementById('buttonConfirm1').click();
				setFormDisabled("form1");
				return true;
			}
		}
	 }else{
		 return false;
	 }
	
}


//add by lzg 20190820
function sendOtp(){
	document.getElementById("submit1").disabled = false;
	var operationType = document.getElementById("operationType");
	var txnType = document.getElementById("txnType");
	var smsFlowNo = document.getElementById("smsFlowNo");
	
	var btnSendOtp = document.getElementById('btn_send_otp');
	btnSendOtp.disabled = true ;
	btnSendOtp.value =60 + " " + "<set:label name='RESEND_OTP_SECONDS' rb = 'app.cib.resource.sys.login'/>" + " " +  "<set:label name='RESEND_OTP_SECONDS_LABEL' rb = 'app.cib.resource.sys.login'/>";
	
	var url = "/cib/jsax?serviceName=OTPConfirmService&approveType=A&txnType=" + txnType.value + "&operationType=" + operationType.value + "&smsFlowNo=" + smsFlowNo.value;
	getMsgToElement(url,'', '',null, false,language) ;
	document.getElementById("OTPSendTip").style.display = '';
	var showMobile = document.getElementById("showMobileNo").value;
	document.getElementById("MobileContent").innerHTML  = "****" + showMobile.substr(showMobile.length-4);
	var count = 60;
		var timeId = window.setInterval(function(){
			if(count == 1){
				window.clearInterval(timeId);
				btnSendOtp.value = "<set:label name='RESEND_OTP' rb = 'app.cib.resource.sys.login'/>";
				btnSendOtp.disabled = false;
			}else{
				btnSendOtp.disabled = true;
				count--;
				btnSendOtp.value =count + " " + "<set:label name='RESEND_OTP_SECONDS' rb = 'app.cib.resource.sys.login' />" + " " +  "<set:label name='RESEND_OTP_SECONDS_LABEL' rb = 'app.cib.resource.sys.login' />";
			}
		}, 1000);
		
}
//add by lzg 20190820

var popUpWin=0;
function popUpWindow(URLStr, left, top, width, height)
{
  if(popUpWin)
  {
    if(!popUpWin.closed) popUpWin.close();
  }
  getMsgToElement('/cib/jsax?serviceName=TimeOutService', '', "", "", false,language) ;
  //var timeOutFlag = document.getElementById("timeOutFlag").innerHTML;
  var errMsg = document.all[chkValidationErrorMessageMarker].innerHTML;
  if(errMsg.indexOf('Session invalidate.',0)>0){
		return;  
  }
  popUpWin = open(URLStr, 'popUpWin', 'toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbar=yes,resizable=no,copyhistory=yes,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
}
function setSecurityCode(code){
	document.getElementById('securityCode').value=code;
	//setFormDisabled("form1");
	//document.form1.submit();
	document.getElementById('buttonConfirm1').click(); // CR202 20151126 chen_y 
	setFormDisabled("form1");
}
<!--update by liang_ly for CR204 2015-4-9-->
function setOtpCode(otp,smsFlowNo,exceedResend){
	
	document.getElementById("otp").value=otp;
	document.getElementById("smsFlowNo").value = smsFlowNo;
	document.getElementById("exceedResend").value=exceedResend;
	//alert(otp);
	document.getElementById('buttonConfirm1').click(); 
	setFormDisabled("form1");
	//document.form1.submit();
}
/*
<!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end-->
function enabled(){
	
	$("form[id='form1'] :text").attr("disabled",false); 
	$("form[id='form1'] :password").attr("disabled",false); 
	$("form[id='form1'] :submit").attr("disabled",false); 
	$("form[id='form1'] :hidden").attr("disabled",false); 
	$("#form1").attr("disabled",false); 
	var options ={
			beforeEncryption: doSafe
			//submitEvent:"submit"
			};
	$("#form1").jCryption(options);
	
}*/

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
			beforeEncryption: doSafe,
			getKeysURL: "/cib/EncryptionServlet?getPublicKey=true",
    		handshakeURL: "/cib/EncryptionServlet?handshake=true"
			};
		$("#form1").jCryption(options);
		//$("#form1").jCryption();
	});
</script>
 <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end-->
 
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpUser.do?ActionMethod=setPassword" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.corp_user" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_pin" defaultvalue="MDB Corporate Online Banking > Services > Change Password"/><!-- InstanceEndEditable --></td>
    <td class="buttonprint" style="background-image:url(images/button-print_<%=session.getAttribute("Locale$Of$Neturbo")%>.gif)"><a href="#" onClick="printPage('pageheader');"><img src="/cib/images/shim.gif" width="61" height="18" border="0"></a></td>
	<!--
    <td class="buttonhelp"><a href="#"><img src="/cib/images/shim.gif" width="36" height="18" border="0"></a></td>
	-->
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="EDC64A">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="white">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="40">
  <tr>
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_pin" defaultvalue="CHANGE Password"/><!-- InstanceEndEditable --></td>
    <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="1" bgcolor="white"><img src="/cib/images/shim.gif" width="1" height="1"></td>
        </tr>
      </table></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="19" class="topborderlong"><img src="/cib/images/table_top_long.gif" width="100%" height="19"></td>
  </tr>
  <tr>
    <td align="center" bgcolor="#999999"><table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
        <tr>
          <td width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
          <td>
		  <form action="/cib/corpUser.do?ActionMethod=setPassword" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="setPassword" form="form1" file="set_password" />                        </td>
                      </tr>
                    </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="Fill_Info" defaultvalue="Fill the following Information"/></td>
              </tr>
              <tr>
                	<!--update by liang_ly for CR204 2015-4-9-->
              <tr>
                <td width="28%" class="label1"><span class = "xing">*</span><set:label name="Old_PIN" defaultvalue="Old Password"/></td>
                <td width="72%" class="content1"><input type="password" style="display:none"><input id="oldPassword" name="oldPassword" type="text" value="<set:data name='oldPassword'/>" size="20" maxlength="20" onfocus="this.type='password';" autocomplete="off"></td>
              </tr> 
              	<!--update by liang_ly for CR204 2015-4-9 end-->
              </tr>
			  <tr class="greyline">
                <td class="label1"><span class = "xing">*</span><set:label name="New_PIN" defaultvalue="New Password "/></td>
                <td class="content1"><input type="password" style="display:none"><input id="newPassword" name="newPassword" type="text" value="<set:data name='newPassword'/>" size="20" maxlength="20" onfocus="this.type='password';" autocomplete="off"></td>
              </tr>
              <tr>
                <td class="label1"><span class = "xing">*</span><set:label name="New_PIN_Confirm" defaultvalue="New Password Confirm"/></td>
                <td class="content1"><input type="password" style="display:none"><input id="confirmPassword" name="confirmPassword" type="text" value="<set:data name='confirmPassword'/>" size="20" maxlength="20" onfocus="this.type='password';" autocomplete="off"></td>
              </tr>
            </table>
			 <table width="100%" border="0" cellpadding="0" cellspacing="0">
               <tr>
                 <td class="groupseperator">&nbsp;</td>
               </tr>
               
                <!-- add by lzg 20190819 -->
              <tr id='otp_tr' style = "display: none;">
              	<td class="content1">
              		<set:label name="OTP" rb = "app.cib.resource.sys.login"/>&nbsp;&nbsp;&nbsp;
              		<br>
              		<div id = "OTPSendTip" style = "font-size: 1vw;color:rgb(208, 68, 55);display:none;">
              			<set:label name="SEND_OTP_SUCCESS" rb = "app.cib.resource.sys.login"/>&nbsp;<span id = "MobileContent"></span>&nbsp;<set:label name="Mobile_no" rb = "app.cib.resource.sys.login"/>
              		</div>
              		<input name="otp" type="text" id="otp" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" class="form-control form-control-custom-2 default-radius"  value="" maxlength="6" autocomplete="off" >
              		<input name="button" type="button" id="btn_send_otp" onClick="sendOtp()" value=" <set:label name='SEND_OTP' rb = "app.cib.resource.sys.login" /> " />
              		<br><span style = "font-size: 1vw;color:rgb(208, 68, 55);"><set:label name="otp_info_1" rb = "app.cib.resource.common.otp"/></span>
              	</td>
              </tr>
              <tr id='sec_tr' style = "display: none;">
              	<td class="content1">
              		<set:label name="Security_Code" rb = "app.cib.resource.sys.login"  />&nbsp;&nbsp;&nbsp;
              		<input onkeypress="getKey(event);" name="showSecurityCode" type="password" id="showSecurityCode" class="form-control form-control-custom-2 default-radius"  value="" maxlength="20" autocomplete="off" >
              		<input name = "securityCodeFlag" id = "securityCodeFlag" type = "hidden" value = "Y"/>
              	</td>
              </tr>
              <!-- add by lzg end -->
               
             </table>
			 <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
                <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin-->
				<input id="submit1" name="submit1" type="button" value="<set:label name='buttonChangePIN' defaultvalue=' Change ' />"  onClick="doSubmit();">
               <!-- <input id="submit1" name="submit1" type="submit" value="<set:label name='buttonChangePIN' defaultvalue=' Change ' />">-->
                 <!--//CR202 20151126 chen_y begin-->
                <input id="buttonConfirm1" name="buttonConfirm1" style="display:none;"  type="submit" value="<set:label name='buttonConfirm' defaultvalue='Confirm'/>">
                <!--//CR202 20151126 chen_y end-->
                <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end-->
<!--                  <input id="bottonReset" name="bottonReset" type="reset" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
				  <input id="ActionMethod" name="ActionMethod" type="hidden" value="setPassword">			
				  <input name="checkUpperNum" id="checkUpperNum" type="hidden" value="0">
				  <input name="checkLowerNum" id="checkLowerNum" type="hidden" value="0">
                   <!-- add by long_zg 2015-06-24 for CR210 New policy to BOB/BOL password  -->
                  <input name="idNo" id="idNo" type="hidden" value="<set:data name='idNo' />">
                  <input name="userId" id="userId" type="hidden" value="<set:data name='userId' />">
                  <!--update by liang_ly for CR204 2015-4-9-->
                   <input name="securityCode" type="hidden" id="securityCode" value="<set:data name='securityCode'/>">
                 
                  <input name="smsFlowNo" type="hidden" id="smsFlowNo"  value = ""/>
                  <input name="exceedResend" type="hidden" id="exceedResend" value="<set:data name='exceedResend'/>">    
                  
                  <!-- add by lzg 20190820 -->
				  <input id="corpType" name="corpType" type="hidden" value="<set:data name = 'corpType' />" >
				  <input name="operationType" type="hidden" id="operationType" value = "<set:data name = 'operationType'/>" >
                  <input name="submitFlag" type="hidden" id="submitFlag">
                  <input name = "showMobileNo" id = "showMobileNo" type = "hidden" value = "<set:data name = 'showMobileNo' />" >
                  <input id="txnType" name="txnType" type="hidden" value="<set:data name='txnType'/>">
                  <input id="checkFlag" name="checkFlag" type="hidden" value="<set:data name='checkFlag'/>">
                  <input name="checkOTPFlag" type="hidden" id="checkOTPFlag" value="<set:data name='checkOTPFlag'/>">
				  <!-- add by lzg end -->
                   
				  </td>
              </tr>
            </table>
<!-- InstanceEndEditable -->
		  </form>
		  </td>
          <td width="1%"><img src="/cib/images/shim.gif" width="10" height="1"></td>
      </table></td>
    <td align="right" width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="999999">
    <td><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr>
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="1"></td>
  </tr>
  <tr bgcolor="white">
    <td colspan="3"><img src="/cib/images/shim.gif" width="1" height="2"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
  </tr>
</table>
</body>
</set:loadrb>
<!-- InstanceEnd --></html>
