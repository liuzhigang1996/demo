<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.transfer_macau">
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
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></SCRIPT>
<script language=JavaScript>
//mod by linrui for mul-language20171117
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	if(language == "en_US"){
		document.getElementById("showENG").style.display = "block";
	}else{
		document.getElementById("showCHN").style.display = "block";
	}
	//modified by lzg for GAPMC-EB-001-0061
	
	//changeFromAcc("<set:data name='fromAccount' />");
	//changeRequestType('<set:data name="requestType"/>');
	//changeToCcy('<set:data name="toCurrency"/>');
	if(!"<set:data name='fromAccount' />" == ""){
		//changeFromAcc("<set:data name='fromAccount' />");
		var fromAccount = document.getElementById("fromAccount");
		var fromCurrency = document.getElementById("fromCurrency");
		for(var i=0;i<fromAccount.options.length;i++){
    		if(fromAccount[i].value=='<set:data name='fromAccount' />'){  
        		fromAccount[i].selected=true;   
    		}  
		} 
		showFromCcyList(fromAccount, fromCurrency,'getFromCcyRemittance', fromAccount.value);
		if(!"<set:data name='fromCurrency' />" == ""){
			for(var i=0;i<fromCurrency.options.length;i++){
	    		if(fromCurrency[i].value=='<set:data name='fromCurrency' />'){  
	        		fromCurrency[i].selected=true;   
	    		}  
			}  
		}
		changeFromCcy(fromCurrency.value);
	}
	
	if(!"<set:data name='toCurrency' />" == ""){
			var toCurrency = document.getElementById("toCurrency");
			for(var i=0;i<toCurrency.options.length;i++){
		    	if(toCurrency[i].value=='<set:data name='toCurrency' />'){
		        	toCurrency[i].selected=true;   
	    		}  
			}
			changeToCcy(toCurrency.value);  
		}
		
		if(!"<set:data name='chargeAccount' />" == ""){
			var chargeAccount = document.getElementById("chargeAccount");
			for(var i=0; i<chargeAccount.options.length;i++){
		    	if(chargeAccount[i].value=='<set:data name='chargeAccount' />'){
		        	chargeAccount[i].selected=true;   
	    		}  
			}	
		}
		
		if(!"<set:data name='chargeBy' />" == ""){
			var chargeBy = document.getElementsByName("chargeBy");
			for(var i = 0; i < chargeBy.length; i++){
				if(chargeBy[i].value == "<set:data name='chargeBy' />"){
					chargeBy[i].checked=true;
					hiddenContent("<set:data name='chargeBy' />");
					break;
				}
			}
		}
		
		if(!"<set:data name='purposeCode' />" == ""){
			var purposeCode = document.getElementById("purposeCode");
			for(var i=0; i<purposeCode.options.length;i++){
		    	if(purposeCode[i].value=='<set:data name='purposeCode' />'){
		        	purposeCode[i].selected=true;   
	    		}  
			}	
		}
		
		//modified by lzg end
}

//add by lzg for GAPMC-EB-001-0050
function hiddenContent(val){
	var hiddenContent =  document.getElementById("hiddenContent");
	if(val == "S"){
		hiddenContent.style.display = "none";
		var chargeAccount = document.getElementById("chargeAccount");
		var fromAccount = document.getElementById("fromAccount"); 
		for(var i=0; i<chargeAccount.options.length;i++){
	    	if(chargeAccount[i].value == fromAccount.value){
	        	chargeAccount[i].selected=true;   
    		}  
		}
	}else{
		hiddenContent.style.display = "table-row";
	}
}
//add by lzg

