<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.corp_history">
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
}
function doSubmit()
{
	if(validate_corpHistory(document.getElementById("form1"))){
		setFormDisabled('form1');
		document.getElementById("form1").submit();
	}
}
var listNameToBeChange ="";
function showTree(listName) {
	listNameToBeChange = listName;
	var selectCorpId = document.getElementById(listNameToBeChange).value;
	var url="/cib/corporation.do?ActionMethod=listCorpForTree&showRoot=Y&selectCorpId=" + selectCorpId;
	var popWin1= window.open(url,"temp1","toolbar=no,location=no,top=50,left=50,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=530,height=400");
	popWin1.focus();
}
function changeCorp(arg) {
	putFieldValues(listNameToBeChange, arg);
	if(listNameToBeChange=='fromCorporation'){
		showList($(listNameToBeChange),$('fromAccount'));
	}else{
		showList($(listNameToBeChange),$('toAccount'));
	}
}
function changeToAcc(toAcc){
	if ((toAcc != null) && (toAcc != '')) {
		var url = '/cib/jsax?serviceName=AccInTxnService&showToAcc=' + toAcc + '&language=' + language+'&language='+language;
   		//registerElement('showToCurrency');
		getMsgToElement(url, toAcc, '', null,false,language);
	}
}
function showList(originSelect, targetSelect){
	if ((originSelect.value != null) && (originSelect.value != '')) {
		var params = getParams(originSelect, targetSelect);
		var url = '/cib/jsax?serviceName=CorpAccListService&'+ params+'&language='+language;
		getMsgToSelect(url,'', null,true,language);
  	} else {
		targetSelect.options.length = 0;
		var optElement = document.createElement('option');
		optElement.setAttribute('value', '');
		optElement.innerHTML = '----- Select an Account ------';
		targetSelect.appendChild(optElement);
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
	if ((fromAcc != null) && (fromAcc != '')) {
		var url = '/cib/jsax?serviceName=AccInTxnService&showFromAcc=' + fromAcc+'&language='+language;
   		//registerElement('showAmount');
   		//registerElement('showFromCurrency');
		getMsgToElement(url, fromAcc, '', null,true,language);
		document.getElementById("showAmountArea").style.display = "block";
	}else{
		document.getElementById("showAmountArea").style.display = "none";
	}
}
function changeRange(range){
if(range == 'all'){
document.getElementById("dateFrom").value='';
document.getElementById("dateTo").value='';
document.getElementById("dateFrom").disabled = true;
document.getElementById("dateTo").disabled = true;
document.getElementById("dateRange").value=0;
document.getElementById("dateRange").disabled = true;
}else{
document.getElementById("dateFrom").disabled = false;
document.getElementById("dateTo").disabled = false;
document.getElementById("dateRange").disabled = false;
}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpHistory.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.corp_history" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="BANK Online Banking >Corp Fund Allocation > Corporation Transfer History"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="CORPORATION TRANSFER HISTORY"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/corpHistory.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="corpHistory" form="form1" file="corp_transfer" />                        </td>
                      </tr>
            </table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr class="greyline">
                <td width="28%" class="label1"><set:label name="Corporation" defaultvalue="Corporation"/></td>
                <td class="content1" ><select name="fromCorporation"  id="fromCorporation" nullable="0" onChange="showList(this,$('fromAccount'));">
                    <option value=""><set:label name="select_corporation" defaultvalue="----- Select a Corporation ------"/></option>
                    <set:rblist db="corpInTreeWithRoot"> <set:rboption/> </set:rblist>
                  </select>
                    <a href="#" onClick="showTree('fromCorporation');"> <img src="/cib/images/tree.gif" border="0" align="absmiddle"></a></td>
                </tr>
              <tr >
                <td class="label1"><set:label name="Account_Number"/></td>
                <td class="content1" ><select name="fromAccount" id="fromAccount" nullable="0" onChange=" ">
                    <option value=""><set:label name="select_account" defaultvalue="----- Select an Account ------"/></option>
                  </select>
                    <div name="showAmountArea" id="showAmountArea" style="display: none">
                      <table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td>Available Balance :&nbsp;</td>
                          <td><div name="showAmount" id="showAmount"></div></td>
                        </tr>
                      </table>
                  </div></td>
                </tr>
              <tr class="greyline">
                <td width="28%" height="39" class="label1"><set:label name="payment_period" defaultvalue="payment_period"/></td>
                <td width="72%" class="content1"><span class="content">
                  <input id="date_range" name="date_range" type="radio" value="all" checked onClick="changeRange('all')">
                  <set:label name="all" defaultvalue="all"/> </span></td>
              </tr>
              <tr>
                <td width="28%" class="label1">&nbsp;</td>
                <td width="72%" class="content1"><span class="content">
                  <input id="date_range" name="date_range" type="radio" value="range" onClick="changeRange('range')">
                  </span> <set:label name="from"/>
                  <input id="dateFrom" name="dateFrom" type="text" value="" size="12" maxlength="10" disabled="disabled">
                  <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language)};" >&nbsp;&nbsp;&nbsp;&nbsp; <set:label name="to"/>&nbsp;&nbsp;&nbsp;&nbsp;
                  <input id="dateTo" name="dateTo" type="text" value="" size="12" maxlength="10" disabled="disabled">
                  <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language)};" >
                  <select id="dateRange" name="dateRange" disabled="disabled" onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'));">
                    <option value='0'><set:label name="select_date_short_cut" defaultvalue="----- Select a Date Short-cut ------"/></option>
                    <set:rblist file="app.cib.resource.common.date_selection"> <set:rboption/> </set:rblist>
                </select></td>
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
					<input id="ok" name="ok" type="button" value="&nbsp;&nbsp;<set:label name='search' defaultvalue=' search '/>&nbsp;&nbsp;" onClick="doSubmit('listHistory')">
				  	<input id="ActionMethod" name="ActionMethod" type="hidden" value="listHistory">
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
