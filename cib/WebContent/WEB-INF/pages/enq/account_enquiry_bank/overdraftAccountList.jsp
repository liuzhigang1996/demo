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
  <script language=JavaScript>
  var language ="<%=session.getAttribute("Locale$Of$Neturbo")%>";
function pageLoad(){
	document.getElementById("showEqCcy").innerHTML = "<set:label name='Equivalent_In' defaultvalue='Equivalent in '/>&nbsp;" + toCcyDisplay("<set:data name='eqCcy' defaultvalue='MOP'/>");
	// add by hjs
	var listsize = 0;
	<set:list name="overdraftAccountList">
		listsize ++ ;
	</set:list>
	$('eqCcy').value = 'HKD';
	if (listsize > 0) {
		$('eqCcy').onchange();
	} else {
		$('eqCcy').disabled = true;
	}
}
// add by hjs
function showEquivalent(toCcy){
	if ((toCcy != null) && (toCcy != 'none')) {
		var params = getParams(toCcy);
		//modified by xyf 20090902
		//var url = '/cib/jsax?serviceName=EquivalentService&corpId=<set:data name="corpId"/>&' + params + '&language=' + language;
		var tempId = escape('<set:data name="corpId"/>');
		var url = '/cib/jsax?serviceName=EquivalentService&corpId=' + tempId + '&' + params + '&language=' + language;
		getMsgToElement(url, toCcy, '', null,true,language);
		document.getElementById("showEqCcy").innerHTML = "<set:label name='Equivalent_In' defaultvalue='Equivalent in '/>&nbsp;" + toCcyDisplay(toCcy);
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
function changeDoc(arg) {
	if (arg == 'sum') {
		window.location.href='/cib/accEnquiryBank.do?ActionMethod=listAccountSummary';
	} else if (arg == 'ca') {
		window.location.href='/cib/accEnquiryBank.do?ActionMethod=listCurrentAccount';
	} else if (arg == 'td') {
		window.location.href='/cib/accEnquiryBank.do?ActionMethod=listTimeDeposit';
	} else if (arg == 'oa') {
		window.location.href='/cib/accEnquiryBank.do?ActionMethod=listOverdraftAccount';
	} else if (arg == 'la') {
		window.location.href='/cib/accEnquiryBank.do?ActionMethod=listLoanAccount';
	} else if (arg == 'sa') {
		window.location.href='/cib/accEnquiryBank.do?ActionMethod=listSavingAccount';
	} else if (arg == 'out') {
		window.location.href='/cib/approve.do?ActionMethod=statusEnquiryBank&procStatus=P';
	} else if (arg == 'cc') {
	    window.location.href='/cib/accEnquiryBank.do?ActionMethod=listCreditAccount';
	  }
	setFormDisabled("form1");
}
</script>
  <!-- InstanceEndEditable --><!-- InstanceParam name="page_action" type="text" value="/cib/accEnquiryBank.do" --><!-- InstanceParam name="help_href" type="text" value="#" --><!-- InstanceParam name="resource_file" type="text" value="app.cib.resource.enq.account_enquiry" -->
</head>
<body class="body1" onLoad="pageLoad();">
<div name="pageheader" id="pageheader">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" height="18" class="navigationbar"><!-- InstanceBeginEditable name="section_navbar" -->
          <set:label name="navigationTitle_oa_bank" defaultvalue="BANK Online Banking > Account Enquiry > Overdraft Account"/>
          <!-- InstanceEndEditable --></td>
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
    <td class="title1" nowrap><!-- InstanceBeginEditable name="section_title" -->
        <set:label name="functionTitle_oa" defaultvalue="OVERDRAFT ACCOUNT(S) ENQUIRY"/>
        <!-- InstanceEndEditable --></td>
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
		  <form action="/cib/accEnquiryBank.do" method="post" name="form1" id="form1">
		    <!-- InstanceBeginEditable name="sectioncontent" -->
                <set:messages width="100%" cols="1"/>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td height="40" valign="top" class="content1"><table width="100" border="0" cellspacing="0" cellpadding="0"><%--
                        <tr>
                          <td class="buttonexcel"><a href="/cib/DownloadCVS?listName=overdraftAccountList&fileName=overdraft_account&columnTitles=<set:label name='Account_Number'/>,<set:label name='Description_Remark'/>,<set:label name='Original_Currency'/>,<set:label name='Balance'/>&columnNames=ACCOUNT_NO,ACCOUNT_NO||db@accountDesc||specialChar@yes,CURRENCY_CODE||db@currencyCBS,CURRENT_BALANCE||format@amount">Download</a></td>
                        </tr>
                      --%></table></td>
                    <td align="right" valign="top" class="content1"><b>
                      <set:label name="Equivalent_In" defaultvalue="Equivalent amount in: "/>
                      </b>
                      <select name="eqCcy" id="eqCcy" onChange="showEquivalent(this.value);">
                        <set:rblist file="app.cib.resource.enq.equivalent_ccy">
                          <set:rboption name="eqCcy"/>
                        </set:rblist>
                      </select></td>
                  </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="28%" class="label1"><set:label name="Corp_Id" rb="app.cib.resource.bnk.corporation"/></td>
                    <td width="72%" class="content1"><set:data name='corpId'/></td>
                  </tr>
                  <tr>
                    <td class="label1"><set:label name="Corp_Name" rb="app.cib.resource.bnk.corporation"/></td>
                    <td class="content1"><set:data name='corpName'/></td>
                  </tr>
                </table>
                <table border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('sum');">
                            <set:label name="functionTitle_sum_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('ca');">
                            <set:label name="functionTitle_ca_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('sa');">
                            <set:label name="functionTitle_sa_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('td');">
                            <set:label name="functionTitle_td_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_o"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_o"><a href="#" onClick="changeDoc('oa');">
                            <set:label name="functionTitle_oa_CR"/>
                            </a></td>
                          <td class="rtab1_o"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('la');">
                            <set:label name="functionTitle_la_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>

                      <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('cc');">
                            <set:label name="functionTitle_cc_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                      
                    <td><table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td class="ltab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                          <td class="tab1_c"><a href="#" onClick="changeDoc('out');">
                            <set:label name="functionTitle_out_CR"/>
                            </a></td>
                          <td class="rtab1_c"><img src="/cib/images/shim.gif" width="7" height="26"></td>
                        </tr>
                      </table></td>
                  </tr>
                </table>
                <table width="100%" border="0" cellspacing="0" cellpadding="3">
                  <tr>
                    <td align="left" class="listheader1"><set:label name="Account_Number" defaultvalue="Account Number"/></td>
                    <td align="left" class="listheader1"><set:label name="Description_Remark" defaultvalue="Description/Personal Remark"/></td>
                    <%--
                    <td align="center" class="listheader1"><set:label name="Online_Statement" defaultvalue="Online Statement"/></td>
                    --%>
                    <td align="center" class="listheader1"><set:label name="Original_Currency" defaultvalue="Original Currency"/></td>
                    <td align="right" class="listheader1"><set:label name="Balance" defaultvalue="Balance"/></td>
                    <td align="right" class="listheader1"><div name="showEqCcy" id="showEqCcy">
                        <set:label name="Equivalent_In" defaultvalue="Equivalent in "/>
                        &nbsp;
                        <set:data name="eqCcy" defaultvalue="MOP"/>
                      </div></td>
                  </tr>
                  <set:list name="overdraftAccountList" showNoRecord="YES">
                    <set:listif name='ACCOUNT_STATUS' condition='notequals' value='4'>
                      <tr class="<set:listclass class1='' class2='greyline'/>">
                        <td align="left" class="listcontent1"><a onClick="toTargetFormgoToTarget('/cib/accEnquiryBank.do?ActionMethod=viewOverdraftAccount&range=1&accountNo=<set:listdata name='ACCOUNT_NO'/>&from_ccy=<set:listdata name="CURRENCY_CODE" db="currencyCBS"/>')" href="#">
                          <set:listif name='SPECIAL_ACC_FLAG' condition='equals' value='Y'>*</set:listif>
                          <set:listdata name='ACCOUNT_NO'/>
                          </a></td>
                        <td align="left" class="listcontent1"><set:listdata name="ACCOUNT_NO" db="accountDesc"/></td>
                        <%--
                        <td align="center" class="listcontent1"><set:listdata name="ACCOUNT_NO" db="statementForList"/></td>
                        --%>
                        <td align="center" class="listcontent1"><set:listdata name="CURRENCY_CODE" db="rcCurrencyCBS"/>
                          <span class="content1">
                          <input name="fromCcy_<set:listdata name='ACCOUNT_NO'/>" type="hidden" id="fromCcy_<set:listdata name='ACCOUNT_NO'/>" value="<set:listdata name='CURRENCY_CODE' db='currencyCBS'/>">
                          </span></td>
                        <td align="right" class="listcontent1"><set:listdata name="CURRENT_BALANCE" format="amount"/>
                          <input name="fromAmt_<set:listdata name='ACCOUNT_NO'/>" type="hidden" id="fromAmt_<set:listdata name='ACCOUNT_NO'/>" value="<set:listdata name='CURRENT_BALANCE'/>"></td>
                        <td align="right" class="listcontent1"><div id="equ_<set:listdata name='ACCOUNT_NO'/>">
                          <set:listdata name="EQUIVALENT" format="amount"/>
                          <div></td>
                      </tr>
                    </set:listif>
                  </set:list>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td class="groupseperator">&nbsp;</td>
                  </tr>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <set:if name='SPECIAL_ACC_FLAG' condition='equals' value='Y'>
                    <tr>
                      <td class="label1"><set:label name="*_account" defaultvalue="* means special account"/></td>
                    </tr>
                  </set:if>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                  <tr>
                    <td height="40" class="sectionbutton"><input id="ActionMethod" name="ActionMethod" type="hidden" value="listOverdraftAccount"></td>
                    <td height="40" class="sectionbutton"><input id="equivalentCCY" name="equivalentCCY" type="hidden" value="<set:data name='equivalentCCY'/>"></td>
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
