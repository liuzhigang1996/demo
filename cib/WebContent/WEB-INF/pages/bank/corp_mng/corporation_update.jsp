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

<!-- modify by long_zg 2014-03-17 for IE11 begin-->
<!--<SCRIPT language=JavaScript src="/cib/javascript/jsax.js"></SCRIPT>-->
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<!-- modify by long_zg 2014-03-17 for IE11 end-->

<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	onOptionChange(document.getElementById('corpType'));
}
function doSubmit()
{
	if(validate_new_corp(document.getElementById("form1"))){
		setFormDisabled("form1");
		document.getElementById("form1").submit();
	}
}
function onOptionChange(obj){
	if(obj.value=='2' || obj.value=='3' || obj.value=='4'){
		document.getElementById('allowExecutor').value = 'N';
		document.getElementById('allowExecutor').disabled = true;
		document.getElementById('authenticationMode').disabled = false;				
	} else {
//		document.getElementById('allowExecutor').value = 'Y';
		document.getElementById('allowExecutor').disabled = false;
		document.getElementById('authenticationMode').value = 'C';
		document.getElementById('authenticationMode').disabled = true;		
	}
}
function onOptionChange2(){
	document.getElementById('noties').style.display = 'block';
}
</script>
  <!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corporation.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.corporation" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
          <set:label name="navigationTitle_edit" defaultvalue="MDB Corporate Online Banking > Corporation Management > Edit Corporation"/>
          <!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" -->
        <set:label name="functionTitle_edit" defaultvalue="Edit Corporation"/>
        <!-- InstanceEndEditable --></td>
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
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td><set:messages width="100%" cols="1" align="center"/>
                      <set:fieldcheck name="new_corp" form="form1" file="corp_mng" />
                    </td>
                  </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td colspan="2" class="groupinput"><set:label name="Fill_Info" defaultvalue="Fill the following Information"/></td>
                  </tr>
                  <tr>
                    <td width="28%" class="label1"><set:label name="Corp_Id" defaultvalue="Company Id"/></td>
                    <td width="72%" class="content1"><input class="bankMustInput" id="corpId" name="corpId" type="text" value="<set:data name='corpId'/>" size="12" maxlength="10"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Corp_Name" defaultvalue="Company Name"/></td>
                    <td class="content1"><input class="bankMustInput" id="corpName" name="corpName" type="text" value="<set:data name='corpName'/>" size="40" maxlength="40"></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Full_Name" defaultvalue="Company Full Name"/></td>
                    <td class="content1"><input class="bankMustInput" id="fullName" name="fullName" type="text" value="<set:data name='fullName'/>" size="100" maxlength="100"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="corp_type"/></td>
                    <td class="content1">
					  <select class="bankMustInput" name="corpType" id="corpType" nullable="0" onChange="onOptionChange(this);onOptionChange2();">
						<option value="0">--<set:label name="select_type"/>--</option>
                        <set:rblist file="app.cib.resource.common.corp_type">
                          <set:rboption name="corpType"/>
                        </set:rblist>
                      </select>
                    </td>
                  </tr>
                  
                  <%--add by linrui 20190712 UAT6-933--%>
                <tr>
     			<td colspan="2">  
     			<div id="noties" name="noties"  style="display:none;">
     			<table width="100%">
     			<tr>
                  	<td  style="color:red">
      				<set:label name="NOTIES_MODEL" defaultvalue="NOTIES_MODEL"/>
      				</td>
     			</tr>
     			</table>
     			</div>
     			</td> 
     			</tr>
                  <%--add by linrui 20190712 --%>
                  
                  
                  <tr class="">
                    <td class="label1"><set:label name="Commercial_Registry_No" defaultvalue="Commercial Registry No"/></td>
                    <td class="content1"><input class="bankMustInput" id="commercialRegistryNo" name="commercialRegistryNo" type="text" value="<set:data name='commercialRegistryNo'/>" size="20" maxlength="20"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Country_of_Registry" defaultvalue="Country of Registry"/></td>
                    <td class="content1"><input  id="registryCountry" name="registryCountry" type="text" value="<set:data name='registryCountry'/>" size="40" maxlength="40"></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Address" defaultvalue="Address"/></td>
                    <td class="content1"><input id="address1" name="address1" type="text" value="<set:data name='address1'/>" size="40" maxlength="40">
                      <br>
                      <input id="address2" name="address2" type="text" value="<set:data name='address2'/>" size="40" maxlength="40">
                      <br>
                      <input id="address3" name="address3" type="text" value="<set:data name='address3'/>" size="40" maxlength="40">
                      <br>
                      <input id="address4" name="address4" type="text" value="<set:data name='address4'/>" size="40" maxlength="40">
                    </td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Telephone" defaultvalue="Telephone"/></td>
                    <td class="content1"><input class="bankMustInput" id="telephone" name="telephone" type="text" value="<set:data name='telephone'/>" size="20" maxlength="20"></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Fax_no" defaultvalue="Fax No."/></td>
                    <td class="content1"><input id="faxNo" name="faxNo" type="text" value="<set:data name='faxNo'/>" size="20" maxlength="20"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Email" defaultvalue="E-mail"/></td>
                    <td class="content1"><input id="email" name="email" type="text" value="<set:data name='email'/>" size="60" maxlength="60"></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Foreign_City" defaultvalue="Foreign City"/></td>
                    <td class="content1"><input id="foreignCity" name="foreignCity" type="text" value="<set:data name='foreignCity'/>" size="30" maxlength="30"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Time_Zone" defaultvalue="Time Zone"/></td>
                    <td class="content1"><input id="timeZone" name="timeZone" type="text" value="<set:data name='timeZone'/>" size="3" maxlength="3"></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="AUTHENTICATION_MODE"/></td>
                    <td class="content1"><select class="bankMustInput" id="authenticationMode" name="authenticationMode" nullable="0">
					<option value="0">--<set:label name="select_authentication_mode"/>--</option>
                        <set:rblist file="app.cib.resource.bnk.authentication_mode">
                          <set:rboption name="authenticationMode"/>
                        </set:rblist>
                      </select></td>
                  </tr>

                  <!-- add by long_zg 2019-05-16 for otp login begin -->
                  <tr class="greyline">
                    <td class="label1"><set:label name="OTP_LOGIN"/></td>
                    <td class="content1"><select id="otpLogin" name="otpLogin" nullable="0">
                        <set:rblist file="app.cib.resource.common.yes_no">
                          <set:rboption name="otpLogin"/>
                        </set:rblist>
                        </select>
                    </td>
                  </tr>
                  <!-- add by long_zg 2019-05-16 for otp login end -->
                  
                  <tr class="">
                    <td class="label1"><set:label name="ALLOW_TD" defaultvalue="Allow Time Deposit Operation"/></td>
                    <td class="content1"><select id="allowTd" name="allowTd" nullable="0">
                        <set:rblist file="app.cib.resource.common.yes_no">
                          <set:rboption name="allowTd"/>
                        </set:rblist>
                      </select>
                    </td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="ALLOW_EXECUTOR" defaultvalue="Allow Transaction Executor"/></td>
                    <td class="content1"><select name="allowExecutor" id="allowExecutor" nullable="0">
                        <set:rblist file="app.cib.resource.common.yes_no">
                          <set:rboption name="allowExecutor"/>
                        </set:rblist>
                      </select></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="ALLOW_APPROVER_SELECTION" defaultvalue="Allow Approver Selection"/></td>
                    <td class="content1"><select id="allowApproverSelection" name="allowApproverSelection" nullable="0">
                        <set:rblist file="app.cib.resource.common.yes_no">
                          <set:rboption name="allowApproverSelection"/>
                        </set:rblist>
                      </select></td>
                  </tr><!--
                  <tr class="">
                    <td class="label1"><set:label name="ALLOW_TAX_PAYMENT" defaultvalue="Allow Tax Payment"/></td>
                    <td class="content1"><select id="allowTaxPayment" name="allowTaxPayment" nullable="0">
                        <set:rblist file="app.cib.resource.common.yes_no">
                          <set:rboption name="allowTaxPayment"/>
                        </set:rblist>
                      </select></td>
                  </tr>
                  -->
                  <!-- modified by lzg 20190626 -->
                  <!--<tr class="greyline">
                    <td class="label1"><set:label name="ALLOW_DISPLAY_BOTTOM" defaultvalue="Allow Display Bottom Menu"/></td>
                    <td class="content1"><select id="allowDisplayBottom" name="allowDisplayBottom" nullable="0">
                        <set:rblist file="app.cib.resource.common.yes_no">
                          <set:rboption name="allowDisplayBottom"/>
                        </set:rblist>
                      </select></td>
                  </tr>
                  -->
                  <input name = "allowDisplayBottom" value = "N" type = "hidden"/>
                  <!-- modified by lzg end -->
                  <tr class="greyline">
                    <td class="label1"><set:label name="ALLOW_FINANCIAL_CONTROLLER"/></td>
                    <td class="content1"><select id="allowFinancialController" name="allowFinancialController" nullable="0">
                        <set:rblist file="app.cib.resource.common.yes_no">
                          <set:rboption name="allowFinancialController"/>
                        </set:rblist>
                      </select></td>
                  </tr>
                  <!--<tr class="greyline">
                    <td class="label1"><set:label name="Authorization_Cur" defaultvalue="Authorization Preference Currency"/></td>
                    <td class="content1">
						<input id="authCurMop" name="authCurMop" type="checkbox" value="Y" <set:selected key='authCurMop' equalsvalue='Y'/>>MOP<br>
						<input id="authCurHkd" name="authCurHkd" type="checkbox" value="Y" <set:selected key='authCurHkd' equalsvalue='Y'/>>HKD<br>
						<input id="authCurUsd" name="authCurUsd" type="checkbox" value="Y" <set:selected key='authCurUsd' equalsvalue='Y'/>>USD<br>
						<input id="authCurEur" name="authCurEur" type="checkbox" value="Y" <set:selected key='authCurEur' equalsvalue='Y'/>>EUR
					</td>
                  </tr>
				  --><tr>
                    <td class="label1"><set:label name="merchant_group"/></td>
                    <td class="content1">
					<%/*<select name="merchantGroup" id="merchantGroup" onChange="">
					<option value="">--<set:label name="select_mechant"/>--</option>
					<set:list name='merchantGroupList'>
					<option value="<set:listdata  name='MERCHANT_GROUP' />" <set:listselected key='MERCHANT_GROUP' equals='merchantGroup' output='selected'/>>
					<set:listdata  name='DESCRIPTION' />
					</option>
					</set:list>
					</select>*/%>
					<input id="merchantGroup" name="merchantGroup" type="text" value="<set:data name='merchantGroup'/>" size="10" maxlength="10">
					</td>
                  </tr>
                  <tr >
                    <td colspan="2" class="groupinput"><set:label name="Fill_Info_Representative1" defaultvalue="Fill the following Information for Representative1"/></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Full_Name" defaultvalue="Full Name"/></td>
                    <td class="content1"><input id="rep1FullName" name="rep1FullName" type="text" value="<set:data name='rep1FullName'/>" size="100" maxlength="100"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Short_Name" defaultvalue="Short Name"/></td>
                    <td class="content1"><input id="rep1ShortName" name="rep1ShortName" type="text" value="<set:data name='rep1ShortName'/>" size="20" maxlength="20"></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Title" defaultvalue="Title"/></td>
                    <td class="content1"><input id="rep1Title" name="rep1Title" type="text" value="<set:data name='rep1Title'/>" size="10" maxlength="10"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="ID_Type" defaultvalue="ID_Type"/></td>
                    <td class="content1"><select id="rep1IdType" name="rep1IdType" nullable="0" >
                        <set:rblist file="app.cib.resource.common.id_type">
                          <set:rboption name="rep1IdType"/>
                        </set:rblist>
                      </select>
                    </td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="ID_NO" defaultvalue="ID No."/></td>
                    <td class="content1"><input id="rep1IdNo" name="rep1IdNo" type="text" value="<set:data name='rep1IdNo'/>" size="20" maxlength="20"></td>
                  </tr>
                  <tr  class="greyline">
                    <td class="label1"><set:label name="ID_Issue_date" defaultvalue="ID Issue date"/></td>
                    <td class="content1" colspan="3"><input id="rep1IdIssueDate" name="rep1IdIssueDate" type="text" value="<set:data name='rep1IdIssueDate' format='date'/>" size="10" maxlength="10"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "{scwShow(document.getElementById('rep1IdIssueDate'), this,language)};" > </td>
                  </tr>
                  <tr class="" >
                    <td class="label1"><set:label name="ID_Issuer" defaultvalue="ID Issuer"/></td>
                    <td class="content1"><input id="rep1IdIssuer" name="rep1IdIssuer" type="text" value="<set:data name='rep1IdIssuer'/>" size="40"  maxlength="40"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Telephone" defaultvalue="Telephone"/></td>
                    <td class="content1"><input  id="rep1Telephone" name="rep1Telephone" type="text" value="<set:data name='rep1Telephone'/>" size="20"  maxlength="20"></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Mobile_Telephone" defaultvalue="Mobile Telephone"/></td>
                    <td class="content1"><input id="rep1Mobile" name="rep1Mobile" type="text" value="<set:data name='rep1Mobile'/>" size="20"  maxlength="20"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Email" defaultvalue="Email"/></td>
                    <td class="content1"><input id="rep1Email" name="rep1Email" type="text" value="<set:data name='rep1Email'/>" size="60"></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Fax_no" defaultvalue="Fax No."/></td>
                    <td class="content1"><input id="rep1FaxNo" name="rep1FaxNo" type="text" value="<set:data name='rep1FaxNo'/>" size="60"></td>
                  </tr>
                  <td colspan="2" class="groupinput"><set:label name="Fill_Info_Representative2" defaultvalue="Fill the following Information for Representative2"/></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Full_Name" defaultvalue="Full Name"/></td>
                    <td class="content1"><input id="rep2FullName" name="rep2FullName" type="text" value="<set:data name='rep2FullName'/>" size="100" maxlength="100"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Short_Name" defaultvalue="Short Name"/></td>
                    <td class="content1"><input id="rep2ShortName" name="rep2ShortName" type="text" value="<set:data name='rep2ShortName'/>" size="20" maxlength="20"></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Title" defaultvalue="Title"/></td>
                    <td class="content1"><input id="rep2Title" name="rep2Title" type="text" value="<set:data name='rep2Title'/>" size="10" maxlength="10"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="ID_Type" defaultvalue="ID_Type"/></td>
                    <td class="content1"><select id="rep2IdType" name="rep2IdType" nullable="0" >
                        <set:rblist file="app.cib.resource.common.id_type">
                          <set:rboption name="rep2IdType"/>
                        </set:rblist>
                      </select>
                    </td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="ID_NO" defaultvalue="ID No."/></td>
                    <td class="content1"><input id="rep2IdNo" name="rep2IdNo" type="text" value="<set:data name='rep2IdNo'/>" size="20" maxlength="20"></td>
                  </tr>
                  <tr  class="greyline">
                    <td class="label1"><set:label name="ID_Issue_date" defaultvalue="ID Issue date"/></td>
                    <td class="content1" colspan="3"><input id="rep2IdIssueDate" name="rep2IdIssueDate" type="text" value="<set:data name='rep2IdIssueDate' format='date'/>" size="10" maxlength="10"><img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "{scwShow(document.getElementById('rep2IdIssueDate'), this,language)};" > </td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="ID_Issuer" defaultvalue="ID Issuer"/></td>
                    <td class="content1"><input id="rep2IdIssuer" name="rep2IdIssuer" type="text" value="<set:data name='rep2IdIssuer'/>" size="40"  maxlength="40"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Telephone" defaultvalue="Telephone"/></td>
                    <td class="content1"><input id="rep2Telephone" name="rep2Telephone" type="text" value="<set:data name='rep2Telephone'/>" size="20"  maxlength="20"></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Mobile_Telephone" defaultvalue="Mobile Telephone"/></td>
                    <td class="content1"><input id="rep2Mobile" name="rep2Mobile" type="text" value="<set:data name='rep2Mobile'/>" size="20"  maxlength="20"></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Email" defaultvalue="Email"/></td>
                    <td class="content1"><input id="rep2Email" name="rep2Email" type="text" value="<set:data name='rep2Email'/>" size="60"></td>
                  </tr>
                  <tr class="">
                    <td class="label1"><set:label name="Fax_no" defaultvalue="Fax No."/></td>
                    <td class="content1"><input id="rep2FaxNo" name="rep2FaxNo" type="text" value="<set:data name='rep2FaxNo'/>" size="60"></td>
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
                      <!--                  <input id="bottonReset" name="bottonReset" type="reset" value="<set:label name='buttonReset' defaultvalue='Reset'/>">
 -->
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="update">
                      <input id="parentCorp" name="parentCorp" type="hidden" value="<set:data name='parentCorp'/>">					  
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
