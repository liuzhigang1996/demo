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
	$('billNo').style.display = 'none';
	$('otherBillNo').disabled = true;
	var cgr = $F('category');
	if ((cgr!="") && (cgr!="0")){
		var params = getParams($('category'), $('merchant'));
		var url = '/cib/jsax?serviceName=MerchantListService&' + params +'&language='+language;
		getMsgToSelect(
			url,
			'',
			function() {
				$('merchant').value = '<set:data name="merchant"/>';
				if ($('merchant').value == '<set:data name="merchant"/>') {
					loadBillNoList($('merchant'), $('billNo1'));										
				}
			},true,language
		);
	}
}
function loadBillNoList(originSelect, targetSelect){
	var params = getParams(originSelect, targetSelect);
	var url = '/cib/jsax?serviceName=BillNoListService&' + params +'&language='+language;
	getMsgToSelect(
		url,
		'', 
		function(){
			if($('billNo1')){
				$('billNo1').value = '<set:data name="billNo1" />';
				if ($F('billNo1') == '<set:data name="billNo1" />') {
					varBillNo();
					getBillName($('billNo1').value);
					if($('otherBillNo')){
						$('otherBillNo').value = '<set:data name="otherBillNo" />';
					}
				}
			}
		},true,language
	);
}
function doSubmit(arg) {
	if(validate_general_payment_check(document.getElementById("form1"))){
		if (arg == 'ok') {
			document.form1.ActionMethod.value = 'generalPaymentCheck';
		} 
		document.form1.submit();
		setFormDisabled("form1");
		//setFieldEnabled("cancel");
	}
}
function showMerchantList(originSelect, targetSelect){
	var params = getParams(originSelect, targetSelect);
	var url = '/cib/jsax?serviceName=MerchantListService&' + params +'&language='+language;
	getMsgToSelect(url,'', 
		function() {
			$('billNo1').options.length=0;
			var newOpt=new Option('<set:label name="Select_Bill_Number" rb="app.cib.resource.common.operation"/>', 0);
			if (navigator.userAgent.indexOf("MSIE") != -1) {
				$('billNo1').add(newOpt);
			} else {
				$('billNo1').appendChild(newOpt);
			}
			varBillNo();
		},true,language
	);
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
function showBillNoList(originSelect, targetSelect){
	var params = getParams(originSelect, targetSelect);
	var url = '/cib/jsax?serviceName=BillNoListService&' + params +'&language='+language;
	getMsgToSelect(url,'', initBillNo,false,language);
}
function initBillNo(){
	varBillNo();
}
function varBillNo(){
	var billNoValue = $F('billNo1');
	$('otherBillNo').value = '';
	if (billNoValue == '0') {
		$('billNo').style.display = 'none';
		$('otherBillNo').disabled = true;
	} else if (billNoValue == '9') {
		$('billNo').style.display = 'block';
		$('otherBillNo').disabled = false;
	} else  {
		$('billNo').style.display = 'none';
		$('otherBillNo').disabled = true;
	}
}
function getBillName(val) {
	if (val != '9') {
		var opts = $('billNo1').options;
		var opt = null;
		for (i=1; i<opts.length; i++) {
			opt = opts[i];
			if (opt.value == val) {
				idx = opt.text.indexOf('-');
				if (idx==-1) {
					$('billName').value = opt.text.substring(idx + 1);
				} else {
					$('billName').value = opt.text.substring(idx + 2);
				}
			}
		}
	} else {
		$('billName').value = '';
	}
}
var websiteUpdator = Class.create();
websiteUpdator.prototype = {
	initialize: function() {
		this.website = '';
		this.noSiteInfo = '<set:label name="no_website" defaultvalue="no_website"/>';
	},
	ajaxUpdate: function(ajaxResponse){
		website = RicoUtil.getContentAsString(ajaxResponse);
		if (website != '') {
			popUpWindow(website, 0, 0, screen.width, screen.height);
		} else {
			alert(this.noSiteInfo);
		}
	}
}
function linkToMerWebSite(merchant) {
	if (merchant.value == '0') {
		alert('<set:label name="no_merchant_selected" defaultvalue="no_merchant_selected"/>');
		merchant.focus();
	} else {
		var url = '/cib/jsax?serviceName=MerchantWebsiteService&targetId=linkToWebsite&merchant=' + merchant.value +'&language='+language;
		var wUpdater = new websiteUpdator();
		doUpdateByObj(url, '', 'linkToWebsite', wUpdater);
	}
}
var popUpWin=0;
function popUpWindow(URLStr, left, top, width, height)
{
  if(popUpWin)
  {
    if(!popUpWin.closed) popUpWin.close();
  }
  popUpWin = open(URLStr, 'popUpWin', 'toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbar=yes,resizable=no,copyhistory=yes,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/billPayment.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.bill_payment" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_general" defaultvalue="MDB Corporate Online Banking > Pay Bills > General Payment"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_general" defaultvalue="GENERAL PAYMENT"/><!-- InstanceEndEditable --></td>
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
						<set:fieldcheck name="general_payment_check" form="form1" file="bill_payment" />
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3" id="round">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="select_info" defaultvalue="Select the Payment Bill"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><set:label name="category" defaultvalue="category"/></td>
                <td width="72%" class="content1">
					<select name="category" id="category" nullable="0" onChange="showMerchantList(this, $('merchant'));" style="width:180px">
						<option value="0" selected><set:label name="sel_category"/></option>
                      	<set:rblist db="category">
							<set:rboption name="category"/>
						</set:rblist>
                    </select>
				</td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="merchant" defaultvalue="merchant"/></td>
                <td class="content1">
					<select name="merchant" id="merchant" nullable="0" onChange="showBillNoList(this, $('billNo1'));" style="width:180px" >
						<option value="0" selected><set:label name="sel_mer"/></option>
                    </select>
				</td>
              </tr>
              <tr>
                <td class="label1">&nbsp;</td>
                <td class="content1">
				<script language="javascript">
					var merWebsite = '<set:label name="merchant_website" defaultvalue="merchant_website"/>';
					var noWebsite = '<set:label name="no_website" defaultvalue="no_website"/>';
				</script>
					<a id="linkToWebsite" href="#" onClick="linkToMerWebSite($('merchant'));">
						<set:label name="merchant_website" defaultvalue="merchant_website"/>
					</a>
				</td>
              </tr>
              <tr class="greyline">
                <td class="label1"><set:label name="billNo" defaultvalue="billNo"/></td>
                <td class="content1">
					<input name="billName" type="hidden" id="billName" value="">
					<select name="billNo1" id="billNo1" nullable="0" onChange="varBillNo();getBillName(this.value);" style="width:180px ">
						<option value="0" selected><set:label name="sel_bill_no"/></option>
               	  	</select>
					<div id="billNo" style="display:none"><input type="text" name="otherBillNo" id="otherBillNo" maxlength="16"></div>
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
			<script language="javascript">
			</script>
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
