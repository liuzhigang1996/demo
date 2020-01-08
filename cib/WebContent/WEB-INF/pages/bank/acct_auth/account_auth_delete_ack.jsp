<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.sys.account_authorization">
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
<SCRIPT language=JavaScript src="/cib/javascript/calendar.js"></script>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit()
{
	setFormDisabled('form1');
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/accountAuthorization.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.sys.account_authorization" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_Delete" defaultvalue="MDB Corporate Online Banking > Company Preferences >Delete Account Authorization"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_Delete" defaultvalue="DELETE ACCOUNT AUTHORIZATION"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/accountAuthorization.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->			
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="2" class="groupack"><set:label name="Acknowledgement" defaultvalue="Acknowledgement"/></td>
                </tr>
                <tr>
                  <td width="28%"  class="label1" ><set:label name="Operation" defaultvalue="Operation"/></td>
                  <td width="72%" class="label1" ><set:label name="Delete_Acct_Auth" defaultvalue="Delete Account Authorization"/></td>
                </tr>
                <tr   class="greyline">
                  <td class="label1"><set:label name="Ack_Status" defaultvalue="Status"/></td>
                  <td  class="content1"><set:label name='ackStatusAccepted' defaultvalue="Accepted"/></td>
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
                <td colspan="2" class="groupinput">&nbsp;</td>
              </tr>
            </table>
			 <table width="100%" border="0" cellspacing="0" cellpadding="3">              
			<tr>
				<td class="content1" width="28%"><set:label name="Acct_No" defaultvalue="Account No"/></td>
				<td class="content1" width="72%"><set:data name='account'/></td>
			</tr>
			<tr class="greyline">
				<td class="content1" width="28%"><set:label name="Auth_User" defaultvalue="Authorization User"/></td>
				<td class="content1" width="72%"><set:data name='authUser'/></td>
             </tr>
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr align="center">
                <td width="55%" height="40" align="right" class="sectionbutton"> 
					<input id="buttonOk" name="buttonOk" type="button" value="<set:label name='buttonOK' defaultvalue='OK'/>" onClick="doSubmit()">
               	  <input id="ActionMethod" name="ActionMethod" type="hidden" value="listAccountAuthorization">
				  <input id="corpId" name="corpId" type="hidden" value="<set:data name='corpId'/>">				  
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
