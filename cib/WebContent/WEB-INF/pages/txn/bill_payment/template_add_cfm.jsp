<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.bill_payment">
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
	var genDiv = document.getElementById('general');
	var cardDiv = document.getElementById('card');
	var payType = '<set:data name="payType"/>';
	if (payType == '<set:data name="payType1"/>') {
		genDiv.style.display = 'block';
		cardDiv.style.display = 'none';
	} else if (payType == '<set:data name="payType2"/>') {
		cardDiv.style.display = 'block';
		genDiv.style.display = 'none';
	}
}
function doSubmit(arg) {
	if (arg == 'add') {
		document.form1.ActionMethod.value = 'addTemplateCfm';
	} else if (arg == 'cancel') {
		document.form1.ActionMethod.value = 'listTemplate';
	}
	document.form1.submit();
	setFormDisabled("form1");
	//setFieldEnabled("cancel");
}
function doCancel(){
	setFormDisabled("form1");
	document.getElementById("ActionMethod").value="addTemplateCancel";
	document.getElementById("form1").submit();
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/templatePayment.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.bill_payment" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_tmp_add" defaultvalue="MDB Corporate Online Banking > Pay Bills > Frequent Payment List"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_tmp_add" defaultvalue="Add An Item To Frequent Payment List"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/templatePayment.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><set:messages width="100%" cols="1" align="center"/> </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="2" class="groupinput"><set:label name="confirmation" defaultvalue="confirmation"/></td>
                </tr>
                <tr>
                  <td width="28%" class="label1"><set:label name="operation" defaultvalue="operation"/></td>
                  <td width="72%" class="content1"><set:label name='functionTitle_tmp_add'/></td>
                </tr>
                <tr>
                  <td colspan="2" class="groupinput"><set:label name='template_info'/></td>
                </tr>
                <tr>
                  <td width="28%" class="label1"><set:label name="payType" defaultvalue="payType"/></td>
                  <td width="72%" class="content1"><set:data name='payType'rb="app.cib.resource.txn.pay_type"/></td>
                </tr>
                <tr>
                  <td colspan="2"><div id="general" style="display:block">
                      <table width="100%" border="0" cellpadding="3" cellspacing="0">
                        <tr class="greyline">
                          <td width="28%" class="label1"><set:label name="category" defaultvalue="category"/></td>
                          <td width="72%" class="content1"><set:data name='category' db="category"/></td>
                        </tr>
                        <tr>
                          <td class="label1"><set:label name="merchant" defaultvalue="merchant"/></td>
                          <td class="content1"><set:data name='merchant' db="merchant"/></td>
                        </tr>
                        <tr class="greyline">
                          <td class="label1"><set:label name="billNo" defaultvalue="billNo"/></td>
                          <td class="content1"><set:data name='billNo1'/> </td>
                        </tr>
                        <tr>
                          <td class="label1"><set:label name="payment_account" defaultvalue="payment_account"/></td>
                          <td class="content1"><set:data name='fromAccount' db="accountByUser"/>
                            <input id="fromAccountName" type="hidden" name="fromAccountName" value="<set:data name='fromAccountName'/>">
                          </td>
                        </tr>
                        <tr class="greyline">
                          <td class="label1"><set:label name="billName" defaultvalue="billName"/></td>
                          <td class="content1"><set:data name='billName'/></td>
                        </tr>
                        <tr>
                          <td class="label1"><set:label name="remark" defaultvalue="remark"/></td>
                          <td class="content1"><set:data name='remark'/></td>
                        </tr>
                      </table>
                    </div>
                    <div id="card" style="display:none">
                      <table width="100%" border="0" cellpadding="3" cellspacing="0">
                        <tr class="greyline">
                          <td width="28%" class="label1"><set:label name="card_no" defaultvalue="card_no"/></td>
                          <td width="72%" class="content1">
						  	<set:data name='billNo1'/>
                          </td>
                        </tr>
                        <tr>
                          <td class="label1"><set:label name="debit_account" defaultvalue="debit_account"/></td>
                          <td class="content1">
						  	<set:data name='fromAccount' db="accountByUser"/>
                          </td>
                        </tr>
                        <tr class="greyline">
                          <td class="label1"><set:label name="payment_amount" defaultvalue="payment_amount"/></td>
                          <td class="content1">
                            <set:data name='cardCcy'/> - <set:data name='transferAmount' format="amount"/>
						  </td>
                        </tr>
                        <tr>
                          <td class="label1"><set:label name="remark" defaultvalue="remark"/></td>
                          <td class="content1">
						  	<set:data name='remark'/>
						  </td>
                        </tr>
                      </table>
                    </div>
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
                  <td height="40" colspan="2" class="sectionbutton"><input id="add2" name="add2" type="button" value="&nbsp;&nbsp;<set:label name='confirm' defaultvalue=' confirm '/>&nbsp;&nbsp;" onClick="doSubmit('add')">
                    <input name="return" id="return" type="button" value="&nbsp;<set:label name='return' defaultvalue='return'/>&nbsp;" onClick="doCancel()">
                    <input id="ActionMethod" name="ActionMethod" type="hidden" value="ok">
                    <input id="cardCcy" name="cardCcy" type="hidden" value="<set:data name='cardCcy'/>">
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
