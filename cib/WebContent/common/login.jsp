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
  <title>
  <set:label name="documentTitle_Login"/>
  </title>
  <link href="/cib/css/bootstrap.min.css" rel="stylesheet" type="text/css">
  <link href="/cib/css/light.min.css" rel="stylesheet" type="text/css">
  <!--
  <link href="/cib/css/main.css" rel="stylesheet" type="text/css">
  -->
  <link href="/cib/css/custom.css" rel="stylesheet" type="text/css">
  <link href="/cib/css/login.css" rel="stylesheet" type="text/css">
  <SCRIPT language=JavaScript src="/cib/javascript/common.js"></SCRIPT>
  <SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
  <SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
  <!-- <SCRIPT language=JavaScript src="/cib/javascript/jsax3.1.js"></SCRIPT> -->
  <SCRIPT language=JavaScript src="/cib/javascript/jsax3.2.js?v=20190520"></SCRIPT>
  <script language="JavaScript">
  var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
  if (top.location !== self.location) {
		top.location=self.location;
  } 
<!--hide
var count = 0;
function pageLoad()
{	

	//document.getElementById("creatVcode").style.fontSize = document.getElementById("creatVcode").clientWidth * 20 / 320 + '10px';
	/*if('<set:data name='VerificationCodeFlag'/>' == "Y"){
		document.getElementById("Verification_tr").style.display = "block";
		createCode();
	}*/
	//alert('<set:data name="PageLanguage"/>');
	if(language == "en_US"){
		$("#showHK").show();
		$("#showUS").hide();
		document.getElementById("footerbox_ENG").style.display = "block";
	}else if(language == "zh_HK"){
		$("#showUS").show();
		$("#showHK").hide();
		document.getElementById("footerbox_CHN").style.display = "block";
	}else{
		language = "en_US";
		$("#showHK").show();
		$("#showUS").hide();
		document.getElementById("footerbox_ENG").style.display = "block";
	}
	
	/*if(document.getElementById("OtpWrongFlag").value == "Y"){
		document.getElementById('otpLogin').value = "Y";
		checkback();
	}else{
		savePassword = "";
 		countShowPass = 0;
 		str = "";
		document.getElementById("password").value = "";
		document.getElementById("showPassword").value = "";
	}*/
	//When session invalid，forward to login，jsp，need to show it in top frame.
	if(window.parent.frames.length > 1){
		window.parent.frames['topFrame'].document.getElementById("exitActionFlag").value="existByTimeout";
		 window.parent.location="/cib/login.do?ActionMethod=load&PageLanguage=<set:data name='PageLanguage'/>";//mod by linrui 20190801
	}
	document.getElementById("cifNo").focus();
	showCapsLockWarningMsg('<set:data name="capsLockErrFlag"/>');
	showLogoutMsg('<set:data name="logoutType"/>');

}

