<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.autopay_instruction">
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
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function doSubmit(arg){
	if (arg == 'add') {
		document.form1.ActionMethod.value = 'addLoad';
		document.form1.mode.value = 'A';
		document.getElementById("form1").submit();
		setFormDisabled("form1");
	} else if('edit'==arg){
		if(validate_list(document.getElementById("form1"))){
			document.form1.ActionMethod.value = 'editLoad';
			document.form1.mode.value = 'E';
			document.getElementById("form1").submit();
			setFormDisabled("form1");
		}
	} else if('delete'==arg){
		if(validate_list(document.getElementById("form1"))){
			document.form1.ActionMethod.value = 'deleteLoad';
			document.form1.mode.value = 'D';
			document.getElementById("form1").submit();
			setFormDisabled("form1");
		}
	}
		
}

function pageLoad(){
}

function checkSelectedItem(cb,status)
{
	var arr = document.form1.transNo;
	var enableFlag = false ;
	if(arr != cb ){
		for (i = 0; arr.length>1 && i<arr.length; i++){
			if(arr[i] == cb){
				arr[i].checked = cb.checked;
				if(cb.checked){
					enableFlag = true ;
				}
				//enableFlag = true ;
				//document.fm.apsCode.value = aps_code;
				//document.fm.contractNo.value = contract_no;
			}
			else{
				arr[i].checked = false;
			}
		}
	}else {
		arr.checked = cb.checked;
		if(cb.checked){
			enableFlag = true ;
		}
	}
	//alert(status);
	document.getElementById("status").value=status;
	if(enableFlag){
	
		document.getElementById("edit").disabled = false ;
		document.getElementById("delete").disabled = false ;
	} else {
		document.getElementById("edit").disabled = true ;
		document.getElementById("delete").disabled = true ;
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/autopayAuthorization.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.autopay_instruction" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_autopay_list" defaultvalue="navigationTitle_autopay_list=MDB Corporate Online Banking > Pay Bills > Autopay Instruction List "/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_autopay_list" defaultvalue="AUTOPAY INSTRUCTION MAINTENANCE"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/autopayAuthorization.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="list" form="form1" file="autopay" />
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td width="5%" class="listheader1" align="center" valign="middle"></td>
                <td width="20%" class="listheader1" align="left"><set:label name="PAYMENT_TYPE" defaultvalue="Payment Type"/></td>
                <td width="25%" class="listheader1" align="left"><set:label name="CONTRACT_NUMBER" defaultvalue="Contract Number"/></td>
                <td width="20%" class="listheader1" align="left"><set:label name="PAYMENT_LIMINT" defaultvalue="Payment Limit"/></td>
                <td width="15%" class="listheader1" align="left"><set:label name="PAYMENT_ACCOUNT" defaultvalue="Payment Account"/></td>
                <!--<td width="15%" class="listheader1" align="left"><set:label name="PAYMENT_STATUS" defaultvalue="Status"/></td>-->
              </tr>
			  <set:list name="autopayList" showPageRows="10" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1" align="center" valign="middle"><input type="checkbox" name="transNo" value="<set:listdata name='transNo' />" onClick="checkSelectedItem(this,'<set:listdata name='status' />')" /></td>
                <td class="listcontent1" align="left" valign="middle"><set:listdata name="apsCode" db="autopay"/>&nbsp;</td>
                <td class="listcontent1" align="left" valign="middle">
					<set:listdata name="contractNo"/>&nbsp;
				</td>
                <td class="listcontent1" align="left" valign="middle">
                <set:listif name='paymentLimit' condition='equals' value='9.999999999E9'>
                	<set:label name="FULL_PAYMENT" defaultvalue="Full Payment"/>
                </set:listif>
                <set:listif name='paymentLimit' condition='notequals' value='9.999999999E9'>
                	<set:listif name='paymentLimit' condition='equals' value='0.0'>
                		<set:label name="Minimum_Payment" defaultvalue="Minimum Payment"/>
                	</set:listif>
                    <set:listif name='paymentLimit' condition='notequals' value='0.0'>
                		<set:listdata name='paymentLimit' format='amount' pattern='#' />
                	</set:listif>
                </set:listif>
                </td>
                <td class="listcontent1" align="left" valign="middle"><set:listdata name="payAcct"/></td>
               <!--
                <td class="listcontent1" align="left" valign="middle">
                 <set:listif name='status' condition='equals' value='0'>
                 	<set:listdata name="STATUS_NORMAL" defaultvalue="Nomal"/>
                 </set:listif>
                 <set:listif name='status' condition='equals' value='1'>
                 	<set:listdata name="AUTH_STATUS_SUBMITED" defaultvalue="Pending Approval"/>
                 </set:listif>
                 <set:listif name='status' condition='equals' value='9'>
                 	<set:listdata name="AUTH_STATUS_REMOVED" defaultvalue="Removed"/>
                 </set:listif>
                </td>-->
              </tr>
			  </set:list>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
                <input type="hidden" name="status" id="status" value="">
                <input name="edit" id="edit" type="button" value="&nbsp;&nbsp;<set:label name='EDIT' defaultvalue=' Edit '/>&nbsp;&nbsp;" onClick="doSubmit('edit')" disabled>
				<input name="delete" id="delete" type="button" value="&nbsp;&nbsp;<set:label name='DELETE' defaultvalue=' Delete '/>&nbsp;&nbsp;" onClick="doSubmit('delete')" disabled>
				<input name="add" id="add" type="button" value="&nbsp;&nbsp;<set:label name='ADD' defaultvalue=' Add '/>&nbsp;&nbsp;" onClick="doSubmit('add')">
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="add">
                <input id="mode" name="mode" type="hidden" value="">
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
