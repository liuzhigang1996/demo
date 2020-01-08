<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
function doSubmit(arg) {
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/wireInInfo.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.wire_in" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_inward" defaultvalue="TIME DEPOSIT DETIAL"/><!-- InstanceEndEditable --></td>
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
                <td colspan="3" class="groupinput"><set:label name="inward_rem_info" defaultvalue="inward_rem_info"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1" align=""><b><set:label name='date_time' defaultvalue="date_time"/>&nbsp;<set:label name='received' defaultvalue="received"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
				<td width="70%" class="content1"><set:data name="RECEIVED_DATE" format="date"/>&nbsp;<set:data name="RECEIVED_TIME" format="time"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align=""><b><set:label name="reference2" defaultvalue="reference2"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name="REF_NO"/></td>
              </tr>
              <tr class="">
                <td class="label1" align=""><b><set:label name="sender" defaultvalue="sender"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name="SWIFT_SENDER"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align=""><b><set:label name="ordering_customer_name" defaultvalue="ordering_customer_name"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name="ORDER_CUSTOMER_INFO1"/></td>
              </tr>
              <tr class="">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='ORDER_CUSTOMER_INFO2'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='ORDER_CUSTOMER_INFO3'/></td>
              </tr>
              <tr class="">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='ORDER_CUSTOMER_INFO4'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='ORDER_CUSTOMER_INFO5'/></td>
              </tr>
              <tr class="">
                <td class="label1" align=""><b><set:label name="amount" defaultvalue="amount"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name="CURRENCY" db="rcCurrencyCBS"/>&nbsp;<set:data name="AMOUNT" format='amount'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align=""><b><set:label name="value_date" defaultvalue="value_date"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='VALUE_DATE' format='date'/></td>
              </tr>
              <tr class="">
                <td class="label1" align=""><b><set:label name="beneficiary_customer" defaultvalue="beneficiary_customer"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='BENEFICIARY_ACC'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='BENEFICIARY_CUSTOMER2'/></td>
              </tr>
              <tr class="">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='BENEFICIARY_CUSTOMER3'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='BENEFICIARY_CUSTOMER4'/></td>
              </tr>
              <tr class="">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='BENEFICIARY_CUSTOMER5'/></td>
              </tr>
			 </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput">&nbsp;</td>
              </tr>
              <tr class="">
                <td width="30%" class="label1"><b><set:label name="remittance_info" defaultvalue="remittance_info"/></td>
                <td width="70%" class="content1"><set:data name="REMITTANCE_INFO1"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1">&nbsp;</td>
                <td class="content1"><set:data name='REMITTANCE_INFO2'/></td>
              </tr>
              <tr class="">
                <td class="label1">&nbsp;</td>
                <td class="content1"><set:data name='REMITTANCE_INFO3'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1">&nbsp;</td>
                <td class="content1"><set:data name='REMITTANCE_INFO4'/></td>
              </tr>
              <tr class="">
                <td class="label1"><b><set:label name="details_of_charges" defaultvalue="details_of_charges"/></td>
                <td class="content1"><set:data name='DETAILS_CHARGES'/></td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
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
