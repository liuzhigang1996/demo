<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
<SCRIPT language=JavaScript src="/cib/javascript/common1.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corporation.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.group_man" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_list_bankside" defaultvalue="BANK Online Banking >Corporation Preferences > User Group List"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_list" defaultvalue="USER GROUP LIST"/><!-- InstanceEndEditable --></td>
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
			<set:messages width="100%" cols="1" align="center"/>
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
                <td class="listheader1"><set:label name="User_Group_Name" defaultvalue="User Group Name"/></td>
                <td class="listheader1"><set:label name="Role" defaultvalue="Role"/></td>
				<td class="listheader1"><set:label name="Description" defaultvalue="Description"/></td>
				<td class="listheader1"><set:label name="Status" defaultvalue="Status"/></td>
                <td align="center" class="listheader1"><set:label name="View_Detail" defaultvalue="View Detail"/></td>
              </tr>
			  <set:list name="userGroupList" showPageRows="10" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1"><set:listdata name="groupName"/></td>
                <td class="listcontent1"><set:listdata name="roleId" rb="app.cib.resource.common.corp_role"/></td>
                <td class="listcontent1"><set:listdata name="groupDesc"/></td>
				<td class="listcontent1"><set:listdata name="status" rb="app.cib.resource.common.status"/></td>
                <td align="center" class="listcontent1"><a onClick="postToMainFrame('/cib/corpInfo.do?ActionMethod=viewGroupDetail',{groupId:'<set:listdata name='groupId'/>',corpType:'<set:data name='corpType'/>'})" href="#">
                  <set:label name="View_Detail" defaultvalue="View Detail"/></a></td>
              </tr>
			  </set:list>
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
