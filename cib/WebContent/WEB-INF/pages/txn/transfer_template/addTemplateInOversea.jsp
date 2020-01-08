<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.transfer_oversea">
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
	//modified by lzg for GAPMC-EB-001-0050
	//if ($('fromAccount')) {
		//changeFromAcc($F('fromAccount'));
	//}
	//if ($('toCurrency')) {
		//changeToCcy($F('toCurrency'));
	//}
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
		
		if(!"<set:data name='chareBy' />" == ""){
			var chareBy = document.getElementsByName("chareBy");
			for(var i = 0; i < chareBy.length; i++){
				if(chareBy[i].value == "<set:data name='chareBy' />"){
					chareBy[i].checked=true;
					hiddenContent("<set:data name='chareBy' />");
					break;
				}
			}
		}else{
			var chareBy = document.getElementsByName("chareBy");
			for(var i = 0; i < chareBy.length; i++){
				if(chareBy[i].value == "P"){
					chareBy[i].checked=true;
					hiddenContent("P");
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
	
	setTimeout(function(){
	
		if(!"<set:data name='beneficiaryCountry' />" == ""){
			var beneficiaryCountry = document.getElementById("beneficiaryCountry");
			for(var i=0; i<beneficiaryCountry.options.length;i++){
		    	if(beneficiaryCountry[i].value=='<set:data name='beneficiaryCountry' />'){
		        	beneficiaryCountry[i].selected=true;
		        	showList(beneficiaryCountry,$('beneficiaryCity'),'country');
	    		}  
			}	
		}
		
		if(!"<set:data name='beneficiaryCity' />" == ""){
		var beneficiaryCity = document.getElementById("beneficiaryCity");
		for(var i=0; i<beneficiaryCity.options.length;i++){
		    if(beneficiaryCity[i].value=='<set:data name='beneficiaryCity' />'){
	        	beneficiaryCity[i].selected=true;
	        	otherCityControl('<set:data name='beneficiaryCity' />');    
	    	}  
		}	
		}
		
		if(!"<set:data name='beneficiaryBank1' />" == ""){
		var beneficiaryBank1 = document.getElementById("beneficiaryBank1");
		for(var i=0; i<beneficiaryBank1.options.length;i++){
		    if(beneficiaryBank1[i].value=='<set:data name='beneficiaryBank1' />'){
	        	beneficiaryBank1[i].selected=true;
	        	otherBankControl('<set:data name='beneficiaryBank1' />');
	    	}  
		}	
		}
	},500);
}
function onLoadCallback(){
	$('beneficiaryCity').value = '<set:data name="beneficiaryCity" />';
	otherCityControl($F('beneficiaryCity'));
	$('beneficiaryBank1').value = '<set:data name="beneficiaryBank1" />';
	otherBankControl($F('beneficiaryBank1'));
	$('swiftAddress').value = '<set:data name="swiftAddress"/>';
	if ($('beneficiaryBank1')) {
		if($F('beneficiaryBank1')!='0' && $F('beneficiaryBank1')!=''){
			changeCgd('<set:data name="beneficiaryBank1"/>')
		}
	}
	putFieldValues("spbankCode", "<set:data name='spbankCode' />");
	putFieldValues("accType", "<set:data name='accType' />");
	putFieldValues("<set:data name='accType' />", "<set:data name='showToAccount' />");
	//modified by lzg for GAPMC-EB-001-0050
	//if(!$("_accType").checked) {
		//$("toAccount").value = "";
	//}
	//modified by lzg end
//	changeRequestType('<set:data name="requestType"/>');
	putFieldValues("chareBy", "<set:data name='chareBy' />");
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
//add by lzg end

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
	if(((Trim(beneName1) != "") && (Trim(beneName2) == "")) && ((Trim(beneName1)).indexOf(" ")==-1)){
		document.getElementById("beneJustify").value="Y";
	}else if(((Trim(beneName1) == "") && (Trim(beneName2) != "")) && ((Trim(beneName2)).indexOf(" ")==-1)){
		document.getElementById("beneJustify").value="Y";
	}else{
		document.getElementById("beneJustify").value="N";
	}
	if(validate_templateInOversea(document.getElementById("form1"))){


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
function showSwiftAddr(value)
{
	if(value!=null && value!=''){
		var values = value.split(',');
	  if(values.length > 1){
	  	$('swiftAddress').value=values[1];
		return true;
	  }
	}
}
function clearAll(){
	clearCity();
	clearBank();
}
function clearCity(){
	//selectCityObj =$('beneficiaryCity');
	//selectCityObj.options.length=0;
	//var newCityOption = new Option('---Select a City---','');
	//selectCityObj.add(newCityOption);
	$('showOtherCity').style.display = "none";
	if ($('otherCity')) {
		$('otherCity').value="";
	}
}
function clearBank(){
	//selectObj =$('beneficiaryBank1');
	//selectObj.options.length=0;
	//var newOption = new Option('---Select a Beneficiary Bank---','');
	//selectObj.add(newOption);
	$('showOtherBank').style.display = "none";
	if ($('otherBank')) {
		$('otherBank').value="";
	}
}
//modified by lzg for GAPMC-EB-001-0050
function showList(originSelect, targetSelect,action, callback){
	if(callback == 'undefined'){
		callback = null;
	}
//	clearAll();
	if ((originSelect.value != null)) {
		var params = getParamsForCountry(originSelect, targetSelect);
		var url = '/cib/jsax?serviceName=TxnInOverseaService&action='+action+'&'+ params+'&language='+language;//mod by linrui for mul-language20171117
		//registerElement('showBankLabel');
		//registerElement('showAccountLabel');
		//registerElement('beneficiaryBank1');
		getMsgToSelect(url,'', callback, false,language);
		/*add by lw 20101101*/
		/*document.getElementById("proofVisible").value = ""; 
	    checkIfShowPurpose();
		document.getElementById('proofCheckBox').checked = false;*/
		/*add by lw end*/
		if(originSelect.value != null && originSelect.value == "OT"){
			document.getElementById("beneficiaryCity").options[1].selected = "true";
			otherCityControl("OTH");
		}
  	}
  	
}
//modified by lzg end
function otherBankControl(bankCode){
	$('showOtherBank').style.display = "none";
	document.getElementById("swiftAddress").readOnly = "readOnly";
	if($('otherBank')) {
		$('otherBank').value="";
	}
	showSwiftAddr(bankCode);
	if(bankCode!=null && bankCode=='other'){
		$('showOtherBank').style.display = "block";
		document.getElementById("swiftAddress").readOnly = "";
	}
	putFieldValues("otherBank", "<set:data name='otherBank' />");
}
function otherCityControl(cityCode){
	if(cityCode == "OTH"){
		var beneficiaryBank1 =  document.getElementById("beneficiaryBank1")
		for(var i = 0; i < beneficiaryBank1.options.length;i++){
			if(beneficiaryBank1.options[i].value == "other"){
				beneficiaryBank1.options[i].selected = "true";
			}
		}
		otherBankControl("other");
	}
}
//modiied by lzg for GAPMC-EB-001-0050
function getParamsForCountry(originSelect, targetSelect) {
	var targetType = 'targetType=object';
	var targetId = 'targetId=' + originSelect.id;
	var originValue = 'originValue=' + originSelect.value;
	var subListId = 'subListId=' + targetSelect.id;
	var subListId2 = 'subListId2=' + $('beneficiaryBank1').id;
	var params = '';
	params = params + targetType + '&' + targetId + '&' + originValue + '&' + subListId+ '&' +subListId2;
	return params;
}
//modified by lzg end
function changeFromAcc(fromAcc){
	var fromAccount = document.getElementById("fromAccount");
	var from_ccy = fromAccount.options[fromAccount.selectedIndex].innerHTML;
	var arr = from_ccy.split("-");
	from_ccy = arr[1].substring(1,4);
	//add by linrui for mul-ccy	
    fromAcc = arr[0].replace(/(^\s*)|(\s*$)/g, "");
	if (fromAcc != '0' && fromAcc != '') { //modify by hjs
		document.getElementById("fromCurrency").value= from_ccy;
		var url = '/cib/jsax?serviceName=AccInTxnService&showFromAcc=' + fromAcc + '&from_ccy=' + from_ccy  +'&language='+language;//mod by linrui for mul-language20171117 for mul-language201711117
   		//registerElement('showAmount');
   		//registerElement('showFromCurrency');
		getMsgToElement(url, fromAcc, '', null,false,language);
		document.getElementById("showAmountArea").style.display = "block";
	}else{
		document.getElementById("showAmountArea").style.display = "none";
	}
}
// Jet added 2008-1-4
function chargeAcc(fromAcc){
	if (fromAcc != '0' && fromAcc != '') {
		var chargeAccount = document.getElementById("chargeAccount");
    	var charge_ccy = chargeAccount.options[chargeAccount.selectedIndex].innerHTML;
    	var arr = charge_ccy.split("-");
    	charge_ccy = arr[1].substring(1,4);
        charge_ccy = charge_ccy.replace(/(^\s*)|(\s*$)/g, "");
        fromAcc = arr[0].replace(/(^\s*)|(\s*$)/g, "");
    	document.getElementById("chargeCurrency").value= charge_ccy;
    	document.getElementById("chargeAccount").value = fromAcc;
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
	if (toCcy != '0') {
		document.getElementById("showToCurrency").innerHTML=toCcy;
		if(toCcy == ""){
			document.getElementById("showToCurrency").setAttribute("style", "");
		}else{
			document.getElementById("showToCurrency").setAttribute("style", "display: inline-block;margin-right:10px;");
		}
	}
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
		document.getElementById("showFromCurrency").setAttribute("style", "");		
	}
}

function changeCgd(bank){
	if (bank != '0') { //modify by hjs
		var url = '/cib/jsax?serviceName=CgdOverseaService&bank=' + bank;
   		//registerElement('cgd_flag');
   		getMsgToElement(url, bank, '', null, false,language);
		var cgdValue = $F('cgd_flag');
//		document.form1.chareBy[0].disabled = false;
//		document.form1.chareBy[1].disabled = false;
//		document.form1.chareBy[0].checked = false;
//		document.form1.chareBy[1].checked = false;
		/*
	    if (cgdValue == "Y") {
			document.form1.chareBy[0].checked = true;
			document.form1.chareBy[1].checked = false;
			document.form1.chareBy[1].disabled = true;
	   }
	   */ 
	}
}

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
	
	var chareBy = document.getElementsByName("chareBy");
	for(var i = 0; i < chareBy.length; i++){
		if(chareBy[i].value == "S"){
			if(document.getElementById("fromAccount").selectedIndex != 0){
				chareBy[i].disabled=false;
			}else{
				chareBy[i].disabled=true;
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

//add by lzg for Correspondent Bank
function chosenStyle(targetDom){
	targetDom.style.color  = "#002D72";
	targetDom.style.fontWeight  = "bold";
}

function outStyle(targetDom){
	targetDom.style.color  = "";
	targetDom.style.fontWeight  = "";
}

function hiddenSearchArea(domValue,operator,searchType){
	setTimeout( function(){
		if(operator == "bankName"){
			if(searchType == "correspondentBank"){
				document.getElementById("showBankNameArea").style.display = "none";
			}else{
				document.getElementById("showotherBankNameArea").style.display = "none";
			}
		}else{
			if(searchType == "correspondentBank"){
				document.getElementById("showCodeArea").style.display = "none";
			}else{
				document.getElementById("showSwifrAddressArea").style.display = "none";
			}
		}
	}, 200 );
}

var showSwiftFlag = "N";
var showBankFlag = "N";
function showSearchArea(domValue,operator,searchType){
	if(domValue != ""){
		if(operator == "bankName"){
			if(showBankFlag == "Y"){
				if(searchType == "correspondentBank"){
					document.getElementById("showBankNameArea").style.display = "";	
				}else{
					document.getElementById("showotherBankNameArea").style.display = "";
				}
			}
		}else{
			if(showSwiftFlag == "Y"){
				if(searchType == "correspondentBank"){
					document.getElementById("showCodeArea").style.display = "";	
				}else{
					document.getElementById("showSwifrAddressArea").style.display = "";	
				}
			}
		}
	}
}

function setCorrespondentBank(swiftCode,BankName,operator,searchType){
	if(operator == "swiftCode"){
		if(searchType == "correspondentBank"){
			document.getElementById("showCodeArea").style.display = "none";	
		}else{
			document.getElementById("showSwifrAddressArea").style.display = "none";	
		}
		showSwiftFlag = "N";
	}else{
		if(searchType == "correspondentBank"){
			document.getElementById("showBankNameArea").style.display = "none";
		}else{
			document.getElementById("showotherBankNameArea").style.display = "none";
		}
		showBankFlag = "N";
	}
	if(searchType == "correspondentBank"){
		document.getElementById("correspondentBankCode").value = swiftCode.trim();
		document.getElementById("correspondentBankName").value = BankName;	
	}else{
		document.getElementById("swiftAddress").value = swiftCode.trim();
		document.getElementById("otherBank").value = BankName;
	}
}


var currentTimes = 1;
function loadMore(showArea,operator,searchType){
	var scrollTop = showArea.scrollTop;
	var clientHeight = showArea.clientHeight;
	var scrollHeight = showArea.scrollHeight;
	if(scrollHeight > clientHeight && scrollTop + clientHeight === scrollHeight) {
  		if(times > 0){
  			showcorrBankCode(operator,20*currentTimes,20*currentTimes+20,'N',searchType);
  			times = times -1;
  			currentTimes = currentTimes+1;
  		}
	}
}

var times = 0;
function showcorrBankCode(operator,firstRecord,endRecord,isFirstSearch,searchType){
	if(operator == "swiftCode"){
		if(searchType == "correspondentBank"){
			var arg = document.getElementById("correspondentBankCode").value;
		}else{
			var arg = document.getElementById("swiftAddress").value;
		}
		if(arg == ""){
			if(searchType == "correspondentBank"){
				var showCodeArea = document.getElementById("showCodeArea");
			}else{
				var showCodeArea = document.getElementById("showSwifrAddressArea");
			}
			showCodeArea.style.display = "none";
			return;
		}
	}else if(operator == "bankName"){
		if(searchType == "correspondentBank"){
			var arg = document.getElementById("correspondentBankName").value;
		}else{
			var arg = document.getElementById("otherBank").value;
		}
		if(arg == ""){
			if(searchType == "correspondentBank"){
				var showBankNameArea = document.getElementById("showBankNameArea");
			}else{
				var showBankNameArea = document.getElementById("showotherBankNameArea");
			}
			showBankNameArea.style.display = "none";
			return;
		}
	}
	var url = "/cib/jsax?serviceName=GetCorrespondentBank&operator=" + operator 
			+ "&arg=" + arg + "&isFirstSearch=" + isFirstSearch
			+ "&firstRecord=" + firstRecord + "&endRecord=" + endRecord;
	getMsgToElement(url,'', '',function(){
		if(operator == "swiftCode"){
			if(searchType == "correspondentBank"){
				var showArea = document.getElementById("showCodeArea");	
			}else{
				var showArea = document.getElementById("showSwifrAddressArea");	
			}
			showSwiftFlag = "Y";
		}else{
			if(searchType == "correspondentBank"){
				var showArea = document.getElementById("showBankNameArea");
			}else{
				var showArea = document.getElementById("showotherBankNameArea");
			}
			showBankFlag = "Y";
		}
		if(isFirstSearch == ""){
			var count = document.getElementById("GetCorrespondentBankResultCount").value;
			times = parseInt(count/20);
			currentTimes = 1;
			showArea.innerHTML = "";
		}
		var GetCorrespondentBankResult = document.getElementById("GetCorrespondentBankResult").value;
		GetCorrespondentBankResult = GetCorrespondentBankResult.replace(/\n/g, "");
		var obj =  JSON.parse(GetCorrespondentBankResult);
		var swiftCodeLabel = "<set:label name='swiftCodeLabel' />";
		for(var i = 0; i < obj.length; i++){
			var div = document.createElement("div");
			div.setAttribute("style", "border-bottom: 1px solid #A9A9A9;padding-bottom: 2px;cursor: pointer;");
			div.setAttribute("onmouseover", "chosenStyle(this)");
			div.setAttribute("onmouseout", "outStyle(this)");
			if(language == "en_US"){
				div.setAttribute("onclick", "setCorrespondentBank('" + obj[i].BICD_SWF_CD + "','" + obj[i].BICD_SWF_NAME1.trim()+ obj[i].BICD_SWF_NAME2.trim() + obj[i].BICD_SWF_NAME3.trim() + "','"+operator+"','"+searchType+"')");
			}else{
				if(obj[i].BICD_CH_NAME.trim() != ""){
					div.setAttribute("onclick", "setCorrespondentBank('" + obj[i].BICD_SWF_CD + "','" + obj[i].BICD_CH_NAME + "','"+operator+"','"+searchType+"')");
				}else{
					div.setAttribute("onclick", "setCorrespondentBank('" + obj[i].BICD_SWF_CD + "','" + obj[i].BICD_SWF_NAME1.trim()+ obj[i].BICD_SWF_NAME2.trim() + obj[i].BICD_SWF_NAME3.trim() + "','"+operator+"','"+searchType+"')");
				}
			}
			var swiftCodeDiv = document.createElement("div");
			swiftCodeDiv.setAttribute("style", "font-weight: bold;padding: 0px;margin: 0px;");
			swiftCodeDiv.innerHTML =swiftCodeLabel +  obj[i].BICD_SWF_CD;
			div.appendChild(swiftCodeDiv);
			
			var bankNameDiv = document.createElement("div");
			bankNameDiv.setAttribute("style", "padding: 0px;margin: 0px;");
			if(language == "en_US"){
				bankNameDiv.innerHTML = obj[i].BICD_SWF_NAME1.trim()+ obj[i].BICD_SWF_NAME2.trim() + obj[i].BICD_SWF_NAME3.trim();
			}else{
				if(obj[i].BICD_CH_NAME.trim() != ""){
					bankNameDiv.innerHTML = obj[i].BICD_CH_NAME;
				}else{
					bankNameDiv.innerHTML = obj[i].BICD_SWF_NAME1.trim()+ obj[i].BICD_SWF_NAME2.trim() + obj[i].BICD_SWF_NAME3.trim();
				}
			}
			div.appendChild(bankNameDiv);
			showArea.appendChild(div);
		}
		if(obj.length == 0){
			var noRecords = "<set:label name='noRecords' />";
			showArea.style.textAlign = "center";
			showArea.style.height = "50";
			showArea.style.paddingTop = "20";
			showArea.innerHTML = noRecords;
		}else{
			showArea.style.textAlign = "";
			showArea.style.height = "200";
			showArea.style.paddingTop = "";
		}
		showArea.style.display = "";
		//showArea.focus();
	}, false,language) ;
	
}
//add by lzg end

</script>

<style type="text/css">
	#clickSearchOtherBank{
		text-decoration: none;
		margin: 1px;
	}
	#clickSearchOtherBank:HOVER {
		color:#50A6EF;
	}
	
</style>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/transferTemplateInOversea.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.transfer_oversea" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
        <set:label name="navigationTitleTemplate" defaultvalue="BANK Online Banking >Transfer > Transfer Templates"/>
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
        <set:label name="functionTitleAdd" defaultvalue="TRANSFER TEMPLATES ADD"/>
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
		  <form action="/cib/transferTemplateInOversea.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><set:messages width="100%" cols="1" align="center"/>
                      <set:fieldcheck name="templateInOversea" form="form1" file="transfer_template_oversea" />                    </td>
                  </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td colspan="4" class="groupinput"><set:label name="Fill_Info" defaultvalue="Fill the following Information"/>
                    </td>
                  </tr>
                  <tr >
                    <td width="20%" class="label1",colspan="3"><span>&nbsp;&nbsp;</span><b>
                      <set:label name="Transfer_From" defaultvalue="Transfer From"/>
                      </b>
                        <input id="action" type="hidden" name="action"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><span class = "xing">*</span><set:label name="Transfer_Account" defaultvalue="Account Number"/></td>
                    <td class="content1" valign="top" colspan="4" >
                    <!-- modified by lzg for GAPMC-EB-001-0050 -->
                    <!--<select id="fromAccount" name="fromAccount" nullable="0" onChange="changeFromAcc(this.value)">
				<option selected value="" selected><set:label name="select_account" defaultvalue="----- Select  an Account ------"/></option>
                  <set:list name="transferuser"> <option value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_INFO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_INFO' />
                      </option>
                  </set:list>
                </select>
                        <div name="showAmountArea" id="showAmountArea" style="display: none">
                          <table border="0" cellpadding="0" cellspacing="0">
                            <tr>
                              <td><set:label name="available_balance" defaultvalue="Available Balance :"/>&nbsp;</td>
                              <td><div name="showAmount" id="showAmount"></div></td>
                            </tr>
                          </table>
                        </div>
                        </td>
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
                 <tr class="greyline">
                  <td class="label1" colspan="4"><span class = "xing">*</span><b><set:label name="remittance" /></b>&nbsp;(<span style = "color:#838383;"><set:label name="Transfer_Info2" /></span>)</td>
                </tr>
                 <tr >
                  <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Currency" defaultvalue="Currency"/></td>
                  <td class="content1" colspan="3" ><select id="toCurrency" name="toCurrency" nullable="0" onChange="changeToCcy(this.value)">
                      <option value="" selected><set:label name="select_currency" defaultvalue="----- Select a Currency ------"/></option>
                      <set:rblist db="currencyOversea"> <set:rboption name="toCurrency"/> </set:rblist>
                    </select>
                  </td>
                </tr>
                <tr class="greyline">
                	<td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Amount_to_be_Transferred" /></td>
                	<td class="content1" colspan="3"><div  name="showToCurrency" id="showToCurrency"><set:data name="toCurrency" db="rcCurrencyCBS"/></div><input size = "24" placeholder="<set:label  name = 'amount_input_tips'/>" id="transferAmount" name="transferAmount" type="text" value="<set:data name='transferAmount' format='amount' />"  maxlength="20" onFocus ="resetTransferAmount()" ></td>
                </tr>
                <tr >
                  <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Amount_to_be_debited" defaultvalue="Amount to be debited from the selected account above "/></td>
                  <td  class="content1" colspan="3"><div  name="showFromCurrency" id="showFromCurrency"><set:data name="fromCurrency" db="rcCurrencyCBS"/></div><input size = "24" placeholder="<set:label  name = 'amount_input_tips'/>" id="debitAmount" name="debitAmount" type="text" value="<set:data name='debitAmount' format='amount'/>"  maxlength="20"  onFocus ="resetDebitAmount()"></td>
                </tr>
                  <tr class="greyline">
                    <td class="label1"  colspan="4"><span>&nbsp;&nbsp;</span><b>
                      <set:label name="Transfer_To" defaultvalue="Transfer To"/>
                    </b></td>
                  </tr>
                  <tr>
                    <td class="label1"><span class = "xing">*</span><set:label name="Beneficiary_Name" defaultvalue="Beneficiary Name "/></td>
                    <td  class="content1"  colspan="3"><input placeholder="<set:label  name = 'common_input_tips'/>" id="beneficiaryName1" name="beneficiaryName1" type="text" value="<set:data name='beneficiaryName1'/>" size="45" maxlength="35"><br>
					<input placeholder="<set:label  name = 'common_input_tips'/>" id="beneficiaryName2" name="beneficiaryName2" type="text" value="<set:data name='beneficiaryName2'/>" size="45" maxlength="35">
                    <input type="hidden" name="beneJustify" id="beneJustify" value="N" />
					</td>
                  </tr>
                  <!-- add by lzg 20190523 -->
                  <!--<tr >
                    <td class="label1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                    <td  class="content1" colspan="3"><div name="showAccountLabel" id="showAccountLabel"></div>
                        <table>
                          <tr>
                            <td class="label1"><input type="radio" name="accType" id="_accType" value="toAccount" ></td>
                            <td class="label1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                            <td class="content1"><input name="toAccount" id="toAccount" type="text" value="<set:data name='toAccount'/>" size="34" maxlength="34"></td>
                          </tr>
                      </table></td>
                  </tr>
                  -->
                  <tr class="greyline">
                  <td class="label1"><span class = "xing">*</span><set:label name="Beneficiary_Account" defaultvalue="Account Number"/></td>
                  <td  class="content1" colspan="3">
                  	<input placeholder="<set:label  name = 'common_input_tips'/>" name="toAccount" type="text" id="toAccount" value="<set:data name='toAccount' />" size="45" maxlength="34">
                    <input type="hidden" name="accType" id="_accType" value="toAccount" >
                  </td>
                </tr>
                  <!-- add by lzg end -->
				  <!----------------Modified by xyf 20090721 begin----------------->
				  <tr >
                    <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Beneficiary_Address" defaultvalue="Beneficiary Address "/></td>
                    <td  class="content1"  colspan="3">
					<input placeholder="<set:label  name = 'common_input_tips'/>" id="beneficiaryName3" name="beneficiaryName3" type="text" value="<set:data name='beneficiaryName3'/>" size="45" maxlength="31"><br>
					<input placeholder="<set:label  name = 'common_input_tips'/>" id="beneficiaryName4" name="beneficiaryName4" type="text" value="<set:data name='beneficiaryName4'/>" size="45" maxlength="35">				
					</td>
                  </tr>
				  <!----------------Modified by xyf 20090721 end------------------->
                  <tr class="greyline">
                    <td class="label1"><span>&nbsp;&nbsp;</span><b><set:label name="Beneficiar_Bank" defaultvalue="Beneficiary Bank "/></b></td>
                    <td colspan="3">&nbsp;</td>
                  </tr>
                  <tr >
                    <td class="label1"><span class = "xing">*</span><set:label name="Country" defaultvalue="Country"/></td>
                    <td class="content1" colspan="3" ><table><tr><td><select name="beneficiaryCountry" id="beneficiaryCountry" nullable="0"  onChange="showList(this, $('beneficiaryCity'),'country');">
                        <option value=""><set:label name="select_country" defaultvalue="---Select a Country ---"/></option>
                        <set:list name="country"><option value="<set:listdata  name='COUNTRY_CODE' />"
                            <set:listselected key='COUNTRY_CODE' equals='beneficiaryCountry'  output='selected'/> > <set:listdata  name='COUNTRY_NAME' />
                          </option>
                        </set:list>
                      </select>    </td><td width="10">&nbsp;</td><td>  <set:label name="Oversea_Country"/> </td></tr></table>
                    </td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><span class = "xing">*</span><set:label name="City" defaultvalue="City"/></td>
                    <td class="content1" colspan="3" ><table><tr><td><select name="beneficiaryCity" id="beneficiaryCity" nullable="0"  onChange="otherCityControl(this.value);">
                        <option value=""><set:label name="select_city" defaultvalue="---Select a City ---"/></option>
                      </select>  </td><td width="10">&nbsp;</td><td><%--  <set:label name="Oversea_City"/> --%> </td></tr></table>
                        </td>
                  </tr>
                  <tr >
                    <td class="label1"><span class = "xing">*</span><set:label name="Bank_Name" defaultvalue="Bank Name"/></td>
                    <td class="content1" colspan="3" ><select name="beneficiaryBank1" id="beneficiaryBank1" nullable="0"  onChange="changeCgd(this.value);otherBankControl(this.value);">
                        <option value="0"><set:label name="select_bene_bank" defaultvalue="---Select a Beneficiary Bank---"/></option>
                      </select>
                        <div name="showOtherBank" id="showOtherBank" style="display:none">
                          <div style = "position: relative;">
	                      	<input placeholder="<set:label  name = 'common_input_tips'/>" onblur="hiddenSearchArea(this.value,'bankName','bank')" onfocus="showSearchArea(this.value,'bankName','bank')" autocomplete="off"  id="otherBank" name="otherBank" type="text" value="<set:data name='otherBank'/>" size="45"   onKeyUp = "this.value=this.value.toUpperCase();showcorrBankCode('bankName','0','20','','bank')" >
	                  		<div onscroll="loadMore(this,'bankName','bank')" id = "showotherBankNameArea" style = "padding-top: 0;border:1px solid #A9A9A9;width: 33.3%;position: absolute;z-index: 99999;background-color: white;display: none;height:200;overflow-y: auto;overflow-x: hidden;" >
	                  		</div>
                  		</div>
                      </div>
                      </td>
                  </tr>
                  <input placeholder="<set:label  name = 'common_input_tips'/>" id="beneficiaryBank2" name="beneficiaryBank2" type="hidden" value="<set:data name='beneficiaryBank2'/>" size="45" maxlength="35">
                <input placeholder="<set:label  name = 'common_input_tips'/>" id="beneficiaryBank2" name="beneficiaryBank3" type="hidden" value="<set:data name='beneficiaryBank3'/>" size="45" maxlength="35">
                  <tr style = "display:none;">
                    <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Branch_Address" defaultvalue="Branch Address"/></td>
                    <td  class="content1" colspan="3">
                    </td>
                  </tr>
                  <tr class="greyline" >
                    <td class="label1"><span class = "xing">*</span><set:label name="SWIFT_Address" defaultvalue="SWIFT Address"/></td>
                    <td  class="content1" colspan="3">
                    <div style = "position: relative;">
                  	<input placeholder="<set:label  name = 'swift_input_tips'/>" onKeyUp="this.value=this.value.replace(/[\W]/g,'');this.value=this.value.toUpperCase();showcorrBankCode('swiftCode','0','20','','bank')" onblur="hiddenSearchArea(this.value,'swiftCode','bank')" onfocus="showSearchArea(this.value,'swiftCode','bank')" readonly="readonly" autocomplete="off" name="swiftAddress" id="swiftAddress" type="text" value="<set:data name='swiftAddress'/>" size="45" maxlength="11">
                  	<div onscroll="loadMore(this,'swiftCode','bank')" id = "showSwifrAddressArea" style = "padding-top: 0;border:1px solid #A9A9A9;width: 33.3%;position: absolute;z-index: 99999;background-color: white;display: none;height:200;overflow-y: auto;overflow-x: hidden;" >
                  	</div>
                  	</div>
                    </td>
                  </tr>
                 
                 <tr>
                  <td class="label1"><span>&nbsp;&nbsp;</span><b><set:label name="Correspondent_Bank" defaultvalue="Correspondent Bank"/></b></td>
                  <td colspan="3">&nbsp;</td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Correspondent_Bank_Name" defaultvalue="Correspondent Bank Name"/></td>
                  <td  class="content1" colspan="3">
                  <div style = "position: relative;">
                  	<input placeholder="<set:label  name = 'common_input_tips'/>" onblur="hiddenSearchArea(this.value,'bankName','correspondentBank')" onfocus="showSearchArea(this.value,'bankName','correspondentBank')" autocomplete="off" name="correspondentBankName" id="correspondentBankName" type="text" value="<set:data name='correspondentBankName'/>" size="45"   onKeyUp = "this.value=this.value.toUpperCase();showcorrBankCode('bankName','0','20','','correspondentBank')" >
                  	<div onscroll="loadMore(this,'bankName','correspondentBank')" id = "showBankNameArea" style = "padding-top: 0;border:1px solid #A9A9A9;width: 33.3%;position: absolute;z-index: 99999;background-color: white;display: none;height:200;overflow-y: auto;overflow-x: hidden;" >
                  	</div>
                  	</div>
                  </td>
                </tr>
                <tr >
                  <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Correspondent_Bank_Code" defaultvalue="Correspondent Bank Swift Code"/></td>
                  <td  class="content1" colspan="3">
                  	<div style = "position: relative;">
                  	<input placeholder="<set:label  name = 'swift_input_tips'/>"  onblur="hiddenSearchArea(this.value,'swiftCode','correspondentBank')" onfocus="showSearchArea(this.value,'swiftCode','correspondentBank')" autocomplete="off" name="correspondentBankCode" id="correspondentBankCode" type="text" value="<set:data name='correspondentBankCode' />"   onKeyUp="this.value=this.value.replace(/[\W]/g,'');this.value=this.value.toUpperCase();showcorrBankCode('swiftCode','0','20','','correspondentBank')"  size="45" maxlength="11" />
                  	<div  onscroll="loadMore(this,'swiftCode','correspondentBank')" id = "showCodeArea" style = "padding-top: 0;border:1px solid #A9A9A9;width: 33.3%;position: absolute;z-index: 99999;background-color: white;display: none;height:200;overflow-y: auto;overflow-x: hidden;" >
                  	</div>
                  	</div>
                  </td>
                </tr>
                 
                  <tr class="greyline" >
                    <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Message_Send" defaultvalue="Message to be sent "/></td>
                    <td  class="content1"  colspan="3"><input placeholder="<set:label  name = 'common_input_tips'/>" id="messsage" name="messsage" type="text" value="<set:data name='messsage'/>" size="45" maxlength="35">
                        <br>
                        <input placeholder="<set:label  name = 'common_input_tips'/>" id="messsage2" name="messsage2" type="text" value="<set:data name='messsage2'/>" size="45" maxlength="35">
                        <br><input placeholder="<set:label  name = 'common_input_tips'/>" id="messsage3" name="messsage3" type="text" value="<set:data name='messsage3'/>" size="45" maxlength="35">
                    </td>
                  </tr>
                  <tr >
                    <td class="label1"><span class = "xing">*</span><set:label name="Commission_charges_by" defaultvalue="Commission and charges to be paid to banks overseas by "/></td>
                    <!-- modified by lzg for GAPMC-EB-001-0050 -->
                    <!--<td class="content1" colspan="3" ><set:rblist file="app.cib.resource.txn.charge_name">
                        <set:rbradio name="chareBy"/>
                      </set:rblist>
                    </td>
                  -->
                  <td class="content1" colspan="3">
                  	<input name = "chareBy" type = "radio" value = "S" onchange = "hiddenContent(this.value)" disabled /><set:label name='S'/>
                  	<input name = "chareBy" type = "radio" value = "P" onchange = "hiddenContent(this.value)" /><set:label name='P'/>
                  	<input name = "chareBy" type = "radio" value = "O" onchange = "hiddenContent(this.value)" /><set:label name='O'/>
                  </td>
                  <!-- modified by lzg end -->
                  </tr>
                  <!-- modified by lzg for GAPMC-EB-001-0050 -->
                  <tr class="greyline" id = "hiddenContent">
                    <td class="label1"><span class = "xing">*</span><set:label name="Deduct_Charge_from_Account" defaultvalue="Deduct Charge from Account"/></td>
                    <td class="content1" colspan="3" >
                    <!--<select id="chargeAccount" name="chargeAccount" nullable="0" onChange="chargeAcc(this.value)">
				<option selected value="" selected><set:label name="select_account" defaultvalue="----- Select  an Account ------"/></option>
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
                    </td>
                  </tr>
                  <!-- modified by lzg end -->
                  
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
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <!-- add by linrui 20190522 -->
              <%@ include file="/common/transferTips.jsp" %>
              <!-- add by linrui end -->
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
				<input id="submit1" name="submit1" type="button" value="<set:label name='buttonOK' defaultvalue=' OK ' />" tabindex="3"  onClick="doSubmit();">
<!--                  <input id="bottonReset" name="bottonReset" type="reset" onClick="document.getElementById('showAmountArea').style.display='none';" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
				 <input id="ActionMethod" name="ActionMethod" type="hidden" value="addTemplate">	
				 <input name="cgd_flag" type="hidden" id="cgd_flag">
				 <input name="GetCorrespondentBankResult" type="hidden" id="GetCorrespondentBankResult" value = "<set:data  name = 'GetCorrespondentBankResult'/>">
                    <input name="GetCorrespondentBankResultCount" type="hidden" id="GetCorrespondentBankResultCount" value = "<set:data  name = 'GetCorrespondentBankResultCount'/>">
                    <input name="isFirstSearch" type="hidden" id="isFirstSearch" value = "<set:data  name = 'isFirstSearch'/>">
				 <input id="fromCurrency" name="fromCurrency" type="hidden" value="">
                 <input id="chargeCurrency" name="chargeCurrency" type="hidden" value="">
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