function Trim(inputStr)
{
	return String(inputStr).replace(/(^\s*)|(\s*$)/g,"");
}
function doSubmit(){
	//add by linrui 20190723
    if(!remarkLength(document.getElementById("remark").value)){
        return false;
    }
    //end
	var beneName1=document.getElementById("beneficiaryName1").value;
	var beneName2=document.getElementById("beneficiaryName2").value;
	//alert("ksdflsff");
	if(((Trim(beneName1) != "") && (Trim(beneName2) == "")) && ((Trim(beneName1)).indexOf(" ")==-1)){
		document.getElementById("beneJustify").value="Y";
	}else if(((Trim(beneName1) == "") && (Trim(beneName2) != "")) && ((Trim(beneName2)).indexOf(" ")==-1)){
		document.getElementById("beneJustify").value="Y";
	}else{
		document.getElementById("beneJustify").value="N";
	}
	if(validate_templateInMacau(document.getElementById("form1"))){

		/* Add by long_zg 2019-05-21 UAT6-195 COB：海外匯款JPY輸入扣款幣金額卻提示金額過小，沒有拿到匯率 begin */
		var transferAmount = $('transferAmount').value ;
		var debitAmount = $('debitAmount').value ;
		/* Add by long_zg 2019-05-21 UAT6-195 COB：海外匯款JPY輸入扣款幣金額卻提示金額過小，沒有拿到匯率 begin */
		
	    if ($('toCurrency').value=='JPY' || $('toCurrency').value=='KRW') {
			if (!checkInteger($('transferAmount').value)) {
				alert('<set:label name="err.txn.DecimalNotAllowed" rb="app.cib.resource.common.errmsg"/>');
				$('transferAmount').select();
				return false;
			}
			//add by linrui for JPY
			
			/* Modify by long_zg 2019-05-21 UAT6-195 COB：海外匯款JPY輸入扣款幣金額卻提示金額過小，沒有拿到匯率 begin */
			/*if($('transferAmount').value< 1000){*/
			if( transferAmount < 1000 && debitAmount == "" ){
			/* Modify by long_zg 2019-05-21 UAT6-195 COB：海外匯款JPY輸入扣款幣金額卻提示金額過小，沒有拿到匯率 end */
		
				alert('<set:label name="err.jpy.least" rb="app.cib.resource.common.errmsg"/>');
				$('transferAmount').select();
				return false;
			}
			//add by linrui end  
		}     
		if (document.getElementById("showFromCurrency").innerHTML =='JPY' || document.getElementById("showFromCurrency").innerHTML =='KRW') {
			if (!checkInteger($('debitAmount').value)) {
				alert('<set:label name="err.txn.DecimalNotAllowed" rb="app.cib.resource.common.errmsg"/>');
				$('debitAmount').select();
				return false;
			}
			//add by linrui for JPY
			
			/* Modify by long_zg 2019-05-21 UAT6-195 COB：海外匯款JPY輸入扣款幣金額卻提示金額過小，沒有拿到匯率 begin */
			/*if($('debitAmount').value< 1000){*/
			if( debitAmount < 1000 && transferAmount == ""){
			/* Modify by long_zg 2019-05-21 UAT6-195 COB：海外匯款JPY輸入扣款幣金額卻提示金額過小，沒有拿到匯率 end */
				
				alert('<set:label name="err.jpy.least" rb="app.cib.resource.common.errmsg"/>');
				$('debitAmount').select();
				return false;
			}
			//add by linrui end    
		}   
		setFormDisabled('form1');
		document.getElementById("form1").submit();
	}
}
function checkInteger(amt) {
	if (amt.indexOf('.') > 0) {
		return false;
	} else {
		return true;
	}
}
function changeFromAcc(fromAcc){
	var fromAccount = document.getElementById("fromAccount");
	var from_ccy = fromAccount.options[fromAccount.selectedIndex].innerHTML;
	var arr = from_ccy.split("-");
	from_ccy = arr[1].substring(1,4);
	//add by linrui for mul-ccy	
    fromAcc = arr[0].replace(/(^\s*)|(\s*$)/g, "");
	if (fromAcc != '0' && fromAcc != '') { //modify by hjs
		document.getElementById("fromCurrency").value= from_ccy;
		document.getElementById("chargeCurrency").value= from_ccy;
		var url = '/cib/jsax?serviceName=AccInTxnService&showFromAcc=' + fromAcc + '&from_ccy=' + from_ccy  +'&language='+language;//mod by linrui for mul-language20171117 for mul-language201711117
   	    //registerElement('showAmount');
        //registerElement('showFromCurrency');
		getMsgToElement(url, fromAcc, '', null,false,language);
		document.getElementById("showAmountArea").style.display = "block";
	}else{
		document.getElementById("showAmountArea").style.display = "none";
	}
}
function changeRequestType(requestType){
	if (requestType=='2') {
		document.getElementById("showTransferDate").style.display="block";
	} else {
		document.getElementById("showTransferDate").style.display="none";
	}
}
function changeToCcy(toCcy){
	if (toCcy != '0') { //modify by hjs
		document.getElementById("showToCurrency").innerHTML=toCcy;
		if(toCcy == ""){
			document.getElementById("showToCurrency").setAttribute("style", "");
		}else{
			document.getElementById("showToCurrency").setAttribute("style", "display: inline-block;margin-right:10px;");
		}
	}
	// add by Jet
	document.form1.chargeBy[0].disabled = false;
	document.form1.chargeBy[1].disabled = false;
	document.form1.chargeBy[2].disabled = false;
	document.form1.chargeBy[0].checked = false;
	document.form1.chargeBy[1].checked = false;
	document.form1.chargeBy[2].checked = false;
	document.getElementById("chargeMessageArea").style.display = "none";
	
}

