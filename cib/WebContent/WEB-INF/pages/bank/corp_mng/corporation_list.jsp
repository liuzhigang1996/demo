<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit(arg) {
	if(validate_pref_entry(document.getElementById("form1"))){
	if (arg == 'account') {
		document.getElementById("form1").action = '/cib/corpAccount.do';
		document.getElementById("ActionMethod").value = 'listCorpAccount';
	} else if (arg == 'user') {
		document.getElementById("form1").action = '/cib/corpUser.do';
		document.getElementById("ActionMethod").value = 'list';
	} else if (arg == 'limit') {
		document.getElementById("form1").action = '/cib/corpAccountLimits.do';
		document.getElementById("ActionMethod").value = 'listCorpAccount';
	} else if (arg == 'auth') {
		document.getElementById("form1").action = '/cib/authorizationPreference.do';
		document.getElementById("ActionMethod").value = 'query';
	} else if (arg == 'subsidiary') {
		document.getElementById("form1").action = '/cib/corpSubsidiary.do';
	    document.getElementById("ActionMethod").value = 'listSubsidiary';
	}
	setFormDisabled("form1");
	document.getElementById("form1").submit();
	}
}
function doEnquiry()
{
	setFormDisabled("form1");
	document.getElementById("form1").submit();
}
//added by xyf 20090901
function doViewDetail(varId){
	var tempId = escape(varId);
	//location.replace('/cib/corporation.do?ActionMethod=view&corpId=' + tempId);
	window.document.location.href="/cib/corporation.do?ActionMethod=view&corpId=" + tempId;
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corporation.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.corporation" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_list" defaultvalue="MDB Corporate Online Banking > Corporation Management > Corporation List"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_list" defaultvalue="Corporation List"/><!-- InstanceEndEditable --></td>
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
						<set:fieldcheck name="perf_entry" form="form1" file="corp_mng" /></td>
				</tr>
			</table>
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="label1"><set:label name="Corp_Id" defaultvalue="Company Id"/>&nbsp;<input id="queryCorpId" name="queryCorpId" type="text" value="<set:data name='queryCorpId'/>" size="20" maxlength="20">&nbsp;&nbsp;&nbsp;&nbsp;<set:label name="Corp_Name" defaultvalue="Company Name"/>&nbsp;<input id="queryCorpName" name="queryCorpName" type="text" value="<set:data name='queryCorpName'/>" size="20" maxlength="20">&nbsp;&nbsp;&nbsp;&nbsp;<input id="buttonEnquiry" name="buttonEnquiry" type="button" value="<set:label name='buttonEnquiry' defaultvalue=' Enquiry '/>" onClick="doEnquiry()"></td>
              </tr>
			  </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="listheader1">&nbsp;</td>
                <td class="listheader1"><set:label name="Corp_Id" defaultvalue="Corporations ID"/></td>
                <td class="listheader1"><set:label name="Corp_Name" defaultvalue="Corporations Name"/></td>
                <td class="listheader1"><set:label name="corp_type" defaultvalue="Corporations Type"/></td>
                <td class="listheader1"><set:label name="Status" defaultvalue="Status"/></td>
                <td align="center" class="listheader1"><set:label name="View_Detail" defaultvalue="View Detail"/></td>
              </tr>
			  <set:list name="corpList" showPageRows="10" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1"><input id="corpId" type="radio" name="corpId" value="<set:listdata name='corpId' />"  <set:listselected key="corpId" equals='corpId' output='checked'/> <set:listselected key='status' equalsvalue='1' output='disabled'/> ></td>
                <td class="listcontent1"><set:listdata name="corpId"/></td>
                <td class="listcontent1"><set:listdata name="corpName"/></td>
                <td class="listcontent1"><set:listdata name="corpType" rb="app.cib.resource.common.corp_type"/></td>
                <td class="listcontent1"><set:listdata name="status" rb="app.cib.resource.common.status"/></td>
                <td align="center" class="listcontent1"><a href="#" onClick="doViewDetail('<set:listdata name='corpId'/>');">
                  <set:label name="View_Detail" defaultvalue="View Detail"/></a></td>
              </tr>
			  </set:list>
            </table>
			<!--
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
			-->
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
			 <tr>
			   <td rowspan="6" class="title1"><b>&nbsp;<set:label name="Corp_Wizard" defaultvalue="Corporation Management Wizard"/></b></td>
			   <td>&nbsp;</td>
			   <td>&nbsp;</td>
			   </tr>
			 <tr>
                <td><set:label name="User_Management" defaultvalue="1 Corporation User Management"/> </td>
                <td height="25"><input id="buttonOk1" name="buttonOk1" type="button" value="<set:label name='buttonGo' defaultvalue=' >> Go '/>" onClick="doSubmit('user')">
                <input id="ActionMethod" name="ActionMethod" type="hidden" value="list"></td>
			 </tr>
			 <tr>
			   <td><set:label name="Acc_Management" defaultvalue="2 Account Managment"/></td>
			   <td height="25"><input id="buttonOk2" name="buttonOk2" type="button" value="<set:label name='buttonGo' defaultvalue=' >> Go '/>" onClick="doSubmit('account')"></td>
			   </tr>
			 <tr>
			   <td><set:label name="Limit_Management" defaultvalue="3 Setup Transaction Limits"/></td>
			   <td height="25"><input id="buttonOk3" name="buttonOk3" type="button" value="<set:label name='buttonGo' defaultvalue=' >> Go '/>" onClick="doSubmit('limit')"></td>
			   </tr>
			 <tr>
			   <td><set:label name="Auth_Management" defaultvalue="4 Setup Authorization Preferences"/></td>
			   <td height="25"><input id="buttonOk4" name="buttonOk4" type="button" value="<set:label name='buttonGo' defaultvalue=' >> Go '/>" onClick="doSubmit('auth')"></td>
			   </tr>
			 <tr>
			   <td><set:label name="Sub_Management" defaultvalue="5 Set Subsidiaries"/></td>
			   <td height="25"><input id="buttonOk5" name="buttonOk5" type="button" value="<set:label name='buttonGo' defaultvalue=' >> Go '/>" onClick="doSubmit('subsidiary')"></td>
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
