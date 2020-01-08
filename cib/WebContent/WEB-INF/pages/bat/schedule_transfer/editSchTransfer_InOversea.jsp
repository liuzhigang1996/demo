<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bat.schedule_transfer_oversea">
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
// add by li_zd at 20171117 for mul-language
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";

function pageLoad(){
	if(language == "en_US"){
		document.getElementById("showENG").style.display = "block";
	}else{
		document.getElementById("showCHN").style.display = "block";
	}
	var myDayType = '<set:data name="dayType"/>';
	if(myDayType==''){
		myDayType = '<set:data name="frequenceType"/>';
	}
	putFieldValues("dayType", myDayType);
	if (myDayType=="2"){
		putFieldValues("frequenceWeekDays", "<set:data name='week0' />");
		putFieldValues("frequenceWeekDays", "<set:data name='week1' />");
		putFieldValues("frequenceWeekDays", "<set:data name='week2' />");
		putFieldValues("frequenceWeekDays", "<set:data name='week3' />");
		putFieldValues("frequenceWeekDays", "<set:data name='week4' />");
	}
	if (myDayType=="3"){
		var myMonthType = '<set:data name="month_type"/>';
		var myDesignedDay = '<set:data name="designed_day"/>';
		var myFreqDays = '<set:data name="frequenceDays"/>';
		if (myMonthType=='' ? myFreqDays : myMonthType=="1"){
			putFieldValues("month_type", myMonthType=='' ? myFreqDays : myMonthType);
		} else if (myMonthType=='' ? myFreqDays : myMonthType=="-1"){
			putFieldValues("month_type", myMonthType=='' ? myFreqDays : myMonthType);
		} else {
			putFieldValues("month_type", "0");
			putFieldValues("designed_day", myDesignedDay=='' ? myFreqDays : myDesignedDay);
		}
	}
	if(myDayType=="4"){
		var myDaysPerMonth = '<set:data name="days_per_month"/>';
		var myFreqDays = '<set:data name="frequenceDays"/>';
		putFieldValues("days_per_month", myDaysPerMonth=='' ? myFreqDays : myDaysPerMonth);
	}
	//modified by lzg for GAPMC-EB-001-0050
//	if ($('fromAccount')) {
//		changeFromAcc($F('fromAccount'));
//	}
//	if ($('toCurrency')) {
//		changeToCcy($F('toCurrency'));
//	}
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
	setTimeout(
		function(){
			showList($('beneficiaryCountry'), $('beneficiaryCity'), 'country', onLoadCallback);
		},
		500
	);
	
	setTimeout(function(){
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
	
	// add by lw 2011-01-27
	//checkIfShowPurpose();
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
	//modified by lzg 20190523
//	if(!$("_accType").checked) {
//		$("toAccount").value = "";
//	}
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
	//alert("ksdflsff");
	if(((Trim(beneName1) != "") && (Trim(beneName2) == "")) && ((Trim(beneName1)).indexOf(" ")==-1)){
		document.getElementById("beneJustify").value="Y";
	}else if(((Trim(beneName1) == "") && (Trim(beneName2) != "")) && ((Trim(beneName2)).indexOf(" ")==-1)){
		document.getElementById("beneJustify").value="Y";
	}else{
		document.getElementById("beneJustify").value="N";
	}
	if(validate_schedule_oversea(document.getElementById("form1"))){
		//add by hjs
		var schName = document.form1.scheduleName.value;
		for(i=0; i<schName.length; i++) {
			if(schName.charAt(i) == '#') {
				alert('<set:label name="err.bat.ScheduleNameError" rb="app.cib.resource.common.errmsg"/>');
				document.form1.scheduleName.focus();
				document.form1.scheduleName.select();
				return;
			}
		}
	     if ($('toCurrency').value=='JPY' || $('toCurrency').value=='KRW') {
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
		//trim purpose value before sumbimt 20110513 by wency
		//document.getElementById("otherPurpose").value = trim(document.getElementById("otherPurpose").value);
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
		$('swiftAddress').readOnly=true;
		return true;
	  }
	}
	$('swiftAddress').value='';
	$('swiftAddress').readOnly=false;
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
function showList(originSelect, targetSelect,action, callback){
	if(callback == 'undefined'){
		callback = null;
	}
	clearAll();
	if ((originSelect.value != null)) {
		//var params = getParams(originSelect, targetSelect);
		var params = getParamsForCountry(originSelect, targetSelect);
		var url = '/cib/jsax?serviceName=TxnInOverseaService&action='+action+'&'+ params + '&language=' + language;
		//registerElement('showBankLabel');
		//registerElement('showAccountLabel');
		//registerElement('beneficiaryBank1');
		getMsgToSelect(url,'', callback, false,language);
  	}
	/*add by lw 2011-01-27*/
	//document.getElementById("proofVisible").value = ""; 
	//checkIfShowPurpose();
	//document.getElementById('proofCheckBox').checked = false;
	/*add by lw end*/
}
function otherBankControl(bankCode){
	$('showOtherBank').style.display = "none";
	if($('otherBank')) {
		$('otherBank').value="";
	}
	showSwiftAddr(bankCode);
	if(bankCode!=null && bankCode=='other'){
		$('showOtherBank').style.display = "block";
	}
	putFieldValues("otherBank", "<set:data name='otherBank' />");
}
function otherCityControl(cityCode){
	$('showOtherCity').style.display = "none";
	if($('otherCity')) {
		$('otherCity').value="";
	}
	if(cityCode!=null && cityCode=='other'){
		$('showOtherCity').style.display = "block";
	}
	putFieldValues("otherCity", "<set:data name='otherCity' />");
}
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
function changeFromAcc(fromAcc){
	var fromAccount = document.getElementById("fromAccount");
	var from_ccy = fromAccount.options[fromAccount.selectedIndex].innerHTML;
	var arr = from_ccy.split("-");
	from_ccy = arr[1].substring(1,4);
	//add by linrui for mul-ccy	
    fromAcc = arr[0].replace(/(^\s*)|(\s*$)/g, "");
	if (fromAcc != '0' && fromAcc != '') { //modify by hjs
		document.getElementById("fromCurrency").value= from_ccy;
		var url = '/cib/jsax?serviceName=AccInTxnService&showFromAcc=' + fromAcc + '&from_ccy=' + from_ccy  + '&language=' + language;//mod by li_zd at 20171117 for mul-language
   		//registerElement('showAmount');
   		//registerElement('showFromCurrency');
		getMsgToElement(url, fromAcc, '', null,false,language);
		document.getElementById("showAmountArea").style.display = "block";
	}else{
		document.getElementById("showAmountArea").style.display = "none";
	}
	// add by lw 2011-01-27
	//document.getElementById("proofVisible").value = "";
	//checkIfShowPurpose();
	//document.getElementById('proofCheckBox').checked = false;
	// add by lw end
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
	if (toCcy != '0') { //modify by hjs
		document.getElementById("showToCurrency").innerHTML=toCcy;
	}
	// add by lw 2011-01-27
	//document.getElementById("proofVisible").value = "";
	//checkIfShowPurpose();
	//document.getElementById('proofCheckBox').checked = false;
	// add by lw end
}
function changeCgd(bank){
	if (bank != '0') { //modify by hjs
		var url = '/cib/jsax?serviceName=CgdOverseaService&bank=' + bank;
   		//registerElement('cgd_flag');
   		getMsgToElement(url, bank, '', null, false,language);
		var cgdValue = $F('cgd_flag');
		document.form1.chareBy[0].disabled = false;
		document.form1.chareBy[1].disabled = false;
		document.form1.chareBy[0].checked = false;
		document.form1.chareBy[1].checked = false;
		/*
	    if (cgdValue == "Y") {
			document.form1.chareBy[0].checked = true;
			document.form1.chareBy[1].checked = false;
			document.form1.chareBy[1].disabled = true;
	   } 
	   */
	}
}
//hjs
var varId = '';
function changeFrequence(oId) {
	if(oId != varId ) {
		init();
		if (oId == 'dayType2') {
			var s1 = document.getElementsByName("frequenceWeekDays");
			if (s1 != null) {
				for (i=0; i<s1.length; i++) {
					s1[i].disabled = false;
				}
			}
		}
		if (oId == 'dayType3') {
			var s2 = document.getElementsByName("month_type");
			if (s2 != null) {
				for (i=0; i<s2.length; i++) {
					s2[i].disabled = false;
				}
			}
			document.form1.designed_day.disabled = false;
		}
		if (oId == 'dayType4') {
			document.form1.days_per_month.disabled = false;
		}
		varId  = oId;
	}
}
function init() {
	var s1 = document.getElementsByName("frequenceWeekDays");
	if (s1 != null) {
		for (i=0; i<s1.length; i++) {
			s1[i].checked = false;
			s1[i].disabled = true;
		}
	}
	var s2 = document.getElementsByName("month_type");
	if (s2 != null) {
		for (i=0; i<s2.length; i++) {
			s2[i].checked = false;
			s2[i].disabled = true;
		}
	}
	document.form1.designed_day.value = '';
	document.form1.days_per_month.value = '';
	document.form1.designed_day.disabled = true;
	document.form1.days_per_month.disabled = true;
}
function initDesignedDay() {
	document.form1.designed_day.value='';
	document.form1.designed_day.disabled = true;
}
function initMonthType() {
	document.form1.designed_day.disabled = false;
}
// add by lw 2011-01-27
function changeProof(){
	if(document.getElementById('proofCheckBox').checked == true){
		document.form1.submit1.disabled = false;
	}
	else{
		document.form1.submit1.disabled = true;
	}
}
/*function checkIfShowPurpose(){
    	var transferAmount = document.form1.transferAmount.value;
		var debitAmount = document.form1.debitAmount.value;
		var fromAccount = document.form1.fromAccount.value;
		var toCurrency = document.form1.toCurrency.value;
		var toCountryCode = document.form1.beneficiaryCountry.value;
		var url = '/cib/jsax?serviceName=CheckNeedPurposeService&transferAmount=' +transferAmount+ '&debitAmount=' +debitAmount+ '&fromAccount='+fromAccount+ '&toCurrency=' +toCurrency+ '&toCountryCode=' + toCountryCode;
		//registerElement('purposeVisible');
		//registerElement('proofVisible'); 
		getMsgToElement(url, transferAmount, '', null,false,language);
		var vi = document.getElementById("purposeVisible").innerHTML;
		var pi = document.getElementById("proofVisible").innerHTML;
		if(vi == "visible"){
		    document.getElementById("purposeArea").style.display = "block";
			if(pi == "needproof"){
				document.getElementById("proofDiv").style.display = "block";
				document.getElementById('proofCheckBox').checked = false;
				document.form1.submit1.disabled = true;
			}
			else{
				document.getElementById("proofDiv").style.display = "none";
				document.form1.submit1.disabled = false;
			}
		}
		else{
		    document.getElementById("purposeArea").style.display = "none";
			document.getElementById("proofVisible").value = "";
			document.getElementById("proofDiv").style.display = "none";
			document.form1.submit1.disabled = false;
		}
}*/
function resetTransferAmount(){
    document.form1.debitAmount.value='';
	//document.getElementById("purposeArea").style.display = "none";
}
function resetDebitAmount(){
    document.form1.transferAmount.value='';
	//document.getElementById("purposeArea").style.display = "none";
}
// add by lw end

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
				
	}else{
		document.getElementById("showAmountArea").style.display = "none";
	}
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
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/scheduleTransferOversea.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bat.schedule_transfer_oversea" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitleDetail" defaultvalue="BANK Online Banking >Transfer > Transfer Templates"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitleModify" defaultvalue="TRANSFER TEMPLATES EDIT"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/scheduleTransferOversea.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
           <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><set:messages width="100%" cols="1" align="center"/>
                        <set:fieldcheck name="schedule_oversea" form="form1" file="schedule_transfer_oversea" />
                    </td>
                  </tr>
                </table>
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator"><set:label name="Fill_Info" defaultvalue="Fill the following Information"/></td>
              </tr>
            </table>
				<table width="100%" border="0" cellspacing="0" cellpadding="3">
			 <tr  >
                <td class="label1"><span class = "xing">*</span><set:label name="Schedule_Name" defaultvalue="Schedule Name"/></td>
                <td class="content1" colspan="3"><input id="scheduleName" name="scheduleName" type="text" value="<set:data name='scheduleName'/>" size="20" maxlength="20"></td>
              </tr>
			   <tr class="greyline" >
                <td class="label1" rowspan="4"><span class = "xing">*</span><set:label name="Frequence" defaultvalue="Frequence"/></td>
                <td class="label1"><input type="radio" name="dayType" value="1" id="dayType1" onClick="changeFrequence(this.id);"></td>
                        <td class="label1"><set:label name="Daily" defaultvalue="Daily (Work Day Only)"/></td>
              </tr>
		    	<tr class="greyline" >
			           <td class="label1"><input type="radio" name="dayType" id="dayType2" value="2" onClick="changeFrequence(this.id);"></td>
                        <td class="label1"><set:label name="Weekly" defaultvalue="Weekly"/>---
						<set:rblist file="app.cib.resource.bat.week"><set:rbcheckbox name="frequenceWeekDays"/></set:rblist></td>
                 </tr>
				  <tr class="greyline" >
			           <td class="label1"><input type="radio" name="dayType" id="dayType3" value="3" onClick="changeFrequence(this.id);" ></td>
                    <td class="label1"><set:label name="Monthly" defaultvalue="Monthly"/>---<input id="month_type" type="radio" name="month_type" value="1" onClick="initDesignedDay();"><set:label name="Start_of_month" defaultvalue="Start of month"/><input id="month_type" type="radio" name="month_type" value="-1" onClick="initDesignedDay();"><set:label name="End_of_month" defaultvalue="End of month"/><input id="month_type" type="radio" name="month_type" value="0" onClick="initMonthType();"><set:label name="Designated_Day" defaultvalue="Designated Day "/>&nbsp;<input id="designed_day" name="designed_day" type="text" value="<set:data name='designed_day'/>" size="8" maxlength="8" onClick="">
                        (1-31)</td>
                 </tr>
				  <tr class="greyline" >
			         <td class="label1"><input type="radio" name="dayType" id="dayType4" value="4" onClick="changeFrequence(this.id);" ></td>
                    <td class="label1"><set:label name="Days_per_month" defaultvalue="Days per month"/>---<input id="days_per_month" name="days_per_month" type="text" value="<set:data name='days_per_month'/>" size="20" maxlength="20"><set:label name="dayInfo" defaultvalue="Days separated with commas"/></td>
                 </tr>
                 <tr>
                  <td class="label1"><span class = "xing">*</span><set:label name="Transfer_End_Date" defaultvalue="Transfer End Date"/></td>
                  <td class="content1" colspan="3"><input id="endDate" name="endDate" type="text" value="<set:data name='endDate' format="date" pattern="dd/MM/yyyy"/>" size="10" maxlength="10"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "{scwShow(document.getElementById('endDate'), this,language)};" > </td>
                </tr>  
			 </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td colspan="4" class="groupinput">&nbsp;</td>
                  </tr>
                  <tr class="greyline">
                    <td width="24%" class="label1",colspan="3"><span>&nbsp;&nbsp;</span><b>
                    <set:label name="Transfer_From" defaultvalue="Transfer From"/></b><input id="action" type="hidden" name="action"></td>
                    <td>&nbsp;</td>
                  </tr>
                  <tr >
                    <td class="label1"><span class = "xing">*</span><set:label name="Transfer_Account" defaultvalue="Account Number"/></td>
                     <td class="content1" valign="top" colspan="4" >
                     <!-- modified by lzg 20190523 -->
                     <!--<select id="fromAccount" name="fromAccount" nullable="0" onChange="changeFromAcc(this.value)">
				<option selected value="" selected><set:label name="select_account" defaultvalue="----- Select  an Account ------"/></option>
                  <set:list name="transferuser"> <option value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_INFO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_INFO' />
                      </option>
                  </set:list>
                </select>	
				  <div name="showAmountArea" id="showAmountArea" style="display: none"><table border="0" cellpadding="0" cellspacing="0">
                    <tr><td>Available Balance :&nbsp;</td><td> <div name="showAmount" id="showAmount"></div></td></tr></table></div></td>			
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
              <tr class="greyline">
                  <td class="label1"><span class = "xing">*</span><set:label name="From_ccy" defaultvalue="From Currency"/></td>
                  <td class="content1" colspan="3" ><table><tr><td><select name="fromCurrency" id="fromCurrency" nullable="0"  onChange="changeFromCcy(this.value);">
                      <option  value="" selected><set:label name="select_ccy" defaultvalue="----Please select Currency----"/></option>
                    </select>  </td></tr></table>
                    
                    <div name="showAmountArea" id="showAmountArea" style="display: none"><table border="0" cellpadding="0" cellspacing="0">
                    <tr><td><set:label name="availd_balance" defaultvalue="Available Balance :"/>&nbsp;</td><td> <div name="showAmount"  format="amount" id="showAmount" ></div></td></tr></table></div>
                  </td>
               </tr>
                <!-- modified by lzg end -->
                  <!--<tr >
                    <td class="label1" ><set:label name="Transfer_Amount" defaultvalue="Transfer Amount"/></td>
					<td class="label1" colspan="3" ><set:label name="Transfer_Info1" defaultvalue=" First select the currency and then input the amount:"/></td>
                  </tr>
                  --><!-- modified by lzg 20190523 -->
                  <!--<tr class="greyline">
                    <td class="label1"></td>
                    <td class="content1" colspan="3" ><select id="toCurrency" name="toCurrency" nullable="0" onChange="changeToCcy(this.value)">
					<option value="0"><set:label name="Select_Currency_Label" defaultvalue="----- Select a Currency ------"/></option>
                        <set:rblist db="currencyOversea">
                          <set:rboption name="toCurrency"/>
                        </set:rblist>
                      </select>                    </td>
                  </tr>-->
                  <!-- modified by lzg end -->
                  <tr >
                   <td class="label1"></td>
					 <td class="label1" colspan="3" ><set:label name="Transfer_Info2" defaultvalue="You either input the actual amount of funds to be transferred OR the amount to be debited from the selected account above "/></td>
                    </tr>
                  <tr class="greyline">
                    <td class="label1"><span class = "xing">*</span><b><set:label name="Transfer_Amount" defaultvalue="Transfer Amount"/></b></td>
                    <td width="50%" class="label1"><set:label name="Amount_to_be_Transferred" defaultvalue="Amount to be Transferred"/></td>
                    <td width="5%" class="content1"><div name="showToCurrency" id="showToCurrency"></div></td>
                    <td width="40%" class="content1"><input id="transferAmount" name="transferAmount" type="text" value="<set:data name='transferAmount' format='amount'/>" size="20" maxlength="20"  onFocus ="resetTransferAmount()"></td>
                  </tr>
                  <tr >
                    <td class="label1"></td>
                    <td width="50%" class="label1"><set:label name="Amount_to_be_debited" defaultvalue="Amount to be debited from the selected account above "/></td>
                    <td width="5%" class="content1"><div name="showFromCurrency" id="showFromCurrency"></div></td>
                    <td width="40%" class="content1"><input id="debitAmount" name="debitAmount" type="text" value="<set:data name='debitAmount' format='amount'/>" size="20" maxlength="20"  onFocus ="resetDebitAmount()"></td>
                  </tr>
				  <!-- add by lw 2011-01-27 -->
				  <!-- modify by lw for CR145 2011-08-24 -->
				<tr class="greyline">
				<td colspan="4">
				<div name="proofDiv" id="proofDiv" style="display:none">
				<table width="100%">
     			<tr>
					<td class="label1" width="20%" align="right"><input name="proofCheckBox" type="checkbox" id="proofCheckBox" onClick="changeProof()"></td>
					<td class="content1" colspan="3" width="80%"><font color="#FF0000"><set:label name="Need_Proof_Message" defaultvalue="* Need to provide Proof Document."/></font></td>
				</tr>
				</table>
				</div>
				</td>
				</tr>
				<!-- add by lw end -->
                  <tr >
                    <td  colspan="4">&nbsp;</td>
                  </tr>
                  <tr  class="greyline">
                    <td class="label1"  colspan="4"><span>&nbsp;&nbsp;</span><b><set:label name="Transfer_To" defaultvalue="Transfer To"/></b></td>
                  </tr>
                  <tr >
                    <td class="label1"><span class = "xing">*</span><set:label name="Beneficiary_Name" defaultvalue="Beneficiary Name "/></td>
                    <td  class="content1"  colspan="3"><input id="beneficiaryName1" name="beneficiaryName1" type="text" value="<set:data name='beneficiaryName1'/>" size="45" maxlength="35"><br>
					<input id="beneficiaryName2" name="beneficiaryName2" type="text" value="<set:data name='beneficiaryName2'/>" size="45" maxlength="35">
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
                        <td class="content1"><input name="toAccount" type="text" id="toAccount" value="<set:data name='toAccount'/>" size="34" maxlength="34"></td>
                      </tr>
                    </table></td>
                </tr>
                -->
                <tr class="greyline">
                  <td class="label1"><span class = "xing">*</span><set:label name="Beneficiary_Account" defaultvalue="Account Number"/></td>
                  <td  class="content1" colspan="3">
                  	<input type="hidden" name="accType" id="_accType" value="toAccount" >
					<input name="toAccount" type="text" id="toAccount" value="<set:data name='toAccount'/>" size="34" maxlength="34">
                  </td>
                </tr>
                <tr >
                  <td class="label1"><span class = "xing">*</span><set:label name="Currency" defaultvalue="Currency"/></td>
                  <td class="content1" colspan="3" ><select id="toCurrency" name="toCurrency" nullable="0" onChange="changeToCcy(this.value)">
                      <option value="0" selected><set:label name="Select_Currency_Label" defaultvalue="----- Select a Currency ------"/></option>
                      <set:rblist db="currencyOversea"> <set:rboption name="toCurrency"/> </set:rblist>
                    </select>
                  </td>
                </tr>
                <!-- add by lzg end -->
				  <!-----------------added by xyf 20090721 begin----------------->
				  <tr class="greyline">
				    <td colspan="4">&nbsp;</td>
				  </tr>
				  <tr >
                    <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Beneficiary_Address" defaultvalue="Beneficiary Address"/></td>
                    <td  class="content1"  colspan="3"><input id="beneficiaryName3" name="beneficiaryName3" type="text" value="<set:data name='beneficiaryName3'/>" size="45" maxlength="35"><br>
					<input id="beneficiaryName4" name="beneficiaryName4" type="text" value="<set:data name='beneficiaryName4'/>" size="45" maxlength="35">
					</td>
                  </tr>
				  <!-----------------added by xyf 20090721 end------------------->
                  <tr class="greyline">
                    <td class="label1"><span>&nbsp;&nbsp;</span><b><set:label name="Beneficiar_Bank" defaultvalue="Beneficiary Bank "/></b></td>
                    <td colspan = "3">&nbsp;</td>
                  </tr>
                  <tr >
                    <td class="label1"><span class = "xing">*</span><set:label name="Country" defaultvalue="Country"/></td>
                    <td class="content1" colspan="3" ><table><tr><td><select name="beneficiaryCountry" id="beneficiaryCountry" nullable="0"  onChange="showList(this, $('beneficiaryCity'),'country');">
                        <option value=""><set:label name="Select_Country_Label" defaultvalue="---Select a Country ---"/></option>
                        <set:list name="country"> <option value="<set:listdata  name='COUNTRY_CODE' />" 
                          <set:listselected key='COUNTRY_CODE' equals='beneficiaryCountry'  output='selected'/>
                          >
                          <set:listdata  name='COUNTRY_NAME' />
                          </option>
                        </set:list>
                      </select>   </td><td width="10">&nbsp;</td><td> <set:label name="Oversea_Country"/> </td></tr></table>                </td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><span class = "xing">*</span><set:label name="City" defaultvalue="City"/></td>
                    <td class="content1" colspan="3" ><table><tr><td><select name="beneficiaryCity" id="beneficiaryCity" nullable="0"  onChange="otherCityControl(this.value);">
                      <option value=""><set:label name="Select_City_Label" defaultvalue="---Select a City ---"/></option>
                    </select>  </td><td width="10">&nbsp;</td><td> <set:label name="Oversea_City"/>  </td></tr></table>
                    <div name="showOtherCity" id="showOtherCity" style="display:none">
					   	<input id="otherCity" name="otherCity" type="text" value="<set:data name='otherCity'/>" size="30" maxlength="30">
				      </div>            </td>
                  </tr>
                  <tr >
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><span class = "xing">*</span><set:label name="Bank_Name" defaultvalue="Bank Name"/></td>
                    <td class="content1" colspan="3" >
					<select name="beneficiaryBank1" id="beneficiaryBank1" nullable="0"  onChange="otherBankControl(this.value);changeCgd(this.value);">
                        <option value="0"><set:label name="Select_Beneficiary_Bank" defaultvalue="---Select a Beneficiary Bank---"/></option>
                    </select>
					  <div name="showOtherBank" id="showOtherBank" style="display:none">
					   	<input id="otherBank" name="otherBank" type="text" value="<set:data name='otherBank'/>" size="50" maxlength="50">
					  </div> 
					             </td>
                  </tr>
                  <tr >
                    <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Branch_Address" defaultvalue="Branch Address"/></td>
                    <td  class="content1" colspan="3"><input id="beneficiaryBank2" name="beneficiaryBank2" type="text" value="<set:data name='beneficiaryBank2'/>" size="45" maxlength="35">
					<br><input id="beneficiaryBank3" name="beneficiaryBank3" type="text" value="<set:data name='beneficiaryBank3'/>" size="45" maxlength="35">					</td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><span class = "xing">*</span><set:label name="SWIFT_Address" defaultvalue="SWIFT Address"/></td>
                    <td  class="content1" colspan="3"><input id="swiftAddress" name="swiftAddress" type="text" value="<set:data name='swiftAddress'/>" size="20" maxlength="11"></td>
                  </tr>
                    <tr >
                      <td  class="content1" colspan="4"><div name="showBankLabel" id="showBankLabel"></div></td>
                    </tr>
                <!--<tr class="greyline">
                  <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Correspondent_Bank" defaultvalue="Correspondent Bank"/></td>
                  <td  class="content1"  colspan="3">
				  <input id="correspondentBankLine1" name="correspondentBankLine1" type="text" value="<set:data name='correspondentBankLine1'/>" size="45" maxlength="35"><br>
				  <input id="correspondentBankLine2" name="correspondentBankLine2" type="text" value="<set:data name='correspondentBankLine2'/>" size="45" maxlength="35"><br>
				  <input id="correspondentBankLine3" name="correspondentBankLine3" type="text" value="<set:data name='correspondentBankLine3'/>" size="45" maxlength="35"><br>
				  <input id="correspondentBankLine4" name="correspondentBankLine4" type="text" value="<set:data name='correspondentBankLine4'/>" size="45" maxlength="35">			  
				  </td>
                </tr>
                <tr>
                  <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Correspondent_Bank_Account" defaultvalue="Correspondent Bank Account"/></td>
                  <td  class="content1"  colspan="3"><input id="correspondentBankAccount" name="correspondentBankAccount" type="text" value="<set:data name='correspondentBankAccount'/>" size="45" maxlength="35">
				  </td>
                </tr>
                  --><tr  class="greyline" >
                    <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Message_Send" defaultvalue="Message to be sent "/></td>
                    <td  class="content1"  colspan="3">
					<input id="messsage" name="messsage" type="text" value="<set:data name='messsage'/>" size="45" maxlength="35">
					<br><input id="messsage2" name="messsage2" type="text" value="<set:data name='messsage2'/>" size="45" maxlength="35">					</td>
                  </tr>
				   <tr>
                    <td class="label1"><span class = "xing">*</span><set:label name="Commission_charges_by" defaultvalue="Commission and charges to be paid to banks overseas by "/></td>
                    <!-- modified by lzg 20190523 -->
                    <!--<td class="content1" colspan="3" ><set:rblist file="app.cib.resource.txn.charge_name">
                        <set:rbradio name="chareBy"/>
                      </set:rblist>                    </td>
                      --><!-- modified by lzg end -->
                  <td class="content1" colspan="3">
                  	<input name = "chareBy" type = "radio" value = "S" onchange = "hiddenContent(this.value)" disabled /><set:label name='S'/>
                  	<input name = "chareBy" type = "radio" value = "P" onchange = "hiddenContent(this.value)" /><set:label name='P'/>
                  	<input name = "chareBy" type = "radio" value = "O" onchange = "hiddenContent(this.value)"  /><set:label name='O'/>
                  </td>
                <!-- modified by lzg end -->
                  </tr>
                  <tr  class="greyline" id = "hiddenContent">
                    <td class="label1"><span class = "xing">*</span><set:label name="Deduct_Charge_from_Account" defaultvalue="Deduct Charge from Account"/></td>
                    <td class="content1" colspan="3" >
                    <!-- modified by lzg 20190523 -->
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
                  
                  <!-- add by lzg 20190602 -->
				<tr>
                  <td class="label1"><span class = "xing">*</span><set:label name="Purpose" defaultvalue="Purpose"/></td>
                  <td  class="content1" colspan="3">
                  	<select id="purposeCode" name="purposeCode">
                  	  <option value = ""><set:label name = "Select_Purpose" /></option>
                      <set:rblist file="app.cib.resource.txn.purposecode"><set:rboption name="purposeCode"/></set:rblist>
                   </select>
                  </td>
                </tr>
                <!-- add by lzg end -->
                  
                  <!-- modified by lzg 20190602 -->
				<!--<div id="purposeVisible" name="purposeVisible" style="display:none"></div>				
				<div id="proofVisible" name="proofVisible" style="display:none"></div>
				<tr>
     			<td colspan="4">
     			<div id="purposeArea" name="purposeArea" style="display:none">
     			<table width="100%">
     			<tr>
                  	<td class="label1" width="24%"><set:label name="Purpose" defaultvalue="Purpose"/></td>
                  	<td class="content1" colspan="3" width="76%">
      				<input id="otherPurpose" name="otherPurpose" value="<set:data name = 'otherPurpose' />" type="text" size="35" maxlength="35">
      				</td>
     			</tr>
     			</table>
     			</div>
     			</td> 
     			</tr>
     			--><!-- modified by lzg end -->
                  <tr class="greyline">
                    <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="Remark" defaultvalue="Remark"/></td>
                    <td class="content1" colspan="3">
                      <input id="remark" name="remark" type="text" value="<set:data name='remark'/>" size="20" maxlength="120"></td>
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
				  <input id="ActionMethod" name="ActionMethod" type="hidden" value="edit">
				   <input name="cgd_flag" type="hidden" id="cgd_flag">
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
