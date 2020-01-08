<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.flow.approve">
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
<script language="javascript" src="/cib/javascript/jsax3.2.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doMultiApprove(){
	var hasMobile = "<set:data name='hasMobile' />";
	var corp_type = parseInt(<set:data name='corpType' />);
	var otp_flag = "<set:data name='OtpFlag' />";//temp otp
  if(!validate_confirm(document.getElementById("form1"))){
    return false;
  }
	document.form1.act.value = "A";
	var retVal = 0;

	if(retVal == 0){
		if('<set:data name="CheckSecurityCodeFlag"/>'=='Y'){
			/*var intLeft = (screen.width-520)/2;
			var intTop = (screen.height-200)/2;
			var popWidth = 520;
			var popHeight = 200;
			if (corp_type==3) {
				if (hasMobile=="N") {
					setFormDisabled("form1");
					document.form1.action = "/cib/approve.do?ActionMethod=unavailableMultiApprove";
					//CR202 20151126 chen_y
		    		document.form1.submit();
		    		return;
				}
				popHeight = 260;
				popUpWindow('/cib/approve.do?ActionMethod=inputCodeLoad&approveType=M', intLeft, intTop, popWidth, popHeight);
			}else{
				popUpWindow('/cib/approve.do?ActionMethod=inputCodeLoad', intLeft, intTop, popWidth, popHeight);
			}*/
			
			//add by lzg 20190820
			var securityCodeFlag = document.getElementById("securityCodeFlag").value;
			if(securityCodeFlag == "Y"){
				document.getElementById("sec_tr").style.display = '';
				document.getElementById("securityCodeFlag").value = 'N';
				return false;
			}else{
				document.getElementById("securityCode").value = document.getElementById("showSecurityCode").value;
				setFormDisabled("form1");
			    document.form1.action = "/cib/approve.do?ActionMethod=multiApprove";
			    document.form1.submit();
			}
			//add by lzg end
			
		}else{
			if(otp_flag == 'Y'){
				/*setFieldDisabled("buttonMultiApprove");
				var popWidth = 520;
				var popHeight = 200;
				var intLeft = (screen.width-520)/2;
				var intTop = (screen.height-200)/2;
				popHeight = 260;
				popUpWindow('/cib/approve.do?ActionMethod=inputCodeLoad&approveType=M', intLeft, intTop, popWidth, popHeight);*/
				//add by lzg 20190820
			var submitFlag = document.getElementById("submitFlag").value;
			if(submitFlag == "Y"){
				var smsFlowNo = document.getElementById("smsFlowNo");
				var otp = document.getElementById("otp");
				var url = "/cib/jsax?serviceName=OTPConfirmService&operatorFlag=Y&smsFlowNo=" + smsFlowNo.value + "&otp=" + otp.value;
				getMsgToElement(url,'', '',null, false,language) ;
				var checkOTPFlag = document.getElementById("checkOTPFlag").value;
				if(checkOTPFlag == "N"){
					setFormDisabled("form1");
		    		document.form1.action = "/cib/approve.do?ActionMethod=multiApprove";
		    		document.form1.submit();
				}
			}else{
				document.getElementById('otp_tr').style.display = '';
				document.getElementById("submitFlag").value = "Y";
				document.getElementById("buttonMultiApprove").disabled = true;
				document.getElementById("buttonCancel").disabled = true;
				sendOtp();
			}
			//add by lzg end
	    	}else{
		    	setFormDisabled("form1");
				document.form1.action = "/cib/approve.do?ActionMethod=multiApprove";//CR202 20151126 chen_y
				document.form1.submit();
			}
		
		}
	}
}


function getKey(Eve){
	if (Eve.keyCode == 13) {
             document.getElementById("securityCode").value = document.getElementById("showSecurityCode").value;
    }
}

