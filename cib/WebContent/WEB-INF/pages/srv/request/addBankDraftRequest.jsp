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
	//changeFromAcc("<set:data name='fromAccount' />");
	//changeToAcc("<set:data name='toAccount' />");
	if("<set:data name='toAccount' />" != "0" && "<set:data name='toAccount' />" != "" ){
			document.getElementById("toAccount2").disabled = true;
	}
	putFieldValues("chargeBy", "1");
	//this.form.chargeAccount.disabled = true;
	document.getElementById("chargeAccount").disabled = true ;
}
function doSubmit()
{
	if(validate_bank_draft_requst(document.getElementById("form1"))){
			setFormDisabled('form1');
			document.getElementById("form1").submit();
	}
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
		    <!-- InstanceBeginEditable name="sectioncontent" --> <set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="bank_draft_requst" form="form1" file="bank_draft" />
              <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_o"><set:label name="Bank_draft_single" defaultvalue="Bank Draft Request"/></td>
                        <td class="rtab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                    </table></td>
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_c"><a onClick="toTargetFormgoToTarget('/cib/bankBatchRequest.do?ActionMethod=uploadFileLoad')" href="#"><set:label name="Bank_draft_batch" defaultvalue="Bank Draft Batch Request"/></a></td>
                        <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                    </table></td>
                  <td></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="4" class="groupinput"><set:label name="Fill_Info" defaultvalue="Enter request information "/></td>
                </tr>
                <tr >
                  <td width="20%" valign="top" class="label1"><set:label name="Account_Number" defaultvalue="Account"/></td>
                  <td class="content1" colspan="3" ><select id="fromAccount" name="fromAccount" nullable="0" >
                      <option selected value=" "><set:label name="Select_Account_Label" defaultvalue="----- Please Select an Account ------"/></option>
                      <set:rblist db="caoasaAccountByUser"> <set:rboption name="fromAccount"/> </set:rblist>
                    </select>
                  </td>
                </tr>
                <tr class="greyline">
                  <td width="20%" valign="top" class="label1"><set:label name="Currency" defaultvalue="Currency"/></td>
                  <td class="content1" colspan="3" ><select id="toCurrency" name="toCurrency" nullable="0" >
                      <option selected value=" "><set:label name="Select_Currency_Label" defaultvalue="----- Please Select a Currency ------"/></option>
                      <set:rblist file="app.cib.resource.srv.bank_draft_request_currency"> 
                      <option value="<set:rbkey/>"><set:rbvalue /></option>
                      </set:rblist>
                    </select>
                  </td>
                </tr>
                <tr >
                  <td class="label1"><set:label name="Draft_Number" defaultvalue="Number of Bank Draft"/></td>
                  <td class="content1" colspan="3"><input id="totalNumber" name="totalNumber" type="text" value="<set:data name='totalNumber'/>" size="2" maxlength="2"></td>
                </tr>
                <!--  <tr  class="greyline">
                  <td class="label1"><set:label name="Total_Amount" defaultvalue="Total Amount"/></td>
                  <td class="content1" colspan="3"><input id="toAmount" name="toAmount" type="text" value="<set:data name='toAmount'/>" size="15" maxlength="15"></td>
                </tr>
			 -->
                <tr class="greyline">
                  <td class="label1" rowspan="2"><set:label name="Charges_From" defaultvalue="Debit charges from"/></td>
                  <td class="label1" colspan="3"><input id="chargeBy" type="radio" name="chargeBy" value="1" onClick="this.form.chargeAccount.disabled = true;" >
                    <set:label name="Same_Account" defaultvalue="Same Account"/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><input id="chargeBy" type="radio" name="chargeBy" value="2" onClick="this.form.chargeAccount.disabled = false;" >
                    <set:label name="Other" defaultvalue="Other"/>&nbsp;&nbsp;&nbsp;&nbsp;
                    <select id="chargeAccount" name="chargeAccount" nullable="0" >
                      <option selected value=" "><set:label name="Select_Account_Label" defaultvalue="----- Please Select an Account ------"/></option>
                      <set:rblist db="caoasaAccountByUser"> <set:rboption name="chargeAccount"/> </set:rblist>
                    </select>
                  </td>  
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td colspan="2" class="sectionbutton"><input id="submit1" name="submit1" type="button" value="<set:label name='buttonOK' defaultvalue=' OK ' />" tabindex="3"  onClick="doSubmit();">
<!--                    <input id="bottonReset" name="bottonReset" type="reset" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="addDetail">
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
