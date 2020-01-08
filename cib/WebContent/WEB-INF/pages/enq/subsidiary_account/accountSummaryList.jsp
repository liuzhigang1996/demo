<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.enq.account_enquiry">
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
<link rel="stylesheet" type="text/css" href="/cib/css/dtree.css">
<SCRIPT language=JavaScript src="/cib/javascript/dtree.js"></SCRIPT>
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	document.getElementById("showEqCcy").innerHTML = "<set:label name='Equivalent_In' defaultvalue='Equivalent in '/>&nbsp;" + toCcyDisplay("<set:data name='eqCcy' defaultvalue='MOP'/>");
	// add by hjs 20070107
	var flag = '<set:data name="flag"/>';
	var showNothing = <set:data name="showNothing"/>;
	$('eqCcy').value = 'MOP';
	if (flag == 'N' && showNothing == 0) {
		$('eqCcy').onchange();
	} else {
		$('eqCcy').disabled = true;
	}
}
function selectNode(arg){
	document.getElementById("corpId").value = arg;
	document.getElementById("form1").submit();
}
// add by hjs 20070107
function showEquivalent(toCcy){
	if ((toCcy != null) && (toCcy != 'none')) {
		var params = getParams(toCcy);
		if($('subTotal_CA')) $('subTotal_CA').innerHTML = 'Loading...';
		if($('subTotal_TD')) $('subTotal_TD').innerHTML = 'Loading...';
		if($('subTotal_OV')) $('subTotal_OV').innerHTML = 'Loading...';
		if($('subTotal_LA')) $('subTotal_LA').innerHTML = 'Loading...';
		if($('subTotal_SA')) $('subTotal_SA').innerHTML = 'Loading...';
		var url = '/cib/jsax?serviceName=EquivalentService&' + params + '&language=' + language;
		getMsgToElement(url, toCcy, '', function(){caculateTotal();},true,language );
		document.getElementById("showEqCcy").innerHTML = "<set:label name='Equivalent_In' defaultvalue='Equivalent in '/>&nbsp;" + toCcyDisplay(toCcy);
		//alert('send ok!');
	}
}
function toCcyDisplay(toCcy){
  if(toCcy =='MOP')
  return "<set:label name='MOP_LABEL' rb='app.cib.resource.bnk.corporation'/>";
  if(toCcy =='USD')
  return "<set:label name='USD_LABEL' rb='app.cib.resource.bnk.corporation'/>";
  if(toCcy =='HKD')
  return "<set:label name='HKD_LABEL' rb='app.cib.resource.bnk.corporation'/>";
  if(toCcy =='EUR')
  return "<set:label name='EUR_LABEL' rb='app.cib.resource.bnk.corporation'/>";  
}
function getParams(toCcy) {
	var div_element = null;
	var fromCcy_element = null;
	var fromAmt_element = null;
	var targetType = 'element';
	var targetId = '';
	var sFromCcy = '';
	var sFromAmt = '';
	var div_elements = document.getElementsByTagName('DIV');
	for(i=0; i<div_elements.length; i++){
		div_element = div_elements[i];
		if (div_element.id.substring(0,3) == 'equ') {
			fromCcy_element = $('fromCcy_' + div_element.id.substring(4));
			fromAmt_element = $('fromAmt_' + div_element.id.substring(4));
   			//registerElement(div_element.id);
			targetId += '&targetId=' + div_element.id;
			sFromCcy += '&fromCcy=' + $F(fromCcy_element);
			sFromAmt += '&fromAmt=' + $F(fromAmt_element);
			div_element.innerHTML = 'Loading...';
		}
	}
	targetType = 'targetType=' + targetType;
	toCcy = 'toCcy=' + toCcy;
	return targetType + '&' + toCcy + targetId + sFromCcy + sFromAmt;
}
function caculateTotal() {
	var params = getParams2('CA') + '&' +  getParams2('TD') + '&' +  getParams2('OV') + '&' +  getParams2('LA') + '&' +  getParams2('SA');
	var url = '/cib/jsax?serviceName=SubTotalCaculaterService&' + params;
	getMsgToElement(url, 'caculateTotal', '', null,true,language);
}
function getParams2(accType) {
	var div_element = null;
	var amount = '';
	var targetId = '';
	var keyWord = '';
	var div_elements = document.getElementsByTagName('DIV');
	targetId = 'subTotal_' + accType;
	keyWord = 'equ_' + accType;
	if($(targetId)) {
		for(i=0; i<div_elements.length; i++){
			div_element = div_elements[i];
			if (div_element.id.substring(0,6) == keyWord) {
				amount += '&amount' + accType + '=' + div_element.innerHTML;
   				//registerElement(targetId);
			}
		}
		targetId = 'targetId=' + targetId;
		return targetId + amount;
	} else {
		return '';
	}
}
</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/subsidiaryAcc.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.account_enquiry" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_sum" defaultvalue="BANK Online Banking > Account Enquiry > Current Account" rb="app.cib.resource.enq.subsidiary_account_enquiry"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_sum" defaultvalue="CURRENT ACCOUNT(S) ENQUIRY"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/subsidiaryAcc.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" --> <set:messages width="100%" cols="1"/>
              <table width="100%" border="0" cellspacing="0" cellpadding="3">
                <tr>
                  <td colspan="2" class="label1"><div class="dtree">
					<p><set:label name="corporation_structure" defaultvalue="corporation structure"/><a href="javascript: d.openAll();"><set:label name="view_all" defaultvalue="view all"/></a> <a href="javascript: d.closeAll();"><set:label name="close_all" defaultvalue="close all"/></a></p>
                    <script type="text/javascript">
						d = new dTree('d');
						if("Y" == "<set:data name='flag'/>"){
							d.clearCookie();
						}
						d.add("<set:data name='CORP_ID'/>","-1","<set:data name='CORP_NAME'/>(<set:data name='CORP_ID'/>)");
						<set:list name="corpList">
							d.add("<set:listdata name='CORP_ID'/>","<set:listdata name='PARENT_CORP'/>","<set:listdata name='CORP_NAME'/>(<set:listdata name='CORP_ID'/>)","javascript:selectNode('<set:listdata name='CORP_ID'/>');");
						</set:list>
						document.write(d);
					</script>
                    </div></td>
                </tr>
              </table>
              <set:if name="flag" condition="notequals" value="Y">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td height="40" valign="top" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0">
                      <%--<tr>
                        <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=csvList&fileName=corp_account_summary&columnTitles=<set:label name='Account_Type'/>,<set:label name='Original_Currency'/>,<set:label name='Amount'/>,,<set:label name='Equivalent_In'/>  <set:data name='eqCcy' defaultvalue='MOP'/>&columnNames=showAccType,CURRENCY_CODE||db@currencyCBS,TOTAL_BALANCE||format@amount,SUBTOTAL_TITLE,TOTAL_LCY_BALANCE||format@amount,SUBTOTAL||format@amount"><set:label name="download" rb="app.cib.resource.common.operation"/></a></td>
                      </tr>
                    --%></table></td>
                  <td align="right" valign="top" class="content1"><b> <set:label name="Equivalent_In" defaultvalue="Equivalent amount in: "/></b>
                    <select name="eqCcy" id="eqCcy" onChange="showEquivalent(this.value);">
                      <set:rblist file="app.cib.resource.enq.equivalent_ccy"> <set:rboption name="eqCcy"/> </set:rblist>
                    </select></td>
                </tr>
              </table>
              </set:if>
			  <set:if name="flag" condition="notequals" value="Y">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td class="listheader1">&nbsp;</td>
                  <td class="listheader1"><set:label name="Account_Type" defaultvalue="Account Type"/></td>
                  <td align="center" class="listheader1"><set:label name="Original_Currency" defaultvalue="Original Currency"/></td>
                  <td align="right" class="listheader1"><set:label name="Amount" defaultvalue="Amount"/></td>
                  <td align="right" class="listheader1">&nbsp;</td>
                  <td align="right" class="listheader1"><span id="showEqCcy"><set:label name="Equivalent_In" defaultvalue="Equivalent in "/>&nbsp;<set:data name="eqCcy" defaultvalue="MOP"/></span></td>
                  <td class="listheader1">&nbsp;</td>
                </tr>
                <set:if name="showCurrentAccount" condition="equals" value="1">
				<set:list name="currentList">
                <tr class="<set:listclass class1='' class2='greyline'/>">
                  <td class="listcontent1">&nbsp;</td>
                  <td align="left" class="listcontent1"><set:listif name='@INDEX'  condition='equals' value='0'><a onClick="toTargetFormgoToTarget('/cib/subsidiaryAcc.do?ActionMethod=listCurrentAccount&corpId=<set:data name='lastCorpId'/>')" href="#"><set:label name="Current_Account" defaultvalue="Current Account"/></a></set:listif></td>
                  <td align="center" class="listcontent1"><input name="fromCcy_CA_<set:listdata name='@INDEX'/>" type="hidden" id="fromCcy_CA_<set:listdata name='@INDEX'/>" value="<set:listdata name='CURRENCY_CODE' db='currencyCBS'/>">
                  <set:listdata name="CURRENCY_CODE" db="rcCurrencyCBS"/></td>
                  <td align="right" class="listcontent1"><input name="fromAmt_CA_<set:listdata name='@INDEX'/>" type="hidden" id="fromAmt_CA_<set:listdata name='@INDEX'/>" value="<set:listdata name='TOTAL_BALANCE'/>">
                  <set:listdata name="TOTAL_BALANCE" format="amount"/></td>
                  <td align="right" class="listcontent1">&nbsp;</td>
                  <td align="right" class="listcontent1"><div id="equ_CA_<set:listdata name='@INDEX'/>"></div></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
                </set:list>
                <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1" width="20%">&nbsp;</td>
                  <td class="listcontent1" width="20%">&nbsp;</td>
                  <td class="listcontent1" width="25%">&nbsp;</td>
                  <td class="listcontent1" width="10%" align="center"><strong><set:label name="Sub_Total" defaultvalue="Sub-Total"/></strong></td>
                  <td width="25%" align="right" class="listcontent1"><div id="subTotal_CA"><!--set:data name="CA_SubTotal" format="amount"/--></div></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
                </set:if>
				<set:if name="showNothing" condition="equals" value="1"> <set:list name="currentList" showNoRecord="YES"> </set:list> </set:if>
              </table>
              </set:if>
			  <set:if name="flag" condition="notequals" value="Y"> <set:if name="showTimeDeposit" condition="equals" value="1">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <set:list name="tdList">
                <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1" align="left"><set:listif name="@INDEX"  condition="equals" value="0"><a onClick="toTargetFormgoToTarget('/cib/subsidiaryAcc.do?ActionMethod=listTimeDeposit&corpId=<set:data name='lastCorpId'/>')" href="#"><set:label name="Time_Deposit" defaultvalue="Time Deposit"/></a></set:listif></td>
                  <td align="center" class="listcontent1"><input name="fromCcy_TD_<set:listdata name='@INDEX'/>" type="hidden" id="fromCcy_TD_<set:listdata name='@INDEX'/>" value="<set:listdata name='CURRENCY_CODE' db='currencyCBS'/>">
                  <set:listdata name="CURRENCY_CODE" db="rcCurrencyCBS"/></td>
                  <td align="right" class="listcontent1"><input name="fromAmt_TD_<set:listdata name='@INDEX'/>" type="hidden" id="fromAmt_TD_<set:listdata name='@INDEX'/>" value="<set:listdata name='TOTAL_BALANCE'/>">
                  <set:listdata name="TOTAL_BALANCE" format="amount"/></td>
                  <td align="right" class="listcontent1">&nbsp;</td>
                  <td align="right" class="listcontent1"><div id="equ_TD_<set:listdata name='@INDEX'/>"></div></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
                </set:list>
                <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1" width="20%">&nbsp;</td>
                  <td class="listcontent1" width="20%">&nbsp;</td>
                  <td class="listcontent1" width="25%">&nbsp;</td>
                  <td class="listcontent1" width="10%" align="center"><strong><set:label name="Sub_Total" defaultvalue="Sub-Total"/></strong></td>
                  <td width="25%" align="right" class="listcontent1"><div id="subTotal_TD"><!--set:data name="TD_SubTotal" format="amount"/--></div></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
              </table>
              </set:if>
			  </set:if>
			  
			  <set:if name="flag" condition="notequals" value="Y"> <set:if name="showSavingAccount" condition="equals" value="1">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <set:list name="savingList">
                <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1" align="left"><set:listif name="@INDEX"  condition="equals" value="0"><a onClick="toTargetFormgoToTarget('/cib/subsidiaryAcc.do?ActionMethod=listSavingAccount&corpId=<set:data name='lastCorpId'/>')" href="#"><set:label name="Saving_Account" defaultvalue="Loan Account"/></a></set:listif></td>
                  <td align="center" class="listcontent1"><input name="fromCcy_SA_<set:listdata name='@INDEX'/>" type="hidden" id="fromCcy_SA_<set:listdata name='@INDEX'/>" value="<set:listdata name='CURRENCY_CODE' db='currencyCBS'/>">
                  <set:listdata name="CURRENCY_CODE" db="rcCurrencyCBS"/></td>
                  <td align="right" class="listcontent1"><input name="fromAmt_SA_<set:listdata name='@INDEX'/>" type="hidden" id="fromAmt_SA_<set:listdata name='@INDEX'/>" value="<set:listdata name='TOTAL_BALANCE'/>">
                  <set:listdata name="TOTAL_BALANCE" format="amount"/></td>
                  <td align="right" class="listcontent1">&nbsp;</td>
                  <td align="right" class="listcontent1"><div id="equ_SA_<set:listdata name='@INDEX'/>"></div></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
                </set:list>
                <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1" width="20%">&nbsp;</td>
                  <td class="listcontent1" width="20%">&nbsp;</td>
                  <td class="listcontent1" width="25%">&nbsp;</td>
                  <td class="listcontent1" width="10%" align="center"><strong><set:label name="Sub_Total" defaultvalue="Sub-Total"/></strong></td>
                  <td width="25%" align="right" class="listcontent1"><div id="subTotal_SA"><!--set:data name="SA_SubTotal" format="amount"/--></div></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
              </table>
              </set:if>
			  </set:if>
			  
			  <set:if name="flag" condition="notequals" value="Y"> <set:if name="showOverdraft" condition="equals" value="1">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <set:if name="showOverdraft" condition="equals" value="1">
                <set:list name="overdraftList">
                <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1" align="left"><set:listif name="@INDEX"  condition="equals" value="0"><a onClick="toTargetFormgoToTarget('/cib/subsidiaryAcc.do?ActionMethod=listOverdraftAccount&corpId=<set:data name='lastCorpId'/>')" href="#"><set:label name="Overdraft_Account" defaultvalue="Overdraft Account"/></a></set:listif></td>
                  <td align="center" class="listcontent1"><input name="fromCcy_OV_<set:listdata name='@INDEX'/>" type="hidden" id="fromCcy_OV_<set:listdata name='@INDEX'/>" value="<set:listdata name='CURRENCY_CODE' db='currencyCBS'/>">
                  <set:listdata name="CURRENCY_CODE" db="rcCurrencyCBS"/></td>
                  <td align="right" class="listcontent1"><input name="fromAmt_OV_<set:listdata name='@INDEX'/>" type="hidden" id="fromAmt_OV_<set:listdata name='@INDEX'/>" value="<set:listdata name='TOTAL_BALANCE'/>">
                  <set:listdata name="TOTAL_BALANCE" format="amount"/></td>
                  <td align="right" class="listcontent1">&nbsp;</td>
                  <td align="right" class="listcontent1"><div id="equ_OV_<set:listdata name='@INDEX'/>"></div></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
                </set:list>
                <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1" width="20%">&nbsp;</td>
                  <td class="listcontent1" width="20%">&nbsp;</td>
                  <td class="listcontent1" width="25%">&nbsp;</td>
                  <td class="listcontent1" width="10%" align="center"><strong><set:label name="Sub_Total" defaultvalue="Sub-Total"/></strong></td>
                  <td width="25%" align="right" class="listcontent1"><div id="subTotal_OV"><!--set:data name="OA_SubTotal" format="amount"/--></div></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
                </set:if>
              </table>
              </set:if>
			  </set:if>
			  <set:if name="flag" condition="notequals" value="Y"> <set:if name="showLoanAccount" condition="equals" value="1">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <set:list name="loanList">
                <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1" align="left"><set:listif name="@INDEX"  condition="equals" value="0"><a onClick="toTargetFormgoToTarget('/cib/subsidiaryAcc.do?ActionMethod=listLoanAccount&corpId=<set:data name='lastCorpId'/>')" href="#"><set:label name="Loan_Account" defaultvalue="Loan Account"/></a></set:listif></td>
                  <td align="center" class="listcontent1"><input name="fromCcy_LA_<set:listdata name='@INDEX'/>" type="hidden" id="fromCcy_LA_<set:listdata name='@INDEX'/>" value="<set:listdata name='CURRENCY_CODE' db='currencyCBS'/>">
                  <set:listdata name="CURRENCY_CODE" db="rcCurrencyCBS"/></td>
                  <td align="right" class="listcontent1"><input name="fromAmt_LA_<set:listdata name='@INDEX'/>" type="hidden" id="fromAmt_LA_<set:listdata name='@INDEX'/>" value="<set:listdata name='TOTAL_BALANCE'/>">
                  <set:listdata name="TOTAL_BALANCE" format="amount"/></td>
                  <td align="right" class="listcontent1">&nbsp;</td>
                  <td align="right" class="listcontent1"><div id="equ_LA_<set:listdata name='@INDEX'/>"></div></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
                </set:list>
                <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1" width="20%">&nbsp;</td>
                  <td class="listcontent1" width="20%">&nbsp;</td>
                  <td class="listcontent1" width="25%">&nbsp;</td>
                  <td class="listcontent1" width="10%" align="center"><strong><set:label name="Sub_Total" defaultvalue="Sub-Total"/></strong></td>
                  <td width="25%" align="right" class="listcontent1"><div id="subTotal_LA"><!--set:data name="LA_SubTotal" format="amount"/--></div></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
              </table>
              </set:if>
			  </set:if> 
			  
              
              <!-- add by Li_zd 2016-08-18 end -->
              <set:if name="flag" condition="notequals" value="Y">
              <set:if name="showCreditAccount" condition="equals" value="1">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <set:list name="creditList">
                <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1" align="left"><set:listif name="@INDEX"  condition="equals" value="0"><a onClick="toTargetFormgoToTarget('/cib/subsidiaryAcc.do?ActionMethod=listCreditAccount&corpId=<set:data name='lastCorpId'/>')" href="#"><set:label name="Credit_Account" defaultvalue="Credit Account"/></a></set:listif></td>
                  <td align="center" class="listcontent1"><input name="fromCcy_CC_<set:listdata name='@INDEX'/>" type="hidden" id="fromCcy_CC_<set:listdata name='@INDEX'/>" value="<set:listdata name='CURRENCY_CODE' db='currencyCBS'/>">
                  <set:listdata name="CURRENCY_CODE" db="rcCurrencyCBS"/></td>
                  <td align="right" class="listcontent1"><input name="fromAmt_CC_<set:listdata name='@INDEX'/>" type="hidden" id="fromAmt_CC_<set:listdata name='@INDEX'/>" value="<set:listdata name='TOTAL_BALANCE'/>">
                  <set:listdata name="TOTAL_BALANCE" format="amount"/></td>
                  <td align="right" class="listcontent1">&nbsp;</td>
                  <td align="right" class="listcontent1"><div id="equ_CC_<set:listdata name='@INDEX'/>"><!--set:listdata name="TOTAL_LCY_BALANCE" format="amount"/--></div></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
                </set:list>
                <tr class="<set:listclass class1='' class2='greyline' inlevel='@CONTINUE'/>">
                  <td class="listcontent1">&nbsp;</td>
                  <td class="listcontent1" width="20%">&nbsp;</td>
                  <td class="listcontent1" width="20%">&nbsp;</td>
                  <td class="listcontent1" width="25%">&nbsp;</td>
                  <td align="center" class="listcontent1" width="10%"><strong><set:label name="Sub_Total" defaultvalue="Sub-Total"/></strong></td>
                  <td width="25%" align="right" class="listcontent1"><div id="subTotal_CC"><!--set:data name="CC_SubTotal" format="amount"/--></div></td>
                  <td class="listcontent1">&nbsp;</td>
                </tr>
              </table>
              </set:if>
              <set:if name="flag" condition="notequals" value="Y">
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="groupseperator">&nbsp;</td>
                </tr>
              </table>
              </set:if>
              </set:if>
              
              <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                  <td class="sectionbutton"><input id="ActionMethod" name="ActionMethod" type="hidden" value="listAccountSummary">
                    <input id="corpId" name="corpId" type="hidden" >
                    <input id="lastCorpId" name="lastCorpId" type="hidden" value="<set:data name='lastCorpId'/>"></td>
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