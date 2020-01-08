<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri='/WEB-INF/neturbo.tld' prefix='set' %>
<html><!-- InstanceBegin template="/Templates/normal.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
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
<SCRIPT language=JavaScript src="/cib/javascript/messages.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/fieldcheck.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/prototype.js"></SCRIPT>
<SCRIPT language=JavaScript src="/cib/javascript/jsax2.js"></SCRIPT>
<!-- InstanceBeginEditable name="javascirpt" -->
<script language=JavaScript>
var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	if ('<set:data name="viewOnly"/>' == 'Y') {
		document.getElementById('fromAccount').disabled = true;
		document.getElementById('ok').style.display = 'none';
	}
	//add by lzg GAPMC-EB-001-0050
	var currentAccount = document.getElementById("currentAccount");
	var currentAccountCcy = document.getElementById("currentAccountCcy");
	if(!"<set:data name='currentAccount' />" == ""){
		for(var i=0;i<currentAccount.options.length;i++){
		    if(currentAccount[i].value=='<set:data name='currentAccount' />'){
		        currentAccount[i].selected=true;
		        showFromCcyList(currentAccount,currentAccountCcy,'getFromCcy','<set:data name='currentAccount' />');
	    	}  
		}  
	}
	if(!"<set:data name='currentAccountCcy' />" == ""){
		for(var i=0;i<currentAccountCcy.options.length;i++){
		    if(currentAccountCcy[i].value=='<set:data name='currentAccountCcy' />'){
		        currentAccountCcy[i].selected=true;
	    	}  
		}
		changeFromCcy("<set:data name='currentAccountCcy' />");
	}
	//add by lzg end
}
function doSubmit(arg) {
	if(validate_withdrawal(document.getElementById("form1"))){
		if (arg == 'ok') {
			document.form1.ActionMethod.value = 'withdrawTimeDeposit';
		} 
		document.form1.submit();
		setFormDisabled("form1");
		//setFieldEnabled("buttonReturn");
	}
}
//add by linrui 20190319
function getCurrency(){
	//currentAccount
	var debitAccount = document.getElementById("currentAccount");
	var accountCcy = debitAccount.options[debitAccount.selectedIndex].innerHTML;
	var arr = accountCcy.split("-");
	accountCcy = arr[1].substring(1,4);
	document.getElementById("AccountCcy").value = accountCcy;
}
//end

//add by lzg for GAPMC-EB-001-0050
function getParams(originSelect, targetSelect) {
	var targetType = 'targetType=object';
	var targetId = 'targetId=' + originSelect.id;
	var originValue = 'originValue=' + originSelect.value;
	var subListId = 'subListId=' + targetSelect.id;

	var params = '';
	params = params + targetType + '&' + targetId + '&' + originValue + '&' + subListId;
	return params;
}
//get ccy list from fromacct
function showFromCcyList(originSelect, targetSelect, action, fromAcc){
	//clearAll();
	//action check ccy
	document.getElementById("currentAccount").value= fromAcc;
	if ((originSelect.value != null) && (originSelect.value != '')) {
		   var params = getParams(originSelect, targetSelect);
		   var url = '/cib/jsax?serviceName=AccInTxnService&action='+action+'&'+ params+ '&showFromAcc=' + fromAcc +'&language='+language;
		   getMsgToSelect(url,'', null, false,language);
	}
	
	//add by lzg 20190612
	var currentAccountCcy = document.getElementById("currentAccountCcy");
	if(currentAccountCcy.options.length == 2){
		currentAccountCcy[1].selected = true;
		changeFromCcy(currentAccountCcy[1].value);
	}
	//add by lzg end
}

function changeFromCcy(fromCcy){
	document.getElementById("AccountCcy").value= fromCcy;
}
//add by lzg end

