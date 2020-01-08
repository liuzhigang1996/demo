<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bnk.corporation">
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
function pageLoad(){
}
function doSubmit()
{
	setFormDisabled("form1");
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corporation.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.corporation" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_unblock" defaultvalue="MDB Corporate Online Banking > Corporation Management > Edit Corporation"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_unblock" defaultvalue="Edit Corporation"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/corporation.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
               <td colspan="2" class="groupack"><set:label name="Acknowledgement" defaultvalue="Acknowledgement"/></td>
              </tr>
             <tr class="greyline">
               <td class="label1"><set:label name="Operation" defaultvalue="Operation"/></td>
               <td class="content1"><set:label name='functionTitle_unblock'/></td>
             </tr>
             <tr>
               <td width="28%" class="label1"><set:label name="Ack_Status" defaultvalue="Status"/></td>
               <td width="72%" class="content1"><set:label name='ackStatusAccepted' defaultvalue="Accepted"/></td>
             </tr>
           </table>
           <table width="100%" border="0" cellspacing="0" cellpadding="3">
               <tr>
                <td colspan="2" class="groupinput"><set:label name="Corp_Info" defaultvalue="Corporation Information"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="Corp_Id" defaultvalue="Company Id"/></td>
                <td width="72%" class="content1"><set:data name="corpId" /></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Corp_Name" defaultvalue="Company Name"/></td>
                <td class="content1"><set:data name="corpName" /></td>
              </tr>
              <tr class="">
                <td class="label1"><set:label name="Full_Name" defaultvalue="Full Name"/></td>
                <td class="content1"><set:data name="fullName" /></td>
              </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="corp_type"/></td>
                    <td class="content1"><set:data name="corpType" rb="app.cib.resource.common.corp_type"/></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Commercial_Registry_No" defaultvalue="Commercial Registry No"/></td>
                    <td class="content1"><set:data name="commercialRegistryNo" /></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Country_of_Registry" defaultvalue="Country of Registry"/></td>
                    <td class="content1"><set:data name="registryCountry" /></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Address" defaultvalue="Address"/></td>
                    <td class="content1"><set:data name="address1" />
                      <br>
                      <set:data name="address2" />
                      <br>
                      <set:data name="address3" />
                      <br>
                      <set:data name="address4" /></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Telephone" defaultvalue="Telephone"/></td>
                    <td class="content1"><set:data name="telephone" /></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Fax_no" defaultvalue="Fax_no"/></td>
                    <td class="content1"><set:data name="faxNo" /></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Email" defaultvalue="E-mail"/></td>
                    <td class="content1"><set:data name="email" /></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Foreign_City" defaultvalue="Foreign City"/></td>
                    <td class="content1"><set:data name="foreignCity" /></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Time_Zone" defaultvalue="Time Zone"/></td>
                    <td class="content1"><set:data name="timeZone" /></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="AUTHENTICATION_MODE" /></td>
                    <td class="content1"><set:data name="authenticationMode" rb="app.cib.resource.bnk.authentication_mode" /></td>
                  </tr>
                  
                  <!-- add by long_zg 2019-05-16 for otp login begin -->
                  <tr class="greyline">
                    <td class="label1"><set:label name="OTP_LOGIN" /></td>
                    <td class="content1"><set:data name="otpLogin" rb="app.cib.resource.common.yes_no" /></td>
                  </tr>
                  <!-- add by long_zg 2019-05-16 for otp login end -->
                  
                  <tr class="">
                    <td class="label1"><set:label name="ALLOW_TD" defaultvalue="Allow Transaction Executer"/></td>
                    <td class="content1"><set:data name="allowTd" rb="app.cib.resource.common.yes_no" />                    </td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="ALLOW_EXECUTOR" defaultvalue="Allow Time Deposit Operation"/></td>
                    <td class="content1"><set:data name="allowExecutor" rb="app.cib.resource.common.yes_no" /></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="ALLOW_APPROVER_SELECTION" defaultvalue="Allow Approver Selection"/></td>
                    <td class="content1"><set:data name="allowApproverSelection" rb="app.cib.resource.common.yes_no" /></td>
                  </tr>
                  <!--<tr class="">
                    <td class="label1"><set:label name="ALLOW_TAX_PAYMENT" defaultvalue="Allow Tax Payment"/></td>
                    <td class="content1"><set:data name="allowTaxPayment" rb="app.cib.resource.common.yes_no" /></td>
                  </tr>
                  --><!--<tr class="greyline">
                    <td class="label1"><set:label name="ALLOW_DISPLAY_BOTTOM" defaultvalue="Allow Display Bottom Menu"/></td>
                    <td class="content1"><set:data name="allowDisplayBottom" rb="app.cib.resource.common.yes_no" /></td>
                  </tr>
                  --><tr class="greyline">
                    <td class="label1"><set:label name="ALLOW_FINANCIAL_CONTROLLER" /></td>
                    <td class="content1"><set:data name="allowFinancialController" rb="app.cib.resource.common.yes_no" /></td>
                  </tr>
                  <!--<tr class="greyline">
                    <td class="label1"><set:label name="Authorization_Cur" defaultvalue="Authorization Preference Currency"/></td>
                    <td class="content1">
						<input id="authCurMop" name="authCurMop" type="checkbox" value="Y" disabled="disabled" <set:selected key="authCurMop" equalsvalue="Y" output="checked"/>>MOP<br>
						<input id="authCurHkd" name="authCurHkd" type="checkbox" value="Y" disabled="disabled" <set:selected key="authCurHkd" equalsvalue="Y" output="checked"/>>HKD<br>
						<input id="authCurUsd" name="authCurUsd" type="checkbox" value="Y" disabled="disabled" <set:selected key="authCurUsd" equalsvalue="Y" output="checked"/>>USD<br>
						<input id="authCurEur" name="authCurEur" type="checkbox" value="Y" disabled="disabled" <set:selected key="authCurEur" equalsvalue="Y" output="checked"/>>EUR
					</td>
                  </tr>
				  --><tr>
                    <td class="label1"><set:label name="merchant_group" /></td>
                    <td class="content1"><set:data name="merchantGroup"/></td>
                  </tr>
			  <tr > 
                <td colspan="2" class="groupinput"><set:label name="Representative1_Information" defaultvalue="Representative1 Information"/></td>
              </tr>
			  <tr class="">
                <td class="label1"><set:label name="Full_Name" defaultvalue="Full Name"/></td>
                <td class="content1"><set:data name="rep1FullName" /></td>
              </tr>
			  <tr class="greyline">
                <td class="label1"><set:label name="Short_Name" defaultvalue="Short Name"/></td>
                <td class="content1"><set:data name="rep1ShortName" /></td>
              </tr>
			  <tr class="">
                <td class="label1"><set:label name="Title" defaultvalue="Title"/></td>
                <td class="content1"><set:data name="rep1Title"   /></td>
              </tr>
			   <tr class="greyline">
                <td class="label1"><set:label name="ID_Type" defaultvalue="ID Type"/></td>
                <td class="content1"><set:data name="rep1IdType"   rb="app.cib.resource.common.id_type"/></td>
              </tr>
			   <tr class="">
                <td class="label1"><set:label name="ID_NO" defaultvalue="ID No."/></td>
                <td class="content1"><set:data name="rep1IdNo" /></td>
              </tr>
			   <tr class="greyline">
                <td class="label1"><set:label name="ID_Issue_date" defaultvalue="ID Issue date"/></td>
                <td class="content1"><set:data name="rep1IdIssueDate" format="date"/></td>
              </tr>
			   <tr class="">
                <td class="label1"><set:label name="ID_Issuer" defaultvalue="ID Issuer"/></td>
                <td class="content1"><set:data name="rep1IdIssuer" /></td>
              </tr>
			   <tr class="greyline">
                <td class="label1"><set:label name="Telephone" defaultvalue="Telephone"/></td>
                <td class="content1"><set:data name="rep1Telephone" /></td>
              </tr>
			  <tr class="">
                <td class="label1"><set:label name="Mobile_Telephone" defaultvalue="Mobile Telephone"/></td>
                <td class="content1"><set:data name="rep1Mobile" /></td>
              </tr>
			   <tr class="greyline">
                <td class="label1"><set:label name="Email" defaultvalue="Email"/></td>
                <td class="content1"><set:data name="rep1Email" /></td>
              </tr>
			  <tr class="">
                <td class="label1"><set:label name="Fax_no" defaultvalue="Fax No."/></td>
                <td class="content1"><set:data name="rep1FaxNo" /></td>
              </tr>
			  <tr > 
                <td colspan="2" class="groupinput"><set:label name="Representative2_Information" defaultvalue="Representative2 Information"/></td>
              </tr>
			  <tr class="">
                <td class="label1"><set:label name="Full_Name" defaultvalue="Full Name"/></td>
                <td class="content1"><set:data name="rep2FullName" /></td>
              </tr>
			  <tr class="greyline">
                <td class="label1"><set:label name="Short_Name" defaultvalue="Short Name"/></td>
                <td class="content1"><set:data name="rep2ShortName" /></td>
              </tr>
			  <tr class="">
                <td class="label1"><set:label name="Title" defaultvalue="Title"/></td>
                <td class="content1"><set:data name="rep2Title"   /></td>
              </tr>
			   <tr class="greyline">
                <td class="label1"><set:label name="ID_Type" defaultvalue="ID Type"/></td>
                <td class="content1"><set:data name="rep2IdType"   rb="app.cib.resource.common.id_type"/></td>
              </tr>
			   <tr class="">
                <td class="label1"><set:label name="ID_NO" defaultvalue="ID No."/></td>
                <td class="content1"><set:data name="rep2IdNo" /></td>
              </tr>
			   <tr class="greyline">
                <td class="label1"><set:label name="ID_Issue_date" defaultvalue="ID Issue date"/></td>
                <td class="content1"><set:data name="rep2IdIssueDate" format="date"/></td>
              </tr>
			   <tr class="">
                <td class="label1"><set:label name="ID_Issuer" defaultvalue="ID Issuer"/></td>
                <td class="content1"><set:data name="rep2IdIssuer" /></td>
              </tr>
			   <tr class="greyline">
                <td class="label1"><set:label name="Telephone" defaultvalue="Telephone"/></td>
                <td class="content1"><set:data name="rep2Telephone" /></td>
              </tr>
			  <tr class="">
                <td class="label1"><set:label name="Mobile_Telephone" defaultvalue="Mobile Telephone"/></td>
                <td class="content1"><set:data name="rep2Mobile" /></td>
              </tr>
			   <tr class="greyline">
                <td class="label1"><set:label name="Email" defaultvalue="Email"/></td>
                <td class="content1"><set:data name="rep2Email" /></td>
              </tr>
			  <tr class="">
                <td class="label1"><set:label name="Fax_no" defaultvalue="Fax No."/></td>
                <td class="content1"><set:data name="rep2FaxNo" /></td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton"><input id="buttonOk" name="buttonOk" type="button" value="<set:label name='buttonOK' defaultvalue=' OK '/>" onClick="doSubmit()">
				  <input id="ActionMethod" name="ActionMethod" type="hidden" value="list">
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
