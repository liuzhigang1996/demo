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
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
}
function doSubmit(){
	if(validate_add(document.getElementById("form1"))){
		if(checkDate()){
  			setFormDisabled("form1");
  			document.form1.submit();
		} else {
			alert('<set:label name="err.srv.toDateError" rb="app.cib.resource.common.errmsg"/>');
			document.form1.toDate.select();
		}
	}
}
function checkDate() {
	var fromDate = document.form1.fromDate.value;
	var toDate = document.form1.toDate.value;
	var today = new Date();
	fromDate = new Date(fromDate.split('/')[2] + '/' + fromDate.split('/')[1] + '/' + fromDate.split('/')[0]);
	toDate = new Date(toDate.split('/')[2] + '/' + toDate.split('/')[1] + '/' + toDate.split('/')[0]);
	today = new Date(today.getYear(), today.getMonth(), today.getDate());
	if(toDate<today) return false;
	return true;
}
</script>
<script language="javascript" src="/cib/javascript/calendar.js"></script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/messageManage.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.srv.message" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_add" defaultvalue="MDB Corporate Online Banking > Service Maintenance> New Message"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_add" defaultvalue="NEW MESSAGE"/><!-- InstanceEndEditable --></td>
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
                            <set:fieldcheck name="message_manage" form="form1" file="message_manage" />                        </td>
                      </tr>
                    </table>
			<table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="Fill_Info" defaultvalue="Fill the following Information"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="Message_Title" defaultvalue="Message Title"/></td>
                <td width="72%" class="content1"><input id="msgTitle" name="msgTitle" type="text" value="<set:data name='msgTitle'/>" size="20" maxlength="20"></td>
              </tr>
			  <tr class="greyline">
                <td width="28%" class="label1"><set:label name="From_Date" defaultvalue="From Date"/></td>
                <td width="72%" class="content1"><input type="text" name="fromDate" id ="fromDate" value="<set:data name='fromDate'/>" size="10" maxlength="10">
                      <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "scwShow(document.getElementById('fromDate'), this,language);" ></td>
              </tr>
			  <tr>
                <td width="28%" class="label1"><set:label name="To_Date" defaultvalue="To Date"/></td>
                <td width="72%" class="content1"><input type="text" name="toDate" id ="toDate" value="<set:data name='toDate'/>" size="10" maxlength="10">
                      <img src="/cib/images/datetime.gif" width="16" height="16" align="absmiddle" style="cursor:hand" onClick= "scwShow(document.getElementById('toDate'), this,language);" ></td>
              </tr>
              <tr class="greyline">
                <td class="label1" valign="top"><set:label name="Message_Content" defaultvalue="Message Content"/><br>( <set:label name="char_limited"/> )</td>
                <td class="content1">
				<textarea name="msgContent" cols="40" rows="6" style="overflow:hidden" onKeyDown="if(this.value.length>=200){if(event.keyCode!=8)event.returnValue=false;}"><set:data name='msgContent'/></textarea>                </td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton"><input id="buttonOk" name="buttonOk" type="button" value="<set:label name='buttonOK' defaultvalue='OK '/>" onClick="doSubmit();">
<!--                <input id="bottonReset" name="bottonReset" type="reset" value="<set:label name='buttonReset' defaultvalue='Reset'/>"> -->
				  <input id="ActionMethod" name="ActionMethod" type="hidden" value="add">				  </td>
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
