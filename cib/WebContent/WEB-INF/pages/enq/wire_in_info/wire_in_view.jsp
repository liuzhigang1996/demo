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
	document.getElementById("form1").submit();
	setFormDisabled("form1");
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/wireInInfo.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.wire_in" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_wire" defaultvalue="MDB Corporate Online Banking > Time Deposit > Time Deposit Detial"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_wire_view" defaultvalue="TIME DEPOSIT DETIAL"/><!-- InstanceEndEditable --></td>
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
                <td colspan="3" class="groupinput"><set:label name="wire_transfer_detail" defaultvalue="wire_transfer_detail"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1" align=""><b><set:label name='date_time' defaultvalue="date_time"/>&nbsp;<set:label name='received' defaultvalue="received"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
				<td width="70%" class="content1"><set:data name="receivedDate" format="date"/>&nbsp;<set:data name="receivedTime" format="time"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align=""><b><set:label name="reference2" defaultvalue="reference2"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name="referenceNo"/></td>
              </tr>
              <tr class="">
                <td class="label1" align=""><b><set:label name="sender" defaultvalue="sender"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name="swiftSender"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align=""><b><set:label name="ordering_customer_name" defaultvalue="ordering_customer_name"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name="orderingCustomerInfo1"/></td>
              </tr>
              <tr class="">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='orderingCustomerInfo2'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='orderingCustomerInfo3'/></td>
              </tr>
              <tr class="">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='orderingCustomerInfo4'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='orderingCustomerInfo5'/></td>
              </tr>
              <tr class="">
                <td class="label1" align=""><b><set:label name="amount" defaultvalue="amount"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name="currency" db="rcCurrencyCBS"/>&nbsp;<set:data name="amount" format='amount'/><input id="currency" type="hidden" name="currency" value="<set:data name='currency'/>"></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align=""><b><set:label name="value_date" defaultvalue="value_date"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='valueDate' format='date'/></td>
              </tr>
              <tr>
                <td class="label1" align=""><b><set:label name="beneficiary_customer" defaultvalue="beneficiary_customer"/></b></td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='beneficiaryAccount'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='beneficiaryCustomer2'/></td>
              </tr>
              <tr class="">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='beneficiaryCustomer3'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='beneficiaryCustomer4'/></td>
              </tr>
              <tr class="">
                <td class="label1" align="">&nbsp;</td>
                <td width="2%" class="content1">&nbsp;</td>
                <td class="content1"><set:data name='beneficiaryCustomer5'/></td>
              </tr>
			 </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput">&nbsp;</td>
              </tr>
              <tr class="">
                <td width="30%" class="label1"><b><set:label name="remittance_info" defaultvalue="remittance_info"/></td>
                <td width="70%" class="content1"><set:data name="remittanceInformation1"/></td>
              </tr>
              <tr class="greyline">
                <td class="label1">&nbsp;</td>
                <td class="content1"><set:data name='remittanceInformation2'/></td>
              </tr>
              <tr class="">
                <td class="label1">&nbsp;</td>
                <td class="content1"><set:data name='remittanceInformation3'/></td>
              </tr>
              <tr class="greyline">
                <td class="label1">&nbsp;</td>
                <td class="content1"><set:data name='remittanceInformation4'/></td>
              </tr>
              <tr class="">
                <td class="label1"><b><set:label name="details_of_charges" defaultvalue="details_of_charges"/></td>
                <td class="content1"><set:data name='detailsOfCharges'/></td>
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
				  	<input name="return" id="return" type="button" value="&nbsp;&nbsp;<set:label name='return' defaultvalue=' return '/>&nbsp;&nbsp;" onClick="doSubmit();">
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
