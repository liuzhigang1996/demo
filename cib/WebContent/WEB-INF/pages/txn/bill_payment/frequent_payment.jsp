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
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	var acc = '<set:data name="fromAccount"/>';
	if (acc != '') {
		$('balAccount').value= acc;
	}
	if($('fromAccount').value != 0){
		$('fromAccount').onchange();
	}
	if('<set:data name="amountErr"/>' == 'Y'){
		$('transferAmount').select();
	}
}
function doSubmit(arg) {
	if(validate_general_payment(document.getElementById("form1"))){
		if (arg == 'ok') {
			document.form1.ActionMethod.value = 'frequentPayment';
		} else if (arg == 'cancel') {
			document.form1.ActionMethod.value = 'generalPaymentCheck';
		}
		document.form1.submit();
		setFormDisabled("form1");
		//setFieldEnabled("cancel");
	}
}
function showBalance(ccyElement, balElement){
	if (($F('balAccount') != '') && ($F('balAccount') != '0')) {
		ccyElement.innerHTML = '<br>loading...';
		balElement.innerHTML = 'loading...';
		//registerElement(ccyElement.id);
		//registerElement(balElement.id);
		var params = 'showFromAcc=' + $F('balAccount');
		var url = '/cib/jsax?serviceName=AccInTxnService&' + params +'&language='+language;
		getMsgToElement(url, $F('balAccount'), '',
			function(){
				ccyElement.innerHTML = '<br>' + ccyElement.innerHTML
			},true,language
		);
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/billPayment.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.bill_payment" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_frequent" defaultvalue="MDB Corporate Online Banking > Bill Payment > Frequent Payment"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_frequent" defaultvalue="FREQUENT PAYMENT"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/billPayment.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="general_payment" form="form1" file="bill_payment" />
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="select_info" defaultvalue="Select the Payment Bill"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="merchant" defaultvalue="merchant"/></td>
                <td width="72%" class="content1"><set:data name='merchant' db="merchant"/>
				</td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="billNo" defaultvalue="billNo"/></td>
                <td class="content1"><set:data name='billNo1'/></td>
              </tr>
              <tr>
                <td class="label1"><set:label name="bill_amount" defaultvalue="bill_amount"/></td>
                <td class="content1"><set:data name='currency' db="rcCurrencyCBS"/> - 
				<set:if name="inputAmountFlag" value="0" condition="equals">
					<set:data name='transferAmount' format="amount"/>
				</set:if>
				<set:if name="inputAmountFlag" value="1" condition="equals">
                	<input type="text" name="transferAmount" id="transferAmount" maxlength="20" size="20" value="<set:data name='transferAmount' format='amount'/>">
				</set:if>
				</td>
              </tr>
			  <set:if name="inputAmountFlag" value="0" condition="equals">
              <tr class="greyline">
                <td class="label1"><set:label name="expiry_date" defaultvalue="expiry_date"/></td>
                <td class="content1"><set:data name='EXPIRY_DATE' format='date'/>                  
				</td>
              </tr>
			  <tr>
                <td class="label1"><set:label name="merchant_ref" defaultvalue="merchant_ref"/></td>
                <td class="content1"><set:data name='REMARKS'/>
				</td>
              </tr>
			  </set:if>
              <tr class="greyline">
                <td class="label1" valign="top"><set:label name="payment_account" defaultvalue="payment_account"/></td>
                <td class="content1">
					<select name="fromAccount" id="fromAccount" nullable="0" onChange="$('balAccount').value=this.value;$('showFromCurrency').innerHTML='';$('showAmount').innerHTML='';showBalance($('showFromCurrency'), $('showAmount'));">
						<option value="0" selected><set:label name="sel_debit_acc"/></option>
                  		<set:rblist db="caoasaAccountByUser">
				  			<set:rboption name="fromAccount"/>
				  		</set:rblist>
               	  	</select>
					<input type="hidden" name="balAccount" id="balAccount" value="">
                  	<span id="showFromCurrency"></span>&nbsp;&nbsp;<span id="showAmount"></span>
				</td>
              </tr>
              <tr class="">
                <td class="label1"><set:label name="remark" defaultvalue="remark"/></td>
                <td class="content1">
					<input name="remark" type="text" id="remark" value="<set:data name='remark'/>" size="50" maxlength="50">
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
					<input id="ok" name="ok" type="button" value="&nbsp;&nbsp;<set:label name='ok' defaultvalue=' ok '/>&nbsp;&nbsp;" onClick="doSubmit('ok')">
				  	<input id="ActionMethod" name="ActionMethod" type="hidden" value="ok">
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
