<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.doc_archive">
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
<script language="JavaScript" src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
var listSize = 0;
if('<set:data name="listSize"/>' != ''){
	listSize = parseInt('<set:data name="listSize"/>');
}
var selectedCount = 0;
function pageLoad(){
	var form1 = document.form1;
	if ('<set:data name="rangeType"/>' != 'date'){
		form1.rangeType[0].checked = true;
		form1.rangeType[0].onclick();
	} else {
		form1.rangeType[1].checked = true;
		form1.rangeType[1].onclick();
	}
	// review account number
	if('<set:data name="accountType"/>' != '' && '<set:data name="accountType"/>' != '0'){
		showAccList($('accountType'), $('accountNo'), 
			function(){
				if('<set:data name="accountNo"/>' != '')
					$('accountNo').value = '<set:data name="accountNo"/>';
			}
		);
	}
	//set event for ALL checkbox
	var allCheckbox = document.getElementsByName('allCheckbox')[0];
	allCheckbox.onclick = function(){selectAll(this)};
	//set event for checkbox
	var keys = document.getElementsByName('key');
	if(!keys) return;
	if(keys.length<1) return;
	if(keys.length){
		for(i=0; i<keys.length; i++) {
			keys[i].onclick = function(){countSelected(this);checkStatus();};
			//for review
			if(keys[i].checked){
				keys[i].onclick();
			}
		}
	} else {
		keys.onclick = function(){countSelected(this);checkStatus();};
		if(keys.checked){
			keys.onclick();
		}
	}
}
//for button
function checkStatus() {
	if(selectedCount < 1){
		document.getElementById('download').disabled =true;
	} else if(selectedCount >= 1){
		document.getElementById('download').disabled =false;
	}
}
// count the checkbox selected
function countSelected(obj) {
	if(obj.checked){
		if(selectedCount != listSize)
			selectedCount++;
	} else {
		if(selectedCount > 0)
			selectedCount--;
	}
}
// select all checkbox
function selectAll(obj) {
	var keys = document.getElementsByName('key');
	if(!keys) return;
	if(keys.length<1) return;
	if(keys.length){
		for(i=0; i<keys.length; i++) {
			keys[i].checked = obj.checked;
			keys[i].onclick();
		}
	} else {
		keys.checked = obj.checked;
		keys.onclick();
	}
}
function getZipFileParams(){
	var retValue = '';
	var keys = document.getElementsByName('key');
	if(!keys) return false;
	if(keys.length<1) return false;
	if(keys.length){
		for(i=0; i<keys.length; i++) {
			if(keys[i].checked){
				retValue += 'key='+keys[i].value+'&';
			}
		}
		if(retValue!=''){
			retValue = retValue.substring(0, retValue.length-1);
		}
	} else {
		if(keys.checked){
			retValue += 'key='+keys;
		}
	}
	return retValue;
}
function doSubmit(arg) {
	if(validate_statement(document.getElementById("form1"))){
		if (arg == 'list') {
			document.form1.ActionMethod.value = 'listDoc';
			document.getElementById("form1").submit();
			setFormDisabled("form1");
		} else if(arg=='download'){
			var retValue = getZipFileParams();
			if(retValue==false) return;
			window.location.href='/cib/DownloadZip?' + retValue;
		}
	}
}
function changeDoc(arg) {
	if (arg == 'statement') {
		window.location.href='/cib/estatement.do?ActionMethod=listDocLoad';
	} else if (arg == 'advice') {
		window.location.href='/cib/eadvice.do?ActionMethod=listDocLoad';
	} else if (arg == 'reports') {
		window.location.href='/cib/ereports.do?ActionMethod=listDocLoad';
	} else if (arg == 'forms') {
		window.location.href='/cib/eforms.do?ActionMethod=listDocLoad';
	}
	setFormDisabled("form1");
}
function changeRange(range){
	if(range == 'range'){
		document.getElementById("dateFrom").value='';
		document.getElementById("dateTo").value='';
		document.getElementById("dateFrom").disabled = true;
		document.getElementById("dateTo").disabled = true;
		document.getElementById("dateRange").value=0;
		document.getElementById("dateRange").disabled = true;
		document.getElementById("range").disabled = false;
	}else{
		document.getElementById("dateFrom").disabled = false;
		document.getElementById("dateTo").disabled = false;
		document.getElementById("dateRange").disabled = false;
		document.getElementById("range").value = '1';
		document.getElementById("range").disabled = true;
	}
}
function showAccList(originSelect, targetSelect, callback){
	if(callback == 'undefined'){
		callback = null;
	}
	var params = getParams(originSelect, targetSelect);
	var url = '/cib/jsax?serviceName=AccountsService&' + params;
	getMsgToSelect(url,'', callback,true,language);
}
function getParams(originSelect, targetSelect) {
	var targetType = 'targetType=object';
	var targetId = 'targetId=' + originSelect.id;
	var originValue = 'originValue=' + originSelect.value;
	var subListId = 'subListId=' + targetSelect.id;
	var params = '';
	params = params + targetType + '&' + targetId + '&' + originValue + '&' + subListId;
	return params;
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/estatement.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.doc_archive" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="MDB Corporate Online Banking > Pay Bills >  Payment History"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="PAYMENT HISTORY"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/estatement.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/>
				  <set:fieldcheck name="statement" form="form1" file="doc_archive" /> </td>
                </tr>
              </table><table border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_o"><a href="#" onClick="changeDoc('statement');"><set:label name="functionTitle_statement"/></a></td>
                        <td class="rtab1_o"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                  </table></td><%--
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_c"><a href="#" onClick="changeDoc('advice');"><set:label name="functionTitle_advice"/></a></td>
                        <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                  </table></td>
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_c"><a href="#" onClick="changeDoc('reports');"><set:label name="functionTitle_reports"/></a></td>
                        <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                  </table></td>
                  <td><table border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td class="ltab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                        <td class="tab1_c"><a href="#" onClick="changeDoc('forms');"><set:label name="functionTitle_forms"/></a></td>
                        <td class="rtab1_c"><img src="/cib/images/shim.gif" width="8" height="26"></td>
                      </tr>
                  </table></td>--%>
                  
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupinput">&nbsp;</td>
                </tr>
              </table>
              <table border=0 bgcolor=#808080 cellpadding=0 cellspacing=1 width=100%>
                <tr>
                  <td bgcolor="#FFFFFF" class="content"><table border="0" cellpadding="5" cellspacing="0" width="100%" bgcolor="#FFFFFF">
                      <tr>
                        <td width="79" valign="top" class="content" align="right"><input id="rangeType" name="rangeType" type="radio" value="range" checked onClick="changeRange('range')"></td>
                        <td colspan="2" valign="top" class="content"><set:label name='range' defaultvalue='range'/>&nbsp;
                          <select id="range" name="range" onChange="">
                            <set:rblist file="app.cib.resource.srv.range_type"> <set:rboption name="range"/> </set:rblist>
                          </select></td>
                      </tr>
                      <tr class="greyline">
                        <td valign="top" class="content" align="right"><input id="rangeType" name="rangeType" type="radio" value="date" onClick="changeRange('date')"></td>
                        <td colspan="2" valign="top" class="content"><set:label name='from' defaultvalue='from'/>&nbsp;&nbsp;&nbsp;
                          <input name="dateFrom" type="text" id="dateFrom" size="12" maxlength="10" value="<set:data name='dateFrom'/>" disabled>
                          <span class="content1"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateFrom').disabled){scwShow(document.getElementById('dateFrom'), this,language);}" ></span> &nbsp;&nbsp;&nbsp; <set:label name='to' defaultvalue='to'/>&nbsp;
                          <input name="dateTo" type="text" id="dateTo" size="12" maxlength="10" value="<set:data name='dateTo'/>" disabled>
                          <span class="content1"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "if(!document.getElementById('dateTo').disabled){scwShow(document.getElementById('dateTo'), this,language);}" ></span>
                          <select id="dateRange" name="dateRange" disabled="disabled" onChange="setDateRange(this, document.getElementById('dateFrom'), document.getElementById('dateTo'))">
                            <option value='0'><set:label name="select_date_short_cut" defaultvalue="----- Select a Date Short-cut ------"/></option>
                            <set:rblist file="app.cib.resource.common.date_selection"> <set:rboption/> </set:rblist>
                          </select></td>
                      </tr>
                      <%--
                      <tr>
                        <td valign="top"><b><set:label name="view_by"/></b></td>
                        <td width="81" valign="top"><set:label name="account_type"/></td>
                        <td width="777">
						  <select name="accountType" id="accountType" onChange="showAccList(this, $('accountNo'));">
                            <option value="0" selected><set:label name="all"/></option>
                            <set:rblist file="app.cib.resource.common.account_type_host"> <set:rboption name="accountType"/> </set:rblist>
                          </select></td>
                      </tr>
                      --%>
                      <tr class="greyline">
                        <td valign="top">&nbsp;</td>
                        <td valign="top"><set:label name="accounts"/></td>
                        <td width="777">
						  <select name="accountNo" id="accountNo" nullable="0">
                            <option value="0" selected><set:label name="all"/></option>
							<!--
                            <set:rblist db="caoasaAccountByUser"> <set:rboption name="accountNo"/> </set:rblist>
							-->
                          </select>
                        </td>
                      </tr>
                    </table></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td width="53%" height="40" class="sectionbutton"><div align="right">
                      <input id="search" type="button" value="&nbsp;<set:label name='enquiry' defaultvalue='enquiry'/>&nbsp;" name="search" onClick="doSubmit('list')">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="list">
                    </div></td>
                  <td width="47%" height="40" class="sectionbutton">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td width="6%" align="center" class="listheader1"><set:label name="all"/><set:listcheckbox name="allCheckbox" value="allCheckbox" text=""/></td>
                  <td align="left" class="listheader1"><set:label name="date"/></td>
                  <td align="left" class="listheader1"><set:label name="account_no"/></td>
                  <td align="center" class="listheader1"><set:label name="currency"/></td>
                  <td align="left" class="listheader1"><set:label name="account_name"/></td>
                  <td align="left" class="listheader1"><set:label name="account_type"/></td>
                  <td width="11%" align="center" class="listheader1"><set:label name="statement"/></td>
                </tr>
                <set:list name="docList" showPageRows="10" showNoRecord="YES">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1" align="center"><set:listcheckbox name="key" value="KEY" text=""/></td>
                  <td class="listcontent1" align="left"><set:listdata name="STATEMENT_DATE" format="date"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="ACCOUNT_NO"/></td>
                  <td class="listcontent1" align="center"><set:listdata name="ACCOUNT_CCY" db="rcCurrencyCBS"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="ACCOUNT_NAME"/></td>
                  <td class="listcontent1" align="left"><set:listdata name="ACCOUNT_TYPE" rb="app.cib.resource.common.account_type_host"/></td>
                  <td class="listcontent1" align="center"><set:listdata name="FILE_LINK"/></td>
                </tr>
                </set:list>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><div align="left">
                    <input name="download" id="download" type="button" value="&nbsp;<set:label name='download'/>&nbsp;" onClick="doSubmit('download')" disabled>
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
