<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bnk.corp_subsidiary">
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
	if("1" == "<set:data name='editableStatus' />"){
		setFormDisabled("form1");
	}
}
function doSubmit(arg, accessId) {
	if (arg == 'addSubsidiay') {
		var url="/cib/corpSubsidiary.do?ActionMethod=listCorpForAdd";
		var popWin1= window.open(url,"temp1","toolbar=no,location=no,top=50,left=50,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=530,height=400");
		popWin1.focus();
	}else if(arg == 'removeSubsidiary'){
		setFormDisabled("form1");
		document.getElementById("ActionMethod").value = arg;
		document.getElementById("removeIDs").value = accessId;
		document.getElementById("form1").submit();
	}else if(arg == 'updateSubsidiary'){
		setFormDisabled("form1");
		document.getElementById("ActionMethod").value = arg;
		document.getElementById("form1").submit();
	}
}
function addSubList(arg) {
		setFormDisabled("form1");
		document.getElementById("ActionMethod").value = "addSubsidiary";
		document.getElementById("subsidiaryIDs").value = arg;
		document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/corpSubsidiary.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bnk.corp_subsidiary" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
        <set:label name="navigationTitle" defaultvalue="MDB Corporate Online Banking > Corp Preferences > Set Subsidiaries"/>
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
        <set:label name="functionTitle" defaultvalue="SUBSIDIARY MAINTENANCE"/>
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
		  <form action="/cib/corpSubsidiary.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
                <set:messages width="100%" cols="1" align="center"/>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td colspan="2" class="groupinput"><set:label name="Corp_Info" defaultvalue="Corporation Information"/></td>
                  </tr>
                  <tr>
                    <td width="150" class="label1"><set:label name="Corp_Id" defaultvalue="Company Id"/></td>
                    <td width="750" class="content1"><set:data name='corpId'/></td>
                  </tr>
                  <tr class="greyline">
                    <td class="label1"><set:label name="Corp_Name" defaultvalue="Company Name"/></td>
                    <td class="content1"><set:data name='corpName'/></td>
                  </tr>
                  <tr>
                    <td width="28%" class="label1"><set:label name="Status" defaultvalue="Status"/></td>
                    <td width="72%" class="content1"><set:data name='editableStatus' rb='app.cib.resource.common.status' /></td>
                  </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td colspan="3" class="groupinput"><set:label name="Subsidiaries" defaultvalue="Subsidiaries"/>
					</td>
					<td align="center" class="groupinput">
                      <input id="buttonAdd" name="buttonAdd" type="button" value="<set:label name='buttonAddSub'/>" onClick="doSubmit('addSubsidiay')">                    </td>
                  </tr>
                  <tr>
                    <td class="listheader1"><set:label name="Corp_Id" defaultvalue="Subsidiary ID"/></td>
                    <td class="listheader1"><set:label name="Corp_Name" defaultvalue="Subsidiary Name"/></td>
                    <td class="listheader1"><set:label name="Status" defaultvalue="Status"/></td>
                    <td align="center" class="listheader1"><set:label name="Remove"/></td>
                  </tr>
                  <set:list name="subsidiaryList" showNoRecord="YES">
                    <tr class="<set:listclass class1='' class2='greyline'/>">
                      <td class="listcontent1"><set:listdata name="corpId"/></td>
                      <td class="listcontent1"><set:listdata name="corpName"/></td>
                      <td class="listcontent1"><set:listdata name="status" rb="app.cib.resource.common.status"/></td>
                      <td align="center" class="listcontent1">
                        <input id="buttonRemove" name="buttonRemove" type="button" value="<set:label name='buttonRemove' defaultvalue='Remove'/>" onClick="doSubmit('removeSubsidiary', '<set:listdata name='corpId'/>')">                      </td>
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
                    <td height="40" colspan="2" class="sectionbutton"><input id="buttonOK" name="buttonOK" type="button" value="<set:label name='buttonOK'/>" onClick="doSubmit('updateSubsidiary')">
                      <input id="ActionMethod" name="ActionMethod" type="hidden" value="updateSubsidiary">
                      <input id="subsidiaryIDs" name="subsidiaryIDs" type="hidden" value="">
                      <input id="excludeIDs" name="excludeIDs" type="hidden" value="">
				    <input id="removeIDs" name="removeIDs" type="hidden" value=""></td>
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
