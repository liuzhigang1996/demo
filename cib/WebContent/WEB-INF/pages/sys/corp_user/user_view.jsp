<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.sys.corp_user">
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
var id_edit_load = null;
var id_block_load = null;
var id_unblock_load = null;
var id_delete_load = null;
var id_resetpwd_load = null;
var id_resetcode_load = null;
function pageLoad(){
	id_edit_load = document.getElementById('id_edit');
	id_block_load = document.getElementById('id_block');
	id_unblock_load = document.getElementById('id_unblock');
	id_delete_load = document.getElementById('id_delete');
	id_resetpwd_load = document.getElementById('id_resetpwd');
	id_resetcode_load = document.getElementById('id_resetcode');
	checkStatus('<set:data name='status'/>');
//	if("0" != "<set:data name='status' />" && "2" != "<set:data name='status' />"){
//		setFormDisabled("form1");
//		setFieldEnabled("buttonReturn");
//	}
}
function checkStatus(status) {
	if (status == '0') { //normal
		id_edit_load.disabled = false;
		id_block_load.disabled = false;
		id_unblock_load.disabled = true;
		id_delete_load.disabled = false;
		id_resetpwd_load.disabled = false;
		if('<set:data name='authenticationMode'/>' == 'S' && '<set:data name='roleId'/>' == '2'){
			id_resetcode_load.disabled = false;
		}		
	} else if (status == '2') { //block
		id_edit_load.disabled = true;
		id_block_load.disabled = true;
		id_unblock_load.disabled = false;
		id_delete_load.disabled = false;
		id_resetpwd_load.disabled = true;
		if('<set:data name='authenticationMode'/>' == 'S' && '<set:data name='roleId'/>' == '2'){
			id_resetcode_load.disabled = true;
		}		
	} else { //remove && pending
		id_edit_load.disabled = true;
		id_block_load.disabled = true;
		id_unblock_load.disabled = true;
		id_delete_load.disabled = true;
		id_resetpwd_load.disabled = true;
		if('<set:data name='authenticationMode'/>' == 'S' && '<set:data name='roleId'/>' == '2'){
			id_resetcode_load.disabled = true;
		}		
	}
}

