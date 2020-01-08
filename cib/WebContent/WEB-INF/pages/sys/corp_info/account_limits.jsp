<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bnk.corp_account">
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
var flag = false;
function pageLoad(){
	var allAccounts = '<set:data name='allAccounts'/>';
	var allAccountStatus = '<set:data name="allAccountLimitStatus"/>';
	if ((allAccounts != '') || (allAccountStatus == '0')) {
		document.form1.allAccounts.checked = true;
		disButton();
		document.form1.allAccounts2.checked = true;
	}
	document.getElementById('allAccounts2').disabled = true;
	/*
	var listsize = 0;
	<set:list name="corpAccList">
		listsize ++;
	</set:list>
	if (listsize == 0) {
		document.getElementById('allAccounts2').disabled = true;
	}
	*/
}
function doSubmit(arg) {
	if (arg == 'view') {
			document.form1.ActionMethod.value = 'viewLimitsDetial';
	} 
	document.getElementById("form1").submit();
	setFormDisabled("form1");
	//setFieldEnabled("buttonReturn");
}
function viewByAccNo(accNo){
	document.form1.account.value = accNo;
	doSubmit('view');
}
function disButton(){
	var form = document.form1;
	for (i=0; i<form.elements.length; i++){
		if (form.elements[i].type == 'button') {
			if (form.elements[i].name == 'viewAll') {
				form.elements[i].disabled = flag;
				flag = !flag;
			} else {
				form.elements[i].disabled = flag;
			}
		}
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpInfo.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.corp_account" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_view_limits" defaultvalue="MDB Corporate Online Banking > Corporatin Info > Account Limits"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_view_limits" defaultvalue="ACCOUNT LIMITS"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/corpInfo.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td colspan="4" class="groupinput" valign="middle">
                  <set:label name="limit_for_account" />
                </td>
              </tr>
              <tr>
                <td width="20%" class="label1">
                    <input name="allAccounts2" type="checkbox" id="allAccounts2" onClick="disButton()" value="checked">
                    <input name="allAccounts" type="checkbox" id="allAccounts" onClick="" value="checked" style="display:none ">                  	
                    <set:label name="all_accounts" defaultvalue="all_accounts"/>
				</td>
                <td width="25%" class="content1">&nbsp;</td>
              	<td width="25%" class="content1"><div align="right"><font color="#FF0000"><set:data name='allAccountLimitStatus' rb='app.cib.resource.sys.pref_status' /></font></div></td>
              	<td width="30%" class="content1">
					<div align="right">
                  			<input name="viewAll" id="viewAll" type="button" value="&nbsp;&nbsp;<set:label name='view' defaultvalue=' view '/>&nbsp;&nbsp;" onClick="viewByAccNo('<set:data name='accountAll'/>');" disabled>
					</div>
				</td>
              </tr>
			  <set:list name='corpAccList' showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="label1"><set:listdata name="accountNo"/></td>
                <td class="content1"><set:listdata name="accountNo" db='accountName'/></td>
              	<td class="content1"><div align="right"><font color="#FF0000"><set:listdata name='accountLimitStatus' rb='app.cib.resource.sys.pref_status'/></font></div></td>
                <td class="content1">
			  <div align="right">
                <input name="view" id= "view" type="button" value="&nbsp;&nbsp;<set:label name='view' defaultvalue=' view '/>&nbsp;&nbsp;" onClick="viewByAccNo('<set:listdata name='accountNo'/>')" >
              </div></td>
              </tr>
			  </set:list>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
					<input id="ActionMethod" name="ActionMethod" type="hidden" value="add">
                  	<input name="account" type="hidden" id="account">
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
