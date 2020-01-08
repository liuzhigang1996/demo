<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normallist.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<set:loadrb file="app.cib.resource.txn.time_deposit">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<!-- InstanceBeginEditable name="doctitle" -->
<title>Corporation Banking</title>
<!-- InstanceEndEditable -->
<link rel="stylesheet" type="text/css" href="/cib/css/page.css">
<SCRIPT language=JavaScript src="/cib/javascript/common.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/common1.js?v=20130117"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<script language=JavaScript>
// add by li_qun at 20171121 for mul-language
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	var listsize = 0;
	<set:list name="timeDepositList">
		listsize ++ ;
	</set:list>
	$('ccy').value = 'MOP';
	if (listsize > 0) {
		$('ccy').onchange();
	} else {
		$('ccy').disabled = true;
		$('sortType').disabled = true;
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

function doSubmit(arg) {
		//document.getElementById("submit1").disabled=true;
	if (arg == 'withdraw') {
		if(validate_withdrawal_list(document.getElementById("form1"))){
			document.form1.ActionMethod.value = 'withdrawTimeDepositLoad';
			document.getElementById("form1").submit();
			setFormDisabled("form1");
			//setFieldEnabled("buttonReturn");
		}
	} else if (arg == 'list') {
		document.form1.ActionMethod.value = 'listTimeDepositWithdrawal';
		document.getElementById("form1").submit();
		setFormDisabled("form1");
		//setFieldEnabled("buttonReturn");
	}
}
function showCcy(toCcy){
	if ((toCcy != null) && (toCcy != 'none')) {
		$('ccySpan').innerHTML = toCcy;
	}
}
// Model 2
function showEquivalent(toCcy){
	if ((toCcy != null) && (toCcy != 'none')) {
		var params = getParams(toCcy);
		var url = '/cib/jsax?serviceName=EquivalentService&' + params + '&language=' + language;//mod by li_qun at 20171121 for mul-language
		getMsgToElement(url, toCcy, '', null,true,language);
		//alert('send ok!');
	}
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


function chanPage(flagPage,arg){
	document.getElementById("flagPage").value=flagPage;
	if (arg == 'list') {
		document.form1.ActionMethod.value = 'listTimeDepositWithdrawal';
	}
	document.getElementById("form1").submit();
	setFormDisabled("form1");
}

</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/timeDeposit.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.time_deposit" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_withdraw" defaultvalue="MDB Corporate Online Banking > Time Deposit >  "/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_withdraw" defaultvalue="FREQUENT PAYMENT LIST"/><!-- InstanceEndEditable --></td>
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
		  <form action="/cib/timeDeposit.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="withdrawal_list" form="form1" file="time_deposit" />
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
			  <tr>
			  	<td colspan="5"><set:label name='notice' defaultvalue="notice"/></td>
			  	<td colspan="2" width="14%" align="right">
					<b><set:label name="equivalent_in" defaultvalue="Equivalent amount in: "/></b>
                        <select name="ccy" id="ccy" onChange="showEquivalent(this.value);$('ccySpan2').innerHTML=toCcyDisplay(this.value)">
                          <!--<option value="none">--Select Currency--</option> -->
                      		<set:rblist file="app.cib.resource.enq.equivalent_ccy">
                        		<set:rboption name="eqCcy"/>
                      		</set:rblist>
                        </select>
				</td>
			  </tr>
			  <%--
			  <tr>
			  	<td colspan="7"><div align="left">
					<b><set:label name="sort_type"/>:</b>
                        <select name="sortType" id="sortType" onChange="doSubmit('list');this.disabled=true;$('ccy').disabled=true;">
                      		<set:rblist file="app.cib.resource.txn.td_sort_ype">
                        		<set:rboption name="sortType"/>
                      		</set:rblist>
                        </select>
				</div></td>
			  </tr>
              --%>
              <tr>
                <td class="listheader1"><div align="center"></div></td>
                <td colspan="2" class="listheader1"><div align="left"><set:label name="account_no" defaultvalue="account_no"/></div></td>
                <td class="listheader1"><div align="left"><set:label name="currency" defaultvalue="currency"/></div></td>
                <%--modify by wcc 20180323 <td width="17%" class="listheader1"><div align="left"><set:label name="maturity_date" defaultvalue="maturity_date"/></div></td>--%>
                <%--modify by wcc 20180323 <td width="15%" class="listheader1"><div align="center"><set:label name="currency" defaultvalue="currency"/></div></td>--%>
                <td class="listheader1"><div align="right"><set:label name="principal" defaultvalue="principal"/></div></td>
                <td colspan="2" class="listheader1"><div align="right"><set:label name="equivalent_in" defaultvalue="equivalent_in"/>&nbsp;<span id="ccySpan2">MOP</span></div></td>
              </tr>
              <!-- if possible please reference  timeDepositList.jsp-->
			  <set:list name="timeDepositList" showNoRecord="YES">
              <tr class="<set:listclass class1='' class2='greyline'/>">
                <td class="listcontent1"><set:listradio name="accountNo" value="ACCOUNT_NO" text=""/>
                  <input name="fromCcy_<set:listdata name='ACCOUNT_NO'/>" type="hidden" id="fromCcy_<set:listdata name='ACCOUNT_NO'/>" value="<set:listdata name='CURRENCY_CODE' db='currencyCBS'/>">
                </td>
                <td colspan="2" class="listcontent1"><div align="left"><a onClick="postToMainFrame('/cib/timeDeposit.do?ActionMethod=viewTimeDepositforWithdrawl',{tdAccountNo:'<set:listdata name='ACCOUNT_NO'/>'})" href="#"><set:listdata name="ACCOUNT_NO"/></a></div></td>
                <%--modify by wcc 20180323 <td class="listcontent1"><div id="1" align="left"><set:listdata name="period" db="period"/></div></td>--%>
                <%--modify by wcc 20180323 <td class="listcontent1"><div align="left"><set:listdata name="MATURITY_DATE" format='date'/></div></td>--%>
                <td class="listcontent1"><div align="center"><set:listdata name="currency" db="rcCurrencyCBS"/></div></td>
                <td class="listcontent1"><div align="right"><set:listdata name="PRINCIPAL_AMOUNT" format="amount"/><input name="fromAmt_<set:listdata name='ACCOUNT_NO'/>" type="hidden" id="fromAmt_<set:listdata name='ACCOUNT_NO'/>" value="<set:listdata name='PRINCIPAL_AMOUNT'/>"></div></td>
                <td colspan="2" class="listcontent1"><div align="right" id="equ_<set:listdata name='ACCOUNT_NO'/>">&nbsp;</div></td>
              </tr>
			  </set:list>
			  
            </table>
            
            <!-- add by linrui for class style -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
				  	<tr>
				  	<td align="right" class="pageline">
					<set:if name='disableP' condition='EQUALS' value='button'>
                    <span class="pagelink">
					<a href="#" onClick="chanPage('P','list')">&lt;&lt;<set:label name='previous_page' rb='app.cib.resource.common.operation'/></a>
					</span> | 
					</set:if>
					<set:if name='disableP' condition='EQUALS' value='hidden'>
					<span class="pagedisabled">&lt;&lt;<set:label name='previous_page' rb='app.cib.resource.common.operation'/></span> | 
					</set:if>
					<set:if name='disableN' condition='EQUALS' value='button'>
					<span class="pagelink">
					<a href="#" onClick="chanPage('N','list')"><set:label name='next_page' rb='app.cib.resource.common.operation'/>&gt;&gt;</a>
					</span>
					</set:if>
					<set:if name='disableN' condition='EQUALS' value='hidden'>
				  	<span class="pagedisabled"><set:label name='next_page' rb='app.cib.resource.common.operation'/>&gt;&gt;</span>
					</set:if>
				  	</td>
				  	</tr>
              </table>
              <!-- add by linrui end -->
            
            <%--<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            --%>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
				<input id="pay" name="pay" type="button" value="<set:label name='withdraw_time_deposit' defaultvalue='withdraw_time_deposit'/>" onClick="doSubmit('withdraw')">
				<input id="ActionMethod" name="ActionMethod" type="hidden" value="withdraw">
				<input type="hidden" name="prePage" id="prePage" value="<set:data name='prePage' />">
			  	<input type="hidden" name="nextPage" id="nextPage" value="<set:data name='nextPage' />">
			  	<input type="hidden" name="flagPage" id="flagPage" value="<set:data name='flagPage' />">
			  	<input type="hidden" name="keyAll" id="keyAll" value="<set:data name='keyAll' />"> 
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
