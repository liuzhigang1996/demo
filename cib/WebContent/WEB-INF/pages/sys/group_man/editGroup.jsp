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
function showFunctionList(objValue){
	//alert(obj.options[obj.selectedIndex].value);
	if(objValue=='1'){
		document.getElementById("operator_function").style.display = "";
		document.getElementById("approver_function").style.display = "none";
		clearAllSelection("approver_function");
	}else if(objValue=='2'){
		document.getElementById("operator_function").style.display = "none";
		document.getElementById("approver_function").style.display = "";
		clearAllSelection("operator_function");
	}else{
		document.getElementById("operator_function").style.display = "none";
		document.getElementById("approver_function").style.display = "none";
		clearAllSelection("operator_function");
		clearAllSelection("approver_function");
	}
}
function clearAllSelection(divName){
	var checkboxs = document.getElementById(divName).getElementsByTagName("input");
	for (var i=0; i<checkboxs.length; i++) {
		checkboxs[i].checked=false;
	}
}
function pageLoad(){
	showFunctionList(document.form1.roleId.value);
}
function doSubmit(action){
	if(action=='ok'){
		setFormDisabled("form1");
		document.form1.submit();
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/group.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.group_man" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_edit" defaultvalue="BANK Online Banking >User Management > Edit User Group"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_edit" defaultvalue="EDIT USER GROUP"/><!-- InstanceEndEditable --></td>
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
            <set:fieldcheck name="group_man" form="form1" file="group_man" />
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="Fill_Info" defaultvalue="Fill the following Information"/></td>
              </tr>
              <tr>
                <td width="20%" class="label1"><set:label name="User_Group_Name" defaultvalue="User Group Name"/></td>
                <td width="70%" class="content1"><input id="groupName" type="text" name="groupName" value="<set:data name='groupName'/>"></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Role" defaultvalue="Role"/></td>
                <td class="content1"><set:data name='roleId' rb="app.cib.resource.common.corp_role"/>
								  <input id="roleId" name="roleId" type="hidden" value="<set:data name='roleId'/>">
<!--
				<set:if name="corpType" condition="EQUALS" value="1" >
				  <select name="roleId" id="roleId" nullable="0" onChange="showFunctionList(this.value);">
					<set:rblist file="app.cib.resource.common.corp_role1">
                    	<set:rboption name="roleId"/>
					</set:rblist>
                  </select>
				</set:if>
				<set:if name="corpType" condition="EQUALS" value="2" >
				  <select name="roleId" id="roleId" nullable="0">
					<set:rblist file="app.cib.resource.common.corp_role2">
                    	<set:rboption name="roleId"/>
					</set:rblist>
                  </select>
				</set:if>
				<set:if name="corpType" condition="EQUALS" value="3" >
				  <select name="roleId" id="roleId" nullable="0">
					<set:rblist file="app.cib.resource.common.corp_role2">
                    	<set:rboption name="roleId"/>
					</set:rblist>
                  </select>
				</set:if>
				<set:if name="corpType" condition="EQUALS" value="4" >
				  <select name="roleId" id="roleId" nullable="0" onChange="showFunctionList(this.value);">
					<set:rblist file="app.cib.resource.common.corp_role1">
                    	<set:rboption name="roleId"/>
					</set:rblist>
                  </select>
				</set:if>
-->
			  	</td>
              </tr>
              <tr>
                <td class="label1"><set:label name="Account_List" defaultvalue="Account List"/></td>
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<set:list name="accountList">
             		 <tr>
                		<td width="30" class="content1"><set:listcheckbox name='item_accountList' value="accountNo" text=""/></td>
               	 		<td class="content1"><set:listdata name='accountNo'/></td>
                		<td class="content1"><set:listdata name='accountName'/> </td>
						<td class="content1"><set:listdata name='accountType' rb="app.cib.resource.common.account_type_desc"/></td>
              		</tr>
					<tr>
               	 		<td colspan="4" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:list>
					</table>
				</td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="Function_List" defaultvalue="Function List"/></td>
				<td>
				<set:if name="corpType" condition="EQUALS" value="1" >
				<div title="operator_function"  id="operator_function">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<set:rblist file="app.cib.resource.sys.operator_function">
             		 <tr>
                		<td class="content1" width="30"><input id="item_functionList" type=checkbox name="item_functionList" value="<set:rbkey/>" <set:rbselected  equals='item_functionList'  output='checked'/>></td>
               	 		<td class="content1"><set:rbvalue/></td>
                		<td class="content1">&nbsp;</td>
              		</tr>
					<tr>
               	 		<td colspan="4" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:rblist>
					</table>
				</div>
				<div title="approver_function"  id="approver_function" style="display:none">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<set:rblist file="app.cib.resource.sys.approver_function">
             		 <tr>
                		<td class="content1" width="30"><input id="item_functionList" type=checkbox name="item_functionList" value="<set:rbkey/>" <set:rbselected  equals='item_functionList'  output='checked'/>></td>
               	 		<td class="content1"><set:rbvalue/></td>
                		<td class="content1">&nbsp;</td>
              		</tr>
					<tr>
               	 		<td colspan="4" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:rblist>
					</table>
				</div>
				</set:if>
				<set:if name="corpType" condition="EQUALS" value="2" >
				<div title="operator_approver_function"  id="operator_approver_function">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<set:rblist file="app.cib.resource.sys.operator_approver_function">
             		 <tr>
                		<td class="content1" width="30"><input id="item_functionList" type=checkbox name="item_functionList" value="<set:rbkey/>" <set:rbselected  equals='item_functionList'  output='checked'/>></td>
               	 		<td class="content1"><set:rbvalue/></td>
                		<td class="content1">&nbsp;</td>
              		</tr>
             		 <tr>
               	 		<td colspan="3" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:rblist>
					</table>
				</div>
				</set:if>
				<set:if name="corpType" condition="EQUALS" value="3" >
				<div title="operator_approver_function"  id="operator_approver_function">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<set:rblist file="app.cib.resource.sys.operator_approver_function">
             		 <tr>
                		<td class="content1" width="30"><input id="item_functionList" type=checkbox name="item_functionList" value="<set:rbkey/>" <set:rbselected  equals='item_functionList'  output='checked'/>></td>
               	 		<td class="content1"><set:rbvalue/></td>
                		<td class="content1">&nbsp;</td>
              		</tr>
             		 <tr>
               	 		<td colspan="3" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:rblist>
					</table>
				</div>
				</set:if>
				<set:if name="corpType" condition="EQUALS" value="4" >
				<div title="operator_function"  id="operator_function">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<set:rblist file="app.cib.resource.sys.operator_function">
             		 <tr>
                		<td class="content1" width="30"><input id="item_functionList" type=checkbox name="item_functionList" value="<set:rbkey/>" <set:rbselected  equals='item_functionList'  output='checked'/>></td>
               	 		<td class="content1"><set:rbvalue/></td>
                		<td class="content1">&nbsp;</td>
              		</tr>
					<tr>
               	 		<td colspan="4" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:rblist>
					</table>
				</div>
				<div title="approver_function"  id="approver_function" style="display:none">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
					<set:rblist file="app.cib.resource.sys.approver_function">
             		 <tr>
                		<td class="content1" width="30"><input id="item_functionList" type=checkbox name="item_functionList" value="<set:rbkey/>" <set:rbselected  equals='item_functionList'  output='checked'/>></td>
               	 		<td class="content1"><set:rbvalue/></td>
                		<td class="content1">&nbsp;</td>
              		</tr>
					<tr>
               	 		<td colspan="4" class="seperateline"><img src="/cib/images/shim.gif" width="1" length="1"></td></tr>
					</set:rblist>
					</table>
				</div>
				</set:if>
				</td>
              </tr>
              <tr>
                <td class="label1"><set:label name="Description" defaultvalue="Description"/></td>
                <td class="content1"><input name="groupDesc" type="text" id="groupDesc" value="<set:data name='groupDesc'/>" size="60" maxlength="60"></td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton"><input id="buttonOk" name="buttonOk" type="button" value="<set:label name='buttonOK' defaultvalue=' OK '/>" onClick="doSubmit('ok')">
<!--                  <input id="bottonReset" name="bottonReset" type="reset" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
				  <input id="ActionMethod" name="ActionMethod" type="hidden" value="editGroup">
				  <input id="groupId" name="groupId" type="hidden" value="<set:data name='groupId'/>">
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
