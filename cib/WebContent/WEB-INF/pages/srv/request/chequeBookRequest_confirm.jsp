<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.chequeBookRequest">
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
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<script language="javascript" src="/cib/javascript/jsax2.js"></script>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	//var mailName = document.getElementById("Pickuptype").value;<set:data name='Pickuptype'/>
	var mailName = '<set:data name='Pickuptype'/>';
	if("P"==mailName){
		document.getElementById("mailName").style.display = 'none';
		document.getElementById("mailAdress").style.display = 'none';
		}
}
function doSubmit(arg){
	if("confirm"==arg){
		/*if(checkAssignedUser()){
		document.getElementById("form1").submit() ;
		}*/
		
		if(document.getElementById("corpType").value != "3"){
			if(checkAssignedUser()){
			document.getElementById("form1").submit() ;
			}
		}else{
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
					document.getElementById("form1").submit() ;
				}
			}else{
				document.getElementById('otp_tr').style.display = '';
				document.getElementById("submit1").disabled = true;
				document.getElementById("bottonReset").disabled = true;
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
				document.getElementById("form1").submit() ;
			}
		}
	}
	//modified by lzg end
		
	}
	else {
		document.getElementById("ActionMethod").value="addCancel" ;
		document.getElementById("form1").submit() ;
	}
}

function sendOtp(){
	document.getElementById("submit1").disabled = false;
	document.getElementById("bottonReset").disabled = false;
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


</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/chequeBookRequest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.chequeBookRequest" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
    <set:label name="navigationTitleRequest" defaultvalue="BANK Online Banking >Cheque > Cheque Book Request"/>
	<!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" -->
        <set:label name="chequeBookRequestTitle" defaultvalue="CHEQUE BOOK REQUEST"/>
      <!-- InstanceEndEditable --></td>
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
		  <form action="/cib/chequeBookRequest.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            
			<set:messages width="100%" cols="1" align="center"/>
            <set:fieldcheck name="stop_check" form="form1" file="check_request" />
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="CONFIRM_INFORMATION" defaultvalue="CONFIRM_INFORMATION"/></td>
              </tr>
          		<tr>
                <td class="label1" width="28%"><set:label name="Current_Account" defaultvalue="Current Account"/></td>
                <td class="content1" width="72%">
                <set:data name='payCurrency' db="rcCurrencyCBS"/> - <set:data name='accountNo' />
                </td>
              </tr>   
			   <tr>
                <td class="label1"><set:label name="No_Of_Books" defaultvalue="No. Of Books"/></td>
                <td class="content1"><set:data  name='noOfBook' format='amount' pattern='#' /></td>
              </tr><%--
			  <tr>
                <td class="label1"><set:label name="Pickup_Branch" defaultvalue="Pickup Branch"/></td>
                <td class="content1">
                <set:data name='pickupBranchCode' db='branch' />
                </td>
              </tr>
               --%>
             
             <!-- add by lzg 20190606 -->
             <tr>
                <td class="label1" ><set:label name="Pickup_Type_Label" defaultvalue="Pickup Type"/></td>
                <td class="content1"><set:data  name='Pickuptype' rb = "app.cib.resource.srv.pick_up_type" /></td>
              </tr>
             <!-- add by lzg end -->
           
            <tr id="mailName">
                <td class="label1"><set:label name="Local_Mail_Name" defaultvalue="Name"/></td>
                <td class="content1">
                    
                    <set:data name='mailName1'/>
                    
                    <set:data name='mailName2'/>
                    
                </td>
            </tr>
            <tr id="mailAdress">
                <td class="label1"><set:label name="Local_Mail_Adress" defaultvalue="Adress"/></td>
                <td class="content1">
                   
                    <set:data name='mailAdress1'/>
                   
                  
                    <set:data name='mailAdress2'/>
                   
                    <set:data name='mailAdress3'/>
                   
                </td>
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
              		<input name="showSecurityCode" type="password" id="showSecurityCode" class="form-control form-control-custom-2 default-radius"  value="" maxlength="20" autocomplete="off" >
              		<input name = "securityCodeFlag" id = "securityCodeFlag" type = "hidden" value = "Y"/>
              	</td>
              </tr>
              <!-- add by lzg end -->
              
            </table>
            <!-- add by lzg 20190820 -->
            <set:if name = "corpType" condition="notequals" value = "3" >
				<set:assignuser selectFlag="Y" />
			</set:if>
			<!-- add by lzg 20190820 -->
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
			  <td colspan="2" class="sectionbutton">
                <input id="submit1" name="submit1" type="button" value="<set:label name='buttonConfirm' defaultvalue=' confirm ' />" tabindex="3"  onClick="doSubmit('confirm');">
                <input id="bottonReset" name="bottonReset" type="button" value="<set:label name='buttonCancel' defaultvalue='Cancel'/>" onClick="doSubmit('addLoad')";>
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="confirm">
				
				<!-- add by lzg 20190820 -->
				  <input id="corpType" name="corpType" type="hidden" value="<set:data name = 'corpType' />" >
				  <input name="operationType" type="hidden" id="operationType" value = "<set:data name = 'operationType'/>" >
                  <input name="submitFlag" type="hidden" id="submitFlag">
                  <input name = "showMobileNo" id = "showMobileNo" type = "hidden" value = "<set:data name = 'showMobileNo' />" >
                  <input name="smsFlowNo" type="hidden" id="smsFlowNo" value = "<set:data name='smsFlowNo'/>">
                  <input id="txnType" name="txnType" type="hidden" value="<set:data name='txnType'/>">
                  <input id="checkFlag" name="checkFlag" type="hidden" value="<set:data name='checkFlag'/>">
                  <input name="checkOTPFlag" type="hidden" id="checkOTPFlag" value="<set:data name='checkOTPFlag'/>">
				  <!-- add by lzg end -->
				
				<!-- add by lzg 20190624 for pwc-->
					<set:singlesubmit/>
				  <!-- add by lzg end -->
				<input id="Pickuptype" name="Pickuptype" type="hidden" value="<set:data name='Pickuptype'/>">
				<input id="mailN1" name="mailN1" type="hidden" value="<set:data name='mailName1'/>">
				<input id="mailN2" name="mailN2" type="hidden" value="<set:data name='mailName2'/>">
				<input id="mailAdr1" name="mailAdr1" type="hidden" value="<set:data name='mailAdress1'/>">
				<input id="mailAdr2" name="mailAdr2" type="hidden" value="<set:data name='mailAdress2'/>">
				<input id="mailAdr3" name="mailAdr3" type="hidden" value="<set:data name='mailAdress3'/>">
				<!-- add by linrui 20190514 -->
				<input id="accountNo" name="accountNo" type="hidden" value="<set:data name='accountNo' />">
				<input id="AccountCcy" name="payCurrency" type="hidden" value="<set:data name='payCurrency' />">
				<input id="noOfBook" name="noOfBook" type="hidden" value="<set:data name='noOfBook' format='amount' pattern='#'/>">
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