function doSubmit()
{	
	if(!(document.getElementById("OtpWrongFlag").value == "Y")){
		document.getElementById("password").value = savePassword;	
	}
	
	if(validate_login(document.getElementById("form1"))){
		if(checkCertFlagBeforeLogin()){
			//add by lzg 20190805
			if(checkVerificationCode()){
				enabled2();
				return false;
			}
			if(document.getElementById('otpLogin').value == ""){
				doLogin('check');
				if("Y" == document.getElementById('otpLogin').value){
					checkback();
					doLogin('send');
					enabled2();
					return false;
				}
			}
			if(document.getElementById("submitFlag").value == "N"){
				document.getElementById("Verification_tr").style.display = "block";
				createCode();
				savePassword = "";
 				countShowPass = 0;
 				str = "";
				document.getElementById("password").value = "";
				document.getElementById("showPassword").value = "";
				document.getElementById("cifNo").value = "";
				document.getElementById("loginId").value = "";
				enabled2();
				return false;
			}
			/*if(checkVerificationCode()){
				return false;
			};*/
			
			//add by lzg end
			if("Y" == document.getElementById('otpLogin').value){
				var smsFlowNo = document.getElementById("smsFlowNo");
				var otp = document.getElementById("otp");
				var OTPWrongMaxFlag = document.getElementById("OTPWrongMaxFlag").value;
				var url = "/cib/jsax?serviceName=OTPConfirmService&operatorFlag=Y&smsFlowNo=" + smsFlowNo.value + "&otp=" + otp.value + "&loginFlag=Y" + "&OTPWrongMaxFlag="+OTPWrongMaxFlag;
				getMsgToElement(url,'', '',null, false,language) ;
				var refreshFlag = document.getElementById("refreshFlag").value;
				if(refreshFlag == "Y"){
					document.getElementById("ActionMethod").value = "load";
					form1.submit();
				}
				var checkOTPFlag = document.getElementById("checkOTPFlag").value;
				if(checkOTPFlag == "N"){
					setFormDisabled("form1");
					return true;
				}else{
					document.getElementById("Verification_tr").style.display = "block";
					document.getElementById("otp").value = "";
					document.getElementById("Verification_Code").value = "";
					createCode();
					enabled2();
					return false;
				}
			}else{
				setFormDisabled("form1");
				return true;
			}
		}
		
		
		
	}
	
	
	enabled2();
	return false ;
}