//add by lzg 20190820
function sendOtp(){
	
	document.getElementById("buttonMultiApprove").disabled = false;
	document.getElementById("buttonCancel").disabled = false;

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
//add by lzg end

function doMultiExecute(){
    setFormDisabled("form1");
	document.form1.action = "/cib/approve.do?ActionMethod=multiApprove";//CR202 20151126 chen_y
	document.form1.act.value = "E";
    document.form1.submit();
}
function doCancel(){
     //document.form1.ActionMethod.value = "cancelMultiApprove";
	 document.form1.action = "/cib/approve.do?ActionMethod=cancelMultiApprove";//CR202 20151126 chen_y
	 setFormDisabled("form1");
	 document.form1.submit();
}
function doSubmit(){
  document.form1.action = "/cib/approve.do?ActionMethod=multiApprove";
  setFormDisabled("form1");
  return true;	
}

function addSignFields(){
	var fieldsStr = "act|workId|txnType";
	var fieldArray = fieldsStr.split("|");
	var dataToBeAdd = "";
	for(var i=0;i<fieldArray.length;i++){
		var fieldValue = document.getElementById(fieldArray[i]).value;
		if(i>0){
			dataToBeAdd = dataToBeAdd + "|";
		}
		dataToBeAdd = dataToBeAdd + fieldValue;
	}
	return dataToBeSigned;
}

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
  popUpWin = open(URLStr, 'popUpWin', 'toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbar=no,resizable=yes,copyhistory=no,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
  //add by linrui 20190710
  parent.setOtpWIndows(popUpWin);
  //setTimeout("autoclose();",300000);
}
//add by linrui 20190710
/*function autoclose()  
{  
	if(popUpWin)
	  {
	    if(!popUpWin.closed) popUpWin.close();
	  }
} */
//add by linrui 20190710 end
function setSecurityCode(code){
	document.getElementById('securityCode').value=code;
	//setFormDisabled("form1");
	document.getElementById('buttonConfirm1').click(); // CR202 20151126 chen_y
	//document.form1.submit();
}
<!--update by liang_ly for CR204 2015-4-9-->
function setOtpCode(otp,smsFlowNo,exceedResend){
	
	document.getElementById("otp").value=otp;
	document.getElementById("smsFlowNo").value = smsFlowNo;
	document.getElementById("exceedResend").value=exceedResend;
	//alert(otp);
	setFormDisabled("form1");
	document.form1.submit();
	//alert(111);
}
</script>
<!-- add by chen_y 2015-11-26 for CR202  Application Level Encryption for BOB begin-->
<script type="text/javascript" src="/cib/javascript/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="/cib/javascript/jquery.jcryption.3.1.0.js?v=20150105"></script>
<script type="text/javascript"><%--
	$(function() {
		var options ={
			beforeEncryption: doSubmit,
			getKeysURL: "/cib/EncryptionServlet?getPublicKey=true",
    		handshakeURL: "/cib/EncryptionServlet?handshake=true"
			};
		$("#form1").jCryption(options);
		//$("#form1").jCryption();
	});
--%></script>
 <!-- add by chen_y 2015-11-26 for CR202  Application Level Encryption for BOB end-->
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/approve.do?ActionMethod=multiApprove" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.flow.approve" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
        <set:label name="navigationTitle_multilist" defaultvalue="MDB Corporate Online Banking > Authorization > Multi Approval List"/>
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
        <set:label name="functionTitle_multilist" defaultvalue="MULTI APPROVE LIST"/>
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
		  <form action="/cib/approve.do?ActionMethod=multiApprove" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<set:if name="SignDataFlag" condition="EQUALS" value="Y">
			<div style="display:none">
			<applet  code="app.cib.cert.applet.CertApplet.class" name="CertApplet" width="0" height="0" align="absmiddle" MAYSCRIPT archive="/cib/applet/CertApplet_1_7.jar" id="CertApplet">
            </applet>
			</set:if>
		  <input id="UserCert" type="hidden" name="UserCert" >
			</div>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td>
						<set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="approve_confirm_multi" form="form1" file="approve_confirm_multi" />
						</td>
                      </tr>
                </table>
				<table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td class="listheader1"><set:label name="Proc_Create_Time" defaultvalue="Request Time"/></td>
                    <td class="listheader1"><set:label name="Txn_Type" defaultvalue="Type"/></td>
                    <td class="listheader1"><set:label name="Proc_Creator_Name" defaultvalue="Requestor"/></td>
                    <td class="listheader1"><set:label name="Trans_Desc" defaultvalue="Description"/></td>
                    <set:if name= "FinanceFlag" condition="equals" value = "Y">
					<td align="center" class="listheader1"><set:label name="Currency" defaultvalue="CCY"/></td>
                    <td align="right" class="listheader1"><set:label name="Amount" defaultvalue="Amount"/></td>
                    </set:if>
					<td align="center" class="listheader1"><set:label name="Approve_Include" defaultvalue="Can Approve"/></td>
                  </tr>
                  <set:list name="selectedWorkList" showNoRecord="YES">
                    <tr class="<set:listclass class1='' class2='greyline' />">
                      <td class="listcontent1"><set:listdata name="procCreateTime" format='datetime'/>
<input id="workId" name="workId" type="hidden" value="<set:listdata name='workId'/>">
<input id="signatureValue" type="hidden" name="signatureValue" >
<input id="dataToBeSigned" type="hidden" name="dataToBeSigned" >
<input id="_fieldsToBeSigned" type="hidden" name="_fieldsToBeSigned">
                      </td>
                      <td class="listcontent1"><set:listdata name="txnType" rb="app.cib.resource.common.subtype"/>
<input id="txnType" name="txnType" type="hidden" value="<set:listdata name='txnType'/>">
                      </td>
                      <td class="listcontent1"><set:listdata name="procCreatorName"/></td>
                      <td class="listcontent1"><set:listdata name="transDesc"/>
<input id="transDesc" name="transDesc" type="hidden" value="<set:listdata name='transDesc'/>">
					  </td>
					  <set:if name= "FinanceFlag" condition="equals" value = "Y">
                      <td align="center" class="listcontent1">
					  <set:listif name="ruleFlag" condition="equals" value="0">	  <set:listdata name="currency" db="rcCurrencyCBS"/>
 <input id="currency" name="currency" type="hidden" value="<set:listdata name='currency'/>">					 <input id="toCurrency" name="toCurrency" type="hidden" value="">					
 </set:listif>
<set:listif name="ruleFlag" condition="equals" value="1">
 <set:listdata name="toCurrency" db="rcCurrencyCBS"/>
 <input id="currency" name="currency" type="hidden" value="">					
 <input id="toCurrency" name="toCurrency" type="hidden" value="<set:listdata name='toCurrency'/>">					</set:listif>
                        &nbsp;</td>
				<td align="right" class="listcontent1">
                <!-- modify by long_zg 2014-01-08 for CR192 bob batch begin -->
                <!--<set:listif name="ruleFlag" condition="equals" value="0">
				<set:listdata name="amount" format="amount"/>
 <input id="toAmount" name="toAmount" type="hidden" value="">					
 <input id="amount" name="amount" type="hidden" value="<set:listdata name='amount' format='amount'/>">									</set:listif>
				<set:listif name="ruleFlag" condition="equals" value="1">
				<set:listdata name="toAmount" format="amount"/>
 <input id="amount" name="amount" type="hidden" value="">					
 <input id="toAmount" name="toAmount" type="hidden" value="<set:listdata name='toAmount' format='amount'/>">									</set:listif></td>
					  </set:if>-->
                <set:listif name="txnType" condition="equals" value="AUTOPAY_ADD">
                    <set:listif name='amount' condition='equals' value='9.999999999E9'>
                        <set:label name="FULL_PAYMENT" defaultvalue="Full Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                    </set:listif>
                    <set:listif name='amount' condition='notequals' value='9.999999999E9'>
                        <set:listif name='amount' condition='equals' value='0.0'>
                            <set:label name="Minimum_Payment" defaultvalue="Minimum Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                        </set:listif>
                        <set:listif name='amount' condition='notequals' value='0.0'>
                            <set:listdata name='amount' format='amount' />
                        </set:listif>
                    </set:listif>
                    <input id="toAmount" name="toAmount" type="hidden" value="">					
 					<input id="amount" name="amount" type="hidden" value="<set:listdata name='amount' format='amount'/>">
                </set:listif>
                <set:listif name="txnType" condition="equals" value="AUTOPAY_EDIT">
                    <set:listif name='amount' condition='equals' value='9.999999999E9'>
                        <set:label name="FULL_PAYMENT" defaultvalue="Full Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                    </set:listif>
                    <set:listif name='amount' condition='notequals' value='9.999999999E9'>
                        <set:listif name='amount' condition='equals' value='0.0'>
                            <set:label name="Minimum_Payment" defaultvalue="Minimum Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                        </set:listif>
                        <set:listif name='amount' condition='notequals' value='0.0'>
                            <set:listdata name='amount' format='amount' />
                        </set:listif>
                    </set:listif>
                    <input id="toAmount" name="toAmount" type="hidden" value="">					
 					<input id="amount" name="amount" type="hidden" value="<set:listdata name='amount' format='amount'/>">
                </set:listif>
                <set:listif name="txnType" condition="equals" value="AUTOPAY_DELETE">
                    <set:listif name='amount' condition='equals' value='9.999999999E9'>
                        <set:label name="FULL_PAYMENT" defaultvalue="Full Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                    </set:listif>
                    <set:listif name='amount' condition='notequals' value='9.999999999E9'>
                        <set:listif name='amount' condition='equals' value='0.0'>
                            <set:label name="Minimum_Payment" defaultvalue="Minimum Payment" rb="app.cib.resource.txn.autopay_instruction"/>
                        </set:listif>
                        <set:listif name='amount' condition='notequals' value='0.0'>
                            <set:listdata name='amount' format='amount' />
                        </set:listif>
                    </set:listif>
                    <input id="toAmount" name="toAmount" type="hidden" value="">					
 					<input id="amount" name="amount" type="hidden" value="<set:listdata name='amount' format='amount'/>">
                </set:listif>
                <set:listif name="txnType" condition="equals" value="CHEQUE_BOOK_REQUEST">
                	<input id="toAmount" name="toAmount" type="hidden" value="">					
 					<input id="amount" name="amount" type="hidden" value="<set:listdata name='amount' format='amount'/>">
                </set:listif>
                <set:listif name="txnType" condition="notequals" value="AUTOPAY_ADD">
	                <set:listif name="txnType" condition="notequals" value="AUTOPAY_EDIT">
		                <set:listif name="txnType" condition="notequals" value="AUTOPAY_DELETE">
			                <set:listif name="txnType" condition="notequals" value="CHEQUE_BOOK_REQUEST">
                				<set:listif name="ruleFlag" condition="equals" value="0">
                    				<set:listdata name="amount" format='amount'/>
                    				<input id="toAmount" name="toAmount" type="hidden" value="">					
 									<input id="amount" name="amount" type="hidden" value="<set:listdata name='amount' format='amount'/>">
                                </set:listif>
                    			<set:listif name="ruleFlag" condition="equals" value="1">
                    				<set:listdata name="toAmount" format='amount'/>
                        			<input id="amount" name="amount" type="hidden" value="">					
 								    <input id="toAmount" name="toAmount" type="hidden" value="<set:listdata name='toAmount' format='amount'/>">	
                     		   </set:listif>
                             
			               </set:listif>
		                </set:listif>
	                </set:listif>
                </set:listif>
				<!-- modify by long_zg 2014-01-08 for CR192 bob batch end -->
                      <td align="center" class="listcontent1"><set:listdata name="checkout"/></td>
                    </tr>
                  </set:list>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td class="groupconfirm"><set:label name="Confirmation_multilist" defaultvalue="MY OPINION"/></td>
                  </tr>
                  <tr>
                    <td class="content1"><textarea name="memo" cols="80" rows="3" style = "resize: none;"><set:data name='memo'/>
</textarea></td>
                  </tr>
                  
                  	<!-- add by lzg 20190819 -->
		              <tr id='otp_tr' style = "display: none;">
		              	<td class="content1">
		              		<set:label name="OTP" rb = "app.cib.resource.sys.login"/>&nbsp;&nbsp;&nbsp;
		              		<br>
		              		<div id = "OTPSendTip" style = "font-size: 1vw;color:red;display:none;">
		              			<set:label name="SEND_OTP_SUCCESS" rb = "app.cib.resource.sys.login"/>&nbsp;<span id = "MobileContent"></span>&nbsp;<set:label name="Mobile_no" rb = "app.cib.resource.sys.login"/>
		              		</div>
		              		<input name="otp" type="text" id="otp" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" class="form-control form-control-custom-2 default-radius"  value="" maxlength="6" autocomplete="off" >
		              		<input name="button" type="button" id="btn_send_otp" onClick="sendOtp()" value=" <set:label name='SEND_OTP' rb = "app.cib.resource.sys.login" /> " />
		              		<br><span style = "font-size: 1vw;color:red;"><set:label name="otp_info_1" rb = "app.cib.resource.common.otp"/></span>
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
                    <td height="40" class="sectionbutton">
					<set:if name= "ExecuteFlag" condition="equals" value = "N">
					<input id="buttonMultiApprove" name="buttonMultiApprove" type="button" value="<set:label name='buttonMultiApprove' defaultvalue='Multi Approve'/>" onClick="doMultiApprove()">
					</set:if>
					<set:if name= "ExecuteFlag" condition="equals" value = "Y">
					<input id="buttonMultiExecute" name="buttonMultiExecute" type="button" value="<set:label name='buttonMultiExecute' defaultvalue='Multi Execute'/>" onClick="doMultiExecute()">
					</set:if>
                      <input id="buttonCancel" name="buttonCancel" type="button" value="<set:label name='return' defaultvalue='Cancel'/>" onClick="doCancel()">
                      <!--//CR202 20151126 chen_y begin-->
                      <input id="buttonConfirm1" name="buttonConfirm1" style="display:none;"  type="submit" value="<set:label name='buttonConfirm' defaultvalue='Confirm'/>">
                <!--//CR202 20151126 chen_y end-->
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="multiApprove">
                      <input id="act" name="act" type="hidden" value="A">
					  <input id="multiFlag" name="multiFlag" type="hidden" value="M">
                    <input id="fieldsToBeSigned" name="fieldsToBeSigned" type="hidden" value="workId|txnType|transDesc|currency|toCurrency|amount|toAmount">
                    <input name="securityCode" type="hidden" id="securityCode" value="<set:data name='securityCode'/>">
                    <!--update by liang_ly for CR204 2015-4-9-->
                  <input name="otp" type="hidden" id="otp" value="<set:data name='otp'/>">
                  <input name="smsFlowNo" type="hidden" id="smsFlowNo" value="<set:data name='smsFlowNo'/>">
                  <input name="checkOTPFlag" type="hidden" id="checkOTPFlag" value="<set:data name='checkOTPFlag'/>">
                  <input name="exceedResend" type="hidden" id="exceedResend" value="<set:data name='exceedResend'/>">
                  <!-- add by lzg 20190819 -->
                  <input name="operationType" type="hidden" id="operationType" value = "<set:data name = 'operationType'/>" >
                  <input name="submitFlag" type="hidden" id="submitFlag">
                  <input name = "showMobileNo" id = "showMobileNo" type = "hidden" value = "<set:data name = 'showMobileNo' />" >
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
