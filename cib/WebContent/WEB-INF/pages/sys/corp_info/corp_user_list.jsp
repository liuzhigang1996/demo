<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.sys.corp_user">
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
function doSubmit(){
	setFormDisabled("form1");
	document.getElementById("ActionMethod").value='addLoad';
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpInfo.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.corp_user" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_corpuser_list" defaultvalue="MDB Corporate Online Banking > Corporatin Info > Corp User List"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_list" defaultvalue="Corp User List"/><!-- InstanceEndEditable --></td>
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
		    <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td class="listheader1"><set:label name="User_Id" defaultvalue="User ID"/></td>
                <td class="listheader1"><set:label name="User_Name" defaultvalue="User_Name"/></td>
                <td class="listheader1"><set:label name="Role" defaultvalue="Role"/></td>
                <td class="listheader1"><set:label name="Group" defaultvalue="Group"/></td>
                <td align="center" class="listheader1"><set:label name="Auth_Level" defaultvalue="Level"/></td>
                <td class="listheader1"><set:label name="Status" defaultvalue="Status"/></td>
                <td align="center" class="listheader1"><set:label name="View_Detail" defaultvalue="View Detail"/></td>
              </tr>
			  <set:list name="userList" showPageRows="10" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline' />">
              	<!--  -->
                <td class="listcontent1">
                <set:listdata name="userId"/>
                </td>
                <td class="listcontent1"><set:listdata name="userName" maxlen="20"/></td>
                <td class="listcontent1"><set:listdata name="roleId" rb="app.cib.resource.common.corp_role"/></td>
                <td class="listcontent1"><set:listdata name="groupId" db="groupByCorp"/></td>
                <td align="center" class="listcontent1"><set:listdata name="authLevel" defaultvalue='-'/></td>
                <td class="listcontent1"><set:listdata name="status" rb="app.cib.resource.common.status"/></td>
                <!-- modified by lzg for GAPMC-EB-001-0040 -->
                <td align="center" class="listcontent1">
                <!-- <a onClick="postToMainFrame('/cib/corpInfo.do?ActionMethod=viewCorpUserDetial',{userId:'<set:listdata name='userId'/>'})" href="#">
                  <set:label name="View_Detail" defaultvalue="View_Detail" /></a> -->
                <a onClick="postToMainFrame('/cib/corpInfo.do?ActionMethod=viewCorpUserDetial',{userId:'<set:listdata name='userId'/>',corpId:'<set:listdata name='corpId'/>'})" href="#">
                  <set:label name="View_Detail" defaultvalue="View_Detail" /></a>
                  </td>
                  <!-- modified by lzg end -->
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
