<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.bill_payment">
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
<script language=JavaScript>
/*
 * create a transId_payType Mapping
 * hjs 2006-07-24
 */
 var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
var listSize = <set:data name='listSize'/>;
var id_payType_map = new Array();
var PAYMENT_TYPE_GENERAL_TEMPLATE = '<set:data name="general_Template"/>';
var PAYMENT_TYPE_CARD_TEMPLATE = '<set:data name="card_Template"/>';
var selectedCount = 0;
<set:list name="templateList">
	id_payType_map['<set:listdata name='transId'/>'] = '<set:listdata name='payType'/>';
</set:list>
var p = null;
var e = null;
var d = null;
var a = null;
function pageLoad(){
	p = document.getElementById('pay');
	e = document.getElementById('edit');
	d = document.getElementById('delete');
	a = document.getElementById('add');
	//set event for ALL checkbox
	var allCheckbox = document.getElementsByName('allCheckbox')[0];
	allCheckbox.onclick = function(){selectAll(this)};
	//set event for checkbox
	var transIds = document.getElementsByName('transId');
	if(!transIds) return;
	if(transIds.length<1) return;
	if(transIds.length){
		for(i=0; i<transIds.length; i++) {
			transIds[i].onclick = function(){countSelected(this);checkStatus();};
			//for review
			if(transIds[i].checked){
				transIds[i].onclick();
			}
		}
	} else {
		transIds.onclick = function(){countSelected(this);checkStatus();};
		if(transIds.checked){
			transIds.onclick();
		}
	}
}
//for button
function checkStatus() {
	if(selectedCount < 1){
		p.disabled =true;
		e.disabled =true;
		d.disabled =true;
	} else if(selectedCount == 1){
		p.disabled =false;
		e.disabled =false;
		d.disabled =false;
	} else if(selectedCount > 1) {
		p.disabled =false;
		e.disabled =true;
		d.disabled =true;
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
	var transIds = document.getElementsByName('transId');
	if(!transIds) return;
	if(transIds.length<1) return;
	if(transIds.length){
		for(i=0; i<transIds.length; i++) {
			transIds[i].checked = obj.checked;
			transIds[i].onclick();
		}
	} else {
		transIds.checked = obj.checked;
		transIds.onclick();
	}
}
function checkPayType(transID){
	var payType = id_payType_map[transID];
	if (payType == null || payType == '' || typeof payType == 'undefined') {
		return null;
	} else {
		return payType;
	}
}
function doSubmit(arg) {

	if (arg == 'add') {
		document.form1.ActionMethod.value = 'addTemplateLoad';
		document.getElementById("form1").submit();
		setFormDisabled("form1");
	} else if (arg == 'list') {
		document.form1.ActionMethod.value = 'listTemplate';
		document.getElementById("form1").submit();
		setFormDisabled("form1");
	} else if(validate_template(document.getElementById("form1"))){
		if (arg == 'edit') {
			document.form1.ActionMethod.value = 'editTemplateLoad';
		} else if (arg == 'delete') {
			document.form1.ActionMethod.value = 'deleteTemplateLoad';
		} else if (arg == 'pay') {
			var transIds = document.form1.transId;
			if(!transIds.length){
				transIds = new Array();
				transIds[0] = document.form1.transId;
			}
			if(selectedCount>1){
				document.form1.ActionMethod.value = 'batchPaymentLoad';
			} else {
				for (i=0; i<transIds.length; i++){
					if (transIds[i].checked) {
						var payType = checkPayType(transIds[i].value);
						if (payType == null) {
							alert('payType is null(transID:'+ transIds[i].value +')');
							break;
						}
						if (payType == PAYMENT_TYPE_GENERAL_TEMPLATE) {
							document.form1.ActionMethod.value = 'gtPaymentLoad';
							break;
						} else if (payType == PAYMENT_TYPE_CARD_TEMPLATE) {
							document.form1.ActionMethod.value = 'ctPaymentLoad';
							break;
						}
					}
				}
			}
		}

		document.getElementById("form1").submit();
		setFormDisabled("form1");
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/templatePayment.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.bill_payment" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_tmp_list" defaultvalue="MDB Corporate Online Banking > Pay Bills >  FREQUENT PAYMENT LIST"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_tmp_list" defaultvalue="FREQUENT PAYMENT LIST"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/templatePayment.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="template" form="form1" file="bill_payment" />
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
			  <tr>
			  	<td colspan="4">
					<b><set:label name="merchant" defaultvalue="merchant_list"/>:</b>
					<select id="merSelected" name="merSelected" onChange="doSubmit('list')">
						<option value="9999" selected><set:label name="all"/></option>
						<set:rblist db="merchant">
                    		<set:rboption name="merSelected"/>
						</set:rblist>
					</select>
				</td>
			  </tr>
              <tr>
                <td width="5%" class="listheader1" align="center" valign="middle"><set:label name="all"/><set:listcheckbox name="allCheckbox" value="allCheckbox" text=""/></td>
                <td width="33%" class="listheader1" align="left"><set:label name="merchant" defaultvalue="merchant"/></td>
                <td width="33%" class="listheader1" align="left"><set:label name="billNoAndCardNo" defaultvalue="billNoAndCardNo"/></td>
                <td width="29%" class="listheader1" align="left"><set:label name="debit_account" defaultvalue="debit_account"/></td>
              </tr>
			  <set:list name="templateList" showPageRows="10" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1" align="center" valign="middle"><set:listcheckbox name="transId" value="transId" text=""/>&nbsp;</td>
                <td class="listcontent1" align="left" valign="middle"><set:listdata name="merchant" db="merchantAll"/>&nbsp;</td>
                <td class="listcontent1" align="left" valign="middle">
					<set:listdata name="billNo1"/>&nbsp;
					<set:listif name="billName" value="" condition="notequals"><br><set:listdata name="billName"/>&nbsp;</set:listif>
					<set:listif name="remark" value="" condition="notequals"><br><set:listdata name="remark"/>&nbsp;</set:listif>
				</td>
                <td class="listcontent1" align="left" valign="middle"><set:listdata name="fromAccount"/></td>
              </tr>
			  </set:list>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
				<input name="pay" id="pay" type="button" value="&nbsp;&nbsp;<set:label name='pay' defaultvalue=' pay '/>&nbsp;&nbsp;" onClick="doSubmit('pay')" disabled>
				<input name="edit" id="edit" type="button" value="&nbsp;&nbsp;<set:label name='edit' defaultvalue=' edit '/>&nbsp;&nbsp;" onClick="doSubmit('edit')" disabled>
				<input name="delete" id="delete" type="button" value="&nbsp;&nbsp;<set:label name='delete' defaultvalue=' delete '/>&nbsp;&nbsp;" onClick="doSubmit('delete')" disabled>
				<input name="add" id="add" type="button" value="&nbsp;&nbsp;<set:label name='add' defaultvalue=' add '/>&nbsp;&nbsp;" onClick="doSubmit('add')">
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="add">
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
