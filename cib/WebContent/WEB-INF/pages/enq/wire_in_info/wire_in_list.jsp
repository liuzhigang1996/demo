<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.enq.wire_in">
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
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/wireInInfo.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.wire_in" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_wire" defaultvalue="MDB Corporate Online Banking > Time Deposit >  "/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_wire_list" defaultvalue="FREQUENT PAYMENT LIST"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/wireInInfo.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
			  <tr>
			  	<td colspan="7" class="label"><b><set:label name='sentence' defaultvalue="sentence"/></b></td>
			  </tr>
			  <tr>
			  	<td colspan="5" align="left">
					<set:label name="currency" defaultvalue="currency"/>:
					<select name="currency" id="currency" onChange="document.form1.submit();">
					  <!--
					  <option value="0" selected><set:label name="all" defaultvalue="all"/></option>
					  -->
                   	    <set:rblist file="app.cib.resource.enq.wire_in_ccy">
						    <set:rboption name="currency"/>
					    </set:rblist>
			        </select>
				</td>
			  	<td height="40" valign="top" class="content1" colspan="2"></td>
			  </tr>
              <tr>
                <td class="listheader1" align="left" valign="middle"><set:label name="date_time" defaultvalue="date_time"/><br><set:label name="received" defaultvalue="received"/></td>
                <td class="listheader1" align="left" valign="middle"><set:label name="reference" defaultvalue="reference"/></td>
                <td class="listheader1" align="left" valign="middle"><set:label name="ordering_customer_name" defaultvalue="ordering_customer_name"/></td>
                <td class="listheader1" align="center" valign="middle"><set:label name="currency" defaultvalue="currency"/></td>
                <td class="listheader1" align="right" valign="middle"><set:label name="amount" defaultvalue="amount"/></td>
                <td class="listheader1" align="left" valign="middle"><set:label name="beneficiary" defaultvalue="beneficiary"/></td>
                <td class="listheader1" align="left" valign="middle"><set:label name="status" defaultvalue="status"/></td>
              </tr>
			  <set:list name="wireInfoList" showPageRows="10" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1" align="left" valign="middle"><set:listdata name="receivedDate" format="date"/><br><set:listdata name="receivedTime" format="time"/></td>
                <td class="listcontent1" align="left" valign="middle"><a onClick="toTargetFormgoToTarget('/cib/wireInInfo.do?ActionMethod=viewWireInInfo&seqNo=<set:listdata name='seqNo'/>')" href="#"><set:listdata name="referenceNo"/></a></td>
                <td class="listcontent1" align="left" valign="middle"><set:listdata name="orderingCustomerName"/><br><set:listdata name="orderingCustomerName2"/></td>
                <td class="listcontent1" align="center" valign="middle"><set:listdata name="currency" db="rcCurrencyCBS"/></td>
                <td class="listcontent1" align="right" valign="middle"><set:listdata name="amount" format="amount"/></td>
                <td class="listcontent1" align="left" valign="middle"><set:listdata name="beneficiaryAccount"/><br><set:listdata name="beneficiaryCustomer2"/></td>
                <td class="listcontent1" align="left" valign="middle"><set:listdata name="status"/></td>
              </tr>
			  </set:list>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="listWireInInfo">
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
