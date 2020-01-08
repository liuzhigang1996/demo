<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal1.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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

<!-- modify by long_zg 2014-03-17 for IE11 begin-->
<!--<SCRIPT language=JavaScript src="/cib/javascript/jsax.js"></SCRIPT>-->
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<!-- modify by long_zg 2014-03-17 for IE11 end-->

<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	changRoleId(document.form1.roleId);
}
function doCancel()
{
	setFormDisabled("form1");
	document.getElementById("ActionMethod").value="list";
	document.getElementById("form1").submit();
}
function doSubmit()
{
	if(validate_corpUser(document.getElementById("form1"))){
		setFormDisabled("form1");
		document.getElementById("form1").submit();
	}
}
function changRoleId(obj){
	
	if('<set:data name="corpType"/>' == '1'){
		if(obj.options[obj.selectedIndex].value=='1'){
			document.getElementById("operator").style.display = "";
			document.getElementById("approver").style.display = "none";
			document.getElementById("certInfo1").value = "";
			setFieldDisabled("certInfo1");
			document.getElementById("authLevel")[0].selected = true;//add by linrui 20190617
			setFieldDisabled("authLevel");
			setDivDisabled("approver");
			setDivEnabled("operator");
			document.getElementById('viewAllTrans').style.display='block';
			document.getElementById('financialController').style.display='none';
		} else if(obj.options[obj.selectedIndex].value=='2') {
			document.getElementById("operator").style.display = "none";
			document.getElementById("approver").style.display = "";
			setFieldEnabled("certInfo1");
			setFieldEnabled("authLevel");
			setDivDisabled("operator");
			setDivEnabled("approver");
			document.getElementById('viewAllTrans').style.display='block';
			document.getElementById('financialController').style.display='block';

		} else if(obj.options[obj.selectedIndex].value=='3'){
			document.getElementById("operator").style.display = "none";
			document.getElementById("approver").style.display = "none";
			document.getElementById("certInfo1").value = "";
			setFieldDisabled("certInfo1");
			document.getElementById("authLevel")[0].selected = true;//add by linrui 20190617
			setFieldDisabled("authLevel");
			setDivDisabled("operator");
			setDivDisabled("approver");
			document.getElementById('viewAllTrans').style.display='none';
			document.getElementById('financialController').style.display='none';
		} else if(obj.options[obj.selectedIndex].value=='4'){
			document.getElementById("operator").style.display = "none";
			document.getElementById("approver").style.display = "none";
			document.getElementById("certInfo1").value = "";
			setFieldDisabled("certInfo1");
			document.getElementById("authLevel")[0].selected = true;//add by linrui 20190617
			setFieldDisabled("authLevel");
			setDivDisabled("operator");
			setDivDisabled("approver");
			document.getElementById('viewAllTrans').style.display='none';
			document.getElementById('financialController').style.display='none';
		} else {
			document.getElementById("operator").style.display = "none";
			document.getElementById("approver").style.display = "none";
			setFieldDisabled("authLevel");
			setDivDisabled("operator");
			setDivDisabled("approver");
			document.getElementById('viewAllTrans').style.display='none';
			document.getElementById('financialController').style.display='none';
		}
	} else if('<set:data name="corpType"/>' == '2'){
		if(obj.options[obj.selectedIndex].value=='2') {
			document.getElementById("operator").style.display = "";
			document.getElementById("approver").style.display = "";
			setFieldEnabled("certInfo1");
			setFieldEnabled("authLevel");
			setDivEnabled("operator");
			setDivEnabled("approver");
			document.getElementById('viewAllTrans').style.display='block';
			document.getElementById('financialController').style.display='block';
		} else if(obj.options[obj.selectedIndex].value=='4'){
			document.getElementById("operator").style.display = "none";
			document.getElementById("approver").style.display = "none";
			document.getElementById("certInfo1").value = "";
			setFieldDisabled("certInfo1");
			document.getElementById("authLevel")[0].selected = true;//add by linrui 20190617
			setFieldDisabled("authLevel");
			setDivDisabled("operator");
			setDivDisabled("approver");
			document.getElementById('viewAllTrans').style.display='none';
			document.getElementById('financialController').style.display='none';
		} else {
			document.getElementById("operator").style.display = "none";
			document.getElementById("approver").style.display = "none";
			document.getElementById("authLevel")[0].selected = true;//add by linrui 20190617
			setFieldDisabled("authLevel");
			setDivDisabled("operator");
			setDivDisabled("approver");
			document.getElementById('viewAllTrans').style.display='none';
			document.getElementById('financialController').style.display='none';
		}
	} else if('<set:data name="corpType"/>' == '3'){
		document.getElementById("operator").style.display = "";
		document.getElementById("approver").style.display = "";
		setFieldEnabled("certInfo1");
		setFieldEnabled("authLevel");
		setDivEnabled("operator");
		setDivEnabled("approver");
		document.getElementById('viewAllTrans').style.display='block';
		document.getElementById('financialController').style.display='block';
		<!-- modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB begin -->
		//document.getElementById("country_area").style.display = "";
		//document.getElementById("mobile_hits").innerHTML = "<set:label name="mobile_hits" />";
		<!-- modify by long_zg 2014-04-03 for CR204 Apply OTP to BOB end -->
	} else if('<set:data name="corpType"/>' == '4'){
		if(obj.options[obj.selectedIndex].value=='1'){
			document.getElementById("operator").style.display = "";
			document.getElementById("approver").style.display = "none";
			document.getElementById("certInfo1").value = "";
			setFieldDisabled("certInfo1");
			document.getElementById("authLevel")[0].selected = true;//add by linrui 20190617
			setFieldDisabled("authLevel");
			setDivDisabled("approver");
			setDivEnabled("operator");
			document.getElementById('viewAllTrans').style.display='block';
			document.getElementById('financialController').style.display='none';
		} else if(obj.options[obj.selectedIndex].value=='2') {
			document.getElementById("operator").style.display = "none";
			document.getElementById("approver").style.display = "";
			setFieldEnabled("certInfo1");
			setFieldEnabled("authLevel");
			setDivDisabled("operator");
			setDivEnabled("approver");
			document.getElementById('viewAllTrans').style.display='block';
			document.getElementById('financialController').style.display='block';
		} else {
			document.getElementById("operator").style.display = "none";
			document.getElementById("approver").style.display = "none";
			document.getElementById("authLevel")[0].selected = true;//add by linrui 20190617
			setFieldDisabled("authLevel");
			setDivDisabled("operator");
			setDivDisabled("approver");
			document.getElementById('viewAllTrans').style.display='none';
			document.getElementById('financialController').style.display='none';
		}
	}
}
function setDivDisabled(divName){
	var input = document.getElementById(divName).getElementsByTagName("input");
	for (var i=0; i<input.length; i++) {
		input[i].disabled=true;
	}
}
function setDivEnabled(divName){
	var input = document.getElementById(divName).getElementsByTagName("input");
	for (var i=0; i<input.length; i++) {
		input[i].disabled=false;
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpUser.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.corp_user" --><!-- InstanceParam name="form_onreset" type="text" value="changRoleId(document.form1.roleId);" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_edit" defaultvalue="MDB Corporate Online Banking > Corp User Management > Edit Corp User"/><!-- InstanceEndEditable --></td>
    <td class="buttonprint" style="background-image:url(images/button-print_<%=session.getAttribute("Locale$Of$Neturbo")%>.gif)"><a href="#"><img src="/cib/images/shim.gif" width="61" height="18" border="0"></a></td>
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
		  <form action="/cib/corpUser.do" method="post" name="form1" id="form1" onReset="changRoleId(document.form1.roleId);">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			  <set:messages width="100%" cols="1" />
			  <set:if name="corpType" value="3" condition="equals">
			  	<set:fieldcheck name="" form="form1" file="corp_user" />
			  </set:if>
			  <set:if name="corpType" value="3" condition="notequals">
			  	<set:fieldcheck name="" form="form1" file="corp_user_opt2&3" />
			  </set:if>
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
                  <td colspan="2" class="groupinput"><set:label name="Fill_Info"/></td>
                </tr>
                <tr>
                  <td width="28%" class="label1"><set:label name="User_Id" defaultvalue="User Id"/></td>
                  <td  width="72%" class="content1"><input id="userId" name="userId" type="hidden" value="<set:data name='userId'/>">
                    <set:data name='userId'/></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Role" defaultvalue="Role"/></td>
                  <td class="content1">
				    <select id="roleId" name="roleId" nullable="0" onChange="changRoleId(this);" >
					  <set:if name="allowExcutor" value="Y" condition="equals">
                      	<set:rblist file="app.cib.resource.common.corp_role"> <set:rboption name="roleId"/> </set:rblist>
					    </set:if>
					    <set:if name="allowExcutor" value="N" condition="equals">
                      	<set:rblist file="app.cib.resource.common.corp_role_opt5"> <set:rboption name="roleId"/> </set:rblist>
					    </set:if>
					  <set:if name="corpType" value="2" condition="equals">
                      	<set:rblist file="app.cib.resource.common.corp_role_opt2"> <set:rboption name="roleId"/> </set:rblist>
					  </set:if>
					  <set:if name="corpType" value="3" condition="equals">
                      	<set:rblist file="app.cib.resource.common.corp_role_opt3"> <set:rboption name="roleId"/> </set:rblist>
					  </set:if>
					  <set:if name="corpType" value="4" condition="equals">
                      	<set:rblist file="app.cib.resource.common.corp_role_opt4"> <set:rboption name="roleId"/> </set:rblist>
					  </set:if>
                    </select>
                  </td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="User_Name" defaultvalue="User Name"/></td>
                  <td class="content1"><input class="bankMustInput" id="userName" name="userName" type="text" value="<set:data name='userName'/>" size="20"></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Gender" defaultvalue="Gender"/></td>
                  <td class="content1"><select class="bankMustInput" id="gender" name="gender" nullable="0" >
                      <set:rblist file="app.cib.resource.common.gender"> <set:rboption name="gender"/> </set:rblist>
                    </select>
                  </td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="Title" defaultvalue="Title"/></td>
                  <td class="content1"><input id="title" name="title" type="text" value="<set:data name='title'/>" size="10" maxlength="10"></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Full_Name" defaultvalue="Full Name"/></td>
                  <td class="content1"><input class="bankMustInput" id="fullName" name="fullName" type="text" value="<set:data name='fullName'/>" size="100" maxlength="100"></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="ID_Type" defaultvalue="ID_Type"/></td>
                  <td class="content1"><select class="bankMustInput" id="idType" name="idType" nullable="0" >
                      <set:rblist file="app.cib.resource.common.id_type"> <set:rboption name="idType"/> </set:rblist>
                    </select>
                  </td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="ID_NO" defaultvalue="ID NO."/></td>
                  <td class="content1"><input class="bankMustInput" id="idNo" name="idNo" type="text" value="<set:data name='idNo'/>" size="20"  maxlength="20"></td>
                </tr>
                <tr  >
                  <td class="label1"><set:label name="ID_Issue_date" defaultvalue="ID Issue date"/></td>
                  <td class="content1" colspan="3"><input id="idIssueDate" name="idIssueDate" type="text" value="<set:data name='idIssueDate' format="date"/>" size="10" maxlength="10"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "{scwShow(document.getElementById('idIssueDate'), this,language)};" > </td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="ID_Issuer" defaultvalue="ID Issuer"/></td>
                  <td class="content1"><input id="idIssuer" name="idIssuer" type="text" value="<set:data name='idIssuer'/>" size="40"  maxlength="40"></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="Telephone" defaultvalue="Telephone"/></td>
                  <td class="content1"><input id="telephone" name="telephone" type="text" value="<set:data name='telephone'/>" size="20"  maxlength="20"></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Mobile_Telephone" defaultvalue="Mobile Telephone"/></td>
                  <td class="content1"><input class="bankMustInput" id="mobile" name="mobile" type="text" value="<set:data name='mobile'/>" size="20"  maxlength="20"><!-- <span id="mobile_hits">&nbsp;&nbsp;</span> --></td>
                </tr>
                <tr>
                  <td class="label1"><set:label name="Email" defaultvalue="Email"/></td>
                  <td class="content1"><input class="bankMustInput" id="email" name="email" type="text" value="<set:data name='email'/>" size="60"></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="Fax_no" defaultvalue="Fax No."/></td>
                  <td class="content1"><inputid="faxNo" name="faxNo" type="text" value="<set:data name='faxNo'/>" size="60"></td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="CertInfo1" defaultvalue="User Certificate "/></td>
                  <td class="content1"><textarea id="certInfo1" name="certInfo1" cols="60" rows="3"><set:data name='certInfo1'/></textarea></td>
                </tr>
                <tr  class="greyline">
                  <td class="label1"><set:label name="Authoriztion_Level" defaultvalue="Authoriztion Class"/></td>
                  <td class="content1"><select class="bankMustInput" id="authLevel" name="authLevel" nullable="0">
                      <option value=""><set:label name="Select_level" defaultvalue="-- Select a level --" /></option>
                      <set:rblist file="app.cib.resource.common.auth_level"> <set:rboption name="authLevel"/> </set:rblist>
                    </select>
                  </td>
                </tr>
                <tr class="">
                  <td class="label1"><set:label name="User_Group" defaultvalue="User Group"/></td>
                  <td class="content1"><select id="groupId" name="groupId" nullable="0" disabled="disabled">
                      <option value="0">Default</option>
                    </select>
                  </td>
                </tr>
                <tr class="greyline" id="viewAllTrans">
                  <td class="label1"><set:label name="view_all_trans"/></td>
                  <td class="content1"><select id="viewAllTransFlag" name="viewAllTransFlag" nullable="0">
                      <set:rblist file="app.cib.resource.sys.view_all_trans_flag"> <set:rboption name="viewAllTransFlag"/> </set:rblist>
                    </select>
                  </td>
                </tr>
                <tr class=""  id="financialController">
				<set:if name="allowFinancialController" value="Y" condition="equals">
                  <td class="label1"><set:label name="financial_controller"/></td>
                  <td class="content1"><select id="financialControllerFlag" name="financialControllerFlag" nullable="0">
                      <set:rblist file="app.cib.resource.sys.view_all_trans_flag"> <set:rboption name="financialControllerFlag"/> </set:rblist>
                    </select>
                  </td>
				</set:if>
                </tr>
				<tr class="">
                  <td class="label1"><set:label name="merchantType"/></td>
                  <td class="content1">
				  <table id="mt">
				   <tr>
				   <td><set:label name="Cage"/></td>
				   <td><select id="cage" name="cage">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="cage"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="FB"/></td>
				   <td><select id="fb" name="fb">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="fb"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="Ticketing"/> </td>
				   <td><select id="ticketing" name="ticketing">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="ticketing"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="Hotel"/></td>
				   <td><select id="hotel" name="hotel">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="hotel"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="Retail"/></td>
				   <td><select id="retail" name="retail">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="retail"/> </set:rblist>
                   </select></td>
				   </tr>
				   <tr>
				   <td><set:label name="General"/></td>
				   <td><select id="general" name="general">
                      <set:rblist file="app.cib.resource.sys.corp_user_merchantType"> <set:rboption name="general"/> </set:rblist>
                   </select></td>
				   </tr>
				   </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="2" ><div title="operator"  id="operator" >
                      <table width="100%" border="0" cellspacing="0" cellpadding="3">
                        <tr>
                          <td colspan="2" class="groupinput"><set:label name="Transaction_Limits" defaultvalue="User's Transaction Limits"/></td>
                        </tr>
                        <tr >
                          <td class="label1" width="28%"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="Own_Accounts" defaultvalue="Online Own Accounts"/>--><set:label name="TO_MY_ACCOUNT" defaultvalue="To My Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                          <td class="content1" width="72%"><input class="bankMustInput" id="limit1" name="limit1" type="text" value="<set:data name='limit1' format='amount'/>" size="20"></td>
                        </tr>
                        <tr class="greyline" >
                          <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="Accounts_BANK" defaultvalue="Accounts in BANK"/>--><set:label name="TO_MDB_BANK_ACCOUNT" defaultvalue="To MDB Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                          <td class="content1"><input class="bankMustInput" id="limit2" name="limit2" type="text" value="<set:data name='limit2' format='amount'/>" size="20"></td>
                        </tr>
                        <tr >
                          <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="Accounts_Macau" defaultvalue="Accounts in Other Banks in Macau"/>--><set:label name="TO_LOCAL_BANK_ACCOUNT" defaultvalue="To Local Bank Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                          <td class="content1"><input class="bankMustInput" id="limit3" name="limit3" type="text" value="<set:data name='limit3' format='amount'/>" size="20"></td>
                        </tr>
                        <tr class="greyline" >
                          <td class="label1"><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 begin --><!--<set:label name="Accounts_Overseas" defaultvalue="Accounts in Banks Overseas"/>--><set:label name="TO_OVERSEAS_BANK_ACCOUNT" defaultvalue="To Overseas Bank Account"/><!-- Modify by long_zg 2019-05-22 UAT6-245 COB：四種轉賬的名稱需要更改 end --></td>
                          <td class="content1"><input class="bankMustInput" id="limit4" name="limit4" type="text" value="<set:data name='limit4' format='amount' />" size="20"></td>
                        </tr>
                        <!--<tr >
                          <td class="label1"><set:label name="Corp_Fund_Alloc" defaultvalue="Corporation Fund Allocation"/></td>
                          <td class="content1"><input id="limit5" name="limit5" type="text" value="<set:data name='limit5' format='amount'/>" size="20"></td>
                        </tr>
                        --><!--<tr class="greyline">
                          <td class="label1"><set:label name="Bill_Payment" defaultvalue="Bill Payment"/></td>
                          <td class="content1"><input id="limit6" name="limit6" type="text" value="<set:data name='limit6' format='amount'/>" size="20"></td>
                        </tr>
                        --><tr >
                          <td class="label1"><set:label name="Time_Deposit" defaultvalue="Time Deposit"/></td>
                          <td class="content1"><input class="bankMustInput" id="limit7" name="limit7" type="text" value="<set:data name='limit7' format='amount'/>" size="20"></td>
                        </tr>
                        <!--<tr class="greyline">
                          <td class="label1" width="28%"><set:label name="Payroll" defaultvalue="Payroll"/></td>
                          <td class="content1" width="72%"><input id="limit8" name="limit8" type="text" value="<set:data name='limit8' format='amount'/>" size="20"></td>
                        </tr>
                        <tr class="">
                          <td class="label1" width="28%"><set:label name="Bank_Draft" defaultvalue="Bank Draft"/></td>
                          <td class="content1" width="72%"><input id="limit9" name="limit9" type="text" value="<set:data name='limit9' format='amount'/>" size="20"></td>
                        </tr>
                        <tr class="greyline">
                          <td class="label1" width="28%"><set:label name="Cashier_Order" defaultvalue="Cashier Order"/></td>
                          <td class="content1" width="72%"><input id="limit10" name="limit10" type="text" value="<set:data name='limit10' format='amount'/>" size="20"></td>
                        </tr>
                      --></table>
                    </div>
                    <div title="approver"  id="approver" style="display:none"> </div></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton"><input id="buttonOk" name="buttonOk" type="button" value="<set:label name='buttonOK' defaultvalue=' OK '/>" onClick="doSubmit();">
                    <input id="buttonCancel" name="buttonCancel" type="button" value="<set:label name='buttonCancel' defaultvalue='Cancel'/>" onClick="doCancel()">
                    <!--                  <input id="bottonReset" name="bottonReset" type="reset" value="<set:label name='buttonReset' defaultvalue='Reset'/>" onClick="document.form1.reset();"> -->
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="update">
                    <input id="authenticationMode" name="authenticationMode" type="hidden" value="<set:data name='authenticationMode'/>">
                    <!--  Add by long_zg 2019-05-16 for otp login begin  --> 
                    <input id="otpLogin" name="otpLogin" type="hidden" value="<set:data name='otpLogin'/>">
                  	<!--  Add by long_zg 2019-05-16 for otp login begin  -->
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