function checkVerificationCode(){
	if(document.getElementById("VerificationCodeFlag").value == "Y"){
		if(!(document.getElementById("Verification_Code").value.toUpperCase() == document.getElementById("Verification_Code_Confirm").value.toUpperCase())){
			document.getElementById("Verification_Code").value = "";
			alert("<set:label name = 'Verification_Code_Error'/>");
			createCode();
			return true;
		}else{
			return false;
		}
	}
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

function enabled2(){

	
	$("form[id='form1'] :text").attr("disabled",false); 
	$("form[id='form1'] :password").attr("disabled",false); 
	$("form[id='form1'] :submit").attr("disabled",false); 
	$("form[id='form1'] :hidden").attr("disabled",false); 
	$("#form1").attr("disabled",false);
	
}

function getCertCardType(userId, targetId) {
		//registerElement('checkCertFlag');
		//registerElement(targetId);
		//var url = '/cib/jsax?serviceName=CorpUserAxService&targetId=' + targetId + '&userId=' + userId;
		//showMsgToElement(url, targetId, '', null,false);
		//var url = '/cib/jsax?serviceName=CorpUserAxService&targetId=' + targetId + '&userId=' + userId;
		//getMsgToElement(url, targetId, '', null,false,language);
}

function checkBrowser(){
	//if (navigator.appName == 'Microsoft Internet Explorer') {
	if (!!window.ActiveXObject || 'ActiveXObject' in window) {
		return "IE";
	}else{
		return "O";
	}
}
function selCert()
{
	var strTmp;
	var userId;
	userId = document.getElementById("loginId").value;
	if (trim(userId)=='') {
		alert("The field 'Login Id' required to select the user certificate.");
		return false;
	}else{
	
		getCertCardType(userId, "CertModuleList");
		
		//add by Sam 2012-7-30 if user login from non IE,and this user's corpration use cert,reject!
		//check browser
		var browserType=checkBrowser();		
		if(browserType!="IE"){
			//check corpration authenticationMode
			var checkCertFlag = document.getElementById("checkCertFlag").innerHTML;
			if(checkCertFlag=="Y"){
				//return false;//stop
			}
		}
		//end Sam
		
		
		//为了CR145投产，先注释CorpUserAxService,login.jsp/ Sam 2012-11-6
		
		var CertModuleList = document.getElementById("CertModuleList").innerHTML;
		// 20130129
		document.getElementById("CertModuleListString").value=CertModuleList;
		document.applets['CertApplet'].setModuleList(CertModuleList);
		
		//为了CR145投产，先注释CorpUserAxService,login.jsp/ Sam 2012-11-6
	}
	
	retVal = document.applets['CertApplet'].selCert();
	if(retVal !=0){
		return;
	}else{
		document.getElementById("UserCert").value=document.applets['CertApplet'].getCert();
		document.getElementById("certTitle").value=document.applets['CertApplet'].getCertInfo(9);
		
		//为了CR145投产，先注释CorpUserAxService,login.jsp/ Sam 2012-11-6
		document.getElementById("CertCardType").value=document.applets['CertApplet'].getCurrentModule();
		
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
function showLogoutMsg(errFlag){
	if(errFlag == 'T'){
		alert("<set:label name='error.kickedout' rb='app.cib.resource.common.errmsg'/>");
	}else if(errFlag == 'S'){
		alert("<set:label name='err.sessioninvalidate' rb='app.cib.resource.common.errmsg'/>");
	}
}
function login_window(){
window.open("http://www.bank.com.mo/en/bob/SecurityAdvices.htm","","left=20,width=720,height=524,status=yes,scrollbars=yes,resizable=yes");
}

function checkCertFlagBeforeLogin(){

	var userId = document.getElementById("loginId").value;
	
		getCertCardType(userId, "CertModuleList");
		
		//add by Sam 2012-7-30 if user login from non IE,and this user's corpration use cert,reject!
		//check browser
		var browserType=checkBrowser();	
		if(browserType!="IE"){
			//check corpration authenticationMode
			var checkCertFlag = document.getElementById("checkCertFlag").innerHTML;
			if(checkCertFlag=="Y"){
				//return false;//stop
			}
		}
		//end Sam
	return true;
}
//endhide-->
</script>
  <!-- add by long_zg 2014-01-05 for CR202  Application Level Encryption for BOL&BOB begin-->
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
  <!--  Add by long_zg 2019-05-16 for otp login begin  -->
  <script>

 function doLogin(arg){

	var loginId = document.getElementById('loginId').value+'' ;
	var cifNo = document.getElementById('cifNo').value+'' ;
	var smsFlowNo = document.getElementById('smsFlowNo').value+'' ;
	var cifLogin = document.getElementById('cifLogin').value+'' ;
	var tmpCifLogin = trim(cifNo) + trim(loginId) ;
	var submitFlag = document.getElementById('submitFlag').value
	
	
	if(tmpCifLogin != cifLogin){
		var btnSendOtp = document.getElementById('btn_send_otp') ;
		btnSendOtp.value = '<set:label name="SEND_OTP"/>' ;
		btnSendOtp.disabled = false ;
		document.getElementById('smsFlowNo').value = '' ;
	}

	var url= "" ;
	if("check" == arg){
		url = "/cib/jsax?serviceName=loginJsaxService&loginId=" + loginId + "&cifNo="+ cifNo + "&operate=check" + "&password=" + savePassword + "&submitFlag=" + submitFlag + "&PageLanguage=<set:data name='PageLanguage'/>";
		getMsgToElement(url,'', '',null, false,language) ;
	} else if("send" == arg) {
		/*if(checkVerificationCode()){
				return false;
		}*/
		var password = document.getElementById("password").value;
		document.getElementById("OTPSendTip").style.display = "table-row";
		var showMobile = document.getElementById("showMobile").value;
		document.getElementById("MobileContent").innerHTML  = "***" + showMobile.substr(showMobile.length-4);
		if("" == trim(smsFlowNo) || "-1" == trim(smsFlowNo)){
			url = "/cib/jsax?serviceName=loginJsaxService&loginId=" + loginId + "&cifNo="+ cifNo + "&operate=send" + "&password=" + password + "&submitFlag=" + submitFlag  + "&PageLanguage=<set:data name='PageLanguage'/>";
		} else {
			url = "/cib/jsax?serviceName=loginJsaxService&loginId=" + loginId + "&cifNo="+ cifNo + "&password=" + password + "&operate=resend&smsFlowNo=" + smsFlowNo + "&submitFlag=" + submitFlag + "&PageLanguage=<set:data name='PageLanguage'/>";
		}
		
		getMsgToElement(url,'', '', sendback, false,language) ;
		
		//add by lzg 20190805
		var count = 60;
		var timeId = window.setInterval(function(){
			if(count == 1){
				window.clearInterval(timeId);
				document.getElementById("btn_send_otp").value = "<set:label name='RESEND_OTP'/>";
				document.getElementById("btn_send_otp").disabled = false;
			}else{
				document.getElementById("btn_send_otp").disabled = true;
				count--;
				document.getElementById("btn_send_otp").value =count + " " + "<set:label name='RESEND_OTP_SECONDS'/>" + " " +  "<set:label name='RESEND_OTP_SECONDS_LABEL'/>";
			}
		}, 1000);
		//add by lzg end
	} 

	document.getElementById('cifLogin').value = tmpCifLogin ;

 }


	
 function checkback(){
	 var otpLogin = document.getElementById('otpLogin').value ;
	 
	 if('Y' == otpLogin){
		 document.getElementById('otp_tr').style.display = '' ;
		 document.getElementById("buttonLogin").disabled = true;//add by linrui 20190718
		 document.getElementById("buttonLogin").style.background = '#C0C0C0';//add by linrui 20190820
		 ///add by lzg end
		  document.getElementById("cifNo").readOnly = true;
		  document.getElementById("loginId").readOnly = true;
		  document.getElementById("showPassword").readOnly = true;
	 } else {
		 document.getElementById('otp_tr').style.display = 'none' ;
	 }
 }

 function sendback(){
	 document.getElementById("buttonLogin").disabled = false;//add by linrui 20190718
	 document.getElementById("buttonLogin").style.background = '#4A90E2';//add by linrui 20190820
	 var btnSendOtp = document.getElementById('btn_send_otp') ;
	 btnSendOtp.disabled = true ;
	 btnSendOtp.value =60 + " " + "<set:label name='RESEND_OTP_SECONDS'/>" + " " +  "<set:label name='RESEND_OTP_SECONDS_LABEL'/>";

	 var secsStr = '<set:data name="disableTime" />' ;
	 if(null==secsStr || ""==secsStr){
	 	secsStr = "60" ;
	 } 

	 var secs = new Number(secsStr) ;

	 window.setTimeout("enable()", secs * 1000);
 }

 function enable(){
	 var btnSendOtp = document.getElementById('btn_send_otp') ;
	 btnSendOtp.disabled = false ;
 }


 
 </script>
  <!--  Add by long_zg 2019-05-16 for otp login end  -->
  
  <!-- add by lzg -->
  <style type="text/css">  
        .code   
        {   
            background-image:url(code.jpg);   
            font-family:Arial;   
            font-style:italic;   
            color:Red;   
            border:0;   
            padding:2px 3px;   
            letter-spacing:3px;   
            font-weight:bolder;
            -moz-user-select: none; 
    		-webkit-user-select: none; 
    		-ms-user-select: none; 
    		-khtml-user-select: none;
    		user-select: none;   
        }   
        #canvas{
            float:right;
            display: inline-block;
            border:1px solid #ccc;
            border-radius: 5px;
            cursor: pointer;
        }
        #creatVcode:hover{
        	color:#FFA500;
        } 
        #creatVcode{
        	color:#1E90FF;
        }  
    </style> 
    
    <script language="javascript" type="text/javascript">  
     var code;  
     function createCode()   
     { 
     	var show_num = [];
       //create style
       var canvas = document.getElementById("canvas");
       var canvas_width=$('#canvas').width();
       var canvas_height=$('#canvas').height();
       var context = canvas.getContext("2d");
       canvas.width = canvas_width;
       canvas.height = canvas_height;
        var sCode = "A,B,C,E,F,G,H,J,K,L,M,N,P,Q,R,S,T,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0";
        var aCode = sCode.split(",");
        var aLength = aCode.length;
        for (var i = 0; i <= 3; i++) {
            var j = Math.floor(Math.random() * aLength);
            var deg = Math.random() * 30 * Math.PI / 180;
            var txt = aCode[j];
            show_num[i] = txt.toLowerCase();
            var x = 10 + i * 20;
            var y = 20 + Math.random() * 8;
            context.font = "bold 23px 微软雅黑";

            context.translate(x, y);
            context.rotate(deg);

            context.fillStyle = randomColor();
            context.fillText(txt, 0, 0);

            context.rotate(-deg);
            context.translate(-x, -y);
        }
        for (var i = 0; i <= 5; i++) { 
            context.strokeStyle = randomColor();
            context.beginPath();
            context.moveTo(Math.random() * canvas_width, Math.random() * canvas_height);
            context.lineTo(Math.random() * canvas_width, Math.random() * canvas_height);
            context.stroke();
        }
        for (var i = 0; i <= 30; i++) { 
            context.strokeStyle = randomColor();
            context.beginPath();
            var x = Math.random() * canvas_width;
            var y = Math.random() * canvas_height;
            context.moveTo(x, y);
            context.lineTo(x + 1, y + 1);
            context.stroke();
        }
        var codeValue = "";
        for(var i = 0; i < show_num.length; i++){
        	codeValue = codeValue + show_num[i];
        }
       document.getElementById("Verification_Code_Confirm").value = codeValue;
     }
     
     
     function randomColor() {
        var r = Math.floor(Math.random() * 256);
        var g = Math.floor(Math.random() * 256);
        var b = Math.floor(Math.random() * 256);
        return "rgb(" + r + "," + g + "," + b + ")";
    }
  	</script>
  	<!-- addby lzg end -->
  </head>
  <body onLoad="pageLoad();">
  <table width="100%" border=0 cellspacing=0 cellpadding=0>
    <tr>
      <td><div class="login-page" align="center" bgcolor="#FFFFFF" style="overflow-x: auto; overflow-y: auto; ">
          <table width="100%" border=0 cellspacing=0 cellpadding=0 bgcolor="#FFFFFF" height="10%">
            <tr>
              <TD class="login-page-top" height="100%" align="right" style="background-repeat:no-repeat">
              &nbsp;
              <a id="showHK"  href="/cib/login.do?ActionMethod=load&PageLanguage=zh_HK" ><set:label name='LANG_HK' /></a>
              <a id="showUS"  href="/cib/login.do?ActionMethod=load&PageLanguage=en_US" ><set:label name='LANG_ENG' /></a>
              </TD>
            </tr>
          </table>
          <br>
          <table width="40%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 4%;">
              <!--
            <tr>
              <td bgcolor="#FFFFFF"><img src="/cib/images/shim.gif" width="10" height="18"></td>
		      <td width="34" align="right" height="18"><a href="/ebank/bank/login.jsp?lang=C"><img src="/cib/images/button-lang_C.gif" width="34" height="18" border="0"></a></td>
		      <td width="69" height="18"><a href="/ebank/bank/login.jsp?lang=P"><img src="/cib/images/button-lang_P.gif" width="69" height="18" border="0"></a></td>
		      <td><img src="/cib/images/horn.gif" width="19" height="18"></td>
            </tr>
			  -->
            <tr>
              <td align="center"  bgcolor="#EEEEEE"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                  
              <%--  <td><table width="100%" border="0" cellspacing="0" cellpadding="5">--%>
                    <td><table width="760" height="520" border="0" cellspacing="0" cellpadding="5">
                        <tr>
                          <td valign="top" bgcolor="#FFFFFF"><table width="100%" align="center" border="0" cellspacing="10" cellpadding="10">
                              <tr>
                                <td height="40"><div class="login-page-title"><set:label name='documentTitle_main' /></div></td>
                              </tr>
                              <tr>
                                <td height="40"><div class="login-page-sub-title"><set:label name="label_Enter_Pin"/></div></td>
                              </tr>
                            </table></td>
                          <td width="55%" rowspan="2" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="3">
                              <tr>
                                <td class="login-page-sec-advice" colspan="2"><br></td>
                              </tr>                              <tr>
                                <td class="login-page-sec-advice" colspan="2">
                                  <set:label name="label_Login_title2"/></td>
                              </tr>
                                    <TR>
                                      <TD class="login-page-sec-advice" vAlign="top" width="1%">1. </TD>
                                      <TD class="login-page-sec-advice"><set:label name="label_Login_1"/></TD>
                                    </TR>
                                    <TR>
                                      <TD class="login-page-sec-advice" vAlign="top" width="1%">2. </TD>
                                      <TD class="login-page-sec-advice"><set:label name="label_Login_2"/></TD>
                                    </TR>
                                    <TR>
                                      <TD class="login-page-sec-advice" vAlign="top" width="1%">3. </TD>
                                      <TD class="login-page-sec-advice"><set:label name="label_Login_3"/></TD>
                                    </TR>
                                    <TR>
                                      <TD class="login-page-sec-advice" vAlign="top" width="1%">4. </TD>
                                      <TD class="login-page-sec-advice"><set:label name="label_Login_4"/></TD>
                                    </TR>
                             <tr>
                                <td colspan="2" align="center"><br></td>
                              </tr>
                            </table></td>
                        </tr>
                        <tr>
                          <td width="45%" valign="top" bgcolor="#FFFFFF"><form   action="/cib/login.do?ActionMethod=login" method="post" name="form1" target="_top" id="form1" autocomplete="off">
                              <table width="90%" border="0" align="center" cellspacing="0" cellpadding="0">
                                <tr>
                                  <td><set:messages width="100%" cols="1" align="center"/>
                                    <set:fieldcheck name="login" form="form1" file="login" />
                                  </td>
                                </tr>
                              </table>
                              <table width="90%" border="0"  align="center" cellpadding="0" cellspacing="0">
                                <!-- add by lzg for GAPMC-EB-001-0040 -->
                                <tr>
                                  <td height="30"><set:label name="CIF_NO"/>
                                  </td>
                                </tr>
                                <tr>
                                  <td><input id="cifNo" name="cifNo" type="text" value="<set:data name='cifNo' />" class="form-control form-control-custom-2 default-radius"  maxlength="20">
                                  </td>
                                </tr>
                                <!-- add by lzg end -->
                                <tr>
                                  <td height="30"><set:label name="Login_Id"/>
                                  </td>
                                </tr>
                                <tr>
                                  <td><input id="loginId" name="loginId" type="text" value="<set:data name='loginId' />" class="form-control form-control-custom-2 default-radius" maxlength="20">
                                  </td>
                                </tr>
                                <tr>
                                  <td height="30"><set:label name="PIN"/>
                                  </td>
                                </tr>
                                <tr>
                                  <td><input name="text" type="text" id = "showPassword" value = "<set:data name='showPassword' />" class="form-control form-control-custom-2 default-radius"  style="-webkit-text-security:disc;text-security:disc;" onkeydown="clearPassForIE()" onKeyUp="replaceContent(event)" value="" maxlength="20" onpaste="return false" oncopy="return false" oncut="return false"  autocomplete="off"></td>
                                </tr>
                                <tr>
                                  <!-- modified by lzg for pwc not prompt for passwords when logging in -->
                                  <td><input name="password" id="password" value = "<set:data name='password' />" class="form-control form-control-custom-2 default-radius" type="hidden">
                                  </td>
                                  <!-- modified by lzg end -->
                                </tr>
                                <!-- add by lzg -->
                                <tr id='Verification_tr' style="display:none;">
                                	<td align="right" width="100%">
                                		<table width="100%" align="right">
                                			<tr>
                                				<td colspan="3" height="30"><set:label name='Verification_Code'/></td>
                               				 </tr>
                               				 <tr>
                               				 	<td width="40%">
                               				 	<input name="Verification_Code" type="text" id="Verification_Code" class="form-control form-control-custom-2 default-radius"  value="" maxlength="20" autocomplete="off" >
                               				 	</td>
                               				 	<td >
                               				 		<canvas id="canvas"  width = "100%" height = "38"  onclick="createCode();">
                               				 	</td>
                               				 	<td style = "font-size:1vw;color:#338833" >
                               				 		<div>
                               				 		<a id = "creatVcode" href="#" onclick="createCode();" style = "text-decoration: none"><set:label name='Verification_Code_Change'/></a>
                               				 		<input type = "hidden" name = "Verification_Code_Confirm" id = "Verification_Code_Confirm" value = "" />
                               				 		</div>
                               				 	</td>
                               				 </tr>
                                		</table>
                                	</td>
                                </tr>
                                <!-- add by lzg end -->
                                <tr id='otp_tr' style="display:none">
                                  <td align="right" width="100%"><table width="100%" align="right">
                                      <tr>
                                        <td colspan="3" height="30"><set:label name="OTP"/>
                                        </td>
                                      </tr>
                                      <tr style = "display:none" id = "OTPSendTip">
                                      	<td colspan="3" style = "font-size: 10pt;color:rgb(208, 68, 55);">
                                      		<set:label name="SEND_OTP_SUCCESS"/>&nbsp;<span id = "MobileContent"></span>&nbsp;<set:label name="Mobile_no"/>
                                      	</td>
                                      </tr>
                                      <tr>
                                        <td width="50%"><input name="otp" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" type="text" id="otp" class="form-control form-control-custom-2 default-radius"  value="" maxlength="6" autocomplete="off" >
                                        </td>
										<td width="10">&nbsp;
                                        </td>
                                        <td width="200"><input name="button" type="button" id="btn_send_otp"  class="btn btn-block blue-1 submit" onClick="doLogin('send')" value=" <set:label name='SEND_OTP'/> " />
                                        </td>
                                      </tr>
                                    </table></td>
                                </tr>
                                <tr><td style="height:30px;border-bottom:1px solid #dcdcdc"></td></tr>
                                <tr>
                                  <td align="right" width="100%"><table width="100%" align="right">
                                      <tr>
                                        <td width="50%">
                                        </td>
										<td width="10">&nbsp;
                                        </td>
                                        <td width="200"><input name="buttonLogin" id="buttonLogin" type="submit" class="btn btn-block mt-30 blue-1 submit" value="<set:label name='buttonLogin'/>" tabindex="3" />
                                        </td>
                                      </tr>
                                    </table></td>
                                </tr>
                                <tr>
                                <tr align="center">
                                <td>
                                <!-- 
                                  <td height="30" align="left"><input id="buttonReset" name="buttonReset" type="reset" class="btn btn-block btn-lg mt-30 blue-1 submit" value="<set:label name='buttonReset'/>" tabindex="4">
                                 -->
                                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="login">
                                    <input id="UserCert" name="UserCert" type="hidden">
                                    <input id="CertCardType" name="CertCardType" type="hidden">
                                    <!--<input id="CertModuleList" name="CertModuleList" type="hidden">-->
                                    <input id="PageLanguage" name="PageLanguage" type="hidden" value="<set:data name='PageLanguage'/>">
                                    <input name="capsLockOnFlag" type="hidden" id="capsLockOnFlag" value="N">
                                    <!-- add by long_zg 2019-05-16 for otp login begin -->
                                    <input name="smsFlowNo" type="hidden" id="smsFlowNo" value="">
                                    <input name="otpLogin" type="hidden" id="otpLogin" value="<set:data name='otpLogin'/>">
                                    <input name="cifLogin" type="hidden" id="cifLogin" value="">
                                    <!-- add by long_zg 2019-05-16 for otp login end -->
                                    <!-- add by lzg 20190810 -->
                                    <input name="submitFlag" type="hidden" id="submitFlag" value="<set:data name='submitFlag'/>">
                                    <input name="showMobile" type="hidden" id="showMobile" value="">
                                    <input name="VerificationCodeFlag" type="hidden" id="VerificationCodeFlag" value="<set:data name='VerificationCodeFlag'/>">
                                    <input name="OtpWrongFlag" type="hidden" id="OtpWrongFlag" value="<set:data name='OtpWrongFlag'/>">
                                    <input name="checkOTPFlag" type="hidden" id="checkOTPFlag" value="<set:data name='checkOTPFlag'/>">
                                    <input name="OTPWrongMaxFlag" type="hidden" id="OTPWrongMaxFlag" value="<set:data name='OTPWrongMaxFlag'/>">
                                    <input name="refreshFlag" type="hidden" id="refreshFlag" value="<set:data name='refreshFlag'/>">
                                    <!-- add by lzg end -->
                                    <!--<input id="checkCertFlag" name="checkCertFlag" type="hidden">
                          <input id="CertModuleList" name="CertModuleList" type="hidden">
						  -->
                                    <input id="CertModuleListString" name="CertModuleListString" type="hidden" value="<set:data name='certModuleListString'/>">
                                     <div id="checkCertFlag" name="checkCertFlag" style="display:none"></div>
                                    <div id="CertModuleList" name="CertModuleList" style="display:none"></div>
                                    </td>
                                </tr>
                              </table>
                            </form>
                                                                <br><br><br>
                                                                </td>
                        </tr>
                      </table></td>
                </table></td>
            </tr>
          </table>
        </div></td>
    </tr>
  </table>
  <div id="footerbox_ENG" class="login-page-footer" style = "display:none">
        <a href="javascript:void(0);" onclick="window.open('/cib/common/MDB_Limited.jsp');" target="_parent"><set:label name="TERMS_OF_USE"/></a> &nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp; 
        <a href="javascript:void(0);" onclick="window.open('/cib/common/MDB_Important_Statement.jsp');" target="_top"><set:label name="Important_Statement"/></a> &nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp; 
        <a href="javascript:void(0);" onclick="window.open('/cib/common/MDB_Data_Protection.jsp');" target="_top"><set:label name="Data_Protection_Statement"/></a>&nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp; 
        <a href="javascript:void(0);" onclick="window.open('/cib/common/MDB_IPR_Statement.jsp');" target="_top"><set:label name="IPR_Statement"/></a> 
        <br><set:label name="COPYRIGHT"/>
        <span style="right:20px;position: absolute;"><set:label name='RECOMMENDED_SCREEN_RESOLUTION' /></span>
  </div>
  
  <div id="footerbox_CHN" class="login-page-footer" style = "display:none">
        <a href="javascript:void(0);" onclick="window.open('/cib/common/MDB_Limited_ZH.jsp');" target="_parent"><set:label name="TERMS_OF_USE"/></a> &nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp; 
        <a href="javascript:void(0);" onclick="window.open('/cib/common/MDB_Important_Statement_ZH.jsp');" target="_top"><set:label name="Important_Statement"/></a> &nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp; 
        <a href="javascript:void(0);" onclick="window.open('/cib/common/MDB_Data_Protection_ZH.jsp');" target="_top"><set:label name="Data_Protection_Statement"/></a>&nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp; 
        <a href="javascript:void(0);" onclick="window.open('/cib/common/MDB_IPR_Statement_ZH.jsp');" target="_top"><set:label name="IPR_Statement"/></a> 
        <br><set:label name="COPYRIGHT"/>
        <span style="right:20px;position: absolute;"><set:label name='RECOMMENDED_SCREEN_RESOLUTION' /></span>
  </div>
  </body>
</set:loadrb>
</html>
