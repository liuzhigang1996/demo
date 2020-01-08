<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.time_deposit">
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
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></script>
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
//----------------modified by lzg for GAPMC-EB-001-0050-----------------

//	var ccy = $F('currency');
//	if ((ccy!="") && (ccy!="0")){
//		showPeriodList($('currency'), $('term'), true);
//	}
	if(language == "zh_HK"){
		document.getElementById("showENG").style.display="none";
		document.getElementById("showCHN").style.display="";
	}else{
		document.getElementById("showENG").style.display="";
		document.getElementById("showCHN").style.display="none";
	}
	addTrList();
	if(!"<set:data name='currency' />" == ""){
		var currency = document.getElementById("currency");
		for(var i=0;i<currency.options.length;i++){
		    if(currency[i].value=='<set:data name='currency' />'){
		        currency[i].selected=true;
		        showPeriodList($('currency'), $('term'), true);   
	    	}  
		}  
	}
	
	/*if(!"<set:data name='selectTermValue' />" == ""){
		var term = document.getElementById("term");
		setTimeout(function(){
			for(var i=0;i<term.options.length;i++){
			    if(term[i].value=='<set:data name='selectTermValue' />'){
			        term[i].selected=true;
		    	}
			}
		},1000);
	}*/
	var currentAccount = document.getElementById("currentAccount");
	var currentAccountCcy = document.getElementById("currentAccountCcy");
	if(!"<set:data name='currentAccount' />" == ""){
		for(var i=0;i<currentAccount.options.length;i++){
		    if(currentAccount[i].value=='<set:data name='currentAccount' />'){
		        currentAccount[i].selected=true;
		        showFromCcyList(currentAccount,currentAccountCcy,'getFromCcy','<set:data name='currentAccount' />');
	    	}  
		}  
	}
	if(!"<set:data name='currentAccountCcy' />" == ""){
		for(var i=0;i<currentAccountCcy.options.length;i++){
		    if(currentAccountCcy[i].value=='<set:data name='currentAccountCcy' />'){
		        currentAccountCcy[i].selected=true;
	    	}  
		}
		changeFromCcy("<set:data name='currentAccountCcy' />");
	}
//---------------------------modfied by lzg end---------------------------
}


function IEVersion() {
   var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串  
   var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器  
   var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器  
   var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
   if(isIE) {
       var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
       reIE.test(userAgent);
       var fIEVersion = parseFloat(RegExp["$1"]);
       if(fIEVersion == 7) {
           return 7;
       } else if(fIEVersion == 8) {
           return 8;
       } else if(fIEVersion == 9) {
           return 9;
       } else if(fIEVersion == 10) {
           return 10;
       } else {
           return 6;//IE版本<=7
       }   
   } else if(isEdge) {
       return 'edge';//edge
   } else if(isIE11) {
       return 11;
   }else{
       return -1;//不是ie浏览器
   }
}


