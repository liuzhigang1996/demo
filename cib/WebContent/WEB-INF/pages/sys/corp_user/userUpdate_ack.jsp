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
function pageLoad(){
}
function doSubmit()
{
	setFormDisabled("form1");
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpUser.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.corp_user" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_edit" defaultvalue="MDB Corporate Online Banking > Corp User Management > Edit Corp User"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_edit" defaultvalue="Edit Corp User"/><!-- InstanceEndEditable --></td>
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
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="2" class="groupack"><set:label name="Acknowledgement" defaultvalue="Acknowledgement"/></td>
                </tr>
                <tr >
                  <td class="label1" ><set:label name="Operation" defaultvalue="Operation"/></td>
                  <td class="label1" ><set:label name="Modify_User" defaultvalue="Modify User"/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1" ><set:label name="Status" defaultvalue="Status"/></td>
                  <td class="label1" ><set:label name='ackStatusAccepted' /></td>
                </tr>
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
                  <td colspan="2" class="groupinput">><set:label name="Corp_User_Info"/></td>
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
                  <td class="label1"><set:label name="Email" defaultvalue="Email"/></td>
                  <td class="content1"><set:data name='email'/></td>
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
                <tr >
                  <td class="label1"><set:label name="Cert_Info1" defaultvalue="User Certificate"/></td>
                  <td class="content1"><set:data name='certInfo1'/></td>
                </tr>
                <tr  class="greyline">
                  <td class="label1"><set:label name="Authoriztion_Level" defaultvalue="Authoriztion Class"/></td>
                  <td class="content1"><set:data name="authLevel" rb="app.cib.resource.common.auth_level" /></td>
                </tr>
                <tr>
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
				<set:if name="showOperator" condition="EQUALS" value="Y" >
                <tr>
                  <td colspan="2" class="groupinput"><set:label name="Transaction_Limits" defaultvalue="User's Transfer Limits"/></td>
                </tr>
                <tr >
                  <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="Own_Accounts" defaultvalue="Online Own Accounts"/>--><set:label name="TO_MY_ACCOUNT" defaultvalue="To My Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                  <td class="content1"><set:data name='limit1' format='amount'/></td>
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
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input id="submit1" name="submit1" type="button" value="<set:label name='buttonOK' defaultvalue=' OK ' />" tabindex="3"  onClick="doSubmit();">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="list" >
                    <input id="corpId" name="corpId" type="hidden" value="<set:data name='corpId' />" ></td>
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
