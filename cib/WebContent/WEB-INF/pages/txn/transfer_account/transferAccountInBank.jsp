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
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js?v=20141217"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js?v=20141217"></SCRIPT>
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
	if(!"<set:data name='fromAccount' />" == ""){
		//changeFromAcc("<set:data name='fromAccount' />");
		var fromAccount = document.getElementById("fromAccount");
		var fromCurrency = document.getElementById("fromCurrency");
		for(var i=0;i<fromAccount.options.length;i++){
    		if(fromAccount[i].value=='<set:data name='fromAccount' />'){  
        		fromAccount[i].selected=true;   
    		}  
		}  
		showFromCcyList(fromAccount, fromCurrency,'getFromCcy', fromAccount.value);
		if(!"<set:data name='fromCurrency' />" == ""){
			for(var i=0;i<fromCurrency.options.length;i++){
	    		if(fromCurrency[i].value=='<set:data name='fromCurrency' />'){  
	        		fromCurrency[i].selected=true;   
	    		}  
			}  
		}
		changeFromCcy(fromCurrency.value);
	}
	if(!"<set:data name='toAccount' />" == ""){
		var toAccount = document.getElementById("toAccount");
		var toCurrency = document.getElementById("toCurrency");
		for(var i=0;i<toAccount.options.length;i++){
    		if(toAccount[i].value=='<set:data name='toAccount' />'){  
        		toAccount[i].selected=true;   
    		}  
		}
		showToCcyList(toAccount, toCurrency,'getToCcy', toAccount.value);
		if(!"<set:data name='toCurrency' />" == ""){
			for(var i=0;i<toCurrency.options.length;i++){
		    	if(toCurrency[i].value=='<set:data name='toCurrency' />'){
		        	toCurrency[i].selected=true;   
	    		}  
			}  
		}
		changeToCcy(toCurrency.value);
	}
	//modified by lzg end
	setTimeout(
		function(){
			if('<set:data name="toAccount"/>' == ''){
			} else if('<set:data name="toAccount"/>' == '0'){
				//otherAcc('<set:data name="toAccount2"/>');
			} else {
				//changeToAccList('<set:data name="toAccount"/>');
			}
		},
		500
	);
}
function doSubmit()
{   
    //add by linrui 20190723
    if(!remarkLength(document.getElementById("remark").value)){
        return false;
    }
    //end
	//add by linrui for mul-ccy
	document.getElementById("toCurrency").value= document.getElementById("showToCurrency").innerHTML;
	if(validate_transferInBANK(document.getElementById("form1"))){
		if(document.getElementById("showToCurrency").innerHTML == ""){
			alert('<set:label name="To_Acc_Not_Available" defaultvalue = "To account not available, can not submit this transactions"/>');
		}else{

			/* Add by long_zg 2019-05-21 UAT6-195 COB：海外匯款JPY輸入扣款幣金額卻提示金額過小，沒有拿到匯率 begin */
			var transferAmount = $('transferAmount').value ;
			var debitAmount = $('debitAmount').value ;
			/* Add by long_zg 2019-05-21 UAT6-195 COB：海外匯款JPY輸入扣款幣金額卻提示金額過小，沒有拿到匯率 begin */
			
			 if (document.getElementById("showToCurrency").innerHTML =='JPY' || document.getElementById("showToCurrency").innerHTML =='KRW') {
			
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
}
function checkInteger(amt) {
	if (amt.indexOf('.') > 0) {
		return false;
	} else {
		return true;
	}
}
function changeToAccList(toAcc){
	if (toAcc=='0') {
		document.getElementById("toAccount2").disabled = false;
		document.getElementById("beneficiaryName").style.display = "";
		document.getElementById("toAccount2").value = "";
		document.getElementById("showToCurrency").innerHTML = "";
	} else {
		document.getElementById("toAccount2").disabled = true;
		document.getElementById("beneficiaryName").style.display = "none";
		document.getElementById("toAccount2").value = toAcc;
		changeToAcc(toAcc);
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
		//add by linrui for mul-ccy		
		document.getElementById("fromCurrency").value= from_ccy;
		var url = '/cib/jsax?serviceName=AccInTxnService&showFromAcc=' + fromAcc + '&from_ccy=' + from_ccy +'&language='+language;//mod by linrui for mul-language20171117
		getMsgToElement(url, fromAcc, '', null,false,language);
		document.getElementById("showAmountArea").style.display = "block";
				
	}else{
		document.getElementById("showAmountArea").style.display = "none";
	}
}
function changeToAcc(toAcc){
	var toAccount = document.getElementById("toAccount");
	var to_ccy = toAccount.options[toAccount.selectedIndex].innerHTML;
	var arr = to_ccy.split("-");
	to_ccy = arr[1].substring(1,4);
	//add by linrui for mul-ccy
	toAcc = arr[0].replace(/(^\s*)|(\s*$)/g, "");
	if (toAcc != '0' && toAcc != '') {//modify by hjs
		//add by linrui for mul-ccy
		document.getElementById("toCurrency").value= to_ccy;
		document.getElementById("showToCurrency").innerHTML = "";
		var url = '/cib/jsax?serviceName=AccInTxnService&showToAcc=' + toAcc + '&language=' + language + '&to_ccy=' + to_ccy;//mod by linrui for mul-language20171117
   		//registerElement('showToCurrency');
		getMsgToElement(url, toAcc, '', null,true,language);
		document.getElementById("toAccount2").value = toAcc;
	}
}
function otherAcc(toAcc){
	if ((toAcc != null) && (toAcc != '')) {
		var url = '/cib/jsax?serviceName=AccInTxnService&accType=other&showToAcc=' + toAcc +'&language='+language;//mod by linrui for mul-language20171117
   		//registerElement('showToCurrency');
		getMsgToElement(url, toAcc, '', null,true,language);
		
	}
}



//add by linrui for mul-ccy select begin 20190426
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
	//add by lzg 20190612
	var fromCurrency = document.getElementById("fromCurrency");
	if(fromCurrency.options.length == 2){
		fromCurrency[1].selected = true;
		changeFromCcy(fromCurrency[1].value);
	}
	//add by lzg end
  	
}
function showToCcyList(originSelect, targetSelect,action, toAcct){
	//action check ccy
	document.getElementById("toAccount").value= toAcct;
	if ((originSelect.value != null) && (originSelect.value != '')) {
		   var params = getParams(originSelect, targetSelect);
		   var url = '/cib/jsax?serviceName=AccInTxnService&action='+action+'&'+ params+ '&showToAcc=' + toAcct +'&language=' + language;
		   getMsgToSelect(url,'', null, false,language);
		   
	}
  	//add by lzg 20190612
	var toCurrency = document.getElementById("toCurrency");
	if(toCurrency.options.length == 2){
		toCurrency[1].selected = true;
		changeToCcy(toCurrency[1].value);
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
//show showToCcy Amt Dev
function changeToCcy(toCcy){
	var action = 'toCcy';
	//add by linrui for mul-ccy		
	document.getElementById("toCurrency").value= toCcy;
	var url = '/cib/jsax?serviceName=AccInTxnService&action='+action+ '&showToCcy=' + toCcy +'&language='+language;
	//getMsgToElement(url, fromAcc, '', null,false,language);
	getMsgToElement(url, toCcy, '', null,true,language);			
	
}
//get length remark
function remarkLength(str){
	var strLength = str.replace(/[\u4e00-\u9fa5]/g,"***").length;
	if(strLength>120){
		//alert(strLength);
		alert('<set:label name="err.sys.reference.maxerror" rb="app.cib.resource.common.errmsg"/>');
		return false;
	}
	return true;
	
}


</script>
<style type="text/css">
<!--
.STYLE1 {font-size: 60pt}
-->
</style>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/transferInBANK.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.transfer_bank" -->
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
		  <form action="/cib/transferInBANK.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <set:messages width="100%" cols="1" align="center"/>
            <set:fieldcheck name="transferInBANK" form="form1" file="transfer_BANK" />
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="4" class="groupinput"><set:label name="Fill_Info" defaultvalue="Fill the following Information"/></td>
              </tr>
              <tr>
                <td colspan="4"  class="label1"><span>&nbsp;&nbsp;</span><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
              </tr>
              <tr class="greyline">
                <td width="16%" valign="top" class="label1"><span class = "xing">*</span><set:label name="From_Account" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="3" >
                <%-- <select id="fromAccount" name="fromAccount" nullable="0" onChange="changeFromAcc(this.value)">
				<option selected value="" selected><set:label name="select_account" defaultvalue="----- Select  an Account ------"/></option>
                  <set:list name="transferuser"> <option id = "<set:listdata  name='ACCOUNT_NO' />" value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_INFO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_INFO' />
                      </option>
                  </set:list>
                </select> --%>
                <%-- add by linrui for change mul-fromaccount --%>
                <select id="fromAccount" name="fromAccount" nullable="0" onChange="showFromCcyList(this, $('fromCurrency'),'getFromCcy', this.value);">
				<option selected value="" selected><set:label name="select_account" defaultvalue="----- Select  an Account ------"/></option>
                  <set:list name="transferuser"> <option  value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_NO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_NO' />
                      </option>
                  </set:list>
                  </select>
                <%-- end --%>
                
                  </td>
              </tr>
              
              <%--add by linrui for select from ccy --%>
              <tr>
                  <td class="label1"><span class = "xing">*</span><set:label name="From_ccy" defaultvalue="From Currency"/></td>
                  <td class="content1" colspan="3" ><table><tr><td><select name="fromCurrency" id="fromCurrency" nullable="0"  onChange="changeFromCcy(this.value);">
                      <option  value="" selected><set:label name="select_ccy" defaultvalue="----Please select Currency----"/></option>
                    </select>  </td></tr></table>
                    
                    <div name="showAmountArea" id="showAmountArea" style="display: none"><table border="0" cellpadding="0" cellspacing="0">
                    <tr><td><set:label name="availd_balance" defaultvalue="Available Balance :"/>&nbsp;</td><td> <div name="showAmount"  format="amount" id="showAmount" ></div></td></tr></table></div>
                  </td>
               </tr>
               <%-- end --%>
              
              <tr class="greyline"> 
                <td colspan="4"  class="label1"><span>&nbsp;&nbsp;</span><b><set:label name="Transfer_To" defaultvalue="Transfer To"/></b></td>
              </tr>
			  <tr>
                <td class="label1"><span class = "xing">*</span><set:label name="To_Account" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="3" >
                <%--
                <select id="toAccount" name="toAccount" nullable="0" onChange="changeToAccList(this.value);">
				 <option value="0" selected><set:label name="other_account" defaultvalue="----- Other Accounts ------"/></option>
				   <set:list name="transferuser"> <option value="<set:listdata  name='ACCOUNT_NO' />" 
                           <set:listselected key='ACCOUNT_INFO' equals='selectToAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_INFO' />
                      </option>
                  </set:list>
				  </select> 
				   --%>
				  <%-- mod by linrui for select ccy list 20190426 --%>
				  <select id="toAccount" name="toAccount" nullable="0" onChange="showToCcyList(this, $('toCurrency'),'getToCcy', this.value);">
				 <option value="0" selected><set:label name="select_account" defaultvalue="----- Select  an Account ------"/></option>
				   <set:list name="transferuser"> <option  value="<set:listdata  name='ACCOUNT_NO' />" 
                           <set:listselected key='ACCOUNT_NO' equals='selectToAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_NO' />
                      </option>
                  </set:list>
				  </select>
				  <%-- end --%>
				  
				 </td>
			  </tr>
			  <%-- add by linrui for select ccy list 20190426 --%>
			  <tr class="greyline">
                  <td class="label1"><span class = "xing">*</span><set:label name="To_ccy" defaultvalue="To Currency"/></td>
                  <td class="content1" colspan="3" ><table><tr><td><select name="toCurrency" id="toCurrency" nullable="0" onChange="changeToCcy(this.value);">
                      <option value="" selected><set:label name="select_ccy" defaultvalue="----Please select Currency----"/></option>
                    </select>  </td></tr></table>
                  </td>
              </tr>
              <%--end --%>
			  <%--<tr>
                <td class="label1"></td>
				<td class="content1" colspan="3"><input name="toAccount2" type="text" id="toAccount2" value="<set:data name='toAccount2'/>" size="20" maxlength="12" onblur="otherAcc(this.value)"></td>
			  </tr>--%>
              <%--<tr class="label1">
                <td valign="top" class="label1"></td>
                <td class="content1" colspan="3" ><b><set:label name="Transfer_Amount_Info1" defaultvalue="You either input the actual amount of funds to be transferred OR the amount to be debited from the selected account above" /></b></td>
              </tr>
              --%>
              <!-- modified by lzg 20190509 -->
              <!-- add by linrui 20181105 -->
			  <!--<tr class="greyline" id="beneficiaryName" style='display:"";'>
                <td valign="top" class="label1"><set:label name="To_Name" defaultvalue="Beneficiary Name"/></b></td>
				<td class="content1" colspan="3"><input name="toName" type="text" id="toName" value="<set:data name='toName'/>" size="60" maxlength="60" ></td>
			  </tr>
			  --><!-- end -->
			  <!-- modified by lzg end -->
			  <tr class="label1" >
                <td valign="top" class="label1"></td>
                <td class="content1" colspan="3" ><b><set:label name="Transfer_Amount_Info1" defaultvalue="You either input the actual amount of funds to be transferred OR the amount to be debited from the selected account above" /></b></td>
              </tr>
              <tr class="greyline">
                 <td valign="top" class="label1"><span class = "xing">*</span><set:label name="Transfer_Amount" defaultvalue="Transfer Amount"/></b></td>
                <td width="34%" class="label1"><set:label name="Amount_to_be_Transferred" defaultvalue="Amount to be Transferred"/></td>
                <td width="5%" align="right" class="content1"><div name="showToCurrency" id="showToCurrency"><set:data name="toCurrency" db="rcCurrencyCBS"/></div></td>
                <td width="45%" class="content1"><input id="transferAmount" name="transferAmount" type="text" value="<set:data name='transferAmount' format='amount'/>" size="15" maxlength="20"  onFocus ="document.form1.debitAmount.value='';"></td>
              </tr>
              <tr>
                <td></td>
                <td width="34%" class="label1"><set:label name="Transfer_Info1" defaultvalue="Amount to be debited from the selected account above"/></td>
                <td align="right" class="content1"><div name="showFromCurrency" id="showFromCurrency"><set:data name="fromCurrency" db="rcCurrencyCBS"/></div></td>
                <td class="content1"><input id="debitAmount" name="debitAmount" type="text" value="<set:data name='debitAmount' format='amount'/>" size="15" maxlength="20" onFocus="document.form1.transferAmount.value='';"></td>
              </tr>
              <tr class="greyline">
                <td class="greyline"><span>&nbsp;&nbsp;&nbsp;</span><set:label name="Remark" defaultvalue="Remark"/></td>
                <td class="content1" colspan="3"><input id="remark" name="remark" type="text" value="<set:data name='remark'/>" size="50" maxlength="120" ></td>
              </tr>
              <!-- ADD BY LINRUI 20190911 -->
              <tr>
                <td class="greyline"><span>&nbsp;&nbsp;&nbsp;</span><set:label name="Remark_Host" defaultvalue="Remark"/></td>
                <td class="content1" colspan="3"><input id="remarkHost" name="remarkHost" type="text" value="<set:data name='remarkHost'/>" size="50" maxlength="120" ></td>
              </tr>
              <!-- end -->
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <!-- add by lzg 20190612 -->
            	<%@ include file="/common/transferTips.jsp" %>
              <!-- add by lzg end -->
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
			  <td colspan="2" class="sectionbutton">
                <input id="submit1" name="submit1" type="button" value="<set:label name='Transfer' defaultvalue='Transfer' />" tabindex="3"  onClick="doSubmit();">
<!--                <input id="bottonReset" name="bottonReset" type="reset" onClick="document.getElementById('showAmountArea').style.display='none';" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="add">	
				<!-- get four parameter 20190424 -->
				<input id="fromAccount" name="fromAccount" type="hidden" value="">	
				<input id="fromCurrency" name="fromCurrency" type="hidden" value="">	
				<input id="toAccount" name="toAccount" type="hidden" value="">	
				<input id="toCurrency" name="toCurrency" type="hidden" value="">
				<!-- add by lzg 20190731 -->
				<input id="language" name = "language" type = "hidden" value = "">
				<!-- add by lzg end -->	
				<!-- end -->
				<!-- add by lzg 20190624 for pwc-->
				<set:singlesubmit/>
				<!-- add by lzg end -->
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
