<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html>
<set:loadrb file="app.cib.resource.bnk.txn_pro">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<title>Corporation Banking</title>
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<link rel="stylesheet" type="text/css" href="/cib/css/txnPro.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/baidu_jquery.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	
}

function doSubmit(operator,menu2nd)
{
	document.getElementById("ActionMethod").value = operator;
	document.getElementById("menu2nd").value = menu2nd;
	setFormDisabled("form1");
	document.getElementById("form1").submit();
}
</script>
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><set:label name="navigationTitle" /></td>
    <td class="buttonprint" style="background-image:url(images/button-print_<%=session.getAttribute("Locale$Of$Neturbo")%>.gif)"><a href="#" onClick="printPage('pageheader');"><img src="/cib/images/shim.gif" width="61" height="18" border="0"></a></td>
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
    <td class="title1" nowrap><set:label name="functionTitle" /></td>
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
		  <form action="/cib/transferPrompt.do" method="post" name="form1" id="form1">
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
					<set:messages width="100%" cols="1" align="center"/>
					</td>
				</tr>
			</table>
			
			<div>
				<table cellspacing="0">
					<tr style = "background-color: #D7D7D7;">
						<td class = "table_header"><set:label name = 'No_label' /></td>
						<td class = "table_header" style = "text-align: left;padding-left: 0px;"><set:label name = '2nd_menu' /></td>
						<td class = "table_header"><set:label name = 'timeLabel' /></td>
						<td class = "table_header"><set:label name = 'operatorLabel' /></td>
						<td class = "table_header"><set:label name = 'approverLabel' /></td>
						<td class = "table_header"><set:label name = 'content_e' /></td>
						<td class = "table_header"><set:label name = 'content_c' /></td>
						<td class = "table_header"><set:label name = 'actionLabel' /></td>
					</tr>
					<set:list name="retList">
					<tr>
						<td class = "table_content"><set:listdata name = 'number' /></td>
						<td class = "table_content" style = "text-align: left;padding-left: 0px;"><set:listdata name = 'txnType'  rb = "app.cib.resource.bnk.2ndMenu"/></td>
						<td class = "table_content"><set:listdata name = 'modifiedTime' /></td>
						<td class = "table_content"><set:listdata name = 'operator' /></td>
						<td class = "table_content"><set:listdata name = 'approver' /></td>
						<td class = "table_content">xxxx</td>
						<td class = "table_content">xxxx</td>
						<td class = "table_content">
							<set:listif name = 'txnType' condition="notequals" value="8">
								<span ><a class ="myHref" href = "#" onclick = "doSubmit('view','<set:listdata name = 'txnType' />')"><set:label name = 'viewButton' /></a></span>
								<span ><a class ="myHref" href = "#" onclick = "doSubmit('editLoad','<set:listdata name = 'txnType' />')"><set:label name = 'editButton' /></a></span>
								<span ><a class ="myHref" href = "#" onclick = "doSubmit('clearLoad','<set:listdata name = 'txnType' />')"><set:label name = 'clearButton' /></a></span>
							</set:listif>
							<set:listif name = 'txnType' condition="equals" value="8">
								<span ><a class ="myHref" href = "#" onclick = "doSubmit('viewLoginMessage','<set:listdata name = 'txnType' />')"><set:label name = 'viewButton' /></a></span>
								<span ><a class ="myHref" href = "#" onclick = "doSubmit('editLoginMessageLoad','<set:listdata name = 'txnType' />')"><set:label name = 'editButton' /></a></span>
								<span ><a class ="myHref" href = "#" onclick = "doSubmit('clearLoginMessageLoad','<set:listdata name = 'txnType' />')"><set:label name = 'clearButton' /></a></span>
							</set:listif>
						</td>
					</tr>
					</set:list>
				</table>
			</div>
			
			
			
			<input id="ActionMethod" name="ActionMethod" type="hidden" value="edit">
			<input id="menu2nd" name="menu2nd" type="hidden" >
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