</script>
<!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/timeDeposit.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.txn.time_deposit" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" --><set:label name="navigationTitle_withdraw" defaultvalue="MDB Corporate Online Banking > Time Deposit > TIME DEPOSIT WITHDRAWAL"/><!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" --><set:label name="functionTitle_withdraw" defaultvalue="TIME DEPOSIT WITHDRAWAL"/><!-- InstanceEndEditable --></td>
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
					<td>
						<set:messages width="100%" cols="1" align="center"/>
						<set:fieldcheck name="withdrawal" form="form1" file="time_deposit" />
					</td>
				</tr>
			</table>
            <table width="100%" border="0" cellspacing="0" cellpadding="3">
              <tr>
                <td colspan="2" class="groupinput"><set:label name="withdraw_info" defaultvalue="withdraw_info"/></td>
              </tr>
              <tr>
                <td width="28%" class="label1"><span>&nbsp;&nbsp;</span><set:label name='time_deposit_No' defaultvalue="time_deposit_No"/></td>
                <td width="72%" class="content1"><set:data name="accountNo" db='tdAccountByUser'/>(<set:data name="period"/>)</td>
              </tr>
              <tr class="greyline">
                <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="principal" defaultvalue="principal"/></td>
                <td class="content1"><set:data name="currency" db="rcCurrencyCBS"/>  <set:data name="PRINCIPAL" format="amount"/></td>
              </tr>
              <!-- add by lzg 20190718 -->
              <tr>
                <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="period" defaultvalue="Period"/></td>
                <td class="content1"><set:data name="period" db="period"/></td>
              </tr>
              <!-- add by lzg end -->
              <tr class="greyline">
                <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="maturity_date" defaultvalue="maturity_date"/></td>
                <td class="content1"><set:data name="maturityDate" format='date'/></td>
              </tr>
              <tr >
                <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="interest_rate" defaultvalue="interest_rate"/></td>
                <td class="content1">  <set:data name="interest" format="percent"/></td>
              </tr>
              <!-- add by lzg 20190610 -->
              <tr class="greyline">
                <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="interest_accrued" defaultvalue="interest_accrued"/></td>
                <td class="content1"> <set:data name="currency" db="rcCurrencyCBS"/> <set:data name="netCreditAmount" format='amount'/></td>
              </tr>
              <!-- add by lzg 20190718 -->
              <tr>
                <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="value_date" defaultvalue="Value Date"/></td>
                <td class="content1"><set:data name="valueDate" format='date'/></td>
              </tr>
              <!-- add by lzg end -->
              <!-- add by lzg end -->
              <!--<tr class="greyline">
                <td class="label1"><span>&nbsp;&nbsp;</span><set:label name="penalty_for_early_withdrawal" defaultvalue="penalty_for_early_withdrawal"/></td>
                <td class="content1"><set:data name="currency" db="rcCurrencyCBS"/>  <font color="#FF0000">-<set:data name='penalty' format="amount"/></font></td>
              </tr>
              --><%--
              <tr class="greyline">
                <td class="label1"><set:label name="net_interest_amount" defaultvalue="net_interest_amount"/></td>
                <td class="content1"><set:data name="currency" db="rcCurrencyCBS"/>  <set:data name="netInterestAmt" format="amount"/></td>
              </tr>
              --%>
              
              <tr class="greyline">
                <td class="label1"><span class = "xing">*</span><set:label name="credit_ccount" defaultvalue="credit_ccount"/></td>
                <td class="content1">
				<%--<select name="currentAccount" id="fromAccount" nullable="0">
					<option value="0" selected><set:label name="sel_credit_acc"/></option>
                  	<set:rblist db="caoasaAccountByUser"> <set:rboption name="currentAccount"/> </set:rblist>
                </select>--%>
                
                <!-- modified by lzg for GAPMC-EB-001-0050 --><!--
                
                <select name="currentAccount" id="currentAccount" nullable="0" onChange="getCurrency()">
					<option value="0" selected><set:label name="sel_debit_acc"/></option>
                    <set:list name="transferuser"> <option value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_INFO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_INFO' />
                      </option>
                  </set:list>
                </select>
                </td>
              </tr>
            -->
            <select id="currentAccount" name="currentAccount" nullable="0" onChange="showFromCcyList(this, $('currentAccountCcy'),'getFromCcy', this.value);">
				<option selected value="" selected><set:label name="sel_credit_acc" defaultvalue="--Select Credit Account--"/></option>
                  <set:list name="transferuser"> <option  value="<set:listdata  name='ACCOUNT_NO' />" 
                          <set:listselected key='ACCOUNT_NO' equals='selectFromAcct'  output='selected'/> > <set:listdata  name='ACCOUNT_NO' />
                      </option>
                  </set:list>
                  </select>
                  </td>
              </tr>
              <tr >
                  <td class="label1"><span class = "xing">*</span><set:label name="Credit_Currency" defaultvalue="Credit Currency"/></td>
                  <td class="content1" colspan="3" ><table><tr><td><select name="currentAccountCcy" id="currentAccountCcy" nullable="0"  onChange="changeFromCcy(this.value);">
                      <option  value="" selected><set:label name="sel_ccy" defaultvalue="--Select a Currency--"/></option>
                    </select>  </td></tr></table>
                  </td>
               </tr>
              <!-- modified by lzg end -->
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="groupseperator">&nbsp;</td>
              </tr>
            </table>
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td height="40" colspan="2" class="sectionbutton">
					<input name="ok" id="ok" type="button" value="&nbsp;&nbsp;<set:label name='ok' defaultvalue=' ok '/>&nbsp;&nbsp;" onClick="doSubmit('ok')">
				  	<input id="ActionMethod" name="ActionMethod" type="hidden" value="ok">
				  	<input id="AccountCcy" name="AccountCcy" type="hidden" value="">
				  	<input id="currency" name="currency" type="hidden" value="<set:data name="currency"/>">
				  	<input id="accountNo" name="accountNo" type="hidden" value="<set:data name="accountNo"/>">
				  	<set:singlesubmit/>
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