function addTrList(){
	url = "/cib/jsax?serviceName=GetInterestRateList&pageLanguage=" + language;
	getMsgToElement(url,'', '',function(){
		var showInterestRateTable = document.getElementById("showInterestRateTable");
		var showInterestList = document.getElementById("showInterestList").value;
		var tableHeader = document.getElementById("tableHeader").value;
		showInterestList = showInterestList.replace(/\n/g, "");
		tableHeader = tableHeader.replace(/\n/g, "");
		showInterestList = showInterestList.replace(/\'/g,"");
		tableHeader = tableHeader.replace(/\'/g,"");
		var objList =  JSON.parse(showInterestList);
		var objHeader =  JSON.parse(tableHeader);
		
		document.getElementById("showUpdateShowTime").innerHTML = document.getElementById("updateShowTime").value;
		
		//add header
		var Mythead = document.createElement("thead");
		Mythead.setAttribute("style", "text-align: center;");
		var headerTr = document.createElement("tr");
		headerTr.setAttribute("style", "display: table;width: 100%;table-layout: fixed;text-align: center;")
		var blankTd = document.createElement("td");
		blankTd.innerHTML = "&nbsp;";
		blankTd.setAttribute("style", "width:102px;border-bottom:1 px solid white;font-size: 10px;height:40px;text-align: center;");
		blankTd.setAttribute("id", "blankTd");
		headerTr.appendChild(blankTd);
		for(var i = 0; i < objHeader.length; i++){
			var headerTd = document.createElement("td");	
			headerTd.setAttribute("style", "width:105px;border-bottom:1 px solid white;font-size: 10px;height:40px;text-align: center;");
			headerTd.setAttribute("name", "headerTd");
			headerTd.innerHTML =objHeader[i].PERIOD_DESCRIPTION;
			headerTr.appendChild(headerTd);
		}
		Mythead.appendChild(headerTr);
		showInterestRateTable.appendChild(Mythead);
		
		//add content
		var MyTbody = document.createElement("tbody");
		var isIe = IEVersion();
		if(isIe == -1){
			MyTbody.setAttribute("style", "height:200px;overflow-y: scroll;");
		}else{
			document.getElementById("showRateArea").style.height = "200px";
			document.getElementById("showRateArea").style.overflowY = "scroll";
			MyTbody.setAttribute("style", "display: block;height:40px;overflow-y: scroll;");
		}
		
		for(var i = 0; i < objList.length; i++){
			var contentTr = document.createElement("tr");
			contentTr.setAttribute("name", "contentTr");
			contentTr.setAttribute("style", "display: table;width: 100%;table-layout: fixed;text-align: center;");
			var firstTd = document.createElement("td");
			firstTd.setAttribute("style", "width:105px;height:40px;font-size: 10px;border-bottom: 1px solid #C1C1C1;text-align: center;");
			firstTd.innerHTML = objList[i].CCY_NAME;
			contentTr.appendChild(firstTd);
			for(var j = 0; j < objHeader.length;j++){
				var contentTd = document.createElement("td");
				contentTd.setAttribute("style", "width:105px;height:40px;font-size: 10px;border-bottom: 1px solid #C1C1C1;text-align: center;");
				var tempObj = objList[i];
				var key = objHeader[j].PERIOD_DESCRIPTION;
				key = key.replace(/\s/g,"");
				contentTd.innerHTML = tempObj[key];
				contentTr.appendChild(contentTd);
			}
			MyTbody.appendChild(contentTr);
			showInterestRateTable.appendChild(MyTbody);
		}
		var headTd = document.getElementsByName("headerTd");
		var contentTr = document.getElementsByName("contentTr");
		for(var i = 0; i < headTd.length; i++){
			headTd[i].style.width =(headTd[0].scrollWidth)-3;
		}
		headerTr.style.height = contentTr[0].offsetHeight;
	},false,language);
}

function doSubmit(arg) {
	if(validate_new_tdAccount($("form1"))){
		if ($('currency').value=='JPY' || $('currency').value=='KRW') {
			if (!checkInteger($('principal').value)) {
				alert('<set:label name="err.txn.DecimalNotAllowed" rb="app.cib.resource.common.errmsg"/>');
				$('principal').select();
				return false;
			}
		}
		if (arg == 'ok') {
			document.form1.ActionMethod.value = 'addTimeDeposit';
		}
		document.getElementById("form1").submit();
		setFormDisabled("form1");
		//setFieldEnabled("buttonReturn");
	}
}
function checkInteger(amt) {
	if (amt.indexOf('.') > 0) {
		return false;
	} else {
		return true;
	}
}
function showPeriodListCallback(flag) {
	if(flag) {
		//$('term').value = '<set:data name="term"/>';
		var term = document.getElementById("term");
		for(var i=0;i<term.options.length;i++){
			    if(term[i].value=='<set:data name='selectTermValue' />'){
			        term[i].selected=true;
		    	}
		}
	}
}
function showPeriodList(originSelect, targetSelect, callbackFlag) {
	var params = getParamsForPeriod(originSelect, targetSelect);
	var url = '/cib/jsax?serviceName=PeriodByCcyService&' + params + '&language=' + language;
	getMsgToSelect(url,'', function(){showPeriodListCallback(callbackFlag);},true,language );
}
function getParamsForPeriod(originSelect, targetSelect) {
	var targetType = 'targetType=object';
	var targetId = 'targetId=' + originSelect.id;
	var originValue = 'originValue=' + originSelect.value;
	var subListId = 'subListId=' + targetSelect.id;
	var params = '';
	params = params + targetType + '&' + targetId + '&' + originValue + '&' + subListId;
	return params;
}
//add by linrui 20190319
function getCurrency(){
	//currentAccount
	var debitAccount = document.getElementById("currentAccount");
	var accountCcy = debitAccount.options[debitAccount.selectedIndex].innerHTML;
	var arr = accountCcy.split("-");
	accountCcy = arr[1].substring(1,4);
	document.getElementById("AccountCcy").value = accountCcy;
}
//end

//add by lzg for GAPMC-EB-001-0050
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
	document.getElementById("currentAccount").value= fromAcc;
	if ((originSelect.value != null) && (originSelect.value != '')) {
		   var params = getParams(originSelect, targetSelect);
		   var url = '/cib/jsax?serviceName=AccInTxnService&action='+action+'&'+ params+ '&showFromAcc=' + fromAcc +'&language='+language;
		   getMsgToSelect(url,'', null, false,language);
	}
	
	//add by lzg 20190612
	var currentAccountCcy = document.getElementById("currentAccountCcy");
	if(currentAccountCcy.options.length == 2){
		currentAccountCcy[1].selected = true;
		changeFromCcy(currentAccountCcy[1].value);
	}
	//add by lzg end
}

function changeFromCcy(fromCcy){
	document.getElementById("AccountCcy").value= fromCcy;
}
//add by lzg end
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/timeDeposit.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.time_deposit" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_new" defaultvalue="MDB Corporate Online Banking > Time Deposit > New Time Deposit"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_new" defaultvalue="NEW TIME DEPOSTIT"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/timeDeposit.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="new_tdAccount" form="form1" file="time_deposit" />
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="select_info" defaultvalue="select_info"/></td>
              </tr>
              <tr>
                <!--<td colspan="2" class="label1"><set:label name="notice" defaultvalue="notice"/></td>
                --></tr>
              <tr class="greyline">
                <td width="28%" class="label1"><span class = "xing">*</span><set:label name="currency" defaultvalue="currency"/></td>
                <td width="72%" class="content1">
					<select name="currency" id="currency" nullable="0" onChange="showPeriodList(this, $('term'), false);">
						<option value="0" selected><set:label name="sel_ccy"/></option>
                  		<set:rblist db="newTdCcy"> <set:rboption name="currency"/> </set:rblist>
                	</select>
				</td>
              </tr>
              <tr>
                <td class="label1"><span class = "xing">*</span><set:label name="period" defaultvalue="period"/></td>
                <td class="content1">
					<select name="term" id="term" nullable="0">
						<option value="0" selected><set:label name="sel_period"/></option>
                	</select></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><span class = "xing">*</span><set:label name="amount" defaultvalue="amount"/></td>
                <td class="content1">
					<input name="principal" type="text" id="principal" maxlength="20" value="<set:data name='principal' format='amount'/>" size="20">	
				</td>
              </tr>
              <tr>
                <td class="label1"><span class = "xing">*</span><set:label name="debit_account" defaultvalue="debit_account"/></td>
                <td class="content1">
                <!-- modified by lzg for GAPMC-EB-001-0050 -->
				<!--<select name="currentAccount" id="currentAccount" nullable="0" onChange="getCurrency()">
					<option value="0" selected><set:label name="sel_debit_acc"/></option>
                  	<set:rblist db="caoasaAccountByUser"> <set:rboption name="currentAccount"/> </set:rblist>
                    
                    <set:list name="transferuser"> <option value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_INFO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_INFO' />
                      </option>
                  </set:list>
                  
                </select>
                </td>
              </tr>
              -->
              <select id="currentAccount" name="currentAccount" nullable="0" onChange="showFromCcyList(this, $('currentAccountCcy'),'getFromCcy', this.value);">
				<option selected value="" selected><set:label name="sel_debit_acc" defaultvalue="----- --Select Debit Account-- ------"/></option>
                  <set:list name="transferuser"> <option  value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_NO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_NO' />
                      </option>
                  </set:list>
                  </select>
                  </td>
              </tr>
              <tr>
                  <td class="label1"><span class = "xing">*</span><set:label name="From_ccy" defaultvalue="Debit Currency"/></td>
                  <td class="content1" colspan="3" ><table><tr><td><select name="currentAccountCcy" id="currentAccountCcy" nullable="0"  onChange="changeFromCcy(this.value);">
                      <option  value="" selected><set:label name="sel_ccy" defaultvalue="--Select a Currency--"/></option>
                    </select>  </td></tr></table>
                  </td>
               </tr>
              <!-- modified by lzg end -->
              <!-- add by linrui for add Maturity_Instruction 20190424 -->
              <tr class="greyline">
                  <td class="label1"><span class = "xing">*</span><set:label name="Maturity_Instruction" defaultvalue="Maturity Instruction"/></td>
                  <td class="content1">
				    <select id="instCd" name="instCd" nullable="0">
                      	<set:rblist file="app.cib.resource.txn.timedeposit_inst_cd"> <set:rboption name="instCd"/> </set:rblist>		 
                    </select>
                  </td>
                </tr>
              <tr>
              <!-- end -->
              
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
           <!-- add by lzg 20190612 -->
				<%@ include file="/common/otherTips.jsp" %>
			<!-- add by lzg end -->
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
					<input id="ok" name="ok" type="button" value="&nbsp;&nbsp;<set:label name='ok' defaultvalue=' ok '/>&nbsp;&nbsp;" onClick="doSubmit('ok')">
				  	<input id="ActionMethod" name="ActionMethod" type="hidden" value="ok">
				  	<input id="showInterestList" name="showInterestList" type="hidden" >
				  	<input id="tableHeader" name="tableHeader" type="hidden" >
				  	<input id="updateShowTime" name="updateShowTime" type="hidden" >
				  	<input id="AccountCcy" name="AccountCcy" type="hidden" value="">
				  	<set:singlesubmit/>
				</td>
              </tr>
            </table>
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
<!-- add by lzg -->
<div style = "margin-left: 1%;">
	<div style = "font-weight: bold;"><set:label name = 'deposit_interest_rate' /></div><br>
	<div style = "font-size: 10px;margin-bottom: 5px;"><set:label name = 'update_time' /><span id = "showUpdateShowTime"></span></div>
	<div id = "showRateArea" >
		<table cellspacing="0" cellpadding="0" border="0" id = "showInterestRateTable"  >
		</table>
	</div>
</div>
<br><div style = "margin-left: 1%;"><span style = "color:red;font-size: 15px;"><set:label name ='showRateListTips' /></span></div>
<!-- add by lzg end -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img src="/cib/images/shim.gif" width="12" height="12"></td>
  </tr>
</table>
</body>
</set:loadrb>
<!-- InstanceEnd --></html>