function doSubmit(arg){
	if (arg == 'block') {
		document.form1.ActionMethod.value = 'block';
	} else if (arg == 'edit') {
		document.form1.ActionMethod.value = 'updateLoad';
	} else if (arg == 'delete') {
		document.form1.ActionMethod.value = 'delete';
	} else if (arg == 'return') {
		document.form1.ActionMethod.value = 'list';
	} else if (arg == 'unblock') {
	    document.form1.ActionMethod.value = 'unblock';
	} else if (arg == 'resetpwd') {
	    document.form1.ActionMethod.value = 'resetPassword';
	} else if (arg == 'resetcode') {
	    document.form1.ActionMethod.value = 'resetSecurityCode';
	}
	setFormDisabled("form1");
	document.form1.submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpUser.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.corp_user" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --> <set:label name="navigationTitle_view" defaultvalue="MDB Corporate Online Banking >User Management > New User"/> <!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --> <set:label name="functionTitle_view" defaultvalue="VIEW USER INFORMATION"/> <!-- InstanceEndEditable --></td>
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
		  <form action="/cib/corpUser.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" --> <set:messages width="100%" cols="1"/>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="2" class="groupinput"><set:label name="Corp_Info" defaultvalue="Corporation Information"/></td>
                </tr>
                <tr>
                  <td width="28%" class="label1"><set:label name="Corp_Id" defaultvalue="Company Id"/></td>
                  <td width="72%" class="content1"><set:data name='corpId'/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Corp_Name" defaultvalue="Company Name"/></td>
                  <td class="content1"><set:data name='corpName'/></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="2" class="groupinput"><set:label name="Corp_User_Info"/></td>
                </tr>
                <tr>
                  <td width="28%" class="label1"><set:label name="User_Id" defaultvalue="User Id"/></td>
                  <td width="72%" class="content1"><set:data name='userId'/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Role" defaultvalue="Role"/></td>
                  <td class="content1"><set:data name="roleId" rb="app.cib.resource.common.corp_role" /></td>
                </tr>
                <tr >
                  <td class="label1"><set:label name="User_Name" defaultvalue="User Name"/></td>
                  <td class="content1"><set:data name='userName'/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Gender" defaultvalue="Gender"/></td>
                  <td class="content1"><set:data name='gender' rb="app.cib.resource.common.gender"/></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="Title" defaultvalue="Title"/></td>
                  <td class="content1"><set:data name='title' /></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Full_Name" defaultvalue="Full Name"/></td>
                  <td class="content1"><set:data name='fullName'/></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="ID_Type" defaultvalue="ID_Type"/></td>
                  <td class="content1"><set:data name='idType' rb="app.cib.resource.common.id_type" /></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="ID_NO" defaultvalue="ID NO."/></td>
                  <td class="content1"><set:data name='idNo' format = "IDNO"/></td>
                </tr>
                <tr  class="">
                  <td class="label1"><set:label name="ID_Issue_date" defaultvalue="ID Issue date"/></td>
                  <td class="content1"><set:data name='idIssueDate' format="date"/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="ID_Issuer" defaultvalue="ID Issuer"/></td>
                  <td class="content1"><set:data name='idIssuer' /></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="Telephone" defaultvalue="Telephone"/></td>
                  <td class="content1"><set:data name='telephone' /></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Mobile_Telephone" defaultvalue="Mobile Telephone"/></td>
                  <td class="content1">
                  <!-- modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB begin -->
                  <set:data name='mobileCountryCode' /> <set:data name='mobileAreaCode' />
                  <!-- modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB end --> 
                  <set:data name='mobile' /></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="Email" defaultvalue="Email"/></td>
                  <td class="content1"><set:data name='email'/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Fax_no" defaultvalue="Fax No."/></td>
                  <td class="content1"><set:data name='faxNo'/></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="Cert_Info1" defaultvalue="User Certificate"/></td>
                  <td class="content1"><set:data name='certInfo1'/></td>
                </tr>
                <tr  class="greyline">
                  <td class="label1"><set:label name="Authoriztion_Level" defaultvalue="Authoriztion Class"/></td>
                  <td class="content1"><set:data name="authLevel" rb="app.cib.resource.common.auth_level" /></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="User_Group" defaultvalue="User Group"/></td>
                  <td class="content1"><set:data name="groupId" db="groupByCorp"/></td>
                </tr>
                <set:if name="roleId" value="4" condition="notequals">
                <set:if name="roleId" value="3" condition="notequals">
                <tr class="greyline">
                  <td class="label1"><set:label name="view_all_trans"/></td>
                  <td class="content1"><set:data name="viewAllTransFlag" rb="app.cib.resource.sys.view_all_trans_flag"/></td>
                </tr>
                </set:if>
                </set:if>
				<set:if name="allowFinancialController" value="Y" condition="equals">
                <set:if name="roleId" value="2" condition="equals">
                <tr class="">
                  <td class="label1"><set:label name="financial_controller"/></td>
                  <td class="content1"><set:data name="financialControllerFlag" rb="app.cib.resource.sys.view_all_trans_flag"/></td>
                </tr>
                </set:if> 
                </set:if> 
				<tr class="">
                  <td class="label1"><set:label name="merchantType"/></td>
                  <td class="content1">
				  <table id="mt">
				   <tr>
				   <td><set:label name="Cage"/></td>
				   <td><select id="cage" name="cage" disabled="disabled">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="cage"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="FB"/></td>
				   <td><select id="fb" name="fb" disabled="disabled">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="fb"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="Ticketing"/> </td>
				   <td><select id="ticketing" name="ticketing" disabled="disabled">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="ticketing"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="Hotel"/></td>
				   <td><select id="hotel" name="hotel" disabled="disabled">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="hotel"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="Retail"/></td>
				   <td><select id="retail" name="retail" disabled="disabled">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="retail"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="General"/></td>
				   <td><select id="general" name="general" disabled="disabled">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="general"/> </set:rblist>
                   </select></td>
				   </tr>
				   </table>
				  </td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Status" defaultvalue="Status"/></td>
                  <td class="content1"><set:data name="status" rb="app.cib.resource.common.status" /></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="Last_Modify" defaultvalue="Last Modify Time"/></td>
                  <td class="content1"><set:data name="lastUpdateTime" format="datetime"/></td>
                </tr>
                <set:if name="showOperator" condition="EQUALS" value="Y" >
                <tr class="">
                  <td colspan="2" class="groupinput"><set:label name="Transaction_Limits" defaultvalue="User's Transfer Limits"/></td>
                </tr>
                <tr >
                  <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="Own_Accounts" defaultvalue="Online Own Accounts"/>--><set:label name="TO_MY_ACCOUNT" defaultvalue="To My Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                  <td class="content1"><set:data name='limit1' format="amount"/></td>
                </tr>
                <tr class="greyline" >
                  <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="Accounts_BANK" defaultvalue="Accounts in BANK"/>--><set:label name="TO_MDB_BANK_ACCOUNT" defaultvalue="To MDB Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                  <td class="content1"><set:data name="limit2" format="amount"/></td>
                </tr>
                <tr >
                  <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="Accounts_Macau" defaultvalue="Accounts in Other Banks in Macau"/>--><set:label name="TO_LOCAL_BANK_ACCOUNT" defaultvalue="To Local Bank Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                  <td class="content1"><set:data name="limit3" format="amount"/></td>
                </tr>
                <tr class="greyline" >
                  <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="Accounts_Overseas" defaultvalue="Accounts in Banks Overseas"/>--><set:label name="TO_OVERSEAS_BANK_ACCOUNT" defaultvalue="To Overseas Bank Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                  <td class="content1"><set:data name="limit4" format="amount"/></td>
                </tr>
                <!--<tr >
                  <td class="label1"><set:label name="Corp_Fund_Alloc" defaultvalue="Corporation Fund Allocation"/></td>
                  <td class="content1"><set:data name="limit5" format="amount"/></td>
                </tr>
                --><!--<tr class="greyline">
                  <td class="label1"><set:label name="Bill_Payment" defaultvalue="Bill Payment"/></td>
                  <td class="content1"><set:data name="limit6" format="amount"/></td>
                </tr>
                --><tr>
                  <td class="label1"><set:label name="Time_Deposit" defaultvalue="Time Deposit"/></td>
                  <td class="content1"><set:data name="limit7" format="amount"/></td>
                </tr>
                <!--<tr class="greyline">
                  <td class="label1" width="28%"><set:label name="Payroll" defaultvalue="Payroll"/></td>
                  <td class="content1" width="72%"><set:data name='limit8' format="amount"/></td>
                </tr>
                <tr class="">
                  <td class="label1" width="28%"><set:label name="Bank_Draft" defaultvalue="Bank Draft"/></td>
                  <td class="content1" width="72%"><set:data name='limit9' format="amount"/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1" width="28%"><set:label name="Cashier_Order" defaultvalue="Cashier Order"/></td>
                  <td class="content1" width="72%"><set:data name='limit10' format="amount"/></td>
                </tr>
                --></set:if> <set:if name="showApprover" condition="EQUALS" value="Y" > </set:if>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td colspan="2" class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton">
				  <input id="id_edit" name="buttonEdit" type="button" value="<set:label name='buttonEdit' defaultvalue='Edit'/>" disabled onClick="doSubmit('edit')">
                    <input id="id_block" name="buttonBlock" type="button" value="<set:label name='buttonBlock' defaultvalue='Block'/>" disabled onClick="doSubmit('block');">
                    <input id="id_unblock" name="buttonUnblock" type="button" value="<set:label name='buttonUnblock' defaultvalue='Unblock'/>" disabled onClick="doSubmit('unblock');">
                    <input id="id_delete" name="buttonDelete" type="button" value="<set:label name='buttonDelete' defaultvalue='Delete'/>" disabled onClick="doSubmit('delete');">
                    <input id="id_resetpwd" name="buttonResetPwd" type="button" value="<set:label name='buttonResetPwd' defaultvalue='Reset Password'/>" disabled onClick="doSubmit('resetpwd');">
                    <set:if name="corpType" value="3" condition="notequals">
					<input id="id_resetcode" name="buttonResetCode" type="button" value="<set:label name='buttonResetCode'/>" disabled onClick="doSubmit('resetcode');">
                    </set:if>
					<input id="buttonReturn" name="buttonReturn" type="button" value="<set:label name='buttonReturn' defaultvalue=' Return '/>" onClick="doSubmit('return')">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="updateLoad">
                    <input id="userId" name="userId" type="hidden" value="<set:data name='userId'/>">
                    <input id="corpId" name="corpId" type="hidden" value="<set:data name='corpId'/>"></td>
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
