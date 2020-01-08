<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.time_deposit">
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
}
function doSubmit(arg) {
	if (arg == 'ok') {
		//modified by lzg 20190820
		/*if(checkAssignedUser()){
			document.form1.ActionMethod.value = 'addTimeDepositCfm';
			document.form1.submit();
			setFormDisabled("form1");
			setFieldEnabled("cancel");
		} else {
			alert('Check Assigned User Fail');
		}*/
	if(document.getElementById("corpType").value != "3"){
		if(checkAssignedUser()){
			document.form1.ActionMethod.value = 'addTimeDepositCfm';
			document.form1.submit();
			setFormDisabled("form1");
			setFieldEnabled("cancel");
		} else {
			alert('Check Assigned User Fail');
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
					document.form1.ActionMethod.value = 'addTimeDepositCfm';
					document.form1.submit();
					setFormDisabled("form1");
					setFieldEnabled("cancel");
				}
			}else{
				document.getElementById('otp_tr').style.display = '';
				document.getElementById("ok").disabled = true;
				document.getElementById("return").disabled = true;
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
				document.form1.ActionMethod.value = 'addTimeDepositCfm';
				document.form1.submit();
				setFormDisabled("form1");
				setFieldEnabled("cancel");
			}
		}
	}
	//modified by lzg end
		
		
	} else if (arg == 'cancel') {
		document.form1.ActionMethod.value = 'addTimeDepositLoad';
		document.form1.submit();
		setFormDisabled("form1");
		setFieldEnabled("cancel");
	}
}

//add by lzg 20190820
function sendOtp(){
	document.getElementById("ok").disabled = false;
	document.getElementById("return").disabled = false;
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

function doCancel(){
	setFormDisabled("form1");
	document.getElementById("ActionMethod").value="addTimeDepositCancel";
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/timeDeposit.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.time_deposit" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_new" defaultvalue="MDB Corporate Online Banking > Time Deposit > New Time Deposit"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_new" defaultvalue="NEW TIME DEPOSTIT"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/timeDeposit.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="confirmation" defaultvalue="confirmation"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="debit_account" defaultvalue="debit_account"/></td>
                <td width="72%" class="content1"><set:data name='currentAccountCcy' db="rcCurrencyCBS"/>&nbsp;<set:data name='currentAccount'/>
				 </td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="debit_amount" defaultvalue="debit_amount"/></td>
                <td class="content1"><set:data name='currentAccountCcy' db="rcCurrencyCBS"/>&nbsp;<set:data name='currentAccPrincipal' format="amount"/>
				</td>
              </tr>
              <tr>
                <td class="label1"><set:label name="time_deposit_currency" defaultvalue="time_deposit_currency"/></td>
                <td class="content1"><set:data name='currency' db="rcCurrencyCBS"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="period" defaultvalue="period"/></td>
                <td class="content1">
					<set:data name='term' db="period"/>
				</td>
              </tr>
              <tr>
                <td class="label1"><set:label name="time_deposit_amount" defaultvalue="time_deposit_amount"/></td>
                <td class="content1"><set:data name='currency' db="rcCurrencyCBS"/>&nbsp;<set:data name='principal' format="amount"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="interest_rate" defaultvalue="interest_rate"/></td>
                <td class="content1"><set:data name='DEFAULT_RATE' format='percent' /></td><!-- pattern="#,##0.0000" -->
              </tr>
              <tr>
                <td class="label1"><set:label name="value_date" defaultvalue="value_date"/></td>
                <td class="content1"><set:data name='EFFECTIVE_DATE' format='date'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="maturity_date" defaultvalue="maturity_date"/></td>
                <td class="content1"><set:data name='MATURITY_DATE' format='date'/></td>
              </tr>
              <!-- add by lzg 20191018 -->
              <tr >
                <td class="label1"><set:label name="Expected_Interest" /></td>
                <td class="content1"><set:data name='expectedInterest' format='amount'/></td>
              </tr>
              <!-- add by lzg end -->
              <!-- add by linrui for add Maturity Instruction 20190424 -->
              <tr class="greyline">
                <td class="label1"><set:label name="Maturity_Instruction" defaultvalue="Maturity Instruction"/></td>
                <td class="content1"><set:data name='instCd' rb='app.cib.resource.txn.timedeposit_inst_cd'/></td>
              </tr>
              <!-- end -->
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
                <td height="40" colspan="2" class="sectionbutton">
					<input id="ok" name="ok" type="button" value="&nbsp;&nbsp;<set:label name='confirm' defaultvalue=' confirm '/>&nbsp;&nbsp;" onClick="doSubmit('ok')">
				  	<input id="return" name="return" type="button" value="&nbsp;<set:label name='return' defaultvalue='return'/>&nbsp;" onClick="doCancel()">
				  	<input id="ActionMethod" name="ActionMethod" type="hidden" value="ok">
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
				  	<set:singlesubmit/>
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
