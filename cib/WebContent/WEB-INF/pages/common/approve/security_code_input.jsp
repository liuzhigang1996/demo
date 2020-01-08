<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="../../../../Templates/dialog.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	<set:if name="authenticationMode" condition="equals" value = "C">
		document.getElementById("openerURL").value=window.location.href;
	</set:if> 
}
function doSubmit(){
	<!--update by liang_ly for CR204 2015-4-9-->
	 <set:if name="authenticationMode" condition="equals" value = "C">
	 var debug_ind = 1;
	 try{
		debug_ind = 11;
		var otp = document.getElementById("otp").value;
		var smsFlowNo = document.getElementById("smsFlowNo").value;
		//var exceedResendFlag = document.getElementById("exceedResendFlag").value;
		var exceedResend = document.getElementById("exceedResend").value;
		debug_ind = 12;
		//alert(smsFlowNo);
		/*if(document.getElementById("form1")){
			window.opener.setOtpCode(otp,smsFlowNo,exceedResend);
			window.close();
		}*/
		debug_ind = 2;
		if(document.getElementById("form1")){
			var args = '&otp='+otp+'&smsFlowNo='+smsFlowNo+'&exceedResend='+exceedResend;
			//alert(args);
			debug_ind = 21;
			getMsgAndErrorToElement('/cib/jsax?serviceName=SMSOtpService'+args, '', "", callback, false,language) ;
			debug_ind = 22;
			var errorFlag = document.getElementById("errorFlag").innerHTML;
			//alert(errorFlag);
			if(errorFlag=="N"){
				debug_ind = 23;
				window.opener.setOtpCode(otp,smsFlowNo,exceedResend);
				window.close();
			}else if(errorFlag!=""){
				//document.getElementById("ok").disabled=true;
				document.getElementById("ok").style.display="none";
				document.getElementById("reSend").style.display="none";
				document.getElementById("getNewOtpBtn").style.display="";
			}
		}
	  }catch(ex){
		alert("Error process muliti-approval/single approval [" + debug_ind + "] -" + ex.message);
		return 1;
	 }
      	
     </set:if> 
	<set:if name="authenticationMode" condition="notequals" value = "C">
        
	
		//var codeval = document.getElementById("securityCode").value;
		//if(validate_inputSecurityCode(document.getElementById("form1"))){
		//	window.opener.setSecurityCode(codeval);
		//	window.close();
		//}
		//mod by linrui for temp otp 20190403
	    <set:if name="OtpFlag" condition="equals" value = "Y">
	     var debug_ind = 1;
		      try{
			     debug_ind = 11;
			     var otp = document.getElementById("otp").value;
			     var smsFlowNo = document.getElementById("smsFlowNo").value;
			     var exceedResend = document.getElementById("exceedResend").value;
			     debug_ind = 12;
			     debug_ind = 2;
			     if(document.getElementById("form1")){
				     var args = '&otp='+otp+'&smsFlowNo='+smsFlowNo+'&exceedResend='+exceedResend;
				     debug_ind = 21;
				     getMsgAndErrorToElement('/cib/jsax?serviceName=SMSOtpService'+args, '', "", callback, false,language) ;
				     debug_ind = 22;
				     var errorFlag = document.getElementById("errorFlag").innerHTML;
				     if(errorFlag=="N"){
					      debug_ind = 23;
					      window.opener.setOtpCode(otp,smsFlowNo,exceedResend);
					      window.close();
				     }else if(errorFlag!=""){
					      document.getElementById("ok").style.display="none";
					      document.getElementById("reSend").style.display="none";
					      document.getElementById("getNewOtpBtn").style.display="";
				     }
			     }
		      }catch(ex){
			     alert("Error process muliti-approval/single approval [" + debug_ind + "] -" + ex.message);
			     return 1;
		      }
	     </set:if>
	     <set:if name="OtpFlag" condition="notequals" value = "Y">
	          var codeval = document.getElementById("securityCode").value;
			          if(validate_inputSecurityCode(document.getElementById("form1"))){
				          window.opener.setSecurityCode(codeval);
				          window.close();
			          }
	     </set:if>
		//tem otp 20190403


	</set:if> 
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="#" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.flow.approve" -->
</head>
<body onLoad="pageLoad();">
<set:loadrb file="app.cib.resource.flow.approve">
  <table width="500" border="0" cellspacing="0" cellpadding="0" height="40">
    <tr>
      <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" -->
     <!--update by liang_ly for CR204 2015-4-9-->
     
      <set:if name="authenticationMode" condition="equals" value = "C">
      	<set:label name="functionTitle_otpcode" defaultvalue=""/>
      </set:if> 
      <set:if name="authenticationMode" condition="notequals" value="C">
      	<%--
      	<set:label name="functionTitle_securitycode" defaultvalue=""/>
        --%>
        <!-- mod by linrui 20190403 for temp otp -->
        <set:if name="OtpFlag" condition="equals" value = "Y">
           <set:label name="functionTitle_otpcode" defaultvalue=""/>
        </set:if>
        <set:if name="OtpFlag" condition="notequals" value = "Y">
           <set:label name="functionTitle_securitycode" defaultvalue=""/>
        </set:if>
        <!-- mod for temp otp -->
      </set:if> 
        
      <!-- InstanceEndEditable --></td>
      <td width="100%"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td height="1" bgcolor="#FF0000"><img src="/cib/images/menu_image/spacer.gif" width="1" height="1"></td>
          </tr>
      </table></td>
    </tr>
  </table>
  <table width="500" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
  <tr>
    <td width="1%"><img src="/cib/images/shim.gif" width="1" height="1"></td>
    <td><form action="#" method="post" name="form1" id="form1" onSubmit="javascript:return false;">
      <!-- InstanceBeginEditable name="sectioncontent" -->
      <!-- modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB begin -->
    	<set:messages width="100%" cols="1" align="center"/>
      <set:if name="authenticationMode" condition="equals" value = "C">
      	<table width="100%" border="0" cellspacing="0" cellpadding="3">
      		<set:otp amtField='transAmt' accField='fromAcc' funcName='' txnCode='' colspan='1,1' />
        </table>
      </set:if> 
      <set:if name="authenticationMode" condition="notequals" value="C">
              <%-- mod by linrui for temp otp 20190403 begin--%>
              <set:if name="OtpFlag" condition="equals" value = "Y">
                  <table width="100%" border="0" cellspacing="0" cellpadding="3">
      		          <set:otp amtField='transAmt' accField='fromAcc' funcName='' txnCode='' colspan='1,1' />
                  </table>
              </set:if>
              <%-- temp otp end--%>
              <set:if name="OtpFlag" condition="notequals" value = "Y"><%-- mod by linrui for temp otp 20190403--%>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/><set:fieldcheck name="inputSecurityCode" form="form1" file="login" /></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="2" class="groupinput"><set:label name="input_info"/></td>
                </tr>
                <tr>
                  <td width="28%" class="label1"><set:label name="security_code"/></td>
                  <td width="72%" class="content1"><input type="password" name="securityCode" id="securityCode" maxlength="12" size="20" value="<set:data name='securityCode'/>"></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input id="ok" name="ok" type="button" value="<set:label name='OTP_submit'/>" onClick="doSubmit()">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="ok">
                    <!-- add by long_zg 2015-06-24 for CR210 New policy to BOB/BOL password -->
                  <input name="userId" type="hidden" id="userId" value="<set:data name='userId'/>">
                  <input name="idNo" type="hidden" id="idNo" value="<set:data name='idNo'/>">
                  </td>
                </tr>
              </table>
              </set:if><%-- mod by linrui for temp otp 20190403--%>
              
        </set:if>
       <!-- modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB end -->  
	  <!-- InstanceEndEditable -->
    </form></td>
</table>
<table width="500" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
  </tr>
</table>
</set:loadrb>
</body>
<!-- InstanceEnd --></html>
