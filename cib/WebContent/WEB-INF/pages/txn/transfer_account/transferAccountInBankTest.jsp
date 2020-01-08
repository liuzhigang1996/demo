<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.transfer_bank">
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
}
function doSubmit() {
	if(validate_transferInBANK(document.getElementById("form1"))){
		//modify by hjs 20070321
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
		//changeToAcc(toAcc);
	}
}
function changeFromAcc(fromAcc){
	if (fromAcc != '0' && toAcc != '') { //modify by hjs
		var url = '/cib/jsax\?serviceName=AccInTxnService&showFromAcc=' + fromAcc + '&language=' + language;
   		//registerElement('showAmount');
   		//registerElement('showFromCurrency');
		getMsgToElement(url, fromAcc, '', null, false,language);
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
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/transferInbankTest.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.transfer_bank" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="BANK Online Banking >Transfer > Accounts in BANK"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="TRANSFER TO ACCOUNTS IN BANK"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/transferInbankTest.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <set:messages width="100%" cols="1" align="center"/>
            <set:fieldcheck name="transferInBANK" form="form1" file="transfer" />
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="4" class="groupinput"><set:label name="Fill_Info" defaultvalue="Fill the following Information"/></td>
              </tr>
              <tr>
                <td colspan="4"  class="label1"><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
              </tr>
              <tr class="greyline">
                <td width="16%" valign="top" class="label1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="3" ><select id="fromAccount" name="fromAccount" nullable="0" onChange="/*changeFromAcc(this.value)*/">
				<option selected value="">----- Select an Account ------</option>
                  <set:rblist db="caoasaAccountByUser">
                    <set:rboption name="fromAccount"/>
                  </set:rblist>
                </select>
                  <div name="showAmountArea" id="showAmountArea" style="display: none"><table border="0" cellpadding="0" cellspacing="0">
                    <tr><td>Available Balance :&nbsp;</td><td> <div name="showAmount"  format="amount" id="showAmount" ></div></td></tr></table></div></td>
              </tr>
              <tr>
                <td colspan="4"  class="label1"><b><set:label name="Transfer_To" defaultvalue="Transfer To"/></b></td>
              </tr>
			  <tr class="greyline">
                <td class="label1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="3" ><select id="toAccount" name="toAccount" nullable="0" onChange="changeToAccList(this.value);">
					<option value="0" selected>----- Other Accounts ------</option>
					<set:rblist db="caoasaAccountByUser">
                    <set:rboption name="toAccount"/>
					</set:rblist>
                  </select></td>
			  </tr>
			  <tr>
                <td class="label1"></td>
				<td class="content1" colspan="3"><input name="toAccount2" type="text" id="toAccount2" value="<set:data name='toAccount2'/>" size="20" maxlength="12" onChange="/*otherAcc(this.value)*/"></td>
			</tr>
              <tr class="greyline">
                <td valign="top" class="label1"></td>
                <td class="content1" colspan="3" ><b><set:label name="Transfer_Amount_Info1" defaultvalue="You either input the actual amount of funds to be transferred OR the amount to be debited from the selected account above" /></b></td>
              </tr>
              <tr>
                 <td valign="top" class="label1"><set:label name="Transfer_Amount" defaultvalue="Transfer Amount"/></b></td>
                <td width="34%" class="label1"><set:label name="Amount_to_be_Transferred" defaultvalue="Amount to be Transferred"/></td>
                <td width="5%" align="right" class="content1"><div name="showToCurrency" id="showToCurrency"><set:data name="toCurrency"/></div></td>
                <td width="45%" class="content1"><input id="transferAmount" name="transferAmount" type="text" value="<set:data name='transferAmount' format='amount'/>" size="15" maxlength="20"  onFocus ="document.form1.debitAmount.value='';"></td>
              </tr>
              <tr class="greyline">
                <td></td>
                <td width="34%" class="label1"><set:label name="Transfer_Info1" defaultvalue="Amount to be debited from the selected account above"/></td>
                <td align="right" class="content1"><div name="showFromCurrency" id="showFromCurrency"><set:data name="fromCurrency"/></div></td>
                <td class="content1"><input id="debitAmount" name="debitAmount" type="text" value="<set:data name='debitAmount' format='amount'/>" size="15" maxlength="20" onFocus="document.form1.transferAmount.value='';"></td>
              </tr>
              <tr>
                <td class="label1"><set:label name="Remark" defaultvalue="Remark"/></td>
                <td class="content1" colspan="3"><input id="remark" name="remark" type="text" value="<set:data name='remark'/>" size="50" maxlength="50"></td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
			  <td colspan="2" class="sectionbutton">
                <input id="submit1" name="submit1" type="button" value="<set:label name='Transfer' defaultvalue='Transfer' />" tabindex="3"  onClick="doSubmit();">
<!--                <input id="bottonReset" name="bottonReset" type="reset" onClick="document.getElementById('showAmountArea').style.display='none';" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="add">				  </td>
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
