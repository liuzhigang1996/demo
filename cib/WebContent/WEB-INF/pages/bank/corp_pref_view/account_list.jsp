<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
function pageLoad(){
}
function doSubmit(arg) {
	if (arg == 'add') {
		if (document.getElementById('offlineAcc') == null) {
			alert('No Any Accounts Not Online');
		} else {
			if(validate_acc_add(document.getElementById("form1"))){
				document.form1.ActionMethod.value = 'addAccount';
				document.getElementById("form1").submit();
				setFormDisabled("form1");
				//setFieldEnabled("cancel");
			}
		}
	} else if (arg == 'remove') {
		if (document.getElementById('onlineAcc') == null) {
			alert('No Any Accounts Online');
		} else {
			if(validate_acc_remove(document.getElementById("form1"))){
				document.form1.ActionMethod.value = 'removeAccount';
				document.getElementById("form1").submit();
				setFormDisabled("form1");
				//setFieldEnabled("cancel");
			}
		}
	} else if (arg == 'return') {
		window.location = '/cib/corporationForSupervisor.do?ActionMethod=prefEntry&prefType=account';
		setFormDisabled("form1");
		//setFieldEnabled("cancel");
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpAccountForSupervisor.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.corp_account" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_list" defaultvalue="MDB Corporate Online Banking > Corp Account > Account List"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_list" defaultvalue="ACCOUNT LIST MAINTENANCE"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/corpAccountForSupervisor.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> <set:fieldcheck name="acc_remove" form="form1" file="corp_account" /> <set:fieldcheck name="acc_add" form="form1" file="corp_account" /> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="2" class="groupinput"><set:label name="Corp_Info"/></td>
                </tr>
                <tr>
                  <td width="28%" class="label1"><set:label name="corp_Id" defaultvalue="corp_Id"/></td>
                  <td width="72%" class="content1"><set:data name='corpId'/>
                    <input name="corpId" type="hidden" id="corpId" value="<set:data name='corpId' />"></td>
                </tr>
                <tr class="greyline">
                  <td class="label1"><set:label name="corp_name" defaultvalue="corp_name"/></td>
                  <td class="content1"><set:data name='corpName'/></td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="4" class="groupinput"><set:label name="acc_in_bank_online" defaultvalue="acc_in_bank_online"/></td>
                </tr>
                <tr>
                  <td class="listheader1" align="center"><set:label name="account_no"/>&nbsp;</td>
                  <td class="listheader1" colspan="2" align="center"><set:label name="type"/>&nbsp;</td>
                  <td class="listheader1" align="center"><set:label name="currency"/>&nbsp;</td>
                </tr>
                <set:list name="onlineAccList" showNoRecord='YES'>
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1" align="left"><set:listdata name="ACCOUNTNO"/>&nbsp;</td>
                  <td class="listcontent1" align="left" colspan="2"><set:listdata name="ACCOUNTTYPE" rb = "app.cib.resource.common.account_type_desc"/>&nbsp;</td>
                  <%--<td class="listcontent1" align="left"><set:listdata name="accountName"/>&nbsp;</td>
                  <td class="listcontent1" align="left"><set:listdata name="accountDesc"/>&nbsp;</td>--%>
                  <td class="listcontent1" align="center"><set:listdata name="CURRENCY"/>&nbsp;</td>
                </tr>
                </set:list>
                <tr>
                  <td colspan="4"><div align="center"><span class="sectionbutton"> </span></div></td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td height="40" colspan="2" class="sectionbutton">                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="add">
                  </td></tr>
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
