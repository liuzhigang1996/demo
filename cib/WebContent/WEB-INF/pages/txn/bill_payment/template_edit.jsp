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
	var form1 = document.form1;
	var category = '<set:data name="category"/>';
	var merchant = '<set:data name="merchant"/>';
	var fromAccount = '<set:data name="fromAccount"/>';
	var payType = '<set:data name="payType"/>';
	var genDiv = document.getElementById('general');
	var cardDiv = document.getElementById('card');
	if (payType == '<set:data name="payType1"/>') {
		genDiv.style.display = 'block';
		cardDiv.style.display = 'none';
		cardDiv.innerHTML = '';
		form1.payType[0].checked = true;
		form1.payType[1].disabled = true;
		showMerchantList($('category'), $('merchant'),
			function(){
				$('merchant').value = '<set:data name="merchant"/>';
			}
		);
	} else if (payType == '<set:data name="payType2"/>') {
		cardDiv.style.display = 'block';
		genDiv.style.display = 'none';
		genDiv.innerHTML = '';
		$('billNo1_2').disabled = false;
		$('fromAccount_2').disabled = false;
		$('transferAmount_2').disabled = false;
		$('remark_2').disabled = false;
		form1.payType[1].checked = true;
		form1.payType[0].disabled = true;
		showCcyByCardNo('<set:data name="billNo1"/>', $('card_ccy').id);
	}
}
function doSubmit(arg) {
	if(validate_template_edit(document.getElementById("form1"))){
		//document.getElementById("submit1").disabled=true;
		if (arg == 'ok') {
			document.form1.ActionMethod.value = 'editTemplate';
		} else if (arg == 'cancel') {
			if($('merchant')){
				$('merchant').value= '';
			}
			document.form1.ActionMethod.value = 'listTemplate';
		} 
		document.getElementById("form1").submit();
		setFormDisabled("form1");
		//setFieldEnabled("cancel");
	}
}
function showMerchantList(originSelect, targetSelect, callback){
	if(callback == 'undefined') callback = null;
	var params = getParams(originSelect, targetSelect);
	var url = '/cib/jsax?serviceName=MerchantListService&' + params +'&language='+language;
	getMsgToSelect(url,'', callback,true,language);
}
function getParams(originSelect, targetSelect) {
	var targetType = 'targetType=object';
	var targetId = 'targetId=' + originSelect.id;
	var originValue = 'originValue=' + originSelect.value;
	var subListId = 'subListId=' + targetSelect.id;
	var params = '';
	params = params + targetType + '&' + targetId + '&' + originValue + '&' + subListId;
	return params;
}
function showCcyByCardNo(cardNo, targetId) {
	if (isNaN(cardNo)) {
		$(targetId).innerHTML = '';
	} else {
		//registerElement(targetId);
		var url = '/cib/jsax?serviceName=CardCurrencyService&targetId=' + targetId + '&cardNo=' + cardNo;
		getMsgToElement(url, targetId, '', null,true,language);
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/templatePayment.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.bill_payment" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_tmp_edit" defaultvalue="MDB Corporate Online Banking > Pay Bills > FREQUENT PAYMENT LIST"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_tmp_edit" defaultvalue="Edit An Item To Frequent Payment List"/><!-- InstanceEndEditable --></td>
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
					<td><set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="template_edit" form="form1" file="bill_payment" />
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="fill_info" defaultvalue="Fill the following Information"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="payType" defaultvalue="payType"/></td>
                <td width="72%" class="content1"><input id="payType" name="payType" type="radio" value="<set:data name='payType1'/>" checked>
                  General Payment&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; <input id="payType" type="radio" name="payType" value="<set:data name='payType2'/>">
                  Credit Card Payment </td>
              </tr>
                <tr>
                  <td colspan="2"><div id="general" style="display:block">
                      <table width="100%" border="0" cellpadding="3" cellspacing="0">
                        <tr class="greyline">
                          <td width="28%" class="label1"><set:label name="category" defaultvalue="category"/></td>
                          <td width="72%" class="content1"><select name="category" id="category" nullable="0" onChange="showMerchantList(this, $('merchant'))" style="width:180px">
                              <option value="0" selected><set:label name="sel_category"/></option>
                              <set:rblist db="category"> <set:rboption name="category"/> </set:rblist>
                            </select>
                          </td>
                        </tr>
                        <tr>
                          <td class="label1"><set:label name="merchant" defaultvalue="merchant"/></td>
                          <td class="content1"><select name="merchant" id="merchant" nullable="0" style="width:180px">
                              <option value="0" selected><set:label name="sel_mer"/></option>
                            </select></td>
                        </tr>
                        <tr class="greyline">
                          <td class="label1"><set:label name="billNo" defaultvalue="billNo"/></td>
                          <td class="content1"><input name="billNo1" type="text" id="billNo1" value="<set:data name='billNo1'/>" maxlength="16">
                          </td>
                        </tr>
                        <tr>
                          <td class="label1"><set:label name="payment_account" defaultvalue="payment_account"/></td>
                          <td class="content1"><select name="fromAccount" id="fromAccount" nullable="0">
                              <option value="0" selected><set:label name="sel_debit_acc"/></option>
                              <set:rblist db="caoasaAccountByUser"> <set:rboption name="fromAccount"/> </set:rblist>
                            </select>
                          </td>
                        </tr>
                        <tr class="greyline">
                          <td class="label1"><set:label name="billName" defaultvalue="billName"/></td>
                          <td class="content1"><input name="billName" type="text" id="billName" value="<set:data name='billName'/>"></td>
                        </tr>
                        <tr>
                          <td class="label1"><set:label name="remark" defaultvalue="remark"/></td>
                          <td class="content1"><input name="remark" type="text" id="remark" value="<set:data name='remark'/>" size="50" maxlength="50"></td>
                        </tr>
                      </table>
                    </div>
                    <div id="card" style="display:none">
                      <table width="100%" border="0" cellpadding="3" cellspacing="0">
                        <tr class="greyline">
                          <td width="28%" class="label1"><set:label name="card_no" defaultvalue="card_no"/></td>
                          <td width="72%" class="content1">
						  	<input name="billNo1" type="text" disabled id="billNo1_2" onBlur="showCcyByCardNo(this.value, $('card_ccy').id);" value="<set:data name='billNo1'/>" maxlength="16">
                          </td>
                        </tr>
                        <tr>
                          <td class="label1"><set:label name="debit_account" defaultvalue="debit_account"/></td>
                          <td class="content1">
						  	<select name="fromAccount" id="fromAccount_2" nullable="0" disabled>
                              <option value="0" selected><set:label name="Select_Debit_Account_Label" defaultvalue="--Select A Debit Account No.--"/></option>
                              <set:rblist db="accountByUser"> <set:rboption name="fromAccount"/> </set:rblist>
                            </select>
                          </td>
                        </tr>
                        <tr class="greyline">
                          <td class="label1"><set:label name="payment_amount" defaultvalue="payment_amount"/></td>
                          <td class="content1"><span id="card_ccy"></span>
                            <input name="transferAmount" type="text" id="transferAmount_2" value="<set:data name='transferAmount'/>" disabled>
						  </td>
                        </tr>
                        <tr>
                          <td class="label1"><set:label name="remark" defaultvalue="remark"/></td>
                          <td class="content1">
						  	<input name="remark" type="text" disabled id="remark_2" value="<set:data name='remark'/>" size="50" maxlength="50">
						  </td>
                        </tr>
                      </table>
                    </div></td>
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
                  	<input name="cancel" id="cancel" type="button" value="&nbsp;<set:label name='cancel' defaultvalue='cancel'/>&nbsp;" onClick="doSubmit('cancel')">
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
