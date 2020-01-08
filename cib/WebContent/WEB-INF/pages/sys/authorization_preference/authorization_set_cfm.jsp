<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.sys.auth_pref">
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
function doSubmit(arg) {
	document.form1.ActionMethod.value = arg;
	setFormDisabled("form1");
	document.getElementById("form1").submit();
}
function doReturn(){
	document.form1.ActionMethod.value = 'setReturn';
	setFormDisabled("form1");
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/authorizationPreference.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.auth_pref" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle" defaultvalue="MDB Corporate Online Banking > Corp Preferences > Set Authorization Preference"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle" defaultvalue="Set Authorization Preference"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/authorizationPreference.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <set:messages width="100%" cols="1" />
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupack"><set:label name="Confirmation" defaultvalue="Confirmation"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="Operation" defaultvalue="Operation"/></td>
                <td width="72%" class="content1"><set:label name='functionTitle'/></td>
              </tr>
            </table>
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="Corp_Info" defaultvalue="Company Information"/></td>
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
                <td colspan="2" class="groupinput"><set:label name="Txn_Type_Currency" defaultvalue="Transaction Type and Currency"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="Txn_Type" defaultvalue="Transaction Type"/></td>
                <td width="72%" class="content1"><set:data name='txnType' rb="app.cib.resource.common.txn_type"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Currency" defaultvalue="Currency Type"/></td>
                <td class="content1"><set:data name='currency' db="rcCurrencyCBS"/></td>
              </tr>             
            </table>
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="8" class="groupinput"><set:label name="Auth_Type" defaultvalue="Authorization Type"/></td>
              </tr>
              <tr>
                <td class="label1" width="28%"><set:label name="Auth_Type" defaultvalue="Authorization Type"/></td>
				<td class="content1" width="72%"><set:data name='ruleType' rb="app.cib.resource.sys.auth_pref"/> &nbsp;&nbsp; - &nbsp;&nbsp;
				<set:if name="ruleType" condition="equals" value="0"><set:data name='singleLevel'/></set:if>
			  <set:if name="ruleType" condition="equals" value="1">
				<set:label name="Multi_Class_Matrix"/>
			  </set:if>
				</td>				
              </tr>
			</table>
			  <set:if name="ruleType" condition="equals" value="1">
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="listheader1"><set:label name="Amount_Range" defaultvalue="Amount Range"/>(<set:data name='currency' db="rcCurrencyCBS"/>)</td>
                <td colspan="6" class="listheader1"><set:label name="Number_Approver" defaultvalue="Number of Approver(s)"/></td>
              </tr>
              <tr>
                <td align="right" class="listheader1"><set:label name="From" defaultvalue="From"/></td>
                <td align="right" class="listheader1"><set:label name="To" defaultvalue="To"/></td>
                <td align="center" class="listheader1"><set:label name="Level_A" defaultvalue="Level A"/></td>
                <td align="center" class="listheader1"><set:label name="Level_B" defaultvalue="Level B"/></td>
                <td align="center" class="listheader1"><set:label name="Level_C" defaultvalue="Level C"/></td>
                <td align="center" class="listheader1"><set:label name="Level_D" defaultvalue="Level D"/></td>
                <td align="center" class="listheader1"><set:label name="Level_E" defaultvalue="Level E"/></td>
              </tr>
			  <set:list name="ruleList" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline' />">
                <td align="right" class="listcontent1"><set:listdata name='fromAmount' format='amount'/></td>
                <td align="right" class="listcontent1"><set:listdata name='toAmount' format='amount'/></td>
                <td align="center" class="listcontent1"><set:listdata name='levelA'/></td>
                <td align="center" class="listcontent1"><set:listdata name='levelB'/></td>
                <td align="center" class="listcontent1"><set:listdata name='levelC'/></td>
                <td align="center" class="listcontent1"><set:listdata name='levelD'/></td>
                <td align="center" class="listcontent1"><set:listdata name='levelE'/></td>
              </tr>
			  </set:list>
            </table>
			  </set:if>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton"><input id="confirm" name="confirm" type="button" value="<set:label name='buttonConfirm' defaultvalue=' Confirm '/>" onClick="doSubmit('setCfm')">
                  <input id="confirm" name="confirm" type="button" value="<set:label name='buttonReturn' defaultvalue=' buttonReturn '/>" onClick="doReturn()">
				  </td>
              </tr>
			 <tr>
  			  	<input id="ActionMethod" name="ActionMethod" type="hidden" value="setCfm">
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
