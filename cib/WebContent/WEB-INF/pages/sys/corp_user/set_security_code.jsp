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
function checkPin2() {
	var newPinValue = document.form1.newSecurityCode.value;
	var checkUpperNum = checkChar(newPinValue,'upper');
	var checkLowerNum = checkChar(newPinValue,'lower');
	document.form1.checkUpperNum.value = checkUpperNum;
    document.form1.checkLowerNum.value = checkLowerNum;
/*	var currentLen = 6;
	if(pin2_min < 6) currentLen = pin2_min;
	var oldPinValue = document.form1.oldSecurityCode.value;
	var newPinValue = document.form1.newSecurityCode.value;
	var confirmPin = document.form1.confirmSecurityCode.value;
	if (oldPinValue=='') {
		alert('<set:label name="err.InitialPin.blank" rb="app.cib.resource.common.errmsg"/>');
		document.form1.oldSecurityCode.focus();
		return false;
	}
	else if (newPinValue=='') {
		alert('<set:label name="err.newPin.blank" rb="app.cib.resource.common.errmsg"/>');
		document.form1.newSecurityCode.focus();
		return false;
	}
	else if (newPinValue != confirmPin) {
		alert('<set:label name="err.pin.different" rb="app.cib.resource.common.errmsg"/>');
		document.form1.confirmSecurityCode.focus();
		return false;
	}
	else if (newPinValue == oldPinValue) {
		alert('<set:label name="err.pin.sameAsOld" rb="app.cib.resource.common.errmsg"/>');
		document.form1.newSecurityCode.focus();
		return false;
	}
	else if (oldPinValue.length < currentLen) {
		alert("Old Password shall have at least " + currentLen + " digits long");
		document.form1.oldSecurityCode.focus();
		return false;
	}
	else if (newPinValue.length < pin2_min || newPinValue.length > pin2_max) {
		alert("New Password shall have at least " + pin2_min + " but not exceeding " + pin2_max + " digits long");
		document.form1.newSecurityCode.focus();
		return false;
	}
	else if(checkChar(newPinValue,'upper') < pin2_upper){
		alert("New Password shall have at least " + pin2_upper + " digits long upper letter");
		document.form1.newSecurityCode.focus();
		return false;
	}
	else if(checkChar(newPinValue,'lower') < pin2_lower){
		alert("New Password shall have at least " + pin2_lower + " digits long lower letter");
		document.form1.newSecurityCode.focus();
		return false;
	}
	else
		return true;*/
}
function pageLoad(){
}
function doSubmit()
{
	/*if (!checkPin2()) {
		return false;
	}*/
	/*checkPin2();
	if(validate_setSecurityCode(document.getElementById("form1"))){
		document.getElementById("form1").submit();
		setFormDisabled("form1");
	}*/
	<!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin-->
	checkPin2();
	if(validate_setSecurityCode(document.getElementById("form1"))){
		//document.getElementById("form1").submit();
		setFormDisabled("form1");
		return true;
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
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpUser.do?ActionMethod=setSecurityCode" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.corp_user" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_code" defaultvalue="MDB Corporate Online Banking > Services > Change Password"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_code" defaultvalue="CHANGE Password"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/corpUser.do?ActionMethod=setSecurityCode" method="post" name="form1" id="form1">
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
                <td width="28%" class="label1"><set:label name="Old_S_Code" defaultvalue="Old Password"/></td>
                <td width="72%" class="content1"><input id="oldSecurityCode" name="oldSecurityCode" type="password" value="<set:data name='oldSecurityCode'/>" size="20" maxlength="20"></td>
              </tr>
			  <tr class="greyline">
                <td class="label1"><set:label name="New_S_Code" defaultvalue="New Password "/></td>
                <td class="content1"><input id="newSecurityCode" name="newSecurityCode" type="password" value="<set:data name='newSecurityCode'/>" size="20" maxlength="20"></td>
              </tr>
              <tr>
                <td class="label1"><set:label name="New_S_Code_Confirm" defaultvalue="New Password Confirm"/></td>
                <td class="content1"><input id="confirmSecurityCode" name="confirmSecurityCode" type="password" value="<set:data name='confirmSecurityCode'/>" size="20" maxlength="20"></td>
              </tr>
            </table>
			 <table width="100%" border="0" cellpadding="0" cellspacing="0">
               <tr>
                 <td class="groupseperator">&nbsp;</td>
               </tr>
             </table>
			 <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
                 <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB begin-->
				<!--<input id="submit1" name="submit1" type="button" value="<set:label name='buttonChangeCode' />"  onClick="doSubmit();">-->
                <input id="submit1" name="submit1" type="submit" value="<set:label name='buttonChangeCode' />">
                 <!-- add by chen_y 2015-11-20 for CR202  Application Level Encryption for BOB end-->
<!--                  <input id="bottonReset" name="bottonReset" type="reset" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
				  <input id="ActionMethod" name="ActionMethod" type="hidden" value="setSecurityCode">	
				  <input name="checkUpperNum" id="checkUpperNum" type="hidden" value="0">
				  <input name="checkLowerNum" id="checkLowerNum" type="hidden" value="0">
                  <!-- add by long_zg 2015-06-24 for CR210 New policy to BOB/BOL password  -->
                  <input name="idNo" id="idNo" type="hidden" value="<set:data name='idNo' />">
                  <input name="userId" id="userId" type="hidden" value="<set:data name='userId' />">
                  
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
