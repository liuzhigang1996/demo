<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.bank_draft_request">
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
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	if("<set:data name='toAccount' />" != "0" && "<set:data name='toAccount' />" != "" ){
			document.getElementById("toAccount2").disabled = true;
	}
}
function doSubmit(arg){
	if(checkAssignedUser()){
		if(arg == 'confirm') {
			document.form1.ActionMethod.value ='addConfirm';
	    	setFormDisabled('form1');
			document.form1.submit();
		} else if (arg == 'cancel') {
			document.form1.ActionMethod.value ='addLoad';
	    	setFormDisabled('form1');
			document.form1.submit();
		}
	}
}
function doCancel()
{
	setFormDisabled("form1");
	document.form1.ActionMethod.value="addCancel";
	document.form1.submit();
}
function changeToAccList(toAcc){
	if (toAcc=='0') {
		document.getElementById("toAccount2").disabled = false;
		document.getElementById("toAccount2").value = "";
		document.getElementById("showToCurrency").innerHTML = "";
	} else {
		document.getElementById("toAccount2").disabled = true;
		document.getElementById("toAccount2").value = toAcc;
		changeToAcc(toAcc);
	}
}
function changeFromAcc(fromAcc){
	if (fromAcc != '0') { //modify by hjs
		var url = '/cib/jsax\?serviceName=AccInTxnService&showFromAcc=' + fromAcc + '&language=' + language;
   		//registerElement('showAmount');
   		//registerElement('showFromCurrency');
		getMsgToElement(url, fromAcc, '', null,true,language);
		$("showAmountArea").style.display = "block";		
	}else{
		document.getElementById("showAmountArea").style.display = "none";
	}
}
function changeToAcc(toAcc){
	if (toAcc != '0') {//modify by hjs
		document.getElementById("showToCurrency").innerHTML = "";
		var url = '/cib/jsax?serviceName=AccInTxnService&showToAcc=' + toAcc + '&language=' + language;
   		//registerElement('showToCurrency');
		getMsgToElement(url, toAcc, '', null,true,language);
	}
}
function otherAcc(toAcc){
	if ((toAcc != null) && (toAcc != '')) {
		var url = '/cib/jsax?serviceName=AccInTxnService&accType=other&showToAcc=' + toAcc + '&language=' + language;
   		//registerElement('showToCurrency');
		getMsgToElement(url, toAcc, '', null,true,language);
	}
}
</script>
<style type="text/css">
<!--
.STYLE1 {font-size: 60pt}
-->
</style>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/bankRequest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.bank_draft_request" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="BANK Online Banking >Services > Bank Draft Request"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="BANK DRAFT REQUEST"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/bankRequest.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <set:messages width="100%" cols="1" align="center"/>			 
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr >
				 <td class="groupinput" colspan="2"><set:label name="Confirmation" defaultvalue="Confirmation"/></td>
              </tr>
			  <tr >
			   <td width="29%" class="label1"><set:label name="Debit_Account" defaultvalue="Account to be debited "/></td>
               <td width="71%" class="content1"><set:data name='fromAccount' /></td>
			 </tr>
			  <tr class="greyline" >
                <td class="label1"><set:label name="Charge_Account" defaultvalue="Account to be debited for Charges"/></td>
				 <td class="content1"><set:data name='chargeAccount' /></td>
              </tr>
            </table>
			 <table width="100%" border="0" cellspacing="0" cellpadding="3">
			  <tr >
                 <td width="12%" class="listheader1" align="left"><set:label name="Beneficiary_Name" defaultvalue="Beneficiary Name"/></td>
                 <td width="12%" class="listheader1" align="right"><set:label name="Amount_confirm" defaultvalue="Amount"/>(in <set:data name='toCurrency' db="rcCurrencyCBS"/>)</td>
				 <td width="12%" class="listheader1" align="right"><set:label name="Charges" defaultvalue="Charges"/>(in <set:data name='chargeCurrency' db="rcCurrencyCBS"/>)</td>
				 <!-- add by lw 20100902 -->
				  <td width="12%" class="listheader1" align="right"><set:label name="Purpose" defaultvalue="Purpose"/></td>
				  <!-- add by lw end -->
              </tr>
			  <set:list name="inputList"  showNoRecord="YES">
			  <tr class="<set:listclass class1='' class2='greyline'/>">
			    <td align="left" class="listcontent1"><set:listdata name='beneficiaryName'/><br><set:listdata name='beneficiaryName2'/></td>
                <td align="right" class="listcontent1"><set:listdata name='draftAmount' format="amount"/></td>
				<td align="right" class="listcontent1"><set:listdata name='chargeAmount' format="amount"/></td>
				<!-- add by lw 20100902 -->			
				<td align="right" class="listcontent1">
				<set:listif name="showPurpose" condition="EQUALS" value="true">
				<set:listdata name='purposeString' />
				</set:listif>
				</td>			
				<!-- add by lw end -->
			  </tr>
			  </set:list>
			   <tr >
                 <td  class="listheader1"><div align="center"><b><set:label name="Total_Amount" defaultvalue="Total Amount"/><b></div></td>
                 <td  class="listheader1"><div align="right"><b><set:data name='toAmount' format="amount"/></div><b></td>
				 <td  class="listheader1"><div align="right"><b><set:data name='totalChargesAmount' format="amount"/><b></div></td>
				 <!-- add by lw 20100902 -->				 
				 <td  align="right" class="listheader1"></td>					
				 <!-- add by lw end -->	
			   </tr>
            </table>
            <set:assignuser selectFlag="Y" /> 
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
			  <td colspan="2" class="sectionbutton">
                <input id="submit1" name="submit1" type="button" value="<set:label name='Confirm' defaultvalue='Confirm' />" tabindex="3"  onClick="doSubmit('confirm');">
				<input id="buttonCancel" name="buttonCancel" type="button" value="<set:label name='buttonCancel' defaultvalue='Return' />" tabindex="3"  onClick="doCancel();">
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="addConfirm">				  </td>
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
