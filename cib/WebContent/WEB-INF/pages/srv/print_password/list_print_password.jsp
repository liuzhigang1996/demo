<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.srv.print">
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
<script src="ocx/AC_ActiveX.js"></script>
<script src="ocx/AC_RunActiveContent.js"></script>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function printMailer(printId, transPwsdFlag)
{
  if(document.getElementById("PrintCom")==null){
		alert("Load PrintCom ActiveX failed, Exit!");
  }
  PrintComAX1 = document.getElementById("PrintCom");
  PrintComAX1.BatchNo = printId;
  PrintComAX1.Images = '';
  url =  location.protocol + "//" + location.hostname + ":" + location.port + "/cib/prtcom";
  PrintComAX1.ServerUrl = url;
  PrintComAX1.LocalWorkPath = 'C:/printcom';
  if(transPwsdFlag == 'Y'){
      PrintComAX1.Template = 'pinmailer1.rav';
  }else{
      PrintComAX1.Template = 'pinmailer2.rav';
  }
  PrintComAX1.PrintObject = 'pinmailer';
  PrintComAX1.EnableZip = 'Y';
  PrintComAX1.DuplexPrint = 'N';
  PrintComAX1.print();
  
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/messageManage.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.message" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
        <set:label name="navigationTitle_printlist" defaultvalue="MDB Corporate Online Banking > Service > Print Password"/>
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
        <set:label name="functionTitle_printlist" defaultvalue="PRINT LIST"/>
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
		  <form action="/cib/messageManage.do?ActionMethod=listPrintPassword" method="post" name="form1" id="form1">
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
    				<td>
    					<div id="TestDiv1"  style="display:block">
    					<OBJECT id="PrintCom" classid="clsid:F050702E-DE37-4DD8-A690-7451F28B4636" codebase="/cib/ocx/PrintCom.ocx" width="1" height="1" border="">
  						</OBJECT>
    					</div>
    				</td>
  				 </tr>
                  <tr>
                    <td class="listheader1"><set:label name="Cif_Name" defaultvalue="Cif Name"/></td>
                    <td class="listheader1"><set:label name="Cif_No" defaultvalue="CifNo"/></td>
                    <td class="listheader1"><set:label name="Login_Id" defaultvalue="Login ID"/></td>
                    <td class="listheader1"><set:label name="Remark" defaultvalue="Remark"/></td>
                    <td class="listheader1"><set:label name="Create_Time" defaultvalue="Create Time"/></td>
                    <td align="center" class="listheader1"><set:label name="Do_Print" defaultvalue="Print Password"/></td>
                  </tr>
                  <set:list name="printList" showPageRows="10" showNoRecord="YES">
                    <tr class="<set:listclass class1='' class2='greyline'/>">
                      <td class="listcontent1"><set:listdata name="cifName" /></td>
                      <td class="listcontent1"><set:listdata name="cifNo" /></td>
                      <td class="listcontent1"><set:listdata name="loginId" /></td>
                      <td class="listcontent1"><set:listdata name="remark"/></td>
                      <td class="listcontent1"><set:listdata name="createTime"/></td>
                      <td align="center" class="listcontent1"><button onClick="printMailer('<set:listdata name='printId'/>', '<set:listdata name='transPwsdFlag'/>')", href="#"><set:label name="Do_Print" defaultvalue="Print"/></button></td>
                    </tr>
                  </set:list>
                </table>
                <!--  
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td height="40" colspan="2" class="sectionbutton">
						<input id="buttonAdd" name="buttonAdd" type="button" value="<set:label name='buttonAdd' defaultvalue='Add'/>" onClick="addMessage()">
                      	<input id="buttonDelete" name="buttonDelete" type="button" value="<set:label name='buttonDelete' defaultvalue='Delete'/>" onClick="deleteMessage();" disabled>
                      	<input id="ActionMethod" name="ActionMethod" type="hidden" value="delete"></td>
                  </tr>
                </table>
                -->
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