//get from acct balance
function changeFromCcy(fromCcy){
	var action = 'fromCcy';
	var fromAcct = document.getElementById("fromAccount").value ;
	document.getElementById("fromCurrency").value= fromCcy;
	if (fromAcct != '0' && fromAcct != '') { 
		//add by linrui for mul-ccy		
		var url = '/cib/jsax?serviceName=AccInTxnService&action='+action+ '&showFromAcc=' + fromAcct + '&fromCcy=' + fromCcy +'&language='+language;
		getMsgToElement(url, fromAcct, '', null,false,language);
		document.getElementById("showAmountArea").style.display = "block";
		if(fromCcy == ""){
			document.getElementById("showFromCurrency").setAttribute("style", "");		
		}else{
			document.getElementById("showFromCurrency").setAttribute("style", "display: inline-block;margin-right:10px;");			
		}				
	}else{
		document.getElementById("showAmountArea").style.display = "none";
	}
}

/*
function changeCgd(bank){
	if (bank != '0') { //modify by hjs
		var url = '/cib/jsax?serviceName=CgdMacauService&bank=' + bank;
   		registerElement('cgd_flag');
   		showMsgToElement(url, bank, '', null, false);
		var cgdValue = $F('cgd_flag');
		document.form1.chargeBy[0].disabled = false;
		document.form1.chargeBy[1].disabled = false;
		document.form1.chargeBy[0].checked = false;
		document.form1.chargeBy[0].checked = false;
		if (cgdValue == "Y") {
			document.form1.chargeBy[0].checked = true;
			document.form1.chargeBy[1].checked = false;
			document.form1.charchargeByeBy[1].disabled = true;
	    }
	}
}
*/

//add by lzg for mul-ccy select GAPMC-EB-001-0050
function getParams(originSelect, targetSelect) {
	var targetType = 'targetType=object';
	var targetId = 'targetId=' + originSelect.id;
	var originValue = 'originValue=' + originSelect.value;
	var subListId = 'subListId=' + targetSelect.id;

	var params = '';
	params = params + targetType + '&' + targetId + '&' + originValue + '&' + subListId;
	return params;
}
//get ccy list from fromacct
function showFromCcyList(originSelect, targetSelect, action, fromAcc){
	//clearAll();
	//action check ccy
	document.getElementById("fromAccount").value= fromAcc;
	if ((originSelect.value != null) && (originSelect.value != '')) {
		   var params = getParams(originSelect, targetSelect);
		   var url = '/cib/jsax?serviceName=AccInTxnService&action='+action+'&'+ params+ '&showFromAcc=' + fromAcc +'&language='+language;
		   getMsgToSelect(url,'', null, false,language);
	}
	
	document.getElementById("showAmountArea").style.display = "none";
  	
  	var chargeBy = document.getElementsByName("chargeBy");
	for(var i = 0; i < chargeBy.length; i++){
		if(chargeBy[i].value == "S"){
			if(document.getElementById("fromAccount").selectedIndex != 0){
				chargeBy[i].disabled=false;
			}else{
				chargeBy[i].disabled=true;
			}
		}
	}
	
	//add by lzg 20190612
	var fromCurrency = document.getElementById("fromCurrency");
	if(fromCurrency.options.length == 2){
		fromCurrency[1].selected = true;
		changeFromCcy(fromCurrency[1].value);
	}
	//add by lzg end
}
//add by lzg end
//add by linrui 2190723
function remarkLength(str){
	var strLength = str.replace(/[\u4e00-\u9fa5]/g,"***").length;
	if(strLength>120){
		//alert(strLength);
		alert('<set:label name="err.sys.reference.maxerror" rb="app.cib.resource.common.errmsg"/>');
		return false;
	}
	return true;
	
}
//end

