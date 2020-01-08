<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<script language="javascript" src="/cib/javascript/jsax2.js?v=20160908"></script>
<tr>
<td height="24" class="content">
	<%otpInfo%>
</td></tr>
<tr><td>
<input type="text" id="otp" name="otp" size="20">&nbsp;&nbsp; 
<input type="button" id="reSend" name="reSend" value="" onclick="sendOtp()"/>
				<input type="hidden" id="smsFlowNo" name="smsFlowNo" value="<%smsFlowNo%>"/> 
				<input type="hidden" id="exceedResendFlag" name="exceedResendFlag" value="N"/>  
				<input type="hidden" id="exceedResend" name="exceedResend" value="N"/> 
				<div id="errorFlag" name="errorFlag" style="display:none"></div>
                <input type="hidden" id="newOtpFlag" name="newOtpFlag" value="N"/> 
                <input type="hidden" id="openerURL" name="openerURL" value=""/> 
                
</td>
</tr>
<tr>
<td class="content">
	<font color="#FF0000"><%otpInfo1%></font>
</td>
</tr>
<tr><td align="center">
<input id="ok" name="ok" type="button" value="&nbsp;&nbsp;<%buttonOk%>&nbsp;&nbsp;" onClick="doSubmit()">
<input type="button" id="cancelBtn" name="cancelBtn" value="<%buttonCancel%>" onclick="cancel()"/>
<input type="button" id="getNewOtpBtn" name="getNewOtpBtn" value="<%getNewOtp%>" style="display:none" onclick="toGetNewOtp()"/>
&nbsp;&nbsp;
</td></tr>
<SCRIPT langauge=javascript>
document.getElementById("reSend").value= "<%resendOtp%>" ;
var NumFlag = false ;
function dis(){
	NumFlag = true ;
	document.getElementById("reSend").disabled =true;
}
var periodStr = '<%validityPeriod%>' ;
if(null==periodStr || ""== periodStr){
	periodStr = "100" ;
}

var period = new Number(periodStr) ;
window.setTimeout("dis()",period * 1000) ;
var secsStr = '<%disableTime%>' ;
if(null==secsStr || ""==secsStr){
	secsStr = "30" ;
} 

var secs = new Number(secsStr) ;
document.getElementById("reSend").disabled =true;
window.setTimeout("update()", secs * 1000);
function update() {
	if(!NumFlag){
			document.getElementById("reSend").disabled = false ;
	}
}

function cancel() {
	var fromPage=document.getElementById("openerURL").value;
	if(fromPage.indexOf('approve')>0){
		window.opener.location.href = '/cib/approve.do?ActionMethod=list';
	}
	//window.location.href='/cib/approve.do?ActionMethod=list';
	window.close();
}
function toGetNewOtp(){
	window.location.href=document.getElementById("openerURL").value;
}

function sendOtp(){
	var exceedResendFlagH = document.getElementById("exceedResendFlag") ;
	var exceedResendFlag = exceedResendFlagH.value;
	//alert("exceedResendFlagH=" + exceedResendFlag) ;
	if("Y"==exceedResendFlag){
	//	alert("kkk") ;
		document.getElementById("exceedResend").value= "Y" ;
		//document.getElementById("form1").submit() ;
		doSubmit() ;
		return ;
	} 
	document.getElementById("reSend").disabled =true;
	var smsFlowNo = document.getElementById("smsFlowNo").value ;
	args = "&smsFlowNo="+ smsFlowNo;
	//getMsgToElement('/cib/jsax?serviceName=OtpService', '', args, callback, false) ;
	getMsgToElement('/cib/jsax?serviceName=OtpService'+args , '', '', null,false,language);
	//alert(document.getElementById("exceedResend").value) ;
}


function callback(){
	window.setTimeout("update()", secs * 1000);
}
</SCRIPT>