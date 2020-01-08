<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bat.batch_transfer_oversea">
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
}
function doSubmit(arg) {
	if (arg == 'return') {
		document.form1.ActionMethod.value = 'uploadFileLoad';
		document.getElementById("form1").submit();
		setFormDisabled("form1");
		//setFieldEnabled("buttonReturn");
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
		getMsgToElement(url, fromAcc, '', null,true,languag);
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
		getMsgToElement(url, toAcc, '', null,true,languag);
	}
}
function otherAcc(toAcc){
	if ((toAcc != null) && (toAcc != '')) {
		var url = '/cib/jsax?serviceName=AccInTxnService&accType=other&showToAcc=' + toAcc + '&language=' + language;
   		//registerElement('showToCurrency');
		getMsgToElement(url, toAcc, '', null,true,languag);
	}
}
</script>
<style type="text/css">
<!--
.STYLE1 {font-size: 60pt}
-->
</style>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/transferInOverseaStM.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bat.batch_transfer_oversea" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitleStM" defaultvalue="BANK Online Banking >Transfer > Single D to Multi C"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitleStM" defaultvalue="SINGLE DEBIT TO MULTI CREDIT TRANSFER "/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/transferInOverseaStM.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <set:messages width="100%" cols="1" align="center"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
               <tr>
                <td colspan="2" class="groupinput"><set:label name="Acknowledgement" defaultvalue="Acknowledgement"/></td>
              </tr>
             <tr>
               <td width="28%" class="label1"><set:label name="Status" defaultvalue="Status"/></td>
               <td width="72%" class="content1"><set:label name='ackStatusAccepted'/></td>
             </tr>
             <tr class="greyline">
               <td class="label1"><set:label name="Reference_No" defaultvalue="Reference No"/></td>
               <td class="content1"><set:data name='BATCH_ID'/></td>
             </tr>
			   <tr>
                <td colspan="4"  class="label1"><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
              </tr>
			   <tr class="greyline">
                <td  width="28%" class="label1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                <td  width="72%" class="content1" colspan="2" ><set:data name='FROM_ACCOUNT'/>
				</td>
              </tr>
			   <tr >
			    <td class="label1"><set:label name="Amount_to_be_debited" defaultvalue="Amount to be debited"/></td>
               <td class="content1"><set:data name='fromCurrency'/>&nbsp;<set:data name='fromAmount' format="amount"/>
			    </td>
              </tr>
                <tr class="greyline">
                  <td width="28%" class="label1"><set:label name="total_amt" defaultvalue="Total Amount"/></td>
                  <td width="72%" class="content1"><set:data name="TO_CURRENCY"/>&nbsp;<set:data name="totalAmount" format="amount"/></td>
                </tr>			
				<tr class="">
                  <td class="label1"><set:label name="Exchange_Rate" defaultvalue="Exchange Rate"/></td>
                  <td class="content1"><set:data name="exchangeRate" format="rate"/>&nbsp;&nbsp;<span style = "color:red;"><set:label name="Exchange_Rate_Tip" defaultvalue="The Rate is for reference only, the dealing rate will be quoted upon trading."/></span></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="no_of_record" defaultvalue=" Number of Record"/></td>
                  <td class="content1"><set:data name="allCount" /></td>
                </tr>
                <tr class="greyline">
             <td colspan="4"  class="label1"><b><set:label name="Transfer_To" defaultvalue="Transfer To"/></b></td>
            </tr>
            </table>
			 <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td class="listheader1"><set:label name="line_in_file" defaultvalue="line_in_file"/></td>
                  <td class="listheader1"><set:label name="TO_ACCOUNT" defaultvalue="TO ACCOUNT"/></td>
                  <td align="right" class="listheader1"><set:label name="TRANSFER_AMOUNT" defaultvalue="TRANSFER AMOUNT"/></td>
				  <td class="listheader1"><set:label name="BENEFICIARY_NAME" defaultvalue="BENEFICIARY_NAME"/></td>
				  <td class="listheader1"><set:label name="Beneficiary_Bank" defaultvalue="Beneficiary Bank"/></td>
				  <td class="listheader1"><set:label name="Beneficiary_Country" defaultvalue="Beneficiary Country"/></td>
				  <td class="listheader1"><set:label name="Charge_Account" defaultvalue="Charge_Account"/></td>
				</tr>
                <set:list name="recList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                   <td class="listcontent1"><set:listdata name="LINE_NO" /></td>
                  <td class="listcontent1"><set:listdata name="TO_ACCOUNT" /></td>
                  <td align="right" class="listcontent1"><set:listdata name="TRANSFER_AMOUNT" format="amount"/></td>
                  <td class="listcontent1"><set:listdata name="BENEFICIARY_NAME"  escapechar="YES"/><br><set:listdata name="BENEFICIARY_NAME2"  escapechar="YES"/></td>
				  <td class="listcontent1"><set:listdata name="BENEFICIARY_BANK"  escapechar="YES"/></td>
				  <td class="listcontent1"><set:listdata name="BENEFICIARY_COUNTRY" /></td>
				  <td class="listcontent1"><set:listdata name="CHARGE_ACCOUNT" /></td>
                </tr>
                </set:list>
              </table>
             <set:assignuser selectFlag='N'/>
             <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
					<div align="center">
				  	  <input id="return" name="return" type="button" value="&nbsp;&nbsp;<set:label name='buttonOK' defaultvalue='OK '/>&nbsp;&nbsp;" onClick="doSubmit('return')">
				  	  <input id="ActionMethod" name="ActionMethod" type="hidden" value="addLoad">
			        </div></td>
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
