<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.message">
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
var d1 = null;
//modify by hjs 20070213
function pageLoad(){
	b1 = document.getElementById('buttonDelete');
	var messageIds = document.form1.messageId;
	if(messageIds){
		if(messageIds.length){
			for(i=0; i<messageIds.length; i++){
				messageIds[i].onclick = checkMsgSeleted;
				if(messageIds[i].checked){
					activeButton();
				}
			}
		} else {
			messageIds.onclick = checkMsgSeleted;
			if(messageIds.checked){
				activeButton();
			}
		}
	}
}
function activeButton(){
	b1.disabled = false;
}
function disabledButton(){
	b1.disabled = true;
}
function checkMsgSeleted(){
	activeButton();
	var messageIds = document.form1.messageId;
	if(messageIds.length){
		for(i=0; i<messageIds.length; i++){
			if(messageIds[i].checked){
				return;
			}
		}
		disabledButton();
	} else {
		if(!messageIds.checked){
			disabledButton();
		}
	}
}
function deleteMessage() {
	if(validate_bank_list(document.getElementById("form1"))){
   		document.form1.ActionMethod.value = "delete";
  		setFormDisabled("form1");
 		document.form1.submit();
	}
}
function addMessage() {
   document.form1.ActionMethod.value = "addLoad";
   setFormDisabled("form1");
   document.form1.submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/messageManage.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.message" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
        <set:label name="navigationTitle_bnklist" defaultvalue="MDB Corporate Online Banking > Service > Message List"/>
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
        <set:label name="functionTitle_bnklist" defaultvalue="MESSAGE LIST"/>
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
		  <form action="/cib/messageManage.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><set:messages width="100%" cols="1" align="center"/>
                            <set:fieldcheck name="bank_list" form="form1" file="message_manage" />
						</td>
                      </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td class="listheader1"></td>
                    <td class="listheader1"><set:label name="Post_Date" defaultvalue="Post Date"/></td>
                    <td class="listheader1"><set:label name="From_Date" defaultvalue="From Date"/></td>
                    <td class="listheader1"><set:label name="To_Date" defaultvalue="To Date"/></td>
                    <td class="listheader1"><set:label name="Message_Title" defaultvalue="Message Title"/></td>
                    <td class="listheader1"><set:label name="Status" defaultvalue="Status "/></td>
                    <td align="center" class="listheader1"><set:label name="View_Detail" defaultvalue="View Detail"/></td>
                  </tr>
                  <set:list name="messageList" showPageRows="10" showNoRecord="YES">
                    <tr class="<set:listclass class1='' class2='greyline'/>">
                      <td class="listcontent1">
					  <set:listif name="status" condition="equals" value="0">
					  <set:listcheckbox name="messageId" value="messageId" text=""/>
					  </set:listif>&nbsp;
					  </td>
                      <td class="listcontent1"><set:listdata name="submitTime" format="date"/></td>
                      <td class="listcontent1"><set:listdata name="fromDate" format="date"/></td>
                      <td class="listcontent1"><set:listdata name="toDate" format="date"/></td>
                      <td class="listcontent1"><set:listdata name="msgTitle"/></td>
                      <td class="listcontent1"><set:listdata name="status" rb="app.cib.resource.common.status"/></td>
                      <td align="center" class="listcontent1"><a onClick="postToMainFrame('/cib/messageManage.do?ActionMethod=view',{messageId:'<set:listdata name='messageId'/>'})" href="#"><set:label name="View_Detail" defaultvalue="View Detail"/></a></td>
                    </tr>
                  </set:list>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td height="40" colspan="2" class="sectionbutton">
						<input id="buttonAdd" name="buttonAdd" type="button" value="<set:label name='buttonAdd' defaultvalue='Add'/>" onClick="addMessage()">
                      	<input id="buttonDelete" name="buttonDelete" type="button" value="<set:label name='buttonDelete' defaultvalue='Delete'/>" onClick="deleteMessage();" disabled>
                      	<input id="ActionMethod" name="ActionMethod" type="hidden" value="delete"></td>
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