</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/transferTemplateInMacau.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.transfer_macau" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitleTemplate" defaultvalue="BANK Online Banking >Transfer > Transfer Templates"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitleEdit" defaultvalue="TRANSFER Templates Edit"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/transferTemplateInMacau.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><set:messages width="100%" cols="1" align="center"/>
                      <set:fieldcheck name="templateInMacau" form="form1" file="transfer_template_macau" />                    </td>
                  </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr >
                    <td colspan="4" class="groupinput" ><set:label name="Fill_Info" defaultvalue="Fill the following Information"/></td>
                  </tr>
                  <tr>
                    <td class="label1"  colspan="4"><span>&nbsp;&nbsp;</span><b><set:label name="Transfer_From" defaultvalue="Transfer From"/> </b></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><span class = "xing">*</span><set:label name="From_Account" defaultvalue="Account Number"/></td>
                    <td class="content1" colspan="3" >
                    <!-- modified by lzg for GAPMC-EB-001-0050 -->
                    <!--<select id="fromAccount" name="fromAccount" nullable="0" onChange="changeFromAcc(this.value)">
				<option selected value="" selected><set:label name="select_account" defaultvalue="----- Select  an Account ------"/></option>
                  <set:list name="transferuser"> <option value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_INFO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_INFO' />
                      </option>
                  </set:list>
                </select> 
					  <div name="showAmountArea" id="showAmountArea" style="display: none"><table border="0" cellpadding="0" cellspacing="0">
                    <tr><td><set:label name="available_balance" defaultvalue="Available Balance :"/>&nbsp;</td>
                      <td><div name="showAmount" id="showAmount"></div></td>
                    </tr></table></div></td>
                  </tr>
                  -->
                  <select id="fromAccount" name="fromAccount" nullable="0" onChange="showFromCcyList(this, $('fromCurrency'),'getFromCcyRemittance', this.value);">
				<option selected value="" selected><set:label name="select_account" defaultvalue="----- Select  an Account ------"/></option>
                  <set:list name="transferuser"> <option  value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_NO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_NO' />
                      </option>
                  </set:list>
                  </select>
                  </td>
              </tr>
              <tr>
                  <td class="label1"><span class = "xing">*</span><set:label name="From_ccy" defaultvalue="From Currency"/></td>
                  <td class="content1" colspan="3" ><table><tr><td><select name="fromCurrency" id="fromCurrency" nullable="0"  onChange="changeFromCcy(this.value);">
                      <option  value="" selected><set:label name="select_ccy" defaultvalue="----Please select Currency----"/></option>
                    </select>  </td></tr></table>
                    
                    <div name="showAmountArea" id="showAmountArea" style="display: none"><table border="0" cellpadding="0" cellspacing="0">
                    <tr><td><set:label name="availd_balance" defaultvalue="Available Balance :"/>&nbsp;</td><td> <div name="showAmount"  format="amount" id="showAmount" ></div></td></tr></table></div>
                  </td>
               </tr>
                <!-- modified by lzg end -->
                <!-- modified by lzg 20150522 -->
                  <!--<tr>
                    <td class="label1"><set:label name="Currency" defaultvalue="To Currency"/></td>
                    <td class="label1" colspan="3"><set:label name="first_select_ccy_info" defaultvalue="First select the currency and then input the amount"/> <br>
                      <span class="content1">
                      <select id="toCurrency" name="toCurrency" nullable="0" onChange="changeToCcy(this.value)">
                        <option value=""><set:label name="select_ccy" defaultvalue="----- Select a Currency ------"/></option>
                        <set:rblist db="currencyMacau">
                          <set:rboption name="toCurrency"/>
                        </set:rblist>
                      </select>
                    </span></td>
                  </tr>
                  -->
                  <!-- modified by lzg end -->
                  <tr class="greyline">
                  <td class="label1" colspan="4"><span class = "xing">*</span><b><set:label name="remittance" /></b>&nbsp;(<span style = "color:#838383;"><set:label name="Transfer_Amount_Info1" /></span>)</td>
                </tr>
                <!-- add by lzg 20190522 -->
                  <tr>
                    <td class="label1"><span class = "xing">*</span><set:label name="Currency" defaultvalue="To Currency"/></td>
                    <td class="label1" colspan="3">
                      <span class="content1">
                      <select id="toCurrency" name="toCurrency" nullable="0" onChange="changeToCcy(this.value)">
                        <option value=""><set:label name="select_ccy" defaultvalue="----- Select a Currency ------"/></option>
                        <set:rblist db="currencyMacau">
                          <set:rboption name="toCurrency"/>
                        </set:rblist>
                      </select>
                    </span></td>
                  </tr>
                  <!-- add by lzg end -->
                  <tr class="greyline">
                    <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Amount_to_be_Transferred" defaultvalue="Amount to be Transferred"/></td>
                    <td class="content1" colspan="3"><div name="showToCurrency" id="showToCurrency"><set:data name="toCurrency" db="rcCurrencyCBS"/></div><input size = "24" placeholder="<set:label  name = 'amount_input_tips'/>" id="transferAmount" name="transferAmount" type="text" value="<set:data name='transferAmount' format='amount'/>"  maxlength="20" onFocus ="document.form1.debitAmount.value='';"></td>
                  </tr>
                  <tr >
                    <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Amount_to_be_debited" defaultvalue="Amount to be debited from the selected account above "/></td>
                    <td class="content1" colspan="3"><div name="showFromCurrency" id="showFromCurrency"><set:data name="fromCurrency" db="rcCurrencyCBS"/></div><input size = "24" placeholder="<set:label  name = 'amount_input_tips'/>" id="debitAmount" name="debitAmount" type="text" value="<set:data name='debitAmount' format='amount'/>"  maxlength="20"    onFocus ="document.form1.transferAmount.value='';"></td>
                  </tr>
                  <tr >
                  <td class="label1"  width="24%"></td>
                  <td width="38%" class="label1"></td>
                  <td width="5%" align="right" class="content1"></td>
                  <td width="33%" class="content1"></td>
                </tr>
                  <tr class="greyline">
					 <td class="label1" colspan="4"><span>&nbsp;&nbsp;</span><b><set:label name="Transfer_To" defaultvalue="Transfer To"/></b></td>
                  </tr>
                  <tr >
                    <td valign="top" class="label1"><span class = "xing">*</span><set:label name="Beneficiary_Name" defaultvalue="Beneficiary Name "/></td>
                    <td class="content1" colspan="3"><input placeholder="<set:label  name = 'common_input_tips'/>" id="beneficiaryName1" name="beneficiaryName1" type="text" value="<set:data name='beneficiaryName1'/>" size="40" maxlength="35">
                      <br>
                    <input placeholder="<set:label  name = 'common_input_tips'/>" id="beneficiaryName2" name="beneficiaryName2" type="text" value="<set:data name='beneficiaryName2'/>" size="40" maxlength="35"></td>
                    <input type="hidden" name="beneJustify" id="beneJustify" value="N" />
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><span class = "xing">*</span><set:label name="Beneficiary_Bank" defaultvalue="Beneficiary Bank Name"/></td>
                    <td class="content1" colspan="3" ><select id="beneficiaryBank" name="beneficiaryBank" nullable="0" >
                      <option value=""><set:label name="select_beneficiary_bank" defaultvalue="----- Select a Beneficiary Bank ------"/></option>
                      <set:rblist db="localBank">
                        <set:rboption name="beneficiaryBank"/>
                      </set:rblist>
                    </select></td>
                  </tr>
                  <tr >
                    <td class="label1"><span class = "xing">*</span><set:label name="Beneficiary_Account" defaultvalue="Account Number "/></td>
                    <td  class="content1" colspan="3"><input placeholder="<set:label  name = 'common_input_tips'/>" id="toAccount" name="toAccount" type="text" value="<set:data name='toAccount'/>" size="45" maxlength="34"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Message_Send" defaultvalue="Message to be sent "/></td>
                    <td  class="content1" colspan="3"><input placeholder="<set:label  name = 'common_input_tips'/>" id="messsage" name="messsage" type="text" value="<set:data name='messsage'/>" size="45" maxlength="35">
					<br><input placeholder="<set:label  name = 'common_input_tips'/>" id="messsage2" name="messsage2" type="text" value="<set:data name='messsage2'/>" size="45" maxlength="35">
					<br><input placeholder="<set:label  name = 'common_input_tips'/>" id="messsage3" name="messsage3" type="text" value="<set:data name='messsage3'/>" size="45" maxlength="35">					
					</td>
                  </tr>
                <tr>
                  <td class="label1"><span class = "xing">*</span><set:label name="Commission_charges_by" defaultvalue="Commission and charges to be paid to banks overseas by "/></td>
                  <!-- modified by lzg for GAPMC-EB-001-0050 -->
                  <!--<td class="content1" colspan="3" ><set:rblist file="app.cib.resource.txn.charge_name"> <set:rbradio name="chargeBy"/> </set:rblist> </td>
                -->
                  <td class="content1" colspan="3">
                  	<input name = "chargeBy" type = "radio" value = "S" onchange = "hiddenContent(this.value)" disabled /><set:label name='S'/>
                  	<input name = "chargeBy" type = "radio" value = "P" onchange = "hiddenContent(this.value)" /><set:label name='P'/>
                  	<input name = "chargeBy" type = "radio" value = "O" onchange = "hiddenContent(this.value)" /><set:label name='O'/>
                  </td>
                  <!-- modified by lzg end -->	
                </tr>
                
                <!-- add by linrui 20190423 -->
                <!-- add by lzg for GAPMC-EB-001-0050 -->
                <tr id = "hiddenContent" class="greyline">
                  <td class="label1"><span class = "xing">*</span><set:label name="Deduct_Charge_from_Account" defaultvalue="Deduct Charge from Account"/></td>
                  <td class="content1" colspan="3" >
                <!-- add by lzg end -->
                <!-- modified by lzg for  GAPMC-EB-001-0050-->
                  <!--<select id="chargeAccount" name="chargeAccount" nullable="0" onChange="chargeAcc(this.value)">
				  <option selected value=""><set:label name="select_account" defaultvalue="----- Select  an Account ------"/></option>
                  <set:list name="transferuser"> <option value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_INFO' equals='selectChargeAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_INFO' />
                      </option>
                  </set:list>
                  </select>
                  -->
                  <select id="chargeAccount" name="chargeAccount" nullable="0" >
				<option selected value="" selected><set:label name="select_account" defaultvalue="----- Select  an Account ------"/></option>
                  <set:list name="transferuser"> <option  value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_NO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_NO' />
                      </option>
                  </set:list>
                  </select>
                  <!-- modified by lzg end -->
                  </td>
                </tr>
				<!-- end 20190423 -->
                
                <!-- add by lzg 20190603 -->
				<tr >
                  <td class="label1"><span class = "xing">*</span><set:label name="Purpose" defaultvalue="Purpose"/></td>
                  <td  class="content1" colspan="3">
                  	<select id="purposeCode" name="purposeCode">
                  	  <option value = ""><set:label name = "Select_Purpose" /></option>
                      <set:rblist file="app.cib.resource.txn.purposecode"><set:rboption name="purposeCode"/></set:rblist>
                   </select>
                  </td>
                </tr>
                <!-- add by lzg end -->
                
                 <tr class="greyline">
                    <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Remark" defaultvalue="Remark"/></td>
                    <td class="content1" colspan="3"><input placeholder="<set:label  name = 'common_input_tips'/>" id="remark" name="remark" type="text" value="<set:data name='remark'/>" size="45" maxlength="120" ></td>
                  </tr>
				<tr>
                  <td class="label1" colspan="2"><div name="chargeMessageArea" id="chargeMessageArea" style="display: none"><font color="#FF0000"><set:label name="Charge_Message" defaultvalue="* Additional charges may be levied by the beneficiary banks"/></font></div></td>
				</tr>								
                </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <!-- add by lzg 20190522 -->
             <%@ include file="/common/transferTips.jsp" %>
              <!-- add by lzg end -->
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
			   <td height="40" colspan="2" class="sectionbutton">
				  <input id="submit1" name="submit1" type="button" value="<set:label name='buttonOK' defaultvalue=' OK ' />" tabindex="3"  onClick="doSubmit();">
<!--                  <input id="bottonReset" name="bottonReset" type="reset" onClick="document.getElementById('showAmountArea').style.display='none';" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
				  <input id="ActionMethod" name="ActionMethod" type="hidden" value="editTemplate">
				  <input id="fromCurrency" name="fromCurrency" type="hidden" value="">
                   <input id="chargeCurrency" name="chargeCurrency" type="hidden" value="">
				   <input name="cgd_flag" type="hidden" id="cgd_flag">
				   <!-- add by lzg 20190731 -->
				<input id="language" name = "language" type = "hidden" value = "">
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
