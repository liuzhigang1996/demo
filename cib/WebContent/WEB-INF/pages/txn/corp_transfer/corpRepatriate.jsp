<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.corp_repatriate">
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
	showList($('fromCorporation'), $('fromAccount'),
		function(){
			if('<set:data name="fromAccount"/>'!=''){
				$('fromAccount').value = '<set:data name="fromAccount"/>';
				$('fromAccount').onchange();
				setTimeout(
					function(){
						$('toAccount').onchange();
					},
					500
				);
			}
		}
	);
}
function doSubmit()
{
	if(validate_corpRepatriate(document.getElementById("form1"))){
		if(document.getElementById("showToCurrency").innerHTML == ""){
			alert("<set:label name="To_Acc_Not_Available" defaultvalue = "To account not available, can not submit this transactions"/>");
		}else{
		 if (document.getElementById("showToCurrency").innerHTML =='JPY' || document.getElementById("showToCurrency").innerHTML =='KRW') {
			if (!checkInteger($('transferAmount').value)) {
				alert('<set:label name="err.txn.DecimalNotAllowed" rb="app.cib.resource.common.errmsg"/>');
				$('transferAmount').select();
				return false;
			}  
		}     
		 if (document.getElementById("showFromCurrency").innerHTML =='JPY' || document.getElementById("showFromCurrency").innerHTML =='KRW') {
			if (!checkInteger($('debitAmount').value)) {
				alert('<set:label name="err.txn.DecimalNotAllowed" rb="app.cib.resource.common.errmsg"/>');
				$('debitAmount').select();
				return false;
			}    
		}   
			setFormDisabled('form1');
			document.getElementById("form1").submit();
		}
	}
}
function checkInteger(amt) {
	if (amt.indexOf('.') > 0) {
		return false;
	} else {
		return true;
	}
}
function showTree() {
	var selectCorpId = document.getElementById("fromCorporation").value;
	var url="/cib/corporation.do?ActionMethod=listCorpForTree&selectCorpId=" + selectCorpId;
	var popWin1= window.open(url,"temp1","toolbar=no,location=no,top=50,left=50,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=530,height=400");
	popWin1.focus();
}
function changeCorp(arg) {
	putFieldValues("fromCorporation", arg);
	showList($('fromCorporation'),$('fromAccount'));
}
function changeToAcc(toAcc){
	if ((toAcc != null) && (toAcc != '')) {
		var url = '/cib/jsax?serviceName=AccInTxnService&showToAcc=' + toAcc + '&language=' + language;
   		//registerElement('showToCurrency');
		getMsgToElement(url, toAcc, '', null,false,language);
	}
}
function changeToAcc(toAcc){
	if (toAcc != '0' && toAcc != '') {//modify by hjs
		document.getElementById("showToCurrency").innerHTML = "";
		var url = '/cib/jsax?serviceName=AccInTxnService&showToAcc=' + toAcc + '&language=' + language;
   		//registerElement('showToCurrency');
		getMsgToElement(url, toAcc, '', null,true,language);
	}
}
function showList(originSelect, targetSelect, callback){
	if(callback == 'undefined'){
		callback = null;
	}
	$("showAmountArea").style.display = "none";
	if ((originSelect.value != null) && (originSelect.value != '')) {
		var params = getParams(originSelect, targetSelect);
		var url = '/cib/jsax?serviceName=CorpAccListService&'+ params +'&language='+language;
		getMsgToSelect(url,'', callback,true,language);
  	}
}
function getParams(originSelect, targetSelect) {
	var targetType = 'targetType=object';
	var targetId = 'targetId=' + originSelect.id;
	var originValue = 'corpId=' + originSelect.value;
	var subListId = 'subListId=' + targetSelect.id;
	var params = '';
	params = params + targetType + '&' + targetId + '&' + originValue + '&' + subListId;
	return params;
}
function changeFromAcc(fromAcc){
	if (fromAcc != '0' && fromAcc != '') { //modify by hjs
		var url = '/cib/jsax?serviceName=AccInTxnService&showFromAcc=' + fromAcc +'&language='+language;
   		//registerElement('showAmount');
   		//registerElement('showFromCurrency');
		getMsgToElement(url, fromAcc, '',
			// modify by hjs 20070209
			function(){$("showAmountArea").style.display = "block";},
			false,language);				
	}else{
		document.getElementById("showAmountArea").style.display = "none";
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpRepatriate.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.corp_repatriate" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="BANK Online Banking >Corp Fund Allocation > Delivery to Subsidiary"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="DELIVERY TO SUBSIDIARY"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/corpRepatriate.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                           <set:fieldcheck name="corpRepatriate" form="form1" file="corp_transfer" />                       </td>
                      </tr>
                </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="5" class="groupinput"><set:label name="Fill_Info" defaultvalue="Fill the following Information"/></td>
              </tr>
              <tr>
                <td class="label1" colspan="6"><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
              </tr>
              <tr class="greyline" >
                <td width="17%" class="label1"><set:label name="Corporation" defaultvalue="Corporation"/></td>
                <td class="content1" colspan="4" ><select name="fromCorporation" id="fromCorporation" nullable="0" onChange="showList(this,$('fromAccount'));">
                    <option value=""><set:label name="select_corporation" defaultvalue="----- Select a Corporation ------"/></option>
                    <set:rblist db="corpInTree"> <set:rboption name="fromCorporation"/> </set:rblist>
                  </select>
                    <a href="#" onClick="showTree();"> <img src="/cib/images/tree.gif" border="0" align="absmiddle"></a></td>
                </tr>
              <tr >
                <td class="label1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="4" ><select name="fromAccount" id="fromAccount" nullable="0" onChange="changeFromAcc(this.value)">
                    <option value=""><set:label name="select_account" defaultvalue="----- Select an Account ------"/></option>
                  </select>
                    <div name="showAmountArea" id="showAmountArea" style="display: none">
                      <table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td><set:label name="available_balance" defaultvalue="Available Balance :"/>&nbsp;</td>
                          <td><div name="showAmount" format="amount" id="showAmount"></div></td>
                        </tr>
                      </table>
                  </div></td>
                </tr>
              <tr >
                <td class="label1" colspan="3"></td>
              </tr>
              <tr class="greyline">
                <td class="label1" colspan="5"><b><set:label name="Transfer_To" defaultvalue="Transfer To"/></b></td>
              </tr>
              <tr >
                <td class="label1"><set:label name="Corporation" defaultvalue="Corporation"/></td>
                <td class="content1" colspan="4" ><set:data name="toCorporation" /></td>
                </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="4" ><select name="toAccount" id="toAccount" nullable="0" onChange="changeToAcc(this.value)">
                    <option value=""><set:label name="select_account" defaultvalue="----- Select an Account ------"/></option>
                    <set:rblist db="caoasaAccountByUser"> <set:rboption name="toAccount"/> </set:rblist>
                  </select>
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Transfer_Amount" defaultvalue="Transfer Amount"/></td>
                <td class="content1" colspan="4" ><set:label name="Transfer_Amount_Info1" defaultvalue="You either input the actual amount of funds to be transferred OR the amount to be debited from the selected account above "/> </td>
              </tr>
              <tr>
                <td >&nbsp;</td>
                <td width="40%" class="label1"><set:label name="Amount_to_be_Transferred" defaultvalue="Amount to be Transferred"/></td>
                <td width="5%" class="content1"><div name="showToCurrency" id="showToCurrency"><set:data name="toCurrency" db="rcCurrencyCBS"/></div></td>
                <td colspan="3" class="content1"><input id="transferAmount" name="transferAmount" type="text" value="<set:data name='transferAmount' format='amount'/>" size="20" maxlength="20" onFocus ="document.form1.debitAmount.value='';"></td>
              </tr>
              <tr class="greyline">
                <td>&nbsp;</td>
                <td width="40%" class="label1"><set:label name="Transfer_Amount_Info2" defaultvalue="Amount to be debited from the selected account above"/></td>
                <td class="content1"><div name="showFromCurrency" id="showFromCurrency"><set:data name="fromCurrency" db="rcCurrencyCBS"/></div></td>
                <td colspan="3" class="content1"><input id="debitAmount" name="debitAmount" type="text" value="<set:data name='debitAmount' format='amount'/>" size="20" maxlength="20" onFocus ="document.form1.transferAmount.value='';"></td>
              </tr>
              <tr class="">
                <td class="label1" ><set:label name="Remark" defaultvalue="Remark"/></td>
                <td class="content1" colspan="4"><input id="remark" name="remark" type="text" value="<set:data name='remark'/>" size="40" maxlength="40"></td>
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
				<input id="submit1" name="submit1" type="button" value="&nbsp;&nbsp;<set:label name='buttonOK' defaultvalue=' OK ' />&nbsp;&nbsp;" tabindex="3"  onClick="doSubmit();">
<!--                  <input id="bottonReset" name="bottonReset" type="reset" onClick="document.getElementById('showAmountArea').style.display='none';" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
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
