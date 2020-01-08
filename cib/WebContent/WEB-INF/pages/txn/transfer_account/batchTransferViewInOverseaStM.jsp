<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.bat.batch_transfer_oversea">
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
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/transferHistory.do" --><!-- InstanceParam name="help_href" type="text" value="#" passthrough="true" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.bat.batch_transfer_oversea" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitleDetailStM" defaultvalue="BANK Online Banking >Transfer > Single D to Multi C In Bank"/><!-- InstanceEndEditable --></td>
    <td class="buttonprint" style="background-image:url(images/button-print_<%=session.getAttribute("Locale$Of$Neturbo")%>.gif)"><a href="#" onClick="printPage('pageheader');"><img src="/cib/images/shim.gif" width="61" height="18" border="0"></a></td>
	<!--
    <td class="buttonhelp"><a href="@@@(help_href)@@@"><img src="/cib/images/shim.gif" width="36" height="18" border="0"></a></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitleDetailStM" defaultvalue="SINGLE DEBIT TO MULTI CREDIT TRANSFER DETAIL"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/transferHistory.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="4"  class="label1"><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
              </tr>
              <tr class="greyline">
                <td  width="28%" class="label1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                <td  width="72%" class="content1" colspan="2" ><set:data name='fromAccount'/>
				</td>
              </tr>
			  <tr >
			    <td class="label1"><set:label name="Amount_to_be_debited" defaultvalue="Amount to be debited"/></td>
               <td class="content1"><set:data name='fromCurrency' db="rcCurrencyCBS"/><set:data name='fromAmount' format="amount"/>
			    </td>
              </tr>
			  <tr class="greyline">
                <td colspan="4"  class="label1"><b><set:label name="Transfer_To" defaultvalue="Transfer To"/></b></td>
              </tr>
			  </table>
			  <table width="100%" border="0" cellspacing="0" cellpadding="3">
			   <tr >
                <td  class="listheader1"><div align="left"><set:label name="SEQUENCE_NO" defaultvalue="SEQUENCE NO"/></td>
                <td  class="listheader1"><div align="left"><set:label name="ACCOUNT_NUMBER" defaultvalue="ACCOUNT NUMBER"/></td>
				<td class="listheader1" align="left"><set:label name="BENEFICIARY_NAME" defaultvalue="BENEFICIARY NAME"/></td>
				<td  class="listheader1"><div align="right"><set:label name="AMOUNT" defaultvalue="AMOUNT"/></td>
				<td  class="listheader1"><div align="left"><set:label name="DESCRIPTION" defaultvalue="DESCRIPTION"/></td>
				<td class="listheader1"><div align="left"><set:label name="upload_status" defaultvalue="upload_status"/></div></td>
			   </tr>
			  <set:list name="toList" showPageRows="10" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1"><div align="left"><set:listdata name="sequenceNo" /></div></td>
                <td class="listcontent1"><div align="left"><set:listdata name="toAccount"/></div></td>
				<td class="listcontent1"><div align="left"><set:listdata name="beneficiaryName"/><br><set:listdata name="beneficiaryName2"/></div></td>
			    <td class="listcontent1"><div align="right"><set:listdata name="transferAmount" format="amount"/></div></td>
			    <td class="listcontent1"><div align="left"><set:listdata name="remark" /></div></td>
				<td class="listcontent1"><div align="left"><set:listdata name="detailResult" rb="app.cib.resource.bat.batch_result"/></div></td>
              </tr>
			  </set:list>
			  <tr>
                <td class="label1"><set:label name="Currency" defaultvalue="Currency"/></td>
                <td class="content1" colspan="4" ><set:data name='toCurrency' db="rcCurrencyCBS"/>
				</td>
              </tr>
			   <tr class="greyline">
                <td class="label1"><set:label name="Total_Number" defaultvalue="Total Transfer Number "/></td>
                <td class="content1" colspan="4" ><set:data name='totalNumber'/>
				</td>
              </tr>
			   <tr>
                <td class="label1"><set:label name="Total_Amount" defaultvalue="Total Transfer Amount"/></td>
                <td class="content1" colspan="4" ><set:data name='totalAmount' format="amount"/>
				</td>
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
				<input id="submit1" name="submit1" type="button" value="<set:label name='buttonOK' defaultvalue=' buttonOK ' />" tabindex="3"  onClick="document.forms[0].submit();">
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="listLoad">				  </td>
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
