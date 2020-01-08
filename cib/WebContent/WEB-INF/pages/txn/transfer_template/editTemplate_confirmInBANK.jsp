<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.transfer_bank">
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
function doSubmit()
{
	setFormDisabled("form1");
	document.getElementById("form1").submit();
}
function doCancel()
{
	setFormDisabled("form1");
	document.getElementById("ActionMethod").value="editCancel";
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/transferTemplate.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.transfer_bank" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitleTemplate" defaultvalue="BANK Online Banking >Transfer > Transfer Templates"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitleEdit" defaultvalue="TRANSFER Templates Edit"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/transferTemplate.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
			<set:messages width="100%" cols="1" align="center"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
			 <tr class="greyline">
				 <td class="groupinput" colspan="4"><set:label name="Confirmation" defaultvalue="Confirmation"/></td>
              </tr>
			   <tr>
                <td class="label1" ><set:label name="Operation" defaultvalue="Operation"/></td>
				<td class="label1" ><set:label name="functionTitleEdit" defaultvalue="TRANSFERS TEMPLATE EDIT"/></td>
              </tr>
			   <tr>
                <td colspan="4" class="groupinput"><set:label name="TemplateTxn_Info" defaultvalue="Transfer Template Information"/></td>
              </tr>
              <tr>
                <td colspan="4"  class="label1"><b><set:label name="Transfer_From" defaultvalue="Transfer From"/></b></td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="From_Account" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="2" ><set:data name='fromAccount'/>
				</td>
              </tr>
			  <tr >
			    <td class="label1"><set:label name="Amount_to_be_debited" defaultvalue="Amount to be debited"/></td>
               <td class="content1"><set:data name='fromCurrency' db="rcCurrencyCBS"/><set:data name='debitAmount' format="amount"/>
			    </td>
              </tr>
			  <tr class="greyline">
                <td colspan="2"  class="label1"><b><set:label name="Transfer_To" defaultvalue="Transfer To"/></b></td>
              </tr>
			  <tr >
                <td class="label1"><set:label name="To_Account" defaultvalue="Account Number"/></td>
                <td class="content1" colspan="3" ><set:data name='toAccount'/>
				</td>
              </tr>
              
              <!-- add by linrui 20181105 -->
			  <tr>
                <td class="label1"><set:label name="To_Name" defaultvalue="Beneficiary Name"/></td>
                <td class="content1" colspan="3" ><set:data name='toName' format='name'/>
				</td>
              </tr>
              <!-- end -->
              
			   <tr class="greyline">
                <td class="label1"><set:label name="Transfer_Amount" defaultvalue="TransferAmount"/></td>
                <td class="content1" colspan="2"><set:data name='toCurrency' db="rcCurrencyCBS"/><set:data name='transferAmount' format="amount"/>
                </td>
              </tr>
              <!-- add by lzg 20190618 -->
              <set:if name="fromCurrency" condition="NOTEQUALS" field="toCurrency">
			  <tr >
			    <td class="label1"><set:label name="Exchange_Rate" defaultvalue="Exchange Rate"/></td>
               <td class="content1"><set:data name='showExchangeRate' format="rate"/>&nbsp;&nbsp;<span style = "color:red;"><set:label name="Exchange_Rate_Tip" defaultvalue="The Rate is for reference only, the dealing rate will be quoted upon trading."/></span>
			    </td>
              </tr>
			  </set:if>
			  <!-- add by lzg end -->
              <tr class="greyline">
                <td class="label1"><set:label name="Remark" defaultvalue="Remark"/></td>
                <td class="content1" colspan="3" ><set:data name='remark'/>
              </tr>
              <!-- ADD BY LINRUI 20190911 -->
              <tr>
                <td class="label1"><set:label name="Remark_Host" defaultvalue="Remark"/></td>
                <td class="content1" colspan="3"> <set:data name='remarkHost'/></td>
              </tr>
              <!-- end -->
            </table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr >
                <td height="40" colspan="2" class="sectionbutton">
				<input id="buttonOk" name="buttonOk" type="button" value="<set:label name='buttonConfirm' defaultvalue='Confirm'/>" onClick="doSubmit();">
				<input id="buttonCancel" name="buttonCancel" type="button" value="<set:label name='buttonCancel' defaultvalue='Cancel'/>" onClick="doCancel()">
				  <input id="ActionMethod" name="ActionMethod" type="hidden" value="editTemplateConfirm">
				  <!-- add by lzg 20190624 for pwc-->
					<set:singlesubmit/>
				  <!-- add by lzg end -->
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
