<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.sys.group_man">
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
function doSubmit(action){
	if(action=='confirm'){
		setFormDisabled("form1");
		document.form1.submit();
	}
}
function doCancel()
{
	setFormDisabled("form1");
	document.getElementById("ActionMethod").value="addGroupCancel";
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/group.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.group_man" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_add" defaultvalue="BANK Online Banking >User Management > New User Group"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_add" defaultvalue="NEW USER GROUP"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/group.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<set:messages width="100%" cols="1" align="center"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupack"><set:label name="Confirmation" defaultvalue="Confirmation"/></td>
              </tr>
              <tr>
                <td width="20%" class="label1"><set:label name="Operation" defaultvalue="Operation"/></td>
                <td width="80%" class="content1"><set:label name='functionTitle_add'/></td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="Group_Information"/></td>
              </tr>
              <tr>
                <td width="20%" class="label1"><set:label name="User_Group_Name" defaultvalue="User Group Name"/></td>
                <td width="80%" class="content1"><set:data name='groupName'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Role" defaultvalue="Role"/></td>
                <td class="content1"><set:data name='roleId' rb="app.cib.resource.common.corp_role"/></td>
              </tr>
              <tr>
                <td class="label1"><set:label name="Account_List" defaultvalue="Account List"/></td>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
             		 <set:list name="accountList">
             		 <tr>
                		<td width="30" class="content1">
						<input id="item_accountList" type=checkbox  disabled="disabled" name="item_accountList" value="<set:listdata name='accountNo'/>" <set:listselected  key="accountNo" equals='item_accountList'  output='checked'/>></td>
               	 		<td class="content1"><set:listdata name='accountNo'/></td>
                		<td class="content1"><set:listdata name='accountName'/> </td>
						<td class="content1"><set:listdata name='accountType' rb="app.cib.resource.common.account_type_desc"/></td>
              		</tr>
             		 <tr>
               	 		<td colspan="4" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:list>
					</table>				</td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Function_List" defaultvalue="Function List"/></td>
				<td>
					<set:if name="roleId" condition="EQUALS" value="1" >
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<set:rblist file="app.cib.resource.sys.operator_function">
             		 <tr>
                		<td class="content1" width="30"><input id="item_functionList" type=checkbox disabled="disabled" name="item_functionList" value="<set:rbkey/>" <set:rbselected  equals='item_functionList'  output='checked'/>></td>
               	 		<td class="content1"><set:rbvalue/></td>
                		<td class="content1">&nbsp;</td>
              		</tr>
             		 <tr>
               	 		<td colspan="3" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:rblist>
					</table>
				</set:if>
				<set:if name="roleId" condition="EQUALS" value="2" >
				<set:if name="corpType" condition="EQUALS" value="1" >
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<set:rblist file="app.cib.resource.sys.approver_function">
             		 <tr>
                		<td class="content1" width="30"><input id="item_functionList" type=checkbox  disabled="disabled" name="item_functionList" value="<set:rbkey/>" <set:rbselected  equals='item_functionList'  output='checked'/>></td>
               	 		<td class="content1"><set:rbvalue/></td>
                		<td class="content1">&nbsp;</td>
              		</tr>
             		 <tr>
               	 		<td colspan="3" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:rblist>
					</table>
				</set:if>
				<set:if name="corpType" condition="EQUALS" value="2" >
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<set:rblist file="app.cib.resource.sys.operator_approver_function">
             		 <tr>
                		<td class="content1" width="30"><input id="item_functionList" type=checkbox  disabled="disabled" name="item_functionList" value="<set:rbkey/>" <set:rbselected  equals='item_functionList'  output='checked'/>></td>
               	 		<td class="content1"><set:rbvalue/></td>
                		<td class="content1">&nbsp;</td>
              		</tr>
             		 <tr>
               	 		<td colspan="3" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:rblist>
					</table>
				</set:if>
				<set:if name="corpType" condition="EQUALS" value="3" >
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<set:rblist file="app.cib.resource.sys.operator_approver_function">
             		 <tr>
                		<td class="content1" width="30"><input id="item_functionList" type=checkbox  disabled="disabled" name="item_functionList" value="<set:rbkey/>" <set:rbselected  equals='item_functionList'  output='checked'/>></td>
               	 		<td class="content1"><set:rbvalue/></td>
                		<td class="content1">&nbsp;</td>
              		</tr>
             		 <tr>
               	 		<td colspan="3" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:rblist>
					</table>
				</set:if>
				<set:if name="corpType" condition="EQUALS" value="4" >
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<set:rblist file="app.cib.resource.sys.approver_function">
             		 <tr>
                		<td class="content1" width="30"><input id="item_functionList" type=checkbox  disabled="disabled" name="item_functionList" value="<set:rbkey/>" <set:rbselected  equals='item_functionList'  output='checked'/>></td>
               	 		<td class="content1"><set:rbvalue/></td>
                		<td class="content1">&nbsp;</td>
              		</tr>
             		 <tr>
               	 		<td colspan="3" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:rblist>
					</table>
				</set:if>
				</set:if>
				</td>
              </tr>
              <tr>
                <td class="label1"><set:label name="Description" defaultvalue="Description"/></td>
                <td class="content1"><set:data name='groupDesc'/></td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton"><input id="buttonConfirm" name="buttonConfirm" type="button" value="<set:label name='buttonConfirm' defaultvalue=' Confirm '/>" onClick="doSubmit('confirm')">
				<input id="buttonCancel" name="buttonCancel" type="button" value="<set:label name='buttonCancel' defaultvalue='Cancel'/>" onClick="doCancel()">
                  <input id="ActionMethod" name="ActionMethod" type="hidden" value="addGroupConfirm">				  </td>
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
