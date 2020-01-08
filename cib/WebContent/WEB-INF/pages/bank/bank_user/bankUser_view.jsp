<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bnk.bank_user">
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
	if("0" != "<set:data name='status' />" ){
		setFormDisabled("form1");
		setFieldEnabled("buttonReturn");
	}
	if("2" == "<set:data name='status' />"){
		setFieldEnabled("buttonUnblock");
		setFieldEnabled("buttonDelete");
	}
}
function doSubmit(arg) {
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
	} else if (arg == 'reset'){
		document.form1.ActionMethod.value = 'resetPassword';
	}
	setFormDisabled("form1");
	document.form1.submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/bankUser.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.bank_user" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_view" defaultvalue="MDB Corporate Online Banking > Bank User Management > View Bank User"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_view" defaultvalue="View Bank User"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/bankUser.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<set:messages width="100%" cols="1" />
           <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="User_Info" defaultvalue="User Information"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="User_Id" defaultvalue="User Id"/></td>
                <td width="72%" class="content1"><set:data name='userId'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="User_Name" defaultvalue="User Name"/></td>
                <td class="content1"><set:data name='userName'/></td>
              </tr>
              <tr>
                <td class="label1"><set:label name="Email" defaultvalue="Email"/></td>
                <td class="content1"><set:data name='email'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Role" defaultvalue="Role"/></td>
                <td class="content1"><set:data name="roleId" rb="app.cib.resource.common.bank_role1" /></td>
              </tr>
              <tr>
                <td class="label1"><set:label name="Status" defaultvalue="Role"/></td>
                <td class="content1"><set:data name="status" rb="app.cib.resource.common.status"/></td>
              </tr>
			<tr class="greyline">
                    <td class="label1"><set:label name="Last_Modify" defaultvalue="Last Modify Time"/></td>
                    <td class="content1"><set:data name="lastUpdateTime" format="datetime"/></td>
            </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
				<set:if name="fromPage" condition="equals" value="1">
				<set:if name="roleId" condition="notequals" value="9">
				<set:if name="status" condition="equals" value="0">
				<input id="buttonBlock" name="buttonBlock" type="button" value="<set:label name='buttonBlock' defaultvalue=' Block '/>" onClick="doSubmit('block')">
				</set:if>
				<set:if name="status" condition="equals" value="2">
				<input id="buttonUnblock" name="buttonUnblock" type="button" value="<set:label name='buttonUnblock' defaultvalue=' Unblock '/>" onClick="doSubmit('unblock')">
				</set:if>
				<input id="buttonEdit" name="buttonEdit" type="button" value="<set:label name='buttonEdit' defaultvalue=' Edit '/>" onClick="doSubmit('edit')">
				<input id="buttonResetPwd" name="buttonResetPwd" type="button" value="<set:label name='buttonResetPwd' defaultvalue='Reset Password'/>" onClick="doSubmit('reset')">
				<input id="buttonDelete" name="buttonDelete" type="button" value="<set:label name='buttonDelete' defaultvalue=' Delete '/>" onClick="doSubmit('delete')">
				</set:if>
				<input id="buttonReturn" name="buttonReturn" type="button" value="<set:label name='buttonReturn' defaultvalue='Return '/>" onClick="doSubmit('return')">
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="add">
				<input id="userId" name="userId" type="hidden" value="<set:data name='userId'/>">
				</set:if>
				<set:if name="fromPage" condition="equals" value="2">
					<input id="buttonReturn" name="buttonReturn" type="button" value="<set:label name='buttonReturn' defaultvalue='Return '/>" onClick="history.go(-1);">
				</set:if>
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
